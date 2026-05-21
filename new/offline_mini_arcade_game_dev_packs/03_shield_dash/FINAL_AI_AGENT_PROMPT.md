# Final AI Agent Prompt — Implement Shield Dash

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

Implement **Shield Dash** as a separate mini-game candidate inside the existing Offline Mini Arcade app.

Game role: **Defensive reflex game and Lane Drift replacement candidate**

Status: **Recommended production replacement for Lane Drift**

One-line concept:

> A central core is attacked by incoming hazards. The player rotates a shield around the core to block attacks. Perfect blocks and streaks build score and combo.

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

- Core stays at center.
- Shield rotates around core.
- Projectiles/hazards approach from screen edges or circular spawn ring.
- Player rotates shield to face incoming hazard.
- Blocked hazards award score.
- Perfect block awards combo/bonus.
- Missed hazard hits core and ends run or removes one life depending MVP decision.
- Difficulty increases through speed, spawn angle variety, and pattern density.

## Control requirements

- Primary: drag around the core to set shield angle.
- Secondary fallback: tap left/right side to rotate shield incrementally.
- Optional accessibility: sensitivity slider later, not first sprint unless already supported.

## Scoring requirements

- Basic block: +10.
- Perfect block: +20.
- Streak bonus every 5 blocks: +25.
- Combo multiplier after 5, 10, 20 successful blocks.
- Survival bonus: +1 per second.
- Coins: awarded by score milestones and daily completion.

## Difficulty requirements

- First 10 hazards from cardinal/intercardinal directions at slow speed.
- After 10 blocks, speed rises gently.
- After 20 blocks, introduce paired hazards with safe timing gap.
- After 30 blocks, introduce feint warning but keep fairness.
- Never spawn impossible simultaneous hazards in MVP.
- Use telegraph markers before projectile movement.

## Daily challenge hooks

Support or prepare safe integration for:

- Block 30 hazards.
- Get 10 perfect blocks.
- Reach a 15-block streak.
- Survive 45 seconds.
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

`shield_dash`

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

- Do not make it another Pulse Orbit timing-gap game.
- Do not require exact pixel-perfect angle matching.
- Do not flood the screen with too many hazards early.
- Do not use effects that make incoming direction hard to read.

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

- Shield angle follows drag/tap input.
- Projectile angle and path are calculated correctly.
- Block collision succeeds within fair angular tolerance.
- Perfect block uses narrower tolerance than normal block.
- Missed projectile triggers damage/game over.
- Spawn patterns do not create impossible early-state collisions.
- Pause freezes projectiles.
- Restart resets state.
- Shared stats/rewards/daily challenge progress update.

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
