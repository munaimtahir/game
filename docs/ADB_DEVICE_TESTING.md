# ADB Device Testing

This project includes an adb-driven physical-device suite that installs the debug APKs, runs the Android instrumentation tests, and saves artifacts locally.

## What it covers

- Locked MVP app shape preflight:
  - exactly 3 game modules
  - required shared feature modules
  - required smoke and persistence test sources
- Local unit-test preflight through `testDebugUnitTest`
- Release variant assembly verification through `:app:assembleRelease`
- App route navigation smoke checks
- Settings screen survival across activity recreate
- Pulse Orbit start flow
- Lane Drift start flow plus live traffic spawn verification
- Stack Drop start flow plus control reachability verification

## Prerequisites

- A physical Android device connected through `adb`
- USB debugging enabled on the device
- Android SDK / `adb` available locally

## Run the suite

```bash
./scripts/run_adb_device_suite.sh
```

Skip local preflights or release verification when you intentionally want a narrower device-only run:

```bash
SKIP_PRECHECKS=1 SKIP_RELEASE_CHECK=1 ./scripts/run_adb_device_suite.sh
```

If multiple devices are connected, target one explicitly:

```bash
DEVICE_SERIAL=<adb-serial> ./scripts/run_adb_device_suite.sh
```

## Outputs

Each run writes artifacts into:

```text
artifacts/device-test/<timestamp>/
```

Files include:

- `prechecks.txt`
- `build-debug.txt`
- `build-release.txt`
- `instrumentation.txt`
- `logcat.txt`
- install logs
- device metadata

## Notes

- The script clears app data before running so saved settings and old state do not pollute results.
- The script installs both the main app APK and the `androidTest` APK before invoking `am instrument`.
- If the APKs are missing, the script builds them automatically.
- Release assembly is used as a packaging sanity check only. It does not sign an upload-ready Play Store bundle.
