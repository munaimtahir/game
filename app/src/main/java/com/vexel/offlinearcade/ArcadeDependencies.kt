package com.vexel.arcadetrio

import android.content.Context
import androidx.room.Room
import com.vexel.offlinearcade.core.common.ArcadeDispatchers
import com.vexel.offlinearcade.core.common.SystemArcadeClock
import com.vexel.offlinearcade.core.data.ArcadeDatabase
import com.vexel.offlinearcade.core.data.OfflineArcadeRepository
import com.vexel.offlinearcade.core.data.SharedPreferencesSettingsStore

object ArcadeDependencies {
    @Volatile
    private var repository: OfflineArcadeRepository? = null

    fun repository(context: Context): OfflineArcadeRepository {
        return repository ?: synchronized(this) {
            repository ?: OfflineArcadeRepository(
                database = Room.databaseBuilder(
                    context.applicationContext,
                    ArcadeDatabase::class.java,
                    "offline-arcade.db",
                ).fallbackToDestructiveMigration().build(),
                preferences = SharedPreferencesSettingsStore(
                    context.applicationContext.getSharedPreferences(
                        "offline_arcade_preferences",
                        Context.MODE_PRIVATE,
                    ),
                ),
                clock = SystemArcadeClock,
                dispatchers = ArcadeDispatchers(),
            ).also { repository = it }
        }
    }
}
