# Testing Gap Analysis

Date: 2026-05-26

## What Passes Now
- `./gradlew clean assembleDebug` passes.
- `./gradlew testDebugUnitTest` passes after making the loopsnake self-collision test deterministic.
- `./gradlew lintDebug` passes.

## Remaining Gaps
1. Emulator evidence still needs to come from GitHub Actions runs, not just local shell execution.
2. Screenshot capture is still more generic than the requested per-game state matrix.
3. `LaneDrift` still lacks a dedicated deterministic debug mode for exact screenshot states.
4. The repo does not yet have golden-image regression coverage for the three MVP games.
5. `connectedAndroidTest` coverage exists, but there is still room to make failure output more targeted per game target.

## Existing Automated Coverage
- JVM tests exist in:
  - `app/src/test`
  - `core/data/src/test`
  - `core/ui/src/test`
  - each game module's `src/test`
- Instrumentation tests exist in:
  - `app/src/androidTest`
- Device helpers exist in:
  - [`DeviceTestHelpers.kt`](../../../app/src/androidTest/java/com/vexel/offlinearcade/DeviceTestHelpers.kt)

## Gaps by Game
### Lane Drift
- Good: collision math is already isolated and unit-tested.
- Missing: a deterministic debug mode for screenshot state capture and repeatable gameplay-state evidence.

### Pulse Orbit
- Good: timing logic already has direct unit tests.
- Missing: explicit emulator-state capture for ready / active / game-over scenarios.

### Stack Drop
- Good: board logic is already an isolated engine.
- Missing: more exact per-state visual evidence and any snapshot-style coverage for line-clear feedback.

## Next Testing Step
- Make the GitHub Actions workflow the primary evidence loop for one game at a time, starting with Lane Drift.
