package com.vexel.offlinearcade.game.pulseorbit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PulseOrbitEngineTest {
    @Test
    fun startRunActivatesReadyState() {
        val ready = createPulseOrbitReadyState(sessionId = "session", seed = 7L)

        val active = startPulseOrbitRun(ready, startedAtMillis = 1_000L)

        assertTrue(active.playing)
        assertFalse(active.paused)
        assertEquals(1_000L, active.runStartMillis)
    }

    @Test
    fun cleanPassScoresOneAndResetsCombo() {
        val active = startPulseOrbitRun(createPulseOrbitReadyState("session", 2L), 1_000L).copy(
            orbitAngle = 24f,
            gapCenterAngle = 16f,
        )

        val result = resolvePulseOrbitTap(active, 2_000L)

        assertEquals(PulseOrbitTapResolution.CLEAN_PASS, result.resolution)
        assertEquals(1, result.state.score)
        assertEquals(1, result.state.passes)
        assertEquals(0, result.state.combo)
        assertEquals(0, result.state.perfectPasses)
    }

    @Test
    fun perfectPassScoresTwoAndBuildsCombo() {
        val active = startPulseOrbitRun(createPulseOrbitReadyState("session", 2L), 1_000L).copy(
            orbitAngle = 2f,
            gapCenterAngle = 0f,
        )

        val result = resolvePulseOrbitTap(active, 2_000L)

        assertEquals(PulseOrbitTapResolution.PERFECT_PASS, result.resolution)
        assertEquals(2, result.state.score)
        assertEquals(1, result.state.combo)
        assertEquals(1, result.state.bestCombo)
        assertEquals(1, result.state.perfectPasses)
    }

    @Test
    fun fifthPerfectAddsComboBonus() {
        val active = startPulseOrbitRun(createPulseOrbitReadyState("session", 2L), 1_000L).copy(
            orbitAngle = 1f,
            gapCenterAngle = 0f,
            combo = 4,
            bestCombo = 4,
            passes = 4,
            score = 8,
            perfectPasses = 4,
        )

        val result = resolvePulseOrbitTap(active, 2_000L)

        assertEquals(11, result.state.score)
        assertEquals(5, result.state.combo)
        assertEquals(5, result.state.bestCombo)
    }

    @Test
    fun failedTapEndsRun() {
        val active = startPulseOrbitRun(createPulseOrbitReadyState("session", 3L), 1_000L).copy(
            orbitAngle = 180f,
            gapCenterAngle = 0f,
        )

        val result = resolvePulseOrbitTap(active, 2_000L)

        assertEquals(PulseOrbitTapResolution.FAIL, result.resolution)
        assertTrue(result.state.gameOver)
        assertFalse(result.state.playing)
        assertEquals(0, result.state.combo)
    }

    @Test
    fun rapidTapCooldownPreventsDuplicateResolution() {
        val active = startPulseOrbitRun(createPulseOrbitReadyState("session", 2L), 1_000L).copy(
            orbitAngle = 0f,
            gapCenterAngle = 0f,
        )

        val first = resolvePulseOrbitTap(active, 2_000L)
        val second = resolvePulseOrbitTap(first.state, 2_020L)

        assertEquals(PulseOrbitTapResolution.NONE, second.resolution)
        assertEquals(first.state.score, second.state.score)
        assertEquals(first.state.passes, second.state.passes)
    }

    @Test
    fun advanceOrbitAppliesRotationSpeed() {
        val active = startPulseOrbitRun(createPulseOrbitReadyState("session", 2L), 1_000L)

        val advanced = advancePulseOrbitState(active, 1f)

        assertEquals(355f, advanced.orbitAngle, 0.001f)
    }

    @Test
    fun gapDirectionIsDeterministicFromSeed() {
        val a = gapDirectionFor(seed = 5L, passIndex = 1)
        val b = gapDirectionFor(seed = 5L, passIndex = 1)
        val c = gapDirectionFor(seed = 6L, passIndex = 1)

        assertEquals(a, b, 0.0f)
        assertTrue(a == 1f || a == -1f)
        assertTrue(c == 1f || c == -1f)
    }
}
