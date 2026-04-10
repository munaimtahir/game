package com.vexel.offlinearcade.core.model

enum class ArcadeFeedbackEvent {
    TAP,
    SUCCESS,
    FAIL,
    PICKUP,
    LINE_CLEAR,
}

interface ArcadeFeedback {
    fun play(event: ArcadeFeedbackEvent)
}
