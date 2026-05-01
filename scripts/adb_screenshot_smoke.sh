#!/usr/bin/env bash
set -euo pipefail

PACKAGE_NAME="${PACKAGE_NAME:-com.vexel.offlinearcade}"
MAIN_ACTIVITY="${MAIN_ACTIVITY:-.MainActivity}"
SCREENSHOT_WAIT_SECONDS="${SCREENSHOT_WAIT_SECONDS:-2}"
SKIP_BUILD="${SKIP_BUILD:-0}"
SKIP_INSTALL="${SKIP_INSTALL:-0}"

TIMESTAMP="$(date +"%Y%m%d_%H%M%S")"
OUT_DIR="artifacts/adb_screenshots/${TIMESTAMP}"

mkdir -p "$OUT_DIR"

log() {
  echo "[$(date +"%H:%M:%S")] $*"
}

report() {
  echo "$*" >> "$OUT_DIR/REPORT.md"
}

adb_device_count() {
  adb devices | awk 'NR>1 && $2=="device" {count++} END {print count+0}'
}

take_screenshot() {
  local filename="$1"
  local label="$2"

  log "Capturing: $label"
  sleep "$SCREENSHOT_WAIT_SECONDS"
  adb exec-out screencap -p > "$OUT_DIR/$filename"
  report "- [$label](./$filename)"
}

start_app_normal() {
  log "Launching app normally"
  adb shell am force-stop "$PACKAGE_NAME" || true
  adb shell monkey -p "$PACKAGE_NAME" -c android.intent.category.LAUNCHER 1 >/dev/null
}

start_route() {
  local route="$1"
  local state="${2:-}"

  log "Launching route: $route ${state:+state=$state}"
  adb shell am force-stop "$PACKAGE_NAME" || true

  if [[ -n "$state" ]]; then
    adb shell am start \
      -n "${PACKAGE_NAME}/${MAIN_ACTIVITY}" \
      --es screenshot_route "$route" \
      --es screenshot_state "$state" >/dev/null
  else
    adb shell am start \
      -n "${PACKAGE_NAME}/${MAIN_ACTIVITY}" \
      --es screenshot_route "$route" >/dev/null
  fi
}

tap_percent() {
  local x_percent="$1"
  local y_percent="$2"

  local size
  size="$(adb shell wm size | tr -d '\r' | awk -F': ' '{print $2}')"

  local width height
  width="$(echo "$size" | cut -d'x' -f1)"
  height="$(echo "$size" | cut -d'x' -f2)"

  local x y
  x="$(awk "BEGIN {printf \"%d\", $width * $x_percent / 100}")"
  y="$(awk "BEGIN {printf \"%d\", $height * $y_percent / 100}")"

  adb shell input tap "$x" "$y"
}

find_debug_apk() {
  find . -path "*/build/outputs/apk/debug/*.apk" \
    ! -name "*androidTest*" \
    ! -name "*unaligned*" \
    | head -n 1
}

create_index_html() {
  local index="$OUT_DIR/index.html"

  cat > "$index" <<HTML
<!doctype html>
<html>
<head>
  <meta charset="utf-8">
  <title>Offline Mini Arcade Screenshot Smoke Test</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background: #07111E;
      color: #F8FAFC;
      padding: 24px;
    }
    .grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
      gap: 20px;
    }
    .card {
      background: #132033;
      border: 1px solid #2E4668;
      border-radius: 16px;
      padding: 12px;
    }
    img {
      width: 100%;
      border-radius: 12px;
      background: #000;
    }
    .name {
      margin-top: 8px;
      color: #D7DEE9;
      font-size: 14px;
    }
  </style>
</head>
<body>
  <h1>Offline Mini Arcade Screenshot Smoke Test</h1>
  <p>Generated: ${TIMESTAMP}</p>
  <div class="grid">
