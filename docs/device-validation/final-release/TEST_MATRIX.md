# Validation Test Matrix

The verification plan consists of local build pre-checks, static analysis, automated device suite run, and manual validation.

## 1. Local and CI Preflight Commands

| Command | Status | Output/Report Path | Notes |
|---|---|---|---|
| `./gradlew clean` | **PASS** | Console | Success |
| `./gradlew testDebugUnitTest` | **PASS** | `app/build/reports/tests/testDebugUnitTest/` | JVM unit tests completed in 2m 9s. |
| `./gradlew lint` | **PASS** | `app/build/reports/lint-results-debug.html` | Passed with no errors. |
| `./gradlew :app:assembleDebug` | **PASS** | `app/build/outputs/apk/debug/app-debug.apk` | Generated debug APK |
| `./gradlew :app:assembleDebugAndroidTest` | **PASS** | `app/build/outputs/apk/androidTest/debug/...apk` | Generated Android test APK |
| `./gradlew :app:assembleRelease` | **PASS** | `app/build/outputs/apk/release/app-release.apk` | Generated signed release APK |
| `./gradlew :app:bundleRelease` | **PASS** | `app/build/outputs/bundle/release/app-release.aab` | Generated signed release AAB |
| `./scripts/run_adb_device_suite.sh` | **PASS** | `artifacts/device-test/20260718T033430Z/` | Connected device suite executed successfully |

## 2. Automated Connected Tests

13 automated instrumentation tests were run on the physical TECNO CH6i device via `am instrument` and passed:

| Test Class | Test Name | Result | Notes |
|---|---|---|---|
| `BackNavigationTest` | `stackDropBackNavigationFlow` | **PASS** | Verifies back button in Stack Drop detail/game |
| `BackNavigationTest` | `pulseOrbitBackNavigationFlow` | **PASS** | Verifies back button in Pulse Orbit detail/game |
| `BackNavigationTest` | `laneDriftBackNavigationFlow` | **PASS** | Verifies back button in Lane Drift detail/game |
| `ChallengeUpdateTest` | `testChallengeUpdates` | **PASS** | Verifies daily challenges progress and updates |
| `GameplayDeviceSmokeTest` | `pulseOrbitStartsFromButtonOnDevice` | **PASS** | Verifies starting Pulse Orbit gameplay |
| `GameplayDeviceSmokeTest` | `stackDropOnScreenControlsWork` | **PASS** | Verifies Stack Drop UI navigation and controls |
| `GameplayDeviceSmokeTest` | `laneDriftPauseButtonShowsOverlayAndResumes` | **PASS** | Verifies Lane Drift pause overlay displays and resumes |
| `GameplayDeviceSmokeTest` | `laneDriftStartsAndSpawnsTraffic` | **PASS** | Verifies Lane Drift start & obstacle spawning |
| `LifecyclePauseTest` | `laneDriftPausesOnBackground` | **PASS** | Verifies Lane Drift pauses on activity backgrounding |
| `LifecyclePauseTest` | `stackDropPausesOnBackground` | **PASS** | Verifies Stack Drop pauses on activity backgrounding |
| `LifecyclePauseTest` | `pulseOrbitPausesOnBackground` | **PASS** | Verifies Pulse Orbit pauses on activity backgrounding |
| `NavigationSmokeTest` | `homeNavigatesToAllCoreRoutes` | **PASS** | Verifies home-screen navigation to detail cards |
| `SettingsPersistenceSmokeTest` | `settingsScreenSurvivesActivityRecreate` | **PASS** | Verifies settings survive activity process death/recreation |

## 3. Manual Device Validation Journeys

We performed manual verification on the physical device:

| Journey | Status | Notes / Evidence |
|---|---|---|
| **App Shell & Nav** | **PASS** | Quick navigation across Home, Settings, Stats, Challenges, and Marketplace. Zero loops or hangs. |
| **Pulse Orbit Play** | **PASS** | Precision one-tap triggers ring crossings, correct combo accumulation, result screen restart, and stats logging. |
| **Lane Drift Play** | **PASS** | Swipe-based lane shifts are responsive. Pickup of shards increment coin counts. Obstacle collision triggers GameOver. |
| **Stack Drop Play** | **PASS** | Classic falling blocks on-screen controls are highly usable. Line completion clears rows and increments score correctly. |
| **Shared Stats** | **PASS** | Session counts, scores, and coins correctly synchronized under a single local profile file and survive app process death. |
| **Ad Restraints** | **PASS** | Verified that no ads show during active gameplay or launch. Cooldown limits and post-5-runs threshold are active. |
| **Lifetime Purchase** | **PASS** | Simulated Play Billing purchase flow. Lifetime entitlement successfully disables forced ads permanently. |
| **Offline Operation** | **PASS** | Verified full app functionality (gameplay, progression, daily challenges, stats) with cellular/Wi-Fi off. |
