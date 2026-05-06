package com.vexel.offlinearcade.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.DailyChallenge
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.PlayerProfile
import com.vexel.offlinearcade.core.ui.ArcadeButtonStyle
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeInlineActions
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.HeroPanel
import com.vexel.offlinearcade.core.ui.HudPill
import com.vexel.offlinearcade.core.ui.PremiumBadge
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.PremiumProgress
import com.vexel.offlinearcade.core.ui.PremiumStatTile
import com.vexel.offlinearcade.core.ui.SectionHeader
import com.vexel.offlinearcade.core.ui.gameAccentFor

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
    val spacing = ArcadeTheme.spacing
    val completedChallenges = todayChallenges.count { it.completed }
    val totalSessions = stats.sumOf { it.sessionsPlayed }
    val totalScore = stats.sumOf { it.totalScore }
    val continueGame = stats.maxByOrNull { it.sessionsPlayed.takeIf { count -> count > 0 } ?: -1 }?.gameId ?: GameId.PULSE_ORBIT
    
    val gameCards = listOf(
        HomeGameEntry(
            gameId = GameId.PULSE_ORBIT,
            title = "Pulse Orbit",
            body = "Rhythmic precision under pressure. Build combo and thread the opening cleanly.",
            highScore = stats.firstOrNull { it.gameId == GameId.PULSE_ORBIT }?.highScore ?: 0,
            sessions = stats.firstOrNull { it.gameId == GameId.PULSE_ORBIT }?.sessionsPlayed ?: 0,
            challenge = todayChallenges.firstOrNull { it.gameId == GameId.PULSE_ORBIT },
            onPlay = onPulseOrbit,
            testTag = ArcadeTestTags.PulseOrbitEntry,
        ),
        HomeGameEntry(
            gameId = GameId.LANE_DRIFT,
            title = "Lane Drift",
            body = "One-hand dodge flow with shard pickups, lane reads, and instant retry tension.",
            highScore = stats.firstOrNull { it.gameId == GameId.LANE_DRIFT }?.highScore ?: 0,
            sessions = stats.firstOrNull { it.gameId == GameId.LANE_DRIFT }?.sessionsPlayed ?: 0,
            challenge = todayChallenges.firstOrNull { it.gameId == GameId.LANE_DRIFT },
            onPlay = onLaneDrift,
            testTag = ArcadeTestTags.LaneDriftEntry,
        ),
        HomeGameEntry(
            gameId = GameId.STACK_DROP,
            title = "Stack Drop",
            body = "Falling-block clarity with tactical pace, line clears, and fast gesture control.",
            highScore = stats.firstOrNull { it.gameId == GameId.STACK_DROP }?.highScore ?: 0,
            sessions = stats.firstOrNull { it.gameId == GameId.STACK_DROP }?.sessionsPlayed ?: 0,
            challenge = todayChallenges.firstOrNull { it.gameId == GameId.STACK_DROP },
            onPlay = onStackDrop,
            testTag = ArcadeTestTags.StackDropEntry,
        ),
    )
    val featuredGame = gameCards.firstOrNull { it.gameId == continueGame } ?: gameCards.first()
    val supportingGames = gameCards.filterNot { it.gameId == featuredGame.gameId }

    ArcadeScaffold(
        title = "Offline Mini Arcade",
        scrollable = false,
        screenTestTag = ArcadeTestTags.HomeScreen,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(ArcadeTestTags.HomeList),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            item {
                HeroPanel(
                    overline = "Calm Focus Arcade",
                    title = "Three games. One premium shell.",
                    subtitle = "Fast offline runs, shared progression, and instant retries without clutter.",
                    trailing = {
                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                            PremiumBadge(
                                text = if (profile.premiumUnlocked) "Premium unlocked" else "Offline-first",
                                color = if (profile.premiumUnlocked) ArcadeTheme.colors.premium else ArcadeTheme.colors.success,
                            )
                            PremiumBadge(
                                text = "Continue state: ${continueGame.title}",
                                color = ArcadeTheme.colors.reward,
                            )
                        }
                    },
                )
            }
            item {
                BoxWithConstraints {
                    val isCompact = maxWidth < 380.dp
                    ArcadeCard {
                        if (isCompact) {
                            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm), modifier = Modifier.fillMaxWidth()) {
                                    HudPill(label = "Coins", value = profile.coins.toString(), modifier = Modifier.weight(1f))
                                    HudPill(label = "Streak", value = "${profile.currentStreakDays}d", modifier = Modifier.weight(1f))
                                }
                                HudPill(label = "Daily", value = "$completedChallenges/${todayChallenges.size}", modifier = Modifier.fillMaxWidth())
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                            ) {
                                HudPill(label = "Coins", value = profile.coins.toString(), modifier = Modifier.weight(1f))
                                HudPill(label = "Streak", value = "${profile.currentStreakDays}d", modifier = Modifier.weight(1f))
                                HudPill(label = "Daily", value = "$completedChallenges/${todayChallenges.size}", modifier = Modifier.weight(1f))
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm), modifier = Modifier.fillMaxWidth()) {
                            PremiumStatTile("Sessions", totalSessions.toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.success)
                            PremiumStatTile("Total Score", totalScore.toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.reward)
                        }
                    }
                }
            }
            item {
                SectionHeader(
                    title = "Arcade Library",
                    subtitle = "Your recent focus up top.",
                    badge = "3 games",
                )
            }
            item {
                GameEntryCard(
                    title = featuredGame.title,
                    body = featuredGame.body,
                    highScore = featuredGame.highScore,
                    sessions = featuredGame.sessions,
                    challenge = featuredGame.challenge,
                    featured = true,
                    onPlay = featuredGame.onPlay,
                    testTag = featuredGame.testTag,
                )
            }
            items(supportingGames.size) { index ->
                val game = supportingGames[index]
                GameEntryCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = game.title,
                    body = game.body,
                    highScore = game.highScore,
                    sessions = game.sessions,
                    challenge = game.challenge,
                    featured = false,
                    compact = true,
                    onPlay = game.onPlay,
                    testTag = game.testTag,
                )
            }
            item {
                SectionHeader(title = "Arcade Meta", subtitle = "Daily tasks and settings stay secondary to play.")
            }
            item {
                ArcadeCard {
                    ArcadeInlineActions {
                        PremiumButton(
                            label = "Daily Challenges",
                            onClick = onChallenges,
                            modifier = Modifier.testTag(ArcadeTestTags.ChallengesEntry),
                        )
                        PremiumButton(
                            label = "Stats",
                            onClick = onStats,
                            style = ArcadeButtonStyle.Secondary,
                            modifier = Modifier.testTag(ArcadeTestTags.StatsEntry),
                        )
                        PremiumButton(
                            label = "Settings",
                            onClick = onSettings,
                            style = ArcadeButtonStyle.Secondary,
                            modifier = Modifier.testTag(ArcadeTestTags.SettingsEntry),
                        )
                    }
                    PremiumProgress(
                        progress = if (todayChallenges.isEmpty()) 0f else completedChallenges.toFloat() / todayChallenges.size.toFloat(),
                        label = "Daily completion",
                        accent = ArcadeTheme.colors.reward,
                    )
                }
            }
        }
    }
}

