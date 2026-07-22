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
import com.vexel.offlinearcade.core.model.RunCompletionReason
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.core.model.SettingsState
import com.vexel.offlinearcade.core.ui.ArcadeButtonStyle
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.CompletionPopup
import com.vexel.offlinearcade.core.ui.GameTutorialContent
import com.vexel.offlinearcade.core.ui.GameplayScaffold
import com.vexel.offlinearcade.core.ui.HudPill
import com.vexel.offlinearcade.core.ui.HowToPlayOverlay
import com.vexel.offlinearcade.core.ui.PremiumBadge
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.PremiumOverlayCard
import com.vexel.offlinearcade.core.ui.StatRow
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import java.util.UUID

@Composable
@Suppress("UNUSED_PARAMETER")
fun PulseOrbitScreen(
    stats: GameStats?,
    settings: SettingsState,
    equippedSkin: String,
    feedback: ArcadeFeedback,
    tutorialSeen: Boolean,
    onTutorialSeen: () -> Unit,
    onRunComplete: (RunResult) -> Unit,
    onPostRunExitRequested: (RunResult, () -> Unit) -> Unit,
    onBack: () -> Unit,
) {
    fun newReadyState(): PulseOrbitState {
        val now = System.currentTimeMillis()
        return createPulseOrbitReadyState(
            sessionId = UUID.randomUUID().toString(),
            seed = now,
        )
    }

    var state by remember { mutableStateOf(newReadyState()) }
    var hasReportedRun by remember { mutableStateOf(false) }
    var lastFrameNanos by remember { mutableLongStateOf(0L) }
    var showTutorial by remember(tutorialSeen) { mutableStateOf(!tutorialSeen) }
    var showCompletionSummary by remember { mutableStateOf(false) }
    var latestRunResult by remember { mutableStateOf<RunResult?>(null) }

    fun restart() {
        hasReportedRun = false
        showCompletionSummary = false
        latestRunResult = null
        lastFrameNanos = 0L
        state = startPulseOrbitRun(newReadyState(), System.currentTimeMillis())
        feedback.play(ArcadeFeedbackEvent.TAP)
    }

    fun showHowToPlay() {
        if (state.playing && !state.paused && !state.gameOver) {
            state = pausePulseOrbitRun(state)
        }
        showTutorial = true
    }

    fun closeTutorial(startAfterClose: Boolean) {
        onTutorialSeen()
        showTutorial = false
        if (startAfterClose) restart()
    }

    fun togglePause() {
        if (state.gameOver) return
        state = if (state.paused) {
            resumePulseOrbitRun(state)
        } else {
            pausePulseOrbitRun(state)
        }
    }

    fun exitCompletedRun() {
        val runResult = latestRunResult
        if (runResult == null) {
            onBack()
            return
        }
        onPostRunExitRequested(runResult, onBack)
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
        } else if (state.gameOver && showCompletionSummary) {
            showCompletionSummary = false
        } else if (state.gameOver) {
            exitCompletedRun()
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
                state = advancePulseOrbitState(state, deltaSeconds)
            }
        }
    }

    if (state.gameOver && !hasReportedRun) {
        hasReportedRun = true
        val duration = (System.currentTimeMillis() - state.runStartMillis).coerceAtLeast(0L)
        val runResult = RunResult(
                sessionId = state.sessionId,
                gameId = GameId.PULSE_ORBIT,
                score = state.score,
                startedAtEpochMillis = state.runStartMillis,
                finishedAtEpochMillis = System.currentTimeMillis(),
                durationMillis = duration,
                completionReason = RunCompletionReason.FAILED,
                bestCombo = state.bestCombo,
                coinsEarned = state.score + state.bestCombo,
                totalPasses = state.passes,
                perfectPasses = state.perfectPasses,
        )
        latestRunResult = runResult
        onRunComplete(runResult)
        showCompletionSummary = true
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
        showTutorial -> {
            {
                HowToPlayOverlay(
                    content = GameTutorialContent(
                        gameId = GameId.PULSE_ORBIT,
                        title = "How to Play Pulse Orbit",
                        lines = listOf(
                            "Tap when the orb reaches the opening.",
                            "Clean passes build combo.",
                            "Miss the opening and the run ends.",
                        ),
                        controls = "Tap anywhere.",
                        goal = "Beat your high score.",
                    ),
                    onPlay = { closeTutorial(startAfterClose = true) },
                    onSkip = { closeTutorial(startAfterClose = false) },
                )
            }
        }
        state.gameOver && showCompletionSummary -> {
            {
                CompletionPopup(
                    title = if (state.score > (stats?.highScore ?: 0)) "New High Score" else "Run Summary",
                    lines = listOf(
                        "Score: ${state.score}",
                        "Best combo: ${state.bestCombo}",
                        "Daily challenges and achievements updated after the run.",
                    ),
                    onContinue = { showCompletionSummary = false },
                )
            }
        }
        state.paused -> {
            {
                PremiumOverlayCard(title = "Run paused", subtitle = "Resume instantly or reset the loop.") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Score", state.score.toString())
                        StatRow("Combo", state.combo.toString())
                        PremiumButton(label = "Resume", onClick = ::togglePause, modifier = Modifier.fillMaxWidth())
                        PremiumButton(label = "How to Play", onClick = ::showHowToPlay, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
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
                        PremiumButton(label = "Back to detail", onClick = ::exitCompletedRun, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
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
                            } else if (state.gameOver && showCompletionSummary) {
                                showCompletionSummary = false
                            } else if (state.gameOver) {
                                exitCompletedRun()
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
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    PremiumButton(
                        label = "How",
                        onClick = ::showHowToPlay,
                        style = ArcadeButtonStyle.Secondary,
                        borderOverride = colors.primaryCyan,
                    )
                    PremiumButton(
                        label = if (state.paused) "Resume" else "Pause",
                        onClick = ::togglePause,
                        style = ArcadeButtonStyle.Secondary,
                        enabled = state.playing || state.paused,
                        borderOverride = colors.accentViolet,
                    )
                }
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
                        state = startPulseOrbitRun(state, System.currentTimeMillis())
                        feedback.play(ArcadeFeedbackEvent.TAP)
                        return@clickable
                    }
                    val tapResult = resolvePulseOrbitTap(state, System.currentTimeMillis())
                    state = tapResult.state
                    when (tapResult.resolution) {
                        PulseOrbitTapResolution.CLEAN_PASS,
                        PulseOrbitTapResolution.PERFECT_PASS,
                        -> feedback.play(ArcadeFeedbackEvent.SUCCESS)
                        PulseOrbitTapResolution.FAIL -> feedback.play(ArcadeFeedbackEvent.FAIL)
                        PulseOrbitTapResolution.NONE -> Unit
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
                val orbAngleRadians = state.orbitAngle * (PI.toFloat() / 180f)
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
                        text = if (state.combo % PulseOrbitTuning.comboBonusEvery == 0) "Perfect chain" else "Perfect timing",
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
