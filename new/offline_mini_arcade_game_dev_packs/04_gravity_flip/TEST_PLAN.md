# Gravity Flip — Test Plan

## Unit/state tests
- Gravity input changes vertical acceleration correctly.
- Character collides with floor/ceiling safely.
- Obstacles use fair collision boxes.
- Generated chunks have at least one valid path.
- Star pickups update score and challenge progress.
- Game over triggers on collision.
- Pause freezes movement.
- Restart resets world and score.
- Compare feel against Pulse Orbit and document overlap risk.

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
