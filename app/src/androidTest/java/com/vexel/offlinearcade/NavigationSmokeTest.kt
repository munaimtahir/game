package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
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
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        openRouteByText("Pulse Orbit", ArcadeTestTags.PulseOrbitDetail)
        openRouteByText("Lane Drift", ArcadeTestTags.LaneDriftDetail)
        openRouteByText("Stack Drop", ArcadeTestTags.StackDropDetail)
        openRouteByText("Daily Challenges", "Daily Challenges")
        openRouteByText("Stats", "Stats")
        openRouteByText("Settings", "Settings")
    }

    private fun openRouteByText(entryText: String, expectedTitle: String) {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        
        try {
            rule.onNodeWithTag(ArcadeTestTags.HomeList, useUnmergedTree = true)
                .performScrollToNode(hasText(entryText, substring = true))
        } catch (e: AssertionError) {
        }

        try {
            rule.onNode(hasText(entryText, substring = true), useUnmergedTree = true).performScrollTo()
        } catch (e: AssertionError) {
        }

        rule.onNode(hasText(entryText, substring = true), useUnmergedTree = true).performClick()
        rule.waitForIdle()
        
        rule.waitUntil(30_000) {
            runCatching {
                rule.onAllNodesWithTag(expectedTitle, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
            }.getOrDefault(false) ||
            runCatching {
                rule.onAllNodes(hasText(expectedTitle, substring = true)).fetchSemanticsNodes().isNotEmpty()
            }.getOrDefault(false)
        }
        
        if (expectedTitle != ArcadeTestTags.PulseOrbitDetail &&
            expectedTitle != ArcadeTestTags.LaneDriftDetail &&
            expectedTitle != ArcadeTestTags.StackDropDetail
        ) {
            rule.onNode(hasText(expectedTitle, substring = true)).assertIsDisplayed()
        }
        
        // Return to Home
        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: AssertionError) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
    }
}
