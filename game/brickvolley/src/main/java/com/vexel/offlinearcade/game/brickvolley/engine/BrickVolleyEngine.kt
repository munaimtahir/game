package com.vexel.offlinearcade.game.brickvolley.engine

import androidx.compose.ui.geometry.Offset
import kotlin.math.*

object BrickVolleyEngine {
    // Minimum drag distance in pixels to start a shot (tweakable)
    const val MIN_DRAG_DISTANCE = 24f

    // Speed factor (pixels per frame at dt=1). Tweak based on canvas size in caller.
    const val BASE_SPEED = 12f

    // Angle clamp in radians to avoid flat shots (15 degrees)
    val MIN_ANGLE = Math.toRadians(15.0).toFloat()
    val MAX_ANGLE = Math.toRadians(165.0).toFloat()

    fun shouldLaunch(start: Offset, end: Offset): Boolean {
        val dx = end.x - start.x
        val dy = end.y - start.y
        return sqrt(dx * dx + dy * dy) >= MIN_DRAG_DISTANCE
    }

    // Drag vector (from start to end) maps to shot vector: user drags down/back -> shoot up.
    fun dragToAngle(start: Offset, end: Offset): Float {
        val dx = start.x - end.x // invert so dragging right -> right
        val dy = end.y - start.y // dragging down increases dy -> produce upward angle
        val angle = atan2(dy, dx) // angle in radians
        // clamp angle between MIN_ANGLE and MAX_ANGLE
        val clamped = angle.coerceIn(MIN_ANGLE, MAX_ANGLE)
        return clamped
    }

    fun angleToVelocity(angle: Float, speed: Float = BASE_SPEED): Offset {
        val vx = cos(angle) * speed
        val vy = -sin(angle) * speed // negative because screen Y increases downward
        return Offset(vx, vy)
    }

    // Circle-Rect collision: returns true if circle at center with radius r intersects rect
    fun circleRectCollision(circleCenter: Offset, radius: Float, left: Float, top: Float, right: Float, bottom: Float): Boolean {
        val closestX = circleCenter.x.coerceIn(left, right)
        val closestY = circleCenter.y.coerceIn(top, bottom)
        val dx = circleCenter.x - closestX
        val dy = circleCenter.y - closestY
        return dx * dx + dy * dy <= radius * radius
    }

    // Pure-kotlin variants that avoid Compose types for JVM unit tests
    fun shouldLaunchF(startX: Float, startY: Float, endX: Float, endY: Float): Boolean {
        val dx = endX - startX
        val dy = endY - startY
        return kotlin.math.sqrt(dx * dx + dy * dy) >= MIN_DRAG_DISTANCE
    }

    fun dragToAngleF(startX: Float, startY: Float, endX: Float, endY: Float): Float {
        val dx = startX - endX
        val dy = endY - startY
        val angle = kotlin.math.atan2(dy, dx)
        return angle.coerceIn(MIN_ANGLE, MAX_ANGLE)
    }

    fun angleToVelocityF(angle: Float, speed: Float = BASE_SPEED): Pair<Float, Float> {
        val vx = kotlin.math.cos(angle) * speed
        val vy = -kotlin.math.sin(angle) * speed
        return Pair(vx, vy)
    }

    fun circleRectCollisionF(circleX: Float, circleY: Float, radius: Float, left: Float, top: Float, right: Float, bottom: Float): Boolean {
        val closestX = circleX.coerceIn(left, right)
        val closestY = circleY.coerceIn(top, bottom)
        val dx = circleX - closestX
        val dy = circleY - closestY
        return dx * dx + dy * dy <= radius * radius
    }

    data class EngineBrick(val row: Int, val col: Int, val hp: Int)

    data class EngineState(
        val bricks: List<EngineBrick>,
        val score: Int,
        val turn: Int,
        val gameOver: Boolean,
    )

    fun initialState(): EngineState {
        val initialBricks = listOf(
            EngineBrick(row = 0, col = 0, hp = 1),
            EngineBrick(row = 0, col = 2, hp = 1),
            EngineBrick(row = 1, col = 1, hp = 1),
        )
        return EngineState(
            bricks = initialBricks,
            score = 0,
            turn = 1,
            gameOver = false,
        )
    }

    fun applyBrickHit(state: EngineState, targetRow: Int, targetCol: Int): EngineState {
        val index = state.bricks.indexOfFirst { it.row == targetRow && it.col == targetCol }
        if (index == -1) return state
        val target = state.bricks[index]
        val updated = if (target.hp <= 1) {
            state.bricks.toMutableList().also { it.removeAt(index) }
        } else {
            state.bricks.toMutableList().also { it[index] = target.copy(hp = target.hp - 1) }
        }
        val clearBonus = if (target.hp <= 1) 5 else 0
        return state.copy(
            bricks = updated,
            score = state.score + 1 + clearBonus,
        )
    }

    fun advanceTurn(state: EngineState, columns: Int = 6, dangerRow: Int = 9): EngineState {
        val moved = state.bricks.map { it.copy(row = it.row + 1) }.toMutableList()
        val nextTurn = state.turn + 1
        val spawned = (0 until columns).mapNotNull { col ->
            if (((nextTurn + col) % 2) == 0) EngineBrick(row = 0, col = col, hp = max(1, nextTurn / 2)) else null
        }
        moved.addAll(spawned)
        val gameOver = moved.any { it.row >= dangerRow }
        return state.copy(
            bricks = moved,
            turn = nextTurn,
            gameOver = gameOver,
        )
    }

    fun updateHighScore(existing: Int, candidate: Int): Int = max(existing, candidate)
}