HTML

  for img in "$OUT_DIR"/*.png; do
    [[ -f "$img" ]] || continue
    local base
    base="$(basename "$img")"
    cat >> "$index" <<HTML
    <div class="card">
      <img src="./${base}" alt="${base}">
      <div class="name">${base}</div>
    </div>
HTML
  done

  cat >> "$index" <<HTML
  </div>
</body>
</html>
HTML
}

log "Starting local/GitHub ADB screenshot smoke test"
log "Package: $PACKAGE_NAME"
log "Main activity: $MAIN_ACTIVITY"
log "Output: $OUT_DIR"

cat > "$OUT_DIR/REPORT.md" <<MD
# ADB Screenshot Smoke Test Report

- Timestamp: $TIMESTAMP
- Package: $PACKAGE_NAME
- Main Activity: $MAIN_ACTIVITY

## Screenshots
MD

log "Checking connected ADB devices"
adb devices

DEVICE_COUNT="$(adb_device_count)"
if [[ "$DEVICE_COUNT" -lt 1 ]]; then
  log "ERROR: No connected ADB device/emulator found."
  exit 1
fi

if [[ "$DEVICE_COUNT" -gt 1 && -z "${ANDROID_SERIAL:-}" ]]; then
  log "ERROR: More than one device connected. Set ANDROID_SERIAL."
  adb devices
  exit 1
fi

DEVICE_SERIAL="$(adb get-serialno)"
ANDROID_VERSION="$(adb shell getprop ro.build.version.release | tr -d '\r')"

report ""
report "## Device"
report "- Serial: $DEVICE_SERIAL"
report "- Android version: $ANDROID_VERSION"
report ""

log "Disabling animations"
adb shell settings put global window_animation_scale 0 || true
adb shell settings put global transition_animation_scale 0 || true
adb shell settings put global animator_duration_scale 0 || true

if [[ "$SKIP_BUILD" != "1" ]]; then
  log "Building debug APK"
  ./gradlew assembleDebug
else
  log "Skipping build because SKIP_BUILD=1"
fi

APK_PATH="$(find_debug_apk || true)"

if [[ -z "$APK_PATH" ]]; then
  log "ERROR: Could not find debug APK."
  find . -path "*/build/outputs/apk/*" -type f || true
  exit 1
fi

log "APK found: $APK_PATH"
report "## APK"
report "- Path: $APK_PATH"
report ""

if [[ "$SKIP_INSTALL" != "1" ]]; then
  log "Installing APK"
  adb install -r "$APK_PATH"
else
  log "Skipping install because SKIP_INSTALL=1"
fi

# 01 launch
start_app_normal
take_screenshot "01_splash_or_launch.png" "Splash or launch screen"

# Route-based screenshots. These require debug screenshot_route support.
# If the app ignores extras, the script will still capture the visible app state.
start_route "home"
take_screenshot "02_home.png" "Home"

start_route "pulse_detail"
take_screenshot "03_pulse_detail.png" "Pulse Orbit detail"

start_route "pulse_game" "ready"
take_screenshot "04_pulse_game_ready.png" "Pulse Orbit gameplay ready"

start_route "pulse_game" "paused"
take_screenshot "05_pulse_game_pause.png" "Pulse Orbit paused"

start_route "lane_detail"
take_screenshot "06_lane_detail.png" "Lane Drift detail"

start_route "lane_game" "ready"
take_screenshot "07_lane_game_ready.png" "Lane Drift gameplay ready"

start_route "lane_game" "playing"
take_screenshot "08_lane_game_active.png" "Lane Drift gameplay active"

start_route "stack_detail"
take_screenshot "09_stack_detail.png" "Stack Drop detail"

start_route "stack_game" "ready"
take_screenshot "10_stack_game_ready.png" "Stack Drop gameplay ready"

start_route "stack_game" "playing"
take_screenshot "11_stack_game_controls.png" "Stack Drop controls"

start_route "stack_game" "paused"
take_screenshot "12_stack_game_pause.png" "Stack Drop paused"

log "Saving logcat"
adb logcat -d > "$OUT_DIR/logcat.txt" || true

report ""
report "## Logcat"
report "- [logcat.txt](./logcat.txt)"
report ""
report "## Final Status"
report "PASS if all screenshots above show the expected screen. Review manually in index.html."

create_index_html

log "Screenshot smoke test complete"
log "Artifacts saved to: $OUT_DIR"
	
