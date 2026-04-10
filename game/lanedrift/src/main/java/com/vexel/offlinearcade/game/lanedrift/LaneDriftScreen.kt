package com.vexel.offlinearcade.game.lanedrift

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.ArcadeFeedback
import com.vexel.offlinearcade.core.model.ArcadeFeedbackEvent
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.core.model.SettingsState
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.HudPill
import kotlin.math.min
import kotlin.random.Random

private enum class DriftItemType { BLOCKER, PICKUP }

private data class DriftItem(val lane: Int, val y: Float, val type: DriftItemType)

private data class LaneDriftState(
    val playing: Boolean = false,
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
    val message: String = "Hold the lane and keep moving.",
)

internal object LaneDriftTuning {
    const val boardHeightDp = 430
    const val initialSpeed = 186f
    const val maxSpeed = 372f
    const val speedRampPerSecond = 6.4f
    const val initialSpawnInterval = 0.94f
    const val minimumSpawnInterval = 0.56f
    const val spawnIntervalRampPerSecond = 0.011f
    const val playerZoneY = 0.82f
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

    fun restart() {
        hasReportedRun = false
        lastFrameNanos = 0L
        state = LaneDriftState(
            playing = true,
            speed = LaneDriftTuning.initialSpeed,
            runStartMillis = System.currentTimeMillis(),
            message = "Find the clean lane and keep drifting.",
        )
        feedback.play(ArcadeFeedbackEvent.TAP)
    }

    fun moveLane(delta: Int) {
        if (!state.playing) return
        val nextLane = (state.lane + delta).coerceIn(0, 2)
        if (nextLane != state.lane) {
            feedback.play(ArcadeFeedbackEvent.TAP)
            state = state.copy(lane = nextLane)
        }
    }

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
                    val nextY = item.y + nextSpeed * delta
                    if (nextY < 1.14f) {
                        nextItems += item.copy(y = nextY)
                    }
                }
                if (nextSpawnTimer >= spawnInterval) {
                    nextSpawnTimer -= spawnInterval
                    val blockerLane = pickBlockerLane(random = random, previousLane = state.lastBlockerLane, elapsedSeconds = nextElapsed)
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
                        message = if (settings.vibrationEnabled) "Hit a blocker. Retry is ready." else "Hit a blocker.",
                    )
                } else if (pickupHit != null) {
                    feedback.play(ArcadeFeedbackEvent.PICKUP)
                    nextItems.remove(pickupHit)
                    nextState = nextState.copy(
                        items = nextItems,
                        pickups = nextState.pickups + 1,
                        score = nextState.score + 14,
                        message = if (settings.soundEnabled) "Shard collected. Keep flow." else "Pickup.",
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

    ArcadeScaffold(
        title = "Lane Drift",
        onBack = onBack,
        scrollable = false,
        screenTestTag = ArcadeTestTags.LaneDriftScreen,
    ) {
        val surface = MaterialTheme.colorScheme.surface
        val primaryContainer = MaterialTheme.colorScheme.primaryContainer
        val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
        val tertiary = MaterialTheme.colorScheme.tertiary
        val secondary = MaterialTheme.colorScheme.secondary
        val primary = MaterialTheme.colorScheme.primary
        val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            HudPill("Score", state.score.toString())
            HudPill("Pickups", state.pickups.toString())
            HudPill("Best", (stats?.highScore ?: 0).toString())
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(LaneDriftTuning.boardHeightDp.dp)
                .padding(top = 16.dp)
                .background(surface, RoundedCornerShape(28.dp)),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val laneWidth = size.width / 3f
                repeat(3) { lane ->
                    drawRoundRect(
                        color = if (lane == state.lane) primaryContainer else surfaceVariant,
                        topLeft = Offset(laneWidth * lane + 8f, 0f),
                        size = Size(laneWidth - 16f, size.height),
                        cornerRadius = CornerRadius(28f, 28f),
                    )
                }
                state.items.forEach { item ->
                    val top = size.height * item.y
                    val left = laneWidth * item.lane + laneWidth * 0.2f
                    drawRoundRect(
                        color = if (item.type == DriftItemType.BLOCKER) tertiary else secondary,
                        topLeft = Offset(left, top),
                        size = Size(
                            laneWidth * if (item.type == DriftItemType.BLOCKER) 0.56f else 0.42f,
                            if (item.type == DriftItemType.BLOCKER) LaneDriftTuning.blockerHeight else LaneDriftTuning.pickupHeight,
                        ),
                        cornerRadius = CornerRadius(22f, 22f),
                    )
                }
                val playerTop = size.height * LaneDriftTuning.playerZoneY
                drawRoundRect(
                    color = primary,
                    topLeft = Offset(laneWidth * state.lane + laneWidth * 0.18f, playerTop),
                    size = Size(laneWidth * 0.64f, 84f),
                    cornerRadius = CornerRadius(26f, 26f),
                )
            }
            Text(
                state.message,
                modifier = Modifier.align(Alignment.BottomCenter).padding(18.dp),
                color = onSurfaceVariant,
            )
        }
        ArcadeCard(modifier = Modifier.padding(top = 16.dp)) {
            Text("Move between three lanes, avoid blockers, and scoop up the brighter shard pickups.")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { moveLane(-1) }, modifier = Modifier.weight(1f).height(52.dp), enabled = state.playing) { Text("Left") }
                Button(onClick = { if (!state.playing) restart() else moveLane(1) }, modifier = Modifier.weight(1f).height(52.dp)) {
                    Text(if (state.playing) "Right" else "Start / Retry")
                }
            }
            if (state.gameOver) {
                Text("Run summary: ${state.score} score and ${state.pickups} pickups.", fontWeight = FontWeight.SemiBold)
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
