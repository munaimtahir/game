package com.vexel.offlinearcade.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class ArcadeButtonStyle { Primary, Secondary, Tonal }

data class ArcadeGameAccent(
    val label: String,
    val brush: Brush,
    val color: Color,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArcadeScaffold(
    title: String,
    onBack: (() -> Unit)? = null,
    scrollable: Boolean = true,
    screenTestTag: String? = null,
    actions: @Composable (() -> Unit) = {},
    content: @Composable () -> Unit,
) {
    val spacing = ArcadeTheme.spacing
    val bodyModifier = if (scrollable) {
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacing.lg, vertical = spacing.md)
    } else {
        Modifier
            .fillMaxSize()
            .padding(horizontal = spacing.lg, vertical = spacing.md)
    }
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Column {
                        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    }
                },
                navigationIcon = {
                    if (onBack != null) {
                        TextButton(
                            onClick = onBack,
                            modifier = Modifier.testTag(ArcadeTestTags.BackButton),
                        ) {
                            Text("Back")
                        }
                    }
                },
                actions = { actions() },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ArcadeTheme.colors.shellGradient)
                .padding(padding),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ArcadeTheme.colors.glow.copy(alpha = 0.35f)),
            )
            Column(
                modifier = if (screenTestTag != null) {
                    bodyModifier.testTag(screenTestTag)
                } else {
                    bodyModifier
                },
                verticalArrangement = Arrangement.spacedBy(spacing.md),
            ) {
                content()
            }
        }
    }
}

@Composable
fun ArcadeCard(
    modifier: Modifier = Modifier,
    contentPadding: androidx.compose.ui.unit.Dp = ArcadeTheme.spacing.md,
    accent: Brush? = null,
    content: @Composable () -> Unit,
) {
    val spacing = ArcadeTheme.spacing
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.92f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ArcadeTheme.colors.panelGradient)
                .then(
                    if (accent != null) {
                        Modifier.border(width = 1.dp, brush = accent, shape = RoundedCornerShape(28.dp))
                    } else {
                        Modifier.border(width = 1.dp, color = ArcadeTheme.colors.outlineMuted.copy(alpha = 0.8f), shape = RoundedCornerShape(28.dp))
                    },
                )
                .padding(contentPadding),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                content()
            }
        }
    }
}

@Composable
fun PremiumButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ArcadeButtonStyle = ArcadeButtonStyle.Primary,
    enabled: Boolean = true,
) {
    when (style) {
        ArcadeButtonStyle.Primary -> Button(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier.shadow(10.dp, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge)
        }

        ArcadeButtonStyle.Secondary -> OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, Brush.linearGradient(listOf(MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.secondary))),
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge)
        }

        ArcadeButtonStyle.Tonal -> Button(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier,
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun HudPill(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .border(1.dp, ArcadeTheme.colors.outlineMuted.copy(alpha = 0.75f), RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp),
        ) {
            Text(label.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun PremiumBadge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = ArcadeTheme.colors.reward,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(color.copy(alpha = 0.14f))
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Text(text, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    badge: String? = null,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            if (badge != null) {
                PremiumBadge(text = badge, color = ArcadeTheme.colors.premium)
            }
        }
        if (subtitle != null) {
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun StatRow(label: String, value: String, valueColor: Color = MaterialTheme.colorScheme.onSurface) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        Text(value, color = valueColor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PremiumStatTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    accent: Color = ArcadeTheme.colors.reward,
) {
    ArcadeCard(modifier = modifier, contentPadding = ArcadeTheme.spacing.md) {
        Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(accent))
        Text(label.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
    }
}

@Composable
fun PremiumProgress(
    progress: Float,
    label: String,
    modifier: Modifier = Modifier,
    accent: Color = ArcadeTheme.colors.reward,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${(progress.coerceIn(0f, 1f) * 100).toInt()}%", style = MaterialTheme.typography.labelLarge)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(999.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(3.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .clip(RoundedCornerShape(999.dp))
                    .background(accent)
                    .padding(vertical = 4.dp),
            )
        }
    }
}

@Composable
fun HeroPanel(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    overline: String? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(ArcadeTheme.colors.heroGradient)
            .padding(24.dp),
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (overline != null) {
                    Text(overline.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                }
                Text(title, style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Black)
                Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.88f))
            }
            trailing?.invoke()
        }
    }
}

@Composable
fun ArcadeInlineActions(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        content()
    }
}

@Composable
fun PremiumOverlayCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ArcadeCard(
        modifier = modifier.widthIn(max = 420.dp),
        accent = Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)),
        contentPadding = ArcadeTheme.spacing.lg,
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        content()
    }
}

@Composable
fun SplashShell(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ArcadeTheme.colors.shellGradient),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(108.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(ArcadeTheme.colors.heroGradient),
                contentAlignment = Alignment.Center,
            ) {
                Text("OMA", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Black)
            }
            Text(title, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black)
            Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            CircularProgressIndicator(
                progress = { 0.72f },
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 4.dp,
            )
        }
    }
}

@Composable
fun gameAccentFor(label: String): ArcadeGameAccent {
    val colors = ArcadeTheme.colors
    return when (label) {
        "Pulse Orbit" -> ArcadeGameAccent(label = "Pulse Orbit", brush = Brush.linearGradient(listOf(colors.pulseAccent, MaterialTheme.colorScheme.secondary)), color = colors.pulseAccent)
        "Lane Drift" -> ArcadeGameAccent(label = "Lane Drift", brush = Brush.linearGradient(listOf(colors.laneAccent, Color(0xFFFFA55B))), color = colors.laneAccent)
        else -> ArcadeGameAccent(label = "Stack Drop", brush = Brush.linearGradient(listOf(colors.stackAccent, colors.reward)), color = colors.stackAccent)
    }
}
