package com.vexel.offlinearcade.game.shielddash

import androidx.compose.ui.geometry.Offset

data class Hazard(
    val id: Int,
    val position: Offset,
    val velocity: Offset
)

data class ShieldDashState(
    val shieldAngle: Float = 0f,
    val hazards: List<Hazard> = emptyList(),
    val score: Int = 0,
    val status: GameStatus = GameStatus.Ready,
    val runStartMillis: Long = 0L,
)

internal object ShieldDashTuning {
    const val coreRadius = 45f
    const val shieldInnerRadius = 65f
    const val shieldOuterRadius = 85f
    const val initialSpawnInterval = 1800L
    const val minimumSpawnInterval = 600L
    const val spawnIntervalReductionPerScore = 15L
}

enum class GameStatus {
    Ready,
    Playing,
    GameOver
}
