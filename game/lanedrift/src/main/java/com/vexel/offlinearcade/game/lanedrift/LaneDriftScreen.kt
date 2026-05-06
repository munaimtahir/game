package com.vexel.offlinearcade.game.lanedrift

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.vexel.offlinearcade.core.model.ArcadeFeedback
import com.vexel.offlinearcade.core.model.ArcadeFeedbackEvent
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.core.model.SettingsState
import com.vexel.offlinearcade.core.ui.ArcadeButtonStyle
import com.vexel.offlinearcade.core.ui.ArcadeGestureAction
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.GameplayScaffold
import com.vexel.offlinearcade.core.ui.HudPill
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.PremiumOverlayCard
import com.vexel.offlinearcade.core.ui.StatRow
import com.vexel.offlinearcade.core.ui.arcadeGestureInput
import com.vexel.offlinearcade.core.ui.rememberArcadeGestureThresholdsPx
import kotlin.math.min
import kotlin.random.Random

internal enum class DriftItemType { BLOCKER, PICKUP }

internal enum class DriftHazardSkin { CONE, BARRIER, CRATE, BARREL, POTHOLE }
internal enum class DriftPickupSkin { COIN, STAR, ENERGY, GEM, FUEL }

internal data class DriftItem(
    val lane: Int,
    val y: Float,
    val type: DriftItemType,
    val skin: Int = 0,
)

private data class LaneDriftState(
    val playing: Boolean = false,
    val paused: Boolean = false,
    val lane: Int = 1,
    val items: List<DriftItem> = emptyList(),
    val score: Int = 0,
    val pickups: Int = 0,
    val speed: Float = LaneDriftTuning.initialSpeed,
    val elapsedSeconds: Float = 0f,
    val spawnTimer: Float = 0f,
    val spawnCount: Int = 0,
    val lastBlockerLane: Int = -1,
    val gameOver: Boolean = false,
    val runStartMillis: Long = 0L,
    val message: String = "Swipe left or right",
)

internal object LaneDriftTuning {
    // Difficulty tuning (made more beginner-friendly).
    const val initialSpeed = 142f
    const val maxSpeed = 352f
    const val speedRampPerSecond = 4.9f
    const val initialSpawnInterval = 1.22f
    const val minimumSpawnInterval = 0.66f
    const val spawnIntervalRampPerSecond = 0.0085f
    const val playerZoneY = 0.88f
    // Visual sizes are expressed in dp and converted to px at runtime for consistent behavior across densities.
    val playerHeightDp = 84.dp
    val blockerHeightDp = 84.dp
    val pickupHeightDp = 50.dp

    // Forgiving hitboxes (smaller than visuals).
    const val playerHitboxInsetXFraction = 0.18f
    const val playerHitboxInsetYFraction = 0.12f
    const val blockerHitboxInsetXFraction = 0.16f
    const val blockerHitboxInsetYFraction = 0.10f
    const val pickupHitboxInsetXFraction = 0.10f
    const val pickupHitboxInsetYFraction = 0.10f

    val blockerMinOverlapDp = 8.dp
    val pickupMinOverlapDp = 6.dp

    // Tutorial-like ramp: keep the first seconds extra readable.
    const val graceSeconds = 15f

    fun speedFor(elapsedSeconds: Float): Float =
        min(maxSpeed, initialSpeed + elapsedSeconds * speedRampPerSecond)

    fun spawnIntervalFor(elapsedSeconds: Float): Float =
        maxOf(minimumSpawnInterval, initialSpawnInterval - elapsedSeconds * spawnIntervalRampPerSecond)
}

