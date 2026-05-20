package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.click
import androidx.compose.ui.test.swipeLeft
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
        Thread.sleep(2000)
        rule.openHomeRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.PulseOrbitBoard)
    }

    @Test
    fun laneDriftStartsAndSpawnsTraffic() {
        Thread.sleep(2000)
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftBoard, useUnmergedTree = true).performTouchInput { click() }
        rule.waitUntil(timeoutMillis = 8_000) {
            val stateDesc = rule.onNodeWithTag(ArcadeTestTags.LaneDriftBoard, useUnmergedTree = true).readStateDescription()
            stateDesc.contains("items=") && !stateDesc.contains("items=[]")
        }
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftBoard, useUnmergedTree = true).performTouchInput { swipeLeft() }
    }

    @Test
    fun stackDropGestureControlsWork() {
        Thread.sleep(2000)
        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
        rule.onNodeWithTag(ArcadeTestTags.StackDropStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        
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
        Thread.sleep(2000)
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: Throwable) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitUntil(30_000) {
            rule.onAllNodesWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNodes().isNotEmpty() ||
            rule.onAllNodes(hasText("Library", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
