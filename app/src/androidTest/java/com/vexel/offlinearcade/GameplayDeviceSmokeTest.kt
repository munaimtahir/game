package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.click
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameplayDeviceSmokeTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun pulseOrbitStartsFromButtonOnDevice() {
        rule.openHomeRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.PulseOrbitBoard)
    }

    @Test
    fun laneDriftStartsAndSpawnsTraffic() {
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.LaneDriftHint)
        rule.onNode(hasText("Got it", substring = true)).performClick()
        
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftBoard, useUnmergedTree = true).performTouchInput { click() }
        rule.waitUntil(timeoutMillis = 6_000) {
            val traffic = rule.onNodeWithTag(ArcadeTestTags.LaneDriftTrafficStatus, useUnmergedTree = true).readText()
            traffic.contains("blockers", ignoreCase = true)
        }
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftBoard, useUnmergedTree = true).performTouchInput { swipeLeft() }
    }

    @Test
    fun stackDropGestureControlsWork() {
        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
        rule.onNodeWithTag(ArcadeTestTags.StackDropStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.StackDropHint)
        rule.onNode(hasText("Got it", substring = true)).performClick()
        
        val startState = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).readStateDescription()

        rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).performTouchInput { click() }
        rule.waitUntil(timeoutMillis = 2_000) {
            val state = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).readStateDescription()
            state != startState
        }
        val afterTap = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).readStateDescription()
        assertNotEquals(startState, afterTap)

        rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).performTouchInput { swipeLeft() }
        rule.waitUntil(timeoutMillis = 2_000) {
            val state = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).readStateDescription()
            state != afterTap
        }
        val afterLeft = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).readStateDescription()
        assertNotEquals(afterTap, afterLeft)
    }

    @Test
    fun gameplayHintsCanBeDismissed() {
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.LaneDriftHint)
        rule.onNode(hasText("Got it", substring = true)).performClick()
        rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Library", substring = true)).fetchSemanticsNode() }.isSuccess
        }
    }
}
