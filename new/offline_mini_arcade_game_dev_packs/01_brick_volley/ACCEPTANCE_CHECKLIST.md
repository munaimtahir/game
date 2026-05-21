# Brick Volley — Acceptance Checklist

## Product fit
- Game is playable from existing home/game list.
- Game has info screen with how-to-play text.
- Game has pause, restart, and result flow.
- No crash during 5-minute repeated play.
- Works offline.
- Does not degrade performance on low-end target settings.
- Visual style matches current light rounded arcade UI.
- Game progress connects to shared stats/reward system.

## Gameplay
- [ ] Core loop is implemented.
- [ ] Controls work reliably.
- [ ] Failure state is fair and understandable.
- [ ] Difficulty progression exists.
- [ ] Score updates correctly.
- [ ] Secondary stat updates correctly.
- [ ] Pause works.
- [ ] Restart works.
- [ ] Back/exit works.

## Integration
- [ ] Game appears in app/game registry.
- [ ] Game info screen exists.
- [ ] Game play screen exists.
- [ ] High score persists.
- [ ] Session count persists.
- [ ] Daily challenge hooks are present or safely stubbed.
- [ ] Soft currency hooks are present or safely stubbed.
- [ ] Existing games are not broken.

## UI
- [ ] Visual style matches current app.
- [ ] HUD is readable.
- [ ] Touch targets are adequate.
- [ ] Gameplay area is not blocked by HUD.
- [ ] Light theme contrast is acceptable.
- [ ] Small-screen layout is usable.

## Technical
- [ ] Build passes.
- [ ] Unit tests pass.
- [ ] Lint passes or documented existing warnings only.
- [ ] No heavy dependency added without reason.
- [ ] Works offline.
- [ ] No internet permission/dependency introduced for core play.
- [ ] No active gameplay ads.

## Final verdict
- [ ] GO
- [ ] CONDITIONAL GO
- [ ] NO-GO
