# Layout Risk Review: Full-Screen Game Navigation

## Pulse Orbit
- **Risk:** HUD overlapping with status bar icons.
- **Mitigation:** `WindowInsets.safeDrawing` provides enough top padding to push the HUD below the status bar.
- **Observation:** `Column` with `padding(spacing.md)` inside `windowInsetsPadding` ensures safe margins.

## Lane Drift
- **Risk:** Bottom gesture handle overlapping with the "Start run" button or player zone.
- **Mitigation:** `windowInsetsPadding(WindowInsets.safeDrawing)` protects the bottom area. The `Box(weight(1f))` allows the game board to fill space while respecting the safe area.

## Stack Drop
- **Risk:** Square grid distortion on tall screens.
- **Mitigation:** The board is rendered inside a `BoxWithConstraints` (in sub-components) or `Box` with fixed/aspect-ratio-aware logic.
- **Observation:** `StackDropBoardCard` uses a `Canvas` that fills the available width and a specified `boardHeight`. On tall screens, the `weight(1f)` on the container ensures it doesn't stretch the grid itself, as the `Canvas` uses `size.width / STACK_DROP_WIDTH` and `size.height / STACK_DROP_HEIGHT` for cells.

## Screen Size Risks
- **Compact Width (< 360dp):** Stack Drop has specific `compactHud` and `compactLayout` logic to stack HUD items and reduce board height.
- **Tall Ratio (20:9):** The use of `weight(1f)` for the game board ensures it centers or fills the middle section, leaving room for the HUD and controls without stretching the gameplay elements.
