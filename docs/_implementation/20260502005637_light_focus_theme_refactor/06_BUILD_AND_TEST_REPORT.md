# 06 — Build / Test / Lint Report

Timestamp (UTC): `20260502005637`

## Commands run

- `./gradlew clean assembleDebug test lint --no-build-cache --rerun-tasks`
- `bash scripts/adb_screenshot_smoke.sh`

## Results

- Build (`assembleDebug`): PASS
- Unit tests (`test`): PASS
- Lint (`lint`): PASS (HTML report generated at `app/build/reports/lint-results-debug.html`)

## Notes

- During Robolectric cleanup, a non-fatal temp-directory deletion error was logged (`DirectoryNotEmptyException` under `/tmp/robolectric-nativeruntime...`). Build/test still completed successfully.

## ADB / screenshots

- `scripts/adb_screenshot_smoke.sh`: FAIL
  - Reason: no connected ADB device/emulator detected.
  - Output directory prepared: `artifacts/adb_screenshots/20260502_010912`

## Remaining blockers

- None build-related.
- Device/emulator required to generate screenshot artifacts.

