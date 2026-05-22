package com.vexel.offlinearcade.game.gravityflip

import androidx.compose.ui.geometry.Offset

data class Obstacle(
    val id: Int,
    var position: Offset,
    val size: androidx.compose.ui.geometry.Size
)

data class Star(
    val id: Int,
    var position: Offset
)

data class GravityFlipState(
    val playerPosition: Offset,
    val playerVelocityY: Float,
    val gravity: Float,
    val obstacles: List<Obstacle>,
    val stars: List<Star>,
    val score: Int,
    val status: GameStatus
)

enum class GameStatus {
    Ready,
    Playing,
    GameOver
}
