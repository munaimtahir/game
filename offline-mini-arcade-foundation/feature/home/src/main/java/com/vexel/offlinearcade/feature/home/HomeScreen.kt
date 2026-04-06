package com.vexel.offlinearcade.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.ui.AppScaffold

@Composable
fun HomeScreen(
    onPulseOrbit: () -> Unit,
    onLaneDrift: () -> Unit,
    onStackDrop: () -> Unit,
    onChallenges: () -> Unit,
    onStats: () -> Unit,
    onSettings: () -> Unit,
) {
    AppScaffold(title = "Offline Mini Arcade") {
        Text("Fast, offline, low-friction arcade.")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onPulseOrbit, modifier = Modifier.fillMaxWidth()) { Text("Play Pulse Orbit") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onLaneDrift, modifier = Modifier.fillMaxWidth()) { Text("Play Lane Drift") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onStackDrop, modifier = Modifier.fillMaxWidth()) { Text("Play Stack Drop") }
        Spacer(Modifier.height(24.dp))
        Button(onClick = onChallenges, modifier = Modifier.fillMaxWidth()) { Text("Daily Challenges") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onStats, modifier = Modifier.fillMaxWidth()) { Text("Stats") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onSettings, modifier = Modifier.fillMaxWidth()) { Text("Settings") }
    }
}
