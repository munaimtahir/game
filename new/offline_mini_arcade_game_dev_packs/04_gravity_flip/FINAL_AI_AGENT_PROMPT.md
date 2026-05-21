# Final AI Agent Prompt — Implement Gravity Flip

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

Implement **Gravity Flip** as a separate mini-game candidate inside the existing Offline Mini Arcade app.

Game role: **Movement survival prototype and alternative Lane Drift replacement**

Status: **Prototype only until compared with Shield Dash**

One-line concept:

> A small runner moves through a side-scrolling tunnel. The player flips gravity or uses hold/release gravity control to avoid hazards and collect stars.

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

- Character moves forward automatically.
- Player changes gravity to move between floor and ceiling or between upward/downward pull.
- Avoid spikes, blocks, gates, lasers, and gaps.
- Collect stars/energy along the path.
- Distance increases score.
- Speed and obstacle density increase over time.
- Collision ends the run.

## Control requirements

- Preferred anti-overlap control: hold to rise / release to fall, or hold to invert gravity and release to normalize.
- Alternative: tap to flip gravity, but only if it does not feel too close to Pulse Orbit.
- Input must be forgiving with short grace periods.

## Scoring requirements

- Distance: +1 per unit or per second.
- Star pickup: +10.
- Clean section bonus: +25.
- Near-miss bonus optional only if collision logic is reliable.
- Combo for consecutive star pickups without collision.

## Difficulty requirements

- Start with wide safe paths.
- Introduce floor/ceiling alternation slowly.
- Add obstacle groups after 20 seconds.
- Increase scroll speed gradually.
- Use generated chunks with guaranteed safe route.
- Never spawn impossible transitions.

## Daily challenge hooks

Support or prepare safe integration for:

- Reach 300 distance.
- Collect 25 stars.
- Complete 5 clean sections.
- Survive 60 seconds.
- Score 500.

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

`gravity_flip`

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

- Do not make the core mechanic 'tap at exact timing window' like Pulse Orbit.
- Do not use circular gap visuals.
- Do not make early obstacle spacing too hard.
- Do not let collision boxes feel unfair.

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

- Gravity input changes vertical acceleration correctly.
- Character collides with floor/ceiling safely.
- Obstacles use fair collision boxes.
- Generated chunks have at least one valid path.
- Star pickups update score and challenge progress.
- Game over triggers on collision.
- Pause freezes movement.
- Restart resets world and score.
- Compare feel against Pulse Orbit and document overlap risk.

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
