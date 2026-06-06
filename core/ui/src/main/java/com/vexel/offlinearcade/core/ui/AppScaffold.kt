package com.vexel.offlinearcade.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.RepeatMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class ArcadeButtonStyle { Primary, Secondary, Tonal }

data class ArcadeGameAccent(
    val label: String,
    val brush: Brush,
    val color: Color,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArcadeScaffold(
    title: String,
    onBack: (() -> Unit)? = null,
    scrollable: Boolean = true,
    resetScrollOnEnter: Boolean = false,
    screenTestTag: String? = null,
    coins: Int? = null,
    streak: Int? = null,
    actions: @Composable (() -> Unit) = {},
    content: @Composable () -> Unit,
) {
    val spacing = ArcadeTheme.spacing
    val scrollState = rememberScrollState()
    if (scrollable && resetScrollOnEnter) {
        LaunchedEffect(title) {
            scrollState.scrollTo(0)
        }
    }
    val safeBodyInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
    val bodyModifier = if (scrollable) {
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .windowInsetsPadding(safeBodyInsets)
            .padding(horizontal = spacing.lg, vertical = spacing.md)
    } else {
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(safeBodyInsets)
            .padding(horizontal = spacing.lg, vertical = spacing.md)
    }
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                    .padding(horizontal = spacing.lg, vertical = spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                ) {
                    if (onBack != null) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ArcadeTheme.colors.controlSurface)
                                .border(1.dp, ArcadeTheme.colors.controlBorder, RoundedCornerShape(12.dp))
                                .clickable(
                                    role = Role.Button,
                                    onClickLabel = "Back",
                                    onClick = onBack,
                                )
                                .semantics {
                                    role = Role.Button
                                    contentDescription = "Back"
                                    onClick(label = "Back") {
                                        onBack()
                                        true
                                    }
                                }
                                .testTag(ArcadeTestTags.BackButton),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "←", 
                                color = ArcadeTheme.colors.textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                    Text(
                        text = title, 
                        style = MaterialTheme.typography.titleLarge, 
                        fontWeight = FontWeight.Black,
                        color = ArcadeTheme.colors.textPrimary
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.xs)
                ) {
                    actions()
                    
                    if (streak != null) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(99.dp))
                                .background(ArcadeTheme.colors.premium.copy(alpha = 0.12f))
                                .border(1.dp, ArcadeTheme.colors.premium.copy(alpha = 0.3f), RoundedCornerShape(99.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("🔥", fontSize = 14.sp)
                            Text(
                                text = streak.toString(),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = ArcadeTheme.colors.textPrimary
                            )
                        }
                    }
                    
                    if (coins != null) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(99.dp))
                                .background(ArcadeTheme.colors.reward.copy(alpha = 0.12f))
                                .border(1.dp, ArcadeTheme.colors.reward.copy(alpha = 0.3f), RoundedCornerShape(99.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("🪙", fontSize = 14.sp)
                            Text(
                                text = coins.toString(),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = ArcadeTheme.colors.textPrimary
                            )
                        }
                    }
                }
            }
        },
    ) { padding ->
        val colors = ArcadeTheme.colors
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(padding)
                .testTag(screenTestTag ?: ""),
        ) {
            Column(
                modifier = bodyModifier,
                verticalArrangement = Arrangement.spacedBy(spacing.md),
            ) {
                content()
            }
        }
    }
}

@Composable
fun GameplayScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    controls: (@Composable () -> Unit)? = null,
    overlay: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val spacing = ArcadeTheme.spacing
    val colors = ArcadeTheme.colors
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.gameBackground)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            topBar()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                content()
            }
            if (controls != null) {
                controls()
            }
        }
        if (overlay != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.overlayScrim),
                contentAlignment = Alignment.Center
            ) {
                overlay()
            }
        }
    }
}

@Composable
fun ArcadeCard(
    modifier: Modifier = Modifier,
    contentPadding: androidx.compose.ui.unit.Dp = ArcadeTheme.spacing.md,
    accent: Brush? = null,
    content: @Composable () -> Unit,
) {
    val spacing = ArcadeTheme.spacing
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = ArcadeTheme.colors.elevatedCardBackground,
            contentColor = ArcadeTheme.colors.textPrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ArcadeTheme.colors.panelGradient)
                .then(
                    if (accent != null) {
                        Modifier.border(width = 1.dp, brush = accent, shape = RoundedCornerShape(28.dp))
                    } else {
                        Modifier.border(width = 1.dp, color = ArcadeTheme.colors.outlineMuted, shape = RoundedCornerShape(28.dp))
                    }
                )
                .padding(contentPadding),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                content()
            }
        }
    }
}

