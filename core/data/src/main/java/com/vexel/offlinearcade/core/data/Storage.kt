package com.vexel.offlinearcade.core.data

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.migration.Migration
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vexel.offlinearcade.core.model.SettingsState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Entity(tableName = "player_profile")
data class PlayerProfileEntity(
    @PrimaryKey val profileId: Int = 0,
    val coins: Int = 0,
    val premiumUnlocked: Boolean = false,
    val selectedThemeId: String = "default",
    val selectedPulseOrbitSkin: String = "po_default",
    val selectedLaneDriftSkin: String = "ld_default",
    val selectedStackDropSkin: String = "sd_default",
    val currentStreakDays: Int = 0,
    val bestStreakDays: Int = 0,
    val lastPlayedEpochDay: Long? = null,
    val completedDailyChallenges: Int = 0,
    val tutorialSeenPulseOrbit: Boolean = false,
    val tutorialSeenLaneDrift: Boolean = false,
    val tutorialSeenStackDrop: Boolean = false,
)

@Entity(tableName = "game_stats")
data class GameStatsEntity(
    @PrimaryKey val gameId: String,
    val highScore: Int = 0,
    val sessionsPlayed: Int = 0,
    val completedRuns: Int = 0,
    val lastPlayedAtEpochMillis: Long = 0,
    val totalPlayMillis: Long = 0,
    val totalScore: Int = 0,
    val totalPickups: Int = 0,
    val totalLinesCleared: Int = 0,
    val bestCombo: Int = 0,
    val bestLines: Int = 0,
    val totalPasses: Int = 0,
    val totalPerfectPasses: Int = 0,
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

@Entity(tableName = "run_records")
data class RunRecordEntity(
    @PrimaryKey val sessionId: String,
    val gameId: String,
    val score: Int,
    val coinsEarned: Int,
    val finishedAtEpochMillis: Long,
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

    @Query("SELECT * FROM run_records WHERE sessionId = :sessionId")
    suspend fun getRunRecord(sessionId: String): RunRecordEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRunRecord(runRecord: RunRecordEntity)
}

@Database(
    entities = [
        PlayerProfileEntity::class,
        GameStatsEntity::class,
        ThemeUnlockEntity::class,
        SkinUnlockEntity::class,
        ChallengeProgressEntity::class,
        RunRecordEntity::class,
    ],
    version = 5,
    exportSchema = true,
)
abstract class ArcadeDatabase : RoomDatabase() {
    abstract fun arcadeDao(): ArcadeDao
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            ALTER TABLE game_stats ADD COLUMN completedRuns INTEGER NOT NULL DEFAULT 0
            """.trimIndent(),
        )
        db.execSQL(
            """
            ALTER TABLE game_stats ADD COLUMN lastPlayedAtEpochMillis INTEGER NOT NULL DEFAULT 0
            """.trimIndent(),
        )
        db.execSQL(
            """
            ALTER TABLE game_stats ADD COLUMN totalPasses INTEGER NOT NULL DEFAULT 0
            """.trimIndent(),
        )
        db.execSQL(
            """
            ALTER TABLE game_stats ADD COLUMN totalPerfectPasses INTEGER NOT NULL DEFAULT 0
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS run_records (
                sessionId TEXT NOT NULL,
                gameId TEXT NOT NULL,
                score INTEGER NOT NULL,
                coinsEarned INTEGER NOT NULL,
                finishedAtEpochMillis INTEGER NOT NULL,
                PRIMARY KEY(sessionId)
            )
            """.trimIndent(),
        )
    }
}

object ArcadePreferenceKeys {
    const val soundEnabled = "sound_enabled"
    const val musicEnabled = "music_enabled"
    const val vibrationEnabled = "vibration_enabled"
    const val reducedEffects = "reduced_effects"
    const val highContrastEnabled = "high_contrast_enabled"
}

fun SharedPreferences.settingsFlow(): Flow<SettingsState> = callbackFlow {
    fun emitCurrent() {
        trySend(readSettings())
    }

    emitCurrent()
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        emitCurrent()
    }
    registerOnSharedPreferenceChangeListener(listener)
    awaitClose {
        unregisterOnSharedPreferenceChangeListener(listener)
    }
}.distinctUntilChanged()

suspend fun SharedPreferences.updateSettings(settings: SettingsState) {
    edit(commit = true) {
        putBoolean(ArcadePreferenceKeys.soundEnabled, settings.soundEnabled)
        putBoolean(ArcadePreferenceKeys.musicEnabled, settings.musicEnabled)
        putBoolean(ArcadePreferenceKeys.vibrationEnabled, settings.vibrationEnabled)
        putBoolean(ArcadePreferenceKeys.reducedEffects, settings.reducedEffects)
        putBoolean(ArcadePreferenceKeys.highContrastEnabled, settings.highContrastEnabled)
    }
}

fun SharedPreferences.readSettings(): SettingsState {
    return SettingsState(
        soundEnabled = getBoolean(ArcadePreferenceKeys.soundEnabled, true),
        musicEnabled = getBoolean(ArcadePreferenceKeys.musicEnabled, true),
        vibrationEnabled = getBoolean(ArcadePreferenceKeys.vibrationEnabled, true),
        reducedEffects = getBoolean(ArcadePreferenceKeys.reducedEffects, false),
        highContrastEnabled = getBoolean(ArcadePreferenceKeys.highContrastEnabled, false),
    )
}

interface SettingsStore {
    val settings: Flow<SettingsState>

    suspend fun updateSettings(transform: (SettingsState) -> SettingsState)
}

class SharedPreferencesSettingsStore(
    private val sharedPreferences: SharedPreferences,
) : SettingsStore {
    override val settings: Flow<SettingsState> = sharedPreferences.settingsFlow()

    override suspend fun updateSettings(transform: (SettingsState) -> SettingsState) {
        sharedPreferences.updateSettings(transform(sharedPreferences.readSettings()))
    }
}
