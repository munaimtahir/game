package com.vexel.offlinearcade.game.lanedrift

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LaneDriftCollisionTest {

    @Test
    fun testHitRectInset() {
        val rect = HitRect(0f, 0f, 100f, 100f)
        val inset = rect.insetFraction(0.1f, 0.2f)
        assertEquals(10f, inset.left, 0.001f)
        assertEquals(90f, inset.right, 0.001f)
        assertEquals(20f, inset.top, 0.001f)
        assertEquals(80f, inset.bottom, 0.001f)
        assertEquals(80f, inset.width, 0.001f)
        assertEquals(60f, inset.height, 0.001f)
    }

    @Test
    fun testOverlapExceeds() {
        val a = HitRect(0f, 0f, 50f, 50f)
        val b = HitRect(40f, 40f, 90f, 90f)
        val o = overlap(a, b)
        // overlapX = min(50,90) - max(0,40) = 50 - 40 = 10
        // overlapY = min(50,90) - max(0,40) = 50 - 40 = 10
        assertEquals(10f, o.overlapX, 0.001f)
        assertEquals(10f, o.overlapY, 0.001f)
        
        assertTrue(o.exceeds(5f))
        assertFalse(o.exceeds(15f))
    }

    @Test
    fun testNoCollisionWhenVisualGapExists() {
        // Player is at lane 1
        val config = LaneDriftCollisionConfig(
            playerInsetXFraction = 0.2f, playerInsetYFraction = 0.2f,
            blockerInsetXFraction = 0.2f, blockerInsetYFraction = 0.2f,
            pickupInsetXFraction = 0.1f, pickupInsetYFraction = 0.1f,
            blockerMinOverlapPx = 2f, pickupMinOverlapPx = 2f
        )
        val sizes = LaneDriftSizesPx(100f, 100f, 50f)
        
        // Item in same lane, but Y is above the player
        val item = DriftItem(type = DriftItemType.BLOCKER, lane = 1, y = 0.5f)
        val items = listOf(item)
        
        val result = resolveLaneDriftCollision(
            playerLane = 1, items = items, 
            boardWidthPx = 1080f, boardHeightPx = 1920f, 
            config = config, sizes = sizes
        )
        // Player Y zone is usually ~0.88 of 1920 = 1689. Item Y is 0.5 of 1920 = 960. No overlap.
        assertEquals(LaneDriftCollisionType.NONE, result.type)
    }

    @Test
    fun testForgivingCollision_CloseButNoHit() {
        // Item is at the edge of the player. Visuals overlap, but hitboxes don't (due to insets).
        val config = LaneDriftCollisionConfig(
            playerInsetXFraction = 0.22f, playerInsetYFraction = 0.18f,
            blockerInsetXFraction = 0.20f, blockerInsetYFraction = 0.18f,
            pickupInsetXFraction = 0.15f, pickupInsetYFraction = 0.15f,
            blockerMinOverlapPx = 5f, pickupMinOverlapPx = 5f
        )
        val sizes = LaneDriftSizesPx(100f, 100f, 50f)
        // Player Top = 1920 * 0.88 = 1689.6
        // Let's place the item slightly above, so it barely touches the visual rect.
        // Item Top = 1920 * 0.83 = 1593.6. Bottom = 1693.6
        // Visual overlap is 1693.6 - 1689.6 = 4px.
        // With insets (18%), the hitboxes won't overlap at all.
        val item = DriftItem(type = DriftItemType.BLOCKER, lane = 1, y = 0.83f)
        val items = listOf(item)
        
        val result = resolveLaneDriftCollision(
            playerLane = 1, items = items, 
            boardWidthPx = 1080f, boardHeightPx = 1920f, 
            config = config, sizes = sizes
        )
        assertEquals(LaneDriftCollisionType.NONE, result.type)
    }

    @Test
    fun testDeepCollision_TriggersBlocker() {
        val config = LaneDriftCollisionConfig(
            playerInsetXFraction = 0.22f, playerInsetYFraction = 0.18f,
            blockerInsetXFraction = 0.20f, blockerInsetYFraction = 0.18f,
            pickupInsetXFraction = 0.15f, pickupInsetYFraction = 0.15f,
            blockerMinOverlapPx = 5f, pickupMinOverlapPx = 5f
        )
        val sizes = LaneDriftSizesPx(100f, 100f, 50f)
        // Player Y = 1689.6
        // Item at Y = 0.87 -> Top = 1670.4. Bottom = 1770.4. Deep overlap.
        val item = DriftItem(type = DriftItemType.BLOCKER, lane = 1, y = 0.87f)
        val items = listOf(item)
        
        val result = resolveLaneDriftCollision(
            playerLane = 1, items = items, 
            boardWidthPx = 1080f, boardHeightPx = 1920f, 
            config = config, sizes = sizes
        )
        assertEquals(LaneDriftCollisionType.BLOCKER, result.type)
        assertEquals(item, result.hitItem)
    }
}
