package com.vexel.offlinearcade.monetization

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class ArcadeInterstitialController(
    context: Context,
    private val fullScreenCoordinator: FullScreenAdCoordinator = FullScreenAdCoordinator(),
) {
    private val appContext = context.applicationContext

    private var loadedAd: InterstitialAd? = null
    private var loading = false
    private var loadedAdUnitId: String? = null

    fun preload(adUnitId: String) {
        if (adUnitId.isBlank()) return
        if (loading) return
        if (loadedAd != null && loadedAdUnitId == adUnitId) return

        loading = true
        InterstitialAd.load(
            appContext,
            adUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    loading = false
                    loadedAd = interstitialAd
                    loadedAdUnitId = adUnitId
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    loading = false
                    loadedAd = null
                    loadedAdUnitId = null
                }
            },
        )
    }

    fun clear() {
        loadedAd = null
        loadedAdUnitId = null
        loading = false
    }

    fun showIfReady(
        activity: Activity,
        adUnitId: String,
        onShown: () -> Unit,
        onFinished: () -> Unit,
    ): Boolean {
        if (activity.isFinishing || activity.isDestroyed || !fullScreenCoordinator.tryBegin()) {
            return false
        }
        val interstitialAd = loadedAd
        if (interstitialAd == null || loadedAdUnitId != adUnitId) {
            fullScreenCoordinator.end()
            preload(adUnitId)
            return false
        }

        loadedAd = null
        loadedAdUnitId = null
        interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            private var finished = false

            override fun onAdShowedFullScreenContent() {
                onShown()
            }

            override fun onAdDismissedFullScreenContent() {
                if (!finished) {
                    finished = true
                    onFinished()
                }
                fullScreenCoordinator.end()
                preload(adUnitId)
            }

            override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                if (!finished) {
                    finished = true
                    onFinished()
                }
                fullScreenCoordinator.end()
                preload(adUnitId)
            }
        }
        interstitialAd.show(activity)
        return true
    }
}
