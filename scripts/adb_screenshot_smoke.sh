#!/usr/bin/env bash
set -euo pipefail

PACKAGE_NAME="${PACKAGE_NAME:-com.vexel.offlinearcade}"
MAIN_ACTIVITY="${MAIN_ACTIVITY:-.MainActivity}"
SCREENSHOT_WAIT_SECONDS="${SCREENSHOT_WAIT_SECONDS:-2}"
SKIP_BUILD="${SKIP_BUILD:-0}"
SKIP_INSTALL="${SKIP_INSTALL:-0}"
FOREGROUND_TIMEOUT="${FOREGROUND_TIMEOUT:-30}"
COLD_START_WAIT="${COLD_START_WAIT:-12}"
GAME_TARGET="${1:-all}"

TIMESTAMP="$(date +"%Y%m%d_%H%M%S")"
OUT_DIR="artifacts/adb_screenshots/${TIMESTAMP}"

mkdir -p "$OUT_DIR"

# Tracking variables for the final report
APP_INSTALLED=0
APP_LAUNCHED=0
FOREGROUND_CONFIRMED=0
ROUTES_LAUNCHED=0
ROUTES_REQUESTED=0
SCREENSHOTS_CAPTURED=0
PASS=1

log() {
  echo "[$(date +"%H:%M:%S")] $*"
}

report() {
  echo "$*" >> "$OUT_DIR/REPORT.md"
}

fail() {
  log "FAIL: $*"
  PASS=0
}

adb_device_count() {
  adb devices | awk 'NR>1 && $2=="device" {count++} END {print count+0}'
}

wake_and_unlock() {
  log "Waking emulator and dismissing keyguard"
  adb shell input keyevent KEYCODE_WAKEUP || true
  sleep 1
  adb shell wm dismiss-keyguard 2>/dev/null || true
  sleep 1
}

# Poll dumpsys until the target package is in the foreground, or time out.
# Sets FOREGROUND_CONFIRMED=1 on success. Returns 0 on success, 1 on timeout.
wait_for_app_foreground() {
  local package="$1"
  local timeout="${2:-$FOREGROUND_TIMEOUT}"
  local elapsed=0

  log "Waiting for $package to be foregrounded (timeout: ${timeout}s)"

  while [[ "$elapsed" -lt "$timeout" ]]; do
    local focused
    focused="$(adb shell dumpsys window windows 2>/dev/null \
      | grep -E 'mCurrentFocus|mFocusedApp' \
      | tr -d '\r' \
      | head -n 5 || true)"

    if echo "$focused" | grep -q "$package"; then
      log "App $package confirmed in foreground after ${elapsed}s"
      FOREGROUND_CONFIRMED=1
      return 0
    fi

    # Backup: check resumed activity via dumpsys activity
    local activity_top
    activity_top="$(adb shell dumpsys activity activities 2>/dev/null \
      | grep -E 'mResumedActivity|topResumedActivity|ResumedActivity' \
      | tr -d '\r' \
      | head -n 3 || true)"

    if echo "$activity_top" | grep -q "$package"; then
      log "App $package confirmed in foreground via activity (${elapsed}s)"
      FOREGROUND_CONFIRMED=1
      return 0
    fi

    sleep 1
    elapsed=$((elapsed + 1))
  done

  local actual_focus
  actual_focus="$(adb shell dumpsys window windows 2>/dev/null \
    | grep -E 'mCurrentFocus' | tr -d '\r' | head -n 1 || true)"

  log "ERROR: $package NOT foregrounded after ${timeout}s. Current focus: $actual_focus"
  return 1
}

take_screenshot() {
  local filename="$1"
  local label="$2"

  log "Capturing: $label"

  if ! wait_for_app_foreground "$PACKAGE_NAME"; then
    fail "Screenshot '$label': app not in foreground — skipping capture"
    report "- **SKIPPED** (app not in foreground): $label"
    return 0
  fi

  sleep "$SCREENSHOT_WAIT_SECONDS"
  adb exec-out screencap -p > "$OUT_DIR/$filename"
  report "- [$label](./$filename)"
  SCREENSHOTS_CAPTURED=$((SCREENSHOTS_CAPTURED + 1))
}

start_app_normal() {
  log "Launching app normally"
  wake_and_unlock
  adb shell am force-stop "$PACKAGE_NAME" || true
  sleep 1
  adb shell am start -W \
    -n "${PACKAGE_NAME}/${MAIN_ACTIVITY}" \
    -c android.intent.category.LAUNCHER \
    -a android.intent.action.MAIN || true
  APP_LAUNCHED=1
}

