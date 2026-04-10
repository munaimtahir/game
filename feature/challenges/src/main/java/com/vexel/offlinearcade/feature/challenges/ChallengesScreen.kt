package com.vexel.offlinearcade.feature.challenges

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.vexel.offlinearcade.core.model.DailyChallenge
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.SectionHeader
import com.vexel.offlinearcade.core.ui.StatRow

@Composable
fun ChallengesScreen(
    challenges: List<DailyChallenge>,
    onBack: () -> Unit,
) {
    ArcadeScaffold(
        title = "Daily Challenges",
        onBack = onBack,
        screenTestTag = ArcadeTestTags.ChallengesScreen,
    ) {
        SectionHeader(
            title = "Offline seeded each day",
            subtitle = "One task per game plus one arcade-wide bundle. Rewards claim automatically on completion.",
        )
        challenges.forEach { challenge ->
            val progressFraction = if (challenge.targetValue == 0) 0f else challenge.progress.toFloat() / challenge.targetValue.toFloat()
            ArcadeCard {
                Text(challenge.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(challenge.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LinearProgressIndicator(progress = { progressFraction.coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth())
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${challenge.progress}/${challenge.targetValue}")
                    Text(
                        if (challenge.completed) "Completed" else "In progress",
                        color = if (challenge.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                StatRow("Reward", "${challenge.rewardCoins} coins")
            }
        }
    }
}
