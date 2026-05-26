#!/usr/bin/env bash
set -euo pipefail

ADB_BIN="${ADB_BIN:-adb}"
TIMEOUT_SECONDS="${TIMEOUT_SECONDS:-300}"
DEVICE_SERIAL="${DEVICE_SERIAL:-}"

ADB_ARGS=()
if [[ -n "$DEVICE_SERIAL" ]]; then
  ADB_ARGS=(-s "$DEVICE_SERIAL")
fi

echo "Waiting for emulator/device boot..."
timeout "$TIMEOUT_SECONDS" "$ADB_BIN" "${ADB_ARGS[@]}" wait-for-device
elapsed=0
while [[ "$elapsed" -lt "$TIMEOUT_SECONDS" ]]; do
  boot_completed="$("$ADB_BIN" "${ADB_ARGS[@]}" shell getprop sys.boot_completed | tr -d '\r')"
  if [[ "$boot_completed" == "1" ]]; then
    echo "Boot completed after ${elapsed}s."
    exit 0
  fi
  sleep 2
  elapsed=$((elapsed + 2))
done
echo "ERROR: emulator did not report boot completion within ${TIMEOUT_SECONDS}s" >&2
exit 1
