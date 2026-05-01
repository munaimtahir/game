# State and Progression Flow

## Transient Game State
Each game screen (e.g., `PulseOrbitScreen`) maintains its own active state using Compose `remember { mutableStateOf(...) }` (e.g., `PulseOrbitState`). This includes score, combo, active entities, speed, and game over status.

## Run Completion
When a game reaches a "Game Over" state, it triggers a side-effect. If `state.gameOver` is true and it hasn't reported the run yet, it invokes the `onRunComplete(RunResult)` callback provided by the parent.

## Global Progression
The `onRunComplete` callback is wired through `ArcadeNavHost` to the `ArcadeViewModel`.
The `ArcadeViewModel` calls `repository.recordRun(result)`.
The `OfflineArcadeRepository` handles persisting the result:
- Updates high scores.
- Increments session counts.
- Adds earned coins to the player profile.
- Updates progress for Daily Challenges.

## Refactor Risks
During the removal of `ArcadeScaffold` and the restructuring of the UI:
- We must ensure we do not accidentally remove the `if (state.gameOver && !hasReportedRun) { ... onRunComplete(...) }` logic.
- We must ensure the `pause` state can be triggered externally via the physical back button, which requires wiring a `BackHandler` properly into the local game state.