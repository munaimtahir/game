package com.vexel.offlinearcade.monetization

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.vexel.offlinearcade.core.data.ArcadeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

interface BillingManager {
    val state: StateFlow<BillingUiState>
    fun start()
    fun refresh()
    fun launchPremiumPurchase(activity: Activity)
}

class PlayBillingManager(
    context: Context,
    private val repository: ArcadeRepository,
    private val preferences: MonetizationPreferences,
    private val premiumProductId: String,
) : BillingManager, PurchasesUpdatedListener {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val billingClient =
        BillingClient.newBuilder(context.applicationContext)
            .setListener(this)
            .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
            .build()

    private val _state = MutableStateFlow(preferences.readBillingState())
    override val state: StateFlow<BillingUiState> = _state.asStateFlow()

    @Volatile
    private var productDetails: ProductDetails? = null

    init {
        scope.launch {
            repository.setPremiumUnlocked(_state.value.premiumActive)
        }
    }

    override fun start() {
        if (premiumProductId.isBlank()) {
            updateState(
                _state.value.copy(
                    entitlementState = if (_state.value.premiumActive) PremiumEntitlementState.PREMIUM else PremiumEntitlementState.FREE,
                    productAvailable = false,
                    message = "Premium product is not configured in this build.",
                ),
            )
            return
        }
        if (billingClient.isReady) {
            refresh()
            return
        }
        billingClient.startConnection(
            object : BillingClientStateListener {
                override fun onBillingServiceDisconnected() {
                    updateState(_state.value.copy(message = "Billing service disconnected."))
                }

                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        refresh()
                    } else {
                        updateState(
                            _state.value.copy(
                                entitlementState = if (_state.value.premiumActive) PremiumEntitlementState.PREMIUM else PremiumEntitlementState.FREE,
                                message = billingResult.debugMessage.ifBlank { "Billing unavailable." },
                            ),
                        )
                    }
                }
            },
        )
    }

    override fun refresh() {
        if (!billingClient.isReady) {
            start()
            return
        }
        scope.launch {
            productDetails = queryProductDetails()
            val purchases = queryPurchases()
            processPurchases(purchases)
            updateState(
                _state.value.copy(
                    productAvailable = productDetails != null,
                    lastVerifiedAtEpochMillis = System.currentTimeMillis(),
                    source = EntitlementSource.BILLING_SYNC,
                    message = null,
                ),
            )
        }
    }

    override fun launchPremiumPurchase(activity: Activity) {
        val details = productDetails ?: run {
            refresh()
            updateState(_state.value.copy(message = "Premium is not available yet. Try again in a moment."))
            return
        }
        val productParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(details)
            .build()
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productParams))
            .build()
        billingClient.launchBillingFlow(activity, params)
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                scope.launch { processPurchases(purchases.orEmpty()) }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                updateState(_state.value.copy(pendingPurchase = false, message = "Purchase cancelled."))
            }
            else -> {
                updateState(
                    _state.value.copy(
                        pendingPurchase = false,
                        message = billingResult.debugMessage.ifBlank { "Purchase failed." },
                    ),
                )
            }
        }
    }

    private suspend fun queryProductDetails(): ProductDetails? = suspendCancellableCoroutine { continuation ->
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(premiumProductId)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),
                ),
            )
            .build()
        billingClient.queryProductDetailsAsync(params) { billingResult, result ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                continuation.resume(result.productDetailsList?.firstOrNull())
            } else {
                continuation.resume(null)
            }
        }
    }

    private suspend fun queryPurchases(): List<Purchase> = suspendCancellableCoroutine { continuation ->
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build(),
        ) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                continuation.resume(purchases)
            } else {
                continuation.resume(emptyList())
            }
        }
    }

    private suspend fun processPurchases(purchases: List<Purchase>) {
        val premiumPurchase = purchases.firstOrNull { purchase ->
            purchase.products.contains(premiumProductId)
        }
        when (premiumPurchase?.purchaseState) {
            Purchase.PurchaseState.PURCHASED -> {
                if (!premiumPurchase.isAcknowledged) {
                    acknowledge(premiumPurchase)
                }
                repository.setPremiumUnlocked(true)
                updateState(
                    _state.value.copy(
                        entitlementState = PremiumEntitlementState.PREMIUM,
                        pendingPurchase = false,
                        source = EntitlementSource.BILLING_SYNC,
                        lastVerifiedAtEpochMillis = System.currentTimeMillis(),
                        message = "Premium active.",
                    ),
                )
            }
            Purchase.PurchaseState.PENDING -> {
                repository.setPremiumUnlocked(false)
                updateState(
                    _state.value.copy(
                        entitlementState = PremiumEntitlementState.FREE,
                        pendingPurchase = true,
                        source = EntitlementSource.BILLING_SYNC,
                        lastVerifiedAtEpochMillis = System.currentTimeMillis(),
                        message = "Purchase pending.",
                    ),
                )
            }
            else -> {
                repository.setPremiumUnlocked(false)
                updateState(
                    _state.value.copy(
                        entitlementState = PremiumEntitlementState.FREE,
                        pendingPurchase = false,
                        source = EntitlementSource.BILLING_SYNC,
                        lastVerifiedAtEpochMillis = System.currentTimeMillis(),
                    ),
                )
            }
        }
    }

    private suspend fun acknowledge(purchase: Purchase) = suspendCancellableCoroutine<Unit> { continuation ->
        billingClient.acknowledgePurchase(
            AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build(),
        ) { _ ->
            continuation.resume(Unit)
        }
    }

    private fun updateState(newState: BillingUiState) {
        _state.value = newState
        preferences.writeBillingState(newState)
    }
}
