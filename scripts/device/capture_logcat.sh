#!/usr/bin/env bash
set -euo pipefail

OUT_DIR="${2:-artifacts/device-validation}"
PID_FILE="$OUT_DIR/logcat.pid"
LOG_FILE="$OUT_DIR/logcat.txt"

if ! command -v adb >/dev/null 2>&1; then
  echo "adb is required but not installed." >&2
  exit 1
fi

mkdir -p "$OUT_DIR"

case "${1:-}" in
  start)
    adb logcat -c
    adb logcat >"$LOG_FILE" 2>&1 &
    echo $! >"$PID_FILE"
    echo "Started logcat capture: $LOG_FILE"
    ;;
  stop)
    if [[ ! -f "$PID_FILE" ]]; then
      echo "No logcat pid file found at $PID_FILE" >&2
      exit 1
    fi
    kill "$(cat "$PID_FILE")"
    rm -f "$PID_FILE"
    echo "Stopped logcat capture: $LOG_FILE"
    ;;
  *)
    echo "Usage: $0 start|stop [output_dir]" >&2
    exit 1
    ;;
esac
