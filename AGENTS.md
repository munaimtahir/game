# Repository Guidelines

## Project Structure & Module Organization

This is a modular Android/Kotlin offline arcade app. Modules are declared in `settings.gradle.kts`.

- `app/` contains the Android application, navigation, resources, and instrumentation tests.
- `core/model`, `core/data`, `core/common`, and `core/ui` contain shared domain types, persistence/runtime data, helpers, and reusable Compose UI.
- `feature/*` contains top-level screens such as home, challenges, stats, settings, and marketplace.
- `game/*` contains individual game implementations such as `pulseorbit`, `lanedrift`, `stackdrop`, `brickvolley`, `loopsnake`, and `shielddash`.
- `scripts/` and `scripts/ci/` contain local and CI Android/ADB helpers.
- `docs/`, `new/`, and `artifacts/` hold planning, release notes, game design packs, and validation output.

## Monetization Policy & Agent Guidelines

The app has transitioned from a paid download model to a **free-plus-advertising model**. Future coding agents must strictly adhere to the following decisions and rules:

### Authoritative Product Decisions
* **Precedence of Truth**: Refer to the canonical documentation for all product policies. Precedence is:
  1. [LOCKED_DECISIONS.md](file:///home/munaim/Documents/github/game/docs/LOCKED_DECISIONS.md) — final approved product decisions
  2. [PROJECT_CONTEXT_MASTER.md](file:///home/munaim/Documents/github/game/docs/PROJECT_CONTEXT_MASTER.md) — complete project context
  3. [PRODUCT_RULES_AND_GUARDRAILS.md](file:///home/munaim/Documents/github/game/docs/PRODUCT_RULES_AND_GUARDRAILS.md) — behavioural and product constraints
  4. [BUILD_PLANNING_NEXT_STEPS.md](file:///home/munaim/Documents/github/game/docs/BUILD_PLANNING_NEXT_STEPS.md) — current development sequence
  5. [NEXT_CHAT_BOOTSTRAP.md](file:///home/munaim/Documents/github/game/docs/NEXT_CHAT_BOOTSTRAP.md) — context restoration
  6. [README.md](file:///home/munaim/Documents/github/game/README.md) / [docs/README.md](file:///home/munaim/Documents/github/game/docs/README.md) — overview summaries
* **Conflict Resolution**: Conflicting older documentation (e.g., legacy files or historical audits) must not be followed. If a conflict arises, the updated policy files above win.

### Critical Rules
* **Free Model**: The app is free to download. All 3 games and core progression/stats remain 100% free.
* **Offline-First & Low-End-Device Friendly**: All gameplay and local progress must work fully offline. Monetization/ad/billing SDK failures must fail-safe silently and never block gameplay or navigation.
* **Restricted Interstitial Cadence**:
  - No ads during active gameplay or on app-open or screen transitions like Retry/Play.
  - Interstitials are allowed only at natural post-run transitions, starting only after the first 5 completed runs.
  - Eligibility begins after every 3 completed runs, with a minimum 2-minute cooldown and a maximum of 4 interstitials per user per day.
  - No app-open or banner ads initially.
* **Lifetime Ad Removal**: A single non-consumable purchase named `premium_lifetime` permanently removes forced ads. Entitlements defined: `FREE` and `LIFETIME_AD_FREE`. Optional rewarded ads can remain available to premium users upon deliberate request.
* **No Subscriptions**: Do not implement subscriptions in the initial release. Subscription-first monetization is prohibited.
* **No Legacy Migration**: The app has no real historical users/purchases. Legacy-user migration, grandfathering, or historical-purchase detection is not required.
* **AI-Led 3-Stage Plan**: Monetization must be developed in three explicit stages:
  1. **Stage 1 (Repository Discovery)**: Architecture/gameplay flow mapping, insertion-point identification, no behavior changes.
  2. **Stage 2 (Consolidated Implementation)**: Core monetization code, consent, billing, UI, analytics, and tests.
  3. **Stage 3 (Release Hardening)**: Testing, verification, store release checklist, final verdict.

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
