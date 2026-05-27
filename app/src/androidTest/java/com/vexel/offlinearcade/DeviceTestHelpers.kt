package com.vexel.offlinearcade

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import com.vexel.offlinearcade.core.ui.ArcadeTestTags

internal fun AndroidComposeTestRule<*, *>.waitUntilExists(tag: String, timeoutMillis: Long = 30_000) {
    waitUntil(timeoutMillis = timeoutMillis) {
        runCatching {
            onAllNodesWithTag(tag, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }.getOrDefault(false)
    }
}

internal fun AndroidComposeTestRule<*, *>.openHomeRoute(entryTag: String, screenTag: String) {
    waitUntil(timeoutMillis = 30_000) {
        runCatching {
            onAllNodesWithTag(ArcadeTestTags.HomeScreen, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }.getOrDefault(false) ||
        runCatching {
            this.onAllNodes(hasText("Library", substring = true)).fetchSemanticsNodes().isNotEmpty()
        }.getOrDefault(false)
    }

    // Ensure the entry is visible and click it
    try {
        onNodeWithTag(ArcadeTestTags.HomeList, useUnmergedTree = true)
            .performScrollToNode(hasTestTag(entryTag))
    } catch (e: AssertionError) {
        try {
            this.onAllNodesWithTag(entryTag, useUnmergedTree = true)[0].performScrollTo()
        } catch (e2: AssertionError) {
            // Ignore if not scrollable or already visible
        }
    }

    this.onAllNodesWithTag(entryTag, useUnmergedTree = true)[0].performClick()

    waitForIdle()

    waitUntil(timeoutMillis = 30_000) {
        runCatching {
            onAllNodesWithTag(screenTag, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }.getOrDefault(false) ||
        runCatching {
            this.onAllNodes(hasText("Game Info")).fetchSemanticsNodes().isNotEmpty()
        }.getOrDefault(false) ||
        runCatching {
            this.onAllNodes(hasText("Daily Challenges")).fetchSemanticsNodes().isNotEmpty()
        }.getOrDefault(false) ||
        runCatching {
            this.onAllNodes(hasText("Stats")).fetchSemanticsNodes().isNotEmpty()
        }.getOrDefault(false) ||
        runCatching {
            this.onAllNodes(hasText("Settings")).fetchSemanticsNodes().isNotEmpty()
        }.getOrDefault(false)
    }
}

internal fun SemanticsNodeInteraction.readText(): String {
    val node = fetchSemanticsNode()
    val texts = runCatching { node.config[SemanticsProperties.Text] }.getOrDefault(emptyList())
    return texts.joinToString(separator = "") { it.text }
}

internal fun SemanticsNodeInteraction.readStateDescription(): String {
    val node = fetchSemanticsNode()
    return runCatching { node.config[SemanticsProperties.StateDescription] }.getOrNull() ?: ""
}
