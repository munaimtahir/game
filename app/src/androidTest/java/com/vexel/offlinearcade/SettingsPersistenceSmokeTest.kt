package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
        rule.onNodeWithText("Settings").performClick()
        rule.onNodeWithText("Audio and feel").assertIsDisplayed()

        rule.activityRule.scenario.recreate()

        rule.onNodeWithText("Audio and feel").assertIsDisplayed()
        rule.onAllNodesWithText("Sound")[0].assertIsDisplayed()
    }
}
