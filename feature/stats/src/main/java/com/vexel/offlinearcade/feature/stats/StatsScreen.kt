package com.vexel.offlinearcade.feature.stats

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.vexel.offlinearcade.core.ui.AppScaffold

@Composable
fun StatsScreen(onBack: () -> Unit) {
    AppScaffold(title = "Stats", onBack = onBack) {
        Text("Shared and per-game local stats will live here.")
    }
}
