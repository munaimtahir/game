# Final AI Agent Prompt — Implement Brick Volley

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

Implement **Brick Volley** as a separate mini-game candidate inside the existing Offline Mini Arcade app.

Game role: **Aim + physics + block-clearing game**

Status: **High-confidence production addition**

One-line concept:

> The player drags to aim and releases a volley of balls upward. Balls bounce, damage numbered bricks, and return. After each turn, bricks descend. The run ends when bricks reach the danger line.

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

- Player aims from bottom launcher.
- Player releases a volley of balls.
- Balls bounce off walls and bricks.
- Each brick has HP shown as a number.
- Each hit reduces brick HP by 1.
- Destroyed bricks award score and possible coins.
- After all balls return or expire, the brick field advances downward.
- New brick row spawns at the top.
- Game ends if any brick reaches the bottom danger zone.

## Control requirements

- Drag from launcher to aim.
- Show clear aiming guide line.
- Release to fire.
- Cancel shot if finger returns close to launcher before release.

## Scoring requirements

- Brick hit: +1 point.
- Brick destroyed: +5 points.
- Full row cleared before descent: +10 bonus.
- Board cleanup bonus: +25 if no bricks remain after a volley.
- Combo: consecutive turns with at least 3 bricks destroyed gives +combo bonus.
- Coins: 1 coin per 50 score, capped per run if app has reward caps.

## Difficulty requirements

- Start with 3-4 bricks per new row.
- Brick HP starts 1-3.
- Every 5 turns increase average HP by 1.
- Every 8 turns increase row density slightly.
- After score milestone, introduce guarded bricks only if performance remains stable.
- Danger line should be visually clear and fair.

## Daily challenge hooks

Support or prepare safe integration for:

- Clear 40 bricks.
- Survive 12 turns.
- Destroy 5 bricks in one volley.
- Score 250 points.
- Clear the board once.

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

`brick_volley`

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

- Do not create excessive particles that hurt low-end devices.
- Do not add complex power-ups in first sprint.
- Do not make the board visually noisy.
- Do not require internet or server-generated levels.

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

- Aiming vector is calculated correctly.
- Volley launches only after release.
- Balls collide with walls and bricks.
- Brick HP decreases correctly.
- Destroyed bricks are removed once only.
- Turn advances only after all balls return/expire.
- Game over triggers when brick crosses danger line.
- Restart resets board and score.
- High score/session tracking updates.
- Daily challenge progress updates.

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
