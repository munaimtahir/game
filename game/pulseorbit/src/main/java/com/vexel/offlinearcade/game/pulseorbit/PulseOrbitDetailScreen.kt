package com.vexel.offlinearcade.game.pulseorbit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeInlineActions
import com.vexel.offlinearcade.core.ui.ArcadePlayButton
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.HeroPanel
import com.vexel.offlinearcade.core.ui.PremiumBadge
import com.vexel.offlinearcade.core.ui.PremiumStatTile
import com.vexel.offlinearcade.core.ui.SectionHeader
import com.vexel.offlinearcade.core.ui.StatRow

@Composable
fun PulseOrbitDetailScreen(
    stats: GameStats?,
    coins: Int,
    streak: Int,
    onPlay: () -> Unit,
    onBack: () -> Unit,
) {
    val colors = ArcadeTheme.colors
    val spacing = ArcadeTheme.spacing
    val backgroundBrush = Brush.radialGradient(
        listOf(
            colors.pulseAccent.copy(alpha = 0.18f),
            colors.background,
            colors.background,
        ),
        center = Offset.Unspecified,
        radius = 1200f,
    )

    ArcadeScaffold(
        title = "Game Info",
        onBack = onBack,
        resetScrollOnEnter = true,
        screenTestTag = ArcadeTestTags.PulseOrbitDetail,
        coins = coins,
        streak = streak,
        backgroundBrush = backgroundBrush,
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val compact = maxWidth < 400.dp
            if (compact) {
                Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                    PulseOrbitHero()
                    ArcadeInlineActions {
                        PremiumBadge(text = "Timing")
                        PremiumBadge(text = "One tap")
                        PremiumBadge(text = "Combo")
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                    PulseOrbitHero()
                    Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                        PremiumBadge(text = "Timing")
                        PremiumBadge(text = "One tap")
                        PremiumBadge(text = "Combo")
                    }
                }
            }
        }

        ArcadeCard {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                PremiumStatTile(
                    label = "Best Score",
                    value = (stats?.highScore ?: 0).toString(),
                    modifier = Modifier.weight(1f),
                    accent = colors.pulseAccent,
                )
                PremiumStatTile(
                    label = "Sessions",
                    value = (stats?.sessionsPlayed ?: 0).toString(),
                    modifier = Modifier.weight(1f),
                    accent = colors.reward,
                )
            }
            if ((stats?.sessionsPlayed ?: 0) == 0) {
                Text(
                    "No runs yet. The first clean pass starts the board.",
                    color = colors.textSecondary,
                )
            }
            StatRow("Focus", "Thread the gap")
            StatRow("Best combo", (stats?.bestCombo ?: 0).toString(), valueColor = colors.pulseAccent)
        }

        ArcadeCard(
            accent = Brush.linearGradient(listOf(colors.pulseAccent, colors.accentViolet)),
        ) {
            SectionHeader(title = "How to play", badge = "Timing")
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                Text("• Tap when the orb lines up with the opening.", color = colors.textSecondary)
                Text("• Perfect timing builds the combo and score.", color = colors.textSecondary)
                Text("• The ring tightens and speeds up after every pass.", color = colors.textSecondary)
            }
        }

        ArcadePlayButton(
            label = "Play Orbit",
            onClick = onPlay,
            accentColor = colors.pulseAccent,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            testTag = ArcadeTestTags.PulseOrbitStartButton,
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(spacing.xl))
    }
}

@Composable
private fun PulseOrbitHero() {
    val colors = ArcadeTheme.colors
    HeroPanel(
        overline = "Timing / Reflex",
        title = "Pulse Orbit",
        subtitle = "Thread the opening. Build clean-pass combos.",
        trailing = {
            Box(
                modifier = Modifier
                    .size(132.dp)
                    .padding(2.dp),
            ) {
                Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                    val radius = size.minDimension * 0.34f
                    val center = Offset(size.width / 2f, size.height / 2f)
                    drawCircle(
                        color = colors.pulseAccent.copy(alpha = 0.14f),
                        radius = radius * 1.55f,
                        center = center,
                    )
                    drawCircle(
                        color = colors.accentViolet.copy(alpha = 0.16f),
                        radius = radius * 1.08f,
                        center = center,
                        style = Stroke(width = radius * 0.20f),
                    )
                    drawArc(
                        color = colors.pulseAccent,
                        startAngle = 210f,
                        sweepAngle = 80f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2f, radius * 2f),
                        style = Stroke(width = radius * 0.20f),
                    )
                    drawCircle(
                        color = colors.reward,
                        radius = radius * 0.18f,
                        center = Offset(center.x + radius * 0.58f, center.y - radius * 0.18f),
                    )
                    drawLine(
                        color = Color.White.copy(alpha = 0.65f),
                        start = Offset(center.x - radius * 0.95f, center.y),
                        end = Offset(center.x + radius * 0.95f, center.y),
                        strokeWidth = 3f,
                    )
                }
            }
        },
    )
}
