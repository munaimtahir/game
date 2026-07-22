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

    fun showIfReady(
        activity: Activity,
        adUnitId: String,
        onShown: () -> Unit,
        onFinished: () -> Unit,
    ): Boolean {
        val interstitialAd = loadedAd
        if (interstitialAd == null || loadedAdUnitId != adUnitId) {
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
                preload(adUnitId)
            }

            override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                if (!finished) {
                    finished = true
                    onFinished()
                }
                preload(adUnitId)
            }
        }
        interstitialAd.show(activity)
        return true
    }
}
