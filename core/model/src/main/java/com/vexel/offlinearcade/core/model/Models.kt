package com.vexel.offlinearcade.core.model

enum class GameId(val title: String) {
    PULSE_ORBIT("Pulse Orbit"),
    LANE_DRIFT("Lane Drift"),
    STACK_DROP("Stack Drop"),
    BRICK_VOLLEY("Brick Volley"),
    LOOP_SNAKE("Loop Snake"),
    SHIELD_DASH("Shield Dash"),
    GRAVITY_FLIP("Gravity Flip"),
}

data class PlayerProfile(
    val coins: Int = 0,
    val premiumUnlocked: Boolean = false,
    val selectedThemeId: String = ArcadeThemeCatalog.defaultTheme.id,
    val selectedPulseOrbitSkin: String = ArcadeSkinCatalog.defaultPulseOrbitSkin.id,
    val selectedGravityFlipSkin: String = ArcadeSkinCatalog.defaultGravityFlipSkin.id,
    val currentStreakDays: Int = 0,
    val lastPlayedEpochDay: Long? = null,
)

data class SkinDefinition(
    val id: String,
    val gameId: GameId,
    val title: String,
    val coinCost: Int,
)

object ArcadeSkinCatalog {
    val defaultPulseOrbitSkin = SkinDefinition("po_default", GameId.PULSE_ORBIT, "Default Core", 0)
    val defaultGravityFlipSkin = SkinDefinition("gf_default", GameId.GRAVITY_FLIP, "Default Ship", 0)

    val skins = listOf(
        defaultPulseOrbitSkin,
        SkinDefinition("po_gold", GameId.PULSE_ORBIT, "Golden Core", 250),
        SkinDefinition("po_neon", GameId.PULSE_ORBIT, "Neon Pink Core", 400),
        defaultGravityFlipSkin,
        SkinDefinition("gf_fighter", GameId.GRAVITY_FLIP, "Fighter Jet", 300),
        SkinDefinition("gf_saucer", GameId.GRAVITY_FLIP, "UFO Saucer", 500)
    )
}

data class SkinUnlock(
    val id: String,
    val gameId: GameId,
    val title: String,
    val coinCost: Int,
    val unlocked: Boolean = false,
)

data class GameStats(
    val gameId: GameId,
    val highScore: Int = 0,
    val sessionsPlayed: Int = 0,
    val totalPlayMillis: Long = 0,
    val totalScore: Int = 0,
    val bestCombo: Int = 0,
    val bestLines: Int = 0,
)

data class ThemeUnlock(
    val id: String,
    val title: String,
    val subtitle: String,
    val coinCost: Int,
    val premiumOnly: Boolean = false,
    val unlocked: Boolean = false,
)

data class SettingsState(
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val reducedEffects: Boolean = false,
    val highContrastEnabled: Boolean = false,
)

enum class ChallengeMetric {
    PULSE_SCORE,
    LANE_PICKUPS,
    STACK_LINES,
    ARCADE_RUNS,
}

data class DailyChallenge(
    val challengeId: String,
    val epochDay: Long,
    val gameId: GameId?,
    val title: String,
    val description: String,
    val metric: ChallengeMetric,
    val targetValue: Int,
    val rewardCoins: Int,
    val progress: Int = 0,
    val completed: Boolean = false,
    val rewardClaimed: Boolean = false,
)

data class RunResult(
    val gameId: GameId,
    val score: Int,
    val durationMillis: Long,
    val bestCombo: Int = 0,
    val pickupsCollected: Int = 0,
    val linesCleared: Int = 0,
    val coinsEarned: Int = 0,
)

data class ArcadeSnapshot(
    val profile: PlayerProfile = PlayerProfile(),
    val settings: SettingsState = SettingsState(),
    val stats: List<GameStats> = GameId.entries.map { GameStats(gameId = it) },
    val themes: List<ThemeUnlock> = ArcadeThemeCatalog.themes.map { theme ->
        ThemeUnlock(
            id = theme.id,
            title = theme.title,
            subtitle = theme.subtitle,
            coinCost = theme.coinCost,
            premiumOnly = theme.premiumOnly,
            unlocked = theme.coinCost == 0,
        )
    },
    val skins: List<SkinUnlock> = ArcadeSkinCatalog.skins.map { skin ->
        SkinUnlock(
            id = skin.id,
            gameId = skin.gameId,
            title = skin.title,
            coinCost = skin.coinCost,
            unlocked = skin.coinCost == 0,
        )
    },
    val challenges: List<DailyChallenge> = emptyList(),
) {
    val statsByGame: Map<GameId, GameStats> = stats.associateBy { it.gameId }
}

data class ThemeDefinition(
    val id: String,
    val title: String,
    val subtitle: String,
    val coinCost: Int,
    val premiumOnly: Boolean,
)

object ArcadeThemeCatalog {
    val defaultTheme = ThemeDefinition(
        id = "default",
        title = "Soft Arcade Light",
        subtitle = "Relaxed light arcade palette.",
        coinCost = 0,
        premiumOnly = false,
    )

    val themes = listOf(
        defaultTheme,
        ThemeDefinition(
            id = "sunset_shift",
            title = "Sunset Shift",
            subtitle = "Warmer reward highlights on the same calm light foundation.",
            coinCost = 120,
            premiumOnly = false,
        ),
        ThemeDefinition(
            id = "ice_grid",
            title = "Ice Grid",
            subtitle = "Crisper cyan-blue highlights for a colder, cleaner light arcade feel.",
            coinCost = 160,
            premiumOnly = false,
        ),
    )
}
