package com.vexel.offlinearcade.core.data

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ArcadeDatabaseMigrationTest {
    @Test
    fun migration4To5PreservesStatsAndAddsRunRecordsTable() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbFile = File.createTempFile("arcade-migration", ".db")
        try {
            createVersion4Database(context, dbFile)

            val roomDb = Room.databaseBuilder(context, ArcadeDatabase::class.java, dbFile.absolutePath)
                .addMigrations(MIGRATION_4_5)
                .allowMainThreadQueries()
                .build()

            val stats = roomDb.arcadeDao().getStats("PULSE_ORBIT")
            val pragma = roomDb.openHelper.readableDatabase.query("PRAGMA table_info(`run_records`)")
            var sawSessionId = false
            while (pragma.moveToNext()) {
                if (pragma.getString(1) == "sessionId") {
                    sawSessionId = true
                }
            }
            pragma.close()
            roomDb.close()

            requireNotNull(stats)
            assertEquals(77, stats.highScore)
            assertEquals(3, stats.sessionsPlayed)
            assertEquals(0, stats.completedRuns)
            assertTrue(sawSessionId)
        } finally {
            dbFile.delete()
        }
    }

    private fun createVersion4Database(
        context: Context,
        dbFile: File,
    ) {
        val helper = FrameworkSQLiteOpenHelperFactory().create(
            SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(dbFile.absolutePath)
                .callback(
                    object : SupportSQLiteOpenHelper.Callback(4) {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            db.execSQL(
                                """
                                CREATE TABLE IF NOT EXISTS `player_profile` (
                                    `profileId` INTEGER NOT NULL,
                                    `coins` INTEGER NOT NULL,
                                    `premiumUnlocked` INTEGER NOT NULL,
                                    `selectedThemeId` TEXT NOT NULL,
                                    `selectedPulseOrbitSkin` TEXT NOT NULL,
                                    `selectedLaneDriftSkin` TEXT NOT NULL,
                                    `selectedStackDropSkin` TEXT NOT NULL,
                                    `currentStreakDays` INTEGER NOT NULL,
                                    `bestStreakDays` INTEGER NOT NULL,
                                    `lastPlayedEpochDay` INTEGER,
                                    `completedDailyChallenges` INTEGER NOT NULL,
                                    `tutorialSeenPulseOrbit` INTEGER NOT NULL,
                                    `tutorialSeenLaneDrift` INTEGER NOT NULL,
                                    `tutorialSeenStackDrop` INTEGER NOT NULL,
                                    PRIMARY KEY(`profileId`)
                                )
                                """.trimIndent(),
                            )
                            db.execSQL(
                                """
                                CREATE TABLE IF NOT EXISTS `game_stats` (
                                    `gameId` TEXT NOT NULL,
                                    `highScore` INTEGER NOT NULL,
                                    `sessionsPlayed` INTEGER NOT NULL,
                                    `totalPlayMillis` INTEGER NOT NULL,
                                    `totalScore` INTEGER NOT NULL,
                                    `totalPickups` INTEGER NOT NULL,
                                    `totalLinesCleared` INTEGER NOT NULL,
                                    `bestCombo` INTEGER NOT NULL,
                                    `bestLines` INTEGER NOT NULL,
                                    PRIMARY KEY(`gameId`)
                                )
                                """.trimIndent(),
                            )
                            db.execSQL(
                                "CREATE TABLE IF NOT EXISTS `theme_unlocks` (`themeId` TEXT NOT NULL, `unlocked` INTEGER NOT NULL, PRIMARY KEY(`themeId`))",
                            )
                            db.execSQL(
                                "CREATE TABLE IF NOT EXISTS `skin_unlocks` (`skinId` TEXT NOT NULL, `unlocked` INTEGER NOT NULL, PRIMARY KEY(`skinId`))",
                            )
                            db.execSQL(
                                """
                                CREATE TABLE IF NOT EXISTS `challenge_progress` (
                                    `challengeId` TEXT NOT NULL,
                                    `epochDay` INTEGER NOT NULL,
                                    `progress` INTEGER NOT NULL,
                                    `completed` INTEGER NOT NULL,
                                    `rewardClaimed` INTEGER NOT NULL,
                                    PRIMARY KEY(`challengeId`)
                                )
                                """.trimIndent(),
                            )
                            db.execSQL(
                                """
                                INSERT INTO `game_stats` (
                                    `gameId`, `highScore`, `sessionsPlayed`, `totalPlayMillis`,
                                    `totalScore`, `totalPickups`, `totalLinesCleared`, `bestCombo`, `bestLines`
                                ) VALUES ('PULSE_ORBIT', 77, 3, 12000, 155, 0, 0, 5, 0)
                                """.trimIndent(),
                            )
                        }

                        override fun onUpgrade(
                            db: SupportSQLiteDatabase,
                            oldVersion: Int,
                            newVersion: Int,
                        ) = Unit
                    },
                )
                .build(),
        )
        helper.writableDatabase.close()
        helper.close()
    }
}
