package com.vexel.offlinearcade.monetization

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ArcadeConsentManagerTest {
    @Test
    fun alwaysPermittedConsentManagerReflectsConfiguredState() {
        val manager = AlwaysPermittedConsentManager(initialCanRequest = true, initialPrivacyOptionsRequired = true)
        assertTrue(manager.canRequestAds())
        assertTrue(manager.isPrivacyOptionsRequired())
    }

    @Test
    fun disabledConsentManagerBlocksAds() {
        val manager = AlwaysPermittedConsentManager(initialCanRequest = false, initialPrivacyOptionsRequired = false)
        assertFalse(manager.canRequestAds())
        assertFalse(manager.isPrivacyOptionsRequired())
    }

    @Test
    fun gatherConsentCallsResultCallback() {
        val manager = AlwaysPermittedConsentManager(initialCanRequest = true)
        var resultReceived: Boolean? = null
        manager.gatherConsent(activity = null) { canRequest ->
            resultReceived = canRequest
        }
        assertTrue(resultReceived == true)
    }
}
