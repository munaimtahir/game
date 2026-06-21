package com.vexel.offlinearcade.game.stackdrop

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.vexel.offlinearcade.core.ui.CompletionPopup
import com.vexel.offlinearcade.core.ui.GameTutorialContent
import com.vexel.offlinearcade.core.ui.GameplayScaffold
import com.vexel.offlinearcade.core.ui.HudPill
import com.vexel.offlinearcade.core.ui.HowToPlayOverlay
import com.vexel.offlinearcade.core.ui.PremiumButton
import com.vexel.offlinearcade.core.ui.PremiumOverlayCard
import com.vexel.offlinearcade.core.ui.StatRow
import com.vexel.offlinearcade.core.ui.arcadeGestureInput
import com.vexel.offlinearcade.core.ui.rememberArcadeGestureThresholdsPx
import kotlinx.coroutines.flow.collectLatest

@Composable
@Suppress("UNUSED_PARAMETER")
fun StackDropScreen(
    stats: GameStats?,
    settings: SettingsState,
    feedback: ArcadeFeedback,
    tutorialSeen: Boolean,
    onTutorialSeen: () -> Unit,
    onRunComplete: (RunResult) -> Unit,
    onBack: () -> Unit,
) {
    val engine = remember { StackDropEngine() }
    var state by remember { mutableStateOf(engine.newState().copy(playing = false)) }
    var lastTickMillis by remember { mutableLongStateOf(0L) }
    var hasReportedRun by remember { mutableStateOf(false) }
    var paused by remember { mutableStateOf(false) }
    var showTutorial by remember(tutorialSeen) { mutableStateOf(!tutorialSeen) }
    var showCompletionSummary by remember { mutableStateOf(false) }
    val gestureThresholds = rememberArcadeGestureThresholdsPx()
    val lineClearFlash = remember { androidx.compose.animation.core.Animatable(0f) }

    fun restart() {
        state = engine.newState()
        lastTickMillis = System.currentTimeMillis()
        hasReportedRun = false
        showCompletionSummary = false
        paused = false
        feedback.play(ArcadeFeedbackEvent.TAP)
    }

    fun showHowToPlay() {
        if (state.playing && !paused && !state.gameOver) {
            paused = true
        }
        showTutorial = true
    }

    fun closeTutorial(startAfterClose: Boolean) {
        onTutorialSeen()
        showTutorial = false
        if (startAfterClose) restart()
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

    val reducedEffects = ArcadeTheme.reducedEffects

    LaunchedEffect(state.playing, state.dropIntervalMillis, paused) {
        while (state.playing && !paused) {
            val delayMillis = if (reducedEffects) 120L else 16L
            kotlinx.coroutines.delay(delayMillis)
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
        showCompletionSummary = true
    }

    val colors = ArcadeTheme.colors
    val spacing = ArcadeTheme.spacing

    val overlayContent: (@Composable () -> Unit)? = when {
        showTutorial -> {
            {
                HowToPlayOverlay(
                    content = GameTutorialContent(
                        gameId = GameId.STACK_DROP,
                        title = "How to Play Stack Drop",
                        lines = listOf(
                            "Move and rotate falling blocks.",
                            "Complete horizontal lines to clear them.",
                            "The run ends when the stack reaches the top.",
                        ),
                        controls = "Left, right, rotate, and drop.",
                        goal = "Clear lines and beat your best score.",
                    ),
                    onPlay = { closeTutorial(startAfterClose = true) },
                    onSkip = { closeTutorial(startAfterClose = false) },
                )
            }
        }
        state.gameOver && showCompletionSummary -> {
            {
                CompletionPopup(
                    title = if (state.score > (stats?.highScore ?: 0)) "New High Score" else "Run Summary",
                    lines = listOf(
                        "Score: ${state.score}",
                        "Lines: ${state.linesCleared}",
                        "Daily challenges and achievements updated after the run.",
                    ),
                    onContinue = { showCompletionSummary = false },
                )
            }
        }
        paused -> {
            {
                PremiumOverlayCard(title = "Run paused", subtitle = "Resume, restart, or leave the board.") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatRow("Score", state.score.toString())
                        StatRow("Lines", state.linesCleared.toString())
                        PremiumButton(label = "Resume", onClick = { paused = false }, modifier = Modifier.fillMaxWidth())
                        PremiumButton(label = "How to Play", onClick = ::showHowToPlay, modifier = Modifier.fillMaxWidth(), style = ArcadeButtonStyle.Secondary)
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
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val compact = maxWidth < 400.dp
                if (compact) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
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
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = colors.textPrimary
                                    )
                                }
                                HudPill("Score", state.score.toString())
                            }
                            HudPill("Lines", state.linesCleared.toString())
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            PremiumButton(
                                label = "How",
                                onClick = ::showHowToPlay,
                                style = ArcadeButtonStyle.Secondary,
                                borderOverride = colors.primaryCyan,
                                modifier = Modifier.weight(1f),
                            )
                            PremiumButton(
                                label = if (paused) "Resume" else "Pause",
                                onClick = { if (!state.gameOver && (state.playing || paused)) paused = !paused },
                                style = ArcadeButtonStyle.Secondary,
                                enabled = state.playing || paused,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                } else {
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
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = colors.textPrimary
                                )
                            }
                            HudPill("Score", state.score.toString())
                            HudPill("Lines", state.linesCleared.toString())
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            PremiumButton(
                                label = "How",
                                onClick = ::showHowToPlay,
                                style = ArcadeButtonStyle.Secondary,
                                borderOverride = colors.primaryCyan,
                            )
                            PremiumButton(
                                label = if (paused) "Resume" else "Pause",
                                onClick = { if (!state.gameOver && (state.playing || paused)) paused = !paused },
                                style = ArcadeButtonStyle.Secondary,
                                enabled = state.playing || paused,
                            )
                        }
                    }
                }
            }
        },
        controls = {
            if (state.playing) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing.sm),
                ) {
                    val compact = maxWidth < 420.dp
                    val controlHeight = if (maxWidth < 340.dp) 48.dp else 52.dp
                    val controlGap = if (compact) 6.dp else 8.dp
                    if (compact) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().height(controlHeight),
                                horizontalArrangement = Arrangement.spacedBy(controlGap)
                            ) {
                                PremiumButton(
                                    label = "Left",
                                    onClick = { handleAction(ArcadeGestureAction.SwipeLeft) },
                                    style = ArcadeButtonStyle.Secondary,
                                    modifier = Modifier.weight(1f).height(controlHeight),
                                )
                                PremiumButton(
                                    label = "Rotate",
                                    onClick = { handleAction(ArcadeGestureAction.Tap) },
                                    style = ArcadeButtonStyle.Secondary,
                                    modifier = Modifier.weight(1f).height(controlHeight),
                                    borderOverride = colors.accentViolet,
                                )
                                PremiumButton(
                                    label = "Right",
                                    onClick = { handleAction(ArcadeGestureAction.SwipeRight) },
                                    style = ArcadeButtonStyle.Secondary,
                                    modifier = Modifier.weight(1f).height(controlHeight),
                                )
                            }
                            PremiumButton(
                                label = "Drop",
                                onClick = { handleAction(ArcadeGestureAction.SwipeDown) },
                                style = ArcadeButtonStyle.Secondary,
                                modifier = Modifier.fillMaxWidth().height(controlHeight),
                                borderOverride = colors.primaryCyan,
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = controlHeight),
                            horizontalArrangement = Arrangement.spacedBy(controlGap),
                        ) {
                            PremiumButton(
                                label = "Left",
                                onClick = { handleAction(ArcadeGestureAction.SwipeLeft) },
                                style = ArcadeButtonStyle.Secondary,
                                modifier = Modifier.weight(1f).height(controlHeight),
                            )
                            PremiumButton(
                                label = "Rotate",
                                onClick = { handleAction(ArcadeGestureAction.Tap) },
                                style = ArcadeButtonStyle.Secondary,
                                modifier = Modifier.weight(1f).height(controlHeight),
                                borderOverride = colors.accentViolet,
                            )
                            PremiumButton(
                                label = "Drop",
                                onClick = { handleAction(ArcadeGestureAction.SwipeDown) },
                                style = ArcadeButtonStyle.Secondary,
                                modifier = Modifier.weight(1.2f).height(controlHeight),
                                borderOverride = colors.primaryCyan,
                            )
                            PremiumButton(
                                label = "Right",
                                onClick = { handleAction(ArcadeGestureAction.SwipeRight) },
                                style = ArcadeButtonStyle.Secondary,
                                modifier = Modifier.weight(1f).height(controlHeight),
                            )
                        }
                    }
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
        
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .background(colors.gameBoard, RoundedCornerShape(28.dp))
        ) {
            val compactPreview = maxWidth < 360.dp || maxHeight < 560.dp
            val boardPadding = if (maxWidth < 340.dp) 8.dp else 12.dp
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(boardPadding),
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
                NextPiecePreview(
                    piece = state.nextPiece,
                    compact = compactPreview,
                    modifier = Modifier.align(Alignment.TopEnd),
                )
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
}

