package com.vexel.offlinearcade.monetization

import android.app.Activity
import android.content.Context
import com.vexel.arcadetrio.BuildConfig
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicBoolean

interface ConsentManager {
    val canRequestAdsState: StateFlow<Boolean>
    val isPrivacyOptionsRequiredState: StateFlow<Boolean>

    fun canRequestAds(): Boolean
    fun isPrivacyOptionsRequired(): Boolean
    fun gatherConsent(activity: Activity?, onConsentResult: (canRequest: Boolean) -> Unit)
    fun showPrivacyOptionsForm(activity: Activity?, onDismiss: () -> Unit)
}

class GoogleUmpConsentManager(
    context: Context,
    private val adsSdkManager: AdSdkManager = AdSdkManager(context),
) : ConsentManager {
    private val appContext = context.applicationContext
    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(appContext)

    private val _canRequestAdsState = MutableStateFlow(consentInformation.canRequestAds())
    override val canRequestAdsState: StateFlow<Boolean> = _canRequestAdsState.asStateFlow()

    private val _isPrivacyOptionsRequiredState = MutableStateFlow(
        consentInformation.privacyOptionsRequirementStatus ==
            ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
    )
    override val isPrivacyOptionsRequiredState: StateFlow<Boolean> =
        _isPrivacyOptionsRequiredState.asStateFlow()
    private val requestInFlight = AtomicBoolean(false)

    override fun canRequestAds(): Boolean {
        val canRequest = consentInformation.canRequestAds()
        _canRequestAdsState.value = canRequest
        return canRequest
    }

    override fun isPrivacyOptionsRequired(): Boolean {
        val required = consentInformation.privacyOptionsRequirementStatus ==
            ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
        _isPrivacyOptionsRequiredState.value = required
        return required
    }

    override fun gatherConsent(activity: Activity?, onConsentResult: (canRequest: Boolean) -> Unit) {
        if (activity == null) {
            val canRequest = consentInformation.canRequestAds()
            updateState()
            adsSdkManager.initializeIfPermitted(canRequest)
            onConsentResult(canRequest)
            return
        }
        if (!requestInFlight.compareAndSet(false, true)) {
            onConsentResult(consentInformation.canRequestAds())
            return
        }
        val paramsBuilder = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
        if (BuildConfig.DEBUG && BuildConfig.UMP_DEBUG_GEOGRAPHY_EEA) {
            val debugSettings = ConsentDebugSettings.Builder(appContext)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            BuildConfig.UMP_DEBUG_TEST_DEVICE_HASH
                .takeIf { it.isNotBlank() }
                ?.let(debugSettings::addTestDeviceHashedId)
            paramsBuilder.setConsentDebugSettings(debugSettings.build())
        }

        consentInformation.requestConsentInfoUpdate(
            activity,
            paramsBuilder.build(),
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) {
                    finishConsentRequest(onConsentResult)
                }
            },
            { _ ->
                finishConsentRequest(onConsentResult)
            }
        )
    }

    override fun showPrivacyOptionsForm(activity: Activity?, onDismiss: () -> Unit) {
        if (activity == null) {
            onDismiss()
            return
        }
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { _ ->
            updateState()
            adsSdkManager.initializeIfPermitted(consentInformation.canRequestAds())
            onDismiss()
        }
    }

    private fun finishConsentRequest(onConsentResult: (Boolean) -> Unit) {
        val canRequest = consentInformation.canRequestAds()
        updateState()
        adsSdkManager.initializeIfPermitted(canRequest)
        requestInFlight.set(false)
        onConsentResult(canRequest)
    }

    private fun updateState() {
        _canRequestAdsState.value = consentInformation.canRequestAds()
        _isPrivacyOptionsRequiredState.value =
            consentInformation.privacyOptionsRequirementStatus ==
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
    }
}

class AlwaysPermittedConsentManager(
    initialCanRequest: Boolean = true,
    initialPrivacyOptionsRequired: Boolean = false,
) : ConsentManager {
    private val _canRequestAdsState = MutableStateFlow(initialCanRequest)
    override val canRequestAdsState: StateFlow<Boolean> = _canRequestAdsState.asStateFlow()

    private val _isPrivacyOptionsRequiredState = MutableStateFlow(initialPrivacyOptionsRequired)
    override val isPrivacyOptionsRequiredState: StateFlow<Boolean> =
        _isPrivacyOptionsRequiredState.asStateFlow()

    override fun canRequestAds(): Boolean = _canRequestAdsState.value

    override fun isPrivacyOptionsRequired(): Boolean = _isPrivacyOptionsRequiredState.value

    override fun gatherConsent(activity: Activity?, onConsentResult: (canRequest: Boolean) -> Unit) {
        onConsentResult(_canRequestAdsState.value)
    }

    override fun showPrivacyOptionsForm(activity: Activity?, onDismiss: () -> Unit) {
        onDismiss()
    }
}
