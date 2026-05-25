package com.vexel.offlinearcade.game.gravityflip

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.ArcadeFeedback
import com.vexel.offlinearcade.core.model.ArcadeFeedbackEvent
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.core.model.SettingsState
import com.vexel.offlinearcade.core.ui.*
import kotlinx.coroutines.delay

@Composable
fun GravityFlipScreen(
    stats: GameStats?,
    settings: SettingsState,
    feedback: ArcadeFeedback,
    onRunComplete: (RunResult) -> Unit,
    onBack: () -> Unit,
) {
    var state by remember { mutableStateOf(GravityFlipState()) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    var hasReportedRun by remember { mutableStateOf(false) }
    var paused by remember { mutableStateOf(false) }
    val colors = ArcadeTheme.colors
    val starPulse = remember { Animatable(0f) }

    fun restart() {
        state = GravityFlipState(
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
                
                // Spawn obstacles and stars
                val currentObstacles = state.obstacles.toMutableList()
                val currentStars = state.stars.toMutableList()
                val spawnInterval = (GravityFlipTuning.initialSpawnInterval - (state.score * 5L))
                    .coerceAtLeast(GravityFlipTuning.minimumSpawnInterval)
                
                if (currentTime - lastSpawnTime > spawnInterval) {
                    val side = (0..1).random()
                    val obsSize = Size(60f, 60f)
                    val obstacleY = if (side == 0) 60f else canvasSize.height - 120f
                    currentObstacles.add(Obstacle(currentTime.toInt(), Offset(canvasSize.width + 60f, obstacleY), obsSize))
                    
                    val starY = (150..(canvasSize.height - 150).toInt()).random().toFloat()
                    currentStars.add(Star(currentTime.toInt() + 1, Offset(canvasSize.width + 100f, starY)))
                    
                    lastSpawnTime = currentTime
                }

                // Physics & Movement
                var newVelocityY = state.playerVelocityY + (if (state.gravity > 0) GravityFlipTuning.gravityForce else -GravityFlipTuning.gravityForce)
                var newPlayerY = state.playerPosition.y + newVelocityY
                
                if (newPlayerY < 60f) {
                    newPlayerY = 60f
                    newVelocityY = 0f
                } else if (newPlayerY > canvasSize.height - 60f) {
                    newPlayerY = canvasSize.height - 60f
                    newVelocityY = 0f
                }
                
                val playerRect = Rect(Offset(state.playerPosition.x - 25f, newPlayerY - 25f), Size(50f, 50f))
                val finalObstacles = mutableListOf<Obstacle>()
                val finalStars = mutableListOf<Star>()
                var gameOver = false
                var scoreGain = 0
                val currentSpeed = state.speed

                for (obs in currentObstacles) {
                    val newPos = obs.position.copy(x = obs.position.x - currentSpeed)
                    if (Rect(newPos, obs.size).overlaps(playerRect)) {
                        gameOver = true
                        feedback.play(ArcadeFeedbackEvent.FAIL)
                        break
                    }
                    if (newPos.x > -100f) finalObstacles.add(obs.copy(position = newPos))
                }

                for (star in currentStars) {
                    val newPos = star.position.copy(x = star.position.x - currentSpeed)
                    if (Rect(Offset(newPos.x - 20f, newPos.y - 20f), Size(40f, 40f)).overlaps(playerRect)) {
                        scoreGain += 10
                        feedback.play(ArcadeFeedbackEvent.SUCCESS)
                        starPulse.snapTo(1f)
                        starPulse.animateTo(0f, tween(200))
                        continue
                    }
                    if (newPos.x > -100f) finalStars.add(star.copy(position = newPos))
                }

                if (gameOver) {
                    state = state.copy(status = GameStatus.GameOver)
                } else {
                    state = state.copy(
                        playerPosition = state.playerPosition.copy(y = newPlayerY),
                        playerVelocityY = newVelocityY,
                        obstacles = finalObstacles,
                        stars = finalStars,
                        score = state.score + scoreGain,
                        speed = (state.speed + GravityFlipTuning.speedRamp * 0.016f).coerceAtMost(GravityFlipTuning.maxSpeed)
                    )
                }
            }
        }
    }

    if (state.status == GameStatus.GameOver && !hasReportedRun) {
        hasReportedRun = true
        onRunComplete(
            RunResult(
                gameId = GameId.GRAVITY_FLIP,
                score = state.score,
                durationMillis = System.currentTimeMillis() - state.runStartMillis,
                coinsEarned = state.score / 2,
            )
        )
    }

    val overlayContent: (@Composable () -> Unit)? = when {
        paused -> {
            {
                PremiumOverlayCard(title = "Paused", subtitle = "Gravity is suspended.") {
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
                    title = if (state.score > (stats?.highScore ?: 0)) "New Galaxy Best!" else "Ship Down",
                    subtitle = "Collision detected."
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Score", state.score.toString(), valueColor = colors.reward)
                        PremiumButton(label = "Retry", onClick = ::restart, modifier = Modifier.fillMaxWidth())
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
                    detectTapGestures {
                        if (state.status == GameStatus.Playing && !paused) {
                            state = state.copy(gravity = -state.gravity)
                            feedback.play(ArcadeFeedbackEvent.TAP)
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                canvasSize = size
                
                // Floor & Ceiling
                drawRect(colors.gridLine.copy(alpha = 0.2f), topLeft = Offset(0f, 0f), size = Size(size.width, 60f))
                drawRect(colors.gridLine.copy(alpha = 0.2f), topLeft = Offset(0f, size.height - 60f), size = Size(size.width, 60f))

                // Player
                drawRoundRect(
                    color = colors.primaryCyan,
                    topLeft = Offset(state.playerPosition.x - 25f, state.playerPosition.y - 25f),
                    size = Size(50f, 50f),
                    cornerRadius = CornerRadius(12f)
                )

                // Obstacles
                state.obstacles.forEach { obs ->
                    drawRoundRect(
                        color = colors.dangerCoral,
                        topLeft = obs.position,
                        size = obs.size,
                        cornerRadius = CornerRadius(8f)
                    )
                }

                // Stars
                state.stars.forEach { star ->
                    val radius = 18f + starPulse.value * 10f
                    drawCircle(colors.reward, radius = radius, center = star.position)
                    drawCircle(colors.reward.copy(alpha = 0.3f), radius = radius + 8f, center = star.position, style = Stroke(2f))
                }
            }

            if (state.status == GameStatus.Ready) {
                Box(Modifier.fillMaxSize().clickable { restart() }, contentAlignment = Alignment.Center) {
                    PremiumButton(label = "Tap to Launch", onClick = ::restart)
                }
            }
        }
    }
}
