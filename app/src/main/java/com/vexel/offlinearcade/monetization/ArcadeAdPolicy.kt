package com.vexel.offlinearcade.monetization

class ArcadeAdPolicy(
    private val minCompletedSessions: Int = 3,
    private val minSessionsSinceLastAd: Int = 2,
    private val minElapsedMillisSinceLastAd: Long = 10 * 60 * 1000L,
) {
    fun canShow(context: AdEligibilityContext): Boolean {
        if (context.premiumActive) return false
        if (!context.onlineCapable) return false
        if (context.onboardingActive) return false
        if (context.activeGameplay) return false
        if (context.completedSessions < minCompletedSessions) return false
        if (context.completedSessionsSinceLastAd < minSessionsSinceLastAd) return false
        if (context.elapsedMillisSinceLastAd < minElapsedMillisSinceLastAd) return false
        return true
    }
}
