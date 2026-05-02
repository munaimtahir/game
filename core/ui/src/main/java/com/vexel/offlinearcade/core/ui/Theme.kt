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

// Calm Focus Arcade — Light Palette (soft light, not pure white)
private val AppBackground = Color(0xFFF4F8FB)
private val SurfacePrimary = Color(0xFFFFFFFF)
private val SurfaceSecondary = Color(0xFFEEF4F8)
private val SurfaceTertiary = Color(0xFFE7EEF5)
private val CardSurface = Color(0xFFFFFFFF)
private val ElevatedCard = Color(0xFFF7FAFD)

// Text
private val TextPrimary = Color(0xFF102033)
private val TextSecondary = Color(0xFF4E6278)
private val TextMuted = Color(0xFF708399)
private val TextInverse = Color(0xFFFFFFFF)

// Borders / dividers
private val SoftBorder = Color(0xFFC9D7E4)
private val StrongBorder = Color(0xFFAFC2D4)

// Actions
private val PrimaryAction = Color(0xFF1CCFE2)
private val PrimaryActionPressed = Color(0xFF12AFC4)
private val PrimaryOnAction = Color(0xFF06121D)
private val SecondaryAction = Color(0xFF6C63FF)
private val SupportAccent = Color(0xFF4A7DFF)

// Gameplay surfaces
private val GameBackground = AppBackground
private val GameBoard = Color(0xFFDDE7F0)
private val GameBoardInner = ElevatedCard
private val GameBoardRaised = Color(0xFFEAF1F7)
private val GridLine = Color(0xFFC6D3DF)
private val HudCard = SurfacePrimary
private val HudBorder = SoftBorder

// Semantic gameplay colors
private val PlayerCyan = PrimaryAction
private val PlayerBlue = SupportAccent
private val PlayerViolet = SecondaryAction
private val DangerBlocker = Color(0xFFE85D75)
private val DangerBlockerDark = Color(0xFFC9435D)
private val PickupMint = Color(0xFF28C7A8)
private val RewardAmber = Color(0xFFF2B94B)
private val ComboViolet = Color(0xFF7C6BFF)

// Overlay
private val OverlayScrim = TextPrimary
private val OverlayCard = SurfacePrimary
private val OverlayBorder = SoftBorder

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
    val outlineStrong: Color,
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
    val gameBoardInner: Color,
    val gameBoardRaised: Color,
    val gridLine: Color,
    val hudCard: Color,
    val hudBorder: Color,
    val controlSurface: Color,
    val controlBorder: Color,
    val primaryCyan: Color,
    val primaryCyanPressed: Color,
    val primaryOnCyan: Color,
    val accentViolet: Color,
    val supportBlue: Color,
    val dangerCoral: Color,
    val dangerCoralDark: Color,
    val pickupMint: Color,
    val overlayScrim: Color,
    val overlayCard: Color,
    val overlayBorder: Color,
)

