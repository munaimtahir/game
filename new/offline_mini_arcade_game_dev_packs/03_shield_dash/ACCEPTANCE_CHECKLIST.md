# Shield Dash — Acceptance Checklist

## Product fit
- Game feels clearly different from Pulse Orbit.
- Player can understand objective within 5 seconds.
- Incoming attacks are readable.
- Controls are responsive.
- Game supports pause/restart/result flow.
- Game works offline.
- Game integrates with existing app shell and progression.
- Lane Drift can be hidden/replaced without breaking navigation.

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
