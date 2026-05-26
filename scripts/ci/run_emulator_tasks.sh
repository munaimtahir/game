#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="${ROOT_DIR:-$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)}"
ARTIFACTS_DIR="${ARTIFACTS_DIR:-$ROOT_DIR/artifacts}"
GAME_TARGET="${GAME_TARGET:-all}"
TEST_LEVEL="${TEST_LEVEL:-smoke}"
RETRY_FAILED="${RETRY_FAILED:-true}"
ADB_BIN="${ADB_BIN:-adb}"
DEVICE_SERIAL="${DEVICE_SERIAL:-}"

ADB_ARGS=()
if [[ -n "$DEVICE_SERIAL" ]]; then
  ADB_ARGS=(-s "$DEVICE_SERIAL")
fi

mkdir -p \
  "$ARTIFACTS_DIR/logs" \
  "$ARTIFACTS_DIR/logcat" \
  "$ARTIFACTS_DIR/screenshots" \
  "$ARTIFACTS_DIR/test-results" \
  "$ARTIFACTS_DIR/reports" \
  "$ARTIFACTS_DIR/ui-dumps" \
  "$ARTIFACTS_DIR/gameplay-smoke"

log() {
  echo "[emulator-ci] $*"
}

run_gradle_once() {
  local task="$1"
  local output_file="$2"

  set +e
  (
    cd "$ROOT_DIR" &&
      ./gradlew "$task" --stacktrace
  ) | tee "$output_file"
  local exit_code=${PIPESTATUS[0]}
  set -e
  return "$exit_code"
}

run_in_root() {
  (cd "$ROOT_DIR" && "$@")
}

run_and_capture() {
  local output_file="$1"
  shift
  set +e
  "$@" | tee "$output_file"
  local exit_code=${PIPESTATUS[0]}
  set -e
  return "$exit_code"
}

run_gradle_with_retry() {
  local task="$1"
  local output_file="$2"
  if run_gradle_once "$task" "$output_file"; then
    return 0
  fi

  if [[ "$RETRY_FAILED" == "true" ]]; then
    log "Retrying $task once after failure."
    run_gradle_once "$task" "${output_file%.txt}.retry.txt"
  else
    return 1
  fi
}

collect_post_run_artifacts() {
  log "Collecting emulator artifacts."
  "$ADB_BIN" "${ADB_ARGS[@]}" logcat -d > "$ARTIFACTS_DIR/logcat/logcat-final.txt" || true
  "$ADB_BIN" "${ADB_ARGS[@]}" shell uiautomator dump /sdcard/window_dump.xml >/dev/null 2>&1 || true
  "$ADB_BIN" "${ADB_ARGS[@]}" pull /sdcard/window_dump.xml "$ARTIFACTS_DIR/ui-dumps/window_dump.xml" >/dev/null 2>&1 || true
}

maybe_collect_bugreport() {
  local reason="$1"
  if [[ "$TEST_LEVEL" == "full" || "$reason" != "success" ]]; then
    log "Collecting bugreport (${reason})."
    "$ADB_BIN" "${ADB_ARGS[@]}" bugreport "$ARTIFACTS_DIR/logs/bugreport.zip" >/dev/null 2>&1 || true
  fi
}

log "Root directory: $ROOT_DIR"
log "Game target: $GAME_TARGET"
log "Test level: $TEST_LEVEL"
log "Retry failed: $RETRY_FAILED"

overall_status=0

if [[ -x "$ROOT_DIR/scripts/ci/wait_for_emulator.sh" ]]; then
  run_and_capture "$ARTIFACTS_DIR/logs/wait-for-emulator.txt" run_in_root scripts/ci/wait_for_emulator.sh || overall_status=1
fi

log "Disabling emulator animations."
"$ADB_BIN" "${ADB_ARGS[@]}" shell settings put global window_animation_scale 0 || true
"$ADB_BIN" "${ADB_ARGS[@]}" shell settings put global transition_animation_scale 0 || true
"$ADB_BIN" "${ADB_ARGS[@]}" shell settings put global animator_duration_scale 0 || true

if [[ "$TEST_LEVEL" != "screenshots_only" ]]; then
  log "Running connected Android tests."
  if ! run_gradle_with_retry "connectedAndroidTest" "$ARTIFACTS_DIR/logs/connectedAndroidTest.txt"; then
    overall_status=1
  fi
else
  log "Skipping connected Android tests for screenshots_only mode."
fi

if [[ -x "$ROOT_DIR/scripts/ci/run_adb_smoke.sh" && "$TEST_LEVEL" != "screenshots_only" ]]; then
  log "Running ADB smoke checks."
  if ! run_and_capture "$ARTIFACTS_DIR/gameplay-smoke/adb-smoke.txt" run_in_root scripts/ci/run_adb_smoke.sh "$GAME_TARGET" "$TEST_LEVEL"; then
    overall_status=1
  fi
fi

if [[ -x "$ROOT_DIR/scripts/ci/capture_game_screenshots.sh" ]]; then
  log "Capturing screenshots."
  if ! run_and_capture "$ARTIFACTS_DIR/logs/screenshot-capture.txt" run_in_root scripts/ci/capture_game_screenshots.sh "$GAME_TARGET"; then
    overall_status=1
  fi
fi

collect_post_run_artifacts

if [[ "$overall_status" -ne 0 ]]; then
  maybe_collect_bugreport "failure"
else
  maybe_collect_bugreport "success"
fi

exit "$overall_status"
