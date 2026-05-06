package com.vexel.offlinearcade.game.stackdrop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.HeroPanel
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.PremiumStatTile
import com.vexel.offlinearcade.core.ui.SectionHeader

@Composable
fun StackDropDetailScreen(
    stats: GameStats?,
    onPlay: () -> Unit,
    onBack: () -> Unit,
) {
    val spacing = ArcadeTheme.spacing

    ArcadeScaffold(
        title = "Game Info",
        onBack = onBack,
        resetScrollOnEnter = true,
        screenTestTag = com.vexel.offlinearcade.core.ui.ArcadeTestTags.StackDropScreen,
    ) {
        HeroPanel(
            overline = "Strategy & Tactics",
            title = "Stack Drop",
            subtitle = "Cobalt and amber mastery. Clear lines and keep the board clean.",
        )

        ArcadeCard {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                PremiumStatTile(label = "Best Score", value = (stats?.highScore ?: 0).toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.stackAccent)
                PremiumStatTile(label = "Sessions", value = (stats?.sessionsPlayed ?: 0).toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.reward)
            }
        }

        ArcadeCard {
            SectionHeader(title = "How to play", badge = "Tutorial")
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                Text("• Tap to rotate the active piece.", color = ArcadeTheme.colors.textSecondary)
                Text("• Swipe left or right to move.", color = ArcadeTheme.colors.textSecondary)
                Text("• Swipe down to drop faster.", color = ArcadeTheme.colors.textSecondary)
                Text("• Use a fast downward flick for an instant hard drop.", color = ArcadeTheme.colors.textSecondary)
            }
        }

        PremiumButton(
            label = "Start Game",
            onClick = onPlay,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(spacing.xl))
    }
}
