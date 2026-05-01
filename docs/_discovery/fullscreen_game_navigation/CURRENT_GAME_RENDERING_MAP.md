# Current Game Rendering Map

## Pulse Orbit
- **File:** `game/pulseorbit/src/main/java/com/vexel/offlinearcade/game/pulseorbit/PulseOrbitScreen.kt`
- **Component:** `PulseOrbitScreen` Composable.
- **Rendering:** It maintains its own game loop using a `LaunchedEffect` and `withFrameNanos`. The visual rendering is done using a Compose `Canvas` inside a `Box`.
- **Current Wrapper:** It is currently wrapped by `ArcadeScaffold`.

## Lane Drift
- **File:** `game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftScreen.kt`
- **Component:** `LaneDriftScreen` Composable.
- **Rendering:** Uses a `LaunchedEffect` and `withFrameNanos` for the game loop. The rendering is done with `Canvas` to draw lanes, blockers, pickups, and the player.
- **Current Wrapper:** Wrapped by `ArcadeScaffold`.

## Stack Drop
- **File:** `game/stackdrop/src/main/java/com/vexel/offlinearcade/game/stackdrop/StackDropScreen.kt` (Assumed based on pattern, yet to be inspected closely, but architecture is consistent).
- **Component:** `StackDropScreen` Composable.
- **Rendering:** Similar to the others, uses local state, a game loop, and `Canvas` or Compose primitives for grid rendering.
- **Current Wrapper:** Wrapped by `ArcadeScaffold`.

## Ownership
The individual game composables completely own the rendering, the game loop, and the immediate transient state of the active run. They do not rely on a shared game engine service; everything is self-contained in the Composable.