package com.vexel.offlinearcade.core.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DailyChallengeGeneratorTest {
    @Test
    fun generatorIsDeterministicForSameDay() {
        val first = DailyChallengeGenerator.generate(42_424L)
        val second = DailyChallengeGenerator.generate(42_424L)

        assertEquals(first, second)
        assertEquals(4, first.size)
        assertTrue(first.any { it.gameId == null })
    }

    @Test
    fun generatorChangesAcrossDays() {
        val dayA = DailyChallengeGenerator.generate(42_424L)
        val dayB = DailyChallengeGenerator.generate(42_425L)

        assertTrue(dayA.map { it.challengeId } != dayB.map { it.challengeId })
    }
}
