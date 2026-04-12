package com.vexel.offlinearcade.core.ui

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ArcadeGesturesTest {
    private val thresholds = ArcadeGestureThresholdsPx(
        tapSlopPx = 10f,
        swipeMinDistancePx = 42f,
        dominantAxisRatio = 1.35f,
        softDropMinDistancePx = 56f,
        hardDropMinDistancePx = 132f,
        hardDropMaxDurationMs = 160L,
    )

    @Test
    fun tapWithinSlopStaysTap() {
        assertEquals(
            ArcadeGestureAction.Tap,
            classifyArcadeGesture(Offset(6f, 8f), durationMillis = 80L, thresholdsPx = thresholds),
        )
    }

    @Test
    fun horizontalSwipeMapsToSingleDirection() {
        assertEquals(
            ArcadeGestureAction.SwipeLeft,
            classifyArcadeGesture(Offset(-72f, 12f), durationMillis = 120L, thresholdsPx = thresholds),
        )
        assertEquals(
            ArcadeGestureAction.SwipeRight,
            classifyArcadeGesture(Offset(72f, 10f), durationMillis = 120L, thresholdsPx = thresholds),
        )
    }

    @Test
    fun downwardGesturesSeparateSoftAndHardDrop() {
        assertEquals(
            ArcadeGestureAction.SwipeDown,
            classifyArcadeGesture(Offset(8f, 88f), durationMillis = 240L, thresholdsPx = thresholds),
        )
        assertEquals(
            ArcadeGestureAction.FlickDown,
            classifyArcadeGesture(Offset(4f, 164f), durationMillis = 110L, thresholdsPx = thresholds),
        )
    }

    @Test
    fun noisyDiagonalDragIsIgnored() {
        assertNull(
            classifyArcadeGesture(Offset(30f, 36f), durationMillis = 150L, thresholdsPx = thresholds),
        )
    }
}
