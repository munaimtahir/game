package com.vexel.offlinearcade.game.pulseorbit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private data class PulseOrbitState(
    val playing: Boolean = false,
    val orbitAngle: Float = -90f,
    val gapCenterAngle: Float = 0f,
    val gapSize: Float = PulseOrbitTuning.initialGapSize,
    val rotationSpeedDegPerSec: Float = PulseOrbitTuning.initialRotationSpeed,
    val passes: Int = 0,
    val score: Int = 0,
    val combo: Int = 0,
    val bestCombo: Int = 0,
    val runStartMillis: Long = 0L,
    val gameOver: Boolean = false,
    val feedback: String = "Tap when the orb lines up with the gap.",
)

internal object PulseOrbitTuning {
    const val boardHeightDp = 430
    const val initialGapSize = 82f
    const val minimumGapSize = 38f
    const val gapShrinkPerPass = 1.15f
    const val initialRotationSpeed = 96f
    const val speedIncreasePerPass = 4.3f
    const val maxRotationSpeed = 222f
    const val gapStepBase = 74f
    const val gapStepPerPass = 3.6f
    const val maxGapStep = 136f
    const val comboBonusEvery = 5

    fun gapSizeFor(passes: Int): Float =
        maxOf(minimumGapSize, initialGapSize - passes * gapShrinkPerPass)

    fun rotationSpeedFor(passes: Int): Float =
        min(maxRotationSpeed, initialRotationSpeed + passes * speedIncreasePerPass)

    fun gapStepFor(passes: Int): Float =
        min(maxGapStep, gapStepBase + passes * gapStepPerPass)
}

@Composable
fun PulseOrbitScreen(
    stats: GameStats?,
    settings: SettingsState,
    feedback: ArcadeFeedback,
    onRunComplete: (RunResult) -> Unit,
    onBack: () -> Unit,
) {
    var state by remember { mutableStateOf(PulseOrbitState()) }
    var hasReportedRun by remember { mutableStateOf(false) }
    var lastFrameNanos by remember { mutableLongStateOf(0L) }

    fun restart() {
        hasReportedRun = false
        lastFrameNanos = 0L
        state = PulseOrbitState(
            playing = true,
            runStartMillis = System.currentTimeMillis(),
            feedback = "Thread the gap and keep rhythm.",
        )
        feedback.play(ArcadeFeedbackEvent.TAP)
    }

    LaunchedEffect(state.playing) {
        while (state.playing) {
            withFrameNanos { frameTime ->
                if (lastFrameNanos == 0L) {
                    lastFrameNanos = frameTime
                    return@withFrameNanos
                }
                val deltaSeconds = (frameTime - lastFrameNanos) / 1_000_000_000f
                lastFrameNanos = frameTime
                state = state.copy(
                    orbitAngle = (state.orbitAngle + state.rotationSpeedDegPerSec * deltaSeconds).normalizeAngle(),
                )
            }
        }
    }

    if (state.gameOver && !hasReportedRun) {
        hasReportedRun = true
        val duration = (System.currentTimeMillis() - state.runStartMillis).coerceAtLeast(0L)
        onRunComplete(
            RunResult(
                gameId = GameId.PULSE_ORBIT,
                score = state.score,
                durationMillis = duration,
                bestCombo = state.bestCombo,
                coinsEarned = state.score + state.bestCombo,
            ),
        )
    }

    ArcadeScaffold(
        title = "Pulse Orbit",
        onBack = onBack,
        scrollable = false,
        screenTestTag = ArcadeTestTags.PulseOrbitScreen,
    ) {
        val primaryContainer = MaterialTheme.colorScheme.primaryContainer
        val primary = MaterialTheme.colorScheme.primary
        val tertiary = MaterialTheme.colorScheme.tertiary
        val surface = MaterialTheme.colorScheme.surface
        val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            HudPill("Score", state.score.toString())
            HudPill("Combo", state.combo.toString())
            HudPill("Best", (stats?.highScore ?: 0).toString())
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(PulseOrbitTuning.boardHeightDp.dp)
                .padding(top = 16.dp)
                .background(surface, RoundedCornerShape(28.dp))
                .clickable {
                    if (!state.playing) {
                        restart()
                        return@clickable
                    }
                    val distance = angularDistance(state.orbitAngle, state.gapCenterAngle)
                    if (distance <= state.gapSize / 2f) {
                        val nextPass = state.passes + 1
                        val nextCombo = state.combo + 1
                        val comboBonus = if (nextCombo % PulseOrbitTuning.comboBonusEvery == 0) 1 else 0
                        feedback.play(ArcadeFeedbackEvent.SUCCESS)
                        state = state.copy(
                            passes = nextPass,
                            score = state.score + 1 + comboBonus,
                            combo = nextCombo,
                            bestCombo = maxOf(state.bestCombo, nextCombo),
                            gapCenterAngle = (state.gapCenterAngle + PulseOrbitTuning.gapStepFor(nextPass)).normalizeAngle(),
                            gapSize = PulseOrbitTuning.gapSizeFor(nextPass),
                            rotationSpeedDegPerSec = PulseOrbitTuning.rotationSpeedFor(nextPass),
                            feedback = if (comboBonus > 0) "Combo boost." else "Clean pass. Tempo up.",
                        )
                    } else {
                        feedback.play(ArcadeFeedbackEvent.FAIL)
                        state = state.copy(
                            playing = false,
                            combo = 0,
                            gameOver = true,
                            feedback = if (settings.vibrationEnabled) "Missed the gap. Fast retry ready." else "Missed the gap.",
                        )
                    }
                },
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = min(size.width, size.height) * 0.28f
                val center = Offset(size.width / 2f, size.height / 2f)
                val ringStroke = radius * 0.22f
                val topLeft = Offset(center.x - radius, center.y - radius)
                drawCircle(color = primaryContainer, radius = radius * 0.42f, center = center)
                drawArc(
                    color = primary,
                    startAngle = state.gapCenterAngle + state.gapSize / 2f,
                    sweepAngle = 360f - state.gapSize,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(radius * 2f, radius * 2f),
                    style = Stroke(width = ringStroke, cap = StrokeCap.Round),
                )
                val orbAngleRadians = state.orbitAngle * (PI / 180f).toFloat()
                val orbCenter = Offset(
                    x = center.x + cos(orbAngleRadians).toFloat() * radius,
                    y = center.y + sin(orbAngleRadians).toFloat() * radius,
                )
                drawCircle(color = tertiary, radius = ringStroke * 0.48f, center = orbCenter)
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(state.feedback, color = onSurfaceVariant)
                Text(
                    if (state.playing) "Tap anywhere on the board" else "Tap board or button to retry",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        ArcadeCard(modifier = Modifier.padding(top = 16.dp)) {
            Text("Tap when the orbiting pulse lines up with the ring opening. Every fifth clean pass adds a score bump and sharper pacing.")
            if (state.gameOver) {
                Text("Run summary: ${state.score} score, ${state.bestCombo} best combo.")
                Button(onClick = ::restart, modifier = Modifier.fillMaxWidth().height(48.dp)) { Text("Retry instantly") }
            } else if (!state.playing) {
                Button(onClick = ::restart, modifier = Modifier.fillMaxWidth().height(48.dp)) { Text("Start run") }
            }
        }
    }
}

internal fun angularDistance(first: Float, second: Float): Float {
    val raw = kotlin.math.abs(first.normalizeAngle() - second.normalizeAngle())
    return min(raw, 360f - raw)
}

internal fun Float.normalizeAngle(): Float {
    var normalized = this % 360f
    if (normalized < 0f) normalized += 360f
    return normalized
}
