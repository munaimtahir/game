package com.vexel.offlinearcade.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class ArcadeDispatchers(
    val io: CoroutineDispatcher = Dispatchers.IO,
    val default: CoroutineDispatcher = Dispatchers.Default,
)

fun interface ArcadeClock {
    fun currentEpochDay(): Long
}

object SystemArcadeClock : ArcadeClock {
    override fun currentEpochDay(): Long = System.currentTimeMillis() / (24 * 60 * 60 * 1000L)
}
