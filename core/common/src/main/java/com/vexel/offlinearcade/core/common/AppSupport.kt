package com.vexel.offlinearcade.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.time.LocalDate
import java.time.ZoneOffset

data class ArcadeDispatchers(
    val io: CoroutineDispatcher = Dispatchers.IO,
    val default: CoroutineDispatcher = Dispatchers.Default,
)

fun interface ArcadeClock {
    fun currentEpochDay(): Long
}

object SystemArcadeClock : ArcadeClock {
    override fun currentEpochDay(): Long = LocalDate.now(ZoneOffset.UTC).toEpochDay()
}
