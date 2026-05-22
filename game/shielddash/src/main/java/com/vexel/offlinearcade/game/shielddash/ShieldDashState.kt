package com.vexel.offlinearcade.game.shielddash

import androidx.compose.ui.geometry.Offset

data class Hazard(
    val id: Int,
    var position: Offset,
    val velocity: Offset
)

data class ShieldDashState(
    val shieldAngle: Float,
    val hazards: List<Hazard>,
    val score: Int,
    val status: GameStatus
)

enum class GameStatus {
    Ready,
    Playing,
    GameOver
}
