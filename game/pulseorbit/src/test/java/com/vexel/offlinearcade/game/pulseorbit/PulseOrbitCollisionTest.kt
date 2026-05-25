package com.vexel.offlinearcade.game.pulseorbit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PulseOrbitCollisionTest {

    @Test
    fun testAngularDistance() {
        assertEquals(10f, angularDistance(355f, 5f), 0.001f)
        assertEquals(10f, angularDistance(5f, 355f), 0.001f)
        assertEquals(90f, angularDistance(0f, 90f), 0.001f)
        assertEquals(180f, angularDistance(0f, 180f), 0.001f)
        assertEquals(170f, angularDistance(0f, 190f), 0.001f)
    }

    @Test
    fun testFairCollisionWithTolerance() {
        val gapSize = 40f
        val gapCenter = 100f
        val tolerance = 5f
        
        // Exact boundary (center of orb at edge of gap)
        val boundaryAngle = gapCenter + gapSize / 2f // 120
        assertTrue(angularDistance(boundaryAngle, gapCenter) <= (gapSize / 2f) + tolerance)
        
        // Slightly outside the visual gap but within tolerance
        val justOutside = boundaryAngle + 3f // 123
        assertTrue(angularDistance(justOutside, gapCenter) <= (gapSize / 2f) + tolerance)
        
        // Far outside tolerance
        val farOutside = boundaryAngle + 10f // 130
        assertFalse(angularDistance(farOutside, gapCenter) <= (gapSize / 2f) + tolerance)
    }

    @Test
    fun testNormalizeAngle() {
        assertEquals(350f, (-10f).normalizeAngle(), 0.001f)
        assertEquals(10f, 370f.normalizeAngle(), 0.001f)
        assertEquals(0f, 0f.normalizeAngle(), 0.001f)
        assertEquals(0f, 360f.normalizeAngle(), 0.001f)
    }
}
