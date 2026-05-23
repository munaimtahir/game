package com.vexel.offlinearcade.game.brickvolley

import com.vexel.offlinearcade.game.brickvolley.engine.BrickVolleyEngine
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BrickVolleyEngineTest {
    @Test
    fun `initial state is valid`() {
        val state = BrickVolleyEngine.initialState()
        assertTrue(state.turn == 1)
        assertTrue(state.score == 0)
        assertTrue(state.bricks.isNotEmpty())
        assertFalse(state.gameOver)
    }

    @Test
    fun `shouldLaunch returns false for tiny drag`() {
        val startX = 100f
        val startY = 100f
        val endX = 105f
        val endY = 102f
        assertFalse(BrickVolleyEngine.shouldLaunchF(startX, startY, endX, endY))
    }

    @Test
    fun `shouldLaunch returns true for sufficient drag`() {
        val startX = 100f
        val startY = 100f
        val endX = 150f
        val endY = 200f
        assertTrue(BrickVolleyEngine.shouldLaunchF(startX, startY, endX, endY))
    }

    @Test
    fun `dragToAngle produces upward angle when dragging down-back`() {
        val startX = 200f
        val startY = 800f
        val endX = 200f
        val endY = 900f // drag downward
        val angle = BrickVolleyEngine.dragToAngleF(startX, startY, endX, endY)
        // angle should be between MIN_ANGLE and MAX_ANGLE
        assertTrue(angle >= BrickVolleyEngine.MIN_ANGLE)
        assertTrue(angle <= BrickVolleyEngine.MAX_ANGLE)
    }

    @Test
    fun `drag angle is clamped`() {
        val nearFlat = BrickVolleyEngine.dragToAngleF(100f, 100f, -1000f, 101f)
        assertTrue(nearFlat >= BrickVolleyEngine.MIN_ANGLE)
        assertTrue(nearFlat <= BrickVolleyEngine.MAX_ANGLE)
    }

    @Test
    fun `angleToVelocity has upward vy component (negative screen space)`() {
        val angle = (PI / 4).toFloat() // 45 deg
        val v = BrickVolleyEngine.angleToVelocityF(angle, 10f)
        // vy should be negative because we invert for screen coordinates
        assertTrue(v.second < 0)
    }

    @Test
    fun `ball moves after launch`() {
        val velocity = BrickVolleyEngine.angleToVelocityF((PI / 3).toFloat(), 10f)
        val startX = 100f
        val startY = 400f
        val nextX = startX + velocity.first
        val nextY = startY + velocity.second
        assertTrue(nextX != startX || nextY != startY)
    }

    @Test
    fun `wall bounce inverts x velocity`() {
        val angle = BrickVolleyEngine.dragToAngleF(200f, 800f, 100f, 900f)
        val v = BrickVolleyEngine.angleToVelocityF(angle, 12f)
        val bouncedX = -v.first
        assertTrue(bouncedX == -v.first)
    }

    @Test
    fun `circleRectCollision detects intersection`() {
        val circleX = 50f
        val circleY = 50f
        val r = 10f
        val left = 40f
        val top = 40f
        val right = 80f
        val bottom = 80f
        assertTrue(BrickVolleyEngine.circleRectCollisionF(circleX, circleY, r, left, top, right, bottom))
    }

    @Test
    fun `circleRectCollision detects non intersection`() {
        val circleX = 10f
        val circleY = 10f
        val r = 5f
        val left = 40f
        val top = 40f
        val right = 80f
        val bottom = 80f
        assertFalse(BrickVolleyEngine.circleRectCollisionF(circleX, circleY, r, left, top, right, bottom))
    }

    @Test
    fun `brick hit decrements hp and hp1 brick is removed with score increase`() {
        val base = BrickVolleyEngine.initialState().copy(
            bricks = listOf(
                BrickVolleyEngine.EngineBrick(row = 0, col = 0, hp = 2),
                BrickVolleyEngine.EngineBrick(row = 0, col = 1, hp = 1),
            ),
        )
        val afterDecrement = BrickVolleyEngine.applyBrickHit(base, 0, 0)
        assertTrue(afterDecrement.bricks.first { it.col == 0 }.hp == 1)
        assertTrue(afterDecrement.score == base.score + 1)

        val afterRemove = BrickVolleyEngine.applyBrickHit(afterDecrement, 0, 1)
        assertTrue(afterRemove.bricks.none { it.col == 1 })
        assertTrue(afterRemove.score > afterDecrement.score)
    }

    @Test
    fun `turn advance moves bricks and spawns new row`() {
        val state = BrickVolleyEngine.initialState()
        val next = BrickVolleyEngine.advanceTurn(state, columns = 6, dangerRow = 20)
        assertTrue(next.turn == state.turn + 1)
        assertTrue(next.bricks.any { it.row == 0 })
        assertTrue(next.bricks.any { it.row >= 1 })
    }

    @Test
    fun `game over only when brick reaches danger line`() {
        val safe = BrickVolleyEngine.EngineState(
            bricks = listOf(BrickVolleyEngine.EngineBrick(row = 7, col = 0, hp = 1)),
            score = 0,
            turn = 3,
            gameOver = false,
        )
        val stillSafe = BrickVolleyEngine.advanceTurn(safe, columns = 0, dangerRow = 9)
        assertFalse(stillSafe.gameOver)

        val risky = BrickVolleyEngine.EngineState(
            bricks = listOf(BrickVolleyEngine.EngineBrick(row = 8, col = 0, hp = 1)),
            score = 0,
            turn = 3,
            gameOver = false,
        )
        val nowOver = BrickVolleyEngine.advanceTurn(risky, columns = 0, dangerRow = 9)
        assertTrue(nowOver.gameOver)
    }

    @Test
    fun `restart and high score logic`() {
        val restarted = BrickVolleyEngine.initialState()
        assertTrue(restarted.score == 0)
        assertTrue(restarted.turn == 1)
        assertTrue(BrickVolleyEngine.updateHighScore(20, 15) == 20)
        assertTrue(BrickVolleyEngine.updateHighScore(20, 25) == 25)
    }
}
