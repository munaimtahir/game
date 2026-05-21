# Offline Mini Arcade — Separate AI Dev Packs

Generated: 2026-05-21

This folder contains separate implementation-ready AI developer packs for candidate games.

## Recommended execution order

1. `01_brick_volley` — strongest new fourth-game candidate.
2. `02_loop_snake` — classic arcade survival addition.
3. `03_shield_dash` — strongest Lane Drift replacement candidate.
4. `04_gravity_flip` — prototype only; compare against Shield Dash because of possible overlap with Pulse Orbit.

## Pack structure

Each game folder contains:

- `GAME_DESIGN.md`
- `GAME_RULES.md`
- `SCORING_AND_DIFFICULTY.md`
- `UI_UX_SPEC.md`
- `INTEGRATION_CONTRACT.md`
- `TEST_PLAN.md`
- `ACCEPTANCE_CHECKLIST.md`
- `FINAL_AI_AGENT_PROMPT.md`

## Standard agent continuity instruction

Every implementation prompt starts with a requirement to create or overwrite `copilot_session.md` in the repository root and keep it updated throughout the sprint.

## Final portfolio recommendation

Preferred five-game set after successful testing:

1. Pulse Orbit
2. Stack Drop
3. Shield Dash
4. Brick Volley
5. Loop Snake

Gravity Flip should remain a prototype until it proves that it does not feel like another Pulse Orbit-style tap timing game.
