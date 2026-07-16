#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
APK_PATH="$ROOT_DIR/app/build/outputs/apk/debug/app-debug.apk"

if ! command -v adb >/dev/null 2>&1; then
  echo "adb is required but not installed." >&2
  exit 1
fi

if [[ ! -f "$APK_PATH" ]]; then
  echo "Debug APK not found at $APK_PATH" >&2
  echo "Build it first with: ./gradlew :app:assembleDebug --no-daemon --console=plain" >&2
  exit 1
fi

adb install -r "$APK_PATH"
