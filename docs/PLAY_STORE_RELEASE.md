# Play Store Release

This repo is prepared to produce a signed Play Store upload bundle on the machine that owns the upload key.

Current product policy source of truth:

- [LOCKED_DECISIONS.md](../LOCKED_DECISIONS.md)
- [docs/product/MONETIZATION_POLICY.md](product/MONETIZATION_POLICY.md)

## What is already prepared

- Release builds assemble successfully through Gradle.
- Release bundles can be generated with `:app:bundleRelease`.
- `app/build.gradle.kts` reads release signing values from either:
  - `key.properties`
  - environment variables

Supported property names:

- `storeFile`
- `storePassword`
- `keyAlias`
- `keyPassword`

## One-time laptop setup

1. Install Java 17.
2. Install Android Studio or Android command-line tools.
3. Make sure `adb` is available on `PATH`.
4. Clone the repository.
5. From the repo root, run:

```bash
./scripts/bootstrap_laptop_android.sh
```

6. If you want to verify on a real phone immediately after bootstrap:

```bash
RUN_DEVICE_SUITE=1 ./scripts/bootstrap_laptop_android.sh
```

## Physical device verification

Use the device suite before producing the signed upload bundle:

```bash
./scripts/run_adb_device_suite.sh
```

If more than one device is attached:

```bash
DEVICE_SERIAL=<adb-serial> ./scripts/run_adb_device_suite.sh
```

Artifacts are written under:

```text
artifacts/device-test/<timestamp>/
```

## Signed release preparation

Copy the template and fill it on the laptop that owns the upload keystore:

```bash
cp key.properties.template key.properties
```

Required values:

- `storeFile`: absolute path to the upload keystore on that laptop
- `storePassword`
- `keyAlias`
- `keyPassword`

`key.properties` is gitignored and should never be committed.

You can also provide the same values as environment variables instead of a file.

## Build the Play Store bundle

After `key.properties` is present:

```bash
./gradlew :app:bundleRelease
```

Primary output:

```text
app/build/outputs/bundle/release/app-release.aab
```

## Release notes for 1.1.2

Version 1.1.2 (versionCode 12)

- Fully resolved Android 15 edge-to-edge / targetSdk 35 UI constraints and deprecated layout API warnings.
- Replaced legacy system bar overrides with a modern, central inset-safe drawing system (EdgeToEdgeAppScaffold).
- Protected top HUD panels, bottom buttons, and overlay cards across all games from notches and gesture zones.
- Kept the app lightweight, gameplay-stable, and offline-first.

Play Console short notes:

```text
Version 1.1.2 resolves Android 15 edge-to-edge constraints, targetSdk 35 API deprecations, and protects gameplay HUDs, buttons, and popups from cutouts and gesture zones.
```

## Release notes for 1.1.1

Version 1.1.1

- Improved launch affordances and first-open clarity across the three MVP games.
- Added clearer ready-state cues, result-state feedback, and accessibility labels for gameplay overlays and summaries.
- Refined Stack Drop compact controls and compact-width HUD behavior for smaller phones.
- Kept the app offline-first, lightweight, and gameplay-stable while polishing first-run and end-of-run feedback.

Play Console short notes:

```text
Version 1.1.1 improves launch affordances, ready-state cues, compact controls, accessibility labels, and short-run feedback across the three MVP games.
```

## Pre-upload checklist

- Confirm `versionCode` and `versionName` are updated in `app/build.gradle.kts`.
- Confirm the current release build is `versionCode 11` and `versionName 1.1.1`.
- Confirm `versionCode 11` has not already been uploaded to Play Console.
- Re-run `./scripts/run_adb_device_suite.sh` on a physical device.
- Install and sanity-check the release variant locally if needed.
- Confirm Play Store listing assets exist outside the repo:
  - app icon
  - feature graphic
  - screenshots
  - privacy policy URL
  - short and full descriptions
- Confirm the upload key is the same one registered in Play Console.
- Confirm store copy matches the locked MVP policy:
  - 3 games
  - offline core gameplay
  - restrained ads in free version
  - optional one-time premium ad removal

## Current repo gaps for store submission

These are not blocked at the Gradle level, but still need product/release work:

- Release versioning should be bumped for each upload and recorded in the release notes.
- Current release notes should reflect `versionCode 12` and `versionName 1.1.2` after the latest upload.
- Store listing metadata is not tracked in this repo.
- Privacy policy URL is not defined in this repo.
- Launcher/adaptive icon assets should be reviewed before store submission.

## 1.1.2 release checklist

Use this checklist for the next Play Store upload:

- [x] Bump `app/build.gradle.kts` to `versionCode 12` and `versionName 1.1.2`.
- [x] Rebuild the release bundle with the upload keystore configured.
- [x] Integrate regression guard script and check edge-to-edge guidelines.
- [x] Perform a clean install or uninstall-reinstall on physical devices/emulators.
- [x] Cold-launch the app from the launcher and confirm it reaches Home without crashing.
- [x] Verify logcat contains no `fatal exception`, `ANR in`, or `Process crashed` entries during launch.
- [x] Launch the locked MVP games and confirm each one opens successfully.
- [x] Re-run `./gradlew testDebugUnitTest lintDebug`.
- [x] Confirm the Play Console upload uses the new `versionCode 12`.
- [ ] Upload to Play Console.
