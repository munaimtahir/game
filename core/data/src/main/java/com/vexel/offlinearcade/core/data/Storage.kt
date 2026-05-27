package com.vexel.offlinearcade.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import com.vexel.offlinearcade.core.model.SettingsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Entity(tableName = "player_profile")
data class PlayerProfileEntity(
    @PrimaryKey val profileId: Int = 0,
    val coins: Int = 0,
    val premiumUnlocked: Boolean = false,
    val selectedThemeId: String = "default",
    val selectedPulseOrbitSkin: String = "po_default",
    val currentStreakDays: Int = 0,
    val lastPlayedEpochDay: Long? = null,
)

@Entity(tableName = "game_stats")
data class GameStatsEntity(
    @PrimaryKey val gameId: String,
    val highScore: Int = 0,
    val sessionsPlayed: Int = 0,
    val totalPlayMillis: Long = 0,
    val totalScore: Int = 0,
    val bestCombo: Int = 0,
    val bestLines: Int = 0,
)

@Entity(tableName = "theme_unlocks")
data class ThemeUnlockEntity(
    @PrimaryKey val themeId: String,
    val unlocked: Boolean = false,
)

@Entity(tableName = "skin_unlocks")
data class SkinUnlockEntity(
    @PrimaryKey val skinId: String,
    val unlocked: Boolean = false,
)

@Entity(tableName = "challenge_progress")
data class ChallengeProgressEntity(
    @PrimaryKey val challengeId: String,
    val epochDay: Long,
    val progress: Int = 0,
    val completed: Boolean = false,
    val rewardClaimed: Boolean = false,
)

@Dao
interface ArcadeDao {
    @Query("SELECT * FROM player_profile WHERE profileId = 0")
    fun observeProfile(): Flow<PlayerProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: PlayerProfileEntity)

    @Query("SELECT * FROM game_stats")
    fun observeStats(): Flow<List<GameStatsEntity>>

    @Query("SELECT * FROM game_stats WHERE gameId = :gameId")
    suspend fun getStats(gameId: String): GameStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStats(stats: GameStatsEntity)

    @Query("SELECT * FROM theme_unlocks")
    fun observeThemeUnlocks(): Flow<List<ThemeUnlockEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertThemeUnlock(themeUnlock: ThemeUnlockEntity)

    @Query("SELECT * FROM skin_unlocks")
    fun observeSkinUnlocks(): Flow<List<SkinUnlockEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSkinUnlock(skinUnlock: SkinUnlockEntity)

    @Query("SELECT * FROM challenge_progress WHERE epochDay = :epochDay")
    fun observeChallengeProgress(epochDay: Long): Flow<List<ChallengeProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertChallengeProgress(progress: ChallengeProgressEntity)
}

@Database(
    entities = [
        PlayerProfileEntity::class,
        GameStatsEntity::class,
        ThemeUnlockEntity::class,
        SkinUnlockEntity::class,
        ChallengeProgressEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class ArcadeDatabase : RoomDatabase() {
    abstract fun arcadeDao(): ArcadeDao
}

object ArcadePreferenceKeys {
    val soundEnabled = booleanPreferencesKey("sound_enabled")
    val musicEnabled = booleanPreferencesKey("music_enabled")
    val vibrationEnabled = booleanPreferencesKey("vibration_enabled")
    val reducedEffects = booleanPreferencesKey("reduced_effects")
    val highContrastEnabled = booleanPreferencesKey("high_contrast_enabled")
}

fun DataStore<Preferences>.settingsFlow(): Flow<SettingsState> = data.map { preferences ->
    SettingsState(
        soundEnabled = preferences[ArcadePreferenceKeys.soundEnabled] ?: true,
        musicEnabled = preferences[ArcadePreferenceKeys.musicEnabled] ?: true,
        vibrationEnabled = preferences[ArcadePreferenceKeys.vibrationEnabled] ?: true,
        reducedEffects = preferences[ArcadePreferenceKeys.reducedEffects] ?: false,
        highContrastEnabled = preferences[ArcadePreferenceKeys.highContrastEnabled] ?: false,
    )
}

suspend fun DataStore<Preferences>.updateSettings(settings: SettingsState) {
    edit { preferences ->
        preferences[ArcadePreferenceKeys.soundEnabled] = settings.soundEnabled
        preferences[ArcadePreferenceKeys.musicEnabled] = settings.musicEnabled
        preferences[ArcadePreferenceKeys.vibrationEnabled] = settings.vibrationEnabled
        preferences[ArcadePreferenceKeys.reducedEffects] = settings.reducedEffects
        preferences[ArcadePreferenceKeys.highContrastEnabled] = settings.highContrastEnabled
    }
}
