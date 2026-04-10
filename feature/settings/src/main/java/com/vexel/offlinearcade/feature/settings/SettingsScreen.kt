package com.vexel.offlinearcade.feature.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.vexel.offlinearcade.core.ui.AppScaffold

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    AppScaffold(title = "Settings", onBack = onBack) {
        Text("Sound, music, vibration, and low-friction settings will live here.")
    }
}
