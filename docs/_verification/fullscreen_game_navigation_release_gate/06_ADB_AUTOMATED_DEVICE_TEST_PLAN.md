# Automated ADB Device Testing Plan

This plan defines the automated validation steps to be executed immediately upon connecting a physical Android device or starting an emulator. These tests ensure the full-screen refactor behaves correctly under real Android runtime conditions.

## 1. Prerequisites
- ADB installed and in PATH.
- Physical Android device connected with USB Debugging enabled.
- One (and only one) device listed in `adb devices`.

## 2. Automated Test Suite Execution
The primary automated gate is the existing instrumentation test suite located in `app/src/androidTest`.

### Command
```bash
./gradlew connectedDebugAndroidTest
```

### Coverage Goals
- **NavigationSmokeTest:** Confirms that the `ArcadeNavHost` correctly transitions from the launcher (Home) to all 3 full-screen game routes and back.
- **BackNavigationTest:** (NEW) Specifically verifies the complex back-button logic (Pauses during play, returns Home from Pause/Ready).
- **LifecyclePauseTest:** (NEW) Confirms that the game state automatically moves to `paused=true` when the app is backgrounded.
- **GameplayDeviceSmokeTest:** 
  - Verifies "Start" buttons are clickable and visible in the new full-screen layouts.
  - Confirms Lane Drift still spawns traffic (verifying logic preservation).
  - Confirms Stack Drop gesture inputs (swipe/tap) still update the game board state.
- **SettingsPersistenceSmokeTest:** Ensures system-wide settings still apply to games.

## 3. ADB Artifact Capture (Post-Test)
Capture screenshots of each game in its new full-screen state to verify WindowInsets/safeDrawing logic.

```bash
# Pulse Orbit
adb shell am start -n com.vexel.offlinearcade/.MainActivity --es "route" "pulse_orbit"
sleep 2
adb shell screencap -p /sdcard/pulse_orbit_fs.png
adb pull /sdcard/pulse_orbit_fs.png .

# Lane Drift
adb shell am start -n com.vexel.offlinearcade/.MainActivity --es "route" "lane_drift"
sleep 2
adb shell screencap -p /sdcard/lane_drift_fs.png
adb pull /sdcard/lane_drift_fs.png .

# Stack Drop
adb shell am start -n com.vexel.offlinearcade/.MainActivity --es "route" "stack_drop"
sleep 2
adb shell screencap -p /sdcard/stack_drop_fs.png
adb pull /sdcard/stack_drop_fs.png .
```

## 4. Execution Protocol
This plan will be executed in a dedicated terminal session once `adb devices` returns a valid target. 
All failures in `connectedDebugAndroidTest` must be treated as **RELEASE BLOCKERS**.
