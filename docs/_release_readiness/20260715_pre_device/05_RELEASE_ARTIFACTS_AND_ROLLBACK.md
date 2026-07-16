# Release Artifacts And Rollback (Pre-Device)

## Available Artifacts

- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Android test APK: `app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk`

## Not Available In This Environment

- Release APK
- Release AAB

Reason:

- Release signing material is not configured in the repository environment.

## Stage 5 Device Package

- `scripts/device/install_debug.sh`
- `scripts/device/install_release.sh`
- `scripts/device/run_connected_tests.sh`
- `scripts/device/capture_device_info.sh`
- `scripts/device/capture_logcat.sh`
- `scripts/device/collect_release_evidence.sh`
- `docs/_verification/stage5_device_validation/DEVICE_VALIDATION_PLAN.md`

## Rollback Guidance

- If a Stage 5 runtime defect is found, stop the release gate immediately.
- Capture device info, logcat, screenshots, and reproduction steps.
- Fix the defect on the branch.
- Add or update automated regression coverage where practical.
- Re-run the affected JVM/build checks before re-running the device journey.
