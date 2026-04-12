package com.vexel.offlinearcade.game.stackdrop

import org.junit.Assert.assertTrue
import org.junit.Test

class StackDropEngineTest {
    @Test
    fun newGameStartsPlayable() {
        val engine = StackDropEngine(seed = 1)
        val state = engine.newState()
        assertTrue(state.playing)
        assertTrue(!state.gameOver)
    }

    @Test
    fun softDropAdvancesPiece() {
        val engine = StackDropEngine(seed = 1)
        val start = engine.newState()
        val dropped = engine.softDrop(start)
        assertTrue(dropped.activePiece.y >= start.activePiece.y)
    }

    @Test
    fun hardDropLocksPieceAndSpawnsNextOne() {
        val engine = StackDropEngine(seed = 1)
        val start = engine.newState()
        val dropped = engine.hardDrop(start)

        assertTrue(dropped.playing || dropped.gameOver)
        assertTrue(dropped.board.cells.any { it != 0 })
        assertTrue(dropped.activePiece.y <= 1)
    }

    @Test
    fun spawnBlockedBoardForcesGameOverOnTick() {
        val engine = StackDropEngine(seed = 1)
        val blockedCells = IntArray(STACK_DROP_WIDTH * STACK_DROP_HEIGHT)
        blockedCells[1 * STACK_DROP_WIDTH + 4] = 1
        blockedCells[1 * STACK_DROP_WIDTH + 5] = 1
        blockedCells[1 * STACK_DROP_WIDTH + 6] = 1
        blockedCells[1 * STACK_DROP_WIDTH + 7] = 1
        val blocked = StackDropState(
            board = StackDropBoard(blockedCells),
            activePiece = ActivePiece(PieceType.O),
            nextPiece = PieceType.I,
            playing = true,
        )
        val result = engine.tick(blocked, forceLock = true)
        assertTrue(result.gameOver)
        assertTrue(!result.playing)
    }

    @Test
    fun retryCreatesFreshPlayableStateAfterGameOver() {
        val engine = StackDropEngine(seed = 1)
        val blockedCells = IntArray(STACK_DROP_WIDTH * STACK_DROP_HEIGHT)
        blockedCells[1 * STACK_DROP_WIDTH + 4] = 1
        blockedCells[1 * STACK_DROP_WIDTH + 5] = 1
        blockedCells[1 * STACK_DROP_WIDTH + 6] = 1
        blockedCells[1 * STACK_DROP_WIDTH + 7] = 1
        val blocked = StackDropState(
            board = StackDropBoard(blockedCells),
            activePiece = ActivePiece(PieceType.O),
            nextPiece = PieceType.I,
            playing = true,
        )
        val gameOver = engine.tick(blocked, forceLock = true)
        val restart = engine.newState()

        assertTrue(gameOver.gameOver)
        assertTrue(restart.playing)
        assertTrue(!restart.gameOver)
    }
}
