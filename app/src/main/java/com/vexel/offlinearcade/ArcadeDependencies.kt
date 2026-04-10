package com.vexel.offlinearcade

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.vexel.offlinearcade.core.common.ArcadeDispatchers
import com.vexel.offlinearcade.core.common.SystemArcadeClock
import com.vexel.offlinearcade.core.data.ArcadeDatabase
import com.vexel.offlinearcade.core.data.OfflineArcadeRepository

private val Context.arcadePreferences by preferencesDataStore(name = "offline_arcade_preferences")

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
                preferences = context.applicationContext.arcadePreferences,
                clock = SystemArcadeClock,
                dispatchers = ArcadeDispatchers(),
            ).also { repository = it }
        }
    }
}
