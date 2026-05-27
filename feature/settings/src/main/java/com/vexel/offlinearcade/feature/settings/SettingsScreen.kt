package com.vexel.offlinearcade.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.vexel.offlinearcade.core.model.SettingsState
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.PremiumBadge
import com.vexel.offlinearcade.core.ui.SectionHeader
import com.vexel.offlinearcade.core.ui.StatRow

@Composable
fun SettingsScreen(
    settings: SettingsState,
    premiumUnlocked: Boolean,
    onToggleSound: (Boolean) -> Unit,
    onToggleMusic: (Boolean) -> Unit,
    onToggleVibration: (Boolean) -> Unit,
    onToggleReducedEffects: (Boolean) -> Unit,
    onToggleHighContrast: (Boolean) -> Unit,
    onBack: () -> Unit,
) {
    ArcadeScaffold(
        title = "Settings",
        onBack = onBack,
        screenTestTag = ArcadeTestTags.SettingsScreen,
    ) {
        SectionHeader(
            title = "Control Cluster",
            subtitle = "Dense control rows keep behavior tuning fast without turning this into an admin wall.",
        )
        ArcadeCard {
            Text("Audio", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            ToggleRow(
                label = "Sound",
                checked = settings.soundEnabled,
                onCheckedChange = onToggleSound,
                modifier = Modifier.testTag(ArcadeTestTags.SoundToggle),
            )
            ToggleRow(
                label = "Music",
                checked = settings.musicEnabled,
                onCheckedChange = onToggleMusic,
                modifier = Modifier.testTag(ArcadeTestTags.MusicToggle),
            )
            ToggleRow(
                label = "Vibration",
                checked = settings.vibrationEnabled,
                onCheckedChange = onToggleVibration,
                modifier = Modifier.testTag(ArcadeTestTags.VibrationToggle),
            )
            Text("Accessibility", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            ToggleRow(
                label = "Battery saver / low effects",
                checked = settings.reducedEffects,
                onCheckedChange = onToggleReducedEffects,
            )
            ToggleRow(
                label = "High contrast",
                checked = settings.highContrastEnabled,
                onCheckedChange = onToggleHighContrast,
            )
        }

        SectionHeader(title = "Premium Layer", subtitle = "Believable value without fake-online friction.")
        ArcadeCard {
            PremiumBadge(
                text = if (premiumUnlocked) "Premium active" else "Premium shell ready",
                color = if (premiumUnlocked) ArcadeTheme.colors.premium else ArcadeTheme.colors.reward,
            )
            Text(
                "Core play stays fully offline. Premium is a cosmetic and presentation layer, not a blocker for arcade runs.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            StatRow("Accounts required", "No")
            StatRow("Online dependency", "None")
        }
    }
}

@Composable
private fun ToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
