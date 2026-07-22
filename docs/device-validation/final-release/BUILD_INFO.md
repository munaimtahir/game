# Build and Packaging Information

Below are the details of the tested build configuration and generated release artifacts:

## Version Configurations

- **Application ID (Package Name):** `com.vexel.arcadetrio`
- **Instrumentation Test ID:** `com.vexel.arcadetrio.test`
- **Version Name:** `1.1.4`
- **Version Code:** `14`
- **Minimum SDK:** `24`
- **Target SDK:** `35`
- **Git Commit Hash:** `063a84df9f9b0667e2ac545017a7858d63ce7a01`
- **Git Branch:** `main`

## Signing Configuration

- **Key Properties File:** Checked in locally as `key.properties`.
- **Keystore File:** `release.keystore` (2.6KB) located in the repository root.
- **Signing Scheme:** Release builds are fully signed using a local key alias (`release`) with passwords defined in `key.properties`.
- **R8 / ProGuard Configuration:** Enabled (`isMinifyEnabled = true`, `isShrinkResources = true`) to strip unused code/resources.

## Release Artifacts

All builds completed successfully. The generated files are:
1. **Debug APK:** `app/build/outputs/apk/debug/app-debug.apk`
2. **Android Test APK:** `app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk`
3. **Release APK:** `app/build/outputs/apk/release/app-release.apk`
4. **Release Bundle (AAB):** `app/build/outputs/bundle/release/app-release.aab`
