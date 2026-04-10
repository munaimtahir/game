package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
        rule.onNodeWithText("Play Pulse Orbit").performClick()
        rule.onNodeWithText("Pulse Orbit").assertIsDisplayed()
        rule.onNodeWithText("Back").performClick()

        rule.onNodeWithText("Play Lane Drift").performClick()
        rule.onNodeWithText("Lane Drift").assertIsDisplayed()
        rule.onNodeWithText("Back").performClick()

        rule.onNodeWithText("Play Stack Drop").performClick()
        rule.onNodeWithText("Stack Drop").assertIsDisplayed()
        rule.onNodeWithText("Back").performClick()

        rule.onNodeWithText("Daily Challenges").performClick()
        rule.onNodeWithText("Offline seeded each day").assertIsDisplayed()
        rule.onNodeWithText("Back").performClick()

        rule.onNodeWithText("Stats").performClick()
        rule.onNodeWithText("Shared totals").assertIsDisplayed()
        rule.onNodeWithText("Back").performClick()

        rule.onNodeWithText("Settings").performClick()
        rule.onNodeWithText("Audio and feel").assertIsDisplayed()
    }
}
