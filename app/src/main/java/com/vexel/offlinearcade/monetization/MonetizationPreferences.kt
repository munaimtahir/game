package com.vexel.offlinearcade.monetization

import android.content.SharedPreferences
import androidx.core.content.edit

private const val EntitlementStateKey = "premium_entitlement_state"
private const val PendingPurchaseKey = "premium_pending_purchase"
private const val ProductAvailableKey = "premium_product_available"
private const val LastVerifiedKey = "premium_last_verified"
private const val EntitlementSourceKey = "premium_entitlement_source"
private const val LastMessageKey = "premium_last_message"
private const val LastAdShownAtKey = "last_ad_shown_at"
private const val LastAdSessionCountKey = "last_ad_session_count"
private const val InterstitialShownDayKey = "interstitial_shown_epoch_day"
private const val InterstitialShownCountKey = "interstitial_shown_count"
private const val LastRewardedShownAtKey = "last_rewarded_shown_at"

class MonetizationPreferences(
    private val sharedPreferences: SharedPreferences,
) {
    fun readBillingState(): BillingUiState {
        val entitlementState = when (sharedPreferences.getString(EntitlementStateKey, PremiumEntitlementState.UNKNOWN.name)) {
            PremiumEntitlementState.PREMIUM.name -> PremiumEntitlementState.PREMIUM
            PremiumEntitlementState.FREE.name -> PremiumEntitlementState.FREE
            else -> PremiumEntitlementState.UNKNOWN
        }
        val source = when (sharedPreferences.getString(EntitlementSourceKey, EntitlementSource.CACHE.name)) {
            EntitlementSource.BILLING_SYNC.name -> EntitlementSource.BILLING_SYNC
            else -> EntitlementSource.CACHE
        }
        return BillingUiState(
            entitlementState = entitlementState,
            pendingPurchase = sharedPreferences.getBoolean(PendingPurchaseKey, false),
            productAvailable = sharedPreferences.getBoolean(ProductAvailableKey, false),
            lastVerifiedAtEpochMillis = sharedPreferences.getLong(LastVerifiedKey, 0L),
            source = source,
            message = sharedPreferences.getString(LastMessageKey, null),
        )
    }

    fun writeBillingState(state: BillingUiState) {
        sharedPreferences.edit(commit = true) {
            putString(EntitlementStateKey, state.entitlementState.name)
            putBoolean(PendingPurchaseKey, state.pendingPurchase)
            putBoolean(ProductAvailableKey, state.productAvailable)
            putLong(LastVerifiedKey, state.lastVerifiedAtEpochMillis)
            putString(EntitlementSourceKey, state.source.name)
            putString(LastMessageKey, state.message)
        }
    }

    fun recordInterstitialShown(nowEpochMillis: Long, completedSessions: Int, epochDay: Long) {
        val storedDay = sharedPreferences.getLong(InterstitialShownDayKey, Long.MIN_VALUE)
        val nextCount = if (storedDay == epochDay) interstitialsShownToday(epochDay) + 1 else 1
        sharedPreferences.edit(commit = true) {
            putLong(LastAdShownAtKey, nowEpochMillis)
            putInt(LastAdSessionCountKey, completedSessions)
            putLong(InterstitialShownDayKey, epochDay)
            putInt(InterstitialShownCountKey, nextCount)
        }
    }

    fun lastAdShownAtEpochMillis(): Long = sharedPreferences.getLong(LastAdShownAtKey, 0L)

    fun lastAdSessionCount(): Int = sharedPreferences.getInt(LastAdSessionCountKey, 0)

    fun interstitialsShownToday(epochDay: Long): Int {
        val storedDay = sharedPreferences.getLong(InterstitialShownDayKey, Long.MIN_VALUE)
        return if (storedDay == epochDay) {
            sharedPreferences.getInt(InterstitialShownCountKey, 0)
        } else {
            0
        }
    }

    fun recordRewardedShown(nowEpochMillis: Long) {
        sharedPreferences.edit(commit = true) { putLong(LastRewardedShownAtKey, nowEpochMillis) }
    }

    fun rewardedRecentlyShown(nowEpochMillis: Long, cooldownMillis: Long = 120_000L): Boolean {
        val lastShown = sharedPreferences.getLong(LastRewardedShownAtKey, 0L)
        return lastShown > 0L && nowEpochMillis - lastShown < cooldownMillis
    }
}
