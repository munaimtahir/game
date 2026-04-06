package com.vexel.offlinearcade.game.pulseorbit

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.vexel.offlinearcade.core.ui.AppScaffold

@Composable
fun PulseOrbitScreen(onBack: () -> Unit) {
    AppScaffold(title = "Pulse Orbit", onBack = onBack) {
        Text("Phase 2: one-tap reflex vertical slice.")
    }
}
