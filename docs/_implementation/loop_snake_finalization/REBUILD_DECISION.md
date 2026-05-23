# Loop Snake - Rebuild Decision

## Path Selected: Path B (Rebuild game logic and screen, keeping outer integration)

### Rationale:
1. **Decoupled Architecture**: The current game logic is inline in the Compose `LaunchedEffect` within `LoopSnakeScreen.kt`. This makes it completely impossible to write standard unit tests for movement, collision, eating, speed progression, and direction validation.
2. **Visual Inconsistency**: The prototype uses a black background and basic rect drawings. To fit with the soft light theme of the Offline Mini Arcade, we must recreate the layout using a custom play grid inside a rounded card/arena, styled with the `loopSnakeAccent` color, showing stats cards (Score and Length) in the header, and rendering smooth capsule-style snake body segments and high-quality orb visuals.
3. **Control Reliability**: Simple drag detection needs to be replaced with a robust gesture detection approach that prevents illegal reverse moves and handles fast swipes without corrupting the direction state.
4. **Data Integration**: We need to correctly integrate the engine and UI state with the arcade progression model, reporting completed runs to the local database upon Game Over using `onRunComplete`.
