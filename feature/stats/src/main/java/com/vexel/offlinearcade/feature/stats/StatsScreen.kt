package com.vexel.offlinearcade.feature.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.PremiumBadge
import com.vexel.offlinearcade.core.ui.PremiumProgress
import com.vexel.offlinearcade.core.ui.PremiumStatTile
import com.vexel.offlinearcade.core.ui.SectionHeader
import com.vexel.offlinearcade.core.ui.StatRow
import com.vexel.offlinearcade.core.ui.gameAccentFor

@Composable
fun StatsScreen(
    stats: List<GameStats>,
    onBack: () -> Unit,
) {
    val totalRuns = stats.sumOf { it.sessionsPlayed }
    val totalScore = stats.sumOf { it.totalScore }
    val totalPlaySeconds = stats.sumOf { it.totalPlayMillis } / 1000
    val bestGame = stats.maxByOrNull { it.highScore }

    ArcadeScaffold(title = "Stats", onBack = onBack) {
        SectionHeader(
            title = "Player Snapshot",
            subtitle = "Prestige comes from consistency: local sessions, strong bests, and recent form across all three games.",
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            PremiumStatTile("Runs", totalRuns.toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.success)
            PremiumStatTile("Score", totalScore.toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.reward)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            PremiumStatTile("Play Time", "${totalPlaySeconds}s", modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.pulseAccent)
            PremiumStatTile("Best Game", bestGame?.gameId?.title ?: "None", modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.premium)
        }

        stats.forEach { gameStats ->
            val accent = gameAccentFor(gameStats.gameId.title)
            val efficiency = if (gameStats.sessionsPlayed == 0) 0f else gameStats.highScore.toFloat() / (gameStats.totalScore.coerceAtLeast(1)).toFloat()
            ArcadeCard(accent = accent.brush) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(gameStats.gameId.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    PremiumBadge(text = "${gameStats.sessionsPlayed} sessions", color = accent.color)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PremiumStatTile("High Score", gameStats.highScore.toString(), modifier = Modifier.weight(1f), accent = accent.color)
                    PremiumStatTile(
                        label = if (gameStats.bestLines > 0) "Best Lines" else "Best Combo",
                        value = if (gameStats.bestLines > 0) gameStats.bestLines.toString() else gameStats.bestCombo.toString(),
                        modifier = Modifier.weight(1f),
                        accent = ArcadeTheme.colors.reward,
                    )
                }
                PremiumProgress(progress = efficiency.coerceIn(0f, 1f), label = "Best-score share", accent = accent.color)
                StatRow("Total score", gameStats.totalScore.toString())
                StatRow("Sessions played", gameStats.sessionsPlayed.toString())
                StatRow("Play time", "${gameStats.totalPlayMillis / 1000}s")
            }
        }
    }
}
