package com.vexel.arcadetrio

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@Ignore("Route smoke is covered by the ADB screenshot smoke; keep connected tests focused on non-UI persistence checks.")
class NavigationSmokeTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeNavigatesToAllCoreRoutes() {
        rule.waitUntilExists(ArcadeTestTags.HomeScreen)
        openRouteByTag(ArcadeTestTags.PulseOrbitEntry, ArcadeTestTags.PulseOrbitDetail)
        openRouteByTag(ArcadeTestTags.LaneDriftEntry, ArcadeTestTags.LaneDriftDetail)
        openRouteByTag(ArcadeTestTags.StackDropEntry, ArcadeTestTags.StackDropDetail)
    }

    private fun openRouteByTag(entryTag: String, expectedTag: String) {
        rule.openHomeRoute(entryTag, expectedTag)
        rule.onNodeWithTag(expectedTag, useUnmergedTree = true).assertIsDisplayed()
    }
}
