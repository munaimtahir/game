package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.click
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BackNavigationTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun pulseOrbitBackNavigationFlow() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        // Home -> Pulse Orbit
        rule.openHomeRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
        
        // On Ready Screen: Back returns to Home
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: AssertionError) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.waitUntil(30_000) {
            rule.onAllNodesWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNodes().isNotEmpty()
        }

        // Home -> Pulse Orbit again
        rule.openHomeRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
        
        // Start Game and verify back pauses first
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.PulseOrbitBoard)
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitBoard, useUnmergedTree = true).performTouchInput { click() }
        
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: AssertionError) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.onNode(hasText("Run paused", substring = true), useUnmergedTree = true).assertIsDisplayed()

        // While Paused: Back returns to the detail screen
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: AssertionError) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.PulseOrbitDetail)
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitDetail, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun laneDriftBackNavigationFlow() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        // Home -> Lane Drift
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        
        // On Ready Screen
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: AssertionError) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.waitUntil(30_000) {
            rule.onAllNodesWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNodes().isNotEmpty()
        }

        // Home -> Lane Drift
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        
        // Start Game
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.LaneDriftBoard)
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftBoard, useUnmergedTree = true).performTouchInput { click() }
        
        // During Gameplay: Back pauses
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: AssertionError) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.onNode(hasText("Run paused", substring = true), useUnmergedTree = true).assertIsDisplayed()

        // While Paused: Back returns to detail
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: AssertionError) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.LaneDriftDetail)
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftDetail, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun stackDropBackNavigationFlow() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        // Home -> Stack Drop
        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
        
        // On Ready Screen
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: AssertionError) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.waitUntil(30_000) {
            rule.onAllNodesWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNodes().isNotEmpty() ||
            rule.onAllNodes(hasText("Library", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Home -> Stack Drop
        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
        
        // Start Game
        rule.onNodeWithTag(ArcadeTestTags.StackDropStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.StackDropBoard)
        rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).performTouchInput { click() }
        
        // During Gameplay: Back pauses
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: AssertionError) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.onNode(hasText("Run paused", substring = true), useUnmergedTree = true).assertIsDisplayed()

        // While Paused: Back returns to Home
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: AssertionError) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.StackDropDetail)
        rule.onNodeWithTag(ArcadeTestTags.StackDropDetail, useUnmergedTree = true).assertIsDisplayed()
    }
}
