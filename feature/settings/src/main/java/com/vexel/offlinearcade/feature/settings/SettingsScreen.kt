package com.vexel.offlinearcade.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.SettingsState
import com.vexel.offlinearcade.core.model.ThemeUnlock
import com.vexel.offlinearcade.core.ui.ArcadeButtonStyle
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.PremiumBadge
import com.vexel.offlinearcade.core.ui.PremiumButton
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
    onToggleReducedEffects: (Boolean) -> Unit,
    onToggleHighContrast: (Boolean) -> Unit,
    onSelectTheme: (String) -> Unit,
    onUnlockTheme: (String) -> Unit,
    onBack: () -> Unit,
) {
    val selectedTheme = themes.firstOrNull { it.id == selectedThemeId }
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

        SectionHeader(
            title = "Theme Showcase",
            subtitle = "One featured preview block, then compact picks for quick swaps.",
        )
        ArcadeCard(accent = themeAccentBrush(selectedTheme?.id ?: "default")) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(selectedTheme?.title ?: "", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                    Text("Selected Theme", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                PremiumBadge(
                    text = if (selectedTheme?.coinCost == 0) "Included" else "${selectedTheme?.coinCost ?: 0} coins",
                    color = if (selectedTheme?.coinCost == 0) ArcadeTheme.colors.success else ArcadeTheme.colors.reward,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                .background(
                    brush = themePreviewBrush(selectedTheme?.id ?: "default"),
                    shape = RoundedCornerShape(22.dp),
                    ),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = selectedTheme?.title ?: "",
                    modifier = Modifier.padding(horizontal = 18.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ArcadeTheme.colors.textInverse,
                )
            }
            Text(
                selectedTheme?.subtitle ?: "",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        themes.chunked(2).forEach { rowThemes ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowThemes.forEach { theme ->
                    ThemeChoiceCard(
                        theme = theme,
                        selectedThemeId = selectedThemeId,
                        premiumUnlocked = premiumUnlocked,
                        onSelectTheme = onSelectTheme,
                        onUnlockTheme = onUnlockTheme,
                        modifier = Modifier.weight(1f),
                    )
                }
                if (rowThemes.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
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
            StatRow("Selected theme", selectedTheme?.title ?: "")
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

@Composable
private fun ThemeChoiceCard(
    theme: ThemeUnlock,
    selectedThemeId: String,
    premiumUnlocked: Boolean,
    onSelectTheme: (String) -> Unit,
    onUnlockTheme: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val actionLabel = when {
        theme.id == selectedThemeId -> "Selected"
        theme.unlocked -> "Use Theme"
        theme.premiumOnly && !premiumUnlocked -> "Premium"
        else -> "Unlock"
    }
    ArcadeCard(modifier = modifier, accent = themeAccentBrush(theme.id)) {
        PremiumBadge(
            text = if (theme.coinCost == 0) "Included" else "${theme.coinCost} coins",
            color = if (theme.coinCost == 0) ArcadeTheme.colors.success else ArcadeTheme.colors.reward,
        )
        Text(theme.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .background(
                    brush = themePreviewBrush(theme.id),
                    shape = RoundedCornerShape(18.dp),
                ),
        )
        PremiumButton(
            label = actionLabel,
            onClick = {
                if (theme.unlocked) onSelectTheme(theme.id) else onUnlockTheme(theme.id)
            },
            modifier = Modifier.fillMaxWidth(),
            style = if (actionLabel == "Use Theme") ArcadeButtonStyle.Primary else ArcadeButtonStyle.Secondary,
            enabled = actionLabel != "Selected" && actionLabel != "Premium",
        )
    }
}

@Composable
private fun themeAccentBrush(themeId: String): Brush {
    val colors = ArcadeTheme.colors
    return when (themeId) {
        "sunset_shift" -> Brush.linearGradient(listOf(colors.premium, colors.reward))
        "ice_grid" -> Brush.linearGradient(listOf(colors.playerAccent, colors.laneAccent))
        else -> Brush.linearGradient(listOf(colors.premium, colors.player))
    }
}

@Composable
private fun themePreviewBrush(themeId: String): Brush {
    val colors = ArcadeTheme.colors
    return when (themeId) {
        "sunset_shift" -> Brush.linearGradient(listOf(colors.elevatedCardBackground, colors.premium, colors.reward))
        "ice_grid" -> Brush.linearGradient(listOf(colors.elevatedCardBackground, colors.playerAccent, colors.laneAccent))
        else -> Brush.linearGradient(listOf(colors.elevatedCardBackground, colors.premium, colors.player))
    }
}
