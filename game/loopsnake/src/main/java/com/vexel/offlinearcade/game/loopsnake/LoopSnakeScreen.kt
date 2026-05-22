package com.vexel.offlinearcade.game.loopsnake

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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun LoopSnakeScreen(
    onBack: () -> Unit
) {
    var gameState by remember {
        mutableStateOf(
            GameState(
                snake = listOf(SnakeBodyPart(Offset(5f, 5f))),
                food = Food(Offset(10f, 10f)),
                score = 0,
                status = GameStatus.Playing,
                direction = Direction.Right
            )
        )
    }

    var canvasSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    LaunchedEffect(Unit) {
        while (true) {
            if (gameState.status == GameStatus.Playing) {
                delay(200)
                val newSnake = gameState.snake.toMutableList()
                val head = newSnake.first()
                val newHead = when (gameState.direction) {
                    Direction.Up -> head.copy(position = head.position.copy(y = head.position.y - 1))
                    Direction.Down -> head.copy(position = head.position.copy(y = head.position.y + 1))
                    Direction.Left -> head.copy(position = head.position.copy(x = head.position.x - 1))
                    Direction.Right -> head.copy(position = head.position.copy(x = head.position.x + 1))
                }

                // Wall collision
                if (newHead.position.x < 0 || newHead.position.x * 20f > canvasSize.width || newHead.position.y < 0 || newHead.position.y * 20f > canvasSize.height) {
                    gameState = gameState.copy(status = GameStatus.GameOver)
                    continue
                }

                // Body collision
                if (newSnake.any { it.position == newHead.position }) {
                    gameState = gameState.copy(status = GameStatus.GameOver)
                    continue
                }

                newSnake.add(0, newHead)
                if (newHead.position != gameState.food.position) {
                    newSnake.removeLast()
                } else {
                    gameState = gameState.copy(
                        score = gameState.score + 1,
                        food = Food(
                            Offset(
                                (0..(canvasSize.width / 20).toInt()).random().toFloat(),
                                (0..(canvasSize.height / 20).toInt()).random().toFloat()
                            )
                        )
                    )
                }

                gameState = gameState.copy(snake = newSnake)
            }
        }
    }

    Box {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val (x, y) = dragAmount
                        if (abs(x) > abs(y)) {
                            if (x > 0 && gameState.direction != Direction.Left) {
                                gameState = gameState.copy(direction = Direction.Right)
                            } else if (x < 0 && gameState.direction != Direction.Right) {
                                gameState = gameState.copy(direction = Direction.Left)
                            }
                        } else {
                            if (y > 0 && gameState.direction != Direction.Up) {
                                gameState = gameState.copy(direction = Direction.Down)
                            } else if (y < 0 && gameState.direction != Direction.Down) {
                                gameState = gameState.copy(direction = Direction.Up)
                            }
                        }
                    }
                }
        ) {
            canvasSize = size
            // Draw background
            drawRect(Color.Black)

            // Draw snake
            gameState.snake.forEach {
                drawRect(
                    color = Color.Green,
                    topLeft = it.position * 20f,
                    size = androidx.compose.ui.geometry.Size(20f, 20f)
                )
            }

            // Draw food
            drawRect(
                color = Color.Red,
                topLeft = gameState.food.position * 20f,
                size = androidx.compose.ui.geometry.Size(20f, 20f)
            )
        }

        Text(
            text = "Score: ${gameState.score}",
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )

        if (gameState.status == GameStatus.GameOver) {
            GameOverScreen(
                score = gameState.score,
                onRestart = {
                    gameState = GameState(
                        snake = listOf(SnakeBodyPart(Offset(5f, 5f))),
                        food = Food(Offset(10f, 10f)),
                        score = 0,
                        status = GameStatus.Playing,
                        direction = Direction.Right
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
