package com.vexel.offlinearcade.core.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.vexel.offlinearcade.core.common.InstantProvider
import com.vexel.offlinearcade.core.common.LocalDayService
import com.vexel.offlinearcade.core.common.LocalDaySnapshot
import com.vexel.offlinearcade.core.common.SystemInstantProvider
import com.vexel.offlinearcade.core.common.SystemZoneIdProvider
import com.vexel.offlinearcade.core.common.ZoneIdProvider

private const val LocalDayEpochKey = "local_day_epoch"
private const val LocalDayZoneKey = "local_day_zone"

class SharedPreferencesLocalDayService(
    private val sharedPreferences: SharedPreferences,
    private val instantProvider: InstantProvider = SystemInstantProvider,
    private val zoneIdProvider: ZoneIdProvider = SystemZoneIdProvider,
) : LocalDayService {

    override fun currentDay(): LocalDaySnapshot {
        val current = LocalDaySnapshot.from(
            instant = instantProvider.now(),
            zoneId = zoneIdProvider.zoneId(),
        )
        val persisted = readPersisted()
        val resolved = when {
            persisted == null -> current
            current.epochDay > persisted.epochDay -> current
            current.epochDay == persisted.epochDay -> current
            else -> persisted.copy(zoneId = current.zoneId)
        }
        if (persisted != resolved) {
            sharedPreferences.edit(commit = true) {
                putLong(LocalDayEpochKey, resolved.epochDay)
                putString(LocalDayZoneKey, resolved.zoneId)
            }
        }
        return resolved
    }

    private fun readPersisted(): LocalDaySnapshot? {
        if (!sharedPreferences.contains(LocalDayEpochKey)) {
            return null
        }
        return LocalDaySnapshot(
            epochDay = sharedPreferences.getLong(LocalDayEpochKey, 0L),
            zoneId = sharedPreferences.getString(LocalDayZoneKey, zoneIdProvider.zoneId().id).orEmpty(),
        )
    }
}
