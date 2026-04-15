package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
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
        openRoute(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitScreen)
        openRoute(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftScreen)
        openRoute(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropScreen)
        openRoute(ArcadeTestTags.ChallengesEntry, ArcadeTestTags.ChallengesScreen)
        openRoute(ArcadeTestTags.StatsEntry, ArcadeTestTags.StatsScreen)

        rule.waitUntilExists(ArcadeTestTags.HomeList)
        rule.onNodeWithTag(ArcadeTestTags.HomeList)
            .performScrollToNode(hasTestTag(ArcadeTestTags.SettingsEntry))
        rule.onNodeWithTag(ArcadeTestTags.SettingsEntry).performClick()
        rule.waitUntilExists(ArcadeTestTags.SettingsScreen)
        rule.onNodeWithTag(ArcadeTestTags.SettingsScreen).assertIsDisplayed()
    }

    private fun openRoute(entryTag: String, screenTag: String) {
        rule.openHomeRoute(entryTag, screenTag)
        rule.onNodeWithTag(screenTag).assertIsDisplayed()
        rule.onNodeWithTag(ArcadeTestTags.BackButton).performClick()
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
    }
}
