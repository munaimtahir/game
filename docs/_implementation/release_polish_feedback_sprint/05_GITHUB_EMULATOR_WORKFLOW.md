# GitHub Emulator Workflow
**Date**: May 2026

## Workflow Path
`.github/workflows/android-emulator-verification.yml`

## Available Modes
The workflow takes an input `test_mode` which supports the following options:
- `smoke`: Runs standard `connectedDebugAndroidTest`.
- `daily_challenge`: Specifically targets tests using the `GameplayDeviceSmokeTest` to verify progression functionality.
- `screenshots`: Uses the custom `adb_screenshot_smoke.sh` script to capture gameplay and screen flows without standard UI test assertions.
- `full`: Runs both connected Android tests and screenshot scripts.

## How to Trigger Manually
Via the GitHub Actions web interface, or using the `gh` CLI:
`gh workflow run android-emulator-verification.yml --ref <branch-name> -f test_mode=smoke`

## Artifact Names
- `android-emulator-screenshots`: Contains PNG captures of the device states.
- `android-build-reports`: Contains the test matrices, code coverage, and error trace HTML files.

## Known Limitations
- Emulator cold boots on GitHub-hosted Ubuntu runners take approximately 3-5 minutes, increasing test duration. 
- Hardware acceleration (KVM) relies on specific system setups and may fall back to `swiftshader_indirect`, potentially affecting rendering fidelity in screenshots, though sufficient for verification.