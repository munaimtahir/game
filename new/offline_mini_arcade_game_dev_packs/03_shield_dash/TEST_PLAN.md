# Shield Dash — Test Plan

## Unit/state tests
- Shield angle follows drag/tap input.
- Projectile angle and path are calculated correctly.
- Block collision succeeds within fair angular tolerance.
- Perfect block uses narrower tolerance than normal block.
- Missed projectile triggers damage/game over.
- Spawn patterns do not create impossible early-state collisions.
- Pause freezes projectiles.
- Restart resets state.
- Shared stats/rewards/daily challenge progress update.

## UI/manual tests
- Launch app.
- Open game info screen.
- Start game.
- Confirm ready instruction appears.
- Play for at least 60 seconds or until failure.
- Pause and resume.
- Restart from game over.
- Exit to home.
- Reopen game and confirm high score/session state persists.

## Regression tests
- Pulse Orbit still opens and plays.
- Stack Drop still opens and plays.
- Lane Drift behavior is unchanged unless this sprint intentionally hides/replaces it.
- Home screen/game list does not crash.
- Stats screen does not crash.
- Daily challenge screen does not crash.
- Settings sound/music/vibration still respected.

## Android/emulator checks
- Run Gradle build.
- Run unit tests.
- Run lint.
- Run existing instrumentation/emulator tests if configured.
- Add screenshot capture for new game info and active play screens if existing workflow supports screenshots.

## Suggested commands
Adapt to actual repo structure after inspection:

```bash
./gradlew clean assembleDebug
./gradlew testDebugUnitTest
./gradlew lint
```

If connected tests are already configured:

```bash
./gradlew connectedDebugAndroidTest
```

## Evidence required from AI agent
- List of changed files.
- Test commands executed.
- Pass/fail output summary.
- Screenshots if emulator workflow supports it.
- Final GO / CONDITIONAL GO / NO-GO verdict.
