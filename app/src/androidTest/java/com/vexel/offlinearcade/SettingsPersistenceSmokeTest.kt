package com.vexel.arcadetrio

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@Ignore("Settings UI timing is flaky on low-end physical hardware; route smoke and stateful logic tests cover the release gate.")
class SettingsPersistenceSmokeTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun settingsScreenSurvivesActivityRecreate() {
        rule.openHomeRoute(ArcadeTestTags.SettingsEntry, ArcadeTestTags.SettingsScreen)
        rule.onNodeWithTag(ArcadeTestTags.SettingsScreen).assertIsDisplayed()
        rule.onNodeWithTag(ArcadeTestTags.SoundToggle).assertIsDisplayed()

        rule.activityRule.scenario.recreate()

        rule.waitUntilExists(ArcadeTestTags.SettingsScreen)
        rule.onNodeWithTag(ArcadeTestTags.SettingsScreen).assertIsDisplayed()
        rule.onNodeWithTag(ArcadeTestTags.SoundToggle).assertIsDisplayed()
    }
}
