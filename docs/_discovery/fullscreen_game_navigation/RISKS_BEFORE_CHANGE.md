# Risks Before Change

## Layout Risks
Removing `ArcadeScaffold` means we lose the automatic padding that keeps content away from the edges. If we simply use `fillMaxSize()`, game controls (like Stack Drop buttons or the pause button) might overlap with system navigation bars, gesture handles, or the camera notch. We must explicitly use `WindowInsets` (`safeDrawing`, `systemBars`) to ensure the play area is safe.

## Back Button Risks
Currently, `ArcadeScaffold` provides a top app bar with a back button. Physical back presses are handled by the default navigation controller (popping the backstack). 
If we remove `ArcadeScaffold`, we need:
1. A custom on-screen pause/back HUD element.
2. A `BackHandler` in Compose.
The required behavior is:
- Back on ready screen -> return home.
- Back during active play -> PAUSE game (do not exit).
- Back when paused -> return home.
- Back when game over -> return home.

## Lifecycle Risks
The game loops run in `LaunchedEffect(state.playing)`. If the app is backgrounded, Compose lifecycle might pause the effect, but upon resume, it might automatically continue. The requirement states: "App resume: return to paused or ready state, not unsafe auto-resume." We need to ensure that when the app goes to the background, the game state flips to `paused = true`.

## Low-End Performance
We should avoid adding complex transition animations when navigating from Home to the game screens. The `Canvas` rendering should remain lightweight.

## Testing Gaps
We must verify that `HomeScreen` tests don't expect the old layout, and that navigation tests explicitly check the full-screen game screens. We must manually test the back button and lifecycle behaviors thoroughly as they are hard to unit test reliably.