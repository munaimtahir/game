package com.vexel.offlinearcade.game.lanedrift

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
import androidx.compose.ui.graphics.drawscope.DrawScope
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
fun LaneDriftDetailScreen(
    stats: GameStats?,
    coins: Int,
    streak: Int,
    onPlay: () -> Unit,
    onBack: () -> Unit,
) {
    val colors = ArcadeTheme.colors
    val spacing = ArcadeTheme.spacing
    val backgroundBrush = Brush.linearGradient(
        listOf(
            colors.background,
            colors.laneAccent.copy(alpha = 0.12f),
            colors.background,
        ),
    )

    ArcadeScaffold(
        title = "Game Info",
        onBack = onBack,
        resetScrollOnEnter = true,
        screenTestTag = ArcadeTestTags.LaneDriftDetail,
        coins = coins,
        streak = streak,
        backgroundBrush = backgroundBrush,
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val compact = maxWidth < 400.dp
            Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                LaneDriftHero()
                if (compact) {
                    ArcadeInlineActions {
                        PremiumBadge(text = "Swipe")
                        PremiumBadge(text = "Dodge")
                        PremiumBadge(text = "Collect")
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                        PremiumBadge(text = "Swipe")
                        PremiumBadge(text = "Dodge")
                        PremiumBadge(text = "Collect")
                    }
                }
            }
        }

        ArcadeCard(
            accent = Brush.linearGradient(listOf(colors.laneAccent, colors.primaryCyan)),
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                PremiumStatTile(
                    label = "Best Score",
                    value = (stats?.highScore ?: 0).toString(),
                    modifier = Modifier.weight(1f),
                    accent = colors.laneAccent,
                )
                PremiumStatTile(
                    label = "Sessions",
                    value = (stats?.sessionsPlayed ?: 0).toString(),
                    modifier = Modifier.weight(1f),
                    accent = colors.reward,
                )
            }
            if ((stats?.sessionsPlayed ?: 0) == 0) {
                Text("No runs yet. Dodge blockers and collect shards to light this up.", color = colors.textSecondary)
            }
            StatRow("Flow", "Hold the lane read")
            StatRow("Shards total", (stats?.totalPickups ?: 0).toString(), valueColor = colors.laneAccent)
        }

        ArcadeCard {
            SectionHeader(title = "How to play", badge = "Flow")
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                Text("• Swipe left or right to move between lanes.", color = colors.textSecondary)
                Text("• Keep moving and read the road ahead.", color = colors.textSecondary)
                Text("• Dodge the blockers, collect cyan shards, and let the pace build.", color = colors.textSecondary)
            }
        }

        ArcadePlayButton(
            label = "Play Drift",
            onClick = onPlay,
            accentColor = colors.laneAccent,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            testTag = ArcadeTestTags.LaneDriftStartButton,
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(spacing.xl))
    }
}

@Composable
private fun LaneDriftHero() {
    val colors = ArcadeTheme.colors
    HeroPanel(
        overline = "Speed / Flow",
        title = "Lane Drift",
        subtitle = "Drift through lanes. Dodge blockers. Chase distance.",
        trailing = {
            Box(
                modifier = Modifier
                    .size(132.dp)
                    .padding(2.dp),
            ) {
                Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                    drawLaneArt(this, colors.laneAccent, colors.primaryCyan, colors.reward)
                }
            }
        },
    )
}

private fun drawLaneArt(
    scope: DrawScope,
    laneAccent: Color,
    secondaryAccent: Color,
    rewardAccent: Color,
) = with(scope) {
    val laneWidth = size.width / 3f
    drawRoundRect(
        color = laneAccent.copy(alpha = 0.12f),
        topLeft = Offset.Zero,
        size = size,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f),
    )
    repeat(3) { lane ->
        val left = laneWidth * lane + 7f
        drawRoundRect(
            color = if (lane == 1) secondaryAccent.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.05f),
            topLeft = Offset(left, 0f),
            size = Size(laneWidth - 14f, size.height),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f, 18f),
            style = Stroke(width = 2f),
        )
        if (lane < 2) {
            val separatorX = laneWidth * (lane + 1)
            drawLine(
                color = secondaryAccent.copy(alpha = 0.40f),
                start = Offset(separatorX, 14f),
                end = Offset(separatorX, size.height - 14f),
                strokeWidth = 3f,
            )
        }
    }
    drawLine(
        color = rewardAccent.copy(alpha = 0.75f),
        start = Offset(laneWidth * 0.5f, size.height * 0.18f),
        end = Offset(laneWidth * 1.75f, size.height * 0.84f),
        strokeWidth = 7f,
    )
    drawCircle(
        color = rewardAccent,
        radius = 9f,
        center = Offset(laneWidth * 1.75f, size.height * 0.84f),
    )
    drawCircle(
        color = secondaryAccent,
        radius = 7f,
        center = Offset(laneWidth * 1.0f, size.height * 0.38f),
    )
}
