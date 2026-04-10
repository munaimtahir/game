package com.vexel.offlinearcade.game.lanedrift

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class LaneDriftLogicTest {
    @Test
    fun speedAndSpawnIntervalClampAtBounds() {
        val speedStart = LaneDriftTuning.speedFor(0f)
        val speedLate = LaneDriftTuning.speedFor(999f)
        val spawnStart = LaneDriftTuning.spawnIntervalFor(0f)
        val spawnLate = LaneDriftTuning.spawnIntervalFor(999f)

        assertEquals(LaneDriftTuning.initialSpeed, speedStart, 0.001f)
        assertEquals(LaneDriftTuning.maxSpeed, speedLate, 0.001f)
        assertEquals(LaneDriftTuning.initialSpawnInterval, spawnStart, 0.001f)
        assertEquals(LaneDriftTuning.minimumSpawnInterval, spawnLate, 0.001f)
    }

    @Test
    fun earlyPatternAvoidsImmediateSameBlockerLane() {
        repeat(64) {
            val lane = pickBlockerLane(random = Random(it), previousLane = 1, elapsedSeconds = 6f)
            assertTrue(lane != 1)
        }
    }

    @Test
    fun pickupLaneNeverMatchesBlockerLane() {
        val random = Random(0)
        repeat(128) {
            val blocker = it % 3
            val pickup = pickupLaneFor(blocker, random)
            assertTrue(pickup in 0..2)
            assertTrue(pickup != blocker)
        }
    }

    @Test
    fun pickupSpawnRulesRespectEarlyGuardrails() {
        repeat(16) {
            assertTrue(shouldSpawnPickup(spawnCount = it * 2, elapsedSeconds = 8f, random = Random(it)))
            assertTrue(!shouldSpawnPickup(spawnCount = it * 2 + 1, elapsedSeconds = 8f, random = Random(it)))
        }
    }
}
