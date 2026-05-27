package com.vexel.offlinearcade.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vexel.offlinearcade.core.model.DailyChallenge
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.PlayerProfile
import com.vexel.offlinearcade.core.ui.ArcadeButtonStyle
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.HeroPanel
import com.vexel.offlinearcade.core.ui.HudPill
import com.vexel.offlinearcade.core.ui.PremiumBadge
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.SectionHeader
import com.vexel.offlinearcade.core.ui.gameAccentFor

@Composable
fun HomeScreen(
    profile: PlayerProfile,
    stats: List<GameStats>,
    todayChallenges: List<DailyChallenge>,
    onPulseOrbit: () -> Unit,
    onLaneDrift: () -> Unit,
    onStackDrop: () -> Unit,
    onBrickVolley: () -> Unit,
    onLoopSnake: () -> Unit,
    onShieldDash: () -> Unit,
    onChallenges: () -> Unit,
    onStats: () -> Unit,
    onSettings: () -> Unit,
    onMarketplace: () -> Unit,
) {
    val spacing = ArcadeTheme.spacing
    val completedChallenges = todayChallenges.count { it.completed }
    val continueGame = stats.maxByOrNull { it.sessionsPlayed.takeIf { count -> count > 0 } ?: -1 }?.gameId ?: GameId.PULSE_ORBIT
    
    val gameCards = listOf(
        HomeGameEntry(
            gameId = GameId.PULSE_ORBIT,
            title = "Pulse Orbit",
            description = "Reflex rhythm.",
            highScore = stats.find { it.gameId == GameId.PULSE_ORBIT }?.highScore ?: 0,
            onPlay = onPulseOrbit,
            testTag = ArcadeTestTags.PulseOrbitEntry,
        ),
        HomeGameEntry(
            gameId = GameId.LANE_DRIFT,
            title = "Lane Drift",
            description = "Dodge & flow.",
            highScore = stats.find { it.gameId == GameId.LANE_DRIFT }?.highScore ?: 0,
            onPlay = onLaneDrift,
            testTag = ArcadeTestTags.LaneDriftEntry,
        ),
        HomeGameEntry(
            gameId = GameId.STACK_DROP,
            title = "Stack Drop",
            description = "Fast line clear.",
            highScore = stats.find { it.gameId == GameId.STACK_DROP }?.highScore ?: 0,
            onPlay = onStackDrop,
            testTag = ArcadeTestTags.StackDropEntry,
        ),
        HomeGameEntry(
            gameId = GameId.BRICK_VOLLEY,
            title = "Brick Volley",
            description = "Ball volley fun.",
            highScore = stats.find { it.gameId == GameId.BRICK_VOLLEY }?.highScore ?: 0,
            onPlay = onBrickVolley,
            testTag = ArcadeTestTags.BrickVolleyEntry,
        ),
        HomeGameEntry(
            gameId = GameId.LOOP_SNAKE,
            title = "Loop Snake",
            description = "Modern snake.",
            highScore = stats.find { it.gameId == GameId.LOOP_SNAKE }?.highScore ?: 0,
            onPlay = onLoopSnake,
            testTag = ArcadeTestTags.LoopSnakeEntry,
        ),
        HomeGameEntry(
            gameId = GameId.SHIELD_DASH,
            title = "Shield Dash",
            description = "Defensive block.",
            highScore = stats.find { it.gameId == GameId.SHIELD_DASH }?.highScore ?: 0,
            onPlay = onShieldDash,
            testTag = ArcadeTestTags.ShieldDashEntry,
        ),
    )

    ArcadeScaffold(
        title = "Arcade Home",
        scrollable = false,
        screenTestTag = ArcadeTestTags.HomeScreen,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .testTag(ArcadeTestTags.HomeList),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            item(span = { GridItemSpan(2) }) {
                HeroPanel(
                    overline = "Daily Session",
                    title = "Keep the streak.",
                    subtitle = "Instant offline play. No ads in-game.",
                    trailing = {
                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                            PremiumBadge(
                                text = "${profile.currentStreakDays} Day Streak",
                                color = ArcadeTheme.colors.premium,
                            )
                        }
                    },
                )
            }

            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HudPill(label = "Coins", value = profile.coins.toString(), modifier = Modifier.weight(1f))
                    HudPill(label = "Daily", value = "$completedChallenges/${todayChallenges.size}", modifier = Modifier.weight(1f))
                    PremiumButton(
                        label = "Market",
                        onClick = onMarketplace,
                        style = ArcadeButtonStyle.Primary,
                        modifier = Modifier.height(52.dp).weight(0.8f),
                        labelOverride = "🛒"
                    )
                    PremiumButton(
                        label = "Settings",
                        onClick = onSettings,
                        style = ArcadeButtonStyle.Secondary,
                        modifier = Modifier.size(52.dp).testTag(ArcadeTestTags.SettingsEntry),
                        labelOverride = "⚙"
                    )
                }
            }

            item(span = { GridItemSpan(2) }) {
                SectionHeader(
                    title = "Arcade Library",
                    badge = continueGame.title,
                )
            }

            items(gameCards.size) { index ->
                val game = gameCards[index]
                GameCard(
                    title = game.title,
                    description = game.description,
                    highScore = game.highScore,
                    onPlay = game.onPlay,
                    isFeatured = game.gameId == continueGame,
                    testTag = game.testTag
                )
            }
            
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(spacing.sm))
                SectionHeader(title = "Arcade Meta", subtitle = "Shared progression & daily tasks.")
            }

            item(span = { GridItemSpan(2) }) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing.md)) {
                    PremiumButton(
                        label = "Daily Challenges",
                        onClick = onChallenges,
                        modifier = Modifier.weight(1f).height(56.dp).testTag(ArcadeTestTags.ChallengesEntry),
                        style = ArcadeButtonStyle.Primary
                    )
                    PremiumButton(
                        label = "Stats",
                        onClick = onStats,
                        modifier = Modifier.weight(1f).height(56.dp).testTag(ArcadeTestTags.StatsEntry),
                        style = ArcadeButtonStyle.Secondary
                    )
                }
            }
        }
    }
}

@Composable
private fun GameCard(
    title: String,
    description: String,
    highScore: Int,
    onPlay: () -> Unit,
    isFeatured: Boolean,
    testTag: String,
) {
    val accent = gameAccentFor(title)
    val containerColor = if (isFeatured) accent.color.copy(alpha = 0.08f) else ArcadeTheme.colors.elevatedCardBackground
    val borderColor = if (isFeatured) accent.color else ArcadeTheme.colors.outlineMuted
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(28.dp))
            .testTag(testTag)
            .clickable { onPlay() },
        color = containerColor,
        shape = RoundedCornerShape(28.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(accent.color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFeatured) Icons.Default.Star else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = accent.color,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                if (highScore > 0) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "BEST",
                            style = MaterialTheme.typography.labelSmall,
                            color = ArcadeTheme.colors.textSecondary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            highScore.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = ArcadeTheme.colors.textPrimary
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = ArcadeTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArcadeTheme.colors.textSecondary,
                    maxLines = 2,
                    lineHeight = 16.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private data class HomeGameEntry(
    val gameId: GameId,
    val title: String,
    val description: String,
    val highScore: Int,
    val onPlay: () -> Unit,
    val testTag: String,
)

private fun adaptiveTextColor(background: Color): Color =
    if (background.luminance() > 0.5f) Color(0xFF0F172A) else Color(0xFFF8FAFC)
