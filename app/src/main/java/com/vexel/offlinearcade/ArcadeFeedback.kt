package com.vexel.arcadetrio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.RemoteException
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.vexel.offlinearcade.core.model.ArcadeFeedback
import com.vexel.offlinearcade.core.model.ArcadeFeedbackEvent
import com.vexel.offlinearcade.core.model.SettingsState

private class ArcadeFeedbackController(
    context: Context,
    settings: SettingsState,
) : ArcadeFeedback {
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 45)
    private val vibrator: Vibrator? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(VibratorManager::class.java)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }

    private var settingsState = settings
    private var lastTapFeedbackAtMs = 0L
    private var lastPickupFeedbackAtMs = 0L

    fun updateSettings(settings: SettingsState) {
        settingsState = settings
        syncMusicPreference()
    }

    override fun play(event: ArcadeFeedbackEvent) {
        if (isThrottled(event)) return
        playTone(event)
        vibrate(event)
    }

    fun release() {
        toneGenerator.release()
    }

    private fun syncMusicPreference() {
        if (!settingsState.musicEnabled) {
            toneGenerator.stopTone()
        }
    }

    private fun isThrottled(event: ArcadeFeedbackEvent): Boolean {
        val now = SystemClock.elapsedRealtime()
        return when (event) {
            ArcadeFeedbackEvent.TAP -> {
                if (now - lastTapFeedbackAtMs < 24L) true else {
                    lastTapFeedbackAtMs = now
                    false
                }
            }
            ArcadeFeedbackEvent.PICKUP -> {
                if (now - lastPickupFeedbackAtMs < 32L) true else {
                    lastPickupFeedbackAtMs = now
                    false
                }
            }
            else -> false
        }
    }

    private fun playTone(event: ArcadeFeedbackEvent) {
        if (!settingsState.soundEnabled) return
        val (tone, durationMs) = when (event) {
            ArcadeFeedbackEvent.TAP -> ToneGenerator.TONE_PROP_BEEP to 15
            ArcadeFeedbackEvent.SUCCESS -> ToneGenerator.TONE_PROP_ACK to 35
            ArcadeFeedbackEvent.FAIL -> ToneGenerator.TONE_PROP_NACK to 60
            ArcadeFeedbackEvent.PICKUP -> ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD to 25
            ArcadeFeedbackEvent.LINE_CLEAR -> ToneGenerator.TONE_PROP_BEEP2 to 50
        }
        toneGenerator.startTone(tone, durationMs)
    }

    private fun vibrate(event: ArcadeFeedbackEvent) {
        if (!settingsState.vibrationEnabled) return
        val vibrator = vibrator ?: return
        if (!vibrator.hasVibrator()) return
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val effectId = when (event) {
                    ArcadeFeedbackEvent.TAP -> VibrationEffect.EFFECT_TICK
                    ArcadeFeedbackEvent.SUCCESS -> VibrationEffect.EFFECT_CLICK
                    ArcadeFeedbackEvent.FAIL -> VibrationEffect.EFFECT_HEAVY_CLICK
                    ArcadeFeedbackEvent.PICKUP -> VibrationEffect.EFFECT_TICK
                    ArcadeFeedbackEvent.LINE_CLEAR -> VibrationEffect.EFFECT_DOUBLE_CLICK
                }
                vibrator.vibrate(VibrationEffect.createPredefined(effectId))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val (durationMs, amplitude) = when (event) {
                    ArcadeFeedbackEvent.TAP -> 15L to 80
                    ArcadeFeedbackEvent.SUCCESS -> 25L to 180
                    ArcadeFeedbackEvent.FAIL -> 50L to 255
                    ArcadeFeedbackEvent.PICKUP -> 20L to 140
                    ArcadeFeedbackEvent.LINE_CLEAR -> 35L to 200
                }
                vibrator.vibrate(VibrationEffect.createOneShot(durationMs, amplitude))
            } else {
                @Suppress("DEPRECATION")
                val durationMs = when (event) {
                    ArcadeFeedbackEvent.TAP -> 15L
                    ArcadeFeedbackEvent.SUCCESS -> 25L
                    ArcadeFeedbackEvent.FAIL -> 50L
                    ArcadeFeedbackEvent.PICKUP -> 20L
                    ArcadeFeedbackEvent.LINE_CLEAR -> 35L
                }
                vibrator.vibrate(durationMs)
            }
        } catch (_: SecurityException) {
            // Ignore missing manifest/device permission issues and keep gameplay responsive.
        } catch (_: RemoteException) {
            // Ignore binder failures from vendor vibrator services.
        }
    }
}

@Composable
fun rememberArcadeFeedback(
    context: Context,
    settings: SettingsState,
): ArcadeFeedback {
    val controller = remember(context) { ArcadeFeedbackController(context, settings) }
    SideEffect {
        controller.updateSettings(settings)
    }
    DisposableEffect(controller) {
        onDispose { controller.release() }
    }
    return controller
}
