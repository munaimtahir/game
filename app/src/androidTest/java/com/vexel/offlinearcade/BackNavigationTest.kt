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
        // Home -> Pulse Orbit
        rule.openHomeRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
        
        // On Ready Screen: Back returns to Home
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: Throwable) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.waitUntil(30_000) {
            rule.onAllNodesWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNodes().isNotEmpty() ||
            rule.onAllNodes(hasText("Library", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Home -> Pulse Orbit
        rule.openHomeRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
        
        // Start Game
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.PulseOrbitBoard)
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitBoard, useUnmergedTree = true).performTouchInput { click() }
        
        // During Gameplay: Back pauses
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: Throwable) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.onNode(hasText("Run paused", substring = true), useUnmergedTree = true).assertIsDisplayed()

        // While Paused: Back returns to Home
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: Throwable) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.waitUntil(30_000) {
            rule.onAllNodesWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNodes().isNotEmpty() ||
            rule.onAllNodes(hasText("Library", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun laneDriftBackNavigationFlow() {
        // Home -> Lane Drift
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        
        // On Ready Screen
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: Throwable) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.waitUntil(30_000) {
            rule.onAllNodesWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNodes().isNotEmpty() ||
            rule.onAllNodes(hasText("Library", substring = true)).fetchSemanticsNodes().isNotEmpty()
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
        } catch (e: Throwable) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.onNode(hasText("Run paused", substring = true), useUnmergedTree = true).assertIsDisplayed()

        // While Paused: Back returns to Home
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: Throwable) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.waitUntil(30_000) {
            rule.onAllNodesWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNodes().isNotEmpty() ||
            rule.onAllNodes(hasText("Library", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun stackDropBackNavigationFlow() {
        // Home -> Stack Drop
        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
        
        // On Ready Screen
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: Throwable) {
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
        rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).performClick()
        
        // During Gameplay: Back pauses
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: Throwable) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.onNode(hasText("Run paused", substring = true), useUnmergedTree = true).assertIsDisplayed()

        // While Paused: Back returns to Home
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: Throwable) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.waitUntil(30_000) {
            rule.onAllNodesWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNodes().isNotEmpty() ||
            rule.onAllNodes(hasText("Library", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
