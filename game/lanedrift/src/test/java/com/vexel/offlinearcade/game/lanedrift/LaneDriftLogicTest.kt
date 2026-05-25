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

    @Test
    fun collision_noCrash_whenSmallVisibleGap() {
        val boardWidthPx = 360f
        val boardHeightPx = 600f
        val sizes = LaneDriftSizesPx(playerHeightPx = 84f, blockerHeightPx = 84f, pickupHeightPx = 50f)
        val config = LaneDriftCollisionConfig(
            playerInsetXFraction = LaneDriftTuning.playerHitboxInsetXFraction,
            playerInsetYFraction = LaneDriftTuning.playerHitboxInsetYFraction,
            blockerInsetXFraction = LaneDriftTuning.blockerHitboxInsetXFraction,
            blockerInsetYFraction = LaneDriftTuning.blockerHitboxInsetYFraction,
            pickupInsetXFraction = LaneDriftTuning.pickupHitboxInsetXFraction,
            pickupInsetYFraction = LaneDriftTuning.pickupHitboxInsetYFraction,
            blockerMinOverlapPx = 8f,
            pickupMinOverlapPx = 6f,
        )

        val laneWidth = boardWidthPx / 3f
        val playerHit = playerVisualRectPx(laneWidth, boardHeightPx, lane = 1, playerHeightPx = sizes.playerHeightPx)
            .insetFraction(config.playerInsetXFraction, config.playerInsetYFraction)

        val gapPx = 1.0f
        val blockerBottom = playerHit.top - gapPx
        val blockerTop = blockerBottom - sizes.blockerHeightPx
        val blockerY = blockerTop / boardHeightPx

        val items = listOf(DriftItem(lane = 1, y = blockerY, type = DriftItemType.BLOCKER, skin = 0))
        val result = resolveLaneDriftCollision(playerLane = 1, items = items, boardWidthPx = boardWidthPx, boardHeightPx = boardHeightPx, config = config, sizes = sizes)
        assertEquals(LaneDriftCollisionType.NONE, result.type)
    }

    @Test
    fun collision_noCrash_whenEdgesBarelyTouch() {
        val boardWidthPx = 360f
        val boardHeightPx = 600f
        val sizes = LaneDriftSizesPx(playerHeightPx = 84f, blockerHeightPx = 84f, pickupHeightPx = 50f)
        val config = LaneDriftCollisionConfig(
            playerInsetXFraction = LaneDriftTuning.playerHitboxInsetXFraction,
            playerInsetYFraction = LaneDriftTuning.playerHitboxInsetYFraction,
            blockerInsetXFraction = LaneDriftTuning.blockerHitboxInsetXFraction,
            blockerInsetYFraction = LaneDriftTuning.blockerHitboxInsetYFraction,
            pickupInsetXFraction = LaneDriftTuning.pickupHitboxInsetXFraction,
            pickupInsetYFraction = LaneDriftTuning.pickupHitboxInsetYFraction,
            blockerMinOverlapPx = 8f,
            pickupMinOverlapPx = 6f,
        )

        val laneWidth = boardWidthPx / 3f
        val playerHit = playerVisualRectPx(laneWidth, boardHeightPx, lane = 1, playerHeightPx = sizes.playerHeightPx)
            .insetFraction(config.playerInsetXFraction, config.playerInsetYFraction)

        val blockerBottom = playerHit.top
        val blockerTop = blockerBottom - sizes.blockerHeightPx
        val blockerY = blockerTop / boardHeightPx

        val items = listOf(DriftItem(lane = 1, y = blockerY, type = DriftItemType.BLOCKER, skin = 0))
        val result = resolveLaneDriftCollision(playerLane = 1, items = items, boardWidthPx = boardWidthPx, boardHeightPx = boardHeightPx, config = config, sizes = sizes)
        assertEquals(LaneDriftCollisionType.NONE, result.type)
    }

    @Test
    fun collision_crash_whenClearOverlap() {
        val boardWidthPx = 360f
        val boardHeightPx = 600f
        val sizes = LaneDriftSizesPx(playerHeightPx = 84f, blockerHeightPx = 84f, pickupHeightPx = 50f)
        val config = LaneDriftCollisionConfig(
            playerInsetXFraction = LaneDriftTuning.playerHitboxInsetXFraction,
            playerInsetYFraction = LaneDriftTuning.playerHitboxInsetYFraction,
            blockerInsetXFraction = LaneDriftTuning.blockerHitboxInsetXFraction,
            blockerInsetYFraction = LaneDriftTuning.blockerHitboxInsetYFraction,
            pickupInsetXFraction = LaneDriftTuning.pickupHitboxInsetXFraction,
            pickupInsetYFraction = LaneDriftTuning.pickupHitboxInsetYFraction,
            blockerMinOverlapPx = 8f,
            pickupMinOverlapPx = 6f,
        )

        val laneWidth = boardWidthPx / 3f
        val playerHit = playerVisualRectPx(laneWidth, boardHeightPx, lane = 1, playerHeightPx = sizes.playerHeightPx)
            .insetFraction(config.playerInsetXFraction, config.playerInsetYFraction)

        val desiredOverlapY = 40f
        val blockerTop = playerHit.bottom - desiredOverlapY
        val blockerY = blockerTop / boardHeightPx

        val items = listOf(DriftItem(lane = 1, y = blockerY, type = DriftItemType.BLOCKER, skin = 0))
        val result = resolveLaneDriftCollision(playerLane = 1, items = items, boardWidthPx = boardWidthPx, boardHeightPx = boardHeightPx, config = config, sizes = sizes)
        println("DEBUG playerHit: $playerHit")
        println("DEBUG itemVisual: ${itemVisualRectPx(laneWidth, boardHeightPx, items[0], sizes)}")
        println("DEBUG itemHit: ${itemVisualRectPx(laneWidth, boardHeightPx, items[0], sizes).insetFraction(config.blockerInsetXFraction, config.blockerInsetYFraction)}")
        println("DEBUG overlap: ${overlap(playerHit, itemVisualRectPx(laneWidth, boardHeightPx, items[0], sizes).insetFraction(config.blockerInsetXFraction, config.blockerInsetYFraction))}")
        assertEquals(LaneDriftCollisionType.BLOCKER, result.type)
    }

    @Test
    fun collision_requiresSameLane() {
        val boardWidthPx = 360f
        val boardHeightPx = 600f
        val sizes = LaneDriftSizesPx(playerHeightPx = 84f, blockerHeightPx = 84f, pickupHeightPx = 50f)
        val config = LaneDriftCollisionConfig(
            playerInsetXFraction = LaneDriftTuning.playerHitboxInsetXFraction,
            playerInsetYFraction = LaneDriftTuning.playerHitboxInsetYFraction,
            blockerInsetXFraction = LaneDriftTuning.blockerHitboxInsetXFraction,
            blockerInsetYFraction = LaneDriftTuning.blockerHitboxInsetYFraction,
            pickupInsetXFraction = LaneDriftTuning.pickupHitboxInsetXFraction,
            pickupInsetYFraction = LaneDriftTuning.pickupHitboxInsetYFraction,
            blockerMinOverlapPx = 8f,
            pickupMinOverlapPx = 6f,
        )

        val laneWidth = boardWidthPx / 3f
        val playerHit = playerVisualRectPx(laneWidth, boardHeightPx, lane = 1, playerHeightPx = sizes.playerHeightPx)
            .insetFraction(config.playerInsetXFraction, config.playerInsetYFraction)
        val blockerTop = playerHit.bottom - 24f
        val blockerY = blockerTop / boardHeightPx

        val items = listOf(DriftItem(lane = 2, y = blockerY, type = DriftItemType.BLOCKER, skin = 0))
        val result = resolveLaneDriftCollision(playerLane = 1, items = items, boardWidthPx = boardWidthPx, boardHeightPx = boardHeightPx, config = config, sizes = sizes)
        assertEquals(LaneDriftCollisionType.NONE, result.type)
    }

    @Test
    fun collision_pickupSeparateFromBlockerCollision() {
        val boardWidthPx = 360f
        val boardHeightPx = 600f
        val sizes = LaneDriftSizesPx(playerHeightPx = 84f, blockerHeightPx = 84f, pickupHeightPx = 50f)
        val config = LaneDriftCollisionConfig(
            playerInsetXFraction = LaneDriftTuning.playerHitboxInsetXFraction,
            playerInsetYFraction = LaneDriftTuning.playerHitboxInsetYFraction,
            blockerInsetXFraction = LaneDriftTuning.blockerHitboxInsetXFraction,
            blockerInsetYFraction = LaneDriftTuning.blockerHitboxInsetYFraction,
            pickupInsetXFraction = LaneDriftTuning.pickupHitboxInsetXFraction,
            pickupInsetYFraction = LaneDriftTuning.pickupHitboxInsetYFraction,
            blockerMinOverlapPx = 8f,
            pickupMinOverlapPx = 6f,
        )

        val laneWidth = boardWidthPx / 3f
        val playerHit = playerVisualRectPx(laneWidth, boardHeightPx, lane = 1, playerHeightPx = sizes.playerHeightPx)
            .insetFraction(config.playerInsetXFraction, config.playerInsetYFraction)

        val blockerTop = playerHit.bottom - 45f
        val pickupTop = playerHit.bottom - 40f
        val items = listOf(
            DriftItem(lane = 1, y = blockerTop / boardHeightPx, type = DriftItemType.BLOCKER, skin = 0),
            DriftItem(lane = 1, y = pickupTop / boardHeightPx, type = DriftItemType.PICKUP, skin = 0),
        )
        val result = resolveLaneDriftCollision(playerLane = 1, items = items, boardWidthPx = boardWidthPx, boardHeightPx = boardHeightPx, config = config, sizes = sizes)
        assertEquals(LaneDriftCollisionType.BLOCKER, result.type)
    }

    @Test
    fun collision_stableAcrossDensities_whenDpConvertedToPx() {
        fun run(density: Float): LaneDriftCollisionType {
            val boardWidthPx = 360f * density
            val boardHeightPx = 600f * density
            val sizes = LaneDriftSizesPx(playerHeightPx = 84f * density, blockerHeightPx = 84f * density, pickupHeightPx = 50f * density)
            val config = LaneDriftCollisionConfig(
                playerInsetXFraction = LaneDriftTuning.playerHitboxInsetXFraction,
                playerInsetYFraction = LaneDriftTuning.playerHitboxInsetYFraction,
                blockerInsetXFraction = LaneDriftTuning.blockerHitboxInsetXFraction,
                blockerInsetYFraction = LaneDriftTuning.blockerHitboxInsetYFraction,
                pickupInsetXFraction = LaneDriftTuning.pickupHitboxInsetXFraction,
                pickupInsetYFraction = LaneDriftTuning.pickupHitboxInsetYFraction,
                blockerMinOverlapPx = 8f * density,
                pickupMinOverlapPx = 6f * density,
            )
            val laneWidth = boardWidthPx / 3f
            val playerHit = playerVisualRectPx(laneWidth, boardHeightPx, lane = 1, playerHeightPx = sizes.playerHeightPx)
                .insetFraction(config.playerInsetXFraction, config.playerInsetYFraction)
            val blockerTop = playerHit.bottom - 40f * density
            val items = listOf(DriftItem(lane = 1, y = blockerTop / boardHeightPx, type = DriftItemType.BLOCKER, skin = 0))
            return resolveLaneDriftCollision(1, items, boardWidthPx, boardHeightPx, config, sizes).type
        }

        assertEquals(LaneDriftCollisionType.BLOCKER, run(density = 1f))
        assertEquals(LaneDriftCollisionType.BLOCKER, run(density = 3f))
    }
}
