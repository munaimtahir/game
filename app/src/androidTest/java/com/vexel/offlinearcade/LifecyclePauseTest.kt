package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.click
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LifecyclePauseTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun pulseOrbitPausesOnBackground() {
        rule.openHomeRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitScreen)
        
        // Start Game
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitBoard).performTouchInput { click() }
        
        // Background app
        rule.activityRule.scenario.moveToState(Lifecycle.State.STARTED) // Simulates background
        
        // Resume app
        rule.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
        
        // Check if paused
        rule.onNode(androidx.compose.ui.test.hasText("Run paused")).assertIsDisplayed()
    }

    @Test
    fun laneDriftPausesOnBackground() {
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftScreen)
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftBoard).performTouchInput { click() }
        
        rule.activityRule.scenario.moveToState(Lifecycle.State.STARTED)
        rule.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
        
        rule.onNode(androidx.compose.ui.test.hasText("Run paused")).assertIsDisplayed()
    }

    @Test
    fun stackDropPausesOnBackground() {
        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropScreen)
        rule.onNodeWithTag(ArcadeTestTags.StackDropStartButton).performClick()
        
        rule.activityRule.scenario.moveToState(Lifecycle.State.STARTED)
        rule.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
        
        rule.onNode(androidx.compose.ui.test.hasText("Run paused")).assertIsDisplayed()
    }
}
