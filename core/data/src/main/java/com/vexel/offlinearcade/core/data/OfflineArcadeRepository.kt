package com.vexel.offlinearcade.core.data

import com.vexel.offlinearcade.core.common.ArcadeDispatchers
import com.vexel.offlinearcade.core.common.LocalDayService
import com.vexel.offlinearcade.core.model.ArcadeSnapshot
import com.vexel.offlinearcade.core.model.DailyChallenge
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.PlayerProfile
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.core.model.SettingsState
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class OfflineArcadeRepository(
    private val database: ArcadeDatabase,
    private val preferences: SettingsStore,
    private val localDayService: LocalDayService,
    private val dispatchers: ArcadeDispatchers,
) : ArcadeRepository {

    private val dao = database.arcadeDao()

    override val snapshot: Flow<ArcadeSnapshot> =
        combine(
            dao.observeProfile(),
            dao.observeStats(),
            dao.observeThemeUnlocks(),
            dao.observeSkinUnlocks(),
            preferences.settings,
            challengesForDay(localDayService.currentDay().epochDay),
        ) { flows ->
            val profileEntity = flows[0] as PlayerProfileEntity?
            val statsEntities = flows[1] as List<GameStatsEntity>
            val themeUnlocks = flows[2] as List<ThemeUnlockEntity>
            val skinUnlocks = flows[3] as List<SkinUnlockEntity>
            val settings = flows[4] as SettingsState
            val challenges = flows[5] as List<DailyChallenge>
            
            val profile = profileEntity?.toModel() ?: PlayerProfile()
            val stats = GameId.entries.map { gameId ->
                statsEntities.firstOrNull { it.gameId == gameId.name }?.toModel() ?: GameStats(gameId = gameId)
            }
            ArcadeSnapshot(
                profile = profile,
                settings = settings,
                stats = stats,
                themes = mergeThemes(themeUnlocks, profile.premiumUnlocked),
                skins = mergeSkins(skinUnlocks, profile.premiumUnlocked),
                challenges = challenges,
                achievements = AchievementCatalog.progressFor(stats, profile),
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
            preferences.updateSettings(transform)
        }
    }

    override suspend fun recordRun(result: RunResult) {
        withContext(dispatchers.io) {
            val sessionId = normalizedSessionId(result)
            if (dao.getRunRecord(sessionId) != null) {
                return@withContext
            }
            val epochDay = localDayService.currentDay().epochDay
            database.withTransaction {
                if (dao.getRunRecord(sessionId) != null) {
                    return@withTransaction
                }
                dao.insertRunRecord(
                    RunRecordEntity(
                        sessionId = sessionId,
                        gameId = result.gameId.name,
                        score = result.score,
                        coinsEarned = result.coinsEarned,
                        finishedAtEpochMillis = result.finishedAtEpochMillis,
                    ),
                )

                val currentProfile = dao.observeProfile().first()?.toModel() ?: PlayerProfile()
                val updatedProfile = updateStreak(currentProfile, epochDay).copy(
                    coins = currentProfile.coins + result.coinsEarned,
                )
                dao.upsertProfile(updatedProfile.toEntity())

                val currentStats = dao.getStats(result.gameId.name)?.toModel() ?: GameStats(gameId = result.gameId)
                val updatedStats = currentStats.copy(
                    highScore = maxOf(currentStats.highScore, result.score),
                    sessionsPlayed = currentStats.sessionsPlayed + 1,
                    completedRuns = currentStats.completedRuns + 1,
                    lastPlayedAtEpochMillis = maxOf(currentStats.lastPlayedAtEpochMillis, result.finishedAtEpochMillis),
                    totalPlayMillis = currentStats.totalPlayMillis + result.durationMillis,
                    totalScore = currentStats.totalScore + result.score,
                    totalPickups = currentStats.totalPickups + result.pickupsCollected,
                    totalLinesCleared = currentStats.totalLinesCleared + result.linesCleared,
                    bestCombo = maxOf(currentStats.bestCombo, result.bestCombo),
                    bestLines = maxOf(currentStats.bestLines, result.linesCleared),
                    totalPasses = currentStats.totalPasses + result.totalPasses,
                    totalPerfectPasses = currentStats.totalPerfectPasses + result.perfectPasses,
                )
                dao.upsertStats(updatedStats.toEntity())

                applyChallengeProgress(epochDay = epochDay, result = result)
            }
        }
    }

    override suspend fun markTutorialSeen(gameId: GameId) {
        withContext(dispatchers.io) {
            val profile = dao.observeProfile().first()?.toModel() ?: PlayerProfile()
            val updated = when (gameId) {
                GameId.PULSE_ORBIT -> profile.copy(tutorialSeenPulseOrbit = true)
                GameId.LANE_DRIFT -> profile.copy(tutorialSeenLaneDrift = true)
                GameId.STACK_DROP -> profile.copy(tutorialSeenStackDrop = true)
            }
            dao.upsertProfile(updated.toEntity())
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
                    GameId.LANE_DRIFT -> profile.copy(selectedLaneDriftSkin = skinId)
                    GameId.STACK_DROP -> profile.copy(selectedStackDropSkin = skinId)
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

    override suspend fun addCoins(amount: Int): Boolean {
        if (amount <= 0) return false
        return withContext(dispatchers.io) {
            database.withTransaction {
                val profile = dao.observeProfile().first()?.toModel() ?: PlayerProfile()
                dao.upsertProfile(profile.copy(coins = profile.coins + amount).toEntity())
                true
            }
        }
    }

    private suspend fun applyChallengeProgress(epochDay: Long, result: RunResult) {
        val generated = DailyChallengeGenerator.generate(epochDay)
        val existingProgress = dao.observeChallengeProgress(epochDay).first().associateBy { it.challengeId }
        var rewardDelta = 0
        var completedDelta = 0
        generated.filter { it.gameId != null }.forEach { challenge ->
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
                completedDelta += 1
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
        val refreshedProgress = dao.observeChallengeProgress(epochDay).first().associateBy { it.challengeId }
        val completedGameChallenges = generated.count { challenge ->
            challenge.gameId != null && refreshedProgress[challenge.challengeId]?.completed == true
        }
        generated.firstOrNull { it.gameId == null }?.let { bundle ->
            val previous = existingProgress[bundle.challengeId]
            val newProgress = minOf(bundle.targetValue, completedGameChallenges)
            val completed = newProgress >= bundle.targetValue
            val rewardClaimed = previous?.rewardClaimed == true || (completed && previous?.rewardClaimed != true)
            if (completed && previous?.rewardClaimed != true) {
                rewardDelta += bundle.rewardCoins
                completedDelta += 1
            }
            dao.upsertChallengeProgress(
                ChallengeProgressEntity(
                    challengeId = bundle.challengeId,
                    epochDay = epochDay,
                    progress = newProgress,
                    completed = completed,
                    rewardClaimed = rewardClaimed,
                ),
            )
        }
        if (rewardDelta > 0 || completedDelta > 0) {
            val profile = dao.observeProfile().first()?.toModel() ?: PlayerProfile()
            dao.upsertProfile(
                profile.copy(
                    coins = profile.coins + rewardDelta,
                    completedDailyChallenges = profile.completedDailyChallenges + completedDelta,
                ).toEntity(),
            )
        }
    }

    private fun updateStreak(profile: PlayerProfile, epochDay: Long): PlayerProfile = when (profile.lastPlayedEpochDay) {
        null -> profile.copy(currentStreakDays = 1, bestStreakDays = maxOf(profile.bestStreakDays, 1), lastPlayedEpochDay = epochDay)
        epochDay -> profile
        epochDay - 1 -> {
            val nextStreak = profile.currentStreakDays + 1
            profile.copy(currentStreakDays = nextStreak, bestStreakDays = maxOf(profile.bestStreakDays, nextStreak), lastPlayedEpochDay = epochDay)
        }
        else -> profile.copy(currentStreakDays = 1, bestStreakDays = maxOf(profile.bestStreakDays, 1), lastPlayedEpochDay = epochDay)
    }

    private fun normalizedSessionId(result: RunResult): String {
        if (result.sessionId.isNotBlank()) {
            return result.sessionId
        }
        return buildString {
            append(result.gameId.name)
            append(':')
            append(result.startedAtEpochMillis)
            append(':')
            append(result.finishedAtEpochMillis)
            append(':')
            append(result.score)
        }
    }
}
