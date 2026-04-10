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
class SettingsPersistenceSmokeTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun settingsScreenSurvivesActivityRecreate() {
        rule.onNodeWithTag(ArcadeTestTags.HomeList)
            .performScrollToNode(hasTestTag(ArcadeTestTags.SettingsEntry))
        rule.onNodeWithTag(ArcadeTestTags.SettingsEntry).performClick()
        rule.waitUntilExists(ArcadeTestTags.SettingsScreen)
        rule.onNodeWithTag(ArcadeTestTags.SettingsScreen).assertIsDisplayed()
        rule.onNodeWithTag(ArcadeTestTags.SoundToggle).assertIsDisplayed()

        rule.activityRule.scenario.recreate()

        rule.waitUntilExists(ArcadeTestTags.SettingsScreen)
        rule.onNodeWithTag(ArcadeTestTags.SettingsScreen).assertIsDisplayed()
        rule.onNodeWithTag(ArcadeTestTags.SoundToggle).assertIsDisplayed()
    }

    private fun androidx.compose.ui.test.junit4.AndroidComposeTestRule<*, *>.waitUntilExists(tag: String) {
        waitUntil(timeoutMillis = 5_000) {
            runCatching { onNodeWithTag(tag).fetchSemanticsNode() }.isSuccess
        }
    }
}
