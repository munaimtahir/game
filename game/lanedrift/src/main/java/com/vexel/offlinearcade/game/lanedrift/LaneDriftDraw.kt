package com.vexel.offlinearcade.game.lanedrift

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.vexel.offlinearcade.core.ui.ArcadeExtendedColors

internal fun DrawScope.drawPlayerCar(
    topLeft: Offset,
    width: Float,
    height: Float,
    colors: ArcadeExtendedColors,
) {
    // Hover-car silhouette: body + canopy + thruster glow. Kept simple for low-end devices.
    val bodyRadius = CornerRadius(height * 0.28f, height * 0.28f)
    drawRoundRect(
        color = colors.primaryCyan.copy(alpha = 0.12f),
        topLeft = Offset(topLeft.x - width * 0.03f, topLeft.y + height * 0.06f),
        size = Size(width * 1.06f, height * 0.86f),
        cornerRadius = CornerRadius(height * 0.32f, height * 0.32f),
    )
    drawRoundRect(
        color = colors.player,
        topLeft = topLeft,
        size = Size(width, height),
        cornerRadius = bodyRadius,
    )

    val canopyInsetX = width * 0.14f
    val canopyInsetY = height * 0.18f
    drawRoundRect(
        color = colors.playerAccent.copy(alpha = 0.95f),
        topLeft = Offset(topLeft.x + canopyInsetX, topLeft.y + canopyInsetY),
        size = Size(width - canopyInsetX * 2f, height * 0.40f),
        cornerRadius = CornerRadius(height * 0.20f, height * 0.20f),
    )

    // Side rails
    drawRoundRect(
        color = colors.primaryOnCyan.copy(alpha = 0.70f),
        topLeft = Offset(topLeft.x + width * 0.06f, topLeft.y + height * 0.62f),
        size = Size(width * 0.88f, height * 0.10f),
        cornerRadius = CornerRadius(height * 0.10f, height * 0.10f),
    )

    // Subtle outline for readability on light boards
    drawRoundRect(
        color = colors.textInverse.copy(alpha = 0.75f),
        topLeft = Offset(topLeft.x + width * 0.05f, topLeft.y + height * 0.05f),
        size = Size(width * 0.90f, height * 0.90f),
        cornerRadius = CornerRadius(height * 0.24f, height * 0.24f),
        style = Stroke(width = maxOf(2f, height * 0.035f)),
    )

    // Thruster glow (kept inside bounds so hitboxes can stay smaller elsewhere)
    val glowHeight = height * 0.16f
    drawRoundRect(
        color = colors.primaryCyan.copy(alpha = 0.22f),
        topLeft = Offset(topLeft.x + width * 0.18f, topLeft.y + height - glowHeight),
        size = Size(width * 0.64f, glowHeight),
        cornerRadius = CornerRadius(glowHeight, glowHeight),
    )
}

internal fun DrawScope.drawPickup(
    skin: DriftPickupSkin,
    topLeft: Offset,
    width: Float,
    height: Float,
    colors: ArcadeExtendedColors,
) {
    when (skin) {
        DriftPickupSkin.COIN -> drawCoinPickup(topLeft, width, height, colors)
        DriftPickupSkin.STAR -> drawStarPickup(topLeft, width, height, colors)
        DriftPickupSkin.ENERGY -> drawEnergyPickup(topLeft, width, height, colors)
        DriftPickupSkin.GEM -> drawGemPickup(topLeft, width, height, colors)
        DriftPickupSkin.FUEL -> drawFuelPickup(topLeft, width, height, colors)
    }
}

private fun DrawScope.drawCoinPickup(
    topLeft: Offset,
    width: Float,
    height: Float,
    colors: ArcadeExtendedColors,
) {
    val cx = topLeft.x + width / 2f
    val cy = topLeft.y + height / 2f
    val r = minOf(width, height) * 0.40f
    drawCircle(color = colors.collectible.copy(alpha = 0.18f), radius = r * 1.48f, center = Offset(cx, cy))
    drawCircle(color = colors.collectible, radius = r, center = Offset(cx, cy))
    drawCircle(color = colors.collectible.copy(alpha = 0.35f), radius = r * 0.78f, center = Offset(cx, cy))
    drawCircle(color = colors.textInverse.copy(alpha = 0.75f), radius = r, center = Offset(cx, cy), style = Stroke(width = maxOf(2f, r * 0.14f)))
}

