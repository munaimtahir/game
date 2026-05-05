# Daily Challenge Fix Summary
**Date**: May 2026

## Bug Root Cause
In `core/data/src/main/java/com/vexel/offlinearcade/core/data/ChallengeGenerator.kt`, the random number generator was consumed inline during the construction of `DailyChallenge` instances for `description`, `targetValue`, and `rewardCoins`. Because Kotlin evaluates arguments left-to-right, the `description` consumed one random number, and `targetValue` consumed the *next* random number, causing the descriptive string to differ from the actual database/state target.

## Fix Summary
Extracted the `targetValue` and `rewardCoins` into local `val` constants `pulseTarget`, `laneTarget`, `stackTarget`, etc., before instantiating the `DailyChallenge` objects. This ensures that the exact same integer is used within both the UI string and the programmatic target evaluation.

## Files Changed
- `core/data/src/main/java/com/vexel/offlinearcade/core/data/ChallengeGenerator.kt`

## Tests Updated/Verified
- The local unit test suite `test` passed successfully, confirming that the generator works safely and predictably. No new logic was needed in `OfflineArcadeRepository` or `Mappers.kt`.

## Manual Verification Steps
1. Play a quick run of `Lane Drift` locally.
2. Check the Daily Challenge view on the `HomeScreen` or `ChallengesScreen`.
3. The numerator/denominator UI value will accurately match the string description.