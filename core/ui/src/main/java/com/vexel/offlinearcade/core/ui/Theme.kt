package com.vexel.offlinearcade.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ────────────────────────────────────────────────────────────
// Daylight Arcade — Global Palette Tokens
// ────────────────────────────────────────────────────────────

// Backgrounds & surfaces
private val DayBackground        = Color(0xFFF8FAFF) // Sky white
private val DaySurface           = Color(0xFFFFFFFF) // Pure white
private val DaySurfaceVariant    = Color(0xFFEAF4FF) // Soft blue
private val DaySurfaceContainer  = Color(0xFFDCE6F2) // Pale blue / border

// Text
private val DayTextPrimary       = Color(0xFF14213D) // Deep navy
private val DayTextSecondary     = Color(0xFF526173) // Slate blue
private val DayTextMuted         = Color(0xFF8B9BB4) // Steel

// Primary action (Arcade Blue)
private val DayPrimary           = Color(0xFF2F80ED)
private val DayOnPrimary         = Color(0xFFFFFFFF)
private val DayPrimaryContainer  = Color(0xFF1565C0)
private val DayOnPrimaryContainer= Color(0xFFE8F1FF)

// Secondary accent (Energy Violet)
private val DaySecondary         = Color(0xFF8E7CFF)
private val DayOnSecondary       = Color(0xFFFFFFFF)
private val DaySecondaryContainer= Color(0xFFE8E4FF)
private val DayOnSecondaryContainer = Color(0xFF2E0082)

// Tertiary / reward (Arcade Gold)
private val DayTertiary          = Color(0xFFFFB703)
private val DayOnTertiary        = Color(0xFF14213D)
private val DayTertiaryContainer = Color(0xFFFFF3C4)
private val DayOnTertiaryContainer = Color(0xFF3D2800)

// Semantic
private val DayDanger            = Color(0xFFEF476F) // Coral red
private val DaySuccess           = Color(0xFF20C997) // Mint teal
private val DayOutline           = Color(0xFFDCE6F2) // Cloud border
private val DayOutlineVariant    = Color(0xFFA8C7FA) // Soft blue outline

// ────────────────────────────────────────────────────────────
// Gameplay tokens
// ────────────────────────────────────────────────────────────

// Shared game board
private val DayGameBackground    = Color(0xFFF4FAFF) // Light sky backdrop
private val DayGameSurface       = Color(0xFFEAF4FF) // Game canvas / board bg
private val DayGameTrack         = Color(0xFFEAF4FF) // Lane track base
private val DayGameGrid          = Color(0xFFC8DAEA) // Grid separators
private val DayGuideLine         = Color(0xFFA8C7FA) // Subtle guide lines

// Player / collectibles / hazard
private val DayPlayer            = Color(0xFF2F80ED) // Arcade blue car
private val DayPlayerAccent      = Color(0xFF60A5FA) // Lighter blue canopy
private val DayCollectible       = Color(0xFFFFB703) // Gold coin/star
private val DayCollectibleSoft   = Color(0xFFFFF3C4) // Soft gold bg
private val DayHazard            = Color(0xFFEF476F) // Coral red hazard
private val DayHazardSoft        = Color(0xFFFFE0E8) // Soft coral bg
private val DaySuccessPulse      = Color(0xFF20C997) // Teal success
private val DayScoreText         = Color(0xFF14213D) // Dark on light board

