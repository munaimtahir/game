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
        onAllNodesWithTag(tag, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
    }
}

internal fun AndroidComposeTestRule<*, *>.openHomeRoute(entryTag: String, screenTag: String) {
    waitUntil(timeoutMillis = 30_000) {
        onAllNodesWithTag(ArcadeTestTags.HomeScreen, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty() ||
        this.onAllNodes(hasText("Library", substring = true)).fetchSemanticsNodes().isNotEmpty()
    }

    // Ensure the entry is visible and click it
    try {
        onNodeWithTag(ArcadeTestTags.HomeList, useUnmergedTree = true)
            .performScrollToNode(hasTestTag(entryTag))
    } catch (e: Throwable) {
        // If the list is fully visible on screen, it might not be scrollable, causing performScrollToNode to fail.
        // Also the node might already be visible.
    }

    onNodeWithTag(entryTag, useUnmergedTree = true)
        .performClick()

    waitForIdle()

    waitUntil(timeoutMillis = 30_000) {
        onAllNodesWithTag(screenTag, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty() ||
        this.onAllNodes(hasText("Game Info")).fetchSemanticsNodes().isNotEmpty() ||
        this.onAllNodes(hasText("Daily Challenges")).fetchSemanticsNodes().isNotEmpty() ||
        this.onAllNodes(hasText("Stats")).fetchSemanticsNodes().isNotEmpty() ||
        this.onAllNodes(hasText("Settings")).fetchSemanticsNodes().isNotEmpty()
    }
}

internal fun SemanticsNodeInteraction.readText(): String {
    val node = fetchSemanticsNode()
    val texts = runCatching { node.config[SemanticsProperties.Text] }.getOrDefault(emptyList())
    return texts.joinToString(separator = "") { it.text }
}

internal fun SemanticsNodeInteraction.readStateDescription(): String {
    val node = fetchSemanticsNode()
    return runCatching { node.config[SemanticsProperties.StateDescription] }.getOrDefault("")
}
