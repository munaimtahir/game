# Project and CI Test Suite Context

## General Repository Information
- **Repo Name:** munaimtahir/game  
- **Repo ID:** 1203260759  
- **GitHub Issues:** [https://github.com/munaimtahir/game/issues](https://github.com/munaimtahir/game/issues)
- **Primary Language Composition:**
  - Kotlin: 69.9%
  - Java: 24.8%
  - Shell: 5.3%

## Purpose of This Document
This context file provides key project, CI, and test logic information to enable automation agents (and human maintainers) to efficiently work through CI, emulator, or test suite failures.

---

## CI Environment

- **Workflow file:** `.github/workflows/completeadbtest.yml`
- **Runner:** Ubuntu, Java 17 (`temurin`), KVM-enabled emulator
- **Device Profile:** Pixel 5, x86_64, API 34 by default
- **Animations:** Disabled via ADB commands in the pipeline
- **APK build/install steps handled automatically using Gradle and GitHub Actions**
- **Artifact outputs:**
  - Screenshots: `adb-artifacts/screenshots/`
  - UI dumps: `adb-artifacts/ui-dumps/`
  - Logcat/logs: `adb-artifacts/logs/`
  - Reports: `adb-artifacts/reports/`
  - Build & test outputs: `**/build/reports/**`, `**/build/outputs/**`

---

## Test Execution

- **Instrumented Test Command:**  
  `./gradlew :app:connectedDebugAndroidTest`  
  _Runs all integration/UI tests on the emulator._

- **Main Test Sources:**  
  `app/src/androidTest/java/com/vexel/offlinearcade/`
    - `BackNavigationTest.kt`
    - `GameplayDeviceSmokeTest.kt`
    - `NavigationSmokeTest.kt`
    - `DeviceTestHelpers.kt`

---

## Specific Test Files With Known Failures

Please prioritize investigation and fixes for the following files:

- `app/src/androidTest/java/com/vexel/offlinearcade/BackNavigationTest.kt`
- `app/src/androidTest/java/com/vexel/offlinearcade/GameplayDeviceSmokeTest.kt`
- `app/src/androidTest/java/com/vexel/offlinearcade/NavigationSmokeTest.kt`

Failures include:
- ComposeTimeoutException and IdlingResourceTimeoutException: Element did not render in time.
- Assert failed: The component is not displayed!: Tag/text not found or UI is blocked.
- Action performScrollTo() failed: Could not locate expected item in scrollable view.

---

## UI Test Tag & Text Conventions

- **Tags and Texts Queried in Tests:**  
    - ArcadeTestTags set on composables (e.g.: `PulseOrbitEntry`, `LaneDriftEntry`, `StackDropEntry`, `PulseOrbitBoard`, etc.).
    - Navigation tests expect to click/scroll on visible texts like "Pulse Orbit", "Lane Drift", "Stack Drop", etc.
- **Custom test helpers:**  
    - Core helpers in `DeviceTestHelpers.kt` including:
      - `waitUntilExists(tag)`
      - `openHomeRoute(entryTag, screenTag)`
- **Important:**  
    All interactive UI elements referenced in test code **must exist in the runtime UI and have matching tags/text**.

---

## Typical CI / Test Issues

- **Test Flakiness:**  
    Emulator boot is sometimes slow. Tests have a default UI wait of 30s (via `waitUntil`).  
    Race conditions, intro or permission dialogs, and slow UI composition can cause timeouts.
- **Common Failures:**  
    - `ComposeTimeoutException` or `IdlingResourceTimeoutException`: The awaited view did not become available in time.
    - `Assert failed: The component is not displayed!`: Test tag/text mismatch or UI got blocked.
    - `Action performScrollTo() failed`: Item not present or not rendered in scrollable container.
- **Debug Hints:**  
    - Add `.assertExists()` before `.assertIsDisplayed()` for more informative errors.
    - Dismiss "Got it" hints or dialogs automatically in test flows.
    - Synchronize ArcadeTestTags and test code.
    - Add screenshots or logs on test failure.

---

## Coverage & ADB Scripts

- **What the suite covers:**
    - Navigation from home, launching and canceling games, dismissing hints, back navigation, pause/resume, general UI responsiveness and presence checks.
    - Monkey and ADB smoke tests for interaction, offline mode, robustness.
- **How/where to update scenarios:**  
    - Instrumented tests are in the Kotlin test sources above.
    - ADB smoke/monkey test script: `.github/workflows/completeadbtest.yml` (Bash section).
    - For new ArcadeTestTags or flows: Update both your UI and the test code.

---

## Useful URLs

- **GitHub Issues:**  
  [https://github.com/munaimtahir/game/issues](https://github.com/munaimtahir/game/issues)

---

## How to Use this Context (Recommended AI/Agent Prompt)

> **You have read/write access to the repository munaimtahir/game.**
>
> The context and CI environment details for the Android emulator/instrumented test suite can be found in `docs/CI_TEST_CONTEXT.md`.
>
> The instrumented test files with the current failures are:
> - `app/src/androidTest/java/com/vexel/offlinearcade/BackNavigationTest.kt`
> - `app/src/androidTest/java/com/vexel/offlinearcade/GameplayDeviceSmokeTest.kt`
> - `app/src/androidTest/java/com/vexel/offlinearcade/NavigationSmokeTest.kt`
>
> - The codebase is primarily Kotlin, with Java and Shell components.
> - Instrumented UI tests and Espresso/Compose helpers are in `app/src/androidTest/java/com/vexel/offlinearcade/`.
> - Matching ArcadeTestTags and text value conventions are essential for tests to pass.
> - Debug logs, screenshots, and UI dumps appear under `adb-artifacts/`.
> - Workflow and test flakiness may be caused by slow emulators, unexpected dialogs, or race conditions.
>
> **Task:**  
> 1. Diagnose and resolve current/most recent CI workflow or job failures, using logs, referenced test files, helpers, and this CI context.
> 2. If tag/text mismatch or blocked UI is detected, align UI and tests as necessary.
> 3. Make minimal, robust code changes for passing tests. Document any new conventions in this context file if you introduce them.
> 4. Reference the GitHub Issues page ([https://github.com/munaimtahir/game/issues](https://github.com/munaimtahir/game/issues)) for similar reports or to file new issues if needed.

---

_Last updated: 2026-05-20_
