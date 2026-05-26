#!/usr/bin/env bash
set -euo pipefail

GAME_TARGET="${1:-all}"
ARTIFACT_DIR="artifacts/screenshots"

mkdir -p "$ARTIFACT_DIR"

echo "Capturing screenshots via route-based smoke for game target: $GAME_TARGET"

if [[ -f "app/build/outputs/apk/debug/app-debug.apk" ]]; then
    export SKIP_BUILD=1
fi

set +e
./scripts/adb_screenshot_smoke.sh "$GAME_TARGET" | tee "$ARTIFACT_DIR/screenshot-smoke.txt"
exit_code=${PIPESTATUS[0]}
set -e

latest_dir="$(find artifacts/adb_screenshots -maxdepth 1 -mindepth 1 -type d | sort | tail -n 1)"
if [[ -n "${latest_dir:-}" ]]; then
    cp -f "$latest_dir"/*.png "$ARTIFACT_DIR/" 2>/dev/null || true
    if [[ -f "$latest_dir/REPORT.md" ]]; then
        cp "$latest_dir/REPORT.md" "$ARTIFACT_DIR/adb-screenshot-report.md"
    fi
    if [[ -f "$latest_dir/logcat.txt" ]]; then
        cp "$latest_dir/logcat.txt" "$ARTIFACT_DIR/adb-logcat.txt"
    fi
fi

exit "$exit_code"
