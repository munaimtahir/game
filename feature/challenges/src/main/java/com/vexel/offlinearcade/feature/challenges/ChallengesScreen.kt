package com.vexel.offlinearcade.feature.challenges

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.DailyChallenge
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
fun ChallengesScreen(
    challenges: List<DailyChallenge>,
    onBack: () -> Unit,
) {
    val completeCount = challenges.count { it.completed }
    val bundleChallenge = challenges.firstOrNull { it.gameId == null }
    ArcadeScaffold(
        title = "Daily Challenges",
        onBack = onBack,
        screenTestTag = ArcadeTestTags.ChallengesScreen,
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
        challenges.filter { it.gameId != null }.forEach { challenge ->
            val gameId = challenge.gameId ?: return@forEach
            val accent = gameAccentFor(gameId.title)
            val progressFraction = if (challenge.targetValue == 0) 0f else challenge.progress.toFloat() / challenge.targetValue.toFloat()
            ArcadeCard(accent = accent.brush) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    ColumnBlock(
                        title = gameId.title,
                        subtitle = challenge.title,
                    )
                    PremiumBadge(
                        text = if (challenge.completed) "Completed" else "Active",
                        color = if (challenge.completed) ArcadeTheme.colors.success else accent.color,
                    )
                }
                Text(challenge.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                PremiumProgress(progress = progressFraction, label = "Challenge progress", accent = accent.color)
                StatRow("Current", "${challenge.progress}/${challenge.targetValue}")
                StatRow("Reward", "${challenge.rewardCoins} coins", valueColor = ArcadeTheme.colors.reward)
            }
        }
    }
}

@Composable
private fun ColumnBlock(title: String, subtitle: String) {
    androidx.compose.foundation.layout.Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(subtitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
    }
}
