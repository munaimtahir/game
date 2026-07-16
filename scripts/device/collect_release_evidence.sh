#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
OUT_DIR="${1:-$ROOT_DIR/artifacts/device-validation}"
mkdir -p "$OUT_DIR"

cp "$ROOT_DIR/docs/_implementation/DEFERRED_DEVICE_VALIDATION_CHECKLIST.md" "$OUT_DIR/deferred_device_validation_checklist.md"
cp "$ROOT_DIR/docs/_verification/stage5_device_validation/DEVICE_VALIDATION_PLAN.md" "$OUT_DIR/device_validation_plan.md"

if [[ -f "$ROOT_DIR/app/build/outputs/apk/debug/app-debug.apk" ]]; then
  cp "$ROOT_DIR/app/build/outputs/apk/debug/app-debug.apk" "$OUT_DIR/"
fi

if [[ -f "$ROOT_DIR/app/build/outputs/apk/release/app-release.apk" ]]; then
  cp "$ROOT_DIR/app/build/outputs/apk/release/app-release.apk" "$OUT_DIR/"
fi

if [[ -f "$ROOT_DIR/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk" ]]; then
  cp "$ROOT_DIR/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk" "$OUT_DIR/"
fi

echo "Collected release-evidence inputs in $OUT_DIR"