@Immutable
data class ArcadeExtendedColors(
    val shellGradient: Brush,
    val panelGradient: Brush,
    val heroGradient: Brush,
    val pulseAccent: Color,
    val laneAccent: Color,
    val stackAccent: Color,
    val brickVolleyAccent: Color,
    val loopSnakeAccent: Color,
    val shieldDashAccent: Color,
    val gravityFlipAccent: Color,
    val reward: Color,
    val success: Color,
    val danger: Color,
    val rewardContainer: Color,
    val successContainer: Color,
    val dangerContainer: Color,
    val newBestContainer: Color,
    val readyContainer: Color,
    val premium: Color,
    val outlineMuted: Color,
    val glow: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val background: Color,
    val cardBackground: Color,
    val elevatedCardBackground: Color,
    // Gameplay tokens
    val gameBackground: Color,
    val gameBoard: Color,
    val gameBoardRaised: Color,
    val hudCard: Color,
    val hudBorder: Color,
    val controlSurface: Color,
    val controlBorder: Color,
    val primaryCyan: Color,
    val primaryOnCyan: Color,
    val accentViolet: Color,
    val dangerCoral: Color,
    val pickupMint: Color,
    val overlayScrim: Color,
    // Compatibility aliases
    val player: Color,
    val playerAccent: Color,
    val collectible: Color,
    val gameBoardInner: Color,
    val gridLine: Color,
    val textInverse: Color,
)

// ────────────────────────────────────────────────────────────
// Daylight Arcade — Extended Color Set
// ────────────────────────────────────────────────────────────

private val DaylightArcadeExtendedColors = ArcadeExtendedColors(
    shellGradient           = Brush.verticalGradient(listOf(DayBackground, DaySurfaceVariant)),
    panelGradient           = Brush.linearGradient(listOf(DaySurface, DaySurfaceVariant)),
    heroGradient            = Brush.linearGradient(listOf(DayPrimary, DaySecondary)),
    pulseAccent             = DaySecondary,
    laneAccent              = DayPrimary,
    stackAccent             = DayTertiary,
    brickVolleyAccent       = Color(0xFFF97316), // Orange
    loopSnakeAccent         = Color(0xFF20C997), // Teal
    shieldDashAccent        = Color(0xFF8E7CFF), // Violet
    gravityFlipAccent       = Color(0xFFFFB703), // Gold
    reward                  = DayCollectible,
    success                 = DaySuccessPulse,
    danger                  = DayHazard,
    rewardContainer         = DayTertiaryContainer,
    successContainer        = Color(0xFFE6FBF5),
    dangerContainer         = Color(0xFFFFE7EE),
    newBestContainer        = Color(0xFFF0E9FF),
    readyContainer          = DaySurfaceVariant,
    premium                 = DaySecondary,
    outlineMuted            = DayOutline,
    glow                    = DayPrimary.copy(alpha = 0.12f),
    textPrimary             = DayTextPrimary,
    textSecondary           = DayTextSecondary,
    textMuted               = DayTextMuted,
    background              = DayBackground,
    cardBackground          = DaySurfaceVariant,
    elevatedCardBackground  = DaySurface,
    gameBackground          = DayGameBackground,
    gameBoard               = DayGameSurface,
    gameBoardRaised         = DaySurface,
    hudCard                 = DaySurface,
    hudBorder               = DayOutline,
    controlSurface          = DaySurface,
    controlBorder           = DayOutlineVariant,
    primaryCyan             = DayPlayer,
    primaryOnCyan           = DayOnPrimary,
    accentViolet            = DaySecondary,
    dangerCoral             = DayHazard,
    pickupMint              = DaySuccessPulse,
    overlayScrim            = Color.Black.copy(alpha = 0.22f),
    player                  = DayPlayer,
    playerAccent            = DayPlayerAccent,
    collectible             = DayCollectible,
    gameBoardInner          = Color(0xFFF0F6FF), // Misty empty cell
    gridLine                = DayGameGrid,
    textInverse             = DayTextPrimary, // On light boards: dark text/outlines
)

val LocalArcadeExtendedColors = staticCompositionLocalOf { DaylightArcadeExtendedColors }
val LocalArcadeReducedEffects = staticCompositionLocalOf { false }

@Immutable
data class ArcadeSpacing(
    val xs: androidx.compose.ui.unit.Dp = 6.dp,
    val sm: androidx.compose.ui.unit.Dp = 10.dp,
    val md: androidx.compose.ui.unit.Dp = 16.dp,
    val lg: androidx.compose.ui.unit.Dp = 20.dp,
    val xl: androidx.compose.ui.unit.Dp = 28.dp,
    val xxl: androidx.compose.ui.unit.Dp = 36.dp,
)

