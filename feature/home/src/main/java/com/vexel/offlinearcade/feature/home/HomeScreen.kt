package com.vexel.offlinearcade.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.DailyChallenge
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.PlayerProfile
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.HeroPanel
import com.vexel.offlinearcade.core.ui.HudPill
import com.vexel.offlinearcade.core.ui.SectionHeader
import com.vexel.offlinearcade.core.ui.StatRow

@Composable
fun HomeScreen(
    profile: PlayerProfile,
    stats: List<GameStats>,
    todayChallenges: List<DailyChallenge>,
    onPulseOrbit: () -> Unit,
    onLaneDrift: () -> Unit,
    onStackDrop: () -> Unit,
    onChallenges: () -> Unit,
    onStats: () -> Unit,
    onSettings: () -> Unit,
) {
    ArcadeScaffold(title = "Offline Mini Arcade") {
        HeroPanel(
            title = "Fast offline arcade sessions",
            subtitle = "Three focused games, shared progression, and near-instant retries.",
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            HudPill(label = "Coins", value = profile.coins.toString(), modifier = Modifier.weight(1f))
            HudPill(label = "Streak", value = "${profile.currentStreakDays}d", modifier = Modifier.weight(1f))
            HudPill(label = "Daily", value = "${todayChallenges.count { it.completed }}/${todayChallenges.size}", modifier = Modifier.weight(1f))
        }

        SectionHeader(
            title = "Play",
            subtitle = "Open, choose, play. Each card jumps straight into the game.",
        )
        GameEntryCard(
            title = "Pulse Orbit",
            body = "One-tap timing loop. Break through the ring gap and build combo.",
            highScore = stats.firstOrNull { it.gameId.name == "PULSE_ORBIT" }?.highScore ?: 0,
            onPlay = onPulseOrbit,
        )
        GameEntryCard(
            title = "Lane Drift",
            body = "Dodge blockers, collect shards, and survive the rising speed.",
            highScore = stats.firstOrNull { it.gameId.name == "LANE_DRIFT" }?.highScore ?: 0,
            onPlay = onLaneDrift,
        )
        GameEntryCard(
            title = "Stack Drop",
            body = "Classic falling-block tension with clean controls and line clears.",
            highScore = stats.firstOrNull { it.gameId.name == "STACK_DROP" }?.highScore ?: 0,
            onPlay = onStackDrop,
        )

        SectionHeader(title = "Arcade", subtitle = "Shared progression across every run.")
        ArcadeCard {
            StatRow("Selected theme", profile.selectedThemeId.replace('_', ' ').replaceFirstChar { it.uppercase() })
            StatRow("Total sessions", stats.sumOf { it.sessionsPlayed }.toString())
            StatRow("Today’s completed challenges", todayChallenges.count { it.completed }.toString())
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = onChallenges, modifier = Modifier.weight(1f)) { Text("Challenges") }
                OutlinedButton(onClick = onStats, modifier = Modifier.weight(1f)) { Text("Stats") }
                OutlinedButton(onClick = onSettings, modifier = Modifier.weight(1f)) { Text("Settings") }
            }
        }
    }
}

@Composable
private fun GameEntryCard(
    title: String,
    body: String,
    highScore: Int,
    onPlay: () -> Unit,
) {
    ArcadeCard(modifier = Modifier.padding(top = 8.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        StatRow("Best score", highScore.toString())
        Button(onClick = onPlay, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text("Play $title")
        }
    }
}
