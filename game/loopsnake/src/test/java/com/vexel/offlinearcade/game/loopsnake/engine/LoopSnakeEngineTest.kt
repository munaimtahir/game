package com.vexel.offlinearcade.game.loopsnake.engine

import androidx.compose.ui.geometry.Offset
import com.vexel.offlinearcade.game.loopsnake.Direction
import com.vexel.offlinearcade.game.loopsnake.GameStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LoopSnakeEngineTest {

    @Test
    fun testInitialStateIsValid() {
        val engine = LoopSnakeEngine(seed = 42L)
        val state = engine.state
        assertEquals(GameStatus.Ready, state.status)
        assertEquals(Direction.Right, state.direction)
        assertEquals(0, state.score)
    }

    @Test
    fun testSnakeStartsWithExpectedLength() {
        val engine = LoopSnakeEngine()
        assertEquals(3, engine.state.snake.size)
        // Snake should extend to the left from (7, 10)
        assertEquals(Offset(7f, 10f), engine.state.snake[0].position)
        assertEquals(Offset(6f, 10f), engine.state.snake[1].position)
        assertEquals(Offset(5f, 10f), engine.state.snake[2].position)
    }

    @Test
    fun testReadyStateCanTransitionToMoving() {
        val engine = LoopSnakeEngine()
        assertEquals(GameStatus.Ready, engine.state.status)
        engine.startGame()
        assertEquals(GameStatus.Playing, engine.state.status)
    }

    @Test
    fun testValidDirectionChangeWorks() {
        val engine = LoopSnakeEngine()
        engine.startGame()
        engine.setDirection(Direction.Down)
        engine.tick()
        assertEquals(Direction.Down, engine.state.direction)
        assertEquals(Offset(7f, 11f), engine.state.snake[0].position)
    }

    @Test
    fun testReverseDirectionIsRejected() {
        val engine = LoopSnakeEngine()
        engine.startGame()
        // Current direction is Right, so Left should be rejected
        engine.setDirection(Direction.Left)
        engine.tick()
        assertEquals(Direction.Right, engine.state.direction)
        assertEquals(Offset(8f, 10f), engine.state.snake[0].position)
    }

    @Test
    fun testSnakeMovesOneCellPerTick() {
        val engine = LoopSnakeEngine()
        engine.startGame()
        engine.tick()
        assertEquals(Offset(8f, 10f), engine.state.snake[0].position)
        assertEquals(Offset(7f, 10f), engine.state.snake[1].position)
        assertEquals(Offset(6f, 10f), engine.state.snake[2].position)
    }

    @Test
    fun testScoreIsInitiallyZero() {
        val engine = LoopSnakeEngine()
        assertEquals(0, engine.state.score)
    }

    @Test
    fun testOrbCollectionIncreasesScoreAndLength() {
        val engine = LoopSnakeEngine(cols = 15, rows = 20)
        engine.setFoodForTesting(Offset(8f, 10f))
        engine.startGame()
        
        // Snake is at (7, 10) moving Right. Hitting (8, 10) should eat food.
        engine.tick()
        
        assertEquals(10, engine.state.score)
        assertEquals(4, engine.state.snake.size)
        assertNotEquals(Offset(8f, 10f), engine.state.food.position)
    }

    @Test
    fun testOrbNeverSpawnsInsideSnakeBody() {
        val engine = LoopSnakeEngine(cols = 5, rows = 5, seed = 999L)
        // Let's make the snake very long to fill up almost the entire grid, 
        // and ensure the food only spawns on the remaining empty spaces
        engine.startGame()
        // Run a few ticks to collect food and grow
        for (i in 0 until 100) {
            val head = engine.state.snake[0].position
            val food = engine.state.food.position
            // Simple direct movement towards food
            if (head.x < food.x) engine.setDirection(Direction.Right)
            else if (head.x > food.x) engine.setDirection(Direction.Left)
            else if (head.y < food.y) engine.setDirection(Direction.Down)
            else if (head.y > food.y) engine.setDirection(Direction.Up)
            engine.tick()
            
            // Check that food is never on the snake body
            val occupied = engine.state.snake.map { it.position }.toSet()
            assertTrue(engine.state.food.position !in occupied)
        }
    }

    @Test
    fun testWallCollisionTriggersGameOver() {
        val engine = LoopSnakeEngine(cols = 10, rows = 20)
        engine.startGame()
        // Move right until hitting the wall (cols = 10, start head x = 7)
        engine.tick() // x = 8
        assertEquals(GameStatus.Playing, engine.state.status)
        engine.tick() // x = 9
        assertEquals(GameStatus.Playing, engine.state.status)
        engine.tick() // x = 10 -> Collision!
        assertEquals(GameStatus.GameOver, engine.state.status)
    }

    @Test
    fun testSelfCollisionTriggersGameOver() {
        val engine = LoopSnakeEngine(cols = 20, rows = 20)
        engine.startGame()
        
        // Let's grow the snake by tricking/navigating or by just making a loop.
        // Wait, a snake of length 3 cannot collide with itself. It needs to be at least length 5 to collide with itself.
        // Let's grow it by navigating to food, then do a self-intersecting move.
        // Let's navigate to collect 2 foods to grow to length 5.
        while (engine.state.snake.size < 5 && engine.state.status == GameStatus.Playing) {
            val head = engine.state.snake[0].position
            val food = engine.state.food.position
            if (head.x < food.x) engine.setDirection(Direction.Right)
            else if (head.x > food.x) engine.setDirection(Direction.Left)
            else if (head.y < food.y) engine.setDirection(Direction.Down)
            else if (head.y > food.y) engine.setDirection(Direction.Up)
            engine.tick()
        }
        
        assertTrue("Snake should have reached length 5 for this test", engine.state.snake.size >= 5)
        
        // Snake is now length 5. Let's make a self-colliding loop.
        // Head is going some direction, let's turn to form a loop.
        // E.g. Right, Down, Left, Up
        engine.setDirection(Direction.Right)
        engine.tick()
        engine.setDirection(Direction.Down)
        engine.tick()
        engine.setDirection(Direction.Left)
        engine.tick()
        engine.setDirection(Direction.Up)
        engine.tick()
        
        assertEquals(GameStatus.GameOver, engine.state.status)
    }

    @Test
    fun testRestartResetsScoreStateLength() {
        val engine = LoopSnakeEngine(cols = 10, rows = 10)
        engine.startGame()
        engine.tick()
        engine.tick()
        engine.tick() // Hits wall -> Game Over
        assertEquals(GameStatus.GameOver, engine.state.status)
        
        engine.reset()
        assertEquals(GameStatus.Ready, engine.state.status)
        assertEquals(3, engine.state.snake.size)
        assertEquals(0, engine.state.score)
    }

    @Test
    fun testDeterministicSeedGivesStableSpawn() {
        val engine1 = LoopSnakeEngine(seed = 42L)
        val engine2 = LoopSnakeEngine(seed = 42L)
        assertEquals(engine1.state.food.position, engine2.state.food.position)
    }
}
