package com.vexel.offlinearcade.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Premium Midnight Arcade Palette
private val PremiumBackground = Color(0xFF07090E) // Very deep navy / near black
private val PremiumSurface = Color(0xFF131A2A) // Main surface
private val PremiumCard = Color(0xFF1F293F) // Elevated card
private val PremiumAction = Color(0xFF00E5FF) // Electric cyan
private val PremiumAccent = Color(0xFFB140FF) // Soft magenta / violet
private val PremiumReward = Color(0xFFFFB800) // Warm amber / gold
private val PremiumDanger = Color(0xFFFF3D71) // Coral / red
private val PremiumSuccess = Color(0xFF00E676) // Mint / green-cyan
private val PremiumCobalt = Color(0xFF2979FF) // Cobalt for Stack Drop

// Explicit text colors
private val TextPrimary = Color(0xFFF8FAFC) // Near-white
private val TextSecondary = Color(0xFFCBD5E1) // Cool light gray
private val TextMuted = Color(0xFF94A3B8) // Slate gray
private val OutlineColor = Color(0xFF334155) // Slate gray for borders
private val DisabledSurface = Color(0xFF1E293B)
private val DisabledText = Color(0xFF64748B)

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
    val background: Color,
    val cardBackground: Color,
    val elevatedCardBackground: Color,
)

val LocalArcadeExtendedColors = staticCompositionLocalOf {
    ArcadeExtendedColors(
        shellGradient = Brush.verticalGradient(listOf(PremiumBackground, PremiumSurface)),
        panelGradient = Brush.linearGradient(listOf(PremiumSurface, PremiumCard)),
        heroGradient = Brush.linearGradient(listOf(PremiumAccent, PremiumAction)),
        pulseAccent = PremiumAccent,
        laneAccent = PremiumAction,
        stackAccent = PremiumCobalt,
        reward = PremiumReward,
        success = PremiumSuccess,
        danger = PremiumDanger,
        premium = PremiumAccent,
        outlineMuted = OutlineColor,
        glow = PremiumAction.copy(alpha = 0.15f),
        textPrimary = TextPrimary,
        textSecondary = TextSecondary,
        textMuted = TextMuted,
        background = PremiumBackground,
        cardBackground = PremiumSurface,
        elevatedCardBackground = PremiumCard,
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
    return darkColorScheme(
        primary = PremiumAction,
        onPrimary = PremiumBackground,
        primaryContainer = PremiumCard,
        onPrimaryContainer = TextPrimary,
        secondary = PremiumAccent,
        onSecondary = PremiumBackground,
        secondaryContainer = PremiumSurface,
        onSecondaryContainer = TextPrimary,
        tertiary = PremiumCobalt,
        onTertiary = PremiumBackground,
        tertiaryContainer = PremiumSurface,
        onTertiaryContainer = TextPrimary,
        background = PremiumBackground,
        onBackground = TextPrimary,
        surface = PremiumSurface,
        onSurface = TextPrimary,
        surfaceVariant = PremiumCard,
        onSurfaceVariant = if (highContrast) TextPrimary else TextSecondary,
        error = PremiumDanger,
        onError = TextPrimary,
        errorContainer = Color(0xFF3B151F),
        onErrorContainer = TextPrimary,
        outline = if (highContrast) TextSecondary else OutlineColor,
        outlineVariant = OutlineColor,
        scrim = Color.Black.copy(alpha = 0.8f),
    )
}

private fun premiumExtendedColors(reducedEffects: Boolean): ArcadeExtendedColors {
    return ArcadeExtendedColors(
        shellGradient = Brush.verticalGradient(
            colors = if (reducedEffects) listOf(PremiumBackground, PremiumBackground) else listOf(Color(0xFF05060A), PremiumBackground, PremiumSurface),
        ),
        panelGradient = Brush.linearGradient(
            colors = if (reducedEffects) listOf(PremiumSurface, PremiumSurface) else listOf(PremiumSurface, PremiumCard),
        ),
        heroGradient = Brush.linearGradient(
            colors = if (reducedEffects) listOf(PremiumAccent, PremiumAction) else listOf(PremiumAccent, PremiumAction, PremiumAccent.copy(alpha = 0.8f)),
        ),
        pulseAccent = PremiumAccent,
        laneAccent = PremiumAction,
        stackAccent = PremiumCobalt,
        reward = PremiumReward,
        success = PremiumSuccess,
        danger = PremiumDanger,
        premium = PremiumAccent,
        outlineMuted = OutlineColor,
        glow = if (reducedEffects) Color.Transparent else PremiumAction.copy(alpha = 0.15f),
        textPrimary = TextPrimary,
        textSecondary = TextSecondary,
        textMuted = TextMuted,
        background = PremiumBackground,
        cardBackground = PremiumSurface,
        elevatedCardBackground = PremiumCard,
    )
}

@Composable
fun OfflineMiniArcadeTheme(
    themeId: String, // Kept for interface compatibility
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
