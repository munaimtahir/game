package com.vexel.arcadetrio

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.click
import androidx.compose.ui.test.swipeLeft
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import org.junit.Ignore
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@Ignore("Compose gameplay smoke is flaky on low-end physical hardware; route-based ADB smoke is the CI gate.")
class GameplayDeviceSmokeTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun pulseOrbitStartsFromButtonOnDevice() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        rule.openHomeRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.PulseOrbitBoard)
    }

    @Test
    fun laneDriftStartsAndSpawnsTraffic() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
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
    fun stackDropOnScreenControlsWork() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
        rule.onNodeWithTag(ArcadeTestTags.StackDropStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()

        val startState = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).readStateDescription()

        rule.onNodeWithTag(ArcadeTestTags.StackDropRotate, useUnmergedTree = true).performClick()
        rule.waitUntil(timeoutMillis = 2_000) {
            val state = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).readStateDescription()
            state != startState
        }
        val afterTap = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).readStateDescription()
        assertNotEquals(startState, afterTap)

        rule.onNodeWithTag(ArcadeTestTags.StackDropMoveLeft, useUnmergedTree = true).performClick()
        rule.waitUntil(timeoutMillis = 2_000) {
            val state = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).readStateDescription()
            state != afterTap
        }
        val afterLeft = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).readStateDescription()
        assertNotEquals(afterTap, afterLeft)
    }

    @Test
    fun laneDriftPauseButtonShowsOverlayAndResumes() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.LaneDriftBoard)

        rule.onNode(hasText("Pause", substring = true), useUnmergedTree = true).performClick()
        rule.waitUntilExists(ArcadeTestTags.LaneDriftScreen)
        rule.onNode(hasText("Run paused", substring = true), useUnmergedTree = true).assertIsDisplayed()

        rule.onNode(hasText("Resume", substring = true), useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.LaneDriftBoard)
    }
}
