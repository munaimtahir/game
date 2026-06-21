# Play Store Release

This repo is prepared to produce a signed Play Store upload bundle on the machine that owns the upload key.

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

## Release notes for 1.1.0

Version 1.1.0

- Improved final-release clarity across the 3 MVP games with stronger launch affordances and more game-specific detail screens.
- Added clearer ready-state cues, result-state feedback, and accessibility labels for gameplay overlays and summaries.
- Refined Stack Drop compact controls and compact-width HUD behavior for smaller phones.
- Kept the app offline-first, lightweight, and gameplay-stable while polishing first-run and end-of-run feedback.

Play Console short notes:

```text
Version 1.1.0 improves launch affordances, ready-state cues, compact controls, accessibility labels, and short-run feedback across the 3 MVP games.
```

## Pre-upload checklist

- Confirm `versionCode` and `versionName` are updated in `app/build.gradle.kts`.
- Confirm the current release build is `versionCode 10` and `versionName 1.1.0`.
- Confirm `versionCode 10` has not already been uploaded to Play Console.
- Re-run `./scripts/run_adb_device_suite.sh` on a physical device.
- Install and sanity-check the release variant locally if needed.
- Confirm Play Store listing assets exist outside the repo:
  - app icon
  - feature graphic
  - screenshots
  - privacy policy URL
  - short and full descriptions
- Confirm the upload key is the same one registered in Play Console.

## Current repo gaps for store submission

These are not blocked at the Gradle level, but still need product/release work:

- Release versioning should be bumped for each upload and recorded in the release notes.
- Current release notes should reflect `versionCode 10` and `versionName 1.1.0` after the latest upload.
- Store listing metadata is not tracked in this repo.
- Privacy policy URL is not defined in this repo.
- Launcher/adaptive icon assets should be reviewed before store submission.
