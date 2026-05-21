# Loop Snake — Acceptance Checklist

## Product fit
- Game can be launched and replayed quickly.
- Controls are responsive and readable.
- Game has clear pause/restart/result flow.
- Game has own info screen.
- Visual style is modern and aligned with app identity.
- No internet required.
- No excessive CPU or animation load.
- Daily challenge and shared progression hooks work.

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
