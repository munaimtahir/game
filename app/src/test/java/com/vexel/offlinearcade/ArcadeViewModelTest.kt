package com.vexel.arcadetrio

import com.vexel.offlinearcade.core.data.ArcadeRepository
import com.vexel.offlinearcade.core.model.ArcadeSnapshot
import com.vexel.offlinearcade.core.model.DailyChallenge
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.core.model.SettingsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArcadeViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeArcadeRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = FakeArcadeRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun togglesPersistThroughRepository() = runTest(dispatcher) {
        val viewModel = ArcadeViewModel(repository)

        viewModel.toggleSound(false)
        viewModel.toggleMusic(false)
        viewModel.toggleVibration(false)
        advanceUntilIdle()

        val settings = repository.settings
        assertTrue(!settings.soundEnabled)
        assertTrue(!settings.musicEnabled)
        assertTrue(!settings.vibrationEnabled)
    }

    @Test
    fun recordRunAndThemeActionsAreForwarded() = runTest(dispatcher) {
        val viewModel = ArcadeViewModel(repository)
        val runResult = RunResult(gameId = GameId.PULSE_ORBIT, score = 14, durationMillis = 12000, bestCombo = 4, coinsEarned = 8)

        viewModel.recordRun(runResult)
        viewModel.markTutorialSeen(GameId.PULSE_ORBIT)
        viewModel.unlockTheme("sunset_shift")
        viewModel.selectTheme("sunset_shift")
        viewModel.setPremiumUnlocked(true)
        advanceUntilIdle()

        assertEquals(runResult, repository.recordedRuns.single())
        assertEquals(GameId.PULSE_ORBIT, repository.tutorialsSeen.single())
        assertEquals("sunset_shift", repository.purchasedThemes.single())
        assertEquals("sunset_shift", repository.selectedThemes.single())
        assertTrue(repository.premiumUnlocked)
    }

    @Test
    fun snapshotFlowUpdatesAreObserved() = runTest(dispatcher) {
        val viewModel = ArcadeViewModel(repository)
        val updatedSnapshot = ArcadeSnapshot(settings = SettingsState(soundEnabled = false, musicEnabled = true, vibrationEnabled = false))

        repository.snapshotFlow.value = updatedSnapshot
        advanceUntilIdle()

        assertEquals(false, viewModel.snapshot.value.settings.soundEnabled)
        assertEquals(false, viewModel.snapshot.value.settings.vibrationEnabled)
    }

    private class FakeArcadeRepository : ArcadeRepository {
        val snapshotFlow = MutableStateFlow(ArcadeSnapshot())
        var settings = SettingsState()
        val recordedRuns = mutableListOf<RunResult>()
        val purchasedThemes = mutableListOf<String>()
        val selectedThemes = mutableListOf<String>()
        val tutorialsSeen = mutableListOf<GameId>()
        var premiumUnlocked = false

        override val snapshot: Flow<ArcadeSnapshot> = snapshotFlow

        override fun challengesForDay(epochDay: Long): Flow<List<DailyChallenge>> = flowOf(emptyList())

        override suspend fun updateSettings(transform: (SettingsState) -> SettingsState) {
            settings = transform(settings)
            snapshotFlow.value = snapshotFlow.value.copy(settings = settings)
        }

        override suspend fun recordRun(result: RunResult) {
            recordedRuns += result
        }

        override suspend fun markTutorialSeen(gameId: GameId) {
            tutorialsSeen += gameId
        }

        override suspend fun purchaseTheme(themeId: String): Boolean {
            purchasedThemes += themeId
            return true
        }

        override suspend fun selectTheme(themeId: String) {
            selectedThemes += themeId
        }

        override suspend fun purchaseSkin(skinId: String): Boolean {
            return true
        }

        override suspend fun selectSkin(skinId: String, gameId: GameId) {
        }

        override suspend fun setPremiumUnlocked(unlocked: Boolean) {
            premiumUnlocked = unlocked
        }
    }
}
