package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.click
import androidx.test.espresso.Espresso
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
        Espresso.pressBack()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Arcade Library")).fetchSemanticsNode() }.isSuccess
        }
        // rule.onNodeWithTag(ArcadeTestTags.HomeScreen).assertIsDisplayed()

        // Home -> Pulse Orbit
        rule.openHomeRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
        
        // Start Game
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitStartButton).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.PulseOrbitBoard)
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitBoard).performTouchInput { click() }
        
        // During Gameplay: Back pauses
        Espresso.pressBack()
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitScreen).assertIsDisplayed()
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitBoard).assertIsDisplayed()
        rule.onNode(androidx.compose.ui.test.hasText("Run paused")).assertIsDisplayed()

        // While Paused: Back returns to Home
        Espresso.pressBack()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Arcade Library")).fetchSemanticsNode() }.isSuccess
        }
    }

    @Test
    fun laneDriftBackNavigationFlow() {
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftScreen)
        
        // On Ready Screen
        Espresso.pressBack()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Arcade Library")).fetchSemanticsNode() }.isSuccess
        }

        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        
        // Start Game
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftStartButton).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.LaneDriftBoard)
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftBoard).performTouchInput { click() }
        
        // During Gameplay: Back pauses
        Espresso.pressBack()
        rule.onNode(androidx.compose.ui.test.hasText("Run paused")).assertIsDisplayed()

        // While Paused: Back returns to Home
        Espresso.pressBack()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Arcade Library")).fetchSemanticsNode() }.isSuccess
        }
    }

    @Test
    fun stackDropBackNavigationFlow() {
        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropScreen)
        
        // On Ready Screen
        Espresso.pressBack()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Arcade Library")).fetchSemanticsNode() }.isSuccess
        }

        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
        
        // Start Game
        rule.onNodeWithTag(ArcadeTestTags.StackDropStartButton).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.StackDropBoard)
        rule.onNodeWithTag(ArcadeTestTags.StackDropBoard).performClick()
        rule.onNode(androidx.compose.ui.test.hasText("Run paused")).assertIsDisplayed()

        // While Paused: Back returns to Home
        Espresso.pressBack()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Arcade Library")).fetchSemanticsNode() }.isSuccess
        }
    }
}
