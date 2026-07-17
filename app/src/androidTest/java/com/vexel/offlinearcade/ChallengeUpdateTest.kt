package com.vexel.arcadetrio

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.RunResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChallengeUpdateTest {
    @Test
    fun testChallengeUpdates() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val repository = ArcadeDependencies.repository(context)
        
        repository.recordRun(
            RunResult(
                gameId = GameId.LANE_DRIFT,
                score = 100,
                durationMillis = 10_000,
                pickupsCollected = 5,
                coinsEarned = 10,
            ),
        )
        
        val snapshot = repository.snapshot.first()
        val laneChallenge = snapshot.challenges.firstOrNull { c -> c.gameId == GameId.LANE_DRIFT }
        
        assertTrue((laneChallenge?.progress ?: 0) >= 5)
    }
}
