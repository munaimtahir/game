package com.vexel.offlinearcade.game.pulseorbit

internal data class PulseOrbitState(
    val sessionId: String,
    val seed: Long,
    val playing: Boolean = false,
    val paused: Boolean = false,
    val orbitAngle: Float = -90f,
    val gapCenterAngle: Float = 0f,
    val gapSize: Float = PulseOrbitTuning.initialGapSize,
    val rotationSpeedDegPerSec: Float = PulseOrbitTuning.initialRotationSpeed,
    val passes: Int = 0,
    val perfectPasses: Int = 0,
    val score: Int = 0,
    val combo: Int = 0,
    val bestCombo: Int = 0,
    val runStartMillis: Long = 0L,
    val gameOver: Boolean = false,
    val feedback: String = "Tap to start",
    val lastResolvedTapAtMillis: Long = 0L,
)

internal enum class PulseOrbitTapResolution {
    NONE,
    CLEAN_PASS,
    PERFECT_PASS,
    FAIL,
}

internal data class PulseOrbitTapResult(
    val state: PulseOrbitState,
    val resolution: PulseOrbitTapResolution,
)

internal object PulseOrbitTuning {
    const val initialGapSize = 88f
    const val minimumGapSize = 40f
    const val gapShrinkPerPass = 1.2f
    const val initialRotationSpeed = 85f
    const val speedIncreasePerPass = 4.2f
    const val maxRotationSpeed = 230f
    const val gapStepBase = 72f
    const val gapStepPerPass = 3.5f
    const val maxGapStep = 140f
    const val comboBonusEvery = 5
    const val collisionToleranceDegrees = 4.5f
    const val perfectWindowDegrees = 6f
    const val tapCooldownMillis = 60L

    fun gapSizeFor(passes: Int): Float = maxOf(minimumGapSize, initialGapSize - passes * gapShrinkPerPass)
    fun rotationSpeedFor(passes: Int): Float = kotlin.math.min(maxRotationSpeed, initialRotationSpeed + passes * speedIncreasePerPass)
    fun gapStepFor(passes: Int): Float = kotlin.math.min(maxGapStep, gapStepBase + passes * gapStepPerPass)
}

internal fun createPulseOrbitReadyState(
    sessionId: String,
    seed: Long,
): PulseOrbitState {
    return PulseOrbitState(
        sessionId = sessionId,
        seed = seed,
    )
}

internal fun startPulseOrbitRun(
    state: PulseOrbitState,
    startedAtMillis: Long,
): PulseOrbitState {
    return state.copy(
        playing = true,
        paused = false,
        runStartMillis = startedAtMillis,
        feedback = "Thread the gap.",
        lastResolvedTapAtMillis = 0L,
    )
}

internal fun pausePulseOrbitRun(state: PulseOrbitState): PulseOrbitState {
    if (!state.playing || state.gameOver) return state
    return state.copy(
        playing = false,
        paused = true,
    )
}

internal fun resumePulseOrbitRun(state: PulseOrbitState): PulseOrbitState {
    if (!state.paused || state.gameOver) return state
    return state.copy(
        playing = true,
        paused = false,
    )
}

internal fun advancePulseOrbitState(
    state: PulseOrbitState,
    deltaSeconds: Float,
): PulseOrbitState {
    if (!state.playing || state.paused || state.gameOver || deltaSeconds <= 0f) {
        return state
    }
    return state.copy(
        orbitAngle = (state.orbitAngle + state.rotationSpeedDegPerSec * deltaSeconds).normalizeAngle(),
    )
}

internal fun resolvePulseOrbitTap(
    state: PulseOrbitState,
    tapAtMillis: Long,
): PulseOrbitTapResult {
    if (!state.playing || state.paused || state.gameOver) {
        return PulseOrbitTapResult(state, PulseOrbitTapResolution.NONE)
    }
    if (state.lastResolvedTapAtMillis > 0L && tapAtMillis - state.lastResolvedTapAtMillis < PulseOrbitTuning.tapCooldownMillis) {
        return PulseOrbitTapResult(state, PulseOrbitTapResolution.NONE)
    }

    val distance = angularDistance(state.orbitAngle, state.gapCenterAngle)
    val fairThreshold = (state.gapSize / 2f) + PulseOrbitTuning.collisionToleranceDegrees
    if (distance > fairThreshold) {
        return PulseOrbitTapResult(
            state.copy(
                playing = false,
                combo = 0,
                gameOver = true,
                feedback = "Missed the gap.",
                lastResolvedTapAtMillis = tapAtMillis,
            ),
            PulseOrbitTapResolution.FAIL,
        )
    }

    val nextPass = state.passes + 1
    val perfect = distance <= PulseOrbitTuning.perfectWindowDegrees
    val nextCombo = if (perfect) state.combo + 1 else 0
    val comboBonus = if (perfect && nextCombo > 0 && nextCombo % PulseOrbitTuning.comboBonusEvery == 0) 1 else 0
    val scoreDelta = if (perfect) 2 else 1
    val step = PulseOrbitTuning.gapStepFor(nextPass)
    val direction = gapDirectionFor(state.seed, nextPass)

    return PulseOrbitTapResult(
        state.copy(
            passes = nextPass,
            perfectPasses = state.perfectPasses + if (perfect) 1 else 0,
            score = state.score + scoreDelta + comboBonus,
            combo = nextCombo,
            bestCombo = maxOf(state.bestCombo, nextCombo),
            gapCenterAngle = (state.gapCenterAngle + step * direction).normalizeAngle(),
            gapSize = PulseOrbitTuning.gapSizeFor(nextPass),
            rotationSpeedDegPerSec = PulseOrbitTuning.rotationSpeedFor(nextPass),
            feedback = when {
                perfect && comboBonus > 0 -> "Perfect chain. Tempo up."
                perfect -> "Perfect pass."
                else -> "Clean pass."
            },
            lastResolvedTapAtMillis = tapAtMillis,
        ),
        if (perfect) PulseOrbitTapResolution.PERFECT_PASS else PulseOrbitTapResolution.CLEAN_PASS,
    )
}

internal fun gapDirectionFor(
    seed: Long,
    passIndex: Int,
): Float {
    val parity = (seed + passIndex.toLong()) and 1L
    return if (parity == 0L) 1f else -1f
}

internal fun angularDistance(first: Float, second: Float): Float {
    val raw = kotlin.math.abs(first.normalizeAngle() - second.normalizeAngle())
    return kotlin.math.min(raw, 360f - raw)
}

internal fun Float.normalizeAngle(): Float {
    var normalized = this % 360f
    if (normalized < 0f) normalized += 360f
    return normalized
}
