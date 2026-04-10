package com.vexel.offlinearcade.game.stackdrop

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.vexel.offlinearcade.core.ui.AppScaffold

@Composable
fun StackDropScreen(onBack: () -> Unit) {
    AppScaffold(title = "Stack Drop", onBack = onBack) {
        Text("Phase 4: falling-block puzzle vertical slice.")
    }
}
