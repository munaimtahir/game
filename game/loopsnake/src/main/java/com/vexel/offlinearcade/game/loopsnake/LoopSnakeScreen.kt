package com.vexel.offlinearcade.game.loopsnake

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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
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
import kotlin.math.abs

@Composable
fun LoopSnakeScreen(
    stats: GameStats?,
    settings: SettingsState,
    feedback: ArcadeFeedback,
    onRunComplete: (RunResult) -> Unit,
    onBack: () -> Unit,
) {
    var state by remember { mutableStateOf(LoopSnakeState()) }
    var hasReportedRun by remember { mutableStateOf(false) }
    var paused by remember { mutableStateOf(false) }
    val colors = ArcadeTheme.colors
    val successPulse = remember { Animatable(0f) }

    fun restart() {
        state = LoopSnakeState(
            status = GameStatus.Playing,
            runStartMillis = System.currentTimeMillis(),
            lastTickMillis = System.currentTimeMillis()
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
        while (state.status == GameStatus.Playing && !paused) {
            delay(state.speedDelay)
            val newSnake = state.snake.toMutableList()
            val head = newSnake.first()
            val newHeadPos = when (state.direction) {
                Direction.Up -> head.position.copy(y = head.position.y - 1)
                Direction.Down -> head.position.copy(y = head.position.y + 1)
                Direction.Left -> head.position.copy(x = head.position.x - 1)
                Direction.Right -> head.position.copy(x = head.position.x + 1)
            }

            // Wall collision
            if (newHeadPos.x < 0 || newHeadPos.x >= LoopSnakeTuning.gridCellsX || 
                newHeadPos.y < 0 || newHeadPos.y >= LoopSnakeTuning.gridCellsY) {
                state = state.copy(status = GameStatus.GameOver)
                feedback.play(ArcadeFeedbackEvent.FAIL)
                continue
            }

            // Body collision
            if (newSnake.any { it.position == newHeadPos }) {
                state = state.copy(status = GameStatus.GameOver)
                feedback.play(ArcadeFeedbackEvent.FAIL)
                continue
            }

            val nextHead = SnakeBodyPart(newHeadPos)
            newSnake.add(0, nextHead)
            
            var nextScore = state.score
            var nextFood = state.food
            var nextDelay = state.speedDelay

            if (newHeadPos == state.food.position) {
                nextScore++
                feedback.play(ArcadeFeedbackEvent.SUCCESS)
                successPulse.snapTo(1f)
                successPulse.animateTo(0f, animationSpec = tween(300))
                
                // Spawn new food not on snake
                var validFood = false
                var foodPos = Offset.Zero
                while (!validFood) {
                    foodPos = Offset(
                        (0 until LoopSnakeTuning.gridCellsX).random().toFloat(),
                        (0 until LoopSnakeTuning.gridCellsY).random().toFloat()
                    )
                    validFood = newSnake.none { it.position == foodPos }
                }
                nextFood = Food(foodPos)
                nextDelay = (LoopSnakeTuning.initialDelay - nextScore * LoopSnakeTuning.delayReductionPerScore)
                    .coerceAtLeast(LoopSnakeTuning.minimumDelay)
            } else {
                newSnake.removeAt(newSnake.size - 1)
            }

            state = state.copy(
                snake = newSnake,
                score = nextScore,
                food = nextFood,
                speedDelay = nextDelay
            )
        }
    }

    if (state.status == GameStatus.GameOver && !hasReportedRun) {
        hasReportedRun = true
        onRunComplete(
            RunResult(
                gameId = GameId.LOOP_SNAKE,
                score = state.score,
                durationMillis = System.currentTimeMillis() - state.runStartMillis,
                coinsEarned = state.score * 5,
            )
        )
    }

    val overlayContent: (@Composable () -> Unit)? = when {
        paused -> {
            {
                PremiumOverlayCard(title = "Paused", subtitle = "Snake is resting.") {
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
                    title = if (state.score > (stats?.highScore ?: 0)) "New Record!" else "Game Over",
                    subtitle = "The loop ends here."
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
                    HudPill("Score", state.score.toString(), modifier = Modifier.testTag(ArcadeTestTags.LoopSnakeScore))
                    HudPill("Length", state.snake.size.toString(), modifier = Modifier.testTag(ArcadeTestTags.LoopSnakeLength))
                }
                PremiumButton(
                    label = if (paused) "Resume" else "Pause",
                    onClick = ::togglePause,
                    style = ArcadeButtonStyle.Secondary,
                    enabled = state.status == GameStatus.Playing,
                    modifier = Modifier.testTag(ArcadeTestTags.LoopSnakePause)
                )
            }
        },
        overlay = overlayContent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag(ArcadeTestTags.LoopSnakeRoot)
                .background(colors.gameBoard, RoundedCornerShape(24.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        if (state.status != GameStatus.Playing || paused) return@detectDragGestures
                        val (x, y) = dragAmount
                        state = if (abs(x) > abs(y)) {
                            if (x > 0 && state.direction != Direction.Left) state.copy(direction = Direction.Right)
                            else if (x < 0 && state.direction != Direction.Right) state.copy(direction = Direction.Left)
                            else state
                        } else {
                            if (y > 0 && state.direction != Direction.Up) state.copy(direction = Direction.Down)
                            else if (y < 0 && state.direction != Direction.Down) state.copy(direction = Direction.Up)
                            else state
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(12.dp).testTag(ArcadeTestTags.LoopSnakePlayArea)) {
                val cellSizeX = size.width / LoopSnakeTuning.gridCellsX
                val cellSizeY = size.height / LoopSnakeTuning.gridCellsY
                
                // Grid lines (subtle)
                for (i in 0..LoopSnakeTuning.gridCellsX) {
                    drawLine(colors.gridLine.copy(alpha = 0.1f), Offset(i * cellSizeX, 0f), Offset(i * cellSizeX, size.height))
                }
                for (i in 0..LoopSnakeTuning.gridCellsY) {
                    drawLine(colors.gridLine.copy(alpha = 0.1f), Offset(0f, i * cellSizeY), Offset(size.width, i * cellSizeY))
                }

                // Food
                drawCircle(
                    color = colors.pickupMint,
                    radius = (cellSizeX * 0.4f) + successPulse.value * 10f,
                    center = Offset(
                        state.food.position.x * cellSizeX + cellSizeX / 2,
                        state.food.position.y * cellSizeY + cellSizeY / 2
                    )
                )
                drawCircle(
                    color = colors.pickupMint.copy(alpha = 0.3f),
                    radius = cellSizeX * 0.7f,
                    center = Offset(
                        state.food.position.x * cellSizeX + cellSizeX / 2,
                        state.food.position.y * cellSizeY + cellSizeY / 2
                    ),
                    style = Stroke(width = 2.dp.toPx())
                )

                // Snake
                state.snake.forEachIndexed { index, part ->
                    val color = if (index == 0) colors.primaryCyan else colors.primaryCyan.copy(alpha = 0.7f)
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(part.position.x * cellSizeX + 2f, part.position.y * cellSizeY + 2f),
                        size = Size(cellSizeX - 4f, cellSizeY - 4f),
                        cornerRadius = CornerRadius(8.dp.toPx())
                    )
                }
            }

            if (state.status == GameStatus.Ready) {
                Box(Modifier.fillMaxSize().clickable { restart() }.testTag(ArcadeTestTags.LoopSnakeReady), contentAlignment = Alignment.Center) {
                    PremiumButton(label = "Tap to Start", onClick = ::restart, modifier = Modifier.testTag(ArcadeTestTags.LoopSnakeStartButton))
                }
            }
        }
    }
}
