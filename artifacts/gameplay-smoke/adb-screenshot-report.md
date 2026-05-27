# ADB Screenshot Smoke Test Report

- Timestamp: 20260526_090837
- Package: com.vexel.offlinearcade
- Main Activity: .MainActivity

## Screenshots

## Device
- Serial: 08357252AE006901
- Android version: 13

## APK
- Path: ./app/build/outputs/apk/debug/app-debug.apk

- [Splash or launch screen](./01_splash_or_launch.png)
- [Home](./02_home.png)
- [Pulse Orbit detail](./03_pulse_detail.png)
- [Pulse Orbit gameplay ready](./04_pulse_game_ready.png)
- [Pulse Orbit paused](./05_pulse_game_pause.png)
- [Lane Drift detail](./06_lane_detail.png)
- [Lane Drift gameplay ready](./07_lane_game_ready.png)
- [Lane Drift gameplay active](./08_lane_game_active.png)
- [Stack Drop detail](./09_stack_detail.png)
- [Stack Drop gameplay ready](./10_stack_game_ready.png)
- [Stack Drop controls](./11_stack_game_controls.png)
- [Stack Drop paused](./12_stack_game_pause.png)

## Logcat
- [logcat.txt](./logcat.txt)

## Final Status

| Check | Result |
|-------|--------|
| App installed | ✅ YES |
| App launched | ✅ YES |
| Foreground package matched (com.vexel.offlinearcade) | ✅ YES |
| Routes launched | 11 / 11 |
| Screenshots captured after foreground confirmation | 12 |

**RESULT: PASS** — App installed, launched, and foregrounded as `com.vexel.offlinearcade`. All routes attempted. 12 screenshot(s) captured after foreground confirmation.
