package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationSmokeTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeNavigatesToAllCoreRoutes() {
        openRouteByText("Pulse Orbit", "Game Info")
        openRouteByText("Lane Drift", "Game Info")
        openRouteByText("Stack Drop", "Game Info")
        openRouteByText("Daily Challenges", "Daily Challenges")
        openRouteByText("Stats", "Stats")
        openRouteByText("Settings", "Settings")
    }

    private fun openRouteByText(entryText: String, expectedTitle: String) {
        rule.waitUntil(30_000) {
            runCatching { rule.onNode(hasText("Arcade Library")).fetchSemanticsNode() }.isSuccess
        }
        
        rule.onNode(hasText(entryText)).performScrollTo().performClick()
        rule.waitForIdle()
        
        rule.waitUntil(30_000) {
            runCatching { rule.onNode(hasText(expectedTitle)).fetchSemanticsNode() }.isSuccess
        }
        
        rule.onNode(hasText(expectedTitle)).assertIsDisplayed()
        
        // Return to Home
        androidx.test.espresso.Espresso.pressBack()
        rule.waitForIdle()
    }
}
