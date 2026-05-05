package com.vexel.offlinearcade.game.lanedrift

import kotlin.math.max
import kotlin.math.min

internal data class HitRect(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
) {
    val width: Float get() = right - left
    val height: Float get() = bottom - top
}

internal fun HitRect.insetFraction(horizontal: Float, vertical: Float): HitRect {
    val dx = width * horizontal
    val dy = height * vertical
    return HitRect(
        left = left + dx,
        top = top + dy,
        right = right - dx,
        bottom = bottom - dy,
    )
}

internal data class Overlap(
    val overlapX: Float,
    val overlapY: Float,
) {
    fun exceeds(minOverlapPx: Float): Boolean = overlapX > minOverlapPx && overlapY > minOverlapPx
}

internal fun overlap(a: HitRect, b: HitRect): Overlap {
    val ox = min(a.right, b.right) - max(a.left, b.left)
    val oy = min(a.bottom, b.bottom) - max(a.top, b.top)
    return Overlap(overlapX = ox, overlapY = oy)
}

internal enum class LaneDriftCollisionType { NONE, BLOCKER, PICKUP }

internal data class LaneDriftCollisionResult(
    val type: LaneDriftCollisionType,
    val hitItem: DriftItem? = null,
)

internal data class LaneDriftCollisionConfig(
    val playerInsetXFraction: Float,
    val playerInsetYFraction: Float,
    val blockerInsetXFraction: Float,
    val blockerInsetYFraction: Float,
    val pickupInsetXFraction: Float,
    val pickupInsetYFraction: Float,
    val blockerMinOverlapPx: Float,
    val pickupMinOverlapPx: Float,
)

internal fun resolveLaneDriftCollision(
    playerLane: Int,
    items: List<DriftItem>,
    boardWidthPx: Float,
    boardHeightPx: Float,
    config: LaneDriftCollisionConfig,
    sizes: LaneDriftSizesPx,
): LaneDriftCollisionResult {
    if (boardWidthPx <= 0f || boardHeightPx <= 0f) return LaneDriftCollisionResult(LaneDriftCollisionType.NONE)

    val laneWidth = boardWidthPx / 3f
    val playerVisual = playerVisualRectPx(
        laneWidth = laneWidth,
        boardHeightPx = boardHeightPx,
        lane = playerLane,
        playerHeightPx = sizes.playerHeightPx,
    )
    val playerHit = playerVisual.insetFraction(config.playerInsetXFraction, config.playerInsetYFraction)

    // Blockers have priority over pickups (keeps existing gameplay rule: no pickup on crash frame).
    val blocker = items
        .asSequence()
        .filter { it.type == DriftItemType.BLOCKER && it.lane == playerLane }
        .firstOrNull { candidate ->
            val obstacleVisual = itemVisualRectPx(
                laneWidth = laneWidth,
                boardHeightPx = boardHeightPx,
                item = candidate,
                sizes = sizes,
            )
            val obstacleHit = obstacleVisual.insetFraction(config.blockerInsetXFraction, config.blockerInsetYFraction)
            overlap(playerHit, obstacleHit).exceeds(config.blockerMinOverlapPx)
        }
    if (blocker != null) return LaneDriftCollisionResult(LaneDriftCollisionType.BLOCKER, blocker)

    val pickup = items
        .asSequence()
        .filter { it.type == DriftItemType.PICKUP && it.lane == playerLane }
        .firstOrNull { candidate ->
            val pickupVisual = itemVisualRectPx(
                laneWidth = laneWidth,
                boardHeightPx = boardHeightPx,
                item = candidate,
                sizes = sizes,
            )
            val pickupHit = pickupVisual.insetFraction(config.pickupInsetXFraction, config.pickupInsetYFraction)
            overlap(playerHit, pickupHit).exceeds(config.pickupMinOverlapPx)
        }
    return if (pickup != null) LaneDriftCollisionResult(LaneDriftCollisionType.PICKUP, pickup) else LaneDriftCollisionResult(LaneDriftCollisionType.NONE)
}

internal data class LaneDriftSizesPx(
    val playerHeightPx: Float,
    val blockerHeightPx: Float,
    val pickupHeightPx: Float,
)

internal fun playerVisualRectPx(
    laneWidth: Float,
    boardHeightPx: Float,
    lane: Int,
    playerHeightPx: Float,
): HitRect {
    val playerTop = boardHeightPx * LaneDriftTuning.playerZoneY
    val playerLeft = laneWidth * lane + laneWidth * 0.18f
    val playerWidth = laneWidth * 0.64f
    return HitRect(
        left = playerLeft,
        top = playerTop,
        right = playerLeft + playerWidth,
        bottom = playerTop + playerHeightPx,
    )
}

internal fun itemVisualRectPx(
    laneWidth: Float,
    boardHeightPx: Float,
    item: DriftItem,
    sizes: LaneDriftSizesPx,
): HitRect {
    val top = boardHeightPx * item.y
    val left = laneWidth * item.lane + laneWidth * 0.2f
    val width = laneWidth * if (item.type == DriftItemType.BLOCKER) 0.56f else 0.42f
    val height = if (item.type == DriftItemType.BLOCKER) sizes.blockerHeightPx else sizes.pickupHeightPx
    return HitRect(
        left = left,
        top = top,
        right = left + width,
        bottom = top + height,
    )
}
