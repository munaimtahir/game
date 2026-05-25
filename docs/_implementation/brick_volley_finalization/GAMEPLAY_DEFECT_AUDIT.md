Gameplay Defect Audit - Brick Volley

File inspected: game/brickvolley/src/main/java/com/vexel/offlinearcade/game/brickvolley/BrickVolleyScreen.kt

Summary of defects observed (detailed):

1) Aim / Input
- No minimum drag threshold: tiny accidental taps will enter Aiming and then launch immediately on drag end.
- Aim direction calculation is inverted/confusing: angle uses (start - end) which may invert expected direction; no explicit mapping from drag vector to shot vector.
- No clamp on angle: player can drag nearly horizontal causing near-flat shots that might never return.
- Aim originates from raw touch start point; no fixed launcher/cannon anchor is present.
- Aiming UI is a single line with no projected path or distance scaling.

2) Physics / Collision
- Brick and ball sizes are hardcoded in pixels (brick width 100f, height 50f; ball radius 10f). Not screen-density or layout scaled.
- Collision detection uses contains(newPosition) only — ball can tunnel through bricks if velocity per frame > brick size.
- Velocity is set to (cos(angle)*10, sin(angle)*10) — speed constant and frame-step dependent; no time-step scaling; may vary across devices.
- Wall collision only checks x < 0 or x > width; but bounds use canvasSize width which may let balls go partially off-screen; no radius accounted.
- Brick hp mutation: brick.hp-- mutates data class field directly; bricks likely immutable data classes — code is mutating shared instance possibly causing undefined behavior.
- Removing brick while iterating newBricks may cause ConcurrentModification or logic issues.

3) Gameplay loop / Turn progression
- When all balls return, bricks are advanced by increasing row index without clamping or consistent spawn spacing.
- New bricks spawn with Math.random() and hp = gameState.turn — randomness not seeded and difficulty may spike too quickly.
- Danger line check is arbitrary (row > 10) not tied to canvas or visible danger zone; screen-size dependent.
- Balls list is re-created but launch uses only one ball — spec expects volley of balls or at least configurable count.
- No safe timeout for stuck balls (if balls loop forever due to shallow angles).

4) UI / Accessibility
- No stable test tags for canvas, aim area, score text, restart button. (Detail screen has testTag for start button.)
- Score text drawn via low-level drawText; Compose test framework may not find it via semantics.
- GameOver overlay uses Button without testTag.
- Colors and font sizes hardcoded; may conflict with theme system.

5) Performance / Frame loop
- Uses while loop with delay(16) in LaunchedEffect; not tied to a fixed timestep or delta-time; collision math and speed depend on frame timing.
- Allocates new lists and copies per frame producing garbage.

6) Testability
- Game logic is embedded in Composable LaunchedEffect; hard to unit-test separately from Compose.
- No separation of engine/state and rendering; tests would need to run Compose or instrumented tests.

7) Integration
- BrickVolleyDetailScreen has testTag for start button; Home entry uses testTag "BrickVolleyEntry" which is good. But main canvas lacks testTag.

Immediate severity ranking:
- High: Collision/tunneling, aim inversion, lack of drag threshold, mutating bricks in-place, dangerous row advance logic.
- Medium: Hardcoded pixel sizes (scaling), lack of testTags on canvas and score, single-ball launch.
- Low: Visual polish, theme integration.

Suggested fixes (high-level):
- Extract deterministic game engine (state machine) separate from Compose, implement unit-testable physics with fixed timestep and radius-aware collision.
- Implement launcher anchor and require minimum drag distance; map drag to upward velocity (drag down -> aim up).
- Clamp angle to sensible range (e.g., 15 to 165 degrees) to avoid shallow angles.
- Use scaled sizes (relative to canvas size) not hardcoded pixels.
- Implement robust brick collision (circle-rect collision) and multiple ball volley support.
- Add semantics/testTag to key UI elements: canvas root, score text, round text, restart button, aiming indicator.
- Replace direct mutation of brick.hp with copy-modify pattern.

Files to change:
- game/brickvolley/src/main/java/.../BrickVolleyScreen.kt
- Possibly add new engine class: game/brickvolley/src/main/java/.../engine/*
- tests: unit tests under game/brickvolley/src/test/
- e2e scripts under e2e/brick-volley/ or scripts/

Next actions:
- Design minimal engine and tests, implement aim threshold and clamp, add testTags, and introduce unit tests covering aim and collisions.
