package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
        openRouteByTag(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
        openRouteByTag(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        openRouteByTag(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
        openRouteByTag(ArcadeTestTags.ChallengesEntry, ArcadeTestTags.ChallengesScreen)
        openRouteByTag(ArcadeTestTags.StatsEntry, ArcadeTestTags.StatsScreen)
        openRouteByTag(ArcadeTestTags.SettingsEntry, ArcadeTestTags.SettingsScreen)
    }

    private fun openRouteByTag(entryTag: String, expectedTag: String) {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)

        rule.onNodeWithTag(entryTag, useUnmergedTree = true).performClick()
        rule.waitForIdle()

        rule.waitUntilExists(expectedTag)
        rule.onNodeWithTag(expectedTag, useUnmergedTree = true).assertIsDisplayed()

        try {
            rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        } catch (e: AssertionError) {
            androidx.test.espresso.Espresso.pressBack()
        }
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
    }
}
