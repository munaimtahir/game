package com.vexel.offlinearcade.game.shielddash

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.*

@Composable
fun ShieldDashScreen(
    onBack: () -> Unit
) {
    var state by remember {
        mutableStateOf(
            ShieldDashState(
                shieldAngle = 0f,
                hazards = emptyList(),
                score = 0,
                status = GameStatus.Playing
            )
        )
    }

    var canvasSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    val textMeasurer = rememberTextMeasurer()

    LaunchedEffect(state.status) {
        if (state.status == GameStatus.Playing) {
            var lastSpawnTime = 0L
            while (state.status == GameStatus.Playing) {
                delay(16)
                val currentTime = System.currentTimeMillis()
                
                // Spawn hazards
                val newHazards = state.hazards.toMutableList()
                if (currentTime - lastSpawnTime > 2000) {
                    val angle = (0..359).random().toFloat()
                    val rad = Math.toRadians(angle.toDouble())
                    val spawnDist = max(canvasSize.width, canvasSize.height) / 2f + 50f
                    val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
                    val pos = Offset(
                        center.x + cos(rad).toFloat() * spawnDist,
                        center.y + sin(rad).toFloat() * spawnDist
                    )
                    val vel = Offset(
                        -cos(rad).toFloat() * 3f,
                        -sin(rad).toFloat() * 3f
                    )
                    newHazards.add(Hazard(currentTime.toInt(), pos, vel))
                    lastSpawnTime = currentTime
                }

                // Move hazards and check collisions
                val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
                val finalHazards = mutableListOf<Hazard>()
                var gameOver = false
                var scoreGain = 0

                for (hazard in newHazards) {
                    val newPos = hazard.position + hazard.velocity
                    val distToCenter = (newPos - center).getDistance()
                    
                    if (distToCenter < 40f) { // Core hit
                        gameOver = true
                        break
                    } else if (distToCenter < 70f) { // Shield hit zone
                        val hazardAngle = Math.toDegrees(atan2((newPos.y - center.y).toDouble(), (newPos.x - center.x).toDouble())).toFloat()
                        val normalizedHazardAngle = (hazardAngle + 360) % 360
                        val normalizedShieldAngle = (state.shieldAngle + 360) % 360
                        
                        val angleDiff = abs(normalizedHazardAngle - normalizedShieldAngle)
                        val wrappedDiff = min(angleDiff, 360 - angleDiff)
                        
                        if (wrappedDiff < 30f) { // Blocked
                            scoreGain += 10
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

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
                        val touchPos = change.position
                        val angle = Math.toDegrees(atan2((touchPos.y - center.y).toDouble(), (touchPos.x - center.x).toDouble())).toFloat()
                        state = state.copy(shieldAngle = (angle + 360) % 360)
                    }
                }
        ) {
            canvasSize = size
            val center = Offset(size.width / 2f, size.height / 2f)

            // Background
            drawRect(Color(0xFF121212))

            // Score
            drawText(
                textMeasurer = textMeasurer,
                text = "Score: ${state.score}",
                topLeft = Offset(20.dp.toPx(), 40.dp.toPx()),
                style = TextStyle(color = Color.White, fontSize = 24.sp)
            )

            // Core
            drawCircle(color = Color.Cyan, radius = 30f, center = center)

            // Shield
            rotate(degrees = state.shieldAngle, pivot = center) {
                drawArc(
                    color = Color.White,
                    startAngle = -30f,
                    sweepAngle = 60f,
                    useCenter = false,
                    topLeft = Offset(center.x - 60f, center.y - 60f),
                    size = androidx.compose.ui.geometry.Size(120f, 120f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 10f)
                )
            }

            // Hazards
            state.hazards.forEach { hazard ->
                drawCircle(color = Color.Red, radius = 10f, center = hazard.position)
            }
        }

        if (state.status == GameStatus.GameOver) {
            GameOverScreen(
                score = state.score,
                onRestart = {
                    state = ShieldDashState(
                        shieldAngle = 0f,
                        hazards = emptyList(),
                        score = 0,
                        status = GameStatus.Playing
                    )
                }
            )
        }
    }
}

@Composable
fun GameOverScreen(score: Int, onRestart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Game Over", color = Color.White, fontSize = 48.sp)
            Text("Score: $score", color = Color.White, fontSize = 32.sp)
            Button(
                onClick = onRestart,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Restart")
            }
        }
    }
}
