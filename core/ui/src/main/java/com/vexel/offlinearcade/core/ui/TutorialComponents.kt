package com.vexel.offlinearcade.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.GameId

data class GameTutorialContent(
    val gameId: GameId,
    val title: String,
    val lines: List<String>,
    val controls: String,
    val goal: String,
)

@Composable
fun HowToPlayOverlay(
    content: GameTutorialContent,
    onPlay: () -> Unit,
    onSkip: () -> Unit,
) {
    val accent = gameAccentFor(content.gameId.title)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ArcadeTheme.colors.overlayScrim),
        contentAlignment = Alignment.Center,
    ) {
        ArcadeCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            accent = accent.brush,
        ) {
            PremiumBadge(text = content.gameId.title, color = accent.color)
            Text(content.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                content.lines.take(3).forEach { line ->
                    Text("- $line", style = MaterialTheme.typography.bodyLarge, color = ArcadeTheme.colors.textPrimary)
                }
            }
            StatRow("Controls", content.controls)
            StatRow("Goal", content.goal)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PremiumButton(
                    label = "Skip",
                    onClick = onSkip,
                    modifier = Modifier.weight(1f),
                    style = ArcadeButtonStyle.Secondary,
                )
                PremiumButton(
                    label = "Play",
                    onClick = onPlay,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
fun CompletionPopup(
    title: String,
    lines: List<String>,
    onContinue: () -> Unit,
) {
    ArcadeCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        accent = gameAccentFor(title).brush,
    ) {
        PremiumBadge(text = "Run Summary", color = ArcadeTheme.colors.success)
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
        lines.take(4).forEach { line ->
            Text(line, color = ArcadeTheme.colors.textSecondary)
        }
        PremiumButton(label = "Continue", onClick = onContinue, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun GameIconBadge(gameId: GameId, modifier: Modifier = Modifier) {
    val accent = gameAccentFor(gameId.title)
    PremiumBadge(text = gameId.title, modifier = modifier, color = accent.color)
}

@Composable
fun RewardBadge(label: String, modifier: Modifier = Modifier) {
    PremiumBadge(text = label, modifier = modifier, color = ArcadeTheme.colors.reward)
}
