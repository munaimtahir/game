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

private enum class DriftItemType { BLOCKER, PICKUP }

private data class DriftItem(val lane: Int, val y: Float, val type: DriftItemType)

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
    const val initialSpeed = 186f
    const val maxSpeed = 372f
    const val speedRampPerSecond = 6.4f
    const val initialSpawnInterval = 0.94f
    const val minimumSpawnInterval = 0.56f
    const val spawnIntervalRampPerSecond = 0.011f
    const val playerZoneY = 0.88f
    const val blockerHeight = 84f
    const val pickupHeight = 50f
    const val blockerCollisionWindow = 0.062f
    const val pickupCollisionWindow = 0.078f

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
    val boardHeightPx = with(density) { 600.dp.toPx() } 

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
                val nextSpeed = LaneDriftTuning.speedFor(nextElapsed)
                val spawnInterval = LaneDriftTuning.spawnIntervalFor(nextElapsed)
                var nextSpawnTimer = state.spawnTimer + delta
                var nextSpawnCount = state.spawnCount
                var nextLastBlockerLane = state.lastBlockerLane
                val nextItems = ArrayList<DriftItem>(state.items.size + 2)
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
                    nextItems += DriftItem(blockerLane, -0.14f, DriftItemType.BLOCKER)
                    if (shouldSpawnPickup(spawnCount = nextSpawnCount, elapsedSeconds = nextElapsed, random = random)) {
                        nextItems += DriftItem(pickupLaneFor(blockerLane, random), -0.44f, DriftItemType.PICKUP)
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
                val blockerHit = nextItems.firstOrNull { item ->
                    item.type == DriftItemType.BLOCKER &&
                        item.lane == nextState.lane &&
                        item.y in
                        (LaneDriftTuning.playerZoneY - LaneDriftTuning.blockerCollisionWindow)..(LaneDriftTuning.playerZoneY + LaneDriftTuning.blockerCollisionWindow)
                }
                val pickupHit = if (blockerHit == null) nextItems.firstOrNull { item ->
                    item.type == DriftItemType.PICKUP &&
                    item.lane == nextState.lane &&
                        item.y in
                        (LaneDriftTuning.playerZoneY - LaneDriftTuning.pickupCollisionWindow)..(LaneDriftTuning.playerZoneY + LaneDriftTuning.pickupCollisionWindow)
                } else {
                    null
                }
                if (blockerHit != null) {
                    feedback.play(ArcadeFeedbackEvent.FAIL)
                    nextState = nextState.copy(
                        playing = false,
                        gameOver = true,
                        message = "Collision.",
                    )
                } else if (pickupHit != null) {
                    feedback.play(ArcadeFeedbackEvent.PICKUP)
                    nextItems.remove(pickupHit)
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

    GameplayScaffold(
        modifier = Modifier.testTag(ArcadeTestTags.LaneDriftScreen),
        topBar = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
        overlay = {
            if (state.paused) {
                PremiumOverlayCard(title = "Run paused", subtitle = "Resume instantly or restart.") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Score", state.score.toString())
                        StatRow("Pickups", state.pickups.toString())
                        PremiumButton(label = "Resume", onClick = ::togglePause, modifier = Modifier.fillMaxWidth())
                        PremiumButton(label = "Restart", onClick = ::restart, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
                        PremiumButton(label = "Quit", onClick = onBack, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
                    }
                }
            } else if (state.gameOver) {
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
                    drawRoundRect(
                        color = if (item.type == DriftItemType.BLOCKER) colors.dangerCoral else colors.pickupMint,
                        topLeft = Offset(left, top),
                        size = Size(
                            laneWidth * if (item.type == DriftItemType.BLOCKER) 0.56f else 0.42f,
                            if (item.type == DriftItemType.BLOCKER) LaneDriftTuning.blockerHeight else LaneDriftTuning.pickupHeight,
                        ),
                        cornerRadius = CornerRadius(22f, 22f),
                    )
                }
                
                val playerTop = size.height * LaneDriftTuning.playerZoneY
                val playerLeft = laneWidth * state.lane + laneWidth * 0.18f
                val playerWidth = laneWidth * 0.64f
                val playerHeight = 84f
                
                drawRoundRect(
                    color = colors.primaryCyan.copy(alpha = 0.35f),
                    topLeft = Offset(playerLeft - 8f, playerTop + 16f),
                    size = Size(playerWidth + 16f, playerHeight),
                    cornerRadius = CornerRadius(34f, 34f),
                )
                drawRoundRect(
                    color = colors.primaryCyan,
                    topLeft = Offset(playerLeft, playerTop),
                    size = Size(playerWidth, playerHeight),
                    cornerRadius = CornerRadius(26f, 26f),
                )
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.8f),
                    topLeft = Offset(playerLeft + 6f, playerTop + 6f),
                    size = Size(playerWidth - 12f, playerHeight - 12f),
                    cornerRadius = CornerRadius(20f, 20f),
                    style = Stroke(width = 4f)
                )
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
