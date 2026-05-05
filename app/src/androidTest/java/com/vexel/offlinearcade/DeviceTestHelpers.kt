package com.vexel.offlinearcade

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.vexel.offlinearcade.core.ui.ArcadeTestTags

internal fun AndroidComposeTestRule<*, *>.waitUntilExists(tag: String, timeoutMillis: Long = 15_000) {
    waitUntil(timeoutMillis = timeoutMillis) {
        runCatching { onNodeWithTag(tag).fetchSemanticsNode() }.isSuccess
    }
}

internal fun AndroidComposeTestRule<*, *>.openHomeRoute(entryTag: String, screenTag: String) {
    waitUntilExists(ArcadeTestTags.HomeScreen)
    waitUntilExists(ArcadeTestTags.HomeList)
    onNodeWithTag(ArcadeTestTags.HomeList).performScrollToNode(hasTestTag(entryTag))
    onNodeWithTag(entryTag).performClick()
    waitUntilExists(screenTag)
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
