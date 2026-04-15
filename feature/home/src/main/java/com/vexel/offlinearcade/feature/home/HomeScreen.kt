package com.vexel.offlinearcade.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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
import com.vexel.offlinearcade.core.ui.StatRow
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
    val continueAction = when (continueGame) {
        GameId.PULSE_ORBIT -> onPulseOrbit
        GameId.LANE_DRIFT -> onLaneDrift
        GameId.STACK_DROP -> onStackDrop
    }

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
                    overline = "Midnight Glow Arcade",
                    title = "Three games. One premium shell.",
                    subtitle = "Fast offline runs, shared progression, and instant retries without clutter.",
                    trailing = {
                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                            PremiumBadge(
                                text = if (profile.premiumUnlocked) "Premium unlocked" else "Offline-first",
                                color = if (profile.premiumUnlocked) ArcadeTheme.colors.premium else ArcadeTheme.colors.success,
                            )
                            PremiumButton(
                                label = "Continue ${continueGame.title}",
                                onClick = continueAction,
                                style = ArcadeButtonStyle.Tonal,
                            )
                        }
                    },
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                ) {
                    HudPill(label = "Coins", value = profile.coins.toString(), modifier = Modifier.weight(1f))
                    HudPill(label = "Streak", value = "${profile.currentStreakDays}d", modifier = Modifier.weight(1f))
                    HudPill(label = "Daily", value = "$completedChallenges/${todayChallenges.size}", modifier = Modifier.weight(1f))
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm), modifier = Modifier.fillMaxWidth()) {
                    PremiumStatTile("Sessions", totalSessions.toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.success)
                    PremiumStatTile("Total Score", totalScore.toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.reward)
                }
            }
            item {
                SectionHeader(
                    title = "Continue Playing",
                    subtitle = "Resume the arcade loop from your strongest lane, orbit, or stack.",
                )
            }
            item {
                ContinuePlayingCard(
                    continueGame = continueGame,
                    stats = stats.firstOrNull { it.gameId == continueGame },
                    challenge = todayChallenges.firstOrNull { it.gameId == continueGame },
                    onContinue = continueAction,
                )
            }
            item {
                SectionHeader(
                    title = "Games",
                    subtitle = "Each card keeps the same arcade shell while carrying its own energy.",
                    badge = "3 MVP",
                )
            }
            item {
                GameEntryCard(
                    title = "Pulse Orbit",
                    body = "Rhythmic precision under pressure. Build combo and thread the opening cleanly.",
                    highScore = stats.firstOrNull { it.gameId == GameId.PULSE_ORBIT }?.highScore ?: 0,
                    challenge = todayChallenges.firstOrNull { it.gameId == GameId.PULSE_ORBIT },
                    onPlay = onPulseOrbit,
                    testTag = ArcadeTestTags.PulseOrbitEntry,
                )
            }
            item {
                GameEntryCard(
                    title = "Lane Drift",
                    body = "One-hand dodge flow with shard pickups, lane reads, and instant retry tension.",
                    highScore = stats.firstOrNull { it.gameId == GameId.LANE_DRIFT }?.highScore ?: 0,
                    challenge = todayChallenges.firstOrNull { it.gameId == GameId.LANE_DRIFT },
                    onPlay = onLaneDrift,
                    testTag = ArcadeTestTags.LaneDriftEntry,
                )
            }
            item {
                GameEntryCard(
                    title = "Stack Drop",
                    body = "Falling-block clarity with tactical pace, line clears, and fast gesture control.",
                    highScore = stats.firstOrNull { it.gameId == GameId.STACK_DROP }?.highScore ?: 0,
                    challenge = todayChallenges.firstOrNull { it.gameId == GameId.STACK_DROP },
                    onPlay = onStackDrop,
                    testTag = ArcadeTestTags.StackDropEntry,
                )
            }
            item {
                SectionHeader(
                    title = "Arcade Meta",
                    subtitle = "Daily tasks, local stats, settings, and premium cosmetics stay tightly integrated.",
                )
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
private fun ContinuePlayingCard(
    continueGame: GameId,
    stats: GameStats?,
    challenge: DailyChallenge?,
    onContinue: () -> Unit,
) {
    val accent = gameAccentFor(continueGame.title)
    ArcadeCard(accent = accent.brush) {
        PremiumBadge(text = "Recommended next run", color = accent.color)
        Text(continueGame.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        Text(
            challenge?.description ?: "Jump straight back in with instant entry and shared progression.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        StatRow("Best score", (stats?.highScore ?: 0).toString())
        StatRow("Sessions", (stats?.sessionsPlayed ?: 0).toString())
        PremiumButton(label = "Play ${continueGame.title}", onClick = onContinue, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun GameEntryCard(
    title: String,
    body: String,
    highScore: Int,
    challenge: DailyChallenge?,
    onPlay: () -> Unit,
    testTag: String,
) {
    val accent = gameAccentFor(title)
    ArcadeCard(accent = accent.brush) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PremiumBadge(text = title, color = accent.color)
                Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Box(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .height(74.dp)
                    .fillMaxWidth(0.28f)
                    .background(accent.brush, shape = androidx.compose.foundation.shape.RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = highScore.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        if (challenge != null) {
            PremiumProgress(
                progress = if (challenge.targetValue == 0) 0f else challenge.progress.toFloat() / challenge.targetValue.toFloat(),
                label = "Daily challenge",
                accent = accent.color,
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Best score", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(highScore.toString(), fontWeight = FontWeight.Bold)
        }
        PremiumButton(
            label = "Play $title",
            onClick = onPlay,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag(testTag),
        )
    }
}
