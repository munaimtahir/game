package com.vexel.offlinearcade.monetization

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ArcadeAdPolicyTest {
    private val policy = ArcadeAdPolicy()

    @Test
    fun premiumUsersAreAlwaysIneligible() {
        assertFalse(
            policy.canShow(
                baseContext.copy(
                    premiumActive = true,
                    completedSessions = 9,
                    completedSessionsSinceLastAd = 9,
                    elapsedMillisSinceLastAd = 60 * 60 * 1000L,
                ),
            ),
        )
    }

    @Test
    fun offlineStateBlocksAds() {
        assertFalse(
            policy.canShow(
                baseContext.copy(
                    onlineCapable = false,
                    completedSessions = 9,
                    completedSessionsSinceLastAd = 9,
                    elapsedMillisSinceLastAd = 60 * 60 * 1000L,
                ),
            ),
        )
    }

    @Test
    fun frequencyCapsMustBeSatisfied() {
        assertFalse(
            policy.canShow(
                baseContext.copy(
                    completedSessions = 2,
                    completedSessionsSinceLastAd = 2,
                    elapsedMillisSinceLastAd = 60 * 60 * 1000L,
                ),
            ),
        )
        assertFalse(
            policy.canShow(
                baseContext.copy(
                    completedSessions = 8,
                    completedSessionsSinceLastAd = 1,
                    elapsedMillisSinceLastAd = 60 * 60 * 1000L,
                ),
            ),
        )
        assertFalse(
            policy.canShow(
                baseContext.copy(
                    completedSessions = 8,
                    completedSessionsSinceLastAd = 4,
                    elapsedMillisSinceLastAd = 1_000L,
                ),
            ),
        )
    }

    @Test
    fun eligibleShellStatePasses() {
        assertTrue(
            policy.canShow(
                baseContext.copy(
                    completedSessions = 8,
                    completedSessionsSinceLastAd = 4,
                    elapsedMillisSinceLastAd = 60 * 60 * 1000L,
                ),
            ),
        )
    }

    private val baseContext = AdEligibilityContext(
        placement = AdPlacement.MARKETPLACE_BANNER,
        premiumActive = false,
        onlineCapable = true,
        onboardingActive = false,
        activeGameplay = false,
        completedSessions = 0,
        completedSessionsSinceLastAd = 0,
        elapsedMillisSinceLastAd = 0L,
    )
}