start_route() {
  local route="$1"
  local state="${2:-}"

  ROUTES_REQUESTED=$((ROUTES_REQUESTED + 1))
  log "Launching route: $route ${state:+state=$state}"
  wake_and_unlock
  adb shell am force-stop "$PACKAGE_NAME" || true
  sleep 2

  local start_result
  if [[ -n "$state" ]]; then
    start_result="$(adb shell am start -W \
      -n "${PACKAGE_NAME}/${MAIN_ACTIVITY}" \
      --es screenshot_route "$route" \
      --es screenshot_state "$state" 2>&1 || true)"
  else
    start_result="$(adb shell am start -W \
      -n "${PACKAGE_NAME}/${MAIN_ACTIVITY}" \
      --es screenshot_route "$route" 2>&1 || true)"
  fi

  log "am start result: $start_result"

  # Report if am start raised a warning — extras may be unsupported or ignored
  if echo "$start_result" | grep -qiE "\bWarning\b|\bError\b|\bException\b"; then
    log "WARNING: am start reported an issue for route '$route': $start_result"
    report "- **WARNING**: am start issue for route '$route': $start_result"
  fi

  ROUTES_LAUNCHED=$((ROUTES_LAUNCHED + 1))
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
  APP_INSTALLED=1
else
  log "Skipping install because SKIP_INSTALL=1"
  APP_INSTALLED=1
fi

# Wake/unlock before first launch
wake_and_unlock

# 01 cold start — give the app time to initialise before the first screenshot
start_app_normal
log "Waiting ${COLD_START_WAIT}s for cold start to settle..."
sleep "$COLD_START_WAIT"
take_screenshot "01_splash_or_launch.png" "Splash or launch screen"

# Route-based screenshots. These require debug screenshot_route extras support.
# If the app ignores extras, it will still be captured from whatever screen it
# lands on — the foreground confirmation ensures the app (not the launcher) is visible.
start_route "home"
take_screenshot "02_home.png" "Home"

case "$GAME_TARGET" in
  all)
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
    ;;
  pulse_orbit)
    start_route "pulse_detail"
    take_screenshot "03_pulse_detail.png" "Pulse Orbit detail"
    start_route "pulse_game" "ready"
    take_screenshot "04_pulse_game_ready.png" "Pulse Orbit gameplay ready"
    start_route "pulse_game" "paused"
    take_screenshot "05_pulse_game_pause.png" "Pulse Orbit paused"
    ;;
  lane_drift)
    start_route "lane_detail"
    take_screenshot "06_lane_detail.png" "Lane Drift detail"
    start_route "lane_game" "ready"
    take_screenshot "07_lane_game_ready.png" "Lane Drift gameplay ready"
    start_route "lane_game" "playing"
    take_screenshot "08_lane_game_active.png" "Lane Drift gameplay active"
    ;;
  stack_drop)
    start_route "stack_detail"
    take_screenshot "09_stack_detail.png" "Stack Drop detail"
    start_route "stack_game" "ready"
    take_screenshot "10_stack_game_ready.png" "Stack Drop gameplay ready"
    start_route "stack_game" "playing"
    take_screenshot "11_stack_game_controls.png" "Stack Drop controls"
    start_route "stack_game" "paused"
    take_screenshot "12_stack_game_pause.png" "Stack Drop paused"
    ;;
  *)
    echo "Unknown game target: $GAME_TARGET" >&2
    exit 1
    ;;
esac

log "Saving logcat"
APP_PID="$(adb shell pidof -s "$PACKAGE_NAME" 2>/dev/null | tr -d '\r' || true)"
if [[ -n "$APP_PID" ]]; then
  log "Filtering logcat to package pid: $APP_PID"
  adb logcat --pid="$APP_PID" -d > "$OUT_DIR/logcat.txt" || true
else
  log "WARNING: package pid not found; falling back to full logcat"
  adb logcat -d > "$OUT_DIR/logcat.txt" || true
fi

if grep -iE "fatal exception|app not responding|ANR in|Process crashed" "$OUT_DIR/logcat.txt" >/dev/null; then
  log "ERROR: crash or ANR detected in logcat"
  report "**RESULT: FAIL** — crash or ANR detected in logcat."
  exit 1
fi

report ""
report "## Logcat"
report "- [logcat.txt](./logcat.txt)"
report ""

# Final pass/fail summary
report "## Final Status"
report ""
report "| Check | Result |"
report "|-------|--------|"
report "| App installed | $([ "$APP_INSTALLED" -eq 1 ] && echo "✅ YES" || echo "❌ NO") |"
report "| App launched | $([ "$APP_LAUNCHED" -eq 1 ] && echo "✅ YES" || echo "❌ NO") |"
report "| Foreground package matched ($PACKAGE_NAME) | $([ "$FOREGROUND_CONFIRMED" -eq 1 ] && echo "✅ YES" || echo "❌ NO") |"
report "| Routes launched | ${ROUTES_LAUNCHED} / ${ROUTES_REQUESTED} |"
report "| Screenshots captured after foreground confirmation | ${SCREENSHOTS_CAPTURED} |"
report ""

if [[ "$APP_INSTALLED" -eq 1 && "$APP_LAUNCHED" -eq 1 && \
      "$FOREGROUND_CONFIRMED" -eq 1 && "$PASS" -eq 1 ]]; then
  report "**RESULT: PASS** — App installed, launched, and foregrounded as \`$PACKAGE_NAME\`. All routes attempted. ${SCREENSHOTS_CAPTURED} screenshot(s) captured after foreground confirmation."
  log "RESULT: PASS"
else
  report "**RESULT: FAIL** — One or more checks failed. See table above."
  log "RESULT: FAIL — installed=$APP_INSTALLED launched=$APP_LAUNCHED foregrounded=$FOREGROUND_CONFIRMED routes=${ROUTES_LAUNCHED}/${ROUTES_REQUESTED} screenshots=$SCREENSHOTS_CAPTURED pass_flag=$PASS"
fi

create_index_html

log "Screenshot smoke test complete"
log "Artifacts saved to: $OUT_DIR"

# Fail the GitHub Action if the app was never confirmed in the foreground
if [[ "$APP_INSTALLED" -ne 1 || "$APP_LAUNCHED" -ne 1 || \
      "$FOREGROUND_CONFIRMED" -ne 1 || "$PASS" -ne 1 ]]; then
  exit 1
fi
