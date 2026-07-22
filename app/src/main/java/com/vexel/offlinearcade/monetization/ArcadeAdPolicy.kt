package com.vexel.offlinearcade.monetization

class ArcadeAdPolicy(
    private val firstRunsWithoutAds: Int = 5,
    private val runsPerInterstitial: Int = 3,
    private val minElapsedMillisSinceLastAd: Long = 2 * 60 * 1000L,
    private val maxInterstitialsPerDay: Int = 4,
    private val minCompletedRunDurationMillis: Long = 8_000L,
) {
    fun canShow(context: AdEligibilityContext): Boolean {
        if (context.placement != AdPlacement.INTERSTITIAL_POST_RUN) return false
        if (context.premiumActive) return false
        if (!context.onlineCapable) return false
        if (context.onboardingActive) return false
        if (context.activeGameplay) return false
        if (context.completedSessions <= firstRunsWithoutAds) return false
        if ((context.completedSessions - firstRunsWithoutAds) % runsPerInterstitial != 0) return false
        if (context.completedSessionsSinceLastAd < runsPerInterstitial) return false
        if (context.elapsedMillisSinceLastAd < minElapsedMillisSinceLastAd) return false
        if (context.impressionsToday >= maxInterstitialsPerDay) return false
        if (context.runDurationMillis < minCompletedRunDurationMillis) return false
        if (context.rewardedRecentlyShown) return false
        return true
    }
}