@Composable
private fun NextPiecePreview(
    piece: PieceType,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = ArcadeTheme.colors
    val panelSize = if (compact) 68.dp else 82.dp
    val cellPadding = if (compact) 1.5f else 2f
    Column(
        modifier = modifier
            .background(colors.hudCard.copy(alpha = 0.94f), RoundedCornerShape(16.dp))
            .border(1.dp, colors.hudBorder, RoundedCornerShape(16.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = "NEXT",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Black,
            color = colors.textSecondary,
        )
        Canvas(modifier = Modifier.size(panelSize)) {
            val cells = piece.rotations.first()
            val minX = cells.minOf { it.x }
            val maxX = cells.maxOf { it.x }
            val minY = cells.minOf { it.y }
            val maxY = cells.maxOf { it.y }
            val pieceWidth = maxX - minX + 1
            val pieceHeight = maxY - minY + 1
            val cellSize = minOf(size.width / 4f, size.height / 4f)
            val startX = (size.width - pieceWidth * cellSize) / 2f
            val startY = (size.height - pieceHeight * cellSize) / 2f
            cells.forEach { cell ->
                val x = startX + (cell.x - minX) * cellSize
                val y = startY + (cell.y - minY) * cellSize
                drawRect(
                    color = Color(piece.color),
                    topLeft = Offset(x + cellPadding, y + cellPadding),
                    size = Size(cellSize - cellPadding * 2f, cellSize - cellPadding * 2f),
                )
            }
        }
    }
}
