# Offline Mini Arcade

Offline Android arcade app with the locked MVP scope:

- Pulse Orbit
- Lane Drift
- Stack Drop
- Shared progression
- Daily challenges
- Local stats
- Minimal settings
- Offline-first only

## Laptop Setup After Clone

Requirements:

- Java 17
- Android SDK / Android Studio
- `adb` on `PATH`
- One physical Android device with USB debugging enabled for device validation

Bootstrap the repo on a fresh laptop:

```bash
./scripts/bootstrap_laptop_android.sh
```

That bootstrap checks the toolchain and runs:

- `testDebugUnitTest`
- `:app:assembleDebug`
- `:app:assembleDebugAndroidTest`
- `:app:assembleRelease`

To run the real-device suite right after bootstrap:

```bash
RUN_DEVICE_SUITE=1 ./scripts/bootstrap_laptop_android.sh
```

Or run the device suite directly:

```bash
./scripts/run_adb_device_suite.sh
```

If multiple devices are attached:

```bash
DEVICE_SERIAL=<adb-serial> ./scripts/run_adb_device_suite.sh
```

## Key Docs

- Device testing: [docs/ADB_DEVICE_TESTING.md](docs/ADB_DEVICE_TESTING.md)
- Play Store release prep: [docs/PLAY_STORE_RELEASE.md](docs/PLAY_STORE_RELEASE.md)
- Implementation plan: [docs/IMPLEMENTATION_PLAN.md](docs/IMPLEMENTATION_PLAN.md)
