#!/usr/bin/env bash
set -euo pipefail

GAME_TARGET="${1:-all}"
TEST_LEVEL="${2:-smoke}"
ARTIFACT_DIR="artifacts/gameplay-smoke"

mkdir -p "$ARTIFACT_DIR"

echo "Running ADB smoke via screenshot smoke script for game target: $GAME_TARGET, level: $TEST_LEVEL"

if [[ -f "app/build/outputs/apk/debug/app-debug.apk" ]]; then
    export SKIP_BUILD=1
fi

set +e
./scripts/adb_screenshot_smoke.sh "$GAME_TARGET" | tee "$ARTIFACT_DIR/adb-smoke.txt"
exit_code=${PIPESTATUS[0]}
set -e

latest_report_dir="$(find artifacts/adb_screenshots -maxdepth 1 -mindepth 1 -type d | sort | tail -n 1)"
if [[ -n "${latest_report_dir:-}" && -f "$latest_report_dir/REPORT.md" ]]; then
    cp "$latest_report_dir/REPORT.md" "$ARTIFACT_DIR/adb-screenshot-report.md"
    if [[ -f "$latest_report_dir/logcat.txt" ]]; then
        cp "$latest_report_dir/logcat.txt" "$ARTIFACT_DIR/adb-logcat.txt"
    fi
fi

exit "$exit_code"
