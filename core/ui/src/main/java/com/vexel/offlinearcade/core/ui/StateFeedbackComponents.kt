package com.vexel.offlinearcade.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.GameId

enum class ArcadeStateTone {
    Ready,
    Success,
    Failure,
    Reward,
    NewBest,
}

private data class StateToneStyle(
    val container: androidx.compose.ui.graphics.Color,
    val border: androidx.compose.ui.graphics.Color,
    val accent: androidx.compose.ui.graphics.Color,
    val icon: ImageVector,
)

@Composable
private fun toneStyle(tone: ArcadeStateTone): StateToneStyle {
    val colors = ArcadeTheme.colors
    return when (tone) {
        ArcadeStateTone.Ready -> StateToneStyle(
            container = colors.readyContainer,
            border = colors.outlineMuted,
            accent = colors.premium,
            icon = Icons.Filled.Info,
        )
        ArcadeStateTone.Success -> StateToneStyle(
            container = colors.successContainer,
            border = colors.success.copy(alpha = 0.35f),
            accent = colors.success,
            icon = Icons.Filled.CheckCircle,
        )
        ArcadeStateTone.Failure -> StateToneStyle(
            container = colors.dangerContainer,
            border = colors.danger.copy(alpha = 0.35f),
            accent = colors.danger,
            icon = Icons.Filled.Close,
        )
        ArcadeStateTone.Reward -> StateToneStyle(
            container = colors.rewardContainer,
            border = colors.reward.copy(alpha = 0.35f),
            accent = colors.reward,
            icon = Icons.Filled.Star,
        )
        ArcadeStateTone.NewBest -> StateToneStyle(
            container = colors.newBestContainer,
            border = colors.premium.copy(alpha = 0.35f),
            accent = colors.premium,
            icon = Icons.Filled.Star,
        )
    }
}

@Composable
fun StateBadge(
    label: String,
    tone: ArcadeStateTone,
    modifier: Modifier = Modifier,
) {
    val style = toneStyle(tone)
    Row(
        modifier = modifier
            .semantics { contentDescription = label }
            .background(style.container, RoundedCornerShape(999.dp))
            .border(1.dp, style.border, RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = style.icon,
            contentDescription = null,
            tint = style.accent,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = ArcadeTheme.colors.textPrimary,
        )
    }
}

@Composable
fun ReadyCueCard(
    gameId: GameId,
    title: String,
    subtitle: String,
    startLabel: String,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
    secondaryLabel: String? = null,
    onSecondaryAction: (() -> Unit)? = null,
    startTestTag: String? = null,
) {
    val accent = gameAccentFor(gameId.title)
    ArcadeCard(
        modifier = modifier.semantics { contentDescription = "$title. $subtitle" },
        accent = accent.brush,
        contentPadding = ArcadeTheme.spacing.md,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            StateBadge(label = "Ready", tone = ArcadeStateTone.Ready)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = ArcadeTheme.colors.textPrimary,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArcadeTheme.colors.textSecondary,
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (onSecondaryAction != null && secondaryLabel != null) {
                    PremiumButton(
                        label = secondaryLabel,
                        onClick = onSecondaryAction,
                        modifier = Modifier.weight(1f),
                        style = ArcadeButtonStyle.Secondary,
                    )
                }
                PremiumButton(
                    label = startLabel,
                    onClick = onStart,
                    modifier = Modifier
                        .weight(if (onSecondaryAction != null && secondaryLabel != null) 1f else 1f)
                        .then(if (startTestTag != null) Modifier.testTag(startTestTag) else Modifier),
                )
            }
        }
    }
}
