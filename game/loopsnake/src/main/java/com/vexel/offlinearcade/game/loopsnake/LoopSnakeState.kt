package com.vexel.offlinearcade.game.loopsnake

import androidx.compose.ui.geometry.Offset

data class SnakeBodyPart(
    val position: Offset
)

data class Food(
    val position: Offset
)

data class LoopSnakeState(
    val snake: List<SnakeBodyPart> = listOf(SnakeBodyPart(Offset(10f, 15f))),
    val food: Food = Food(Offset(10f, 10f)),
    val score: Int = 0,
    val status: GameStatus = GameStatus.Ready,
    val direction: Direction = Direction.Right,
    val runStartMillis: Long = 0L,
    val lastTickMillis: Long = 0L,
    val speedDelay: Long = LoopSnakeTuning.initialDelay,
)

internal object LoopSnakeTuning {
    const val initialDelay = 180L
    const val minimumDelay = 80L
    const val delayReductionPerScore = 2L
    const val gridCellsX = 20
    const val gridCellsY = 32
}

enum class GameStatus {
    Ready,
    Playing,
    Paused,
    GameOver
}

enum class Direction {
    Up,
    Down,
    Left,
    Right
}
