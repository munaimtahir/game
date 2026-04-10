package com.vexel.offlinearcade.game.stackdrop

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vexel.offlinearcade.core.model.ArcadeFeedback
import com.vexel.offlinearcade.core.model.ArcadeFeedbackEvent
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.core.model.SettingsState
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeScaffold
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.HudPill
import kotlinx.coroutines.flow.collectLatest

@Composable
fun StackDropScreen(
    stats: GameStats?,
    settings: SettingsState,
    feedback: ArcadeFeedback,
    onRunComplete: (RunResult) -> Unit,
    onBack: () -> Unit,
) {
    val engine = remember { StackDropEngine() }
    var state by remember { mutableStateOf(engine.newState().copy(playing = false)) }
    var lastTickMillis by remember { mutableLongStateOf(0L) }
    var hasReportedRun by remember { mutableStateOf(false) }

    fun restart() {
        state = engine.newState()
        lastTickMillis = System.currentTimeMillis()
        hasReportedRun = false
        feedback.play(ArcadeFeedbackEvent.TAP)
    }

    LaunchedEffect(state.playing, state.dropIntervalMillis) {
        while (state.playing) {
            kotlinx.coroutines.delay(16L)
            val now = System.currentTimeMillis()
            if (lastTickMillis == 0L) {
                lastTickMillis = now
            }
            if (now - lastTickMillis >= state.dropIntervalMillis) {
                state = engine.tick(state)
                lastTickMillis = now
            }
        }
    }

    LaunchedEffect(Unit) {
        var previousLines = 0
        var previousGameOver = false
        snapshotFlow { state.recentLineClearCount to state.gameOver }.collectLatest { (recentLineClearCount, gameOver) ->
            if (recentLineClearCount > 0 && recentLineClearCount != previousLines) {
                feedback.play(ArcadeFeedbackEvent.LINE_CLEAR)
            }
            if (gameOver && !previousGameOver) {
                feedback.play(ArcadeFeedbackEvent.FAIL)
            }
            previousLines = recentLineClearCount
            previousGameOver = gameOver
        }
    }

    if (state.gameOver && !hasReportedRun) {
        hasReportedRun = true
        onRunComplete(
            RunResult(
                gameId = GameId.STACK_DROP,
                score = state.score,
                durationMillis = System.currentTimeMillis() - state.runStartMillis,
                linesCleared = state.linesCleared,
                coinsEarned = state.linesCleared * 4 + state.score / 40,
            ),
        )
    }

    ArcadeScaffold(
        title = "Stack Drop",
        onBack = onBack,
        scrollable = false,
        screenTestTag = ArcadeTestTags.StackDropScreen,
    ) {
        val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val compactHud = maxWidth < 360.dp
            if (compactHud) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HudPill("Score", state.score.toString(), modifier = Modifier.weight(1f))
                        HudPill("Lines", state.linesCleared.toString(), modifier = Modifier.weight(1f))
                    }
                    HudPill("Best", (stats?.highScore ?: 0).toString(), modifier = Modifier.fillMaxWidth())
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    HudPill("Score", state.score.toString())
                    HudPill("Lines", state.linesCleared.toString())
                    HudPill("Best", (stats?.highScore ?: 0).toString())
                }
            }
        }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            val compactLayout = maxWidth < 420.dp || maxHeight < 700.dp
            val compactBoardHeight = (maxHeight * 0.5f).coerceIn(320.dp, 390.dp)
            if (compactLayout) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    StackDropBoardCard(
                        state = state,
                        surfaceVariant = surfaceVariant,
                        boardHeight = compactBoardHeight,
                        compact = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    StackDropInfoCard(state = state, settings = settings)
                    StackDropControls(
                        state = state,
                        compact = true,
                        onStartOrRetry = ::restart,
                        onMoveLeft = {
                            feedback.play(ArcadeFeedbackEvent.TAP)
                            state = engine.move(state, -1)
                        },
                        onMoveRight = {
                            feedback.play(ArcadeFeedbackEvent.TAP)
                            state = engine.move(state, 1)
                        },
                        onRotate = {
                            feedback.play(ArcadeFeedbackEvent.TAP)
                            state = engine.rotate(state)
                        },
                        onSoftDrop = {
                            feedback.play(ArcadeFeedbackEvent.TAP)
                            state = engine.softDrop(state)
                        },
                    )
                    if (state.gameOver) {
                        StackDropSummary(state = state, onRetry = ::restart)
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth()) {
                    StackDropBoardCard(
                        state = state,
                        surfaceVariant = surfaceVariant,
                        boardHeight = 420.dp,
                        compact = false,
                        modifier = Modifier.weight(0.58f),
                    )
                    Column(modifier = Modifier.weight(0.42f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        StackDropInfoCard(state = state, settings = settings)
                        StackDropControls(
                            state = state,
                            compact = false,
                            onStartOrRetry = ::restart,
                            onMoveLeft = {
                                feedback.play(ArcadeFeedbackEvent.TAP)
                                state = engine.move(state, -1)
                            },
                            onMoveRight = {
                                feedback.play(ArcadeFeedbackEvent.TAP)
                                state = engine.move(state, 1)
                            },
                            onRotate = {
                                feedback.play(ArcadeFeedbackEvent.TAP)
                                state = engine.rotate(state)
                            },
                            onSoftDrop = {
                                feedback.play(ArcadeFeedbackEvent.TAP)
                                state = engine.softDrop(state)
                            },
                        )
                        if (state.gameOver) {
                            StackDropSummary(state = state, onRetry = ::restart)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StackDropBoardCard(
    state: StackDropState,
    surfaceVariant: Color,
    boardHeight: Dp,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val activeCells = remember(state.activePiece) {
        IntArray(STACK_DROP_WIDTH * STACK_DROP_HEIGHT).also { grid ->
            state.activePiece.cells.forEach { cell ->
                val x = state.activePiece.x + cell.x
                val y = state.activePiece.y + cell.y
                if (x in 0 until STACK_DROP_WIDTH && y in 0 until STACK_DROP_HEIGHT) {
                    grid[y * STACK_DROP_WIDTH + x] = state.activePiece.type.color
                }
            }
        }
    }
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp))
            .padding(if (compact) 8.dp else 12.dp),
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(boardHeight)
                .align(Alignment.Center),
        ) {
            val cellWidth = size.width / STACK_DROP_WIDTH
            val cellHeight = size.height / STACK_DROP_HEIGHT
            for (y in 0 until STACK_DROP_HEIGHT) {
                for (x in 0 until STACK_DROP_WIDTH) {
                    val baseColor = state.board.get(x, y)
                    val activeColor = activeCells[y * STACK_DROP_WIDTH + x]
                    val color = when {
                        activeColor != 0 -> Color(activeColor)
                        baseColor != 0 -> Color(baseColor)
                        else -> surfaceVariant
                    }
                    drawRect(
                        color = color,
                        topLeft = Offset(x * cellWidth + 2f, y * cellHeight + 2f),
                        size = Size(cellWidth - 4f, cellHeight - 4f),
                    )
                }
            }
        }
    }
}

@Composable
private fun StackDropInfoCard(
    state: StackDropState,
    settings: SettingsState,
) {
    ArcadeCard {
        Text("Level ${state.level}", fontWeight = FontWeight.Bold)
        Text("Next piece: ${state.nextPiece.name}")
        Text(if (settings.musicEnabled) "Music toggle is saved, but background music is intentionally not shipped yet." else "Music is off.")
    }
}

@Composable
private fun StackDropControls(
    state: StackDropState,
    compact: Boolean,
    onStartOrRetry: () -> Unit,
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onRotate: () -> Unit,
    onSoftDrop: () -> Unit,
) {
    val buttonHeight = if (compact) 60.dp else 56.dp
    val spacing = if (compact) 12.dp else 10.dp
    ArcadeCard {
        if (!state.playing) {
            Button(onClick = onStartOrRetry, modifier = Modifier.fillMaxWidth().height(buttonHeight)) {
                Text(if (state.gameOver) "Retry" else "Start")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(spacing), modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onMoveLeft, modifier = Modifier.weight(1f).height(buttonHeight), enabled = state.playing) {
                Text("Left")
            }
            Button(onClick = onMoveRight, modifier = Modifier.weight(1f).height(buttonHeight), enabled = state.playing) {
                Text("Right")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(spacing), modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onRotate, modifier = Modifier.weight(1f).height(buttonHeight), enabled = state.playing) {
                Text("Rotate")
            }
            Button(onClick = onSoftDrop, modifier = Modifier.weight(1f).height(buttonHeight), enabled = state.playing) {
                Text("Drop")
            }
        }
    }
}

@Composable
private fun StackDropSummary(
    state: StackDropState,
    onRetry: () -> Unit,
) {
    ArcadeCard {
        Text("Run summary: ${state.score} score, ${state.linesCleared} lines.", fontWeight = FontWeight.SemiBold)
        Button(onClick = onRetry, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text("Retry") }
    }
}
