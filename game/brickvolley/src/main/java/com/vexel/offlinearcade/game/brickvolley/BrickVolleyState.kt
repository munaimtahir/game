package com.vexel.offlinearcade.game.brickvolley

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class Brick(
    val id: Int,
    val row: Int,
    val col: Int,
    var hp: Int,
    val color: Color
)

data class Ball(
    val id: Int,
    var position: Offset,
    var velocity: Offset
)

data class GameState(
    val bricks: List<Brick>,
    val balls: List<Ball>,
    val score: Int,
    val turn: Int,
    val status: GameStatus,
    val aimingLine: AimingLine?
)

enum class GameStatus {
    Ready,
    Aiming,
    Animating,
    GameOver
}

data class AimingLine(
    val start: Offset,
    val end: Offset
)
