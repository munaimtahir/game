# Repository Guidelines

## Project Structure & Module Organization

This is a modular Android/Kotlin offline arcade app. Modules are declared in `settings.gradle.kts`.

- `app/` contains the Android application, navigation, resources, and instrumentation tests.
- `core/model`, `core/data`, `core/common`, and `core/ui` contain shared domain types, persistence/runtime data, helpers, and reusable Compose UI.
- `feature/*` contains top-level screens such as home, challenges, stats, settings, and marketplace.
- `game/*` contains game implementations. The locked public MVP scope is `pulseorbit`, `lanedrift`, and `stackdrop`; additional game modules in the repository are legacy or prototype work and are not product source of truth for current staged delivery.
- `scripts/` and `scripts/ci/` contain local and CI Android/ADB helpers.
- `docs/`, `new/`, and `artifacts/` hold planning, release notes, game design packs, and validation output.

## Build, Test, and Development Commands

Use the checked-in Gradle wrapper.

- `./gradlew assembleDebug` builds the debug APK.
- `./gradlew testDebugUnitTest` runs JVM unit tests, including Robolectric where configured.
- `./gradlew lintDebug` runs Android lint for the debug variant.
- `./gradlew assembleDebug assembleDebugAndroidTest` builds app and instrumentation test APKs.
- `./gradlew connectedCheck` runs device/emulator instrumentation tests.
- `./scripts/bootstrap_laptop_android.sh` validates a fresh workstation and runs core checks.
- `./scripts/run_adb_device_suite.sh` runs the physical-device validation suite; set `DEVICE_SERIAL=<adb-serial>` when multiple devices are attached.

## Coding Style & Naming Conventions

Write Kotlin with 4-space indentation and idiomatic Compose patterns. Keep package paths under `com.vexel.offlinearcade` except where the app namespace requires `com.vexel.arcadetrio`. Name Compose screens and reusable UI with `PascalCase` (`HomeScreen`, `ArcadePlayButton`), state/data classes with descriptive nouns, and tests as `SubjectBehaviorTest.kt`. Keep game logic in `game/<name>` and shared UI in `core/ui`.

## Testing Guidelines

Unit tests live in `src/test/java`; instrumentation tests live in `src/androidTest/java`. Tests use JUnit 4, Kotlin test, Robolectric, AndroidX Test, Espresso, and Compose UI test. Add focused unit tests for game rules, persistence, and state transitions before device tests. Run `./gradlew testDebugUnitTest lintDebug` before opening a PR; run `connectedCheck` or the ADB suite for navigation, lifecycle, gameplay, or settings changes.

## Commit & Pull Request Guidelines

Recent history uses short imperative commits and occasional Conventional Commit prefixes such as `feat:` and `refactor:`. Prefer `type: concise summary` for feature, fix, refactor, test, and docs changes. PRs should describe impact, list verification commands, link issues, and include screenshots or artifacts for UI/gameplay changes.

## Security & Configuration

Do not commit local signing material. `key.properties`, keystores, APKs, AABs, and local SDK files are ignored. Release builds require `storeFile`, `storePassword`, `keyAlias`, and `keyPassword` via `key.properties` or environment variables.
