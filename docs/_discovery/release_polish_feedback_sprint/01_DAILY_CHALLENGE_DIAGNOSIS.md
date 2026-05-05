# Daily Challenge Bug Diagnosis
**Date**: May 2026

## Exact Root Cause
The root cause lies in `DailyChallengeGenerator.kt`. The random number generator `Random(epochDay * 31_337L)` is invoked inline within the `DailyChallenge` constructor arguments for both the `description` string and the `targetValue` / `rewardCoins`. 

Because Kotlin evaluates function arguments from left to right, the `random.nextInt()` inside the `description` string interpolation consumes the next random integer, and then the assignment to `targetValue` calls `random.nextInt()` again. This causes the `targetValue` used by the system to track progress to be *different* from the value displayed to the user in the challenge description. 

For example, the description might say "Collect 10 pickups in Lane Drift", but the `targetValue` used by the database and UI progress bar might be `6`. This creates the perception that the challenge bar is broken (e.g. completing at 6/10 visually but finishing, or only being halfway full when the stated goal is reached).

## Why Pulse Orbit might appear to work
Pulse Orbit uses `ChallengeMetric.PULSE_SCORE`. Scores in Pulse Orbit scale higher (e.g., 10-20 per run) and are cumulative. The user is highly likely to overshoot both the stated target and the actual `targetValue` simultaneously, hiding the mismatch. Lane Drift and Stack Drop rely on lower, slower-scaling values (`LANE_PICKUPS` and `STACK_LINES`), where discrepancies of 2-3 units are very noticeable and visibly break the UI's progress representation.

## Affected Files
- `core/data/src/main/java/com/vexel/offlinearcade/core/data/ChallengeGenerator.kt`

## Proposed Minimal Fix
Extract the `targetValue` and `rewardCoins` into local variables *before* instantiating the `DailyChallenge` objects. This guarantees that the `description` string and the `targetValue` property use the exact same integer value. 

## Regression Risks
- This fix changes the generated daily challenges for any given `epochDay`. However, `mergeChallenges` handles mismatches gracefully by using the `challengeId` as the key. Existing progress for the current day might show a sudden jump in completion percentage, which is an acceptable one-time side-effect.
- There are no structural database changes, so persistence layers are safe.