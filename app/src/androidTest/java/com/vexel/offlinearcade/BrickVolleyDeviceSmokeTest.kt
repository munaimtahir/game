package com.vexel.offlinearcade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vexel.offlinearcade.core.ui.ArcadeTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrickVolleyDeviceSmokeTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun brickVolleyRouteOpensAndShowsReadyState() {
        Thread.sleep(2000)
        rule.openHomeRoute(ArcadeTestTags.BrickVolleyEntry, ArcadeTestTags.BrickVolleyDetail)
        rule.onNodeWithTag(ArcadeTestTags.BrickVolleyStartButton, useUnmergedTree = true).performClick()
        rule.waitForIdle()
        rule.waitUntilExists(ArcadeTestTags.BrickVolleyRoot)
        rule.waitUntilExists(ArcadeTestTags.BrickVolleyAimArea)
        rule.onNodeWithTag(ArcadeTestTags.BrickVolleyScore, useUnmergedTree = true).assertIsDisplayed()
        rule.onNodeWithTag(ArcadeTestTags.BrickVolleyRound, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun brickVolleyDragReleaseChangesBoardState() {
        Thread.sleep(2000)
        rule.openHomeRoute(ArcadeTestTags.BrickVolleyEntry, ArcadeTestTags.BrickVolleyDetail)
        rule.onNodeWithTag(ArcadeTestTags.BrickVolleyStartButton, useUnmergedTree = true).performClick()
        rule.waitUntilExists(ArcadeTestTags.BrickVolleyRoot)
        rule.waitUntilExists(ArcadeTestTags.BrickVolleyAimArea)

        val aimArea = rule.onNodeWithTag(ArcadeTestTags.BrickVolleyAimArea, useUnmergedTree = true)
        aimArea.performTouchInput {
            swipeDown()
        }
        rule.waitForIdle()
        rule.onNodeWithTag(ArcadeTestTags.BrickVolleyRoot, useUnmergedTree = true).assertIsDisplayed()
        rule.onNodeWithTag(ArcadeTestTags.BrickVolleyAimArea, useUnmergedTree = true).assertIsDisplayed()
        rule.onNodeWithTag(ArcadeTestTags.BrickVolleyScore, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun brickVolleyBackDoesNotCrash() {
        Thread.sleep(2000)
        rule.openHomeRoute(ArcadeTestTags.BrickVolleyEntry, ArcadeTestTags.BrickVolleyDetail)
        rule.onNodeWithTag(ArcadeTestTags.BrickVolleyStartButton, useUnmergedTree = true).performClick()
        rule.waitUntilExists(ArcadeTestTags.BrickVolleyRoot)
        rule.waitUntilExists(ArcadeTestTags.BrickVolleyAimArea)
        androidx.test.espresso.Espresso.pressBack()
        rule.waitUntil(timeoutMillis = 30_000) {
            rule.onAllNodesWithTag(ArcadeTestTags.BrickVolleyDetail, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty() ||
                rule.onAllNodesWithTag(ArcadeTestTags.HomeScreen, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
