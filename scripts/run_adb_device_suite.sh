#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
APP_APK="${APP_APK:-$ROOT_DIR/app/build/outputs/apk/debug/app-debug.apk}"
TEST_APK="${TEST_APK:-$ROOT_DIR/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk}"
APP_ID="${APP_ID:-com.vexel.offlinearcade}"
TEST_ID="${TEST_ID:-com.vexel.offlinearcade.test}"
RUNNER="${RUNNER:-androidx.test.runner.AndroidJUnitRunner}"
ARTIFACT_DIR="${ARTIFACT_DIR:-$ROOT_DIR/artifacts/device-test}"
SKIP_BUILD="${SKIP_BUILD:-0}"
SKIP_PRECHECKS="${SKIP_PRECHECKS:-0}"
SKIP_RELEASE_CHECK="${SKIP_RELEASE_CHECK:-0}"
PRECHECK_TASKS="${PRECHECK_TASKS:-testDebugUnitTest}"
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

validate_locked_spec() {
  echo "Validating locked app spec shape..."

  local -a expected_games=("lanedrift" "pulseorbit" "stackdrop")
  local -a actual_games=()
  local game_dir
  while IFS= read -r game_dir; do
    actual_games+=("$(basename "$game_dir")")
  done < <(find "$ROOT_DIR/game" -mindepth 1 -maxdepth 1 -type d | sort)

  if [[ "${#actual_games[@]}" -ne "${#expected_games[@]}" ]]; then
    fail "Expected ${#expected_games[@]} MVP game modules, found ${#actual_games[@]}: ${actual_games[*]}"
  fi

  local expected
  for expected in "${expected_games[@]}"; do
    [[ -d "$ROOT_DIR/game/$expected" ]] || fail "Missing locked MVP game module: game/$expected"
  done

  [[ -d "$ROOT_DIR/feature/challenges" ]] || fail "Missing feature/challenges module"
  [[ -d "$ROOT_DIR/feature/stats" ]] || fail "Missing feature/stats module"
  [[ -d "$ROOT_DIR/feature/settings" ]] || fail "Missing feature/settings module"
  [[ -f "$ROOT_DIR/app/src/androidTest/java/com/vexel/offlinearcade/NavigationSmokeTest.kt" ]] || fail "Missing NavigationSmokeTest"
  [[ -f "$ROOT_DIR/app/src/androidTest/java/com/vexel/offlinearcade/GameplayDeviceSmokeTest.kt" ]] || fail "Missing GameplayDeviceSmokeTest"
  [[ -f "$ROOT_DIR/app/src/androidTest/java/com/vexel/offlinearcade/SettingsPersistenceSmokeTest.kt" ]] || fail "Missing SettingsPersistenceSmokeTest"
  [[ -f "$ROOT_DIR/core/data/src/test/java/com/vexel/offlinearcade/core/data/OfflineArcadeRepositoryPersistenceTest.kt" ]] || fail "Missing repository persistence precheck coverage"
}

run_prechecks() {
  if [[ "$SKIP_PRECHECKS" == "1" ]]; then
    echo "SKIP_PRECHECKS=1 set. Skipping local unit-test preflight."
    return
  fi

  echo "Running local preflight tasks: $PRECHECK_TASKS"
  (
    cd "$ROOT_DIR" &&
      ./gradlew $PRECHECK_TASKS
  ) | tee "$RUN_DIR/prechecks.txt"
}

build_apks() {
  if [[ "$SKIP_BUILD" == "1" ]]; then
    echo "SKIP_BUILD=1 set. Reusing existing APK outputs."
    return
  fi
  echo "Building fresh debug and androidTest APKs..."
  (
    cd "$ROOT_DIR" &&
      ./gradlew :app:assembleDebug :app:assembleDebugAndroidTest
  ) | tee "$RUN_DIR/build-debug.txt"
}

