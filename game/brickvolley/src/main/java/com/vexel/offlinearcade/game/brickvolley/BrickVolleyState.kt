package com.vexel.offlinearcade.game.brickvolley

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class Brick(
    val id: Int,
    val row: Int,
    val col: Int,
    val hp: Int,
    val maxHp: Int,
)

data class Ball(
    val position: Offset,
    val velocity: Offset,
    val active: Boolean = true,
)

data class BrickVolleyState(
    val bricks: List<Brick> = emptyList(),
    val balls: List<Ball> = emptyList(),
    val score: Int = 0,
    val turn: Int = 1,
    val status: GameStatus = GameStatus.Ready,
    val aimingLine: AimingLine? = null,
    val runStartMillis: Long = 0L,
    val ballCount: Int = 1,
)

internal object BrickVolleyTuning {
    const val columns = 6
    const val rows = 12
    const val ballSpeed = 22f
    const val initialBalls = 1
    const val bricksPerTurnProb = 0.6f
}

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
