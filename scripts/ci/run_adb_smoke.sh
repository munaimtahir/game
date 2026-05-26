#!/usr/bin/env bash
set -euo pipefail

GAME_TARGET="${1:-all}"
TEST_LEVEL="${2:-smoke}"
PACKAGE="com.vexel.offlinearcade"
ACTIVITY="com.vexel.offlinearcade.MainActivity"
ARTIFACT_DIR="artifacts/gameplay-smoke"

mkdir -p "$ARTIFACT_DIR"

echo "Running ADB smoke test for game target: $GAME_TARGET, level: $TEST_LEVEL"

if [[ ! -d "app/build/outputs/apk/debug" ]]; then
    echo "ERROR: debug APK directory does not exist. Did the build fail?" >&2
    exit 1
fi

APK_PATH="$(find app/build/outputs/apk/debug -name '*debug*.apk' | head -n 1)"
if [[ -z "$APK_PATH" ]]; then
    echo "ERROR: could not find debug APK." >&2
    exit 1
fi

echo "Installing APK: $APK_PATH"
adb install -r "$APK_PATH"

echo "Clearing logcat..."
adb logcat -c

echo "Launching app..."
adb shell am start -W -n "$PACKAGE/$ACTIVITY" > "$ARTIFACT_DIR/am-start.txt" 2>&1
sleep 5

echo "Capturing current focus..."
adb shell dumpsys window windows | grep -E 'mCurrentFocus|mFocusedApp' > "$ARTIFACT_DIR/window-focus.txt" || true

echo "Checking crash and ANR logs..."
if adb logcat -d | tee "$ARTIFACT_DIR/logcat-check.txt" | grep -iE "fatal exception|app not responding|ANR in|Process crashed"; then
    echo "ERROR: crash or ANR detected." >&2
    exit 1
fi

echo "Smoke test complete."
