#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
APP_APK="${APP_APK:-$ROOT_DIR/app/build/outputs/apk/debug/app-debug.apk}"
TEST_APK="${TEST_APK:-$ROOT_DIR/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk}"
APP_ID="${APP_ID:-com.vexel.offlinearcade}"
TEST_ID="${TEST_ID:-com.vexel.offlinearcade.test}"
RUNNER="${RUNNER:-androidx.test.runner.AndroidJUnitRunner}"
ARTIFACT_DIR="${ARTIFACT_DIR:-$ROOT_DIR/artifacts/device-test}"
TIMESTAMP="$(date -u +%Y%m%dT%H%M%SZ)"
RUN_DIR="$ARTIFACT_DIR/$TIMESTAMP"
mkdir -p "$RUN_DIR"

ADB_BIN="${ADB_BIN:-adb}"
DEVICE_SERIAL="${DEVICE_SERIAL:-}"
ADB_ARGS=()
if [[ -n "$DEVICE_SERIAL" ]]; then
  ADB_ARGS=(-s "$DEVICE_SERIAL")
fi

fail() {
  echo "ERROR: $*" >&2
  exit 1
}

require_file() {
  local path="$1"
  [[ -f "$path" ]] || fail "Missing file: $path"
}

build_missing_apks() {
  local need_build=0
  [[ -f "$APP_APK" ]] || need_build=1
  [[ -f "$TEST_APK" ]] || need_build=1
  if [[ "$need_build" -eq 1 ]]; then
    echo "APK outputs missing. Building debug and androidTest APKs..."
    (cd "$ROOT_DIR" && ./gradlew :app:assembleDebug :app:assembleDebugAndroidTest)
  fi
}

select_device() {
  local devices
  devices="$("$ADB_BIN" devices | awk 'NR>1 && $2=="device" {print $1}')"
  if [[ -z "$DEVICE_SERIAL" ]]; then
    local count
    count="$(printf '%s\n' "$devices" | sed '/^$/d' | wc -l | tr -d ' ')"
    [[ "$count" -ge 1 ]] || fail "No adb device detected"
    [[ "$count" -eq 1 ]] || fail "Multiple adb devices detected. Set DEVICE_SERIAL=<serial>."
  else
    printf '%s\n' "$devices" | grep -qx "$DEVICE_SERIAL" || fail "Device $DEVICE_SERIAL not found in adb devices output"
  fi
}

adb_shell() {
  "$ADB_BIN" "${ADB_ARGS[@]}" shell "$@"
}

echo "Using project root: $ROOT_DIR"
select_device
build_missing_apks
require_file "$APP_APK"
require_file "$TEST_APK"

echo "Capturing device context..."
"$ADB_BIN" "${ADB_ARGS[@]}" get-state > "$RUN_DIR/adb-state.txt"
"$ADB_BIN" "${ADB_ARGS[@]}" shell getprop ro.product.model > "$RUN_DIR/device-model.txt"
"$ADB_BIN" "${ADB_ARGS[@]}" shell getprop ro.build.version.release > "$RUN_DIR/android-version.txt"

echo "Clearing previous app state..."
adb_shell pm clear "$APP_ID" > "$RUN_DIR/pm-clear.txt" || true

echo "Installing app APK..."
"$ADB_BIN" "${ADB_ARGS[@]}" install -r -t "$APP_APK" | tee "$RUN_DIR/install-app.txt"

echo "Installing test APK..."
"$ADB_BIN" "${ADB_ARGS[@]}" install -r -t "$TEST_APK" | tee "$RUN_DIR/install-test.txt"

echo "Resetting logcat..."
"$ADB_BIN" "${ADB_ARGS[@]}" logcat -c

echo "Running instrumentation suite..."
set +e
"$ADB_BIN" "${ADB_ARGS[@]}" shell am instrument -w "$TEST_ID/$RUNNER" | tee "$RUN_DIR/instrumentation.txt"
instrument_exit=${PIPESTATUS[0]}
set -e

echo "Collecting logcat..."
"$ADB_BIN" "${ADB_ARGS[@]}" logcat -d > "$RUN_DIR/logcat.txt" || true

if [[ "$instrument_exit" -ne 0 ]]; then
  fail "Instrumentation failed. See $RUN_DIR/instrumentation.txt and $RUN_DIR/logcat.txt"
fi

grep -q "FAILURES!!!" "$RUN_DIR/instrumentation.txt" && fail "Test failures reported. See $RUN_DIR/instrumentation.txt"
grep -q "INSTRUMENTATION_RESULT: shortMsg=Process crashed." "$RUN_DIR/instrumentation.txt" && fail "App process crashed during suite. See $RUN_DIR/logcat.txt"

echo "Device suite passed."
echo "Artifacts saved to: $RUN_DIR"
