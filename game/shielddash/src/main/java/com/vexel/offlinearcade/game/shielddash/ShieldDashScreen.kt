package com.vexel.offlinearcade.game.shielddash

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vexel.offlinearcade.core.model.ArcadeFeedback
import com.vexel.offlinearcade.core.model.ArcadeFeedbackEvent
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.core.model.SettingsState
import com.vexel.offlinearcade.core.ui.*
import kotlinx.coroutines.delay
import kotlin.math.*

@Composable
fun ShieldDashScreen(
    stats: GameStats?,
    settings: SettingsState,
    feedback: ArcadeFeedback,
    onRunComplete: (RunResult) -> Unit,
    onBack: () -> Unit,
) {
    var state by remember { mutableStateOf(ShieldDashState()) }
    var canvasSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    var hasReportedRun by remember { mutableStateOf(false) }
    var paused by remember { mutableStateOf(false) }
    val textMeasurer = rememberTextMeasurer()
    val colors = ArcadeTheme.colors
    val blockPulse = remember { Animatable(0f) }

    fun restart() {
        state = ShieldDashState(
            status = GameStatus.Playing,
            runStartMillis = System.currentTimeMillis()
        )
        hasReportedRun = false
        paused = false
        feedback.play(ArcadeFeedbackEvent.TAP)
    }

    fun togglePause() {
        if (state.status == GameStatus.GameOver) return
        paused = !paused
    }

    BackHandler {
        if (state.status == GameStatus.Playing && !paused) {
            togglePause()
        } else {
            onBack()
        }
    }

    LaunchedEffect(state.status, paused) {
        if (state.status == GameStatus.Playing) {
            var lastSpawnTime = 0L
            while (state.status == GameStatus.Playing && !paused) {
                delay(16)
                val currentTime = System.currentTimeMillis()
                
                // Spawn hazards
                val currentHazards = state.hazards.toMutableList()
                val spawnInterval = (ShieldDashTuning.initialSpawnInterval - state.score * ShieldDashTuning.spawnIntervalReductionPerScore)
                    .coerceAtLeast(ShieldDashTuning.minimumSpawnInterval)
                
                if (currentTime - lastSpawnTime > spawnInterval) {
                    val angle = (0..359).random().toFloat()
                    val rad = Math.toRadians(angle.toDouble())
                    val spawnDist = max(canvasSize.width, canvasSize.height) / 2f + 100f
                    val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
                    val pos = Offset(
                        center.x + cos(rad).toFloat() * spawnDist,
                        center.y + sin(rad).toFloat() * spawnDist
                    )
                    val speed = 3.5f + (state.score / 250f)
                    val vel = Offset(
                        -cos(rad).toFloat() * speed,
                        -sin(rad).toFloat() * speed
                    )
                    currentHazards.add(Hazard(currentTime.toInt(), pos, vel))
                    lastSpawnTime = currentTime
                }

                // Move hazards and check collisions
                val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
                val finalHazards = mutableListOf<Hazard>()
                var gameOver = false
                var scoreGain = 0

                for (hazard in currentHazards) {
                    val newPos = hazard.position + hazard.velocity
                    val distToCenter = (newPos - center).getDistance()
                    
                    if (distToCenter < ShieldDashTuning.coreRadius) { // Core hit
                        gameOver = true
                        feedback.play(ArcadeFeedbackEvent.FAIL)
                        break
                    } else if (distToCenter < ShieldDashTuning.shieldOuterRadius) { // Shield hit zone
                        val hazardAngle = Math.toDegrees(atan2((newPos.y - center.y).toDouble(), (newPos.x - center.x).toDouble())).toFloat()
                        val normalizedHazardAngle = (hazardAngle + 360) % 360
                        val normalizedShieldAngle = (state.shieldAngle + 360) % 360
                        
                        val angleDiff = abs(normalizedHazardAngle - normalizedShieldAngle)
                        val wrappedDiff = min(angleDiff, 360 - angleDiff)
                        
                        if (wrappedDiff < 35f) { // Blocked
                            scoreGain += 10
                            feedback.play(ArcadeFeedbackEvent.SUCCESS)
                            blockPulse.snapTo(1f)
                            blockPulse.animateTo(0f, tween(200))
                            continue // Hazard destroyed
                        }
                    }
                    
                    finalHazards.add(hazard.copy(position = newPos))
                }

                if (gameOver) {
                    state = state.copy(status = GameStatus.GameOver)
                } else {
                    state = state.copy(
                        hazards = finalHazards,
                        score = state.score + scoreGain
                    )
                }
            }
        }
    }

    if (state.status == GameStatus.GameOver && !hasReportedRun) {
        hasReportedRun = true
        onRunComplete(
            RunResult(
                gameId = GameId.SHIELD_DASH,
                score = state.score,
                durationMillis = System.currentTimeMillis() - state.runStartMillis,
                coinsEarned = state.score / 5,
            )
        )
    }

    val overlayContent: (@Composable () -> Unit)? = when {
        paused -> {
            {
                PremiumOverlayCard(title = "Run paused", subtitle = "Core is safe.") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Score", state.score.toString())
                        PremiumButton(label = "Resume", onClick = ::togglePause, modifier = Modifier.fillMaxWidth())
                        PremiumButton(label = "Restart", onClick = ::restart, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
                        PremiumButton(label = "Quit", onClick = onBack, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
                    }
                }
            }
        }
        state.status == GameStatus.GameOver -> {
            {
                PremiumOverlayCard(
                    title = if (state.score > (stats?.highScore ?: 0)) "New High Score!" else "Game Over",
                    subtitle = "The core was breached."
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Score", state.score.toString(), valueColor = colors.reward)
                        PremiumButton(label = "Try Again", onClick = ::restart, modifier = Modifier.fillMaxWidth())
                        PremiumButton(label = "Menu", onClick = onBack, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
                    }
                }
            }
        }
        else -> null
    }

    GameplayScaffold(
        topBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    androidx.compose.material3.IconButton(onClick = onBack) {
                        androidx.compose.material3.Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = colors.textPrimary)
                    }
                    HudPill("Score", state.score.toString())
                }
                PremiumButton(
                    label = if (paused) "Resume" else "Pause",
                    onClick = ::togglePause,
                    style = ArcadeButtonStyle.Secondary,
                    enabled = state.status == GameStatus.Playing
                )
            }
        },
        overlay = overlayContent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.gameBoard, RoundedCornerShape(24.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        if (state.status != GameStatus.Playing || paused) return@detectDragGestures
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val angle = Math.toDegrees(atan2((change.position.y - center.y).toDouble(), (change.position.x - center.x).toDouble())).toFloat()
                        state = state.copy(shieldAngle = (angle + 360) % 360)
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                canvasSize = size
                val center = Offset(size.width / 2f, size.height / 2f)

                // Background ripples
                drawCircle(colors.gridLine.copy(alpha = 0.05f), radius = center.x * 0.4f, center = center, style = Stroke(2f))
                drawCircle(colors.gridLine.copy(alpha = 0.05f), radius = center.x * 0.8f, center = center, style = Stroke(2f))

                // Core
                drawCircle(color = colors.primaryCyan, radius = ShieldDashTuning.coreRadius, center = center)
                drawCircle(color = colors.primaryCyan.copy(alpha = 0.3f), radius = ShieldDashTuning.coreRadius + 10f, center = center, style = Stroke(4f))

                // Shield
                rotate(degrees = state.shieldAngle, pivot = center) {
                    val shieldRadius = ShieldDashTuning.shieldInnerRadius + blockPulse.value * 15f
                    drawArc(
                        color = colors.accentViolet,
                        startAngle = -35f,
                        sweepAngle = 70f,
                        useCenter = false,
                        topLeft = Offset(center.x - shieldRadius, center.y - shieldRadius),
                        size = Size(shieldRadius * 2, shieldRadius * 2),
                        style = Stroke(width = 10.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                    )
                }

                // Hazards
                state.hazards.forEach { h ->
                    drawCircle(color = colors.dangerCoral, radius = 12f, center = h.position)
                    drawCircle(color = colors.dangerCoral.copy(alpha = 0.2f), radius = 20f, center = h.position)
                }
            }

            if (state.status == GameStatus.Ready) {
                Box(Modifier.fillMaxSize().clickable { restart() }, contentAlignment = Alignment.Center) {
                    PremiumButton(label = "Tap to Protect Core", onClick = ::restart)
                }
            }
        }
    }
}
