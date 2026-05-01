package com.vexel.offlinearcade.game.stackdrop

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
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
import com.vexel.offlinearcade.core.ui.ArcadeCard
import com.vexel.offlinearcade.core.ui.ArcadeGestureAction
import com.vexel.offlinearcade.core.ui.ArcadeGestureThresholdsPx
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.HudPill
import com.vexel.offlinearcade.core.ui.PremiumBadge
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.PremiumOverlayCard
import com.vexel.offlinearcade.core.ui.SectionHeader
import com.vexel.offlinearcade.core.ui.StatRow
import com.vexel.offlinearcade.core.ui.arcadeGestureInput
import com.vexel.offlinearcade.core.ui.rememberArcadeGestureThresholdsPx
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
    var showGestureHint by rememberSaveable { mutableStateOf(true) }
    var paused by rememberSaveable { mutableStateOf(false) }
    val gestureThresholds = rememberArcadeGestureThresholdsPx()

    fun restart() {
        state = engine.newState()
        lastTickMillis = System.currentTimeMillis()
        hasReportedRun = false
        paused = false
        feedback.play(ArcadeFeedbackEvent.TAP)
    }

    // Lifecycle Pause
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                if (state.playing && !paused && !state.gameOver) {
                    paused = true
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // BackHandler logic
    BackHandler {
        if (state.playing && !paused) {
            paused = true
        } else {
            onBack()
        }
    }

    LaunchedEffect(state.playing, state.dropIntervalMillis, paused) {
        while (state.playing && !paused) {
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

    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val spacing = ArcadeTheme.spacing

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArcadeTheme.colors.shellGradient)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(spacing.md)
            .testTag(ArcadeTestTags.StackDropScreen)
    ) {
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

        Row(modifier = Modifier.fillMaxWidth().padding(top = spacing.sm), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            PremiumButton(
                label = "Back",
                onClick = onBack,
                style = ArcadeButtonStyle.Secondary,
                modifier = Modifier.testTag(ArcadeTestTags.BackButton)
            )
            PremiumButton(
                label = if (paused) "Resume" else "Pause",
                onClick = { if (!state.gameOver && (state.playing || paused)) paused = !paused },
                style = ArcadeButtonStyle.Secondary,
                enabled = state.playing || paused,
            )
        }

        BoxWithConstraints(modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 12.dp)) {
            val compactLayout = maxWidth < 520.dp
            val compactBoardHeight = if (maxWidth < 360.dp) 300.dp else 340.dp
            if (compactLayout) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    StackDropBoardCard(
                        state = state,
                        paused = paused,
                        surfaceVariant = surfaceVariant,
                        boardHeight = compactBoardHeight,
                        compact = true,
                        modifier = Modifier.fillMaxWidth(),
                        gestureThresholds = gestureThresholds,
                        onAction = actionHandler(state, paused, engine, feedback) { state = it },
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        HudPill("Level", state.level.toString(), modifier = Modifier.weight(1f))
                        HudPill("Next", state.nextPiece.name, modifier = Modifier.weight(1f))
                    }
                    StackDropStartCard(
                        state = state,
                        paused = paused,
                        stats = stats,
                        settings = settings,
                        showGestureHint = showGestureHint,
                        onDismissHint = { showGestureHint = false },
                        onStartOrRetry = ::restart,
                    )
                    if (paused) StackDropPauseCard(state = state, onResume = { paused = false }, onRestart = ::restart, onQuit = onBack)
                    if (state.gameOver) StackDropSummary(state = state, stats = stats, onRetry = ::restart, onBack = onBack)
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxSize()) {
                    StackDropBoardCard(
                        state = state,
                        paused = paused,
                        surfaceVariant = surfaceVariant,
                        boardHeight = 420.dp,
                        compact = false,
                        modifier = Modifier.weight(0.58f),
                        gestureThresholds = gestureThresholds,
                        onAction = actionHandler(state, paused, engine, feedback) { state = it },
                    )
                    Column(modifier = Modifier.weight(0.42f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            HudPill("Level", state.level.toString(), modifier = Modifier.weight(1f))
                            HudPill("Next", state.nextPiece.name, modifier = Modifier.weight(1f))
                        }
                        StackDropStartCard(
                            state = state,
                            paused = paused,
                            stats = stats,
                            settings = settings,
                            showGestureHint = showGestureHint,
                            onDismissHint = { showGestureHint = false },
                            onStartOrRetry = ::restart,
                        )
                        if (paused) StackDropPauseCard(state = state, onResume = { paused = false }, onRestart = ::restart, onQuit = onBack)
                        if (state.gameOver) StackDropSummary(state = state, stats = stats, onRetry = ::restart, onBack = onBack)
                    }
                }
            }
        }
    }
}

