# Brick Volley Finalization

## 1. Current Repository Understanding
- Multi-module Android arcade app with a single-activity Compose shell.
- Shared progression uses `PlayerProfile`, `GameStats`, `DailyChallenge`, and run-result recording.
- Brick Volley now has deterministic engine logic, Compose gameplay, device tests, and ADB smoke/playability scripts.

## 2. Existing Game Architecture Summary
- Navigation lives in `app/src/main/java/com/vexel/offlinearcade/ArcadeNavHost.kt`.
- Home/library cards live in `feature/home`.
- Shared UI/test tags live in `core/ui`.
- Each game has detail + gameplay screens and can report runs back through shared progression hooks.

## 3. Current Brick Volley Implementation Status
- Gameplay is stable: ready state, drag-to-aim, upward launch, wall/brick collisions, turn advance, game over, and restart.
- Brick Volley is wired into home navigation and shared run-result reporting.
- The detail screen now exposes a visible Start Game action with accessibility semantics.

## 4. Serious Flaws Discovered
- Start Game was initially below the fold on the detail screen.
- ADB selectors used human-spaced labels instead of the real Compose accessibility strings.
- Smoke/playability scripts were misreading the UI tree and falsely reporting detail-page states.

## 5. Files Expected to Change
- `game/brickvolley/src/main/java/com/vexel/offlinearcade/game/brickvolley/BrickVolleyScreen.kt`
- `game/brickvolley/src/main/java/com/vexel/offlinearcade/game/brickvolley/BrickVolleyDetailScreen.kt`
- `game/brickvolley/src/main/java/com/vexel/offlinearcade/game/brickvolley/engine/BrickVolleyEngine.kt`
- `app/src/main/java/com/vexel/offlinearcade/ArcadeNavHost.kt`
- `core/ui/src/main/java/com/vexel/offlinearcade/core/ui/ArcadeTestTags.kt`
- `app/src/androidTest/java/com/vexel/offlinearcade/BrickVolleyDeviceSmokeTest.kt`
- `e2e/brick-volley/run-brick-volley-device-smoke.sh`
- `e2e/brick-volley/run-brick-volley-playability.sh`

## 6. Implementation Checklist
- [x] Fix Brick Volley gameplay logic.
- [x] Add deterministic unit-testable engine helpers.
- [x] Add/repair unit tests.
- [x] Add stable accessibility/test tags.
- [x] Fix detail-screen launch affordance.
- [x] Fix ADB scripts to use real accessibility strings.
- [x] Verify device smoke and playability.

## 7. Device/ADB Testing Checklist
- [x] Detect attached device serial.
- [x] Build/install APK.
- [x] Launch app and open Brick Volley.
- [x] Capture ready gameplay canvas.
- [x] Perform launch gesture.
- [x] Verify gameplay state changes and multi-turn flow.
- [x] Verify existing games still open.

## 8. Test Commands
- `./gradlew assembleDebug --no-daemon --console=plain`
- `./gradlew testDebugUnitTest --no-daemon --console=plain`
- `./gradlew lintDebug --no-daemon --console=plain`
- `ANDROID_SERIAL=08357252AE006901 bash e2e/brick-volley/run-brick-volley-device-smoke.sh`
- `ANDROID_SERIAL=08357252AE006901 bash e2e/brick-volley/run-brick-volley-playability.sh`

## 9. Risk List
- Device scripts depend on accessible semantics staying stable.
- Heavy combined Gradle runs can take a long time; split commands are more reliable.
- Additional gameplay tuning could still be added later, but the release criteria are now met.

## 10. Progress Log
- Baseline repo audit completed.
- Brick Volley gameplay, tests, and tags repaired.
- Device scripts hardened around exact accessibility nodes.
- Start Game moved above the fold and tagged for reliable device automation.
- Smoke and playability runs now pass on `08357252AE006901`.
