package com.vexel.offlinearcade.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DaybreakScheme = lightColorScheme(
    primary = Color(0xFF00695C),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF86F2DD),
    onPrimaryContainer = Color(0xFF00201B),
    secondary = Color(0xFF6C4A00),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDDA7),
    onSecondaryContainer = Color(0xFF231800),
    tertiary = Color(0xFF8E3B46),
    tertiaryContainer = Color(0xFFFFD9DD),
    background = Color(0xFFF3FFF9),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFD6E8E1),
    onSurface = Color(0xFF17201D),
    onSurfaceVariant = Color(0xFF42514C),
)

private val SunsetScheme = lightColorScheme(
    primary = Color(0xFFB14500),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDCC9),
    onPrimaryContainer = Color(0xFF381300),
    secondary = Color(0xFF675D00),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF4E46A),
    onSecondaryContainer = Color(0xFF201C00),
    tertiary = Color(0xFF78536C),
    tertiaryContainer = Color(0xFFFFD7F2),
    background = Color(0xFFFFF8F5),
    surface = Color.White,
    surfaceVariant = Color(0xFFF1DED4),
    onSurface = Color(0xFF261A15),
    onSurfaceVariant = Color(0xFF55433A),
)

private val IceGridScheme = lightColorScheme(
    primary = Color(0xFF006782),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBCE9FF),
    onPrimaryContainer = Color(0xFF001F29),
    secondary = Color(0xFF355F72),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD8EEFD),
    onSecondaryContainer = Color(0xFF0F1D25),
    tertiary = Color(0xFF4D57A9),
    tertiaryContainer = Color(0xFFDFE0FF),
    background = Color(0xFFF6FAFD),
    surface = Color.White,
    surfaceVariant = Color(0xFFDBE4EA),
    onSurface = Color(0xFF171C1F),
    onSurfaceVariant = Color(0xFF3F484D),
)

private val DarkFallbackScheme = darkColorScheme()

@Composable
fun OfflineMiniArcadeTheme(
    themeId: String,
    content: @Composable () -> Unit,
) {
    val colorScheme = when (themeId) {
        "sunset_shift" -> SunsetScheme
        "ice_grid" -> IceGridScheme
        "default" -> DaybreakScheme
        else -> DarkFallbackScheme
    }
    MaterialTheme(colorScheme = colorScheme, content = content)
}
