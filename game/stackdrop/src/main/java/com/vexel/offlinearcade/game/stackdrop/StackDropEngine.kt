package com.vexel.offlinearcade.game.stackdrop

import kotlin.random.Random

const val STACK_DROP_WIDTH = 10
const val STACK_DROP_HEIGHT = 18

data class Cell(val x: Int, val y: Int)

enum class PieceType(val color: Int, val rotations: List<List<Cell>>) {
    I(
        color = 0xFF0891B2.toInt(),
        rotations = listOf(
            listOf(Cell(-1, 0), Cell(0, 0), Cell(1, 0), Cell(2, 0)),
            listOf(Cell(1, -1), Cell(1, 0), Cell(1, 1), Cell(1, 2)),
        ),
    ),
    O(
        color = 0xFFD97706.toInt(),
        rotations = listOf(listOf(Cell(0, 0), Cell(1, 0), Cell(0, 1), Cell(1, 1))),
    ),
    T(
        color = 0xFF7C3AED.toInt(),
        rotations = listOf(
            listOf(Cell(-1, 0), Cell(0, 0), Cell(1, 0), Cell(0, 1)),
            listOf(Cell(0, -1), Cell(0, 0), Cell(1, 0), Cell(0, 1)),
            listOf(Cell(0, -1), Cell(-1, 0), Cell(0, 0), Cell(1, 0)),
            listOf(Cell(0, -1), Cell(-1, 0), Cell(0, 0), Cell(0, 1)),
        ),
    ),
    L(
        color = 0xFFEA580C.toInt(),
        rotations = listOf(
            listOf(Cell(-1, 0), Cell(0, 0), Cell(1, 0), Cell(1, 1)),
            listOf(Cell(0, -1), Cell(0, 0), Cell(0, 1), Cell(1, -1)),
            listOf(Cell(-1, -1), Cell(-1, 0), Cell(0, 0), Cell(1, 0)),
            listOf(Cell(-1, 1), Cell(0, -1), Cell(0, 0), Cell(0, 1)),
        ),
    ),
    S(
        color = 0xFF16A34A.toInt(),
        rotations = listOf(
            listOf(Cell(0, 0), Cell(1, 0), Cell(-1, 1), Cell(0, 1)),
            listOf(Cell(0, -1), Cell(0, 0), Cell(1, 0), Cell(1, 1)),
        ),
    ),
}

data class ActivePiece(
    val type: PieceType,
    val x: Int = STACK_DROP_WIDTH / 2,
    val y: Int = 1,
    val rotationIndex: Int = 0,
) {
    val cells: List<Cell> = type.rotations[rotationIndex % type.rotations.size]
}

data class StackDropBoard(
    val cells: IntArray = IntArray(STACK_DROP_WIDTH * STACK_DROP_HEIGHT),
) {
    fun get(x: Int, y: Int): Int = cells[y * STACK_DROP_WIDTH + x]
}

data class StackDropState(
    val board: StackDropBoard = StackDropBoard(),
    val activePiece: ActivePiece = ActivePiece(PieceType.T),
    val nextPiece: PieceType = PieceType.I,
    val score: Int = 0,
    val linesCleared: Int = 0,
    val level: Int = 1,
    val dropIntervalMillis: Long = 700L,
    val recentLineClearCount: Int = 0,
    val playing: Boolean = false,
    val gameOver: Boolean = false,
    val runStartMillis: Long = 0L,
)

class StackDropEngine(seed: Int = 7) {
    private val random = Random(seed)

    fun newState(): StackDropState {
        val first = PieceType.entries.random(random)
        val next = PieceType.entries.random(random)
        val state = StackDropState(
            activePiece = ActivePiece(first),
            nextPiece = next,
            playing = true,
            runStartMillis = System.currentTimeMillis(),
        )
        return if (collides(state.board, state.activePiece)) state.copy(playing = false, gameOver = true) else state
    }

    fun move(state: StackDropState, dx: Int): StackDropState {
        if (!state.playing) return state
        val shifted = state.activePiece.copy(x = state.activePiece.x + dx)
        return if (collides(state.board, shifted)) state else state.copy(activePiece = shifted)
    }

