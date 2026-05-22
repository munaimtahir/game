package com.vexel.offlinearcade.game.brickvolley

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BrickVolleyScreen(
    onBack: () -> Unit
) {
    var gameState by remember {
        mutableStateOf(
            GameState(
                bricks = listOf(
                    Brick(1, 0, 0, 1, Color.Red),
                    Brick(2, 0, 1, 1, Color.Red),
                    Brick(3, 0, 2, 1, Color.Red),
                    Brick(4, 1, 0, 1, Color.Green),
                    Brick(5, 1, 1, 1, Color.Green),
                    Brick(6, 1, 2, 1, Color.Green),
                ),
                balls = emptyList(),
                score = 0,
                turn = 1,
                status = GameStatus.Ready,
                aimingLine = null
            )
        )
    }

    var canvasSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    val textMeasurer = rememberTextMeasurer()

    LaunchedEffect(gameState.status) {
        if (gameState.status == GameStatus.Animating) {
            val balls = mutableListOf<Ball>()
            val angle = atan2(
                gameState.aimingLine!!.start.y - gameState.aimingLine!!.end.y,
                gameState.aimingLine!!.start.x - gameState.aimingLine!!.end.x
            )
            balls.add(
                Ball(
                    id = 1,
                    position = gameState.aimingLine!!.start,
                    velocity = Offset(cos(angle) * 10f, sin(angle) * 10f)
                )
            )
            gameState = gameState.copy(balls = balls)

            while (gameState.status == GameStatus.Animating) {
                val newBricks = gameState.bricks.toMutableList()
                val newBalls = gameState.balls.map { ball ->
                    var newPosition = ball.position + ball.velocity
                    var newVelocity = ball.velocity

                    // Wall collision
                    if (newPosition.x < 0 || newPosition.x > canvasSize.width) {
                        newVelocity = newVelocity.copy(x = -newVelocity.x)
                        newPosition = ball.position + newVelocity
                    }
                    if (newPosition.y < 0) {
                        newVelocity = newVelocity.copy(y = -newVelocity.y)
                        newPosition = ball.position + newVelocity
                    }

                    // Brick collision
                    var collision = false
                    for (brick in newBricks) {
                        val brickRect = androidx.compose.ui.geometry.Rect(
                            left = brick.col * 100f,
                            top = brick.row * 50f,
                            right = (brick.col + 1) * 100f,
                            bottom = (brick.row + 1) * 50f
                        )
                        if (brickRect.contains(newPosition)) {
                            collision = true
                            brick.hp--
                            if (brick.hp <= 0) {
                                newBricks.remove(brick)
                            }
                            newVelocity = newVelocity.copy(y = -newVelocity.y) // Simple bounce
                            gameState = gameState.copy(score = gameState.score + 1)
                            break // only one collision per frame
                        }
                    }

                    // Return to bottom
                    if (newPosition.y > canvasSize.height) {
                        null
                    } else {
                        ball.copy(position = newPosition, velocity = if (collision) newVelocity else ball.velocity)
                    }
                }.filterNotNull()

                gameState = gameState.copy(balls = newBalls, bricks = newBricks)

                if (newBalls.isEmpty()) {
                    val newBricksWithAdvancedRows = newBricks.map { it.copy(row = it.row + 1) }.toMutableList()
                    val newRow = (0..5).mapNotNull { col ->
                        if (Math.random() > 0.5) {
                            Brick(
                                id = (gameState.turn + 1) * 100 + col,
                                row = 0,
                                col = col,
                                hp = gameState.turn,
                                color = Color.Yellow
                            )
                        } else {
                            null
                        }
                    }
                    newBricksWithAdvancedRows.addAll(newRow)

                    if (newBricksWithAdvancedRows.any { it.row > 10 }) {
                        gameState = gameState.copy(status = GameStatus.GameOver)
                    } else {
                        gameState = gameState.copy(
                            status = GameStatus.Ready,
                            bricks = newBricksWithAdvancedRows,
                            turn = gameState.turn + 1
                        )
                    }
                }
                delay(16)
            }
        }
    }

    Box {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { start ->
                            if (gameState.status == GameStatus.Ready) {
                                gameState = gameState.copy(
                                    status = GameStatus.Aiming,
                                    aimingLine = AimingLine(start, start)
                                )
                            }
                        },
                        onDrag = { change, _ ->
                            if (gameState.status == GameStatus.Aiming) {
                                change.consume()
                                val currentPos = change.position
                                gameState = gameState.copy(
                                    aimingLine = gameState.aimingLine?.copy(end = currentPos)
                                )
                            }
                        },
                        onDragEnd = {
                            if (gameState.status == GameStatus.Aiming) {
                                gameState = gameState.copy(status = GameStatus.Animating)
                            }
                        }
                    )
                }
        ) {
            canvasSize = size
            drawRect(Color.Black)
            drawText(
                textMeasurer = textMeasurer,
                text = "Score: ${gameState.score}",
                topLeft = Offset(100f, 100f),
                style = TextStyle(color = Color.White, fontSize = 24.sp)
            )
            gameState.bricks.forEach { brick ->
                drawRect(
                    color = brick.color,
                    topLeft = Offset(brick.col * 100f, brick.row * 50f),
                    size = androidx.compose.ui.geometry.Size(100f, 50f)
                )
            }
            gameState.balls.forEach { ball ->
                drawCircle(
                    color = Color.White,
                    radius = 10f,
                    center = ball.position
                )
            }
            gameState.aimingLine?.let { line ->
                if (gameState.status == GameStatus.Aiming) {
                    drawLine(
                        color = Color.White,
                        start = line.start,
                        end = line.end,
                        strokeWidth = 5f
                    )
                }
            }
        }

        if (gameState.status == GameStatus.GameOver) {
            GameOverScreen(
                score = gameState.score,
                onRestart = {
                    gameState = GameState(
                        bricks = listOf(
                            Brick(1, 0, 0, 1, Color.Red),
                            Brick(2, 0, 1, 1, Color.Red),
                            Brick(3, 0, 2, 1, Color.Red),
                            Brick(4, 1, 0, 1, Color.Green),
                            Brick(5, 1, 1, 1, Color.Green),
                            Brick(6, 1, 2, 1, Color.Green),
                        ),
                        balls = emptyList(),
                        score = 0,
                        turn = 1,
                        status = GameStatus.Ready,
                        aimingLine = null
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
            Button(onClick = onRestart) {
                Text("Restart")
            }
        }
    }
}
