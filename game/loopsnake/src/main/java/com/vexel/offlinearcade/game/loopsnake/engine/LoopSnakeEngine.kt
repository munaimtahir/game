package com.vexel.offlinearcade.game.loopsnake.engine

import androidx.compose.ui.geometry.Offset
import com.vexel.offlinearcade.game.loopsnake.Direction
import com.vexel.offlinearcade.game.loopsnake.Food
import com.vexel.offlinearcade.game.loopsnake.GameState
import com.vexel.offlinearcade.game.loopsnake.GameStatus
import com.vexel.offlinearcade.game.loopsnake.SnakeBodyPart

class LoopSnakeEngine(
    val cols: Int = 15,
    val rows: Int = 20,
    private val seed: Long? = null
) {
    private val random = seed?.let { kotlin.random.Random(it) } ?: kotlin.random.Random.Default

    var state by kotlin.properties.Delegates.observable(createInitialState()) { _, _, newValue ->
        onStateChanged?.invoke(newValue)
    }
        private set

    var onStateChanged: ((GameState) -> Unit)? = null

    private var nextDirection: Direction? = null
    private var bufferedDirection: Direction? = null

    private fun createInitialState(): GameState {
        val initialSnake = listOf(
            SnakeBodyPart(Offset(7f, 10f)),
            SnakeBodyPart(Offset(6f, 10f)),
            SnakeBodyPart(Offset(5f, 10f))
        )
        val initialFood = spawnFood(initialSnake)
        return GameState(
            snake = initialSnake,
            food = initialFood,
            score = 0,
            status = GameStatus.Ready,
            direction = Direction.Right
        )
    }

    private fun spawnFood(snake: List<SnakeBodyPart>): Food {
        val occupied = snake.map { it.position }.toSet()
        val available = mutableListOf<Offset>()
        for (x in 0 until cols) {
            for (y in 0 until rows) {
                val pos = Offset(x.toFloat(), y.toFloat())
                if (pos !in occupied) {
                    available.add(pos)
                }
            }
        }
        return if (available.isNotEmpty()) {
            Food(available[random.nextInt(available.size)])
        } else {
            Food(Offset(-1f, -1f))
        }
    }

    fun startGame() {
        if (state.status == GameStatus.Ready) {
            state = state.copy(status = GameStatus.Playing)
        }
    }

    fun togglePause() {
        state = when (state.status) {
            GameStatus.Playing -> state.copy(status = GameStatus.Paused)
            GameStatus.Paused -> state.copy(status = GameStatus.Playing)
            else -> state
        }
    }

    fun reset() {
        nextDirection = null
        bufferedDirection = null
        state = createInitialState()
    }

    fun setFoodForTesting(position: Offset) {
        state = state.copy(food = Food(position))
    }

    fun setDirection(dir: Direction) {
        if (state.status == GameStatus.GameOver) return

        // If in Ready state, set direction immediately
        if (state.status == GameStatus.Ready) {
            if (!isOpposite(dir, state.direction)) {
                state = state.copy(direction = dir)
            }
            return
        }

        val currentActiveDir = nextDirection ?: state.direction
        if (!isOpposite(dir, currentActiveDir)) {
            if (nextDirection == null) {
                nextDirection = dir
            } else {
                val nextActiveDir = nextDirection!!
                if (!isOpposite(dir, nextActiveDir)) {
                    bufferedDirection = dir
                }
            }
        }
    }

    fun tick() {
        if (state.status != GameStatus.Playing) return

        // Consume next buffered direction if set
        val activeDirection = nextDirection ?: state.direction
        state = state.copy(direction = activeDirection)

        // Shift buffered directions
        nextDirection = bufferedDirection
        bufferedDirection = null

        val head = state.snake.first()
        val newHeadPos = when (activeDirection) {
            Direction.Up -> Offset(head.position.x, head.position.y - 1)
            Direction.Down -> Offset(head.position.x, head.position.y + 1)
            Direction.Left -> Offset(head.position.x - 1, head.position.y)
            Direction.Right -> Offset(head.position.x + 1, head.position.y)
        }

        // Wall collision
        if (newHeadPos.x < 0 || newHeadPos.x >= cols || newHeadPos.y < 0 || newHeadPos.y >= rows) {
            state = state.copy(status = GameStatus.GameOver)
            return
        }

        val newHeadPart = SnakeBodyPart(newHeadPos)

        // Self collision
        val bodyToCheck = if (newHeadPos == state.food.position) {
            state.snake
        } else {
            state.snake.dropLast(1)
        }

        if (bodyToCheck.any { it.position == newHeadPos }) {
            state = state.copy(status = GameStatus.GameOver)
            return
        }

        // Move/Grow
        val newSnake = mutableListOf(newHeadPart)
        newSnake.addAll(state.snake)

        if (newHeadPos == state.food.position) {
            // Eat food
            val newScore = state.score + 10
            state = state.copy(
                snake = newSnake,
                score = newScore,
                food = spawnFood(newSnake)
            )
        } else {
            // Move only
            newSnake.removeAt(newSnake.lastIndex)
            state = state.copy(
                snake = newSnake
            )
        }
    }

    private fun isOpposite(d1: Direction, d2: Direction): Boolean {
        return (d1 == Direction.Up && d2 == Direction.Down) ||
               (d1 == Direction.Down && d2 == Direction.Up) ||
               (d1 == Direction.Left && d2 == Direction.Right) ||
               (d1 == Direction.Right && d2 == Direction.Left)
    }
}
