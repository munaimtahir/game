# Layout Risk Review

## Game-Specific Layout Analysis

### Pulse Orbit
- **Risk:** Center orbit might feel too small on extremely tall screens.
- **Observation:** Code uses `min(size.width, size.height) * 0.28f` for radius, which is safe.

### Lane Drift
- **Risk:** 3-lane track might be partially obscured by the bottom navigation bar if padding is insufficient.
- **Observation:** `WindowInsets.safeDrawing` handles the bottom navigation area correctly. `playerZoneY` is set to `0.88f` to keep the player low but safe.

### Stack Drop
- **Risk:** UI might become cluttered on small screens (< 360dp width).
- **Observation:** Screen implements `compactHud` and `compactLayout` logic using `BoxWithConstraints`. This successfully adapts HUD from Row to Column on narrow devices.

## Device Class Risks
- **Foldables:** If the app is run on a unfolded screen, the game boards might look extremely wide. (Out of scope for current MVP, but a minor risk).
- **Notches/Islands:** The HUD sits in the top section which is now protected by `safeDrawing`. No clipping expected.
