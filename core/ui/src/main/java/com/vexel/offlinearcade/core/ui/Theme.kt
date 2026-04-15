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

private val MidnightBackground = Color(0xFF0B1020)
private val MidnightSurface = Color(0xFF141B2D)
private val MidnightCard = Color(0xFF1C2540)
private val MidnightOutline = Color(0xFF314062)
private val MidnightText = Color(0xFFF4F7FF)
private val MidnightSubtext = Color(0xFFA8B3CF)
private val Indigo = Color(0xFF7C5CFF)
private val Aqua = Color(0xFF35D6D0)
private val Gold = Color(0xFFF7C75B)
private val Green = Color(0xFF46D37B)
private val Coral = Color(0xFFFF6B6B)
private val Magenta = Color(0xFFD96BFF)
private val Orange = Color(0xFFFFA55B)
private val Cobalt = Color(0xFF5E88FF)

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
)

val LocalArcadeExtendedColors = staticCompositionLocalOf {
    ArcadeExtendedColors(
        shellGradient = Brush.verticalGradient(listOf(MidnightBackground, MidnightSurface)),
        panelGradient = Brush.linearGradient(listOf(MidnightSurface, MidnightCard)),
        heroGradient = Brush.linearGradient(listOf(Indigo, Aqua)),
        pulseAccent = Indigo,
        laneAccent = Aqua,
        stackAccent = Cobalt,
        reward = Gold,
        success = Green,
        danger = Coral,
        premium = Magenta,
        outlineMuted = MidnightOutline,
        glow = Aqua.copy(alpha = 0.18f),
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

private val MidnightTypography = Typography(
    displayLarge = TextStyle(fontSize = 38.sp, lineHeight = 42.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.6).sp),
    displayMedium = TextStyle(fontSize = 32.sp, lineHeight = 36.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.4).sp),
    headlineLarge = TextStyle(fontSize = 28.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold),
    headlineMedium = TextStyle(fontSize = 24.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold),
    titleLarge = TextStyle(fontSize = 20.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold),
    labelMedium = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.3.sp),
)

private fun midnightColorScheme(
    themeId: String,
    highContrast: Boolean,
): androidx.compose.material3.ColorScheme {
    val primary = when (themeId) {
        "ice_grid" -> Aqua
        "sunset_shift" -> Color(0xFF8B74FF)
        else -> Indigo
    }
    val secondary = when (themeId) {
        "sunset_shift" -> Orange
        "ice_grid" -> Color(0xFF58D7FF)
        else -> Aqua
    }
    val tertiary = when (themeId) {
        "ice_grid" -> Cobalt
        "sunset_shift" -> Gold
        else -> Magenta
    }
    return darkColorScheme(
        primary = if (highContrast) MidnightText else primary,
        onPrimary = MidnightText,
        primaryContainer = if (highContrast) Color(0xFF17203A) else MidnightCard,
        onPrimaryContainer = MidnightText,
        secondary = secondary,
        onSecondary = MidnightBackground,
        secondaryContainer = if (highContrast) Color(0xFF173241) else Color(0xFF16283C),
        onSecondaryContainer = MidnightText,
        tertiary = tertiary,
        onTertiary = MidnightBackground,
        tertiaryContainer = if (highContrast) Color(0xFF2A243A) else Color(0xFF251E3F),
        onTertiaryContainer = MidnightText,
        background = MidnightBackground,
        onBackground = MidnightText,
        surface = MidnightSurface,
        onSurface = MidnightText,
        surfaceVariant = MidnightCard,
        onSurfaceVariant = if (highContrast) Color(0xFFD6DDF1) else MidnightSubtext,
        error = Coral,
        onError = MidnightText,
        errorContainer = Color(0xFF3D1E28),
        onErrorContainer = MidnightText,
        outline = if (highContrast) Color(0xFF8FA2D1) else MidnightOutline,
        outlineVariant = if (highContrast) Color(0xFF66769E) else Color(0xFF273251),
        scrim = Color.Black.copy(alpha = 0.7f),
    )
}

private fun midnightExtendedColors(themeId: String, reducedEffects: Boolean): ArcadeExtendedColors {
    val pulse = when (themeId) {
        "ice_grid" -> Color(0xFF78DFFF)
        else -> Indigo
    }
    val lane = when (themeId) {
        "sunset_shift" -> Orange
        else -> Aqua
    }
    val stack = if (themeId == "sunset_shift") Gold else Cobalt
    return ArcadeExtendedColors(
        shellGradient = Brush.verticalGradient(
            colors = if (reducedEffects) listOf(MidnightBackground, MidnightBackground) else listOf(Color(0xFF09101F), MidnightBackground, MidnightSurface),
        ),
        panelGradient = Brush.linearGradient(
            colors = if (reducedEffects) listOf(MidnightSurface, MidnightSurface) else listOf(MidnightSurface, MidnightCard),
        ),
        heroGradient = Brush.linearGradient(
            colors = if (reducedEffects) listOf(pulse, lane) else listOf(pulse, lane, Magenta.copy(alpha = 0.72f)),
        ),
        pulseAccent = pulse,
        laneAccent = lane,
        stackAccent = stack,
        reward = Gold,
        success = Green,
        danger = Coral,
        premium = Magenta,
        outlineMuted = MidnightOutline,
        glow = if (reducedEffects) Color.Transparent else Aqua.copy(alpha = 0.16f),
    )
}

@Composable
fun OfflineMiniArcadeTheme(
    themeId: String,
    highContrast: Boolean = false,
    reducedEffects: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = midnightColorScheme(themeId = themeId, highContrast = highContrast)
    val extendedColors = midnightExtendedColors(themeId = themeId, reducedEffects = reducedEffects)
    androidx.compose.runtime.CompositionLocalProvider(
        LocalArcadeExtendedColors provides extendedColors,
        LocalArcadeSpacing provides ArcadeSpacing(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MidnightTypography,
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
