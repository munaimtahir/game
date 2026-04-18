#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
RUN_DEVICE_SUITE="${RUN_DEVICE_SUITE:-0}"
DEVICE_SERIAL="${DEVICE_SERIAL:-}"

fail() {
  echo "ERROR: $*" >&2
  exit 1
}

require_cmd() {
  local cmd="$1"
  command -v "$cmd" >/dev/null 2>&1 || fail "Missing required command: $cmd"
}

echo "Project root: $ROOT_DIR"

require_cmd java
require_cmd adb

if ! java -version 2>&1 | grep -q 'version "17'; then
  fail "Java 17 is required. Current java -version output does not report 17."
fi

if [[ ! -x "$ROOT_DIR/gradlew" ]]; then
  fail "gradlew is missing or not executable"
fi

echo "Java 17 detected."
echo "adb detected: $(command -v adb)"

echo "Validating Gradle wrapper and local build graph..."
(
  cd "$ROOT_DIR" &&
    ./gradlew --version >/dev/null &&
    ./gradlew testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:assembleRelease
)

if [[ "$RUN_DEVICE_SUITE" != "1" ]]; then
  echo
  echo "Local laptop bootstrap passed."
  echo "Connect a physical device with USB debugging enabled, then run:"
  if [[ -n "$DEVICE_SERIAL" ]]; then
    echo "  DEVICE_SERIAL=$DEVICE_SERIAL ./scripts/run_adb_device_suite.sh"
  else
    echo "  ./scripts/run_adb_device_suite.sh"
  fi
  exit 0
fi

echo "RUN_DEVICE_SUITE=1 set. Launching physical-device suite..."
if [[ -n "$DEVICE_SERIAL" ]]; then
  DEVICE_SERIAL="$DEVICE_SERIAL" "$ROOT_DIR/scripts/run_adb_device_suite.sh"
else
  "$ROOT_DIR/scripts/run_adb_device_suite.sh"
fi
