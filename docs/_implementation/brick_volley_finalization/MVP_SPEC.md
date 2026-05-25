Brick Volley - Target MVP Spec

Core loop (concise):
1. Ready state visible with launcher at bottom center and brick grid above.
2. Player drags from launcher downward/back to set aim; visible guide shows projected direction.
3. Minimum drag threshold required; small taps ignored.
4. Release to shoot a volley of N balls (start with N=1, allow future increase).
5. Balls bounce off walls and bricks; each brick hit decrements HP by 1; bricks disappear at 0.
6. When all active balls return to catcher zone (bottom) or a safe timeout hits, the turn ends.
7. Remaining bricks advance one row; new row spawns at top; game over if a brick reaches danger line.
8. Player can restart instantly from Game Over.

Controls:
- Drag to aim, release to shoot. Drag direction maps intuitively to shot direction (dragging down/back shoots up).
- Clamp aim angle to avoid shallow shots (< 15° from horizontal).
- Minimum drag distance threshold.

Physics/Design choices:
- Use a timestep-based engine (fixed dt) so behavior is deterministic and testable.
- Use circle-rect collision for ball-brick collisions with radius-aware response.
- Speeds and sizes scaled relative to canvas size.
- Single-ball volley initially; make engine support multiple balls.

Scoring:
- +1 per hit. Bonus +5 for destroying a brick.
- High score recorded via existing core highscore hook.

Testing:
- Engine unit tests for aim calculation, angle clamp, collision decrement, turn end, row advance, game over.
- Compose tests for route opening, Ready state, start button, and game-over overlay.
- ADB e2e script to perform drag gesture, capture screenshots, logcat, and UI dump.

UI test tags to add:
- "BrickVolleyRoot" (game screen root/canvas)
- "BrickVolleyAimArea"
- "BrickVolleyScore"
- "BrickVolleyTurn"
- "BrickVolleyRestart"

Performance:
- No heavy assets; avoid allocations in the frame loop.
- Keep visuals simple and theme-compatible.
