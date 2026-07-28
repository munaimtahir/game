package com.vexel.offlinearcade.monetization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun ArcadeBanner(
    adUnitId: String,
    modifier: Modifier = Modifier,
    onImpression: () -> Unit = {},
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    var loaded by remember(adUnitId) { mutableStateOf(false) }
    val adView = remember(adUnitId) {
        AdView(context).apply {
            this.adUnitId = adUnitId
        }
    }

    DisposableEffect(adView) {
        val listener = object : com.google.android.gms.ads.AdListener() {
            override fun onAdLoaded() {
                loaded = true
            }

            override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                loaded = false
            }

            override fun onAdImpression() {
                onImpression()
            }
        }
        adView.adListener = listener
        onDispose {
            adView.destroy()
        }
    }

    val widthDp = configuration.screenWidthDp.coerceAtLeast(1)
    LaunchedEffect(adView, widthDp) {
        adView.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, widthDp))
        adView.loadAd(AdRequest.Builder().build())
    }

    if (loaded) {
        AndroidView(
            modifier = modifier,
            factory = { adView },
            update = { },
        )
    }
}

@Composable
fun MarketplaceAdBanner(adUnitId: String, modifier: Modifier = Modifier, onImpression: () -> Unit = {}) =
    ArcadeBanner(adUnitId = adUnitId, modifier = modifier, onImpression = onImpression)
