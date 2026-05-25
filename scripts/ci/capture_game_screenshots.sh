#!/usr/bin/env bash
set -e
GAME_TARGET=$1

echo "Capturing screenshots for Target: $GAME_TARGET"
SCREENSHOT_DIR="artifacts/screenshots"
mkdir -p "$SCREENSHOT_DIR"

# Wait for potential app launch
sleep 3
adb exec-out screencap -p > "$SCREENSHOT_DIR/01_app_state.png" || true

# Simple generic taps to try opening games if possible, then capture
adb shell input tap 500 1000 || true
sleep 2
adb exec-out screencap -p > "$SCREENSHOT_DIR/02_game_state_1.png" || true

adb shell input tap 500 1500 || true
sleep 2
adb exec-out screencap -p > "$SCREENSHOT_DIR/03_game_state_2.png" || true

echo "Screenshots captured to $SCREENSHOT_DIR"
