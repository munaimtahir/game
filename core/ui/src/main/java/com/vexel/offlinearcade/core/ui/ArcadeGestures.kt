package com.vexel.offlinearcade.core.ui

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

enum class ArcadeGestureAction {
    Tap,
    SwipeLeft,
    SwipeRight,
    SwipeDown,
    FlickDown,
}

data class ArcadeGestureThresholds(
    val tapSlopDp: Dp = 10.dp,
    val swipeMinDistanceDp: Dp = 42.dp,
    val dominantAxisRatio: Float = 1.35f,
    val softDropMinDistanceDp: Dp = 56.dp,
    val hardDropMinDistanceDp: Dp = 132.dp,
    val hardDropMaxDurationMs: Long = 160L,
)

internal fun classifyArcadeGesture(
    totalOffset: Offset,
    durationMillis: Long,
    thresholdsPx: ArcadeGestureThresholdsPx,
): ArcadeGestureAction? {
    val absX = abs(totalOffset.x)
    val absY = abs(totalOffset.y)
    if (absX <= thresholdsPx.tapSlopPx && absY <= thresholdsPx.tapSlopPx) {
        return ArcadeGestureAction.Tap
    }
    if (absX >= thresholdsPx.swipeMinDistancePx && absX > absY * thresholdsPx.dominantAxisRatio) {
        return if (totalOffset.x > 0f) ArcadeGestureAction.SwipeRight else ArcadeGestureAction.SwipeLeft
    }
    if (totalOffset.y > 0f && absY > absX * thresholdsPx.dominantAxisRatio) {
        if (absY >= thresholdsPx.hardDropMinDistancePx && durationMillis <= thresholdsPx.hardDropMaxDurationMs) {
            return ArcadeGestureAction.FlickDown
        }
        if (absY >= thresholdsPx.softDropMinDistancePx) {
            return ArcadeGestureAction.SwipeDown
        }
    }
    return null
}

data class ArcadeGestureThresholdsPx(
    val tapSlopPx: Float,
    val swipeMinDistancePx: Float,
    val dominantAxisRatio: Float,
    val softDropMinDistancePx: Float,
    val hardDropMinDistancePx: Float,
    val hardDropMaxDurationMs: Long,
)

@Composable
fun rememberArcadeGestureThresholdsPx(
    thresholds: ArcadeGestureThresholds = ArcadeGestureThresholds(),
): ArcadeGestureThresholdsPx {
    val density = LocalDensity.current
    return ArcadeGestureThresholdsPx(
        tapSlopPx = with(density) { thresholds.tapSlopDp.toPx() },
        swipeMinDistancePx = with(density) { thresholds.swipeMinDistanceDp.toPx() },
        dominantAxisRatio = thresholds.dominantAxisRatio,
        softDropMinDistancePx = with(density) { thresholds.softDropMinDistanceDp.toPx() },
        hardDropMinDistancePx = with(density) { thresholds.hardDropMinDistanceDp.toPx() },
        hardDropMaxDurationMs = thresholds.hardDropMaxDurationMs,
    )
}

fun Modifier.arcadeGestureInput(
    thresholds: ArcadeGestureThresholdsPx,
    enabled: Boolean = true,
    onAction: (ArcadeGestureAction) -> Unit,
): Modifier = pointerInput(enabled, thresholds) {
    if (!enabled) return@pointerInput
    awaitEachGesture {
        val down = awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
        var totalOffset = Offset.Zero
        val startTime = down.uptimeMillis.toLong()
        var pointerId = down.id
        while (true) {
            val event = awaitPointerEvent()
            val change = event.changes.firstOrNull { it.id == pointerId } ?: event.changes.firstOrNull() ?: break
            pointerId = change.id
            totalOffset += change.position - change.previousPosition
            if (!change.pressed) {
                change.consume()
                val action = classifyArcadeGesture(
                    totalOffset = totalOffset,
                    durationMillis = change.uptimeMillis.toLong() - startTime,
                    thresholdsPx = thresholds,
                )
                if (action != null) {
                    onAction(action)
                }
                break
            }
        }
    }
}
