package com.vexel.offlinearcade.game.pulseorbit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.GameplayScaffold
import com.vexel.offlinearcade.core.ui.HudPill
import com.vexel.offlinearcade.core.ui.PremiumBadge
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.PremiumOverlayCard
import com.vexel.offlinearcade.core.ui.StatRow
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private data class PulseOrbitState(
    val playing: Boolean = false,
    val paused: Boolean = false,
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
    val feedback: String = "Tap to start",
)

internal object PulseOrbitTuning {
    const val initialGapSize = 88f
    const val minimumGapSize = 40f
    const val gapShrinkPerPass = 1.2f
    const val initialRotationSpeed = 85f
    const val speedIncreasePerPass = 4.2f
    const val maxRotationSpeed = 230f
    const val gapStepBase = 72f
    const val gapStepPerPass = 3.5f
    const val maxGapStep = 140f
    const val comboBonusEvery = 5
    const val collisionToleranceDegrees = 4.5f

    fun gapSizeFor(passes: Int): Float = maxOf(minimumGapSize, initialGapSize - passes * gapShrinkPerPass)
    fun rotationSpeedFor(passes: Int): Float = min(maxRotationSpeed, initialRotationSpeed + passes * speedIncreasePerPass)
    fun gapStepFor(passes: Int): Float = min(maxGapStep, gapStepBase + passes * gapStepPerPass)
}

