package com.vexel.offlinearcade.monetization

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MonetizationGateTest {
    @Test
    fun initializationGateAllowsOnlyOneInitialization() {
        val gate = InitializationGate()
        assertTrue(gate.tryInitialize())
        assertFalse(gate.tryInitialize())
    }

    @Test
    fun rewardGrantGateAllowsExactlyOneReward() {
        val gate = RewardGrantGate()
        assertTrue(gate.tryGrant())
        assertFalse(gate.tryGrant())
    }
}
