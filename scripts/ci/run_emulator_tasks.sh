#!/usr/bin/env bash
set -e

# Disable animations
adb devices
adb shell settings put global window_animation_scale 0 || true
adb shell settings put global transition_animation_scale 0 || true
adb shell settings put global animator_duration_scale 0 || true

# Run connected tests if not screenshots_only
if [ "${TEST_LEVEL}" != "screenshots_only" ]; then
    echo "Running connectedAndroidTest..."
    ./gradlew connectedAndroidTest --stacktrace | tee artifacts/logs/connectedAndroidTest.txt || true
fi

# Run ADB smoke script
if [ -x scripts/ci/run_adb_smoke.sh ]; then
    echo "Running run_adb_smoke.sh..."
    scripts/ci/run_adb_smoke.sh "${GAME_TARGET}" "${TEST_LEVEL}" | tee artifacts/gameplay-smoke/adb-smoke.txt
fi

# Capture screenshots
if [ -x scripts/ci/capture_game_screenshots.sh ]; then
    echo "Running capture_game_screenshots.sh..."
    scripts/ci/capture_game_screenshots.sh "${GAME_TARGET}" | tee artifacts/logs/screenshot-capture.txt
fi

# Final diagnostics
adb logcat -d > artifacts/logcat/logcat-final.txt || true
adb shell uiautomator dump /sdcard/window_dump.xml || true
adb pull /sdcard/window_dump.xml artifacts/ui-dumps/window_dump.xml || true
