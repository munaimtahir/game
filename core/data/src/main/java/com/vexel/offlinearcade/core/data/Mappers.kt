package com.vexel.offlinearcade.core.data

import com.vexel.offlinearcade.core.model.ArcadeThemeCatalog
import com.vexel.offlinearcade.core.model.ChallengeMetric
import com.vexel.offlinearcade.core.model.DailyChallenge
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.PlayerProfile
import com.vexel.offlinearcade.core.model.ThemeUnlock

internal fun PlayerProfileEntity.toModel(): PlayerProfile = PlayerProfile(
    coins = coins,
    premiumUnlocked = premiumUnlocked,
    selectedThemeId = selectedThemeId,
    selectedPulseOrbitSkin = selectedPulseOrbitSkin,
    selectedGravityFlipSkin = selectedGravityFlipSkin,
    currentStreakDays = currentStreakDays,
    lastPlayedEpochDay = lastPlayedEpochDay,
)

internal fun PlayerProfile.toEntity(): PlayerProfileEntity = PlayerProfileEntity(
    coins = coins,
    premiumUnlocked = premiumUnlocked,
    selectedThemeId = selectedThemeId,
    selectedPulseOrbitSkin = selectedPulseOrbitSkin,
    selectedGravityFlipSkin = selectedGravityFlipSkin,
    currentStreakDays = currentStreakDays,
    lastPlayedEpochDay = lastPlayedEpochDay,
)

internal fun GameStatsEntity.toModel(): GameStats = GameStats(
    gameId = GameId.valueOf(gameId),
    highScore = highScore,
    sessionsPlayed = sessionsPlayed,
    totalPlayMillis = totalPlayMillis,
    totalScore = totalScore,
    bestCombo = bestCombo,
    bestLines = bestLines,
)

internal fun GameStats.toEntity(): GameStatsEntity = GameStatsEntity(
    gameId = gameId.name,
    highScore = highScore,
    sessionsPlayed = sessionsPlayed,
    totalPlayMillis = totalPlayMillis,
    totalScore = totalScore,
    bestCombo = bestCombo,
    bestLines = bestLines,
)

internal fun mergeThemes(unlocks: List<ThemeUnlockEntity>, premiumUnlocked: Boolean): List<ThemeUnlock> {
    val unlockMap = unlocks.associate { it.themeId to it.unlocked }
    return ArcadeThemeCatalog.themes.map { theme ->
        ThemeUnlock(
            id = theme.id,
            title = theme.title,
            subtitle = theme.subtitle,
            coinCost = theme.coinCost,
            premiumOnly = theme.premiumOnly,
            unlocked = theme.coinCost == 0 || premiumUnlocked || unlockMap[theme.id] == true,
        )
    }
}

internal fun mergeSkins(unlocks: List<SkinUnlockEntity>, premiumUnlocked: Boolean): List<com.vexel.offlinearcade.core.model.SkinUnlock> {
    val unlockMap = unlocks.associate { it.skinId to it.unlocked }
    return com.vexel.offlinearcade.core.model.ArcadeSkinCatalog.skins.map { skin ->
        com.vexel.offlinearcade.core.model.SkinUnlock(
            id = skin.id,
            gameId = skin.gameId,
            title = skin.title,
            coinCost = skin.coinCost,
            unlocked = skin.coinCost == 0 || premiumUnlocked || unlockMap[skin.id] == true,
        )
    }
}

internal fun mergeChallenges(
    generated: List<DailyChallenge>,
    progress: List<ChallengeProgressEntity>,
): List<DailyChallenge> {
    val progressMap = progress.associateBy { it.challengeId }
    return generated.map { challenge ->
        val current = progressMap[challenge.challengeId]
        challenge.copy(
            progress = current?.progress ?: 0,
            completed = current?.completed ?: false,
            rewardClaimed = current?.rewardClaimed ?: false,
        )
    }
}

internal fun metricProgress(metric: ChallengeMetric, runResult: com.vexel.offlinearcade.core.model.RunResult): Int =
    when (metric) {
        ChallengeMetric.PULSE_SCORE -> if (runResult.gameId == GameId.PULSE_ORBIT) runResult.score else 0
        ChallengeMetric.LANE_PICKUPS -> if (runResult.gameId == GameId.LANE_DRIFT) runResult.pickupsCollected else 0
        ChallengeMetric.STACK_LINES -> if (runResult.gameId == GameId.STACK_DROP) runResult.linesCleared else 0
        ChallengeMetric.ARCADE_RUNS -> 1
    }
