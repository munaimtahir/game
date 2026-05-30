# Workflow Map

## New Workflows Created
- **android-code-ci.yml**: Runs unit tests, linting, and assembles the debug build.
- **android-runtime-emulator-ci.yml**: Starts an Android Emulator (API 33) and runs `connectedCheck` to verify Compose UI and E2E instrumentation logic.
- **android-release-readiness.yml**: Generates a dummy CI release keystore, assembles `bundleRelease` and `assembleRelease`, and uploads artifacts. This ensures the app is ready for Play Store signing configuration and packaging.

## Existing Workflows (Kept)
- `android-emulator-gameplay-ci.yml`: Kept as a fallback or dispatch-driven CI job.
- `gemini-*.yml`: Agent/AI workflows kept unchanged.
