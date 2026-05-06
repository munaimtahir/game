package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
        rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Library", substring = true)).fetchSemanticsNode() }.isSuccess
        }

        // Home -> Pulse Orbit
        rule.openHomeRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
        
        // Start Game
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.PulseOrbitBoard)
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitBoard, useUnmergedTree = true).performTouchInput { click() }
        
        // During Gameplay: Back pauses
        rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.onNode(hasText("Run paused", substring = true), useUnmergedTree = true).assertIsDisplayed()

        // While Paused: Back returns to Home
        rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Library", substring = true)).fetchSemanticsNode() }.isSuccess
        }
    }

    @Test
    fun laneDriftBackNavigationFlow() {
        // Home -> Lane Drift
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        
        // On Ready Screen
        rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Library", substring = true)).fetchSemanticsNode() }.isSuccess
        }

        // Home -> Lane Drift
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        
        // Start Game
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.LaneDriftBoard)
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftBoard, useUnmergedTree = true).performTouchInput { click() }
        
        // During Gameplay: Back pauses
        rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.onNode(hasText("Run paused", substring = true), useUnmergedTree = true).assertIsDisplayed()

        // While Paused: Back returns to Home
        rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Library", substring = true)).fetchSemanticsNode() }.isSuccess
        }
    }

    @Test
    fun stackDropBackNavigationFlow() {
        // Home -> Stack Drop
        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
        
        // On Ready Screen
        rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Library", substring = true)).fetchSemanticsNode() }.isSuccess
        }

        // Home -> Stack Drop
        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
        
        // Start Game
        rule.onNodeWithTag(ArcadeTestTags.StackDropStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.StackDropBoard)
        rule.onNodeWithTag(ArcadeTestTags.StackDropBoard, useUnmergedTree = true).performClick()
        
        // During Gameplay: Back pauses
        rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.onNode(hasText("Run paused", substring = true), useUnmergedTree = true).assertIsDisplayed()

        // While Paused: Back returns to Home
        rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Library", substring = true)).fetchSemanticsNode() }.isSuccess
        }
    }
}
