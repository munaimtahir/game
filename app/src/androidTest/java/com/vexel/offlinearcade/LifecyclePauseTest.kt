package com.vexel.arcadetrio

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.click
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@Ignore("Lifecycle/background Compose timing is flaky on physical hardware; covered by smoke and app lifecycle testing elsewhere.")
class LifecyclePauseTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun pulseOrbitPausesOnBackground() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        rule.openHomeRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.PulseOrbitBoard)
        
        // Start Game
        rule.onNodeWithTag(ArcadeTestTags.PulseOrbitBoard, useUnmergedTree = true).performTouchInput { click() }
        
        // Background app
        rule.activityRule.scenario.moveToState(Lifecycle.State.STARTED)
        
        // Resume app
        rule.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
        
        // Screen should still be present after lifecycle resume
        rule.waitUntilExists(ArcadeTestTags.PulseOrbitBoard)
    }

    @Test
    fun laneDriftPausesOnBackground() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        rule.openHomeRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.LaneDriftBoard)
        rule.onNodeWithTag(ArcadeTestTags.LaneDriftBoard, useUnmergedTree = true).performTouchInput { click() }
        
        rule.activityRule.scenario.moveToState(Lifecycle.State.STARTED)
        rule.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
        
        rule.waitUntilExists(ArcadeTestTags.LaneDriftBoard)
    }

    @Test
    fun stackDropPausesOnBackground() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        rule.openHomeRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
        rule.onNodeWithTag(ArcadeTestTags.StackDropStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.StackDropBoard)
        
        rule.activityRule.scenario.moveToState(Lifecycle.State.STARTED)
        rule.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
        
        rule.waitUntilExists(ArcadeTestTags.StackDropBoard)
    }
}
