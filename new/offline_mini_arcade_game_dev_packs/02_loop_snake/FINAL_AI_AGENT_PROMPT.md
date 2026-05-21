# Final AI Agent Prompt — Implement Loop Snake

You are working on the Offline Mini Arcade Android project.

## Mandatory continuity / handoff instruction

Before implementation, inspect the current repository and create or overwrite a root-level `copilot_session.md` file.

The file must include:

1. Current repo understanding
2. Existing game architecture summary
3. Implementation checklist
4. Files expected to change
5. Test commands to run
6. Risk list
7. Progress log

Update `copilot_session.md` after every major step so another AI agent can continue the work without restarting discovery.

## Objective

Implement **Loop Snake** as a separate mini-game candidate inside the existing Offline Mini Arcade app.

Game role: **Classic arcade survival game**

Status: **Strong production addition**

One-line concept:

> A modern Snake-style game with a clean rounded arena. The snake collects orbs, grows, builds combo, avoids walls/body/obstacles, and chases high score.

## Existing app context

The app already has these games:

- Pulse Orbit
- Lane Drift
- Stack Drop

The app philosophy is:

- offline-first
- lightweight
- low-end Android friendly
- fast open → choose → play → retry flow
- clean bright rounded UI
- no bloated game count
- no aggressive monetization
- no ads during active gameplay

Do not break existing games.

## Required reading before coding

Read these pack files first:

- `GAME_DESIGN.md`
- `GAME_RULES.md`
- `SCORING_AND_DIFFICULTY.md`
- `UI_UX_SPEC.md`
- `INTEGRATION_CONTRACT.md`
- `TEST_PLAN.md`
- `ACCEPTANCE_CHECKLIST.md`

Also inspect the current codebase to understand:

- current game registry/navigation pattern
- existing game screen architecture
- state management approach
- stats persistence
- daily challenge hooks
- theme system
- pause/restart/result handling
- existing tests and CI workflows

## Gameplay requirements

Implement this core loop:

- Snake moves continuously on a grid or soft-grid arena.
- Player changes direction with swipe or optional directional buttons.
- Collect regular orb to score and grow.
- Collect golden timed orb for bonus.
- Avoid arena edges, body collision, and later obstacles.
- Speed increases gradually.
- Game ends on collision.

## Control requirements

- Primary: swipe up/down/left/right.
- Optional accessibility: four simple direction buttons in settings or game overlay.
- Ignore direct reverse input if it would instantly collide with body.

## Scoring requirements

- Regular orb: +10.
- Golden orb: +50.
- Combo: collecting next orb within target time adds +5, +10, +15 scaling bonus.
- Survival bonus: +1 per second.
- Obstacle near-miss bonus optional: +2, only if easy to implement safely.
- Coins: milestone-based, not every orb if inflation is a concern.

## Difficulty requirements

- Start slow and readable.
- Increase speed every 5 orbs.
- Spawn first static obstacle after 12 orbs.
- Increase obstacle count slowly.
- Golden orb appears every 5-7 regular orbs and expires after 3-4 seconds.
- Do not make early game punishing.

## Daily challenge hooks

Support or prepare safe integration for:

- Collect 20 orbs.
- Reach length 15.
- Collect 3 golden orbs.
- Survive 60 seconds.
- Score 300 without wall hit.

If the daily challenge system is not ready for direct integration, create clean TODO/stub hooks without breaking current app behavior.

## UI requirements

- Add game info/details screen.
- Add playable game screen.
- Add ready state.
- Add pause state.
- Add game-over/result state.
- Add fast restart.
- Use existing app theme and rounded light UI style.
- Use readable HUD with score and one secondary stat.
- Respect sound/music/vibration settings if currently available.
- Do not add noisy visual clutter.

## Integration requirements

Use suggested game ID:

`loop_snake`

Connect to or safely prepare hooks for:

- high score
- session count
- total play time
- daily challenge progress
- soft currency rewards
- cosmetic/theme compatibility
- local stats

Do not require internet. Do not add account/login dependency.

## Safety and non-overlap requirements

- Do not make it look like a plain black old-phone Snake clone.
- Do not make controls feel delayed.
- Do not spawn food inside snake body or unreachable cells.
- Do not add complex mission UI into gameplay screen.

## Testing requirements

Run at minimum:

```bash
./gradlew clean assembleDebug
./gradlew testDebugUnitTest
./gradlew lint
```

If the repository has connected/emulator tests configured and environment supports it, also run:

```bash
./gradlew connectedDebugAndroidTest
```

Add or update tests for:

- Snake advances on tick.
- Swipe changes direction correctly.
- Invalid reverse direction is blocked.
- Food never spawns on snake body.
- Food collection increases score and length.
- Golden orb expires.
- Self-collision ends game.
- Wall collision ends game.
- Restart resets state.
- High score/session/daily challenge progress updates.

Also verify manually or through existing emulator workflow:

- app launches
- game info opens
- gameplay starts
- pause/resume works
- restart works
- game over works
- high score/session count persist
- existing games still work

## Evidence output required

At the end, produce a final report in the chat and in `copilot_session.md` containing:

1. Summary of implementation
2. Files changed
3. Gameplay behavior implemented
4. Integration points completed
5. Tests run with pass/fail results
6. Known limitations
7. Final verdict: GO / CONDITIONAL GO / NO-GO

## Acceptance checklist

Use `ACCEPTANCE_CHECKLIST.md` as the final gate. Do not claim GO unless all critical acceptance items are complete and build/tests pass.
