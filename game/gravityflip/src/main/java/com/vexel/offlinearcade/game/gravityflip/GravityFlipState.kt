package com.vexel.offlinearcade.game.gravityflip

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

data class Obstacle(
    val id: Int,
    val position: Offset,
    val size: Size
)

data class Star(
    val id: Int,
    val position: Offset
)

data class GravityFlipState(
    val playerPosition: Offset = Offset(100f, 300f),
    val playerVelocityY: Float = 0f,
    val gravity: Float = 0.6f,
    val obstacles: List<Obstacle> = emptyList(),
    val stars: List<Star> = emptyList(),
    val score: Int = 0,
    val status: GameStatus = GameStatus.Ready,
    val runStartMillis: Long = 0L,
    val speed: Float = GravityFlipTuning.initialSpeed,
)

internal object GravityFlipTuning {
    const val initialSpeed = 6f
    const val maxSpeed = 15f
    const val speedRamp = 0.05f
    const val initialSpawnInterval = 1600L
    const val minimumSpawnInterval = 700L
    const val gravityForce = 0.7f
}

enum class GameStatus {
    Ready,
    Playing,
    GameOver
}
