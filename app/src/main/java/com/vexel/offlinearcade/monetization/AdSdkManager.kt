package com.vexel.offlinearcade.monetization

import android.content.Context
import com.google.android.gms.ads.MobileAds
import java.util.concurrent.atomic.AtomicBoolean

/** Process-scoped, consent-gated Mobile Ads initialization. */
class AdSdkManager(context: Context) {
    private val appContext = context.applicationContext
    private val initialized = InitializationGate()

    fun initializeIfPermitted(canRequestAds: Boolean, onComplete: (Boolean) -> Unit = {}) {
        if (!canRequestAds) {
            onComplete(false)
            return
        }
        if (!initialized.tryInitialize()) {
            onComplete(true)
            return
        }
        MobileAds.initialize(appContext) {
            onComplete(true)
        }
    }
}

class InitializationGate {
    private val initialized = AtomicBoolean(false)

    fun tryInitialize(): Boolean = initialized.compareAndSet(false, true)
}

class RewardGrantGate {
    private val granted = AtomicBoolean(false)

    fun tryGrant(): Boolean = granted.compareAndSet(false, true)
}

class FullScreenAdCoordinator {
    private val showing = AtomicBoolean(false)

    fun tryBegin(): Boolean = showing.compareAndSet(false, true)

    fun end() {
        showing.set(false)
    }

    fun isShowing(): Boolean = showing.get()
}