val LocalArcadeSpacing = staticCompositionLocalOf { ArcadeSpacing() }

private val PremiumTypography = Typography(
    displayLarge = TextStyle(fontSize = 36.sp, lineHeight = 40.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.5).sp),
    displayMedium = TextStyle(fontSize = 30.sp, lineHeight = 34.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.3).sp),
    headlineLarge = TextStyle(fontSize = 26.sp, lineHeight = 30.sp, fontWeight = FontWeight.Bold),
    headlineMedium = TextStyle(fontSize = 22.sp, lineHeight = 26.sp, fontWeight = FontWeight.Bold),
    titleLarge = TextStyle(fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold),
    labelMedium = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.3.sp),
)

@Suppress("UNUSED_PARAMETER")
private fun getThemeColorScheme(themeId: String, highContrast: Boolean): androidx.compose.material3.ColorScheme {
    return lightColorScheme(
        primary             = DayPrimary,
        onPrimary           = DayOnPrimary,
        primaryContainer    = DayPrimaryContainer,
        onPrimaryContainer  = DayOnPrimaryContainer,
        secondary           = DaySecondary,
        onSecondary         = DayOnSecondary,
        secondaryContainer  = DaySecondaryContainer,
        onSecondaryContainer= DayOnSecondaryContainer,
        tertiary            = DayTertiary,
        onTertiary          = DayOnTertiary,
        tertiaryContainer   = DayTertiaryContainer,
        onTertiaryContainer = DayOnTertiaryContainer,
        background          = DayBackground,
        onBackground        = DayTextPrimary,
        surface             = DaySurface,
        onSurface           = DayTextPrimary,
        surfaceVariant      = DaySurfaceVariant,
        onSurfaceVariant    = if (highContrast) DayTextPrimary else DayTextSecondary,
        error               = DayHazard,
        onError             = Color.White,
        outline             = if (highContrast) DayTextPrimary else DayOutline,
        outlineVariant      = DayOutlineVariant,
        scrim               = Color.Black.copy(alpha = 0.25f),
    )
}

private fun getExtendedColors(themeId: String, reducedEffects: Boolean): ArcadeExtendedColors {
    val base = when (themeId) {
        "sunset_shift" -> DaylightArcadeExtendedColors.copy(
            heroGradient = Brush.linearGradient(listOf(Color(0xFFF97316), DayCollectible))
        )
        "ice_grid" -> DaylightArcadeExtendedColors.copy(
            heroGradient = Brush.linearGradient(listOf(DayPlayerAccent, DayPrimary))
        )
        else -> DaylightArcadeExtendedColors
    }

    return if (reducedEffects) {
        base.copy(
            shellGradient = Brush.verticalGradient(listOf(DayBackground, DayBackground)),
            panelGradient = Brush.linearGradient(listOf(DaySurface, DaySurface)),
            glow = Color.Transparent
        )
    } else {
        base
    }
}

@Composable
fun OfflineMiniArcadeTheme(
    themeId: String,
    highContrast: Boolean = false,
    reducedEffects: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = getThemeColorScheme(themeId, highContrast)
    val extendedColors = getExtendedColors(themeId, reducedEffects)

    androidx.compose.runtime.CompositionLocalProvider(
        LocalArcadeExtendedColors provides extendedColors,
        LocalArcadeSpacing provides ArcadeSpacing(),
        LocalArcadeReducedEffects provides reducedEffects,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = PremiumTypography,
            content = content,
        )
    }
}

object ArcadeTheme {
    val colors: ArcadeExtendedColors
        @Composable get() = LocalArcadeExtendedColors.current

    val spacing: ArcadeSpacing
        @Composable get() = LocalArcadeSpacing.current

    val reducedEffects: Boolean
        @Composable get() = LocalArcadeReducedEffects.current
}
