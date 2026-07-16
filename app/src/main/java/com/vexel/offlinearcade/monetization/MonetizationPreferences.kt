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

    fun recordAdShown(nowEpochMillis: Long, completedSessions: Int) {
        sharedPreferences.edit(commit = true) {
            putLong(LastAdShownAtKey, nowEpochMillis)
            putInt(LastAdSessionCountKey, completedSessions)
        }
    }

    fun lastAdShownAtEpochMillis(): Long = sharedPreferences.getLong(LastAdShownAtKey, 0L)

    fun lastAdSessionCount(): Int = sharedPreferences.getInt(LastAdSessionCountKey, 0)
}
