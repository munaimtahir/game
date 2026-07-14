package com.vexel.offlinearcade.feature.challenges

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
import com.vexel.offlinearcade.core.model.DailyChallenge
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.ui.ArcadeButtonStyle
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.EdgeToEdgeAppScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.PremiumBadge
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.PremiumProgress
import com.vexel.offlinearcade.core.ui.PremiumStatTile
import com.vexel.offlinearcade.core.ui.SectionHeader
import com.vexel.offlinearcade.core.ui.StatRow
import com.vexel.offlinearcade.core.ui.gameAccentFor

@Composable
fun ChallengesScreen(
    challenges: List<DailyChallenge>,
    coins: Int,
    streak: Int,
    onPlayGame: (GameId) -> Unit,
    onBack: () -> Unit,
) {
    val completeCount = challenges.count { it.completed }
    val bundleChallenge = challenges.firstOrNull { it.gameId == null }
    val gameChallenges = challenges.filter { it.gameId != null }
    EdgeToEdgeAppScaffold(
        title = "Daily Challenges",
        onBack = onBack,
        screenTestTag = ArcadeTestTags.ChallengesScreen,
        coins = coins,
        streak = streak,
    ) {
        SectionHeader(
            title = "Daily Circuit",
            subtitle = "Offline-seeded challenge cards with shared rewards and clean progression states.",
            badge = "${completeCount}/${challenges.size}",
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            PremiumStatTile("Completed", completeCount.toString(), modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.success)
            PremiumStatTile("Reward Pool", "${challenges.sumOf { it.rewardCoins }}", modifier = Modifier.weight(1f), accent = ArcadeTheme.colors.reward)
        }
        if (bundleChallenge != null) {
            val bundleProgress = if (bundleChallenge.targetValue == 0) 0f else bundleChallenge.progress.toFloat() / bundleChallenge.targetValue.toFloat()
            ArcadeCard(accent = gameAccentFor("Pulse Orbit").brush) {
                PremiumBadge(
                    text = "Arcade master challenge",
                    color = if (bundleChallenge.completed) ArcadeTheme.colors.success else ArcadeTheme.colors.premium,
                )
                Text(bundleChallenge.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text(bundleChallenge.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                PremiumProgress(progress = bundleProgress, label = "Bundle progress", accent = ArcadeTheme.colors.reward)
                StatRow("Reward", "${bundleChallenge.rewardCoins} coins", valueColor = ArcadeTheme.colors.reward)
            }
        }
        if (gameChallenges.isNotEmpty()) {
            SectionHeader(
                title = "Game Tracks",
                subtitle = "Featured bundle first, then a tighter two-card sweep for each game lane.",
                badge = "${gameChallenges.count { it.completed }}/${gameChallenges.size}",
            )
            gameChallenges.chunked(2).forEach { rowChallenges ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    rowChallenges.forEach { challenge ->
                        GameChallengeCard(
                            challenge = challenge,
                            onPlayGame = onPlayGame,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (rowChallenges.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun GameChallengeCard(
    challenge: DailyChallenge,
    onPlayGame: (GameId) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gameId = challenge.gameId ?: return
    val accent = gameAccentFor(gameId.title)
    val progressFraction = if (challenge.targetValue == 0) 0f else challenge.progress.toFloat() / challenge.targetValue.toFloat()
    ArcadeCard(modifier = modifier, accent = accent.brush) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(gameId.title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(challenge.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            }
            PremiumBadge(
                text = if (challenge.completed) "Completed" else "Active",
                color = if (challenge.completed) ArcadeTheme.colors.success else accent.color,
            )
        }
        PremiumProgress(progress = progressFraction, label = "Progress", accent = accent.color)
        StatRow("Current", "${challenge.progress}/${challenge.targetValue}")
        StatRow("Reward", "${challenge.rewardCoins} coins", valueColor = ArcadeTheme.colors.reward)
        PremiumButton(
            label = if (challenge.completed) "Play Again" else "Continue",
            onClick = { onPlayGame(gameId) },
            modifier = Modifier.fillMaxWidth(),
            style = if (challenge.completed) ArcadeButtonStyle.Secondary else ArcadeButtonStyle.Primary,
        )
    }
}
