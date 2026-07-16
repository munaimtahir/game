package com.vexel.offlinearcade.monetization

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

@Composable
fun MarketplaceAdBanner(
    adUnitId: String,
    modifier: Modifier = Modifier,
    onImpression: () -> Unit,
) {
    val context = LocalContext.current
    val adView = remember(adUnitId) {
        MobileAds.initialize(context) {}
        AdView(context).apply {
            this.adUnitId = adUnitId
            setAdSize(AdSize.MEDIUM_RECTANGLE)
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            loadAd(AdRequest.Builder().build())
        }
    }

    DisposableEffect(adView) {
        val listener = object : com.google.android.gms.ads.AdListener() {
            override fun onAdImpression() {
                onImpression()
            }
        }
        adView.adListener = listener
        onDispose {
            adView.destroy()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { adView },
        update = { },
    )
}
