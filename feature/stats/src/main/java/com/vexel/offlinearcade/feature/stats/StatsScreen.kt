package com.vexel.offlinearcade.feature.stats

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.SectionHeader
import com.vexel.offlinearcade.core.ui.StatRow

@Composable
fun StatsScreen(
    stats: List<GameStats>,
    onBack: () -> Unit,
) {
    ArcadeScaffold(
        title = "Stats",
        onBack = onBack,
        screenTestTag = ArcadeTestTags.StatsScreen,
    ) {
        SectionHeader(title = "Shared totals", subtitle = "Everything is local and updated at the end of each run.")
        ArcadeCard {
            StatRow("Runs played", stats.sumOf { it.sessionsPlayed }.toString())
            StatRow("Combined score", stats.sumOf { it.totalScore }.toString())
            StatRow("Total play time", "${stats.sumOf { it.totalPlayMillis } / 1000}s")
        }

        stats.forEach { gameStats ->
            ArcadeCard {
                Text(gameStats.gameId.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                StatRow("High score", gameStats.highScore.toString())
                StatRow("Sessions", gameStats.sessionsPlayed.toString())
                StatRow("Best combo", gameStats.bestCombo.toString())
                StatRow("Best lines", gameStats.bestLines.toString())
            }
        }
    }
}
