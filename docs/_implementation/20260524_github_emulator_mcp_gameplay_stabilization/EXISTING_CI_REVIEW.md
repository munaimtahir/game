# Existing CI Review

**Date**: May 24, 2026

## Found Workflows
- `android-build.yml`: Basic build pipeline.
- `completeadbtest.yml`: Appears to be an existing adb testing workflow.
- Various `gemini-*.yml` files for AI agent automation.

## CI Needs
We need a unified workflow `.github/workflows/android-emulator-gameplay-ci.yml` focusing on:
1. Building debug APK.
2. Running JVM tests.
3. Linting.
4. Emulator ADB smoke testing (without requiring local device).
5. Capturing screenshots via custom scripts.
