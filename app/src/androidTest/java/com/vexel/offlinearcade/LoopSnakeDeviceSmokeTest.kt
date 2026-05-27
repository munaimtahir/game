package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@Ignore("Legacy non-MVP game; excluded from the locked three-game CI gate.")
class LoopSnakeDeviceSmokeTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loopSnakeRouteOpensAndShowsReadyState() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        rule.openHomeRoute(ArcadeTestTags.LoopSnakeEntry, ArcadeTestTags.LoopSnakeDetailRoot)
        rule.onNodeWithTag(ArcadeTestTags.LoopSnakeStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.LoopSnakeRoot)
        rule.onNodeWithTag(ArcadeTestTags.LoopSnakeReady, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun loopSnakeStartGameAndSteer() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        rule.openHomeRoute(ArcadeTestTags.LoopSnakeEntry, ArcadeTestTags.LoopSnakeDetailRoot)
        rule.onNodeWithTag(ArcadeTestTags.LoopSnakeStartButton, useUnmergedTree = true).performClick()
        rule.waitUntilExists(ArcadeTestTags.LoopSnakeReady)
        
        // Start game via overlay start button
        rule.onNodeWithTag(ArcadeTestTags.LoopSnakeStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        
        rule.onNodeWithTag(ArcadeTestTags.LoopSnakePlayArea, useUnmergedTree = true).assertIsDisplayed()
        rule.onNodeWithTag(ArcadeTestTags.LoopSnakeScore, useUnmergedTree = true).assertIsDisplayed()
        rule.onNodeWithTag(ArcadeTestTags.LoopSnakeLength, useUnmergedTree = true).assertIsDisplayed()

        // Perform a swipe gesture to steer
        rule.onNodeWithTag(ArcadeTestTags.LoopSnakePlayArea, useUnmergedTree = true).performTouchInput {
            swipeUp()
        }
        rule.waitForIdle()
    }

    @Test
    fun loopSnakeBackNavigationDoesNotCrash() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        rule.openHomeRoute(ArcadeTestTags.LoopSnakeEntry, ArcadeTestTags.LoopSnakeDetailRoot)
        rule.onNodeWithTag(ArcadeTestTags.LoopSnakeStartButton, useUnmergedTree = true).performClick()
        rule.waitUntilExists(ArcadeTestTags.LoopSnakeReady)
        
        // Press Back from ready state, should go back to detail screen
        androidx.test.espresso.Espresso.pressBack()
        rule.waitUntil(timeoutMillis = 30_000) {
            rule.onAllNodesWithTag(ArcadeTestTags.LoopSnakeDetailRoot, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
