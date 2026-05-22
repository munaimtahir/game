package com.vexel.offlinearcade.game.gravityflip

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.max

@Composable
fun GravityFlipScreen(
    onBack: () -> Unit
) {
    var state by remember {
        mutableStateOf(
            GravityFlipState(
                playerPosition = Offset(100f, 300f),
                playerVelocityY = 0f,
                gravity = 0.5f,
                obstacles = emptyList(),
                stars = emptyList(),
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
                
                // Spawn obstacles and stars
                val newObstacles = state.obstacles.toMutableList()
                val newStars = state.stars.toMutableList()
                
                if (currentTime - lastSpawnTime > 1500) {
                    val side = (0..1).random()
                    val obstacleY = if (side == 0) 50f else canvasSize.height - 100f
                    newObstacles.add(Obstacle(currentTime.toInt(), Offset(canvasSize.width + 50f, obstacleY), androidx.compose.ui.geometry.Size(50f, 50f)))
                    
                    val starY = (100..(canvasSize.height - 100).toInt()).random().toFloat()
                    newStars.add(Star(currentTime.toInt() + 1, Offset(canvasSize.width + 50f, starY)))
                    
                    lastSpawnTime = currentTime
                }

                // Move items and check collisions
                val finalObstacles = mutableListOf<Obstacle>()
                val finalStars = mutableListOf<Star>()
                var gameOver = false
                var scoreGain = 0

                // Physics
                var newVelocityY = state.playerVelocityY + state.gravity
                var newPlayerY = state.playerPosition.y + newVelocityY
                
                if (newPlayerY < 50f) {
                    newPlayerY = 50f
                    newVelocityY = 0f
                } else if (newPlayerY > canvasSize.height - 50f) {
                    newPlayerY = canvasSize.height - 50f
                    newVelocityY = 0f
                }
                
                val playerRect = Rect(Offset(100f, newPlayerY - 20f), androidx.compose.ui.geometry.Size(40f, 40f))

                for (obs in newObstacles) {
                    val newPos = obs.position.copy(x = obs.position.x - 5f)
                    if (Rect(newPos, obs.size).overlaps(playerRect)) {
                        gameOver = true
                        break
                    }
                    if (newPos.x > -100f) finalObstacles.add(obs.copy(position = newPos))
                }

                for (star in newStars) {
                    val newPos = star.position.copy(x = star.position.x - 5f)
                    if (Rect(Offset(newPos.x - 15f, newPos.y - 15f), androidx.compose.ui.geometry.Size(30f, 30f)).overlaps(playerRect)) {
                        scoreGain += 10
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
                    detectTapGestures {
                        state = state.copy(gravity = -state.gravity)
                    }
                }
        ) {
            canvasSize = size
            
            // Background
            drawRect(Color(0xFF1A1A1A))

            // Score
            drawText(
                textMeasurer = textMeasurer,
                text = "Score: ${state.score}",
                topLeft = Offset(20.dp.toPx(), 40.dp.toPx()),
                style = TextStyle(color = Color.White, fontSize = 24.sp)
            )

            // Floor & Ceiling
            drawRect(Color.DarkGray, topLeft = Offset(0f, 0f), size = androidx.compose.ui.geometry.Size(size.width, 50f))
            drawRect(Color.DarkGray, topLeft = Offset(0f, size.height - 50f), size = androidx.compose.ui.geometry.Size(size.width, 50f))

            // Player
            drawRect(Color.Cyan, topLeft = Offset(state.playerPosition.x - 20f, state.playerPosition.y - 20f), size = androidx.compose.ui.geometry.Size(40f, 40f))

            // Obstacles
            state.obstacles.forEach { obs ->
                drawRect(Color.Red, topLeft = obs.position, size = obs.size)
            }

            // Stars
            state.stars.forEach { star ->
                drawCircle(Color.Yellow, radius = 15f, center = star.position)
            }
        }

        if (state.status == GameStatus.GameOver) {
            GameOverScreen(
                score = state.score,
                onRestart = {
                    state = GravityFlipState(
                        playerPosition = Offset(100f, 300f),
                        playerVelocityY = 0f,
                        gravity = 0.5f,
                        obstacles = emptyList(),
                        stars = emptyList(),
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
