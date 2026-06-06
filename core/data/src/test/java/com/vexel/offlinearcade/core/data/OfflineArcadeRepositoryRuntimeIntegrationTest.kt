package com.vexel.offlinearcade.core.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.vexel.offlinearcade.core.common.ArcadeClock
import com.vexel.offlinearcade.core.common.ArcadeDispatchers
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.core.model.SettingsState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class OfflineArcadeRepositoryRuntimeIntegrationTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var context: Context
    private lateinit var databaseFile: java.io.File
    private lateinit var settingsStore: InMemorySettingsStore

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        databaseFile = File.createTempFile("offline-arcade", ".db")
        settingsStore = InMemorySettingsStore()
    }

    @After
    fun tearDown() {
        databaseFile.delete()
    }

    @Test
    fun roomAndDataStorePersistAcrossRepositoryRecreation() = runTest(dispatcher) {
        val clock = MutableClock(30_000L)
        val firstRuntime = createRuntime(clock)
        firstRuntime.repository.updateSettings { it.copy(soundEnabled = false, musicEnabled = false, vibrationEnabled = true) }
        firstRuntime.repository.recordRun(
            RunResult(
                gameId = GameId.STACK_DROP,
                score = 420,
                durationMillis = 48_000,
                linesCleared = 16,
                coinsEarned = 280,
            ),
        )
        val purchased = firstRuntime.repository.purchaseTheme("sunset_shift")
        firstRuntime.repository.selectTheme("sunset_shift")
        firstRuntime.close()

        val secondRuntime = createRuntime(clock)
        val snapshot = secondRuntime.repository.snapshot.first()
        secondRuntime.close()

        assertTrue(purchased)
        assertTrue(!snapshot.settings.soundEnabled)
        assertTrue(!snapshot.settings.musicEnabled)
        assertEquals("sunset_shift", snapshot.profile.selectedThemeId)
        assertTrue(snapshot.themes.first { it.id == "sunset_shift" }.unlocked)
        assertTrue(snapshot.statsByGame.getValue(GameId.STACK_DROP).sessionsPlayed >= 1)
    }

    @Test
    fun challengeGenerationAndProgressLoadFromStorage() = runTest(dispatcher) {
        val clock = MutableClock(31_111L)
        val firstRuntime = createRuntime(clock)
        val generated = firstRuntime.repository.challengesForDay(clock.currentEpochDay()).first()
        firstRuntime.repository.recordRun(
            RunResult(
                gameId = GameId.PULSE_ORBIT,
                score = 36,
                durationMillis = 21_000,
                bestCombo = 8,
                coinsEarned = 24,
            ),
        )
        firstRuntime.close()

        val secondRuntime = createRuntime(clock)
        val loaded = secondRuntime.repository.challengesForDay(clock.currentEpochDay()).first()
        secondRuntime.close()

        assertEquals(generated.map { it.challengeId }, loaded.map { it.challengeId })
        assertTrue(loaded.first { it.gameId == GameId.PULSE_ORBIT }.progress > 0)
        assertTrue(loaded.first { it.gameId == null }.progress > 0)
    }

    @Test
    fun streakAndCoinsFollowRunDays() = runTest(dispatcher) {
        val clock = MutableClock(32_000L)
        val runtime = createRuntime(clock)

        runtime.repository.recordRun(
            RunResult(
                gameId = GameId.LANE_DRIFT,
                score = 90,
                durationMillis = 18_000,
                pickupsCollected = 9,
                coinsEarned = 30,
            ),
        )
        clock.day = 32_001L
        runtime.repository.recordRun(
            RunResult(
                gameId = GameId.PULSE_ORBIT,
                score = 50,
                durationMillis = 24_000,
                bestCombo = 11,
                coinsEarned = 40,
            ),
        )
        val afterTwoDays = runtime.repository.snapshot.first().profile
        clock.day = 32_003L
        runtime.repository.recordRun(
            RunResult(
                gameId = GameId.STACK_DROP,
                score = 170,
                durationMillis = 36_000,
                linesCleared = 7,
                coinsEarned = 42,
            ),
        )
        val afterGap = runtime.repository.snapshot.first().profile
        runtime.close()

        assertEquals(2, afterTwoDays.currentStreakDays)
        assertEquals(1, afterGap.currentStreakDays)
        assertTrue(afterGap.coins >= 112)
    }

    private fun createRuntime(clock: ArcadeClock): RuntimeHarness {
        val database = Room.databaseBuilder(context, ArcadeDatabase::class.java, databaseFile.absolutePath)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
        return RuntimeHarness(
            repository = OfflineArcadeRepository(
                database = database,
                preferences = settingsStore,
                clock = clock,
                dispatchers = ArcadeDispatchers(io = dispatcher, default = dispatcher),
            ),
            database = database,
        )
    }

    private data class RuntimeHarness(
        val repository: OfflineArcadeRepository,
        val database: ArcadeDatabase,
    ) {
        fun close() {
            database.close()
        }
    }

    private class MutableClock(
        var day: Long,
    ) : ArcadeClock {
        override fun currentEpochDay(): Long = day
    }

    private class InMemorySettingsStore : SettingsStore {
        private val state = MutableStateFlow(SettingsState())

        override val settings = state

        override suspend fun updateSettings(transform: (SettingsState) -> SettingsState) {
            state.value = transform(state.value)
        }
    }
}