private fun DrawScope.drawStarPickup(
    topLeft: Offset,
    width: Float,
    height: Float,
    colors: ArcadeExtendedColors,
) {
    val cx = topLeft.x + width / 2f
    val cy = topLeft.y + height / 2f
    val outer = minOf(width, height) * 0.42f
    val inner = outer * 0.46f
    val path = starPath(cx, cy, outer, inner)
    drawCircle(color = colors.collectible.copy(alpha = 0.14f), radius = outer * 1.45f, center = Offset(cx, cy))
    drawPath(path, color = colors.collectible)
    drawPath(path, color = colors.textInverse.copy(alpha = 0.70f), style = Stroke(width = maxOf(2f, outer * 0.10f)))
}

private fun DrawScope.drawEnergyPickup(
    topLeft: Offset,
    width: Float,
    height: Float,
    colors: ArcadeExtendedColors,
) {
    // Energy cell: capsule + bolt.
    val radius = CornerRadius(height * 0.45f, height * 0.45f)
    drawRoundRect(
        color = colors.pickupMint.copy(alpha = 0.16f),
        topLeft = Offset(topLeft.x + width * 0.10f, topLeft.y + height * 0.08f),
        size = Size(width * 0.80f, height * 0.84f),
        cornerRadius = CornerRadius(height * 0.48f, height * 0.48f),
    )
    drawRoundRect(
        color = colors.pickupMint.copy(alpha = 0.95f),
        topLeft = Offset(topLeft.x + width * 0.16f, topLeft.y + height * 0.18f),
        size = Size(width * 0.68f, height * 0.64f),
        cornerRadius = radius,
    )
    val bolt = Path().apply {
        moveTo(topLeft.x + width * 0.52f, topLeft.y + height * 0.18f)
        lineTo(topLeft.x + width * 0.40f, topLeft.y + height * 0.56f)
        lineTo(topLeft.x + width * 0.55f, topLeft.y + height * 0.56f)
        lineTo(topLeft.x + width * 0.44f, topLeft.y + height * 0.82f)
        lineTo(topLeft.x + width * 0.64f, topLeft.y + height * 0.44f)
        lineTo(topLeft.x + width * 0.50f, topLeft.y + height * 0.44f)
        close()
    }
    drawPath(bolt, color = colors.textInverse.copy(alpha = 0.75f))
}

private fun DrawScope.drawGemPickup(
    topLeft: Offset,
    width: Float,
    height: Float,
    colors: ArcadeExtendedColors,
) {
    // Simple diamond.
    val path = Path().apply {
        val cx = topLeft.x + width / 2f
        val top = topLeft.y + height * 0.12f
        val bottom = topLeft.y + height * 0.88f
        val left = topLeft.x + width * 0.20f
        val right = topLeft.x + width * 0.80f
        moveTo(cx, top)
        lineTo(right, topLeft.y + height * 0.40f)
        lineTo(cx, bottom)
        lineTo(left, topLeft.y + height * 0.40f)
        close()
    }
    drawCircle(color = colors.accentViolet.copy(alpha = 0.16f), radius = minOf(width, height) * 0.50f, center = Offset(topLeft.x + width / 2f, topLeft.y + height / 2f))
    drawPath(path, color = colors.accentViolet.copy(alpha = 0.95f))
    drawPath(path, color = colors.textInverse.copy(alpha = 0.70f), style = Stroke(width = maxOf(2f, height * 0.07f)))
}

private fun DrawScope.drawFuelPickup(
    topLeft: Offset,
    width: Float,
    height: Float,
    colors: ArcadeExtendedColors,
) {
    // Fuel token: ring + center dot.
    val cx = topLeft.x + width / 2f
    val cy = topLeft.y + height / 2f
    val r = minOf(width, height) * 0.40f
    drawCircle(color = colors.collectible.copy(alpha = 0.14f), radius = r * 1.55f, center = Offset(cx, cy))
    drawCircle(color = colors.collectible.copy(alpha = 0.28f), radius = r, center = Offset(cx, cy))
    drawCircle(color = colors.collectible, radius = r * 0.62f, center = Offset(cx, cy), style = Stroke(width = maxOf(2f, r * 0.22f)))
    drawCircle(color = colors.textInverse.copy(alpha = 0.75f), radius = r * 0.14f, center = Offset(cx, cy))
}

