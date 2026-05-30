package com.vexel.offlinearcade.core.ui

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// Soft Arcade Light Baseline - Refined
private val LightBackground = Color(0xFF070B1E) // Sleek dark blue
private val LightSurface = Color(0xFF0F172A)    // Slate-900
private val LightSurfaceContainer = Color(0xFF1E293B) // Slate-800
private val LightSurfaceVariant = Color(0xFF334155) // Slate-700

private val LightPrimary = Color(0xFF0EA5E9)    // Ocean blue/cyan
private val LightOnPrimary = Color(0xFFFFFFFF)
private val LightPrimaryContainer = Color(0xFF0369A1)
private val LightOnPrimaryContainer = Color(0xFFE0F2FE)

private val LightSecondary = Color(0xFFD946EF)  // Fuchsia
private val LightOnSecondary = Color(0xFFFFFFFF)
private val LightSecondaryContainer = Color(0xFF701A75)
private val LightOnSecondaryContainer = Color(0xFFFDF4FF)

private val LightTertiary = Color(0xFFF59E0B)   // Amber
private val LightOnTertiary = Color(0xFFFFFFFF)
private val LightTertiaryContainer = Color(0xFF78350F)
private val LightOnTertiaryContainer = Color(0xFFFEF3C7)

private val LightTextPrimary = Color(0xFFF8FAFC) // Slate-50 (white)
private val LightTextSecondary = Color(0xFF94A3B8) // Slate-400
private val LightOutline = Color(0xFF475569) // Slate-600
private val LightOutlineVariant = Color(0xFF334155) // Slate-700

// Gameplay
private val LightGameBackground = Color(0xFF0F172A)
private val LightGameSurface = Color(0xFF1E293B)
private val LightGameTrack = Color(0xFF1E293B)
private val LightGameGrid = Color(0xFF334155)
private val LightGameGuideLine = Color(0xFF475569)
private val LightPlayer = Color(0xFF0EA5E9)
private val LightPlayerAccent = Color(0xFF38BDF8)
private val LightCollectible = Color(0xFFF59E0B)
private val LightCollectibleSoft = Color(0xFF78350F)
private val LightHazard = Color(0xFFEF4444)
private val LightHazardSoft = Color(0xFF7F1D1D)
private val LightSuccessPulse = Color(0xFF10B981)
private val LightScoreText = Color(0xFFF8FAFC)
private val LightMutedText = Color(0xFF94A3B8)

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

private val SoftArcadeLightExtendedColors = ArcadeExtendedColors(
    shellGradient = Brush.verticalGradient(listOf(LightBackground, LightSurfaceContainer)),
    panelGradient = Brush.linearGradient(listOf(LightSurface, LightSurfaceContainer)),
    heroGradient = Brush.linearGradient(listOf(LightSecondary, LightPrimary)),
    pulseAccent = LightSecondary,
    laneAccent = LightPrimary,
    stackAccent = LightTertiary,
    brickVolleyAccent = Color(0xFFE57373),
    loopSnakeAccent = Color(0xFF4CAF50),
    shieldDashAccent = Color(0xFF9C27B0),
    gravityFlipAccent = Color(0xFFFF9800),
    reward = LightCollectible,
    success = LightSuccessPulse,
    danger = LightHazard,
    premium = LightSecondary,
    outlineMuted = LightOutlineVariant,
    glow = LightPrimary.copy(alpha = 0.10f),
    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary,
    textMuted = LightMutedText,
    background = LightBackground,
    cardBackground = LightSurfaceContainer,
    elevatedCardBackground = LightSurface,
    gameBackground = LightGameBackground,
    gameBoard = LightGameTrack,
    gameBoardRaised = LightSurfaceContainer,
    hudCard = LightSurface,
    hudBorder = LightOutlineVariant,
    controlSurface = LightSurface,
    controlBorder = LightOutline,
    primaryCyan = LightPlayer,
    primaryOnCyan = LightOnPrimary,
    accentViolet = LightSecondary,
    dangerCoral = LightHazard,
    pickupMint = LightSuccessPulse,
    overlayScrim = Color.Black.copy(alpha = 0.35f),
    player = LightPlayer,
    playerAccent = LightPlayerAccent,
    collectible = LightCollectible,
    gameBoardInner = LightGameSurface,
    gridLine = LightGameGrid,
    textInverse = Color.White,
)

val LocalArcadeExtendedColors = staticCompositionLocalOf { SoftArcadeLightExtendedColors }
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

private fun getThemeColorScheme(themeId: String, highContrast: Boolean): androidx.compose.material3.ColorScheme {
    // Aligning with user request to explicitly use themeId logic
    val baseBackground = when (themeId) {
        "sunset_shift" -> LightBackground
        "ice_grid" -> LightBackground
        else -> LightBackground
    }

    return lightColorScheme(
        primary = LightPrimary,
        onPrimary = LightOnPrimary,
        primaryContainer = LightPrimaryContainer,
        onPrimaryContainer = LightOnPrimaryContainer,
        secondary = LightSecondary,
        onSecondary = LightOnSecondary,
        secondaryContainer = LightSecondaryContainer,
        onSecondaryContainer = LightOnSecondaryContainer,
        tertiary = LightTertiary,
        onTertiary = LightOnTertiary,
        tertiaryContainer = LightTertiaryContainer,
        onTertiaryContainer = LightOnTertiaryContainer,
        background = baseBackground,
        onBackground = LightTextPrimary,
        surface = LightSurface,
        onSurface = LightTextPrimary,
        surfaceVariant = LightSurfaceVariant,
        onSurfaceVariant = if (highContrast) Color.Black else LightTextSecondary,
        error = LightHazard,
        onError = Color.White,
        outline = if (highContrast) Color.Black else LightOutline,
        outlineVariant = LightOutlineVariant,
        scrim = Color.Black.copy(alpha = 0.35f),
    )
}

private fun getExtendedColors(themeId: String, reducedEffects: Boolean): ArcadeExtendedColors {
    val base = when (themeId) {
        "sunset_shift" -> SoftArcadeLightExtendedColors.copy(
            heroGradient = Brush.linearGradient(listOf(LightSecondary, LightCollectible))
        )
        "ice_grid" -> SoftArcadeLightExtendedColors.copy(
            heroGradient = Brush.linearGradient(listOf(LightPlayerAccent, LightPrimary))
        )
        else -> SoftArcadeLightExtendedColors
    }

    return if (reducedEffects) {
        base.copy(
            shellGradient = Brush.verticalGradient(listOf(LightBackground, LightBackground)),
            panelGradient = Brush.linearGradient(listOf(LightSurface, LightSurface)),
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
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

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
