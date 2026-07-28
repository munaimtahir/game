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

Authoritative Stage 0 documents:

- [PROJECT_CONTEXT_MASTER.md](PROJECT_CONTEXT_MASTER.md)
- [LOCKED_DECISIONS.md](LOCKED_DECISIONS.md)
- [docs/product/PRODUCT_SPECIFICATION.md](docs/product/PRODUCT_SPECIFICATION.md)
- [docs/product/TECHNICAL_ARCHITECTURE.md](docs/product/TECHNICAL_ARCHITECTURE.md)
- [docs/product/IMPLEMENTATION_BACKLOG.md](docs/product/IMPLEMENTATION_BACKLOG.md)

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

Monetization architecture, consent, ad cadence, rewarded coins, and release configuration are documented in [docs/product/MONETIZATION_IMPLEMENTATION.md](docs/product/MONETIZATION_IMPLEMENTATION.md).

## Key Docs

- Project Context & Monetization Index: [docs/README.md](docs/README.md)
- Locked Decisions: [docs/LOCKED_DECISIONS.md](docs/LOCKED_DECISIONS.md)
- Master Project Context: [docs/PROJECT_CONTEXT_MASTER.md](docs/PROJECT_CONTEXT_MASTER.md)
- Product Rules & Guardrails: [docs/PRODUCT_RULES_AND_GUARDRAILS.md](docs/PRODUCT_RULES_AND_GUARDRAILS.md)
- Build Planning & Next Steps (Monetization): [docs/BUILD_PLANNING_NEXT_STEPS.md](docs/BUILD_PLANNING_NEXT_STEPS.md)
- Next Chat Bootstrap: [docs/NEXT_CHAT_BOOTSTRAP.md](docs/NEXT_CHAT_BOOTSTRAP.md)
- MVP Game Specs: [docs/GAME_SPECS_MVP.md](docs/GAME_SPECS_MVP.md)
- Device testing: [docs/ADB_DEVICE_TESTING.md](docs/ADB_DEVICE_TESTING.md)
- Play Store release prep: [docs/PLAY_STORE_RELEASE.md](docs/PLAY_STORE_RELEASE.md)
- Implementation plan: [docs/IMPLEMENTATION_PLAN.md](docs/IMPLEMENTATION_PLAN.md)
- Locked product decisions: [LOCKED_DECISIONS.md](LOCKED_DECISIONS.md)