internal fun DrawScope.drawHazard(
    skin: DriftHazardSkin,
    topLeft: Offset,
    width: Float,
    height: Float,
    colors: ArcadeExtendedColors,
) {
    drawRoundRect(
        color = colors.dangerCoral.copy(alpha = 0.16f),
        topLeft = Offset(topLeft.x + width * 0.02f, topLeft.y + height * 0.10f),
        size = Size(width * 0.96f, height * 0.84f),
        cornerRadius = CornerRadius(height * 0.22f, height * 0.22f),
    )
    when (skin) {
        DriftHazardSkin.CONE -> drawConeHazard(topLeft, width, height, colors)
        DriftHazardSkin.BARRIER -> drawBarrierHazard(topLeft, width, height, colors)
        DriftHazardSkin.CRATE -> drawCrateHazard(topLeft, width, height, colors)
        DriftHazardSkin.BARREL -> drawBarrelHazard(topLeft, width, height, colors)
        DriftHazardSkin.POTHOLE -> drawPotholeHazard(topLeft, width, height, colors)
    }
}

private fun DrawScope.drawConeHazard(
    topLeft: Offset,
    width: Float,
    height: Float,
    colors: ArcadeExtendedColors,
) {
    val cone = Path().apply {
        moveTo(topLeft.x + width / 2f, topLeft.y + height * 0.10f)
        lineTo(topLeft.x + width * 0.80f, topLeft.y + height * 0.78f)
        lineTo(topLeft.x + width * 0.20f, topLeft.y + height * 0.78f)
        close()
    }
    drawPath(cone, color = colors.dangerCoral.copy(alpha = 0.95f))
    drawRect(
        color = colors.collectible.copy(alpha = 0.70f),
        topLeft = Offset(topLeft.x + width * 0.28f, topLeft.y + height * 0.44f),
        size = Size(width * 0.44f, height * 0.10f),
    )
    drawRoundRect(
        color = colors.textInverse.copy(alpha = 0.70f),
        topLeft = Offset(topLeft.x + width * 0.18f, topLeft.y + height * 0.76f),
        size = Size(width * 0.64f, height * 0.14f),
        cornerRadius = CornerRadius(height * 0.10f, height * 0.10f),
    )
}

private fun DrawScope.drawBarrierHazard(
    topLeft: Offset,
    width: Float,
    height: Float,
    colors: ArcadeExtendedColors,
) {
    val baseRadius = CornerRadius(height * 0.20f, height * 0.20f)
    drawRoundRect(
        color = colors.dangerCoral.copy(alpha = 0.92f),
        topLeft = Offset(topLeft.x + width * 0.06f, topLeft.y + height * 0.20f),
        size = Size(width * 0.88f, height * 0.64f),
        cornerRadius = baseRadius,
    )
    // Stripes
    val stripeColor = colors.collectible.copy(alpha = 0.60f)
    drawRect(color = stripeColor, topLeft = Offset(topLeft.x + width * 0.14f, topLeft.y + height * 0.34f), size = Size(width * 0.72f, height * 0.10f))
    drawRect(color = stripeColor, topLeft = Offset(topLeft.x + width * 0.14f, topLeft.y + height * 0.52f), size = Size(width * 0.72f, height * 0.10f))
    drawRoundRect(
        color = colors.textInverse.copy(alpha = 0.55f),
        topLeft = Offset(topLeft.x + width * 0.06f, topLeft.y + height * 0.20f),
        size = Size(width * 0.88f, height * 0.64f),
        cornerRadius = baseRadius,
        style = Stroke(width = maxOf(2f, height * 0.06f)),
    )
}

