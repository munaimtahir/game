package com.vexel.offlinearcade.feature.marketplace

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.SkinUnlock
import com.vexel.offlinearcade.core.model.ThemeUnlock
import com.vexel.offlinearcade.core.ui.ArcadeButtonStyle
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.HudPill
import com.vexel.offlinearcade.core.ui.PremiumBadge
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.SectionHeader

@Composable
fun MarketplaceScreen(
    coins: Int,
    themes: List<ThemeUnlock>,
    skins: List<SkinUnlock>,
    selectedThemeId: String,
    selectedPulseOrbitSkin: String,
    premiumUnlocked: Boolean,
    onSelectTheme: (String) -> Unit,
    onUnlockTheme: (String) -> Unit,
    onSelectSkin: (String, GameId) -> Unit,
    onUnlockSkin: (String) -> Unit,
    onBack: () -> Unit,
) {
    val selectedTheme = themes.firstOrNull { it.id == selectedThemeId }
    ArcadeScaffold(
        title = "Marketplace",
        onBack = onBack,
    ) {
        SectionHeader(
            title = "Your Wallet",
            subtitle = "Accumulate coins through gameplay to unlock exclusive cosmetics.",
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            HudPill(
                label = "Total Coins",
                value = coins.toString(),
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }

        SectionHeader(
            title = "Theme Showcase",
            subtitle = "Change the entire look and feel of the arcade.",
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

        SectionHeader(
            title = "Game Skins",
            subtitle = "Spend coins to customize individual games.",
        )
        val gameSkins = skins.groupBy { it.gameId }
        gameSkins.forEach { (gameId, gameSpecificSkins) ->
            Text(gameId.title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            val activeSkinId = if (gameId == GameId.PULSE_ORBIT) selectedPulseOrbitSkin else ""
            gameSpecificSkins.chunked(2).forEach { rowSkins ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    rowSkins.forEach { skin ->
                        SkinChoiceCard(
                            skin = skin,
                            selectedSkinId = activeSkinId,
                            premiumUnlocked = premiumUnlocked,
                            onSelectSkin = { onSelectSkin(it, gameId) },
                            onUnlockSkin = onUnlockSkin,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (rowSkins.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
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
private fun SkinChoiceCard(
    skin: SkinUnlock,
    selectedSkinId: String,
    premiumUnlocked: Boolean,
    onSelectSkin: (String) -> Unit,
    onUnlockSkin: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val actionLabel = when {
        skin.id == selectedSkinId -> "Selected"
        skin.unlocked -> "Equip"
        else -> "Unlock"
    }
    ArcadeCard(modifier = modifier, accent = Brush.linearGradient(listOf(ArcadeTheme.colors.outlineMuted, ArcadeTheme.colors.outlineMuted))) {
        PremiumBadge(
            text = if (skin.coinCost == 0) "Included" else "${skin.coinCost} coins",
            color = if (skin.coinCost == 0) ArcadeTheme.colors.success else ArcadeTheme.colors.reward,
        )
        Text(skin.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        PremiumButton(
            label = actionLabel,
            onClick = {
                if (skin.unlocked) onSelectSkin(skin.id) else onUnlockSkin(skin.id)
            },
            modifier = Modifier.fillMaxWidth(),
            style = if (actionLabel == "Equip") ArcadeButtonStyle.Primary else ArcadeButtonStyle.Secondary,
            enabled = actionLabel != "Selected",
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
