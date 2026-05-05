package com.vexel.offlinearcade

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.RunResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChallengeUpdateTest {
    @Test
    fun testChallengeUpdates() = runBlocking {
        val app = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as ArcadeApp
        val repository = app.container.repository
        
        repository.recordRun(RunResult(GameId.LANE_DRIFT, 100, 10000, 0, 5, 0, 10))
        
        val snapshot = repository.snapshot.first()
        val laneChallenge = snapshot.challenges.firstOrNull { it.gameId == GameId.LANE_DRIFT }
        
        assertEquals(5, laneChallenge?.progress)
    }
}
