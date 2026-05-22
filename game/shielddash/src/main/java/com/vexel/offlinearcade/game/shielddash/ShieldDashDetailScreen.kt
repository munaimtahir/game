package com.vexel.offlinearcade.game.shielddash

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
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.HeroPanel
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.PremiumStatTile
import com.vexel.offlinearcade.core.ui.SectionHeader

@Composable
fun ShieldDashDetailScreen(
    stats: GameStats?,
    onPlay: () -> Unit,
    onBack: () -> Unit,
) {
    val spacing = ArcadeTheme.spacing

    ArcadeScaffold(
        title = "Game Info",
        onBack = onBack,
        resetScrollOnEnter = true,
        screenTestTag = "ShieldDashDetailScreen",
    ) {
        HeroPanel(
            overline = "Defensive Reflex",
            title = "Shield Dash",
            subtitle = "Rotate a shield to block incoming hazards and protect the core.",
        )

        ArcadeCard {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                PremiumStatTile(label = "Best Score", value = (stats?.highScore ?: 0).toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.shieldDashAccent)
                PremiumStatTile(label = "Sessions", value = (stats?.sessionsPlayed ?: 0).toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.reward)
            }
        }

        ArcadeCard {
            SectionHeader(title = "How to play", badge = "Tutorial")
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                Text("• Drag around the core to rotate your shield.", color = ArcadeTheme.colors.textSecondary)
                Text("• Block incoming hazards to score points.", color = ArcadeTheme.colors.textSecondary)
                Text("• Perfect blocks award bonus points.", color = ArcadeTheme.colors.textSecondary)
                Text("• Missed hazards will damage the core and end the game.", color = ArcadeTheme.colors.textSecondary)
            }
        }

        PremiumButton(
            label = "Start Game",
            onClick = onPlay,
            modifier = Modifier.fillMaxWidth().height(56.dp).testTag("ShieldDashStartButton")
        )
        
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(spacing.xl))
    }
}
