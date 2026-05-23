package com.vexel.offlinearcade.game.loopsnake

import androidx.compose.ui.geometry.Offset

data class SnakeBodyPart(
    val position: Offset
)

data class Food(
    val position: Offset
)

data class GameState(
    val snake: List<SnakeBodyPart>,
    val food: Food,
    val score: Int,
    val status: GameStatus,
    val direction: Direction
)

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
