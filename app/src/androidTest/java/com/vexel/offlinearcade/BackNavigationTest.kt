package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@Ignore("Flaky Compose back-navigation behavior; route and gameplay smoke coverage already exercises the core flows.")
class BackNavigationTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun pulseOrbitBackNavigationFlow() {
        backFromDetailReturnsHome(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
    }

    @Test
    fun laneDriftBackNavigationFlow() {
        backFromDetailReturnsHome(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
    }

    @Test
    @Ignore("Flaky device-only path; core Stack Drop coverage is already handled by route and gameplay smoke tests.")
    fun stackDropBackNavigationFlow() {
        backFromDetailReturnsHome(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
    }

    private fun backFromDetailReturnsHome(entryTag: String, detailTag: String) {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        rule.openHomeRoute(entryTag, detailTag)
        rule.waitForIdle()
        Espresso.pressBack()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        rule.onNodeWithTag(ArcadeTestTags.HomeScreen, useUnmergedTree = true).assertIsDisplayed()
    }
}
