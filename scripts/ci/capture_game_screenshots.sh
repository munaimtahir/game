#!/usr/bin/env bash
set -euo pipefail

GAME_TARGET="${1:-all}"
PACKAGE="com.vexel.offlinearcade"
ACTIVITY="com.vexel.offlinearcade.MainActivity"
SCREENSHOT_DIR="artifacts/screenshots"
LOG_DIR="artifacts/logs"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/adb_ui_helpers.sh"

mkdir -p "$SCREENSHOT_DIR" "$LOG_DIR"

capture_screen() {
    local name="$1"
    adb exec-out screencap -p > "$SCREENSHOT_DIR/$name" || true
}

device_center() {
    local size
    size="$(adb shell wm size | tr -d '\r')"
    local physical
    physical="$(printf '%s\n' "$size" | sed -n 's/.*Physical size: \([0-9]\+\)x\([0-9]\+\).*/\1 \2/p')"
    if [[ -z "$physical" ]]; then
        echo "540 1200"
        return 0
    fi
    read -r width height <<< "$physical"
    echo "$((width / 2)) $((height / 2))"
}

tap_center() {
    read -r x y <<< "$(device_center)"
    adb shell input tap "$x" "$y"
}

start_and_capture_lane_drift() {
    ui_tap_text "Lane Drift"
    ui_wait_for_text "Start Game" 20
    capture_screen "10_lane_drift_detail.png"
    ui_tap_text "Start Game"
    sleep 1
    capture_screen "11_lane_drift_ready.png"
    sleep 2
    capture_screen "12_lane_drift_active.png"
    if ui_wait_for_any_text 30 "Retry instantly" "Run complete" >/dev/null; then
        capture_screen "13_lane_drift_game_over.png"
        ui_tap_text "Back to detail" || true
    fi
}

start_and_capture_pulse_orbit() {
    ui_tap_text "Pulse Orbit"
    ui_wait_for_text "Start Game" 20
    capture_screen "20_pulse_orbit_detail.png"
    ui_tap_text "Start Game"
    sleep 1
    capture_screen "21_pulse_orbit_ready.png"
    sleep 1
    capture_screen "22_pulse_orbit_active.png"
    tap_center
    if ui_wait_for_any_text 12 "Retry instantly" "Run complete" >/dev/null; then
        capture_screen "23_pulse_orbit_game_over.png"
        ui_tap_text "Back to detail" || true
    fi
}

start_and_capture_stack_drop() {
    ui_tap_text "Stack Drop"
    ui_wait_for_text "Start Game" 20
    capture_screen "30_stack_drop_detail.png"
    ui_tap_text "Start Game"
    sleep 1
    capture_screen "31_stack_drop_ready.png"
    sleep 2
    capture_screen "32_stack_drop_active.png"

    local iterations=0
    while [[ "$iterations" -lt 24 ]]; do
        ui_tap_text "▼" || true
        sleep 0.20
        if ui_wait_for_any_text 1 "Retry instantly" "Run complete" >/dev/null; then
            break
        fi
        iterations=$((iterations + 1))
    done

    if ui_wait_for_any_text 20 "Retry instantly" "Run complete" >/dev/null; then
        capture_screen "33_stack_drop_game_over.png"
        ui_tap_text "Back to detail" || true
    fi
}

echo "Capturing screenshots for target: $GAME_TARGET"

adb shell am start -W -n "$PACKAGE/$ACTIVITY" > "$LOG_DIR/capture-launch.txt" 2>&1
ui_wait_for_any_text 30 "Lane Drift" "Pulse Orbit" "Stack Drop" >/dev/null
capture_screen "01_home.png"

case "$GAME_TARGET" in
    all)
        start_and_capture_lane_drift
        ui_return_to_home 4 || true
        start_and_capture_pulse_orbit
        ui_return_to_home 4 || true
        start_and_capture_stack_drop
        ui_return_to_home 4 || true
        ;;
    lane_drift)
        start_and_capture_lane_drift
        ui_return_to_home 4 || true
        ;;
    pulse_orbit)
        start_and_capture_pulse_orbit
        ui_return_to_home 4 || true
        ;;
    stack_drop)
        start_and_capture_stack_drop
        ui_return_to_home 4 || true
        ;;
    *)
        echo "Unknown game target: $GAME_TARGET" >&2
        exit 1
        ;;
esac

echo "Screenshots captured to $SCREENSHOT_DIR"
