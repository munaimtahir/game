# Baseline Repo Audit

Date: 2026-05-26

## Repo Shape
- Root project name: `OfflineMiniArcade`
- Build system: Gradle Kotlin DSL
- Top-level modules: `:app`, `:core:common`, `:core:data`, `:core:model`, `:core:ui`, `:feature:home`, `:feature:challenges`, `:feature:stats`, `:feature:settings`, and game modules including `:game:pulseorbit`, `:game:lanedrift`, `:game:stackdrop`, `:game:brickvolley`, `:game:loopsnake`, `:game:shielddash`, `:game:gravityflip`
- App module: `app`

## Toolchain
- Android Gradle Plugin: `8.5.2`
- Kotlin: `1.9.24`
- Compose compiler extension: `1.5.14`
- Compose BOM: `2024.06.00`
- Java/JDK requirement: `17`
- Gradle wrapper: `8.7`

## Android SDK Levels
- `compileSdk = 35`
- `targetSdk = 35`
- `minSdk = 24`

## Current App Architecture
- Entry point: [`MainActivity`](../../../app/src/main/java/com/vexel/offlinearcade/MainActivity.kt)
- App shell: [`ArcadeApp`](../../../app/src/main/java/com/vexel/offlinearcade/ArcadeApp.kt)
- Navigation graph: [`ArcadeNavHost`](../../../app/src/main/java/com/vexel/offlinearcade/ArcadeNavHost.kt)
- Route constants: [`ArcadeRoutes.kt`](../../../app/src/main/java/com/vexel/offlinearcade/ArcadeRoutes.kt)
- View-model layer: [`ArcadeViewModel`](../../../app/src/main/java/com/vexel/offlinearcade/ArcadeViewModel.kt)
- Offline persistence: [`OfflineArcadeRepository`](../../../core/data/src/main/java/com/vexel/offlinearcade/core/data/OfflineArcadeRepository.kt)

## Locked MVP Game Routes
- Pulse Orbit: `Routes.PulseOrbitDetail` -> `Routes.PulseOrbitGame`
- Lane Drift: `Routes.LaneDriftDetail` -> `Routes.LaneDriftGame`
- Stack Drop: `Routes.StackDropDetail` -> `Routes.StackDropGame`

## Gameplay State Architecture
- `PulseOrbitScreen` keeps local Compose state for orbit angle, gap size, combo, score, pause, and game over.
- `LaneDriftScreen` keeps local Compose state for lane, items, speed, spawn cadence, pickups, and collision/game over.
- `StackDropEngine` models board state, active piece, next piece, scoring, line clears, and game over.
- Shared progression is stored through `ArcadeSnapshot`, `GameStats`, `PlayerProfile`, and `DailyChallenge` in the data layer.

## Collision / Math Hotspots
- Lane Drift collision logic: [`LaneDriftCollision.kt`](../../../game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftCollision.kt)
- Lane Drift update loop and rendering: [`LaneDriftScreen.kt`](../../../game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftScreen.kt)
- Pulse Orbit timing and fairness: [`PulseOrbitScreen.kt`](../../../game/pulseorbit/src/main/java/com/vexel/offlinearcade/game/pulseorbit/PulseOrbitScreen.kt)
- Stack Drop board math: [`StackDropEngine.kt`](../../../game/stackdrop/src/main/java/com/vexel/offlinearcade/game/stackdrop/StackDropEngine.kt)
- Loopsnake test hang source: [`LoopSnakeEngineTest.kt`](../../../game/loopsnake/src/test/java/com/vexel/offlinearcade/game/loopsnake/engine/LoopSnakeEngineTest.kt)

## Build / Test Commands Run
| Command | Result | Notes |
| --- | --- | --- |
| `./gradlew clean assembleDebug` | PASS | Build succeeded. AGP warned that 8.5.2 is tested up to `compileSdk 34`. |
| `./gradlew testDebugUnitTest` | PASS | Initially hung on `LoopSnakeEngineTest.testSelfCollisionTriggersGameOver`; after making the test deterministic, the full suite passed. |
| `./gradlew lintDebug` | PASS | Completed successfully. |
| `./gradlew :game:loopsnake:testDebugUnitTest` | PASS | Used to verify the loopsnake hang fix. |
| `adb devices -l` | PASS | Physical device detected: `08357252AE006901` (`TECNO_CH6i`). Not used as the primary CI path. |

## Warnings Observed
- AGP 8.5.2 emits a compileSdk 35 compatibility warning.
- Several Compose screens still have unused parameter warnings.
- Some screens still use deprecated `Icons.Default.ArrowBack` instead of `AutoMirrored`.

## Existing CI / Test Surface
- GitHub workflow: [android-emulator-gameplay-ci.yml](../../../.github/workflows/android-emulator-gameplay-ci.yml)
- Android instrumentation tests exist in `app/src/androidTest`
- JVM tests exist in `app/src/test`, `core/data/src/test`, `core/ui/src/test`, and each game module
- Device / adb scripts exist under `scripts/` and `scripts/ci/`

## Existing ADB / Screenshot Scripts
- [`scripts/ci/run_emulator_tasks.sh`](../../../scripts/ci/run_emulator_tasks.sh)
- [`scripts/ci/run_adb_smoke.sh`](../../../scripts/ci/run_adb_smoke.sh)
- [`scripts/ci/capture_game_screenshots.sh`](../../../scripts/ci/capture_game_screenshots.sh)
- [`scripts/ci/wait_for_emulator.sh`](../../../scripts/ci/wait_for_emulator.sh)
- [`scripts/ci/collect_android_artifacts.sh`](../../../scripts/ci/collect_android_artifacts.sh)
- [`scripts/ci/print_android_env.sh`](../../../scripts/ci/print_android_env.sh)
- [`scripts/run_adb_device_suite.sh`](../../../scripts/run_adb_device_suite.sh)
- [`scripts/adb_screenshot_smoke.sh`](../../../scripts/adb_screenshot_smoke.sh)

## Existing Workflow Files
- [`android-emulator-gameplay-ci.yml`](../../../.github/workflows/android-emulator-gameplay-ci.yml)
- `gemini-*.yml` automation workflows are present but unrelated to game CI.

## Known Crash-Prone / Flaky Areas
- `LoopSnakeEngineTest.testSelfCollisionTriggersGameOver` was previously an unbounded while loop and caused `testDebugUnitTest` to stall.
- `ArcadeApp` uses splash/test-environment branching, so any future test harness changes must keep test-mode behavior deterministic.
- Emulator runs can still be delayed by boot timing or KVM availability.

## TODO / FIXME Scan
- No gameplay-relevant `TODO`, `FIXME`, or `HACK` markers were found in the source tree during this audit.

## Baseline Takeaway
- The repo is structurally healthy and builds/tests/lints successfully.
- The main reliability issue found in this pass was a non-deterministic loopsnake unit test, which is now fixed locally.
- Lane Drift remains the next gameplay target for focused tuning once emulator evidence is flowing cleanly through GitHub Actions.