    fun rotate(state: StackDropState): StackDropState {
        if (!state.playing) return state
        val rotated = state.activePiece.copy(rotationIndex = (state.activePiece.rotationIndex + 1) % state.activePiece.type.rotations.size)
        return if (collides(state.board, rotated)) state else state.copy(activePiece = rotated)
    }

    fun softDrop(state: StackDropState): StackDropState = tick(state, forceLock = false, extraScore = 1)

    fun hardDrop(state: StackDropState): StackDropState {
        if (!state.playing) return state
        var dropped = state
        while (true) {
            val fallen = dropped.activePiece.copy(y = dropped.activePiece.y + 1)
            if (collides(dropped.board, fallen)) {
                return lockPiece(dropped)
            }
            dropped = dropped.copy(activePiece = fallen)
        }
    }

    fun tick(state: StackDropState, forceLock: Boolean = false, extraScore: Int = 0): StackDropState {
        if (!state.playing) return state
        val fallen = state.activePiece.copy(y = state.activePiece.y + 1)
        return if (!forceLock && !collides(state.board, fallen)) {
            state.copy(activePiece = fallen, score = state.score + extraScore, recentLineClearCount = 0)
        } else {
            lockPiece(state.copy(score = state.score + extraScore))
        }
    }

    private fun lockPiece(state: StackDropState): StackDropState {
        val nextCells = state.board.cells.copyOf()
        state.activePiece.cells.forEach { cell ->
            val x = state.activePiece.x + cell.x
            val y = state.activePiece.y + cell.y
            if (y in 0 until STACK_DROP_HEIGHT && x in 0 until STACK_DROP_WIDTH) {
                nextCells[y * STACK_DROP_WIDTH + x] = state.activePiece.type.color
            }
        }

        var clearedRows = 0
        var writeRow = STACK_DROP_HEIGHT - 1
        for (row in (STACK_DROP_HEIGHT - 1) downTo 0) {
            val rowStart = row * STACK_DROP_WIDTH
            var full = true
            for (column in 0 until STACK_DROP_WIDTH) {
                if (nextCells[rowStart + column] == 0) {
                    full = false
                    break
                }
            }
            if (full) {
                clearedRows += 1
                continue
            }
            if (writeRow != row) {
                val writeStart = writeRow * STACK_DROP_WIDTH
                for (column in 0 until STACK_DROP_WIDTH) {
                    nextCells[writeStart + column] = nextCells[rowStart + column]
                }
            }
            writeRow -= 1
        }
        while (writeRow >= 0) {
            val clearStart = writeRow * STACK_DROP_WIDTH
            for (column in 0 until STACK_DROP_WIDTH) {
                nextCells[clearStart + column] = 0
            }
            writeRow -= 1
        }
        val nextBoard = StackDropBoard(nextCells)

        val lineBonus = when (clearedRows) {
            1 -> 100
            2 -> 250
            3 -> 450
            4 -> 700
            else -> 0
        }
        val totalLines = state.linesCleared + clearedRows
        val level = 1 + totalLines / 6
        val nextPiece = ActivePiece(type = state.nextPiece)
        val nextState = state.copy(
            board = nextBoard,
            activePiece = nextPiece,
            nextPiece = PieceType.entries.random(random),
            score = state.score + lineBonus,
            linesCleared = totalLines,
            level = level,
            dropIntervalMillis = (700L - (level - 1) * 45L).coerceAtLeast(180L),
            recentLineClearCount = clearedRows,
        )
        return if (collides(nextBoard, nextPiece)) {
            nextState.copy(playing = false, gameOver = true)
        } else {
            nextState
        }
    }

    private fun collides(board: StackDropBoard, piece: ActivePiece): Boolean = piece.cells.any { cell ->
        val x = piece.x + cell.x
        val y = piece.y + cell.y
        x !in 0 until STACK_DROP_WIDTH || y !in 0 until STACK_DROP_HEIGHT || board.get(x, y) != 0
    }
}
