package com.vexel.offlinearcade

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.RunResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.platform.app.InstrumentationRegistry

@RunWith(AndroidJUnit4::class)
class ChallengeUpdateTest {
    @Test
    fun testChallengeUpdates() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val repository = ArcadeDependencies.repository(context)
        
        repository.recordRun(RunResult(GameId.LANE_DRIFT, 100, 10000, 0, 5, 0, 10))
        
        val snapshot = repository.snapshot.first()
        val laneChallenge = snapshot.challenges.firstOrNull { c -> c.gameId == GameId.LANE_DRIFT }
        
        assertEquals(5, laneChallenge?.progress)
    }
}
