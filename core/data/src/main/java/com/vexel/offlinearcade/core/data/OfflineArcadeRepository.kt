package com.vexel.offlinearcade.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.vexel.offlinearcade.core.common.ArcadeClock
import com.vexel.offlinearcade.core.common.ArcadeDispatchers
import com.vexel.offlinearcade.core.model.ArcadeSnapshot
import com.vexel.offlinearcade.core.model.DailyChallenge
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.PlayerProfile
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.core.model.SettingsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class OfflineArcadeRepository(
    private val database: ArcadeDatabase,
    private val preferences: DataStore<Preferences>,
    private val clock: ArcadeClock,
    private val dispatchers: ArcadeDispatchers,
) : ArcadeRepository {

    private val dao = database.arcadeDao()

    override val snapshot: Flow<ArcadeSnapshot> =
        combine(
            dao.observeProfile(),
            dao.observeStats(),
            dao.observeThemeUnlocks(),
            dao.observeSkinUnlocks(),
            preferences.settingsFlow(),
            challengesForDay(clock.currentEpochDay()),
        ) { flows ->
            val profileEntity = flows[0] as PlayerProfileEntity?
            val statsEntities = flows[1] as List<GameStatsEntity>
            val themeUnlocks = flows[2] as List<ThemeUnlockEntity>
            val skinUnlocks = flows[3] as List<SkinUnlockEntity>
            val settings = flows[4] as SettingsState
            val challenges = flows[5] as List<DailyChallenge>
            
            val profile = profileEntity?.toModel() ?: PlayerProfile()
            ArcadeSnapshot(
                profile = profile,
                settings = settings,
                stats = GameId.entries.map { gameId ->
                    statsEntities.firstOrNull { it.gameId == gameId.name }?.toModel() ?: GameStats(gameId = gameId)
                },
                themes = mergeThemes(themeUnlocks, profile.premiumUnlocked),
                skins = mergeSkins(skinUnlocks, profile.premiumUnlocked),
                challenges = challenges,
            )
        }.flowOn(dispatchers.io)

    override fun challengesForDay(epochDay: Long): Flow<List<DailyChallenge>> =
        dao.observeChallengeProgress(epochDay).combine(dao.observeProfile()) { progress, _ ->
            mergeChallenges(
                generated = DailyChallengeGenerator.generate(epochDay),
                progress = progress,
            )
        }.flowOn(dispatchers.io)

    override suspend fun updateSettings(transform: (SettingsState) -> SettingsState) {
        withContext(dispatchers.io) {
            val current = preferences.settingsFlow().first()
            preferences.updateSettings(transform(current))
        }
    }

    override suspend fun recordRun(result: RunResult) {
        withContext(dispatchers.io) {
            val epochDay = clock.currentEpochDay()
            val currentProfile = dao.observeProfile().first()?.toModel() ?: PlayerProfile()
            val updatedProfile = updateStreak(currentProfile, epochDay).copy(
                coins = currentProfile.coins + result.coinsEarned,
            )
            dao.upsertProfile(updatedProfile.toEntity())

            val currentStats = dao.getStats(result.gameId.name)?.toModel() ?: GameStats(gameId = result.gameId)
            val updatedStats = currentStats.copy(
                highScore = maxOf(currentStats.highScore, result.score),
                sessionsPlayed = currentStats.sessionsPlayed + 1,
                totalPlayMillis = currentStats.totalPlayMillis + result.durationMillis,
                totalScore = currentStats.totalScore + result.score,
                bestCombo = maxOf(currentStats.bestCombo, result.bestCombo),
                bestLines = maxOf(currentStats.bestLines, result.linesCleared),
            )
            dao.upsertStats(updatedStats.toEntity())

            applyChallengeProgress(epochDay = epochDay, result = result)
        }
    }

    override suspend fun purchaseTheme(themeId: String): Boolean = withContext(dispatchers.io) {
        val profile = dao.observeProfile().first()?.toModel() ?: PlayerProfile()
        val theme = com.vexel.offlinearcade.core.model.ArcadeThemeCatalog.themes.firstOrNull { it.id == themeId }
            ?: return@withContext false
        val alreadyUnlocked = theme.coinCost == 0 || profile.premiumUnlocked ||
            dao.observeThemeUnlocks().first().any { it.themeId == themeId && it.unlocked }
        if (alreadyUnlocked) {
            return@withContext true
        }
        if (profile.coins < theme.coinCost) {
            return@withContext false
        }
        dao.upsertProfile(profile.copy(coins = profile.coins - theme.coinCost).toEntity())
        dao.upsertThemeUnlock(ThemeUnlockEntity(themeId = themeId, unlocked = true))
        true
    }

    override suspend fun selectTheme(themeId: String) {
        withContext(dispatchers.io) {
            val profile = dao.observeProfile().first()?.toModel() ?: PlayerProfile()
            val unlockedThemes = mergeThemes(dao.observeThemeUnlocks().first(), profile.premiumUnlocked)
            if (unlockedThemes.any { it.id == themeId && it.unlocked }) {
                dao.upsertProfile(profile.copy(selectedThemeId = themeId).toEntity())
            }
        }
    }

    override suspend fun purchaseSkin(skinId: String): Boolean = withContext(dispatchers.io) {
        val profile = dao.observeProfile().first()?.toModel() ?: PlayerProfile()
        val skin = com.vexel.offlinearcade.core.model.ArcadeSkinCatalog.skins.firstOrNull { it.id == skinId }
            ?: return@withContext false
        val alreadyUnlocked = skin.coinCost == 0 || profile.premiumUnlocked ||
            dao.observeSkinUnlocks().first().any { it.skinId == skinId && it.unlocked }
        if (alreadyUnlocked) {
            return@withContext true
        }
        if (profile.coins < skin.coinCost) {
            return@withContext false
        }
        dao.upsertProfile(profile.copy(coins = profile.coins - skin.coinCost).toEntity())
        dao.upsertSkinUnlock(SkinUnlockEntity(skinId = skinId, unlocked = true))
        true
    }

    override suspend fun selectSkin(skinId: String, gameId: GameId) {
        withContext(dispatchers.io) {
            val profile = dao.observeProfile().first()?.toModel() ?: PlayerProfile()
            val unlockedSkins = mergeSkins(dao.observeSkinUnlocks().first(), profile.premiumUnlocked)
            if (unlockedSkins.any { it.id == skinId && it.unlocked && it.gameId == gameId }) {
                val updatedProfile = when (gameId) {
                    GameId.PULSE_ORBIT -> profile.copy(selectedPulseOrbitSkin = skinId)
                    else -> profile // Extend here as more games get skins
                }
                dao.upsertProfile(updatedProfile.toEntity())
            }
        }
    }

    override suspend fun setPremiumUnlocked(unlocked: Boolean) {
        withContext(dispatchers.io) {
            val profile = dao.observeProfile().first()?.toModel() ?: PlayerProfile()
            dao.upsertProfile(profile.copy(premiumUnlocked = unlocked).toEntity())
        }
    }

    private suspend fun applyChallengeProgress(epochDay: Long, result: RunResult) {
        val generated = DailyChallengeGenerator.generate(epochDay)
        val existingProgress = dao.observeChallengeProgress(epochDay).first().associateBy { it.challengeId }
        var rewardDelta = 0
        generated.forEach { challenge ->
            val previous = existingProgress[challenge.challengeId]
            val added = metricProgress(challenge.metric, result)
            if (added <= 0 && previous == null) {
                return@forEach
            }
            val newProgress = minOf(challenge.targetValue, (previous?.progress ?: 0) + added)
            val completed = newProgress >= challenge.targetValue
            val rewardClaimed = previous?.rewardClaimed == true || (completed && previous?.rewardClaimed != true)
            if (completed && previous?.rewardClaimed != true) {
                rewardDelta += challenge.rewardCoins
            }
            dao.upsertChallengeProgress(
                ChallengeProgressEntity(
                    challengeId = challenge.challengeId,
                    epochDay = epochDay,
                    progress = newProgress,
                    completed = completed,
                    rewardClaimed = rewardClaimed,
                ),
            )
        }
        if (rewardDelta > 0) {
            val profile = dao.observeProfile().first()?.toModel() ?: PlayerProfile()
            dao.upsertProfile(profile.copy(coins = profile.coins + rewardDelta).toEntity())
        }
    }

    private fun updateStreak(profile: PlayerProfile, epochDay: Long): PlayerProfile = when (profile.lastPlayedEpochDay) {
        null -> profile.copy(currentStreakDays = 1, lastPlayedEpochDay = epochDay)
        epochDay -> profile
        epochDay - 1 -> profile.copy(currentStreakDays = profile.currentStreakDays + 1, lastPlayedEpochDay = epochDay)
        else -> profile.copy(currentStreakDays = 1, lastPlayedEpochDay = epochDay)
    }
}
