# Stage 5 Device Validation Plan

## Objective

Execute the deferred runtime validation for the three-game MVP, shared progression, and monetization layers on a connected Android device before any release approval.

## Required Artifacts

- Debug APK
- Release APK if available
- Android test APK
- Device info capture
- Logcat capture
- Screenshot set
- Connected test output
- Manual journey notes

## Install Commands

```bash
./scripts/device/install_debug.sh
./scripts/device/install_release.sh
```

## Connected-Test Commands

```bash
./scripts/device/run_connected_tests.sh
```

## Evidence Capture Commands

```bash
./scripts/device/capture_device_info.sh
./scripts/device/capture_logcat.sh start
./scripts/device/capture_logcat.sh stop
./scripts/device/collect_release_evidence.sh
```

## Manual Journey Checklist

### Application Shell

- Fresh install
- First launch
- Warm launch
- Home navigation
- Settings navigation
- Background and foreground
- Process recreation
- Force-stop and reopen
- Rotation if supported
- Increased font scale

### Pulse Orbit

- `home -> launch -> guidance -> play -> score -> fail -> results -> restart -> home`
- Verify tap response, combo, perfect pass, pause, background pause, rapid restart, sound, vibration, result persistence, and high-score persistence

### Lane Drift

- `home -> launch -> guidance -> play -> collect -> avoid -> collide -> results -> restart`
- Verify swipe response, single-lane shifts, valid routes, near-miss logic, increasing speed, fair collisions, rapid restart, and lifecycle handling

### Stack Drop

- `home -> launch -> guidance -> move -> rotate -> drop -> clear line -> game over -> results -> restart`
- Verify button target sizes, compact-screen usability, move, rotate, soft drop, hard drop, line clearing, pause, high-speed readability, and game-over detection

### Shared Progression

- Verify statistics, currency persistence, theme unlock/application, streak behavior, daily challenges, bundle challenge, duplicate-claim prevention, and offline day behavior

### Monetization

- Verify no ad during active gameplay, ad frequency caps, ad-failure safety, offline behavior, premium purchase flow, cancelled flow, pending flow where testable, restore flow, premium ad suppression, and no subscription UX

## Expected Results

- All three games launch and restart cleanly
- No public legacy game appears
- No ad appears during gameplay
- Premium suppresses ads after purchase and restore
- Progression persists across restart and interruption

## Defect Reporting Template

- Device:
- Android version / API:
- Build:
- Area:
- Reproduction steps:
- Expected result:
- Actual result:
- Screenshot path:
- Logcat path:
- Severity:
- Follow-up fix:
