package com.vexel.offlinearcade.game.brickvolley

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.HeroPanel
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.PremiumStatTile
import com.vexel.offlinearcade.core.ui.SectionHeader

@Composable
fun BrickVolleyDetailScreen(
    stats: GameStats?,
    onPlay: () -> Unit,
    onBack: () -> Unit,
) {
    val spacing = ArcadeTheme.spacing

    ArcadeScaffold(
        title = "Game Info",
        onBack = onBack,
        resetScrollOnEnter = true,
        screenTestTag = ArcadeTestTags.BrickVolleyDetail,
    ) {
        HeroPanel(
            overline = "Aim & Physics",
            title = "Brick Volley",
            subtitle = "Clear the descending bricks before they reach the bottom.",
        )

        ArcadeCard {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                PremiumStatTile(label = "Best Score", value = (stats?.highScore ?: 0).toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.brickVolleyAccent)
                PremiumStatTile(label = "Sessions", value = (stats?.sessionsPlayed ?: 0).toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.reward)
            }
        }

        ArcadeCard {
            SectionHeader(title = "How to play", badge = "Tutorial")
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                Text("• Drag from the launcher to aim your shot.", color = ArcadeTheme.colors.textSecondary)
                Text("• Release to fire a volley of balls.", color = ArcadeTheme.colors.textSecondary)
                Text("• Clear all bricks to advance to the next level.", color = ArcadeTheme.colors.textSecondary)
                Text("• The game ends if a brick reaches the danger zone at the bottom.", color = ArcadeTheme.colors.textSecondary)
            }
        }

        PremiumButton(
            label = "Start Game",
            onClick = onPlay,
            modifier = Modifier.fillMaxWidth().height(56.dp).testTag(ArcadeTestTags.BrickVolleyStartButton)
        )
        
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(spacing.xl))
    }
}
