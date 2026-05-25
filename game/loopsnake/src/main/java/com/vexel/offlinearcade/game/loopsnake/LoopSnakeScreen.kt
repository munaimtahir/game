package com.vexel.offlinearcade.game.loopsnake

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.PremiumOverlayCard
import com.vexel.offlinearcade.core.ui.StatRow
import com.vexel.offlinearcade.game.loopsnake.engine.LoopSnakeEngine
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.min

@Composable
fun LoopSnakeScreen(
    stats: GameStats?,
    settings: SettingsState,
    feedback: ArcadeFeedback,
    onRunComplete: (RunResult) -> Unit,
    onBack: () -> Unit
) {
    val engine = remember { LoopSnakeEngine(cols = 15, rows = 20) }
    var gameState by remember { mutableStateOf(engine.state) }
    var hasReportedRun by remember { mutableStateOf(false) }
    var runStartMillis by remember { mutableLongStateOf(0L) }

    DisposableEffect(engine) {
        engine.onStateChanged = { newState ->
            gameState = newState
        }
        onDispose {
            engine.onStateChanged = null
        }
    }

    // Audio / Haptic feedback on score increase
    LaunchedEffect(gameState.score) {
        if (gameState.score > 0) {
            feedback.play(ArcadeFeedbackEvent.PICKUP)
        }
    }

    // Audio feedback on GameOver
    LaunchedEffect(gameState.status) {
        if (gameState.status == GameStatus.GameOver) {
            feedback.play(ArcadeFeedbackEvent.FAIL)
        }
    }

<<<<<<< HEAD
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
                    newSnake.removeAt(newSnake.lastIndex)
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
=======
    // Game tick coroutine loop
    LaunchedEffect(gameState.status) {
        if (gameState.status == GameStatus.Playing) {
            while (true) {
                // Progressive speed: start at 240ms, speed up by 10ms per 20 score points, min 110ms
                val currentDelay = (240 - (gameState.score / 20) * 10).coerceAtLeast(110).toLong()
                delay(currentDelay)
                engine.tick()
>>>>>>> origin/main
            }
        }
    }

    fun start() {
        runStartMillis = System.currentTimeMillis()
        hasReportedRun = false
        engine.startGame()
        feedback.play(ArcadeFeedbackEvent.TAP)
    }

    fun restart() {
        runStartMillis = System.currentTimeMillis()
        hasReportedRun = false
        engine.reset()
        engine.startGame()
        feedback.play(ArcadeFeedbackEvent.TAP)
    }

    fun togglePause() {
        if (gameState.status == GameStatus.GameOver) return
        engine.togglePause()
        feedback.play(ArcadeFeedbackEvent.TAP)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                if (gameState.status == GameStatus.Playing) {
                    engine.togglePause()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    BackHandler {
        if (gameState.status == GameStatus.Playing) {
            engine.togglePause()
        } else {
            onBack()
        }
    }

    // Record progression when game ends
    if (gameState.status == GameStatus.GameOver && !hasReportedRun) {
        hasReportedRun = true
        val lengthGrown = (gameState.snake.size - 3).coerceAtLeast(0)
        onRunComplete(
            RunResult(
                gameId = GameId.LOOP_SNAKE,
                score = gameState.score,
                durationMillis = System.currentTimeMillis() - runStartMillis,
                pickupsCollected = lengthGrown,
                coinsEarned = lengthGrown * 2 + gameState.score / 10
            )
        )
    }

    val colors = ArcadeTheme.colors

    val overlayContent: (@Composable () -> Unit)? = when (gameState.status) {
        GameStatus.Ready -> {
            {
                PremiumOverlayCard(
                    title = "Loop Snake",
                    subtitle = "Swipe to steer, collect glowing orbs, and avoid hitting the boundaries or your own tail.",
                    modifier = Modifier.testTag(ArcadeTestTags.LoopSnakeReady)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        PremiumButton(
                            label = "Start Run",
                            onClick = ::start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(ArcadeTestTags.LoopSnakeStartButton)
                        )
                        PremiumButton(
                            label = "Quit",
                            onClick = onBack,
                            modifier = Modifier.fillMaxWidth(),
                            style = ArcadeButtonStyle.Secondary
                        )
                    }
                }
            }
        }
        GameStatus.Paused -> {
            {
                PremiumOverlayCard(
                    title = "Run Paused",
                    subtitle = "Resume to continue, or restart the session.",
                    modifier = Modifier.testTag(ArcadeTestTags.LoopSnakePause)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Current Score", gameState.score.toString())
                        PremiumButton(
                            label = "Resume",
                            onClick = ::togglePause,
                            modifier = Modifier.fillMaxWidth()
                        )
                        PremiumButton(
                            label = "Restart",
                            onClick = ::restart,
                            modifier = Modifier.fillMaxWidth(),
                            style = ArcadeButtonStyle.Secondary
                        )
                        PremiumButton(
                            label = "Quit",
                            onClick = onBack,
                            modifier = Modifier.fillMaxWidth(),
                            style = ArcadeButtonStyle.Secondary
                        )
                    }
                }
            }
        }
        GameStatus.GameOver -> {
            {
                val lengthGrown = (gameState.snake.size - 3).coerceAtLeast(0)
                val isHighScore = gameState.score > (stats?.highScore ?: 0)
                PremiumOverlayCard(
                    title = if (isHighScore) "New Record!" else "Game Over",
                    subtitle = "Keep the loops tight and retry.",
                    modifier = Modifier.testTag(ArcadeTestTags.LoopSnakeGameOver)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Final Score", gameState.score.toString(), valueColor = colors.reward)
                        StatRow("Snake Length", gameState.snake.size.toString(), valueColor = colors.success)
                        StatRow("Coins Earned", (lengthGrown * 2 + gameState.score / 10).toString(), valueColor = colors.reward)
                        PremiumButton(
                            label = "Try Again",
                            onClick = ::restart,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(ArcadeTestTags.LoopSnakeRestart)
                        )
                        PremiumButton(
                            label = "Back to Details",
                            onClick = onBack,
                            modifier = Modifier.fillMaxWidth(),
                            style = ArcadeButtonStyle.Secondary
                        )
                    }
                }
            }
        }
        else -> null
    }

    GameplayScaffold(
        modifier = Modifier.testTag(ArcadeTestTags.LoopSnakeRoot),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            if (gameState.status == GameStatus.Playing) {
                                togglePause()
                            } else {
                                onBack()
                            }
                        },
                        modifier = Modifier.testTag(ArcadeTestTags.BackButton)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.textPrimary
                        )
                    }
                    HudPill("Score", gameState.score.toString(), modifier = Modifier.testTag(ArcadeTestTags.LoopSnakeScore))
                    HudPill("Length", gameState.snake.size.toString(), modifier = Modifier.testTag(ArcadeTestTags.LoopSnakeLength))
                }
                PremiumButton(
                    label = if (gameState.status == GameStatus.Paused) "Resume" else "Pause",
                    onClick = ::togglePause,
                    style = ArcadeButtonStyle.Secondary,
                    enabled = gameState.status == GameStatus.Playing || gameState.status == GameStatus.Paused,
                    modifier = Modifier.testTag(ArcadeTestTags.LoopSnakePause)
                )
            }
        },
        overlay = overlayContent
    ) {
        var accumulatedDrag by remember { mutableStateOf(Offset.Zero) }
        val swipeMinDistancePx = with(LocalDensity.current) { 36.dp.toPx() }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .testTag(ArcadeTestTags.LoopSnakePlayArea)
                .semantics {
                    stateDescription = "score=${gameState.score};status=${gameState.status};length=${gameState.snake.size}"
                }
                .background(colors.gameBoard, RoundedCornerShape(28.dp))
                .pointerInput(gameState.status) {
                    detectDragGestures(
                        onDragStart = { accumulatedDrag = Offset.Zero },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            if (gameState.status == GameStatus.Playing) {
                                accumulatedDrag += dragAmount

                                val absX = abs(accumulatedDrag.x)
                                val absY = abs(accumulatedDrag.y)

                                if (absX >= swipeMinDistancePx || absY >= swipeMinDistancePx) {
                                    if (absX > absY) {
                                        if (accumulatedDrag.x > 0) {
                                            engine.setDirection(Direction.Right)
                                        } else {
                                            engine.setDirection(Direction.Left)
                                        }
                                    } else {
                                        if (accumulatedDrag.y > 0) {
                                            engine.setDirection(Direction.Down)
                                        } else {
                                            engine.setDirection(Direction.Up)
                                        }
                                    }
                                    accumulatedDrag = Offset.Zero
                                }
                            }
                        }
                    )
                }
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val pulseScale by infiniteTransition.animateFloat(
                initialValue = 0.82f,
                targetValue = 1.18f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse"
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val cols = engine.cols
                val rows = engine.rows
                val cellWidth = size.width / cols
                val cellHeight = size.height / rows
                val cellSize = min(cellWidth, cellHeight)
                val offsetX = (size.width - cellSize * cols) / 2f
                val offsetY = (size.height - cellSize * rows) / 2f

                // Draw subtle grid lines for premium retro-arcade styling
                val gridColor = colors.gameBoardRaised.copy(alpha = 0.15f)
                for (c in 0..cols) {
                    val x = offsetX + c * cellSize
                    drawLine(
                        color = gridColor,
                        start = Offset(x, offsetY),
                        end = Offset(x, offsetY + rows * cellSize),
                        strokeWidth = 1f
                    )
                }
                for (r in 0..rows) {
                    val y = offsetY + r * cellSize
                    drawLine(
                        color = gridColor,
                        start = Offset(offsetX, y),
                        end = Offset(offsetX + cols * cellSize, y),
                        strokeWidth = 1f
                    )
                }

                // Draw Food (Pulsing glowing orb)
                val foodCenter = Offset(
                    offsetX + gameState.food.position.x * cellSize + cellSize / 2f,
                    offsetY + gameState.food.position.y * cellSize + cellSize / 2f
                )
                drawCircle(
                    color = colors.reward.copy(alpha = 0.35f),
                    radius = cellSize * 0.46f * pulseScale,
                    center = foodCenter
                )
                drawCircle(
                    color = colors.reward,
                    radius = cellSize * 0.28f,
                    center = foodCenter
                )

                // Draw Snake segments using lerped gradient colors (accent to reward)
                gameState.snake.forEachIndexed { index, part ->
                    val t = if (gameState.snake.size > 1) index.toFloat() / (gameState.snake.size - 1) else 0f
                    val lerpColor = lerpColor(colors.loopSnakeAccent, colors.reward, t)
                    val partCenter = Offset(
                        offsetX + part.position.x * cellSize + cellSize / 2f,
                        offsetY + part.position.y * cellSize + cellSize / 2f
                    )

                    drawCircle(
                        color = lerpColor,
                        radius = cellSize * 0.44f,
                        center = partCenter
                    )

                    // Draw eyes on the head segment (index 0)
                    if (index == 0) {
                        val eyeColor = Color.White
                        val pupilColor = Color.Black
                        val eyeRadius = cellSize * 0.08f
                        val pupilRadius = cellSize * 0.04f

                        val (eye1, eye2) = when (gameState.direction) {
                            Direction.Right -> {
                                val ex = partCenter.x + cellSize * 0.16f
                                Pair(Offset(ex, partCenter.y - cellSize * 0.16f), Offset(ex, partCenter.y + cellSize * 0.16f))
                            }
                            Direction.Left -> {
                                val ex = partCenter.x - cellSize * 0.16f
                                Pair(Offset(ex, partCenter.y - cellSize * 0.16f), Offset(ex, partCenter.y + cellSize * 0.16f))
                            }
                            Direction.Up -> {
                                val ey = partCenter.y - cellSize * 0.16f
                                Pair(Offset(partCenter.x - cellSize * 0.16f, ey), Offset(partCenter.x + cellSize * 0.16f, ey))
                            }
                            Direction.Down -> {
                                val ey = partCenter.y + cellSize * 0.16f
                                Pair(Offset(partCenter.x - cellSize * 0.16f, ey), Offset(partCenter.x + cellSize * 0.16f, ey))
                            }
                        }

                        drawCircle(color = eyeColor, radius = eyeRadius, center = eye1)
                        drawCircle(color = eyeColor, radius = eyeRadius, center = eye2)
                        drawCircle(color = pupilColor, radius = pupilRadius, center = eye1)
                        drawCircle(color = pupilColor, radius = pupilRadius, center = eye2)
                    }
                }
            }
        }
    }
}

private fun lerpColor(c1: Color, c2: Color, t: Float): Color {
    return Color(
        red = c1.red + (c2.red - c1.red) * t,
        green = c1.green + (c2.green - c1.green) * t,
        blue = c1.blue + (c2.blue - c1.blue) * t,
        alpha = c1.alpha + (c2.alpha - c1.alpha) * t
    )
}
