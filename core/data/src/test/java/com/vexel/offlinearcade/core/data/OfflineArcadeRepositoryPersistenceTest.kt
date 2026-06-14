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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class OfflineArcadeRepositoryPersistenceTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var context: Context
    private lateinit var database: ArcadeDatabase
    private lateinit var settingsStore: InMemorySettingsStore
    private lateinit var repository: OfflineArcadeRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, ArcadeDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        settingsStore = InMemorySettingsStore()
        repository = OfflineArcadeRepository(
            database = database,
            preferences = settingsStore,
            clock = ArcadeClock { 20_000L },
            dispatchers = ArcadeDispatchers(io = dispatcher, default = dispatcher),
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun settingsPersistAcrossSnapshotReads() = runTest(dispatcher) {
        repository.updateSettings { it.copy(soundEnabled = false, vibrationEnabled = false) }

        val settings = repository.snapshot.first().settings
        assertFalse(settings.soundEnabled)
        assertFalse(settings.vibrationEnabled)
        assertTrue(settings.musicEnabled)
    }

    @Test
    fun recordRunUpdatesStatsCoinsStreakAndChallenges() = runTest(dispatcher) {
        repository.recordRun(
            RunResult(
                gameId = GameId.PULSE_ORBIT,
                score = 5,
                durationMillis = 12_000,
                bestCombo = 6,
                coinsEarned = 9,
            ),
        )

        val snapshot = repository.snapshot.first()
        val pulseStats = snapshot.statsByGame.getValue(GameId.PULSE_ORBIT)
        assertEquals(5, pulseStats.highScore)
        assertEquals(1, pulseStats.sessionsPlayed)
        assertEquals(9, snapshot.profile.coins)
        assertEquals(1, snapshot.profile.currentStreakDays)
        assertEquals(1, snapshot.profile.bestStreakDays)
        assertTrue(snapshot.challenges.any { it.challengeId == "pulse-20000" && it.progress > 0 })
    }

    @Test
    fun tutorialSeenFlagsPersistPerGame() = runTest(dispatcher) {
        repository.markTutorialSeen(GameId.PULSE_ORBIT)
        repository.markTutorialSeen(GameId.STACK_DROP)

        val profile = repository.snapshot.first().profile
        assertTrue(profile.tutorialSeenPulseOrbit)
        assertFalse(profile.tutorialSeenLaneDrift)
        assertTrue(profile.tutorialSeenStackDrop)
    }

    @Test
    fun recordRunTracksTotalsAndAchievements() = runTest(dispatcher) {
        repository.recordRun(
            RunResult(
                gameId = GameId.LANE_DRIFT,
                score = 520,
                durationMillis = 60_000,
                pickupsCollected = 25,
                coinsEarned = 10,
            ),
        )
        repository.recordRun(
            RunResult(
                gameId = GameId.STACK_DROP,
                score = 1_100,
                durationMillis = 90_000,
                linesCleared = 5,
                coinsEarned = 12,
            ),
        )

        val snapshot = repository.snapshot.first()
        val lane = snapshot.statsByGame.getValue(GameId.LANE_DRIFT)
        val stack = snapshot.statsByGame.getValue(GameId.STACK_DROP)
        assertEquals(25, lane.totalPickups)
        assertEquals(5, stack.totalLinesCleared)
        assertTrue(snapshot.achievements.first { it.achievementId == "lane_shards_25" }.unlocked)
        assertTrue(snapshot.achievements.first { it.achievementId == "stack_lines_5" }.unlocked)
    }

    @Test
    fun completingTwoGameChallengesCompletesBundle() = runTest(dispatcher) {
        repository.recordRun(
            RunResult(
                gameId = GameId.PULSE_ORBIT,
                score = 200,
                durationMillis = 120_000,
                bestCombo = 20,
                coinsEarned = 0,
            ),
        )
        repository.recordRun(
            RunResult(
                gameId = GameId.LANE_DRIFT,
                score = 2_000,
                durationMillis = 120_000,
                pickupsCollected = 100,
                coinsEarned = 0,
            ),
        )

        val snapshot = repository.snapshot.first()
        val bundle = snapshot.challenges.first { it.gameId == null }
        assertTrue(bundle.completed)
        assertEquals(2, bundle.progress)
        assertTrue(snapshot.profile.completedDailyChallenges >= 3)
    }

    @Test
    fun purchaseAndSelectThemePersist() = runTest(dispatcher) {
        repository.recordRun(
            RunResult(
                gameId = GameId.LANE_DRIFT,
                score = 120,
                durationMillis = 30_000,
                pickupsCollected = 20,
                coinsEarned = 200,
            ),
        )

        val purchased = repository.purchaseTheme("sunset_shift")
        repository.selectTheme("sunset_shift")

        val snapshot = repository.snapshot.first()
        assertTrue(purchased)
        assertEquals("sunset_shift", snapshot.profile.selectedThemeId)
        assertTrue(snapshot.themes.first { it.id == "sunset_shift" }.unlocked)
    }

    private class InMemorySettingsStore : SettingsStore {
        private val state = MutableStateFlow(SettingsState())

        override val settings = state

        override suspend fun updateSettings(transform: (SettingsState) -> SettingsState) {
            state.value = transform(state.value)
        }
    }
}
