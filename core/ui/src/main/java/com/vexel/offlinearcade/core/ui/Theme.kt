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

// Clean Modern Light Palette
private val LightBackground = Color(0xFFF8FAFC) // Slate 50
private val LightSurface = Color(0xFFFFFFFF) // White
private val LightCard = Color(0xFFF1F5F9) // Slate 100
private val LightAction = Color(0xFF0EA5E9) // Cyan 600
private val LightAccent = Color(0xFF8B5CF6) // Violet 500
private val LightReward = Color(0xFFF59E0B) // Amber 500
private val LightDanger = Color(0xFFEF4444) // Red 500
private val LightSuccess = Color(0xFF10B981) // Emerald 500
private val LightCobalt = Color(0xFF3B82F6) // Blue 500

// Explicit text colors for Light Mode
private val TextPrimary = Color(0xFF0F172A) // Slate 900
private val TextSecondary = Color(0xFF475569) // Slate 600
private val TextMuted = Color(0xFF94A3B8) // Slate 400
private val OutlineColor = Color(0xFFE2E8F0) // Slate 200

@Immutable
data class ArcadeExtendedColors(
    val shellGradient: Brush,
    val panelGradient: Brush,
    val heroGradient: Brush,
    val pulseAccent: Color,
    val laneAccent: Color,
    val stackAccent: Color,
    val reward: Color,
    val success: Color,
    val danger: Color,
    val premium: Color,
    val outlineMuted: Color,
    val glow: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val textInverse: Color,
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
)

val LocalArcadeExtendedColors = staticCompositionLocalOf {
    ArcadeExtendedColors(
        shellGradient = Brush.verticalGradient(listOf(LightBackground, LightSurface)),
        panelGradient = Brush.linearGradient(listOf(LightSurface, LightCard)),
        heroGradient = Brush.linearGradient(listOf(LightAccent, LightAction)),
        pulseAccent = LightAccent,
        laneAccent = LightAction,
        stackAccent = LightCobalt,
        reward = LightReward,
        success = LightSuccess,
        danger = LightDanger,
        premium = LightAccent,
        outlineMuted = OutlineColor,
        glow = LightAction.copy(alpha = 0.08f),
        textPrimary = TextPrimary,
        textSecondary = TextSecondary,
        textMuted = TextMuted,
        textInverse = Color.White,
        background = LightBackground,
        cardBackground = LightSurface,
        elevatedCardBackground = LightCard,
        gameBackground = Color(0xFFF1F5F9),
        gameBoard = Color(0xFFFFFFFF),
        gameBoardRaised = Color(0xFFF8FAFC),
        hudCard = Color(0xFFE2E8F0),
        hudBorder = Color(0xFFCBD5E1),
        controlSurface = Color(0xFFFFFFFF),
        controlBorder = Color(0xFFCBD5E1),
        primaryCyan = Color(0xFF0891B2),
        primaryOnCyan = Color.White,
        accentViolet = Color(0xFF7C3AED),
        dangerCoral = Color(0xFFE11D48),
        pickupMint = Color(0xFF059669),
        overlayScrim = Color.Black.copy(alpha = 0.45f),
        player = Color(0xFF0891B2),
        playerAccent = Color(0xFF0EA5E9),
        collectible = Color(0xFF059669),
        gameBoardInner = Color(0xFFFFFFFF),
        gridLine = Color(0xFFCBD5E1),
    )
}

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

private fun premiumColorScheme(highContrast: Boolean): androidx.compose.material3.ColorScheme {
    return lightColorScheme(
        primary = LightAction,
        onPrimary = Color.White,
        primaryContainer = LightCard,
        onPrimaryContainer = TextPrimary,
        secondary = LightAccent,
        onSecondary = Color.White,
        secondaryContainer = LightSurface,
        onSecondaryContainer = TextPrimary,
        tertiary = LightCobalt,
        onTertiary = Color.White,
        tertiaryContainer = LightSurface,
        onTertiaryContainer = TextPrimary,
        background = LightBackground,
        onBackground = TextPrimary,
        surface = LightSurface,
        onSurface = TextPrimary,
        surfaceVariant = LightCard,
        onSurfaceVariant = if (highContrast) TextPrimary else TextSecondary,
        error = LightDanger,
        onError = Color.White,
        errorContainer = Color(0xFFFEE2E2),
        onErrorContainer = Color(0xFF991B1B),
        outline = if (highContrast) TextSecondary else OutlineColor,
        outlineVariant = OutlineColor,
        scrim = Color.Black.copy(alpha = 0.4f),
    )
}

private fun premiumExtendedColors(reducedEffects: Boolean): ArcadeExtendedColors {
    return ArcadeExtendedColors(
        shellGradient = Brush.verticalGradient(
            colors = if (reducedEffects) listOf(LightBackground, LightBackground) else listOf(Color(0xFFF8FAFC), LightBackground, LightSurface),
        ),
        panelGradient = Brush.linearGradient(
            colors = if (reducedEffects) listOf(LightSurface, LightSurface) else listOf(LightSurface, LightCard),
        ),
        heroGradient = Brush.linearGradient(
            colors = if (reducedEffects) listOf(LightAccent, LightAction) else listOf(LightAccent, LightAction, LightAccent.copy(alpha = 0.8f)),
        ),
        pulseAccent = LightAccent,
        laneAccent = LightAction,
        stackAccent = LightCobalt,
        reward = LightCollectible,
        success = LightSuccess,
        danger = LightHazard,
        premium = LightAccent,
        outlineMuted = OutlineColor,
        glow = if (reducedEffects) Color.Transparent else LightAction.copy(alpha = 0.08f),
        textPrimary = TextPrimary,
        textSecondary = TextSecondary,
        textMuted = TextMuted,
        textInverse = Color.White,
        background = LightBackground,
        cardBackground = LightSurface,
        elevatedCardBackground = LightCard,
        gameBackground = Color(0xFFF1F5F9),
        gameBoard = Color(0xFFFFFFFF),
        gameBoardRaised = Color(0xFFF8FAFC),
        hudCard = Color(0xFFE2E8F0),
        hudBorder = Color(0xFFCBD5E1),
        controlSurface = Color(0xFFFFFFFF),
        controlBorder = Color(0xFFCBD5E1),
        primaryCyan = Color(0xFF0891B2),
        primaryOnCyan = Color.White,
        accentViolet = Color(0xFF7C3AED),
        dangerCoral = Color(0xFFE11D48),
        pickupMint = Color(0xFF059669),
        overlayScrim = Color.Black.copy(alpha = 0.45f),
        player = Color(0xFF0891B2),
        playerAccent = Color(0xFF0EA5E9),
        collectible = Color(0xFF059669),
        gameBoardInner = Color(0xFFFFFFFF),
        gridLine = Color(0xFFCBD5E1),
    )
}

// Fallback colors for compilation
private val LightCollectible = Color(0xFFF59E0B)
private val LightHazard = Color(0xFFEF4444)

@Composable
fun OfflineMiniArcadeTheme(
    themeId: String,
    highContrast: Boolean = false,
    reducedEffects: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = premiumColorScheme(highContrast = highContrast)
    val extendedColors = premiumExtendedColors(reducedEffects = reducedEffects)
    androidx.compose.runtime.CompositionLocalProvider(
        LocalArcadeExtendedColors provides extendedColors,
        LocalArcadeSpacing provides ArcadeSpacing(),
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
}
