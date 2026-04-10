package com.vexel.offlinearcade.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.vexel.offlinearcade.core.model.SettingsState
import com.vexel.offlinearcade.core.model.ThemeUnlock
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.SectionHeader
import com.vexel.offlinearcade.core.ui.StatRow

@Composable
fun SettingsScreen(
    settings: SettingsState,
    themes: List<ThemeUnlock>,
    selectedThemeId: String,
    premiumUnlocked: Boolean,
    onToggleSound: (Boolean) -> Unit,
    onToggleMusic: (Boolean) -> Unit,
    onToggleVibration: (Boolean) -> Unit,
    onSelectTheme: (String) -> Unit,
    onUnlockTheme: (String) -> Unit,
    onBack: () -> Unit,
) {
    ArcadeScaffold(title = "Settings", onBack = onBack) {
        SectionHeader(title = "Audio and feel", subtitle = "Simple app-wide preferences saved locally with DataStore.")
        ToggleCard(label = "Sound", checked = settings.soundEnabled, onCheckedChange = onToggleSound)
        ToggleCard(label = "Music", checked = settings.musicEnabled, onCheckedChange = onToggleMusic)
        ToggleCard(label = "Vibration", checked = settings.vibrationEnabled, onCheckedChange = onToggleVibration)

        SectionHeader(title = "Themes", subtitle = "Unlock cosmetic themes with coins. No internet required.")
        themes.forEach { theme ->
            ArcadeCard {
                Text(theme.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(theme.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                StatRow("Cost", if (theme.coinCost == 0) "Included" else "${theme.coinCost} coins")
                val actionLabel = when {
                    theme.id == selectedThemeId -> "Selected"
                    theme.unlocked -> "Use theme"
                    theme.premiumOnly && !premiumUnlocked -> "Premium"
                    else -> "Unlock"
                }
                Button(
                    onClick = {
                        if (theme.unlocked) onSelectTheme(theme.id) else onUnlockTheme(theme.id)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = actionLabel != "Selected" && actionLabel != "Premium",
                ) {
                    Text(actionLabel)
                }
            }
        }

        ArcadeCard {
            SectionHeader(title = "Offline-first MVP", subtitle = null)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Premium foundation")
                Text(if (premiumUnlocked) "Enabled" else "Placeholder only")
            }
            Text(
                "No accounts, sync, leaderboards, or online-only systems are required for core play.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ToggleCard(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    ArcadeCard {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
