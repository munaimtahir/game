# Copilot Session: Brick Volley Finalization

## 1. Current repo understanding
- Multi-module Android app (`app`, `core/*`, `feature/*`, `game/*`) using Kotlin + Jetpack Compose.
- Existing playable games: Pulse Orbit, Lane Drift, Stack Drop, Brick Volley (+ other in-progress game modules).
- Shared progression/high-score and run tracking handled through `RunResult` + `ArcadeViewModel.recordRun`.

## 2. Existing game architecture summary
- Home route opens per-game detail, then game route (`ArcadeNavHost`).
- Mature games use `ArcadeTestTags` for device automation and semantics state.
- Games report completion via `onRunComplete(RunResult)` into shared stats/progression.

## 3. Current Brick Volley implementation status
- Reworked from fragile prototype to a stable loop with:
  - drag threshold, clamped angle, launcher-anchored aiming
  - deterministic row spawning pattern and danger-line game-over
  - fixed bounce/collision checks and turn progression
  - run completion reporting (`GameId.BRICK_VOLLEY`) integrated in nav flow
- Added test tags/constants and dedicated Android/device test coverage.

## 4. Serious flaws discovered
- Tiny drags/taps launching unintended shots.
- Aim direction ambiguity and shallow-angle traps.
- Hardcoded render sizes mismatching collision math.
- Prototype route lacked run-complete integration to shared stats.
- Missing stable tags/automation path for Brick Volley device validation.

## 5. Files expected/actually changed
- `game/brickvolley/src/main/java/.../BrickVolleyScreen.kt`
- `game/brickvolley/src/main/java/.../BrickVolleyDetailScreen.kt`
- `game/brickvolley/src/main/java/.../engine/BrickVolleyEngine.kt`
- `game/brickvolley/src/test/.../BrickVolleyEngineTest.kt`
- `game/brickvolley/build.gradle.kts`
- `core/ui/src/main/java/.../ArcadeTestTags.kt`
- `feature/home/src/main/java/.../HomeScreen.kt`
- `app/src/main/java/.../ArcadeNavHost.kt`
- `app/src/androidTest/java/.../BrickVolleyDeviceSmokeTest.kt`
- `e2e/brick-volley/run-brick-volley-device-smoke.sh`
- `e2e/brick-volley/run-brick-volley-playability.sh`
- `docs/_implementation/brick_volley_finalization/*`

## 6. Implementation checklist
- [x] Discovery + baseline audit
- [x] Gameplay defect audit
- [x] Brick Volley gameplay loop repair
- [x] Integration into shared run result flow
- [x] Unit tests expanded for Brick Volley engine logic
- [x] Compose/device test added for Brick Volley route/interaction
- [x] ADB smoke script created and passing
- [x] ADB playability script created and passing
- [x] Full build/unit/lint regression passing

## 7. Device/ADB testing checklist
- [x] Device detection and explicit serial use (`08357252AE006901`)
- [x] Build/install debug APK
- [x] Wake/unlock + animation scaling adjustments
- [x] Launch app, open Brick Volley, launch run, execute gestures
- [x] Capture screenshots, logcat, UI dump
- [x] Verify Pulse Orbit, Lane Drift, Stack Drop open in playability run

## 8. Test commands
- `./gradlew assembleDebug testDebugUnitTest lintDebug --no-daemon --max-workers=1 --console=plain`
- `./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.vexel.offlinearcade.BrickVolleyDeviceSmokeTest --no-daemon --max-workers=1 --console=plain`
- `ANDROID_SERIAL=08357252AE006901 bash e2e/brick-volley/run-brick-volley-device-smoke.sh`
- `ANDROID_SERIAL=08357252AE006901 bash e2e/brick-volley/run-brick-volley-playability.sh`

## 9. Risk list
- Compose UI dumps can vary by device state/scroll position (handled with scroll-aware lookups in scripts).
- Global legacy instrumentation suite still contains unrelated flaky tests (Brick Volley-specific suite now isolated and passing).

## 10. Progress log
- [DONE] Baseline build/device audit created.
- [DONE] Brick Volley defect audit documented.
- [DONE] Engine + screen logic repaired and made deterministic enough for testing.
- [DONE] Shared run-result integration wired for Brick Volley game route.
- [DONE] Unit tests expanded to cover initial state, launch gating, angle clamp, movement, collision, scoring, turn advance, game over, restart/high-score helper.
- [DONE] Brick Volley Android instrumentation test class added and passing on device.
- [DONE] Brick Volley smoke and playability ADB scripts created and passing with artifacts.
- [DONE] Final regression (`assembleDebug`, `testDebugUnitTest`, `lintDebug`) passing.
