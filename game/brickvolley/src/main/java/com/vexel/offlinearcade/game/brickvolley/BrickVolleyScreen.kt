package com.vexel.offlinearcade.game.brickvolley

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.vexel.offlinearcade.core.model.ArcadeFeedback
import com.vexel.offlinearcade.core.model.ArcadeFeedbackEvent
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.core.model.SettingsState
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import kotlinx.coroutines.delay
import com.vexel.offlinearcade.game.brickvolley.engine.BrickVolleyEngine

@Composable
fun BrickVolleyScreen(
    stats: GameStats?,
    settings: SettingsState,
    feedback: ArcadeFeedback,
    onRunComplete: (RunResult) -> Unit,
    onBack: () -> Unit
) {
    fun initialGameState(): GameState = GameState(
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

    var gameState by remember { mutableStateOf(initialGameState()) }
    var runStartMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var hasReportedRun by remember { mutableStateOf(false) }

    var canvasSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    val reducedEffects = settings.reducedEffects
    fun launcher(): Offset = Offset(canvasSize.width / 2f, (canvasSize.height - 48f).coerceAtLeast(48f))

    fun restart() {
        gameState = initialGameState()
        runStartMillis = System.currentTimeMillis()
        hasReportedRun = false
    }

    BackHandler { onBack() }

    LaunchedEffect(gameState.status) {
        if (gameState.status == GameStatus.Animating) {
            val balls = mutableListOf<Ball>()
            val aiming = gameState.aimingLine
            if (aiming != null && BrickVolleyEngine.shouldLaunch(aiming.start, aiming.end)) {
                val angle = BrickVolleyEngine.dragToAngle(aiming.start, aiming.end)
                // scale speed by screen width so behavior is stable across devices
                val speedScale = (canvasSize.width / 1080f).coerceAtLeast(0.5f)
                val velocity = BrickVolleyEngine.angleToVelocity(angle, BrickVolleyEngine.BASE_SPEED * speedScale)
                balls.add(
                    Ball(
                        id = 1,
                        position = aiming.start,
                        velocity = velocity
                    )
                )
                feedback.play(ArcadeFeedbackEvent.TAP)
            } else {
                gameState = gameState.copy(status = GameStatus.Ready, aimingLine = null)
                return@LaunchedEffect
            }
            gameState = gameState.copy(balls = balls)

            var frameCount = 0
            while (gameState.status == GameStatus.Animating) {
                frameCount++
                // fixed timestep frame
                val ballRadius = 10f * (canvasSize.width / 1080f).coerceAtLeast(0.5f)
                val brickW = canvasSize.width / 6f
                val brickH = 50f * (canvasSize.width / 1080f)

                val newBricks = gameState.bricks.toMutableList()
                val newBalls = mutableListOf<Ball>()

                for (ball in gameState.balls) {
                    var newPosition = ball.position + ball.velocity
                    var newVelocity = ball.velocity

                    // Wall collision (account for radius)
                    if (newPosition.x - ballRadius < 0f || newPosition.x + ballRadius > canvasSize.width) {
                        newVelocity = newVelocity.copy(x = -newVelocity.x)
                        newPosition = ball.position + newVelocity
                    }
                    if (newPosition.y - ballRadius < 0f) {
                        newVelocity = newVelocity.copy(y = -newVelocity.y)
                        newPosition = ball.position + newVelocity
                    }

                    // Brick collision (circle-rect)
                    for (i in newBricks.indices) {
                        val brick = newBricks[i]
                        val left = brick.col * brickW
                        val top = brick.row * brickH
                        val right = left + brickW
                        val bottom = top + brickH
                        if (BrickVolleyEngine.circleRectCollision(newPosition, ballRadius, left, top, right, bottom)) {
                            val newHp = brick.hp - 1
                            if (newHp <= 0) {
                                newBricks.removeAt(i)
                            } else {
                                newBricks[i] = brick.copy(hp = newHp)
                            }
                            newVelocity = newVelocity.copy(y = -newVelocity.y)
                            gameState = gameState.copy(score = gameState.score + 1)
                            feedback.play(ArcadeFeedbackEvent.SUCCESS)
                            break
                        }
                    }

                    // Return to bottom (ball catcher zone)
                    if (newPosition.y - ballRadius > canvasSize.height) {
                        // ball returned; do not add
                    } else {
                        newBalls.add(ball.copy(position = newPosition, velocity = newVelocity))
                    }
                }

                val timedOut = frameCount > 2400
                gameState = gameState.copy(
                    balls = if (timedOut) emptyList() else newBalls,
                    bricks = newBricks,
                )

                if (gameState.balls.isEmpty()) {
                    val advanced = newBricks.map { it.copy(row = it.row + 1) }.toMutableList()
                    val newRow = (0 until 6).mapNotNull { col ->
                        // deterministic-ish spawn: alternate presence by turn+col parity to avoid extreme randomness
                        if (((gameState.turn + col) % 2) == 0) {
                            Brick(
                                id = (gameState.turn + 1) * 100 + col,
                                row = 0,
                                col = col,
                                hp = (gameState.turn).coerceAtLeast(1),
                                color = Color.Yellow
                            )
                        } else null
                    }
                    advanced.addAll(newRow)

                    val dangerRowLimit = (canvasSize.height / brickH).toInt() - 2
                    if (advanced.any { it.row >= dangerRowLimit }) {
                        gameState = gameState.copy(status = GameStatus.GameOver)
                    } else {
                        gameState = gameState.copy(
                            status = GameStatus.Ready,
                            bricks = advanced,
                            turn = gameState.turn + 1,
                            aimingLine = null,
                        )
                    }
                }

                delay(16)
            }
        }
    }

    LaunchedEffect(gameState.status) {
        if (gameState.status == GameStatus.GameOver && !hasReportedRun) {
            hasReportedRun = true
            feedback.play(ArcadeFeedbackEvent.FAIL)
            onRunComplete(
                RunResult(
                    gameId = GameId.BRICK_VOLLEY,
                    score = gameState.score,
                    durationMillis = (System.currentTimeMillis() - runStartMillis).coerceAtLeast(0L),
                    bestCombo = 0,
                    coinsEarned = gameState.score / 2 + gameState.turn,
                ),
            )
        }
    }

    val accessibilityState = "BrickVolleyRoot;score=${gameState.score};turn=${gameState.turn};status=${gameState.status}"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(ArcadeTestTags.BrickVolleyScreen)
            .semantics {
                contentDescription = accessibilityState
                stateDescription = accessibilityState
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .testTag(ArcadeTestTags.BrickVolleyBoard)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { start ->
                            val isNearLauncher = start.y > canvasSize.height * 0.55f
                            if (gameState.status == GameStatus.Ready && isNearLauncher) {
                                gameState = gameState.copy(
                                    status = GameStatus.Aiming,
                                    aimingLine = AimingLine(launcher(), launcher())
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
                                val aiming = gameState.aimingLine
                                val canLaunch = aiming != null && BrickVolleyEngine.shouldLaunch(aiming.start, aiming.end)
                                gameState = gameState.copy(
                                    status = if (canLaunch) GameStatus.Animating else GameStatus.Ready,
                                    aimingLine = if (canLaunch) aiming else null,
                                )
                            }
                        }
                    )
                }
        ) {
            canvasSize = size
            val brickW = size.width / 6f
            val brickH = 50f * (size.width / 1080f)
            val ballRadius = 10f * (size.width / 1080f).coerceAtLeast(0.5f)
            drawRect(Color.Black)
            gameState.bricks.forEach { brick ->
                drawRect(
                    color = brick.color,
                    topLeft = Offset(brick.col * brickW, brick.row * brickH),
                    size = Size(brickW, brickH)
                )
            }
            gameState.balls.forEach { ball ->
                drawCircle(
                    color = Color.White,
                    radius = ballRadius,
                    center = ball.position
                )
            }
            val launcherCenter = launcher()
            drawCircle(color = Color.White, radius = ballRadius * 1.2f, center = launcherCenter)
            drawLine(
                color = Color.DarkGray,
                start = Offset(0f, launcherCenter.y + ballRadius * 2f),
                end = Offset(size.width, launcherCenter.y + ballRadius * 2f),
                strokeWidth = if (reducedEffects) 2f else 4f,
            )
            gameState.aimingLine?.let { line ->
                if (gameState.status == GameStatus.Aiming) {
                    drawLine(
                        color = Color.White,
                        start = line.start,
                        end = line.end,
                        strokeWidth = if (reducedEffects) 3f else 5f
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag(ArcadeTestTags.BrickVolleyAimArea)
                .semantics { contentDescription = ArcadeTestTags.BrickVolleyAimArea }
        )

        Text(
            "Score: ${gameState.score}",
            color = Color.White,
            modifier = Modifier.align(Alignment.TopStart).testTag(ArcadeTestTags.BrickVolleyScore),
            style = TextStyle(fontSize = 24.sp),
        )
        Text(
            "Round: ${gameState.turn}",
            color = Color.White,
            modifier = Modifier.align(Alignment.TopEnd).testTag(ArcadeTestTags.BrickVolleyRound),
            style = TextStyle(fontSize = 20.sp),
        )
        if (gameState.status == GameStatus.Ready) {
            Text(
                "Drag down from launcher to aim up",
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomCenter),
                style = TextStyle(fontSize = 16.sp),
            )
        }

        if (gameState.status == GameStatus.GameOver) {
            GameOverScreen(
                score = gameState.score,
                onRestart = { restart() }
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
                modifier = Modifier
                    .testTag(ArcadeTestTags.BrickVolleyRestart)
                    .semantics { contentDescription = ArcadeTestTags.BrickVolleyRestart },
            ) {
                Text("Restart")
            }
        }
    }
}
