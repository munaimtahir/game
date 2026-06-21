package com.vexel.offlinearcade.game.stackdrop

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
fun StackDropDetailScreen(
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
            colors.stackAccent.copy(alpha = 0.12f),
            colors.background,
        ),
    )

    ArcadeScaffold(
        title = "Game Info",
        onBack = onBack,
        resetScrollOnEnter = true,
        screenTestTag = ArcadeTestTags.StackDropDetail,
        coins = coins,
        streak = streak,
        backgroundBrush = backgroundBrush,
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val compact = maxWidth < 400.dp
            Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                StackDropHero()
                if (compact) {
                    ArcadeInlineActions {
                        PremiumBadge(text = "Rotate")
                        PremiumBadge(text = "Plan ahead")
                        PremiumBadge(text = "Clear lines")
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                        PremiumBadge(text = "Rotate")
                        PremiumBadge(text = "Plan ahead")
                        PremiumBadge(text = "Clear lines")
                    }
                }
            }
        }

        ArcadeCard(
            accent = Brush.linearGradient(listOf(colors.stackAccent, colors.reward)),
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                PremiumStatTile(
                    label = "Best Score",
                    value = (stats?.highScore ?: 0).toString(),
                    modifier = Modifier.weight(1f),
                    accent = colors.stackAccent,
                )
                PremiumStatTile(
                    label = "Sessions",
                    value = (stats?.sessionsPlayed ?: 0).toString(),
                    modifier = Modifier.weight(1f),
                    accent = colors.reward,
                )
            }
            if ((stats?.sessionsPlayed ?: 0) == 0) {
                Text("No runs yet. Clear a line to start building momentum.", color = colors.textSecondary)
            }
            StatRow("Planning", "Keep the stack low")
            StatRow("Best lines", (stats?.bestLines ?: 0).toString(), valueColor = colors.stackAccent)
        }

        ArcadeCard {
            SectionHeader(title = "How to play", badge = "Strategy")
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                Text("• Tap to rotate the active piece.", color = colors.textSecondary)
                Text("• Swipe left or right to move.", color = colors.textSecondary)
                Text("• Swipe down to soft drop and flick down for a hard drop.", color = colors.textSecondary)
                Text("• Watch the next piece so you can build clean line clears.", color = colors.textSecondary)
            }
        }

        ArcadePlayButton(
            label = "Play Stack",
            onClick = onPlay,
            accentColor = colors.stackAccent,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            testTag = ArcadeTestTags.StackDropStartButton,
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(spacing.xl))
    }
}

@Composable
private fun StackDropHero() {
    val colors = ArcadeTheme.colors
    HeroPanel(
        overline = "Strategy / Stack",
        title = "Stack Drop",
        subtitle = "Place blocks under pressure. Clear lines. Survive the rise.",
        trailing = {
            Box(
                modifier = Modifier
                    .size(132.dp)
                    .padding(2.dp),
            ) {
                Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                    val columnWidth = size.width / 4.6f
                    val gap = columnWidth * 0.20f
                    val left = size.width * 0.20f
                    val baseTop = size.height * 0.18f
                    repeat(4) { index ->
                        val heightScale = 0.20f + index * 0.15f
                        drawRect(
                            color = if (index % 2 == 0) colors.stackAccent.copy(alpha = 0.82f) else colors.reward.copy(alpha = 0.78f),
                            topLeft = Offset(left + (columnWidth + gap) * index * 0.55f, size.height - baseTop - size.height * heightScale),
                            size = Size(columnWidth, size.height * heightScale),
                        )
                    }
                    drawRect(
                        color = colors.pulseAccent.copy(alpha = 0.18f),
                        topLeft = Offset(size.width * 0.10f, size.height * 0.18f),
                        size = Size(size.width * 0.80f, size.height * 0.66f),
                    )
                    drawRect(
                        color = colors.reward,
                        topLeft = Offset(size.width * 0.34f, size.height * 0.08f),
                        size = Size(size.width * 0.22f, size.height * 0.16f),
                    )
                }
            }
        },
    )
}
