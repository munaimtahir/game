# Source Verification: Full-Screen Navigation

## Files Inspected
- `app/src/main/java/com/vexel/offlinearcade/ArcadeNavHost.kt`
- `feature/home/src/main/java/com/vexel/offlinearcade/feature/home/HomeScreen.kt`
- `game/pulseorbit/src/main/java/com/vexel/offlinearcade/game/pulseorbit/PulseOrbitScreen.kt`
- `game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftScreen.kt`
- `game/stackdrop/src/main/java/com/vexel/offlinearcade/game/stackdrop/StackDropScreen.kt`

## Verification Checklist

### 1. Dedicated Full-Screen Layouts
- [x] `PulseOrbitScreen`: `ArcadeScaffold` removed, uses `Column` with `fillMaxSize`.
- [x] `LaneDriftScreen`: `ArcadeScaffold` removed, uses `Column` with `fillMaxSize`.
- [x] `StackDropScreen`: `ArcadeScaffold` removed, uses `Column` with `fillMaxSize`.

### 2. Safe Area / Inset Handling
- [x] All game screens implement `.windowInsetsPadding(WindowInsets.safeDrawing)` on the root `Column`.
- [x] Layouts use `weight(1f)` for the game board to ensure they occupy available space between HUD and system bars.

### 3. Back Button Behavior (`BackHandler`)
- [x] `PulseOrbitScreen`: `BackHandler` pauses during active play, returns `onBack()` otherwise.
- [x] `LaneDriftScreen`: `BackHandler` pauses during active play, returns `onBack()` otherwise.
- [x] `StackDropScreen`: `BackHandler` pauses during active play, returns `onBack()` otherwise.
- [x] Manual "Back" buttons added to screens with `ArcadeTestTags.BackButton` for test compatibility.

### 4. Lifecycle Pause
- [x] All game screens implement `LifecycleEventObserver`.
- [x] Confirmed that `ON_PAUSE` event triggers `paused = true` state in all three games.

### 5. Progression & State Preservation
- [x] `onRunComplete(RunResult)` call is preserved in all three screens inside the `gameOver` block.
- [x] `hasReportedRun` flag used to prevent duplicate reporting.
- [x] High-score and stats reporting wired correctly through `ArcadeNavHost`.