build_release() {
  if [[ "$SKIP_RELEASE_CHECK" == "1" ]]; then
    echo "SKIP_RELEASE_CHECK=1 set. Skipping release build verification."
    return
  fi

  echo "Verifying release variant still assembles..."
  (
    cd "$ROOT_DIR" &&
      ./gradlew :app:assembleRelease
  ) | tee "$RUN_DIR/build-release.txt"
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

run_instrumentation() {
  local output_file="$1"
  shift
  set +e
  "$ADB_BIN" "${ADB_ARGS[@]}" shell "$@" 2>&1 | tee "$output_file"
  local command_exit=${PIPESTATUS[0]}
  set -e
  return "$command_exit"
}

echo "Using project root: $ROOT_DIR"
validate_locked_spec
run_prechecks
select_device
build_apks
build_release
require_file "$APP_APK"
require_file "$TEST_APK"

echo "Capturing device context..."
"$ADB_BIN" "${ADB_ARGS[@]}" get-state > "$RUN_DIR/adb-state.txt"
"$ADB_BIN" "${ADB_ARGS[@]}" shell getprop ro.product.model > "$RUN_DIR/device-model.txt"
"$ADB_BIN" "${ADB_ARGS[@]}" shell getprop ro.build.version.release > "$RUN_DIR/android-version.txt"

echo "Clearing previous app state..."
adb_shell pm clear "$APP_ID" > "$RUN_DIR/pm-clear.txt" || true

echo "Uninstalling existing packages to avoid stale signer mismatches..."
"$ADB_BIN" "${ADB_ARGS[@]}" uninstall "$TEST_ID" > "$RUN_DIR/uninstall-test.txt" 2>&1 || true
"$ADB_BIN" "${ADB_ARGS[@]}" uninstall "$APP_ID" > "$RUN_DIR/uninstall-app.txt" 2>&1 || true

echo "Installing app APK..."
"$ADB_BIN" "${ADB_ARGS[@]}" install -r -t "$APP_APK" | tee "$RUN_DIR/install-app.txt"

echo "Installing test APK..."
"$ADB_BIN" "${ADB_ARGS[@]}" install -r -t "$TEST_APK" | tee "$RUN_DIR/install-test.txt"

echo "Listing installed instrumentation targets..."
adb_shell pm list instrumentation > "$RUN_DIR/instrumentation-list.txt" || true
grep -q "$TEST_ID/$RUNNER" "$RUN_DIR/instrumentation-list.txt" || fail "Installed instrumentation target $TEST_ID/$RUNNER was not found on device"

echo "Resetting logcat..."
"$ADB_BIN" "${ADB_ARGS[@]}" logcat -c

echo "Running instrumentation suite..."
instrument_exit=0
run_instrumentation "$RUN_DIR/instrumentation.txt" am instrument -r -w "$TEST_ID/$RUNNER" || instrument_exit=$?
if [[ ! -s "$RUN_DIR/instrumentation.txt" ]]; then
  echo "Primary instrumentation call returned no output. Retrying via cmd activity instrument..." | tee -a "$RUN_DIR/instrumentation.txt"
  instrument_exit=0
  run_instrumentation "$RUN_DIR/instrumentation-retry.txt" cmd activity instrument -r -w "$TEST_ID/$RUNNER" || instrument_exit=$?
  if [[ -s "$RUN_DIR/instrumentation-retry.txt" ]]; then
    cat "$RUN_DIR/instrumentation-retry.txt" >> "$RUN_DIR/instrumentation.txt"
  fi
fi

echo "Collecting logcat..."
"$ADB_BIN" "${ADB_ARGS[@]}" logcat -d > "$RUN_DIR/logcat.txt" || true

if [[ "$instrument_exit" -ne 0 ]]; then
  fail "Instrumentation failed. See $RUN_DIR/instrumentation.txt and $RUN_DIR/logcat.txt"
fi

if [[ ! -s "$RUN_DIR/instrumentation.txt" ]]; then
  fail "Instrumentation produced no output. See $RUN_DIR/instrumentation-list.txt and $RUN_DIR/logcat.txt"
fi

grep -Eq "INSTRUMENTATION_(STATUS|RESULT)|^OK \\(" "$RUN_DIR/instrumentation.txt" || fail "Instrumentation output did not contain any test runner markers. See $RUN_DIR/instrumentation.txt"
grep -q "FAILURES!!!" "$RUN_DIR/instrumentation.txt" && fail "Test failures reported. See $RUN_DIR/instrumentation.txt"
grep -q "INSTRUMENTATION_RESULT: shortMsg=Process crashed." "$RUN_DIR/instrumentation.txt" && fail "App process crashed during suite. See $RUN_DIR/logcat.txt"

echo "Device suite passed."
echo "Artifacts saved to: $RUN_DIR"