@Composable
fun PremiumButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ArcadeButtonStyle = ArcadeButtonStyle.Primary,
    enabled: Boolean = true,
    borderOverride: Color? = null,
    labelOverride: String? = null,
) {
    val colors = ArcadeTheme.colors
    val containerColor = if (style == ArcadeButtonStyle.Primary) MaterialTheme.colorScheme.primary else Color.Transparent
    val contentColor = if (style == ArcadeButtonStyle.Primary) MaterialTheme.colorScheme.onPrimary else colors.textPrimary

    val text = labelOverride ?: label

    when (style) {
        ArcadeButtonStyle.Primary -> Button(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier.shadow(8.dp, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = colors.cardBackground,
                disabledContentColor = colors.textMuted,
            ),
        ) {
            Text(text, style = MaterialTheme.typography.labelLarge, maxLines = 1)
        }

        ArcadeButtonStyle.Secondary -> OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, borderOverride ?: colors.controlBorder),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = colors.controlSurface,
                contentColor = colors.textPrimary,
                disabledContentColor = colors.textMuted,
            )
        ) {
            Text(text, style = MaterialTheme.typography.labelLarge, maxLines = 1)
        }

        ArcadeButtonStyle.Tonal -> Button(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier,
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = colors.textPrimary,
            ),
        ) {
            Text(text, style = MaterialTheme.typography.labelLarge, maxLines = 1)
        }
    }
}

@Composable
fun HudPill(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = ArcadeTheme.colors.hudCard,
        tonalElevation = 0.dp,
        shadowElevation = 4.dp,
    ) {
        Column(
            modifier = Modifier
                .border(1.dp, ArcadeTheme.colors.hudBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp),
        ) {
            Text(label.uppercase(), style = MaterialTheme.typography.labelMedium, color = ArcadeTheme.colors.textSecondary)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = ArcadeTheme.colors.textPrimary)
        }
    }
}

@Composable
fun PremiumBadge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = ArcadeTheme.colors.reward,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Text(text, style = MaterialTheme.typography.labelMedium, color = ArcadeTheme.colors.textPrimary)
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    badge: String? = null,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = ArcadeTheme.colors.textPrimary)
            if (badge != null) {
                PremiumBadge(text = badge, color = ArcadeTheme.colors.premium)
            }
        }
        if (subtitle != null) {
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = ArcadeTheme.colors.textSecondary)
        }
    }
}

@Composable
fun StatRow(label: String, value: String, valueColor: Color = ArcadeTheme.colors.textPrimary) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = ArcadeTheme.colors.textSecondary, style = MaterialTheme.typography.bodyMedium)
        Text(value, color = valueColor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PremiumStatTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    accent: Color = ArcadeTheme.colors.reward,
) {
    ArcadeCard(modifier = modifier, contentPadding = ArcadeTheme.spacing.md) {
        Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(accent))
        Text(label.uppercase(), style = MaterialTheme.typography.labelMedium, color = ArcadeTheme.colors.textSecondary)
        Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = ArcadeTheme.colors.textPrimary)
    }
}

@Composable
fun PremiumProgress(
    progress: Float,
    label: String,
    modifier: Modifier = Modifier,
    accent: Color = ArcadeTheme.colors.reward,
) {
    val animatedProgress by androidx.compose.animation.core.animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 800, easing = androidx.compose.animation.core.FastOutSlowInEasing),
        label = "progress"
    )
    val isComplete = progress >= 1f
    val animatedScale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isComplete) 1.05f else 1f,
        animationSpec = androidx.compose.animation.core.spring(dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy),
        label = "progress_scale"
    )

    Column(
        modifier = modifier.graphicsLayer {
            scaleX = animatedScale
            scaleY = animatedScale
        },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = ArcadeTheme.colors.textSecondary)
            Text("${(animatedProgress * 100).toInt()}%", style = MaterialTheme.typography.labelLarge, color = if (isComplete) ArcadeTheme.colors.success else ArcadeTheme.colors.textPrimary)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(999.dp))
                .background(ArcadeTheme.colors.cardBackground)
                .padding(3.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(999.dp))
                    .background(if (isComplete) ArcadeTheme.colors.success else accent)
                    .padding(vertical = 4.dp),
            )
        }
    }
}

@Composable
fun HeroPanel(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    overline: String? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    val reducedEffects = ArcadeTheme.reducedEffects
    val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
    val animatedOffset by if (reducedEffects) {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0f) }
    } else {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                animation = androidx.compose.animation.core.tween(8000, easing = androidx.compose.animation.core.LinearEasing),
                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
            )
        )
    }
    
    val baseGradient = ArcadeTheme.colors.heroGradient
    
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(baseGradient)
            .padding(24.dp),
    ) {
        // Subtle animated glow overlay
        if (!reducedEffects) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationX = animatedOffset * 0.1f
                        alpha = 0.4f
                    }
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color.White.copy(alpha = 0.2f), Color.Transparent),
                            center = androidx.compose.ui.geometry.Offset(animatedOffset, 0f),
                            radius = 800f
                        )
                    )
            )
        }
        val isCompact = maxWidth < 380.dp
        val textColor = Color.White // Hero background is still vibrant/dark enough for white
        if (isCompact && trailing != null) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (overline != null) {
                        Text(overline.uppercase(), style = MaterialTheme.typography.labelMedium, color = textColor.copy(alpha = 0.85f))
                    }
                    Text(title, style = MaterialTheme.typography.displayMedium, color = textColor, fontWeight = FontWeight.Black)
                    Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = textColor.copy(alpha = 0.9f))
                }
                trailing()
            }
        } else {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (overline != null) {
                        Text(overline.uppercase(), style = MaterialTheme.typography.labelMedium, color = textColor.copy(alpha = 0.85f))
                    }
                    Text(title, style = MaterialTheme.typography.displayMedium, color = textColor, fontWeight = FontWeight.Black)
                    Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = textColor.copy(alpha = 0.9f))
                }
                if (trailing != null) {
                    Box(modifier = Modifier.padding(start = 16.dp)) {
                        trailing()
                    }
                }
            }
        }
    }
}

