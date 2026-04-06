package com.vexel.offlinearcade.game.lanedrift

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.vexel.offlinearcade.core.ui.AppScaffold

@Composable
fun LaneDriftScreen(onBack: () -> Unit) {
    AppScaffold(title = "Lane Drift", onBack = onBack) {
        Text("Phase 3: dodge-and-collect vertical slice.")
    }
}
