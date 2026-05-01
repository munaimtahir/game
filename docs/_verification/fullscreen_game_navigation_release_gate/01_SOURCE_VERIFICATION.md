# Source Verification: Full-Screen Game Navigation

## Files Inspected
- `game/pulseorbit/src/main/java/com/vexel/offlinearcade/game/pulseorbit/PulseOrbitScreen.kt`
- `game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftScreen.kt`
- `game/stackdrop/src/main/java/com/vexel/offlinearcade/game/stackdrop/StackDropScreen.kt`
- `feature/home/src/main/java/com/vexel/offlinearcade/feature/home/HomeScreen.kt`
- `core/common/src/main/java/com/vexel/offlinearcade/core/common/AppSupport.kt`

## Verification Checklist

### Pulse Orbit
- [x] `ArcadeScaffold` removed.
- [x] Uses `Column` + `Modifier.fillMaxSize()`.
- [x] `WindowInsets.safeDrawing` applied via `windowInsetsPadding`.
- [x] `BackHandler` implemented: pauses if playing, returns to home if paused/ready/gameover.
- [x] `LifecycleEventObserver` implemented: pauses game on `ON_PAUSE`.
- [x] `onRunComplete` preserved and called correctly on game over.

### Lane Drift
- [x] `ArcadeScaffold` removed.
- [x] Uses `Column` + `Modifier.fillMaxSize()`.
- [x] `WindowInsets.safeDrawing` applied via `windowInsetsPadding`.
- [x] `BackHandler` implemented: pauses if playing, returns to home if paused/ready/gameover.
- [x] `LifecycleEventObserver` implemented: pauses game on `ON_PAUSE`.
- [x] `onRunComplete` preserved and called correctly on game over.

### Stack Drop
- [x] `ArcadeScaffold` removed.
- [x] Uses `Column` + `Modifier.fillMaxSize()`.
- [x] `WindowInsets.safeDrawing` applied via `windowInsetsPadding`.
- [x] `BackHandler` implemented: pauses if playing, returns to home if paused/ready/gameover.
- [x] `LifecycleEventObserver` implemented: pauses game on `ON_PAUSE`.
- [x] `onRunComplete` preserved and called correctly on game over.

### Home Screen
- [x] Retains `ArcadeScaffold` (correct for launcher/dashboard).
- [x] Does not render active gameplay state.
- [x] Correctly triggers navigation to game routes.

### Infrastructure
- [x] Fixed `NewApi` error in `SystemArcadeClock` by using `System.currentTimeMillis()` instead of `LocalDate`.
