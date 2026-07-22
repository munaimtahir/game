# Defect Log

Final validation on the physical TECNO CH6i (Android 13, API 33) confirmed **0 Critical** and **0 High-severity** defects. 

Below is a registry of noted low-severity issues, system warnings, and their resolutions:

## Defect Registry

### `DEF-001`: Compose Lock Verification Warnings
* **Severity:** Low (System warning only)
* **Observed Device/Build:** TECNO CH6i / `versionName 1.1.3`
* **Symptoms:** Logcat prints `SnapshotStateList.conditionalUpdate failed lock verification and will run slower.`
* **Root Cause:** A known Jetpack Compose optimization warning appearing on specific CPU architectures when code is run in debug variant without full R8 optimizations.
* **Impact:** None. Does not affect functionality. In the optimized release build with full R8 enabled (`minifyEnabled = true`), these verification warnings are optimized out.
* **Status:** **Closed** (Non-blocking, optimized out in release)

### `DEF-002`: WebView Bluetooth connect permission warning
* **Severity:** Low (Third-party library log noise)
* **Observed Device/Build:** TECNO CH6i / `versionName 1.1.3`
* **Symptoms:** Logcat prints `BLUETOOTH_CONNECT permission is missing.`
* **Root Cause:** Initiated by the Chromium engine integrated with the Google Mobile Ads SDK initialization process checking for Bluetooth capabilities.
* **Impact:** None. The app does not request or require Bluetooth permissions, and Ads continue to function properly.
* **Status:** **Closed** (External Ad SDK library log noise, non-blocking)

### `DEF-003`: Firebase integration warning
* **Severity:** Low (Library configuration)
* **Observed Device/Build:** TECNO CH6i / `versionName 1.1.3`
* **Symptoms:** Logcat prints `The Google Mobile Ads SDK will not integrate with Firebase. Admob/Firebase integration requires the latest Firebase SDK...`
* **Root Cause:** AdMob SDK checks for Firebase dependencies, which are not present in this lightweight, offline-first app.
* **Impact:** None. The app purposely avoids cloud or online services. Ads initialize fine without Firebase.
* **Status:** **Closed** (Expected behavior, offline-first target, non-blocking)
