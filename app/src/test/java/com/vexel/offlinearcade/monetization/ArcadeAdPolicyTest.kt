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
    fun firstFiveCompletedRunsNeverShowAds() {
        assertFalse(
            policy.canShow(
                baseContext.copy(
                    completedSessions = 5,
                    completedSessionsSinceLastAd = 5,
                    elapsedMillisSinceLastAd = 60 * 60 * 1000L,
                ),
            ),
        )
    }

    @Test
    fun onlyEveryThirdCompletedRunAfterGraceWindowIsEligible() {
        assertFalse(
            policy.canShow(
                baseContext.copy(
                    completedSessions = 6,
                    completedSessionsSinceLastAd = 6,
                    elapsedMillisSinceLastAd = 60 * 60 * 1000L,
                ),
            ),
        )
        assertFalse(
            policy.canShow(
                baseContext.copy(
                    completedSessions = 7,
                    completedSessionsSinceLastAd = 7,
                    elapsedMillisSinceLastAd = 60 * 60 * 1000L,
                ),
            ),
        )
        assertTrue(
            policy.canShow(
                baseContext.copy(
                    completedSessions = 8,
                    completedSessionsSinceLastAd = 8,
                    elapsedMillisSinceLastAd = 60 * 60 * 1000L,
                ),
            ),
        )
        assertFalse(
            policy.canShow(
                baseContext.copy(
                    completedSessions = 9,
                    completedSessionsSinceLastAd = 9,
                    elapsedMillisSinceLastAd = 60 * 60 * 1000L,
                ),
            ),
        )
        assertFalse(
            policy.canShow(
                baseContext.copy(
                    completedSessions = 10,
                    completedSessionsSinceLastAd = 10,
                    elapsedMillisSinceLastAd = 60 * 60 * 1000L,
                ),
            ),
        )
    }

    @Test
    fun cooldownAndDailyCapAreEnforced() {
        assertFalse(
            policy.canShow(
                baseContext.copy(
                    completedSessions = 8,
                    completedSessionsSinceLastAd = 3,
                    elapsedMillisSinceLastAd = 1_000L,
                ),
            ),
        )
        assertFalse(
            policy.canShow(
                baseContext.copy(
                    completedSessions = 8,
                    completedSessionsSinceLastAd = 3,
                    elapsedMillisSinceLastAd = 60 * 60 * 1000L,
                    impressionsToday = 4,
                ),
            ),
        )
    }

    @Test
    fun shortRunsAreRejected() {
        assertFalse(
            policy.canShow(
                baseContext.copy(
                    completedSessions = 8,
                    completedSessionsSinceLastAd = 3,
                    elapsedMillisSinceLastAd = 60 * 60 * 1000L,
                    runDurationMillis = 4_000L,
                ),
            ),
        )
    }

    private val baseContext = AdEligibilityContext(
        placement = AdPlacement.INTERSTITIAL_POST_RUN,
        premiumActive = false,
        onlineCapable = true,
        onboardingActive = false,
        activeGameplay = false,
        completedSessions = 0,
        completedSessionsSinceLastAd = 0,
        elapsedMillisSinceLastAd = 0L,
        impressionsToday = 0,
        runDurationMillis = 10_000L,
    )
}
