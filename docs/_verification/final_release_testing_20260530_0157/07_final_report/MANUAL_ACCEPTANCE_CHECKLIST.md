# Manual Acceptance Checklist (Locked 3-Game MVP)

## General App Status
- [x] App cold launches to Home without crash (Verified via explicit Activity start and screenshots)
- [x] App background/foreground resume works (Verified via lifecycle resume logs)
- [x] Back navigation behaves correctly, popping stack where appropriate

## Home Library
- [x] Shows only the 3 MVP games (Pulse Orbit, Lane Drift, Stack Drop)
- [x] Games count text and headers reference only the 3 MVP games
- [x] Non-MVP games (Brick Volley, Loop Snake, Shield Dash) are completely hidden from the public Home screen list

## Gameplay Verification
- [x] Pulse Orbit launches, gameplay functions normally, and records results successfully
- [x] Lane Drift launches, controls respond, and collision/traffic flow is active
- [x] Stack Drop launches, accepts grid interactions, and clears score line states

## Shared Systems & Progression
- [x] Daily Challenge Generator includes tasks only for Pulse Orbit, Lane Drift, and Stack Drop
- [x] Player stats and high scores only reference the active 3 MVP games
- [x] Coin balance increases with gameplay results
- [x] Theme unlock transitions reflect correctly in Marketplace preview grid

## Play Store Readiness
- [x] App targetSdk meets Google Play requirements (SDK 35)
- [x] Minimal permissions (VIBRATE only) requested in AndroidManifest.xml
- [x] Release version is updated to `versionCode 5` / `versionName 1.0.4`
- [x] Release bundle (.aab) successfully compiled with active optimization
- [x] Signing key configurations match setup configurations locally without leaks
