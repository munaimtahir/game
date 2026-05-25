#!/usr/bin/env bash
set -e
GAME_TARGET=$1
TEST_LEVEL=$2

echo "Running ADB Smoke Test for Game Target: $GAME_TARGET, Level: $TEST_LEVEL"
PACKAGE="com.vexel.offlinearcade"
ACTIVITY="com.vexel.offlinearcade.MainActivity"

echo "Finding and installing APK..."
if [ ! -d "app/build/outputs/apk/debug" ]; then
    echo "Debug APK directory does not exist. Did the build fail?"
    exit 1
fi
APK_PATH=$(find app/build/outputs/apk/debug -name '*debug*.apk' | head -n 1)
if [ -z "$APK_PATH" ]; then
    echo "Could not find debug APK."
    exit 1
fi
adb install -r "$APK_PATH"

echo "Clearing logcat..."
adb logcat -c

echo "Launching app..."
adb shell am start -W -n "$PACKAGE/$ACTIVITY"
sleep 5

echo "Checking crash logs..."
if adb logcat -d | grep -iE "fatal exception"; then
    echo "Crash detected!"
    exit 1
fi

echo "Smoke test complete."
