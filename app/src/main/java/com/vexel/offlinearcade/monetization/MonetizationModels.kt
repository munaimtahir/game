package com.vexel.offlinearcade.monetization

enum class PremiumEntitlementState {
    UNKNOWN,
    FREE,
    PREMIUM,
}

enum class EntitlementSource {
    CACHE,
    BILLING_SYNC,
}

data class BillingUiState(
    val entitlementState: PremiumEntitlementState = PremiumEntitlementState.UNKNOWN,
    val pendingPurchase: Boolean = false,
    val productAvailable: Boolean = false,
    val lastVerifiedAtEpochMillis: Long = 0L,
    val source: EntitlementSource = EntitlementSource.CACHE,
    val message: String? = null,
) {
    val premiumActive: Boolean = entitlementState == PremiumEntitlementState.PREMIUM
}

enum class AdPlacement {
    INTERSTITIAL_POST_RUN,
}

data class AdEligibilityContext(
    val placement: AdPlacement,
    val premiumActive: Boolean,
    val onlineCapable: Boolean,
    val onboardingActive: Boolean = false,
    val activeGameplay: Boolean = false,
    val completedSessions: Int,
    val completedSessionsSinceLastAd: Int,
    val elapsedMillisSinceLastAd: Long,
    val impressionsToday: Int = 0,
    val runDurationMillis: Long = 0L,
    val rewardedRecentlyShown: Boolean = false,
)
