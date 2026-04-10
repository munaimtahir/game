package com.vexel.offlinearcade.game.pulseorbit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PulseOrbitTuningTest {
    @Test
    fun gapSizeShrinksAndClamps() {
        val initial = PulseOrbitTuning.gapSizeFor(0)
        val mid = PulseOrbitTuning.gapSizeFor(18)
        val late = PulseOrbitTuning.gapSizeFor(999)

        assertTrue(mid < initial)
        assertEquals(PulseOrbitTuning.minimumGapSize, late, 0.001f)
    }

    @Test
    fun speedAndStepIncreaseAndClamp() {
        val speedInitial = PulseOrbitTuning.rotationSpeedFor(0)
        val speedLate = PulseOrbitTuning.rotationSpeedFor(999)
        val stepInitial = PulseOrbitTuning.gapStepFor(0)
        val stepLate = PulseOrbitTuning.gapStepFor(999)

        assertTrue(speedLate > speedInitial)
        assertEquals(PulseOrbitTuning.maxRotationSpeed, speedLate, 0.001f)
        assertTrue(stepLate > stepInitial)
        assertEquals(PulseOrbitTuning.maxGapStep, stepLate, 0.001f)
    }

    @Test
    fun angularDistanceHandlesWrapAround() {
        val aroundZero = angularDistance(355f, 5f)
        val symmetry = angularDistance(5f, 355f)

        assertEquals(10f, aroundZero, 0.001f)
        assertEquals(aroundZero, symmetry, 0.001f)
    }

    @Test
    fun normalizeAngleReturnsExpectedRange() {
        assertEquals(270f, (-450f).normalizeAngle(), 0.001f)
        assertEquals(0f, 720f.normalizeAngle(), 0.001f)
        assertTrue(1234.5f.normalizeAngle() in 0f..360f)
    }
}
