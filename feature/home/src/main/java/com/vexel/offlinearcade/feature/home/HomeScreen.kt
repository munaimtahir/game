package com.vexel.offlinearcade.feature.home

import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.DailyChallenge
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.PlayerProfile
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
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
    val pulseOrbitHighScore = stats.firstOrNull { it.gameId.name == "PULSE_ORBIT" }?.highScore ?: 0
    val laneDriftHighScore = stats.firstOrNull { it.gameId.name == "LANE_DRIFT" }?.highScore ?: 0
    val stackDropHighScore = stats.firstOrNull { it.gameId.name == "STACK_DROP" }?.highScore ?: 0
    val completedChallenges = todayChallenges.count { it.completed }
    val selectedTheme = profile.selectedThemeId.replace('_', ' ').replaceFirstChar { it.uppercase() }
    val totalSessions = stats.sumOf { it.sessionsPlayed }

    ArcadeScaffold(
        title = "Offline Mini Arcade",
        scrollable = false,
        screenTestTag = ArcadeTestTags.HomeScreen,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(ArcadeTestTags.HomeList),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                HeroPanel(
                    title = "Fast offline arcade sessions",
                    subtitle = "Three focused games, shared progression, and near-instant retries.",
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    HudPill(label = "Coins", value = profile.coins.toString(), modifier = Modifier.weight(1f))
                    HudPill(label = "Streak", value = "${profile.currentStreakDays}d", modifier = Modifier.weight(1f))
                    HudPill(label = "Daily", value = "$completedChallenges/${todayChallenges.size}", modifier = Modifier.weight(1f))
                }
            }
            item {
                SectionHeader(
                    title = "Play",
                    subtitle = "Open, choose, play. Each card jumps straight into the game.",
                )
            }
            item {
                GameEntryCard(
                    title = "Pulse Orbit",
                    body = "One-tap timing loop. Break through the ring gap and build combo.",
                    highScore = pulseOrbitHighScore,
                    onPlay = onPulseOrbit,
                    testTag = ArcadeTestTags.PulseOrbitEntry,
                )
            }
            item {
                GameEntryCard(
                    title = "Lane Drift",
                    body = "Dodge blockers, collect shards, and survive the rising speed.",
                    highScore = laneDriftHighScore,
                    onPlay = onLaneDrift,
                    testTag = ArcadeTestTags.LaneDriftEntry,
                )
            }
            item {
                GameEntryCard(
                    title = "Stack Drop",
                    body = "Classic falling-block tension with clean controls and line clears.",
                    highScore = stackDropHighScore,
                    onPlay = onStackDrop,
                    testTag = ArcadeTestTags.StackDropEntry,
                )
            }
            item {
                SectionHeader(title = "Arcade", subtitle = "Shared progression across every run.")
            }
            item {
                ArcadeCard {
                    StatRow("Selected theme", selectedTheme)
                    StatRow("Total sessions", totalSessions.toString())
                    StatRow("Today’s completed challenges", completedChallenges.toString())
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = onChallenges,
                            modifier = Modifier.weight(1f).testTag(ArcadeTestTags.ChallengesEntry),
                        ) { Text("Challenges") }
                        OutlinedButton(
                            onClick = onStats,
                            modifier = Modifier.weight(1f).testTag(ArcadeTestTags.StatsEntry),
                        ) { Text("Stats") }
                        OutlinedButton(
                            onClick = onSettings,
                            modifier = Modifier.weight(1f).testTag(ArcadeTestTags.SettingsEntry),
                        ) { Text("Settings") }
                    }
                }
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
    testTag: String,
) {
    ArcadeCard(modifier = Modifier.padding(top = 8.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        StatRow("Best score", highScore.toString())
        Button(
            onClick = onPlay,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag(testTag),
        ) {
            Text("Play $title")
        }
    }
}
