package com.vexel.offlinearcade.game.stackdrop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.vexel.offlinearcade.core.ui.ArcadeMarquee
import com.vexel.offlinearcade.core.ui.ArcadePlayButton
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
    val spacing = ArcadeTheme.spacing

    val context = LocalContext.current
    val headerResId = remember(context) {
        context.resources.getIdentifier("header_stack_drop", "drawable", context.packageName)
    }

    ArcadeScaffold(
        title = "Game Info",
        onBack = onBack,
        resetScrollOnEnter = true,
        screenTestTag = com.vexel.offlinearcade.core.ui.ArcadeTestTags.StackDropDetail,
        coins = coins,
        streak = streak,
    ) {
        if (headerResId != 0) {
            ArcadeMarquee(
                resId = headerResId,
                contentDescription = "Stack Drop Header",
                accentColor = ArcadeTheme.colors.stackAccent
            )
        } else {
            HeroPanel(
                overline = "Strategy & Tactics",
                title = "Stack Drop",
                subtitle = "Cobalt and amber mastery. Clear lines and keep the board clean.",
            )
        }

        ArcadeCard {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                PremiumStatTile(label = "Best Score", value = (stats?.highScore ?: 0).toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.stackAccent)
                PremiumStatTile(label = "Sessions", value = (stats?.sessionsPlayed ?: 0).toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.reward)
            }
            if ((stats?.sessionsPlayed ?: 0) == 0) {
                Text("No runs yet. Clear your first line to start building mastery.", color = ArcadeTheme.colors.textSecondary)
            }
            StatRow("Planning focus", "Next piece and clean lines")
            StatRow("Best lines", (stats?.bestLines ?: 0).toString(), valueColor = ArcadeTheme.colors.stackAccent)
        }

        ArcadeCard {
            SectionHeader(title = "How to play", badge = "Tutorial")
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                Text("• Tap to rotate the active piece.", color = ArcadeTheme.colors.textSecondary)
                Text("• Swipe left or right to move.", color = ArcadeTheme.colors.textSecondary)
                Text("• Swipe down to drop faster.", color = ArcadeTheme.colors.textSecondary)
                Text("• Use a fast downward flick for an instant hard drop.", color = ArcadeTheme.colors.textSecondary)
                Text("• Watch the Next panel to plan your board.", color = ArcadeTheme.colors.textSecondary)
            }
        }

        ArcadePlayButton(
            label = "Start Game",
            onClick = onPlay,
            accentColor = ArcadeTheme.colors.stackAccent,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            testTag = com.vexel.offlinearcade.core.ui.ArcadeTestTags.StackDropStartButton
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(spacing.xl))
    }
}
