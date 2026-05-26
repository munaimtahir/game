#!/usr/bin/env bash
set -euo pipefail

GAME_TARGET="${1:-all}"
TEST_LEVEL="${2:-smoke}"
PACKAGE="com.vexel.offlinearcade"
ACTIVITY="com.vexel.offlinearcade.MainActivity"
ARTIFACT_DIR="artifacts/gameplay-smoke"
LOG_DIR="artifacts/logs"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/adb_ui_helpers.sh"

mkdir -p "$ARTIFACT_DIR" "$LOG_DIR"

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

ui_wait_for_any_text 30 "Lane Drift" "Pulse Orbit" "Stack Drop" >/dev/null
ui_dump_current "$ARTIFACT_DIR/home-ui.xml" >/dev/null

verify_game_flow() {
    local game_title="$1"
    local detail_label="$2"
    local game_slug
    game_slug="$(printf '%s' "$game_title" | tr '[:upper:] ' '[:lower:]_')"

    echo "Verifying flow for $game_title"
    ui_tap_text "$game_title"
    ui_wait_for_text "$detail_label" 20
    ui_dump_current "$ARTIFACT_DIR/${game_slug}-detail-ui.xml" >/dev/null

    ui_tap_text "Start Game"
    ui_wait_for_text "Pause" 15
    ui_dump_current "$ARTIFACT_DIR/${game_slug}-active-ui.xml" >/dev/null

    ui_tap_text "Pause"
    ui_wait_for_text "Resume" 10

    ui_tap_text "Quit"
    ui_wait_for_text "$detail_label" 15

    ui_tap_text "Start Game"
    ui_wait_for_text "Pause" 15
    ui_tap_text "Pause"
    ui_wait_for_text "Resume" 10

    ui_tap_text "Quit"
    ui_wait_for_text "$detail_label" 15

    ui_return_to_home 4 || true
}

case "$GAME_TARGET" in
    all)
        verify_game_flow "Pulse Orbit" "Game Info"
        verify_game_flow "Lane Drift" "Game Info"
        verify_game_flow "Stack Drop" "Game Info"
        ;;
    lane_drift)
        verify_game_flow "Lane Drift" "Game Info"
        ;;
    pulse_orbit)
        verify_game_flow "Pulse Orbit" "Game Info"
        ;;
    stack_drop)
        verify_game_flow "Stack Drop" "Game Info"
        ;;
    *)
        echo "Unknown game target: $GAME_TARGET" >&2
        exit 1
        ;;
esac

echo "Capturing current focus..."
adb shell dumpsys window windows | grep -E 'mCurrentFocus|mFocusedApp' > "$ARTIFACT_DIR/window-focus.txt" || true

echo "Checking crash and ANR logs..."
if adb logcat -d | tee "$ARTIFACT_DIR/logcat-check.txt" | grep -iE "fatal exception|app not responding|ANR in|Process crashed"; then
    echo "ERROR: crash or ANR detected." >&2
    exit 1
fi

echo "Smoke test complete."