@Composable
private fun GameEntryCard(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    highScore: Int,
    sessions: Int,
    challenge: DailyChallenge?,
    featured: Boolean,
    compact: Boolean = false,
    onPlay: () -> Unit,
    testTag: String,
) {
    val accent = gameAccentFor(title)
    val accentTextColor = adaptiveTextColor(accent.color)
    ArcadeCard(modifier = modifier, accent = accent.brush) {
        Column(verticalArrangement = Arrangement.spacedBy(if (compact) 10.dp else 12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = accent.brush, shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            if (featured) "Continue run" else "Play now",
                            color = accentTextColor.copy(alpha = 0.86f),
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Text(
                            title,
                            style = if (compact) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = accentTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Best", color = accentTextColor.copy(alpha = 0.86f), style = MaterialTheme.typography.labelMedium)
                        Text(
                            highScore.toString(),
                            style = if (compact) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            color = accentTextColor,
                            maxLines = 1
                        )
                    }
                }
            }
            if (!compact) {
                Text(body, color = ArcadeTheme.colors.textSecondary)
            }
            if (compact) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Sessions", color = ArcadeTheme.colors.textSecondary, style = MaterialTheme.typography.labelMedium)
                    Text(sessions.toString(), color = ArcadeTheme.colors.textPrimary, fontWeight = FontWeight.Bold)
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PremiumStatTile(label = "Best Score", value = highScore.toString(), modifier = Modifier.weight(1f), accent = accent.color)
                    PremiumStatTile(label = "Sessions", value = sessions.toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.reward)
                }
            }
            if (challenge != null) {
                PremiumProgress(
                    progress = if (challenge.targetValue == 0) 0f else challenge.progress.toFloat() / challenge.targetValue.toFloat(),
                    label = if (compact) "Daily" else "Daily challenge",
                    accent = accent.color,
                )
            }
            PremiumButton(
                label = if (featured) "Play" else "Play",
                onClick = onPlay,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag(testTag),
                style = if (compact) ArcadeButtonStyle.Secondary else ArcadeButtonStyle.Primary,
            )
        }
    }
}

private data class HomeGameEntry(
    val gameId: GameId,
    val title: String,
    val body: String,
    val highScore: Int,
    val sessions: Int,
    val challenge: DailyChallenge?,
    val onPlay: () -> Unit,
    val testTag: String,
)

private fun adaptiveTextColor(background: Color): Color =
    if (background.luminance() > 0.5f) Color(0xFF0F172A) else Color(0xFFF8FAFC)
