package com.vexel.offlinearcade.game.stackdrop

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
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
import com.vexel.offlinearcade.core.ui.ArcadeGestureAction
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import com.vexel.offlinearcade.core.ui.ArcadeTheme
import com.vexel.offlinearcade.core.ui.GameplayScaffold
import com.vexel.offlinearcade.core.ui.HudPill
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.PremiumOverlayCard
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
    var paused by remember { mutableStateOf(false) }
    val gestureThresholds = rememberArcadeGestureThresholdsPx()
    val lineClearFlash = remember { androidx.compose.animation.core.Animatable(0f) }

    fun restart() {
        state = engine.newState()
        lastTickMillis = System.currentTimeMillis()
        hasReportedRun = false
        paused = false
        feedback.play(ArcadeFeedbackEvent.TAP)
    }

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

    LaunchedEffect(state.recentLineClearCount) {
        if (state.recentLineClearCount > 0) {
            lineClearFlash.snapTo(1f)
            lineClearFlash.animateTo(0f, animationSpec = androidx.compose.animation.core.tween(400, easing = androidx.compose.animation.core.LinearOutSlowInEasing))
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

    val colors = ArcadeTheme.colors
    val spacing = ArcadeTheme.spacing
    val reducedEffects = ArcadeTheme.reducedEffects

    val overlayContent: (@Composable () -> Unit)? = when {
        paused -> {
            {
                PremiumOverlayCard(title = "Run paused", subtitle = "Resume, restart, or leave the board.") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Score", state.score.toString())
                        StatRow("Lines", state.linesCleared.toString())
                        PremiumButton(label = "Resume", onClick = { paused = false }, modifier = Modifier.fillMaxWidth())
                        PremiumButton(label = "Restart", onClick = ::restart, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
                        PremiumButton(label = "Quit", onClick = onBack, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
                    }
                }
            }
        }
        state.gameOver -> {
            {
                PremiumOverlayCard(
                    title = if (state.score > (stats?.highScore ?: 0)) "New best board" else "Run complete",
                    subtitle = "One more clean clear is only a tap away.",
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Score", state.score.toString(), valueColor = colors.reward)
                        StatRow("Lines", state.linesCleared.toString(), valueColor = colors.success)
                        StatRow("Coins earned", (state.linesCleared * 4 + state.score / 40).toString(), valueColor = colors.reward)
                        PremiumButton(
                            label = "Retry instantly",
                            onClick = ::restart,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(ArcadeTestTags.StackDropStartButton),
                        )
                        PremiumButton(label = "Back to detail", onClick = onBack, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
                    }
                }
            }
        }
        else -> null
    }

    val handleAction: (ArcadeGestureAction) -> Unit = { action ->
        when (action) {
            ArcadeGestureAction.Tap -> {
                if (!state.playing && !paused && !state.gameOver) {
                    restart()
                } else if (state.playing && !paused) {
                    feedback.play(ArcadeFeedbackEvent.TAP)
                    state = engine.rotate(state)
                }
            }
            ArcadeGestureAction.SwipeLeft -> if (state.playing && !paused) {
                feedback.play(ArcadeFeedbackEvent.TAP)
                state = engine.move(state, -1)
            }
            ArcadeGestureAction.SwipeRight -> if (state.playing && !paused) {
                feedback.play(ArcadeFeedbackEvent.TAP)
                state = engine.move(state, 1)
            }
            ArcadeGestureAction.SwipeDown -> if (state.playing && !paused) {
                feedback.play(ArcadeFeedbackEvent.TAP)
                state = engine.softDrop(state)
            }
            ArcadeGestureAction.FlickDown -> if (state.playing && !paused) {
                feedback.play(ArcadeFeedbackEvent.TAP)
                state = engine.hardDrop(state)
            }
        }
    }

    GameplayScaffold(
        modifier = Modifier.testTag(ArcadeTestTags.StackDropScreen),
        topBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    androidx.compose.material3.IconButton(
                        onClick = {
                            if (state.playing && !paused) {
                                paused = true
                            } else {
                                onBack()
                            }
                        },
                        modifier = Modifier.testTag(ArcadeTestTags.BackButton)
                    ) {
                        androidx.compose.material3.Icon(
                            androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.textPrimary
                        )
                    }
                    HudPill("Score", state.score.toString())
                    HudPill("Lines", state.linesCleared.toString())
                }
                PremiumButton(
                    label = if (paused) "Resume" else "Pause",
                    onClick = { if (!state.gameOver && (state.playing || paused)) paused = !paused },
                    style = ArcadeButtonStyle.Secondary,
                    enabled = state.playing || paused,
                )
            }
        },
        controls = {
            if (state.playing) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = spacing.sm),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PremiumButton(label = "◀", onClick = { handleAction(ArcadeGestureAction.SwipeLeft) }, style = ArcadeButtonStyle.Secondary, modifier = Modifier.weight(1f).padding(end=4.dp))
                    PremiumButton(label = "⟳", onClick = { handleAction(ArcadeGestureAction.Tap) }, style = ArcadeButtonStyle.Secondary, modifier = Modifier.weight(1f).padding(horizontal=4.dp), borderOverride = colors.accentViolet)
                    PremiumButton(label = "▼", onClick = { handleAction(ArcadeGestureAction.SwipeDown) }, style = ArcadeButtonStyle.Secondary, modifier = Modifier.weight(1f).padding(horizontal=4.dp), borderOverride = colors.primaryCyan)
                    PremiumButton(label = "▶", onClick = { handleAction(ArcadeGestureAction.SwipeRight) }, style = ArcadeButtonStyle.Secondary, modifier = Modifier.weight(1f).padding(start=4.dp))
                }
            }
        },
        overlay = overlayContent,
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
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .background(colors.gameBoard, RoundedCornerShape(28.dp))
                .padding(12.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(ArcadeTestTags.StackDropBoard)
                    .semantics {
                        stateDescription = "x=${state.activePiece.x};y=${state.activePiece.y};rotation=${state.activePiece.rotationIndex};playing=${state.playing}"
                    }
                    .arcadeGestureInput(thresholds = gestureThresholds, enabled = true, onAction = handleAction)
                    .align(Alignment.Center),
            ) {
                val cellWidth = size.width / STACK_DROP_WIDTH
                val cellHeight = size.height / STACK_DROP_HEIGHT
                
                // Danger Glow
                val inDanger = (0 until STACK_DROP_WIDTH).any { x -> (0..3).any { y -> state.board.get(x, y) != 0 } }
                if (inDanger && state.playing && !reducedEffects) {
                    val dangerAlpha = 0.15f + 0.15f * kotlin.math.sin(System.currentTimeMillis() / 200.0).toFloat()
                    drawRect(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(colors.dangerCoral.copy(alpha = dangerAlpha), Color.Transparent),
                            startY = 0f,
                            endY = size.height * 0.3f
                        )
                    )
                }
                
                for (y in 0 until STACK_DROP_HEIGHT) {
                    for (x in 0 until STACK_DROP_WIDTH) {
                        val baseColor = state.board.get(x, y)
                        val activeColor = activeCells[y * STACK_DROP_WIDTH + x]
                        val fillColor = when {
                            activeColor != 0 -> Color(activeColor)
                            baseColor != 0 -> Color(baseColor)
                            else -> colors.gameBoardInner
                        }
                        val topLeft = Offset(x * cellWidth + 2f, y * cellHeight + 2f)
                        val sizeRect = Size(cellWidth - 4f, cellHeight - 4f)
                        drawRect(color = colors.gridLine, topLeft = topLeft, size = sizeRect)
                        drawRect(
                            color = fillColor,
                            topLeft = topLeft + Offset(1.5f, 1.5f),
                            size = Size(sizeRect.width - 3f, sizeRect.height - 3f),
                        )
                    }
                }
                
                // Line Clear Flash
                if (lineClearFlash.value > 0f) {
                    drawRect(
                        color = Color.White.copy(alpha = lineClearFlash.value * 0.6f),
                        topLeft = Offset(0f, 0f),
                        size = Size(size.width, size.height)
                    )
                }
            }
            if (!state.playing && !paused && !state.gameOver) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PremiumButton(
                        label = "Tap to start",
                        onClick = ::restart,
                        modifier = Modifier.testTag(ArcadeTestTags.StackDropStartButton),
                    )
                }
            }
        }
    }
}
