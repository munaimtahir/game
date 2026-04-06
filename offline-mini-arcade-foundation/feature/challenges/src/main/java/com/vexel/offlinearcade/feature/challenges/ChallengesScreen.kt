package com.vexel.offlinearcade.feature.challenges

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.vexel.offlinearcade.core.ui.AppScaffold

@Composable
fun ChallengesScreen(onBack: () -> Unit) {
    AppScaffold(title = "Daily Challenges", onBack = onBack) {
        Text("Offline-seeded daily challenges will live here.")
    }
}
