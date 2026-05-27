# Risk Register

Date: 2026-05-26

## 1. Emulator Boot Instability
- Risk: GitHub-hosted Ubuntu runners can still have flaky emulator boot timing.
- Mitigation:
  - use `android-emulator-runner`
  - wait for `sys.boot_completed`
  - disable animations
  - collect logcat and UI dump artifacts even on failure

## 2. Stalled Unit Tests
- Risk: a single bad test can block the entire JVM test lane.
- Mitigation:
  - keep tests deterministic
  - avoid unbounded loops in test code
  - add targeted module tests when a failure is isolated
- Example from this pass:
  - `LoopSnakeEngineTest.testSelfCollisionTriggersGameOver` was rewritten to avoid an unbounded growth loop.

## 3. Local Device Contamination
- Risk: the workstation has a physical adb device connected (`08357252AE006901`), which can accidentally get used as a verification target.
- Mitigation:
  - keep GitHub Actions as the default evidence loop
  - require explicit `DEVICE_SERIAL` when running local adb helpers

## 4. Screenshot State Gaps
- Risk: generic screenshot scripts may not prove the exact ready/active/game-over states requested for each game.
- Mitigation:
  - keep the current baseline
  - add deterministic debug launch hooks only when a game sprint needs exact screenshot states

## 5. Scope Creep Across Games
- Risk: a single sprint can easily drift into multi-game polishing.
- Mitigation:
  - one game at a time
  - Lane Drift first
  - do not start Pulse Orbit or Stack Drop implementation work before Lane Drift has a clean report

## 6. Platform Compatibility Warning
- Risk: AGP 8.5.2 still warns about `compileSdk 35`.
- Mitigation:
  - record the warning in CI artifacts
  - treat it as a toolchain follow-up, not a gameplay blocker
