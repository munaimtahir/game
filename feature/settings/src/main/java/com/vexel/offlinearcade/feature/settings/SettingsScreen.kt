package com.vexel.offlinearcade.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
    ArcadeScaffold(
        title = "Settings",
        onBack = onBack,
        screenTestTag = ArcadeTestTags.SettingsScreen,
    ) {
        SectionHeader(
            title = "Audio and Feel",
            subtitle = "Keep the shell responsive, readable, and low-friction on real devices.",
        )
        ToggleCard(
            label = "Sound",
            description = "Short feedback cues for taps, clears, and misses.",
            checked = settings.soundEnabled,
            onCheckedChange = onToggleSound,
            modifier = Modifier.testTag(ArcadeTestTags.SoundToggle),
        )
        ToggleCard(
            label = "Music",
            description = "Reserved for future ambient layers without affecting offline play.",
            checked = settings.musicEnabled,
            onCheckedChange = onToggleMusic,
            modifier = Modifier.testTag(ArcadeTestTags.MusicToggle),
        )
        ToggleCard(
            label = "Vibration",
            description = "Haptic confirmation for clean hits, pickups, and failure.",
            checked = settings.vibrationEnabled,
            onCheckedChange = onToggleVibration,
            modifier = Modifier.testTag(ArcadeTestTags.VibrationToggle),
        )

        SectionHeader(
            title = "Accessibility and Power",
            subtitle = "Keep the premium shell restrained on weak hardware or lower-strain sessions.",
        )
        ToggleCard(
            label = "Battery saver / low effects",
            description = "Reduces glow, trims splash timing, and keeps surfaces flatter.",
            checked = settings.reducedEffects,
            onCheckedChange = onToggleReducedEffects,
        )
        ToggleCard(
            label = "High contrast",
            description = "Pushes text and outlines harder for cleaner readability.",
            checked = settings.highContrastEnabled,
            onCheckedChange = onToggleHighContrast,
        )

        SectionHeader(title = "Cosmetic Themes", subtitle = "Themes keep one shared midnight shell while shifting the accent mood.")
        themes.forEach { theme ->
            val actionLabel = when {
                theme.id == selectedThemeId -> "Selected"
                theme.unlocked -> "Use Theme"
                theme.premiumOnly && !premiumUnlocked -> "Premium"
                else -> "Unlock"
            }
            ArcadeCard(
                accent = when (theme.id) {
                    "sunset_shift" -> Brush.linearGradient(listOf(Color(0xFF8B74FF), Color(0xFFFFA55B)))
                    "ice_grid" -> Brush.linearGradient(listOf(Color(0xFF35D6D0), Color(0xFF5E88FF)))
                    else -> Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary))
                },
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(theme.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        Text(theme.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    PremiumBadge(
                        text = if (theme.coinCost == 0) "Included" else "${theme.coinCost} coins",
                        color = if (theme.coinCost == 0) ArcadeTheme.colors.success else ArcadeTheme.colors.reward,
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(78.dp)
                        .background(
                            brush = when (theme.id) {
                                "sunset_shift" -> Brush.linearGradient(listOf(Color(0xFF141B2D), Color(0xFF8B74FF), Color(0xFFFFA55B)))
                                "ice_grid" -> Brush.linearGradient(listOf(Color(0xFF0B1020), Color(0xFF35D6D0), Color(0xFF5E88FF)))
                                else -> Brush.linearGradient(listOf(Color(0xFF0B1020), Color(0xFF7C5CFF), Color(0xFF35D6D0)))
                            },
                            shape = RoundedCornerShape(22.dp),
                        ),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = "Midnight Glow Arcade",
                        modifier = Modifier.padding(horizontal = 18.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
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
            StatRow("Selected theme", themes.firstOrNull { it.id == selectedThemeId }?.title ?: "Midnight Glow")
        }
    }
}

@Composable
private fun ToggleCard(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    ArcadeCard(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