private fun DrawScope.drawCrateHazard(
    topLeft: Offset,
    width: Float,
    height: Float,
    colors: ArcadeExtendedColors,
) {
    val crateTopLeft = Offset(topLeft.x + width * 0.10f, topLeft.y + height * 0.18f)
    val crateSize = Size(width * 0.80f, height * 0.70f)
    drawRoundRect(
        color = colors.textSecondary.copy(alpha = 0.45f),
        topLeft = crateTopLeft,
        size = crateSize,
        cornerRadius = CornerRadius(height * 0.16f, height * 0.16f),
    )
    drawLine(colors.textInverse.copy(alpha = 0.55f), crateTopLeft, Offset(crateTopLeft.x + crateSize.width, crateTopLeft.y + crateSize.height), strokeWidth = maxOf(2f, height * 0.05f))
    drawLine(colors.textInverse.copy(alpha = 0.55f), Offset(crateTopLeft.x + crateSize.width, crateTopLeft.y), Offset(crateTopLeft.x, crateTopLeft.y + crateSize.height), strokeWidth = maxOf(2f, height * 0.05f))
    drawRoundRect(
        color = colors.dangerCoral.copy(alpha = 0.75f),
        topLeft = crateTopLeft,
        size = crateSize,
        cornerRadius = CornerRadius(height * 0.16f, height * 0.16f),
        style = Stroke(width = maxOf(2f, height * 0.07f)),
    )
}

private fun DrawScope.drawBarrelHazard(
    topLeft: Offset,
    width: Float,
    height: Float,
    colors: ArcadeExtendedColors,
) {
    val barrelTopLeft = Offset(topLeft.x + width * 0.18f, topLeft.y + height * 0.14f)
    val barrelSize = Size(width * 0.64f, height * 0.76f)
    drawRoundRect(
        color = colors.dangerCoral.copy(alpha = 0.92f),
        topLeft = barrelTopLeft,
        size = barrelSize,
        cornerRadius = CornerRadius(barrelSize.width * 0.40f, barrelSize.width * 0.40f),
    )
    drawRect(colors.textInverse.copy(alpha = 0.35f), topLeft = Offset(barrelTopLeft.x, barrelTopLeft.y + barrelSize.height * 0.28f), size = Size(barrelSize.width, barrelSize.height * 0.10f))
    drawRect(colors.textInverse.copy(alpha = 0.35f), topLeft = Offset(barrelTopLeft.x, barrelTopLeft.y + barrelSize.height * 0.60f), size = Size(barrelSize.width, barrelSize.height * 0.10f))
    drawRoundRect(
        color = colors.textInverse.copy(alpha = 0.55f),
        topLeft = barrelTopLeft,
        size = barrelSize,
        cornerRadius = CornerRadius(barrelSize.width * 0.40f, barrelSize.width * 0.40f),
        style = Stroke(width = maxOf(2f, height * 0.06f)),
    )
}

private fun DrawScope.drawPotholeHazard(
    topLeft: Offset,
    width: Float,
    height: Float,
    colors: ArcadeExtendedColors,
) {
    val holeRect = Rect(
        offset = Offset(topLeft.x + width * 0.12f, topLeft.y + height * 0.26f),
        size = Size(width * 0.76f, height * 0.48f),
    )
    drawOval(color = colors.textPrimary.copy(alpha = 0.35f), topLeft = holeRect.topLeft, size = holeRect.size)
    drawOval(color = colors.textInverse.copy(alpha = 0.22f), topLeft = Offset(holeRect.left + holeRect.width * 0.12f, holeRect.top + holeRect.height * 0.18f), size = Size(holeRect.width * 0.60f, holeRect.height * 0.52f))
    drawOval(
        color = colors.dangerCoral.copy(alpha = 0.55f),
        topLeft = holeRect.topLeft,
        size = holeRect.size,
        style = Stroke(width = maxOf(2f, height * 0.06f)),
    )
}

private fun starPath(cx: Float, cy: Float, outer: Float, inner: Float): Path {
    val path = Path()
    val points = 10
    for (i in 0 until points) {
        val angle = (Math.PI / 2.0) + i * (Math.PI * 2.0 / points)
        val r = if (i % 2 == 0) outer else inner
        val x = cx + (kotlin.math.cos(angle) * r).toFloat()
        val y = cy - (kotlin.math.sin(angle) * r).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}
