package com.vexel.offlinearcade.monetization

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ArcadeRewardedController(
    context: Context,
    private val fullScreenCoordinator: FullScreenAdCoordinator = FullScreenAdCoordinator(),
) {
    private val appContext = context.applicationContext
    private var loadedAd: RewardedAd? = null
    private var loading = false
    private var loadedAdUnitId: String? = null
    private val _ready = MutableStateFlow(false)
    val ready: StateFlow<Boolean> = _ready.asStateFlow()

    fun preload(adUnitId: String) {
        if (adUnitId.isBlank() || loading || (loadedAd != null && loadedAdUnitId == adUnitId)) return
        loading = true
        RewardedAd.load(
            appContext,
            adUnitId,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    loading = false
                    loadedAd = rewardedAd
                    loadedAdUnitId = adUnitId
                    _ready.value = true
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    loading = false
                    loadedAd = null
                    loadedAdUnitId = null
                    _ready.value = false
                }
            },
        )
    }

    fun isReady(adUnitId: String): Boolean = loadedAd != null && loadedAdUnitId == adUnitId

    fun clear() {
        loadedAd = null
        loadedAdUnitId = null
        loading = false
        _ready.value = false
    }

    fun showIfReady(
        activity: Activity,
        adUnitId: String,
        onRewardEarned: (RewardItem) -> Unit,
        onShown: () -> Unit = {},
        onFinished: () -> Unit = {},
    ): Boolean {
        val rewardedAd = loadedAd
        if (rewardedAd == null || loadedAdUnitId != adUnitId || activity.isFinishing || activity.isDestroyed) {
            preload(adUnitId)
            return false
        }
        if (!fullScreenCoordinator.tryBegin()) return false

        loadedAd = null
        loadedAdUnitId = null
        _ready.value = false
        val rewardGrantGate = RewardGrantGate()
        var finished = false
        fun finishOnce() {
            if (!finished) {
                finished = true
                fullScreenCoordinator.end()
                onFinished()
            }
        }
        rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                onShown()
            }

            override fun onAdDismissedFullScreenContent() {
                finishOnce()
                preload(adUnitId)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                finishOnce()
                preload(adUnitId)
            }
        }
        rewardedAd.show(activity) { rewardItem ->
            if (rewardGrantGate.tryGrant()) {
                onRewardEarned(rewardItem)
            }
        }
        return true
    }
}
