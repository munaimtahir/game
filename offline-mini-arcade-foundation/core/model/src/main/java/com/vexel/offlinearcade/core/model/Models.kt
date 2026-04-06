package com.vexel.offlinearcade.core.model

enum class GameId { PULSE_ORBIT, LANE_DRIFT, STACK_DROP }

data class PlayerProfile(
    val coins: Int = 0,
    val premiumUnlocked: Boolean = false,
    val selectedThemeId: String = "default",
    val currentStreakDays: Int = 0,
    val lastPlayedEpochDay: Long? = null,
)

data class GameStats(
    val gameId: GameId,
    val highScore: Int = 0,
    val sessionsPlayed: Int = 0,
    val totalPlayMillis: Long = 0,
    val bestCombo: Int = 0,
)

data class ThemeUnlock(
    val id: String,
    val title: String,
    val coinCost: Int,
    val premiumOnly: Boolean = false,
    val unlocked: Boolean = false,
)

data class DailyChallenge(
    val challengeId: String,
    val epochDay: Long,
    val gameId: GameId?,
    val description: String,
    val targetValue: Int,
    val rewardCoins: Int,
    val progress: Int = 0,
    val completed: Boolean = false,
)
