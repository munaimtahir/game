package com.vexel.offlinearcade.feature.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
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
    val topScore = stats.maxOfOrNull { it.highScore } ?: 0

    ArcadeScaffold(
        title = "Stats",
        onBack = onBack,
        screenTestTag = ArcadeTestTags.StatsScreen,
    ) {
        SectionHeader(
            title = "Player Snapshot",
            subtitle = "Prestige comes from consistency: local sessions, strong bests, and recent form across all three games.",
        )
        ArcadeCard(accent = gameAccentFor(bestGame?.gameId?.title ?: "Pulse Orbit").brush) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    PremiumBadge(text = "Arcade overview", color = ArcadeTheme.colors.premium)
                    Text("Momentum Block", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                }
                PremiumBadge(
                    text = bestGame?.gameId?.title ?: "No runs yet",
                    color = ArcadeTheme.colors.success,
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PremiumStatTile("Runs", totalRuns.toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.success)
                PremiumStatTile("Score", totalScore.toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.reward)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PremiumStatTile("Play Time", "${totalPlaySeconds}s", modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.pulseAccent)
                PremiumStatTile("Top Score", topScore.toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.premium)
            }
            PremiumProgress(
                progress = if (totalScore == 0) 0f else topScore.toFloat() / totalScore.toFloat(),
                label = "Top-score share",
                accent = ArcadeTheme.colors.reward,
            )
        }
        SectionHeader(
            title = "Per-game drilldown",
            subtitle = "Compact cards keep each game lane scannable without turning this into a report wall.",
        )
        stats.chunked(2).forEach { rowStats ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowStats.forEach { gameStats ->
                    GameStatsDrilldownCard(gameStats = gameStats, modifier = Modifier.weight(1f))
                }
                if (rowStats.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun GameStatsDrilldownCard(
    gameStats: GameStats,
    modifier: Modifier = Modifier,
) {
    val accent = gameAccentFor(gameStats.gameId.title)
    val efficiency = if (gameStats.sessionsPlayed == 0) 0f else gameStats.highScore.toFloat() / gameStats.totalScore.coerceAtLeast(1).toFloat()
    ArcadeCard(modifier = modifier, accent = accent.brush) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(gameStats.gameId.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            PremiumBadge(text = "${gameStats.sessionsPlayed} sessions", color = accent.color)
        }
        PremiumStatTile("High Score", gameStats.highScore.toString(), accent = accent.color)
        StatRow(
            label = if (gameStats.bestLines > 0) "Best lines" else "Best combo",
            value = if (gameStats.bestLines > 0) gameStats.bestLines.toString() else gameStats.bestCombo.toString(),
        )
        StatRow("Total score", gameStats.totalScore.toString())
        StatRow("Play time", "${gameStats.totalPlayMillis / 1000}s")
        PremiumProgress(progress = efficiency, label = "Best-score share", accent = accent.color)
    }
}