val LocalArcadeExtendedColors = staticCompositionLocalOf {
    ArcadeExtendedColors(
        shellGradient = Brush.verticalGradient(listOf(AppBackground, SurfaceSecondary)),
        panelGradient = Brush.linearGradient(listOf(CardSurface, ElevatedCard)),
        heroGradient = Brush.linearGradient(listOf(SecondaryAction, PrimaryAction)),
        pulseAccent = ComboViolet,
        laneAccent = SupportAccent,
        stackAccent = SupportAccent,
        reward = RewardAmber,
        success = PickupMint,
        danger = DangerBlocker,
        premium = SecondaryAction,
        outlineMuted = SoftBorder,
        outlineStrong = StrongBorder,
        glow = PrimaryAction.copy(alpha = 0.10f),
        textPrimary = TextPrimary,
        textSecondary = TextSecondary,
        textMuted = TextMuted,
        textInverse = TextInverse,
        background = AppBackground,
        cardBackground = SurfaceSecondary,
        elevatedCardBackground = CardSurface,
        gameBackground = GameBackground,
        gameBoard = GameBoard,
        gameBoardInner = GameBoardInner,
        gameBoardRaised = GameBoardRaised,
        gridLine = GridLine,
        hudCard = HudCard,
        hudBorder = HudBorder,
        controlSurface = SurfacePrimary,
        controlBorder = StrongBorder,
        primaryCyan = PlayerCyan,
        primaryCyanPressed = PrimaryActionPressed,
        primaryOnCyan = PrimaryOnAction,
        accentViolet = PlayerViolet,
        supportBlue = PlayerBlue,
        dangerCoral = DangerBlocker,
        dangerCoralDark = DangerBlockerDark,
        pickupMint = PickupMint,
        overlayScrim = OverlayScrim,
        overlayCard = OverlayCard,
        overlayBorder = OverlayBorder,
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

private fun calmFocusColorScheme(highContrast: Boolean): androidx.compose.material3.ColorScheme {
    return lightColorScheme(
        primary = PrimaryAction,
        onPrimary = PrimaryOnAction,
        primaryContainer = SurfaceTertiary,
        onPrimaryContainer = TextPrimary,
        secondary = SecondaryAction,
        onSecondary = TextInverse,
        secondaryContainer = SurfaceSecondary,
        onSecondaryContainer = TextPrimary,
        tertiary = SupportAccent,
        onTertiary = TextInverse,
        tertiaryContainer = SurfaceSecondary,
        onTertiaryContainer = TextPrimary,
        background = AppBackground,
        onBackground = TextPrimary,
        surface = SurfacePrimary,
        onSurface = TextPrimary,
        surfaceVariant = SurfaceSecondary,
        onSurfaceVariant = if (highContrast) TextPrimary else TextSecondary,
        error = DangerBlocker,
        onError = TextInverse,
        errorContainer = Color(0xFFFFE6EA),
        onErrorContainer = Color(0xFF3A0C15),
        outline = if (highContrast) StrongBorder else SoftBorder,
        outlineVariant = SoftBorder,
        scrim = OverlayScrim.copy(alpha = 0.72f),
    )
}

private fun calmFocusExtendedColors(themeId: String, reducedEffects: Boolean): ArcadeExtendedColors {
    val heroStops = when (themeId) {
        "sunset_shift" -> listOf(SecondaryAction, RewardAmber)
        "ice_grid" -> listOf(SupportAccent, PrimaryAction)
        else -> listOf(SecondaryAction, PrimaryAction)
    }
    val heroGradient = if (reducedEffects) {
        Brush.linearGradient(heroStops)
    } else {
        Brush.linearGradient(listOf(heroStops.first(), heroStops.last(), heroStops.first().copy(alpha = 0.92f)))
    }
    val shellGradient = if (reducedEffects) {
        Brush.verticalGradient(listOf(AppBackground, AppBackground))
    } else {
        Brush.verticalGradient(listOf(AppBackground, SurfaceSecondary))
    }
    val panelGradient = if (reducedEffects) {
        Brush.linearGradient(listOf(CardSurface, CardSurface))
    } else {
        Brush.linearGradient(listOf(CardSurface, ElevatedCard))
    }
    return ArcadeExtendedColors(
        shellGradient = shellGradient,
        panelGradient = panelGradient,
        heroGradient = heroGradient,
        pulseAccent = ComboViolet,
        laneAccent = SupportAccent,
        stackAccent = SupportAccent,
        reward = RewardAmber,
        success = PickupMint,
        danger = DangerBlocker,
        premium = SecondaryAction,
        outlineMuted = SoftBorder,
        outlineStrong = StrongBorder,
        glow = if (reducedEffects) Color.Transparent else PrimaryAction.copy(alpha = 0.10f),
        textPrimary = TextPrimary,
        textSecondary = TextSecondary,
        textMuted = TextMuted,
        textInverse = TextInverse,
        background = AppBackground,
        cardBackground = SurfaceSecondary,
        elevatedCardBackground = CardSurface,
        gameBackground = GameBackground,
        gameBoard = GameBoard,
        gameBoardInner = GameBoardInner,
        gameBoardRaised = GameBoardRaised,
        gridLine = GridLine,
        hudCard = HudCard,
        hudBorder = HudBorder,
        controlSurface = SurfacePrimary,
        controlBorder = StrongBorder,
        primaryCyan = PlayerCyan,
        primaryCyanPressed = PrimaryActionPressed,
        primaryOnCyan = PrimaryOnAction,
        accentViolet = PlayerViolet,
        supportBlue = PlayerBlue,
        dangerCoral = DangerBlocker,
        dangerCoralDark = DangerBlockerDark,
        pickupMint = PickupMint,
        overlayScrim = OverlayScrim,
        overlayCard = OverlayCard,
        overlayBorder = OverlayBorder,
    )
}

@Composable
fun OfflineMiniArcadeTheme(
    themeId: String, // Kept for interface compatibility
    highContrast: Boolean = false,
    reducedEffects: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = calmFocusColorScheme(highContrast = highContrast)
    val extendedColors = calmFocusExtendedColors(themeId = themeId, reducedEffects = reducedEffects)
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
