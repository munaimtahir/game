package com.vexel.offlinearcade.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.time.Instant
import java.time.ZoneId

data class ArcadeDispatchers(
    val io: CoroutineDispatcher = Dispatchers.IO,
    val default: CoroutineDispatcher = Dispatchers.Default,
)

fun interface ArcadeClock {
    fun currentEpochDay(): Long
}

object SystemArcadeClock : ArcadeClock {
    override fun currentEpochDay(): Long = LocalDaySnapshot.from(Instant.now(), ZoneId.systemDefault()).epochDay
}

data class LocalDaySnapshot(
    val epochDay: Long,
    val zoneId: String,
) {
    companion object {
        fun from(
            instant: Instant,
            zoneId: ZoneId,
        ): LocalDaySnapshot {
            return LocalDaySnapshot(
                epochDay = instant.atZone(zoneId).toLocalDate().toEpochDay(),
                zoneId = zoneId.id,
            )
        }
    }
}

fun interface InstantProvider {
    fun now(): Instant
}

fun interface ZoneIdProvider {
    fun zoneId(): ZoneId
}

interface LocalDayService {
    fun currentDay(): LocalDaySnapshot
}

object SystemInstantProvider : InstantProvider {
    override fun now(): Instant = Instant.now()
}

object SystemZoneIdProvider : ZoneIdProvider {
    override fun zoneId(): ZoneId = ZoneId.systemDefault()
}