@Composable
fun ArcadeInlineActions(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        content()
    }
}

@Composable
fun PremiumOverlayCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ArcadeCard(
        modifier = modifier.widthIn(max = 400.dp).testTag("premium_overlay"),
        accent = Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)),
        contentPadding = ArcadeTheme.spacing.lg,
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), color = ArcadeTheme.colors.textPrimary)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = ArcadeTheme.colors.textSecondary, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        content()
    }
}

@Composable
fun SplashShell(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ArcadeTheme.colors.shellGradient),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .shadow(16.dp, RoundedCornerShape(32.dp))
                    .clip(RoundedCornerShape(32.dp))
                    .background(ArcadeTheme.colors.heroGradient),
                contentAlignment = Alignment.Center,
            ) {
                Text("OMA", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Black)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(title, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black, color = ArcadeTheme.colors.textPrimary, textAlign = TextAlign.Center)
                Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = ArcadeTheme.colors.textSecondary, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun gameAccentFor(label: String): ArcadeGameAccent {
    val colors = ArcadeTheme.colors
    return when (label) {
        "Pulse Orbit" -> ArcadeGameAccent(label = "Pulse Orbit", brush = Brush.linearGradient(listOf(colors.pulseAccent, MaterialTheme.colorScheme.secondary)), color = colors.pulseAccent)
        "Lane Drift" -> ArcadeGameAccent(label = "Lane Drift", brush = Brush.linearGradient(listOf(colors.laneAccent, colors.pulseAccent)), color = colors.laneAccent)
        "Stack Drop" -> ArcadeGameAccent(label = "Stack Drop", brush = Brush.linearGradient(listOf(colors.stackAccent, colors.reward)), color = colors.stackAccent)
        "Brick Volley" -> ArcadeGameAccent(label = "Brick Volley", brush = Brush.linearGradient(listOf(colors.brickVolleyAccent, colors.pulseAccent)), color = colors.brickVolleyAccent)
        "Loop Snake" -> ArcadeGameAccent(label = "Loop Snake", brush = Brush.linearGradient(listOf(colors.loopSnakeAccent, colors.reward)), color = colors.loopSnakeAccent)
        "Shield Dash" -> ArcadeGameAccent(label = "Shield Dash", brush = Brush.linearGradient(listOf(colors.shieldDashAccent, colors.pulseAccent)), color = colors.shieldDashAccent)
        "Gravity Flip" -> ArcadeGameAccent(label = "Gravity Flip", brush = Brush.linearGradient(listOf(colors.gravityFlipAccent, colors.reward)), color = colors.gravityFlipAccent)
        else -> ArcadeGameAccent(label = label, brush = Brush.linearGradient(listOf(colors.stackAccent, colors.reward)), color = colors.stackAccent)
    }
}

@Composable
fun ArcadeMarquee(
    resId: Int,
    contentDescription: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF181A20)) // Cabinet bezel base
            .border(3.dp, Color(0xFF2C2F36), RoundedCornerShape(24.dp)) // Outer plastic molding
            .padding(6.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
            ) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f),
                    contentScale = ContentScale.Crop
                )
                // Acrylic sheen overlay
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.15f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.35f)
                                )
                            )
                        )
                )
            }
            // Neon accent strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .padding(top = 2.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                accentColor.copy(alpha = 0.3f),
                                accentColor,
                                accentColor.copy(alpha = 0.3f)
                            )
                        )
                    )
            )
        }
    }
}

@Composable
fun ArcadePlayButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    testTag: String = ""
) {
    val reducedEffects = ArcadeTheme.reducedEffects
    val scale = if (reducedEffects) {
        1f
    } else {
        val infiniteTransition = rememberInfiniteTransition(label = "play_btn")
        val animatedScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.04f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )
        animatedScale
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = accentColor.copy(alpha = 0.5f),
                spotColor = accentColor
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        accentColor.copy(alpha = 0.9f),
                        accentColor
                    )
                )
            )
            .border(
                border = BorderStroke(2.dp, Color.White.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(20.dp)
            )
            .then(
                if (enabled) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .padding(vertical = 14.dp, horizontal = 24.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "▶",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = label.uppercase(),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }
    }
}
