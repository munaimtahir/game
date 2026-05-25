package com.vexel.offlinearcade.game.stackdrop

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
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
    fun rotateWithWallKickAtLeftEdge() {
        val engine = StackDropEngine(seed = 1)
        // I piece at X=0, vertical. Rotating it might push it out of bounds if not for kicks.
        // I rotations: [-1,0, 0,0, 1,0, 2,0] horizontal
        // [1,-1, 1,0, 1,1, 1,2] vertical
        val state = StackDropState(
            activePiece = ActivePiece(PieceType.I, x = -1, y = 5, rotationIndex = 1), // Vertical I at far left
            playing = true
        )
        // Rotation should normally collide with left wall (x < 0).
        // With kicks, it should shift right.
        val rotated = engine.rotate(state)
        assertTrue(rotated.activePiece.x >= 0)
        assertNotEquals(state.activePiece.rotationIndex, rotated.activePiece.rotationIndex)
    }

    @Test
    fun rotateWithFloorKick() {
        val engine = StackDropEngine(seed = 1)
        // T piece at bottom row.
        val state = StackDropState(
            activePiece = ActivePiece(PieceType.T, x = 5, y = STACK_DROP_HEIGHT - 1, rotationIndex = 0),
            playing = true
        )
        // T rotation 0: [(-1,0), (0,0), (1,0), (0,1)] -> cell at (0,1) is at y = STACK_DROP_HEIGHT
        // Rotating to index 1: [(0,-1), (0,0), (1,0), (0,1)] -> still has cell at y = STACK_DROP_HEIGHT
        // But if we kick it up by 1, it might fit.
        val rotated = engine.rotate(state)
        assertEquals(state.activePiece.y - 1, rotated.activePiece.y)
    }

    @Test
    fun lineClearLogicWorks() {
        val engine = StackDropEngine(seed = 1)
        val cells = IntArray(STACK_DROP_WIDTH * STACK_DROP_HEIGHT)
        // Fill bottom row except one cell
        for (i in 0 until STACK_DROP_WIDTH - 1) {
            cells[(STACK_DROP_HEIGHT - 1) * STACK_DROP_WIDTH + i] = 0xFFFFFFFF.toInt()
        }
        val state = StackDropState(
            board = StackDropBoard(cells),
            activePiece = ActivePiece(PieceType.I, x = STACK_DROP_WIDTH - 2, y = STACK_DROP_HEIGHT - 2, rotationIndex = 1), // Vertical I
            playing = true
        )
        // Force a tick to lock the piece. The I piece has cells at (1, -1), (1, 0), (1, 1), (1, 2)
        // So at x=8,y=20, it has cells at (9, 19), (9, 20), (9, 21), (9, 22).
        // Wait, the I piece rotation cells are relative.
        // (9, 20) would fill the gap in bottom row.
        val result = engine.tick(state, forceLock = true)
        
        assertTrue(result.linesCleared > 0)
        assertEquals(0, result.board.get(0, STACK_DROP_HEIGHT - 1)) // Row should be cleared
    }
}
