package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
        openRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
        openRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        openRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
        openRoute(ArcadeTestTags.ChallengesEntry, ArcadeTestTags.ChallengesScreen)
        openRoute(ArcadeTestTags.StatsEntry, ArcadeTestTags.StatsScreen)

        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Arcade Library")).fetchSemanticsNode() }.isSuccess
        }
        rule.onNodeWithTag(ArcadeTestTags.HomeList, useUnmergedTree = true)
            .performScrollToNode(hasTestTag(ArcadeTestTags.SettingsEntry))
        rule.onNodeWithTag(ArcadeTestTags.SettingsEntry, useUnmergedTree = true).performClick()
        rule.waitUntilExists(ArcadeTestTags.SettingsScreen)
        rule.onNodeWithTag(ArcadeTestTags.SettingsScreen, useUnmergedTree = true).assertIsDisplayed()
    }

    private fun openRoute(entryTag: String, screenTag: String) {
        rule.openHomeRoute(entryTag, screenTag)
        rule.onNodeWithTag(screenTag, useUnmergedTree = true).assertIsDisplayed()
        rule.onNodeWithTag(ArcadeTestTags.BackButton, useUnmergedTree = true).performClick()
        rule.waitUntil(30_000) {
            runCatching { rule.onNodeWithTag(ArcadeTestTags.HomeScreen, true).fetchSemanticsNode() }.isSuccess ||
            runCatching { rule.onNode(hasText("Arcade Library")).fetchSemanticsNode() }.isSuccess
        }
    }
}