@Composable
fun LaneDriftScreen(
    stats: GameStats?,
    settings: SettingsState,
    feedback: ArcadeFeedback,
    onRunComplete: (RunResult) -> Unit,
    onBack: () -> Unit,
) {
    var state by remember { mutableStateOf(LaneDriftState()) }
    var hasReportedRun by remember { mutableStateOf(false) }
    var lastFrameNanos by remember { mutableLongStateOf(0L) }
    val random = remember { Random(System.currentTimeMillis()) }
    val gestureThresholds = rememberArcadeGestureThresholdsPx()
    val pickupFlash = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(state.pickups) {
        if (state.pickups > 0) {
            pickupFlash.snapTo(1f)
            pickupFlash.animateTo(0f, animationSpec = androidx.compose.animation.core.tween(300))
        }
    }

    fun restart() {
        hasReportedRun = false
        lastFrameNanos = 0L
        state = LaneDriftState(
            playing = true,
            paused = false,
            speed = LaneDriftTuning.initialSpeed,
            runStartMillis = System.currentTimeMillis(),
            message = "Clean lane.",
        )
        feedback.play(ArcadeFeedbackEvent.TAP)
    }

    fun togglePause() {
        if (state.gameOver) return
        state = state.copy(
            paused = !state.paused,
            playing = state.paused,
        )
    }

    fun moveLane(delta: Int) {
        if (!state.playing || state.paused) return
        val nextLane = (state.lane + delta).coerceIn(0, 2)
        if (nextLane != state.lane) {
            feedback.play(ArcadeFeedbackEvent.TAP)
            state = state.copy(lane = nextLane)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                if (state.playing && !state.paused && !state.gameOver) {
                    togglePause()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    BackHandler {
        if (state.playing && !state.paused) {
            togglePause()
        } else {
            onBack()
        }
    }

    val density = LocalDensity.current
    var boardSizePx by remember { mutableStateOf(IntSize.Zero) }
    val fallbackBoardHeightPx = with(density) { 600.dp.toPx() }
    val fallbackBoardWidthPx = with(density) { 360.dp.toPx() }

    LaunchedEffect(state.playing) {
        while (state.playing) {
            withFrameNanos { frameTime ->
                if (lastFrameNanos == 0L) {
                    lastFrameNanos = frameTime
                    return@withFrameNanos
                }
                val delta = (frameTime - lastFrameNanos) / 1_000_000_000f
                lastFrameNanos = frameTime
                val nextElapsed = state.elapsedSeconds + delta
                val nextSpeedBase = LaneDriftTuning.speedFor(nextElapsed)
                val nextSpeed = if (nextElapsed < LaneDriftTuning.graceSeconds) {
                    val t = (nextElapsed / LaneDriftTuning.graceSeconds).coerceIn(0f, 1f)
                    nextSpeedBase * (0.86f + 0.14f * t)
                } else {
                    nextSpeedBase
                }
                val spawnIntervalBase = LaneDriftTuning.spawnIntervalFor(nextElapsed)
                val spawnInterval = if (nextElapsed < LaneDriftTuning.graceSeconds) spawnIntervalBase * 1.18f else spawnIntervalBase
                var nextSpawnTimer = state.spawnTimer + delta
                var nextSpawnCount = state.spawnCount
                var nextLastBlockerLane = state.lastBlockerLane
                val nextItems = ArrayList<DriftItem>(state.items.size + 2)
                val boardHeightPx = boardSizePx.height.takeIf { it > 0 }?.toFloat() ?: fallbackBoardHeightPx
                state.items.forEach { item ->
                    val nextY = item.y + (nextSpeed * delta) / boardHeightPx
                    if (nextY < 1.14f) {
                        nextItems += item.copy(y = nextY)
                    }
                }
                while (nextSpawnTimer >= spawnInterval) {
                    nextSpawnTimer -= spawnInterval
                    val blockerLane = pickBlockerLane(
                        random = random,
                        previousLane = nextLastBlockerLane,
                        elapsedSeconds = nextElapsed,
                    )
                    val hazardSkin = random.nextInt(DriftHazardSkin.entries.size)
                    nextItems += DriftItem(blockerLane, -0.14f, DriftItemType.BLOCKER, skin = hazardSkin)
                    if (shouldSpawnPickup(spawnCount = nextSpawnCount, elapsedSeconds = nextElapsed, random = random)) {
                        val pickupSkin = random.nextInt(DriftPickupSkin.entries.size)
                        nextItems += DriftItem(pickupLaneFor(blockerLane, random), -0.44f, DriftItemType.PICKUP, skin = pickupSkin)
                    }
                    nextSpawnCount += 1
                    nextLastBlockerLane = blockerLane
                }
                var nextState = state.copy(
                    items = nextItems,
                    speed = nextSpeed,
                    elapsedSeconds = nextElapsed,
                    score = (nextElapsed * 9f).toInt() + state.pickups * 4,
                    spawnTimer = nextSpawnTimer,
                    spawnCount = nextSpawnCount,
                    lastBlockerLane = nextLastBlockerLane,
                )
                val boardWidthPx = boardSizePx.width.takeIf { it > 0 }?.toFloat() ?: fallbackBoardWidthPx
                val sizes = LaneDriftSizesPx(
                    playerHeightPx = with(density) { LaneDriftTuning.playerHeightDp.toPx() },
                    blockerHeightPx = with(density) { LaneDriftTuning.blockerHeightDp.toPx() },
                    pickupHeightPx = with(density) { LaneDriftTuning.pickupHeightDp.toPx() },
                )
                val collisionConfig = LaneDriftCollisionConfig(
                    playerInsetXFraction = LaneDriftTuning.playerHitboxInsetXFraction,
                    playerInsetYFraction = LaneDriftTuning.playerHitboxInsetYFraction,
                    blockerInsetXFraction = LaneDriftTuning.blockerHitboxInsetXFraction,
                    blockerInsetYFraction = LaneDriftTuning.blockerHitboxInsetYFraction,
                    pickupInsetXFraction = LaneDriftTuning.pickupHitboxInsetXFraction,
                    pickupInsetYFraction = LaneDriftTuning.pickupHitboxInsetYFraction,
                    blockerMinOverlapPx = with(density) { LaneDriftTuning.blockerMinOverlapDp.toPx() },
                    pickupMinOverlapPx = with(density) { LaneDriftTuning.pickupMinOverlapDp.toPx() },
                )
                val collision = resolveLaneDriftCollision(
                    playerLane = nextState.lane,
                    items = nextItems,
                    boardWidthPx = boardWidthPx,
                    boardHeightPx = boardHeightPx,
                    config = collisionConfig,
                    sizes = sizes,
                )
                if (collision.type == LaneDriftCollisionType.BLOCKER) {
                    feedback.play(ArcadeFeedbackEvent.FAIL)
                    nextState = nextState.copy(
                        playing = false,
                        gameOver = true,
                        message = "Collision.",
                    )
                } else if (collision.type == LaneDriftCollisionType.PICKUP && collision.hitItem != null) {
                    feedback.play(ArcadeFeedbackEvent.PICKUP)
                    nextItems.remove(collision.hitItem)
                    nextState = nextState.copy(
                        items = nextItems,
                        pickups = nextState.pickups + 1,
                        score = nextState.score + 14,
                        message = "Shard collected.",
                    )
                }
                state = nextState
            }
        }
    }

    if (state.gameOver && !hasReportedRun) {
        hasReportedRun = true
        onRunComplete(
            RunResult(
                gameId = GameId.LANE_DRIFT,
                score = state.score,
                durationMillis = System.currentTimeMillis() - state.runStartMillis,
                pickupsCollected = state.pickups,
                coinsEarned = state.pickups * 3 + state.score / 20,
            ),
        )
    }

    val colors = ArcadeTheme.colors

    val overlayContent: (@Composable () -> Unit)? = when {
        state.paused -> {
            {
                PremiumOverlayCard(title = "Run paused", subtitle = "Resume instantly or restart.") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Score", state.score.toString())
                        StatRow("Pickups", state.pickups.toString())
                        PremiumButton(label = "Resume", onClick = ::togglePause, modifier = Modifier.fillMaxWidth())
                        PremiumButton(label = "Restart", onClick = ::restart, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
                        PremiumButton(label = "Quit", onClick = onBack, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
                    }
                }
            }
        }
        state.gameOver -> {
            {
                PremiumOverlayCard(
                    title = if (state.score > (stats?.highScore ?: 0)) "New drift record" else "Run complete",
                    subtitle = "Keep the lane read tight and go again.",
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Score", state.score.toString(), valueColor = colors.reward)
                        StatRow("Pickups", state.pickups.toString(), valueColor = colors.success)
                        StatRow("Coins earned", (state.pickups * 3 + state.score / 20).toString(), valueColor = colors.reward)
                        PremiumButton(
                            label = "Retry instantly",
                            onClick = ::restart,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(ArcadeTestTags.LaneDriftStartButton),
                        )
                        PremiumButton(label = "Back to detail", onClick = onBack, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
                    }
                }
            }
        }
        else -> null
    }

    GameplayScaffold(
        modifier = Modifier.testTag(ArcadeTestTags.LaneDriftScreen),
        topBar = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        androidx.compose.material3.IconButton(
                            onClick = {
                                if (state.playing && !state.paused) {
                                    togglePause()
                                } else {
                                    onBack()
                                }
                            },
                            modifier = Modifier.testTag(ArcadeTestTags.BackButton)
                        ) {
                            androidx.compose.material3.Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = colors.textPrimary
                            )
                        }
                        HudPill("Score", state.score.toString())
                        HudPill("Pickups", state.pickups.toString())
                    }
                    PremiumButton(
                        label = if (state.paused) "Resume" else "Pause",
                        onClick = ::togglePause,
                        style = ArcadeButtonStyle.Secondary,
                        enabled = state.playing || state.paused,
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.labelMedium,
                        color = colors.textSecondary
                    )
                }
            }
        },
        overlay = overlayContent,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .testTag(ArcadeTestTags.LaneDriftBoard)
                .semantics {
                    stateDescription = "lane=${state.lane};playing=${state.playing};items=${state.items.size}"
                }
                .background(colors.gameBoard, RoundedCornerShape(28.dp)),
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { boardSizePx = it }
                    .arcadeGestureInput(
                        thresholds = gestureThresholds,
                        enabled = true,
                    ) { action ->
                        when (action) {
                            ArcadeGestureAction.SwipeLeft -> moveLane(-1)
                            ArcadeGestureAction.SwipeRight -> moveLane(1)
                            ArcadeGestureAction.Tap -> if (!state.playing && !state.gameOver) restart()
                            else -> Unit
                        }
                    },
            ) {
                val laneWidth = size.width / 3f
                val playerHeightPx = LaneDriftTuning.playerHeightDp.toPx()
                val blockerHeightPx = LaneDriftTuning.blockerHeightDp.toPx()
                val pickupHeightPx = LaneDriftTuning.pickupHeightDp.toPx()
                
                // Speed Lines
                val baseSpeedOffset = (state.elapsedSeconds * 400f) % size.height
                val speedLineColor = colors.gameBoardRaised.copy(alpha = 0.5f)
                for (i in 0..4) {
                    val yOffset = (baseSpeedOffset + i * (size.height / 4f)) % size.height
                    drawLine(
                        color = speedLineColor,
                        start = Offset(laneWidth, yOffset),
                        end = Offset(laneWidth, yOffset + 60f),
                        strokeWidth = 4f
                    )
                    drawLine(
                        color = speedLineColor,
                        start = Offset(laneWidth * 2, (yOffset + size.height / 2) % size.height),
                        end = Offset(laneWidth * 2, ((yOffset + size.height / 2) % size.height) + 60f),
                        strokeWidth = 4f
                    )
                }

                repeat(3) { lane ->
                    drawRoundRect(
                        color = if (lane == state.lane) colors.hudCard else colors.gameBoardRaised,
                        topLeft = Offset(laneWidth * lane + 8f, 0f),
                        size = Size(laneWidth - 16f, size.height),
                        cornerRadius = CornerRadius(28f, 28f),
                    )
                }
                
                state.items.forEach { item ->
                    val top = size.height * item.y
                    val left = laneWidth * item.lane + laneWidth * 0.2f
                    if (item.type == DriftItemType.BLOCKER) {
                        drawHazard(
                            skin = DriftHazardSkin.entries[item.skin.coerceIn(0, DriftHazardSkin.entries.lastIndex)],
                            topLeft = Offset(left, top),
                            width = laneWidth * 0.56f,
                            height = blockerHeightPx,
                            colors = colors,
                        )
                    } else {
                        drawPickup(
                            skin = DriftPickupSkin.entries[item.skin.coerceIn(0, DriftPickupSkin.entries.lastIndex)],
                            topLeft = Offset(left, top),
                            width = laneWidth * 0.42f,
                            height = pickupHeightPx,
                            colors = colors,
                        )
                    }
                }
                
                val playerTop = size.height * LaneDriftTuning.playerZoneY
                val playerLeft = laneWidth * state.lane + laneWidth * 0.18f
                val playerWidth = laneWidth * 0.64f
                drawPlayerCar(
                    topLeft = Offset(playerLeft, playerTop),
                    width = playerWidth,
                    height = playerHeightPx,
                    colors = colors,
                )
                
                // Pickup Sparkle
                if (pickupFlash.value > 0f) {
                    drawCircle(
                        color = colors.pickupMint.copy(alpha = pickupFlash.value * 0.8f),
                        radius = 40f + (1f - pickupFlash.value) * 60f,
                        center = Offset(playerLeft + playerWidth / 2f, playerTop - 20f)
                    )
                }
            }

            if (!state.playing && !state.paused && !state.gameOver) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PremiumButton(
                        label = "Tap to start",
                        onClick = ::restart,
                        modifier = Modifier.testTag(ArcadeTestTags.LaneDriftStartButton),
                    )
                }
            }
        }
    }
}

internal fun pickBlockerLane(
    random: Random,
    previousLane: Int,
    elapsedSeconds: Float,
): Int {
    if (elapsedSeconds < 12f && previousLane in 0..2) {
        return when (previousLane) {
            0 -> if (random.nextBoolean()) 1 else 2
            1 -> if (random.nextBoolean()) 0 else 2
            2 -> if (random.nextBoolean()) 0 else 1
            else -> random.nextInt(3)
        }
    }
    return random.nextInt(3)
}

internal fun pickupLaneFor(blockerLane: Int, random: Random): Int = when (blockerLane) {
    0 -> if (random.nextBoolean()) 1 else 2
    1 -> if (random.nextBoolean()) 0 else 2
    2 -> if (random.nextBoolean()) 0 else 1
    else -> random.nextInt(3)
}

internal fun shouldSpawnPickup(
    spawnCount: Int,
    elapsedSeconds: Float,
    random: Random,
): Boolean {
    if (spawnCount % 2 == 0) return true
    if (elapsedSeconds < 18f) return false
    return random.nextFloat() < 0.35f
}
