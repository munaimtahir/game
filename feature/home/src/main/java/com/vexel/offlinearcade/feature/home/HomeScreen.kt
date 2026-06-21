package com.vexel.offlinearcade.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
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
    )

    ArcadeScaffold(
        title = "Arcade Home",
        scrollable = false,
        screenTestTag = ArcadeTestTags.HomeScreen,
        coins = profile.coins,
        streak = profile.currentStreakDays,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .testTag(ArcadeTestTags.HomeList),
            contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            item(span = { GridItemSpan(2) }) {
                HeroPanel(
                    overline = "Calm focus arcade",
                    title = "Three games. One premium shell.",
                    subtitle = "Fast offline runs, shared progression, and instant retries.",
                )
            }

            item(span = { GridItemSpan(2) }) {
                SectionHeader(
                    title = "Arcade Library",
                    badge = "3 games",
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
                SectionHeader(
                    title = "Quick Actions",
                    subtitle = "$completedChallenges/${todayChallenges.size} daily tasks complete",
                )
            }

            item(span = { GridItemSpan(2) }) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
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

            item(span = { GridItemSpan(2) }) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                    PremiumButton(
                        label = "Market",
                        onClick = onMarketplace,
                        style = ArcadeButtonStyle.Primary,
                        modifier = Modifier.weight(1f).height(56.dp).testTag(ArcadeTestTags.MarketplaceEntry),
                    )
                    PremiumButton(
                        label = "Settings",
                        onClick = onSettings,
                        style = ArcadeButtonStyle.Secondary,
                        modifier = Modifier.weight(1f).height(56.dp).testTag(ArcadeTestTags.SettingsEntry),
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
    val borderColor = if (isFeatured) accent.color else accent.color.copy(alpha = 0.35f)
    val borderWidth = if (isFeatured) 2.dp else 1.dp
    val context = LocalContext.current
    val iconName = when (title) {
        "Pulse Orbit" -> "icon_pulse_orbit"
        "Lane Drift" -> "icon_lane_drift"
        "Stack Drop" -> "icon_stack_drop"
        else -> null
    }
    val iconResId = remember(context, iconName) {
        if (iconName != null) {
            context.resources.getIdentifier(iconName, "drawable", context.packageName)
        } else {
            0
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 312.dp)
            .clip(RoundedCornerShape(20.dp))
            .semantics(mergeDescendants = true) {
                role = Role.Button
                contentDescription = "Play $title"
            }
            .testTag(testTag)
            .clickable(role = Role.Button, onClickLabel = "Play $title") { onPlay() },
        color = ArcadeTheme.colors.elevatedCardBackground,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(borderWidth, borderColor),
        shadowElevation = if (isFeatured) 8.dp else 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.18f)
                    .background(Color(0xFF181A20))
            ) {
                if (iconResId != 0) {
                    Image(
                        painter = painterResource(id = iconResId),
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            title.take(2).uppercase(),
                            color = accent.color,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                if (highScore > 0) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black.copy(alpha = 0.65f))
                            .border(1.dp, accent.color.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text("🏆", fontSize = 10.sp)
                        Text(
                            text = highScore.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.50f))
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = ArcadeTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArcadeTheme.colors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(accent.color.copy(alpha = 0.94f))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Play",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = adaptiveTextColor(accent.color),
                    )
                }
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
