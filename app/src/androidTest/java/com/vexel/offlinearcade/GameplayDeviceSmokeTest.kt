package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.click
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameplayDeviceSmokeTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun pulseOrbitStartsFromButtonOnDevice() {
        rule.openHomeRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitScreen)
        rule.waitUntilExists(ArcadeTestTags.PulseOrbitStartButton)
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitStartButton).performClick()
    }

    @Test
    fun laneDriftStartsAndSpawnsTraffic() {
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftScreen)
        rule.waitUntilExists(ArcadeTestTags.LaneDriftHint)
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftBoard).performTouchInput { click() }
        rule.waitUntil(timeoutMillis = 6_000) {
            val traffic = rule.onNodeWithTag(ArcadeTestTags.LaneDriftTrafficStatus).readText()
            val blockerCount = """Traffic:\s+(\d+)\s+blockers""".toRegex()
                .find(traffic)
                ?.groupValues
                ?.getOrNull(1)
                ?.toIntOrNull()
                ?: 0
            blockerCount > 0
        }
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftBoard).performTouchInput { swipeLeft() }
    }

    @Test
    fun stackDropGestureControlsWork() {
        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropScreen)
        rule.waitUntilExists(ArcadeTestTags.StackDropHint)
        rule.onNodeWithTag(ArcadeTestTags.StackDropHint).performScrollTo().assertIsDisplayed()
        rule.onNodeWithTag(ArcadeTestTags.StackDropStartButton).performScrollTo().assertIsDisplayed().performClick()
        val startState = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard).readStateDescription()

        rule.onNodeWithTag(ArcadeTestTags.StackDropBoard).performTouchInput { click() }
        rule.waitUntil(timeoutMillis = 2_000) {
            val state = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard).readStateDescription()
            state != startState
        }
        val afterTap = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard).readStateDescription()
        assertNotEquals(startState, afterTap)

        rule.onNodeWithTag(ArcadeTestTags.StackDropBoard).performTouchInput { swipeLeft() }
        rule.waitUntil(timeoutMillis = 2_000) {
            val state = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard).readStateDescription()
            state != afterTap
        }
        val afterLeft = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard).readStateDescription()
        assertNotEquals(afterTap, afterLeft)

        rule.onNodeWithTag(ArcadeTestTags.StackDropBoard).performTouchInput { swipeRight() }
        rule.waitUntil(timeoutMillis = 2_000) {
            val state = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard).readStateDescription()
            state != afterLeft
        }

        val beforeDrop = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard).readStateDescription()
        rule.onNodeWithTag(ArcadeTestTags.StackDropBoard).performTouchInput { swipeDown() }
        rule.waitUntil(timeoutMillis = 2_000) {
            val state = rule.onNodeWithTag(ArcadeTestTags.StackDropBoard).readStateDescription()
            state != beforeDrop
        }
    }

    @Test
    fun gameplayHintsCanBeDismissed() {
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftScreen)
        rule.waitUntilExists(ArcadeTestTags.LaneDriftHint)
        rule.onNode(hasText("Got it")).performClick()
        rule.onNodeWithTag(ArcadeTestTags.BackButton).performClick()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Arcade Library")).fetchSemanticsNode() }.isSuccess
        }
        rule.activityRule.scenario.recreate()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Arcade Library")).fetchSemanticsNode() }.isSuccess
        }

        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropScreen)
        rule.waitUntilExists(ArcadeTestTags.StackDropHint)
        rule.onNodeWithTag(ArcadeTestTags.StackDropHint).performScrollTo().assertIsDisplayed()
    }
}
