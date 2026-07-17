#!/usr/bin/env bash
set -euo pipefail

OUT_DIR="${1:-artifacts/device-validation}"
mkdir -p "$OUT_DIR"

if ! command -v adb >/dev/null 2>&1; then
  echo "adb is required but not installed." >&2
  exit 1
fi

{
  echo "capture_date_utc=$(date -u +%Y-%m-%dT%H:%M:%SZ)"
  echo "serial=$(adb get-serialno)"
  echo "manufacturer=$(adb shell getprop ro.product.manufacturer | tr -d '\r')"
  echo "model=$(adb shell getprop ro.product.model | tr -d '\r')"
  echo "android_version=$(adb shell getprop ro.build.version.release | tr -d '\r')"
  echo "api_level=$(adb shell getprop ro.build.version.sdk | tr -d '\r')"
  echo "build_fingerprint=$(adb shell getprop ro.build.fingerprint | tr -d '\r')"
  echo "screen_size=$(adb shell wm size | tr -d '\r')"
  echo "screen_density=$(adb shell wm density | tr -d '\r')"
  echo "navigation_mode_hint=$(adb shell settings get secure navigation_mode | tr -d '\r')"
  echo "meminfo_summary:"
  adb shell dumpsys meminfo | sed -n '1,25p'
} >"$OUT_DIR/device_info.txt"

echo "Wrote $OUT_DIR/device_info.txt"