@Composable
@Suppress("UNUSED_PARAMETER")
fun PulseOrbitScreen(
    stats: GameStats?,
    settings: SettingsState,
    equippedSkin: String,
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
            paused = false,
            runStartMillis = System.currentTimeMillis(),
            feedback = "Thread the gap.",
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

    LaunchedEffect(state.playing) {
        while (state.playing) {
            withFrameNanos { frameTime ->
                if (lastFrameNanos == 0L) {
                    lastFrameNanos = frameTime
                    return@withFrameNanos
                }
                val deltaSeconds = (frameTime - lastFrameNanos) / 1_000_000_000f
                lastFrameNanos = frameTime
                state = state.copy(orbitAngle = (state.orbitAngle + state.rotationSpeedDegPerSec * deltaSeconds).normalizeAngle())
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

    val colors = ArcadeTheme.colors
    
    val (coreColor, orbColor) = when (equippedSkin) {
        "po_gold" -> colors.reward to colors.reward
        "po_neon" -> Color(0xFFFF007F) to Color(0xFFFF007F) // Custom neon pink
        else -> colors.gameBoardRaised to colors.accentViolet
    }

    // Animation state
    val successPulse = remember { androidx.compose.animation.core.Animatable(0f) }
    val failPulse = remember { androidx.compose.animation.core.Animatable(0f) }
    val ringBurst = remember { androidx.compose.animation.core.Animatable(0f) }
    val readyPulse = remember { androidx.compose.animation.core.Animatable(0.6f) }

    LaunchedEffect(state.playing, state.gameOver) {
        if (!state.playing && !state.gameOver) {
            readyPulse.animateTo(
                targetValue = 1f,
                animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                    animation = androidx.compose.animation.core.tween(1000, easing = androidx.compose.animation.core.LinearOutSlowInEasing),
                    repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                )
            )
        } else {
            readyPulse.snapTo(1f)
        }
    }

    LaunchedEffect(state.passes) {
        if (state.passes > 0) {
            successPulse.snapTo(1f)
            successPulse.animateTo(0f, animationSpec = androidx.compose.animation.core.tween(400))
            if (state.combo > 0 && state.combo % PulseOrbitTuning.comboBonusEvery == 0) {
                ringBurst.snapTo(1f)
                ringBurst.animateTo(0f, animationSpec = androidx.compose.animation.core.tween(600, easing = androidx.compose.animation.core.LinearOutSlowInEasing))
            }
        }
    }

    LaunchedEffect(state.gameOver) {
        if (state.gameOver) {
            failPulse.snapTo(1f)
            failPulse.animateTo(0f, animationSpec = androidx.compose.animation.core.tween(500))
        }
    }

    val overlayContent: (@Composable () -> Unit)? = when {
        state.paused -> {
            {
                PremiumOverlayCard(title = "Run paused", subtitle = "Resume instantly or reset the loop.") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Score", state.score.toString())
                        StatRow("Combo", state.combo.toString())
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
                    title = if (state.score > (stats?.highScore ?: 0)) "New best rhythm" else "Run complete",
                    subtitle = "One more clean sequence is only a tap away.",
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Score", state.score.toString(), valueColor = colors.reward)
                        StatRow("Best combo", state.bestCombo.toString(), valueColor = colors.success)
                        StatRow("Coins earned", (state.score + state.bestCombo).toString(), valueColor = colors.reward)
                        PremiumButton(
                            label = "Retry instantly",
                            onClick = ::restart,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(ArcadeTestTags.PulseOrbitStartButton),
                        )
                        PremiumButton(label = "Back to detail", onClick = onBack, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
                    }
                }
            }
        }
        else -> null
    }
    
    GameplayScaffold(
        modifier = Modifier.testTag(ArcadeTestTags.PulseOrbitScreen),
        topBar = {
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
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.textPrimary
                        )
                    }
                    HudPill("Score", state.score.toString())
                    HudPill("Combo", state.combo.toString())
                }
                PremiumButton(
                    label = if (state.paused) "Resume" else "Pause",
                    onClick = ::togglePause,
                    style = ArcadeButtonStyle.Secondary,
                    enabled = state.playing || state.paused,
                    borderOverride = colors.accentViolet,
                )
            }
        },
        overlay = overlayContent,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag(ArcadeTestTags.PulseOrbitBoard)
                .background(colors.gameBoard, RoundedCornerShape(28.dp))
                .clickable {
                    if (state.paused || state.gameOver) return@clickable
                    if (!state.playing) {
                        restart()
                        return@clickable
                    }
                    val distance = angularDistance(state.orbitAngle, state.gapCenterAngle)
                    val fairThreshold = (state.gapSize / 2f) + PulseOrbitTuning.collisionToleranceDegrees
                    if (distance <= fairThreshold) {
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
                            feedback = if (comboBonus > 0) "Perfect chain. Tempo up." else "Clean pass.",
                        )
                    } else {
                        feedback.play(ArcadeFeedbackEvent.FAIL)
                        state = state.copy(
                            playing = false,
                            combo = 0,
                            gameOver = true,
                            feedback = "Missed the gap.",
                        )
                    }
                },
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = min(size.width, size.height) * 0.28f
                val center = Offset(size.width / 2f, size.height / 2f)
                val ringStroke = radius * 0.22f
                val topLeft = Offset(center.x - radius, center.y - radius)
                
                // Fail Flash Background
                if (failPulse.value > 0f) {
                    drawRect(color = colors.dangerCoral.copy(alpha = failPulse.value * 0.3f))
                }

                // Ring Burst
                if (ringBurst.value > 0f) {
                    drawCircle(
                        color = colors.accentViolet.copy(alpha = ringBurst.value * 0.5f),
                        radius = radius + ringStroke + (1f - ringBurst.value) * radius * 1.5f,
                        center = center,
                        style = Stroke(width = ringStroke * ringBurst.value)
                    )
                }

                // Central core
                drawCircle(
                    color = androidx.compose.ui.graphics.lerp(coreColor, colors.pickupMint, successPulse.value * 0.5f),
                    radius = radius * 0.42f + successPulse.value * 12f,
                    center = center
                )
                
                // Ring
                drawArc(
                    color = androidx.compose.ui.graphics.lerp(colors.primaryCyan, colors.pickupMint, successPulse.value),
                    startAngle = state.gapCenterAngle + state.gapSize / 2f,
                    sweepAngle = 360f - state.gapSize,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(radius * 2f, radius * 2f),
                    style = Stroke(width = ringStroke + successPulse.value * 6f, cap = StrokeCap.Round),
                )
                
                // Orb
                val orbAngleRadians = state.orbitAngle * (PI / 180f).toFloat()
                val orbCenter = Offset(
                    x = center.x + cos(orbAngleRadians).toFloat() * radius,
                    y = center.y + sin(orbAngleRadians).toFloat() * radius,
                )
                drawCircle(
                    color = androidx.compose.ui.graphics.lerp(orbColor, colors.dangerCoral, failPulse.value),
                    radius = ringStroke * 0.48f + successPulse.value * 6f,
                    center = orbCenter
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (state.playing && state.combo > 0) {
                    PremiumBadge(
                        text = if (state.combo % PulseOrbitTuning.comboBonusEvery == 0) "Perfect timing" else "Clean timing",
                        color = colors.accentViolet,
                    )
                }
                Text(
                    state.feedback,
                    color = colors.textSecondary.copy(alpha = if (!state.playing && !state.gameOver) readyPulse.value else 1f),
                    style = MaterialTheme.typography.bodyMedium
                )
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
