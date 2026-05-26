#!/usr/bin/env bash
set -euo pipefail

GAME_TARGET="${1:-all}"
SCREENSHOT_DIR="artifacts/screenshots"

mkdir -p "$SCREENSHOT_DIR"

echo "Capturing screenshots for target: $GAME_TARGET"

sleep 3
adb exec-out screencap -p > "$SCREENSHOT_DIR/01_home_or_current.png" || true

adb shell input tap 500 1000 || true
sleep 2
adb exec-out screencap -p > "$SCREENSHOT_DIR/02_post_tap.png" || true

adb shell input tap 500 1500 || true
sleep 2
adb exec-out screencap -p > "$SCREENSHOT_DIR/03_secondary_state.png" || true

echo "Screenshots captured to $SCREENSHOT_DIR"