private fun actionHandler(
    state: StackDropState,
    paused: Boolean,
    engine: StackDropEngine,
    feedback: ArcadeFeedback,
    update: (StackDropState) -> Unit,
): (ArcadeGestureAction) -> Unit = { action ->
    when (action) {
        ArcadeGestureAction.Tap -> {
            if (!state.playing && !paused && !state.gameOver) {
                update(engine.newState())
            } else if (state.playing && !paused) {
                feedback.play(ArcadeFeedbackEvent.TAP)
                update(engine.rotate(state))
            }
        }
        ArcadeGestureAction.SwipeLeft -> if (state.playing && !paused) {
            feedback.play(ArcadeFeedbackEvent.TAP)
            update(engine.move(state, -1))
        }
        ArcadeGestureAction.SwipeRight -> if (state.playing && !paused) {
            feedback.play(ArcadeFeedbackEvent.TAP)
            update(engine.move(state, 1))
        }
        ArcadeGestureAction.SwipeDown -> if (state.playing && !paused) {
            feedback.play(ArcadeFeedbackEvent.TAP)
            update(engine.softDrop(state))
        }
        ArcadeGestureAction.FlickDown -> if (state.playing && !paused) {
            feedback.play(ArcadeFeedbackEvent.TAP)
            update(engine.hardDrop(state))
        }
    }
}

@Composable
private fun StackDropBoardCard(
    state: StackDropState,
    paused: Boolean,
    surfaceVariant: Color,
    boardHeight: Dp,
    compact: Boolean,
    gestureThresholds: ArcadeGestureThresholdsPx,
    onAction: (ArcadeGestureAction) -> Unit,
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
    Box(modifier = modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp)).padding(if (compact) 8.dp else 12.dp)) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(boardHeight)
                .testTag(ArcadeTestTags.StackDropBoard)
                .semantics {
                    stateDescription = "x=${state.activePiece.x};y=${state.activePiece.y};rotation=${state.activePiece.rotationIndex};playing=${state.playing}"
                }
                .arcadeGestureInput(thresholds = gestureThresholds, enabled = true, onAction = onAction)
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
                    drawRect(color = color, topLeft = Offset(x * cellWidth + 2f, y * cellHeight + 2f), size = Size(cellWidth - 4f, cellHeight - 4f))
                }
            }
        }
        if (!paused && state.playing) {
            PremiumBadge(
                text = if (state.recentLineClearCount > 0) "Clean clear" else "Stack focus",
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 10.dp),
                color = ArcadeTheme.colors.stackAccent,
            )
        }
    }
}

@Composable
private fun StackDropStartCard(
    state: StackDropState,
    paused: Boolean,
    stats: GameStats?,
    settings: SettingsState,
    showGestureHint: Boolean,
    onDismissHint: () -> Unit,
    onStartOrRetry: () -> Unit,
) {
    if (!state.playing && !paused && !state.gameOver) {
        ArcadeCard {
            SectionHeader(title = "Stack Drop", subtitle = "Cobalt and amber mastery with crisp board visibility.")
            StatRow("Best score", (stats?.highScore ?: 0).toString())
            Text(
                if (settings.reducedEffects) "Low-effects mode is active for flatter rendering." else "Full shell mode keeps premium depth while staying readable.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (showGestureHint) {
                Column(
                    modifier = Modifier.testTag(ArcadeTestTags.StackDropHint),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("Tap to rotate • Swipe left/right to move • Swipe down to drop", fontWeight = FontWeight.SemiBold)
                    Text("Use a fast downward flick for a hard drop.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = onDismissHint) { Text("Got it") }
                    }
                }
            }
            PremiumButton(
                label = "Start",
                onClick = onStartOrRetry,
                modifier = Modifier.fillMaxWidth().height(56.dp).testTag(ArcadeTestTags.StackDropStartButton),
            )
        }
    }
}

@Composable
private fun StackDropPauseCard(
    state: StackDropState,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onQuit: () -> Unit,
) {
    PremiumOverlayCard(title = "Run paused", subtitle = "Resume, restart, or leave the board.") {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            StatRow("Score", state.score.toString())
            StatRow("Lines", state.linesCleared.toString())
            PremiumButton(label = "Resume", onClick = onResume, modifier = Modifier.fillMaxWidth())
            PremiumButton(label = "Restart", onClick = onRestart, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
            PremiumButton(label = "Quit", onClick = onQuit, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
        }
    }
}

@Composable
private fun StackDropSummary(
    state: StackDropState,
    stats: GameStats?,
    onRetry: () -> Unit,
    onBack: () -> Unit,
) {
    ArcadeCard {
        PremiumBadge(
            text = if (state.score > (stats?.highScore ?: 0)) "New best board" else "Run complete",
            color = if (state.score > (stats?.highScore ?: 0)) ArcadeTheme.colors.reward else ArcadeTheme.colors.success,
        )
        Text("Run summary", fontWeight = FontWeight.Black, style = MaterialTheme.typography.headlineMedium)
        StatRow("Score", state.score.toString(), valueColor = ArcadeTheme.colors.reward)
        StatRow("Lines", state.linesCleared.toString(), valueColor = ArcadeTheme.colors.success)
        StatRow("Coins earned", (state.linesCleared * 4 + state.score / 40).toString(), valueColor = ArcadeTheme.colors.reward)
        PremiumButton(label = "Retry", onClick = onRetry, modifier = Modifier.fillMaxWidth().height(52.dp))
        PremiumButton(label = "Back to arcade", onClick = onBack, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
    }
}
