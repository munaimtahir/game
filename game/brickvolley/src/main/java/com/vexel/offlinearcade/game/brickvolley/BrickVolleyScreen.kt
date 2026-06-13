package com.vexel.offlinearcade.game.brickvolley

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.graphics.lerp

@Composable
@Suppress("UNUSED_PARAMETER")
fun BrickVolleyScreen(
    stats: GameStats?,
    settings: SettingsState,
    feedback: ArcadeFeedback,
    onRunComplete: (RunResult) -> Unit,
    onBack: () -> Unit,
) {
    var state by remember { mutableStateOf(BrickVolleyState()) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    var hasReportedRun by remember { mutableStateOf(false) }
    val textMeasurer = rememberTextMeasurer()
    val colors = ArcadeTheme.colors
    val brickClearPulse = remember { Animatable(0f) }

    fun restart() {
        val initialBricks = mutableListOf<Brick>()
        for (col in 0 until BrickVolleyTuning.columns) {
            if (Math.random() < 0.7) {
                initialBricks.add(Brick(col, 1, col, 1, 1))
            }
        }
        state = BrickVolleyState(
            bricks = initialBricks,
            status = GameStatus.Ready,
            runStartMillis = System.currentTimeMillis()
        )
        hasReportedRun = false
        feedback.play(ArcadeFeedbackEvent.TAP)
    }

    LaunchedEffect(state.status) {
        if (state.status == GameStatus.Animating) {
            val angle = atan2(
                state.aimingLine!!.end.y - state.aimingLine!!.start.y,
                state.aimingLine!!.end.x - state.aimingLine!!.start.x
            )
            val velocity = Offset(cos(angle) * BrickVolleyTuning.ballSpeed, sin(angle) * BrickVolleyTuning.ballSpeed)
            
            // Multiple balls effect (spawn them with a slight delay)
            val balls = mutableListOf<Ball>()
            for (i in 0 until state.ballCount) {
                balls.add(Ball(state.aimingLine!!.start, velocity))
                state = state.copy(balls = balls.toList())
                delay(100)
            }

            while (state.status == GameStatus.Animating) {
                val currentBricks = state.bricks.toMutableList()
                val currentBalls = state.balls.map { ball ->
                    if (!ball.active) return@map ball
                    var newPos = ball.position + ball.velocity
                    var newVel = ball.velocity

                    // Wall bounce
                    if (newPos.x < 0 || newPos.x > canvasSize.width) {
                        newVel = newVel.copy(x = -newVel.x)
                        feedback.play(ArcadeFeedbackEvent.TAP)
                    }
                    if (newPos.y < 0) {
                        newVel = newVel.copy(y = -newVel.y)
                        feedback.play(ArcadeFeedbackEvent.TAP)
                    }

                    // Brick collision
                    val cellW = canvasSize.width / BrickVolleyTuning.columns
                    val cellH = canvasSize.height / BrickVolleyTuning.rows
                    
                    for (i in currentBricks.indices) {
                        val b = currentBricks[i]
                        val bRect = Rect(b.col * cellW, b.row * cellH, (b.col + 1) * cellW, (b.row + 1) * cellH)
                        if (bRect.contains(newPos)) {
                            val updatedBrick = b.copy(hp = b.hp - 1)
                            if (updatedBrick.hp <= 0) {
                                currentBricks.removeAt(i)
                                feedback.play(ArcadeFeedbackEvent.SUCCESS)
                                brickClearPulse.snapTo(1f)
                                brickClearPulse.animateTo(0f, tween(200))
                            } else {
                                currentBricks[i] = updatedBrick
                                feedback.play(ArcadeFeedbackEvent.TAP)
                            }
                            // Simple bounce logic
                            newVel = if (abs(ball.position.x - bRect.left) < 5f || abs(ball.position.x - bRect.right) < 5f) {
                                newVel.copy(x = -newVel.x)
                            } else {
                                newVel.copy(y = -newVel.y)
                            }
                            state = state.copy(score = state.score + 10)
                            break
                        }
                    }

                    if (newPos.y > canvasSize.height) {
                        ball.copy(active = false)
                    } else {
                        ball.copy(position = newPos, velocity = newVel)
                    }
                }

                state = state.copy(balls = currentBalls, bricks = currentBricks)

                if (currentBalls.none { it.active }) {
                    // Turn over
                    val nextBricks = currentBricks.map { it.copy(row = it.row + 1) }.toMutableList()
                    val nextTurn = state.turn + 1
                    
                    // Spawn new row
                    for (col in 0 until BrickVolleyTuning.columns) {
                        if (Math.random() < BrickVolleyTuning.bricksPerTurnProb) {
                            nextBricks.add(Brick(nextTurn * 10 + col, 0, col, nextTurn, nextTurn))
                        }
                    }

                    if (nextBricks.any { it.row >= BrickVolleyTuning.rows - 1 }) {
                        state = state.copy(status = GameStatus.GameOver, bricks = nextBricks)
                        feedback.play(ArcadeFeedbackEvent.FAIL)
                    } else {
                        state = state.copy(
                            status = GameStatus.Ready,
                            bricks = nextBricks,
                            turn = nextTurn,
                            ballCount = BrickVolleyTuning.initialBalls + nextTurn / 2,
                            balls = emptyList()
                        )
                    }
                }
                delay(16)
            }
        }
    }

    if (state.status == GameStatus.GameOver && !hasReportedRun) {
        hasReportedRun = true
        onRunComplete(
            RunResult(
                gameId = GameId.PULSE_ORBIT,
                score = state.score,
                durationMillis = System.currentTimeMillis() - state.runStartMillis,
                coinsEarned = state.score / 10
            )
        )
    }

    val overlayContent: (@Composable () -> Unit)? = when (state.status) {
        GameStatus.GameOver -> {
            {
                PremiumOverlayCard(title = "Game Over", subtitle = "The bricks reached the bottom.") {
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
                        androidx.compose.material3.Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = colors.textPrimary)
                    }
                    HudPill("Score", state.score.toString(), modifier = Modifier.testTag(ArcadeTestTags.BrickVolleyScore))
                    HudPill("Balls", state.ballCount.toString())
                    HudPill("Round", state.turn.toString(), modifier = Modifier.testTag(ArcadeTestTags.BrickVolleyRound))
                }
            }
        },
        overlay = overlayContent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag(ArcadeTestTags.BrickVolleyRoot)
                .background(colors.gameBoard, RoundedCornerShape(24.dp))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { start ->
                            if (state.status == GameStatus.Ready) {
                                state = state.copy(status = GameStatus.Aiming, aimingLine = AimingLine(start, start))
                            }
                        },
                        onDrag = { change, _ ->
                            if (state.status == GameStatus.Aiming) {
                                change.consume()
                                state = state.copy(aimingLine = state.aimingLine?.copy(end = change.position))
                            }
                        },
                        onDragEnd = {
                            if (state.status == GameStatus.Aiming) {
                                state = state.copy(status = GameStatus.Animating)
                            }
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(8.dp).testTag(ArcadeTestTags.BrickVolleyAimArea)) {
                canvasSize = size
                val cellW = size.width / BrickVolleyTuning.columns
                val cellH = size.height / BrickVolleyTuning.rows

                // Bricks
                state.bricks.forEach { b ->
                    val color = lerp(colors.gameBoardRaised, colors.dangerCoral, b.hp.toFloat() / b.maxHp.toFloat())
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(b.col * cellW + 2f, b.row * cellH + 2f),
                        size = Size(cellW - 4f, cellH - 4f),
                        cornerRadius = CornerRadius(8.dp.toPx())
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = b.hp.toString(),
                        topLeft = Offset(b.col * cellW + cellW / 2 - 10f, b.row * cellH + cellH / 2 - 20f),
                        style = TextStyle(color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                }

                // Balls
                state.balls.forEach { ball ->
                    if (ball.active) {
                        drawCircle(color = colors.primaryCyan, radius = 8.dp.toPx(), center = ball.position)
                    }
                }

                // Aiming Line
                if (state.status == GameStatus.Aiming && state.aimingLine != null) {
                    drawLine(
                        color = colors.primaryCyan.copy(alpha = 0.5f),
                        start = state.aimingLine!!.start,
                        end = state.aimingLine!!.end,
                        strokeWidth = 4.dp.toPx(),
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                }
            }

            if (state.status == GameStatus.Ready) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Swipe down to aim", color = colors.textSecondary.copy(alpha = 0.6f))
                }
            }
        }
    }
}
