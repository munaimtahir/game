# Current State Audit

## Current Navigation Routes
Currently defined in `ArcadeNavHost.kt`:
- `Home`
- `PulseOrbit`
- `LaneDrift`
- `StackDrop`
- `Challenges`
- `Stats`
- `Settings`

There are no separate "Detail" vs "Gameplay" screens. The game screens themselves act as both landing pages (Ready state with start buttons/instructions) and active gameplay areas.

## Current Component Map
- `HomeScreen`: Uses `ArcadeScaffold`, `LazyColumn`. Includes `HeroPanel` and multiple `GameEntryCard`s.
- `SplashShell`: Centered `Column` with app initials, title, subtitle, and progress indicator.
- `PulseOrbitScreen`: Contains HUD (Score, Combo, Best, Back/Pause buttons), central Canvas board, feedback text, and overlays for Pause/GameOver.
- `LaneDriftScreen`: Contains HUD, debug text ("Traffic: ..."), Canvas board for lanes, and start/pause/gameover cards.
- `StackDropScreen`: Layout switches based on width constraint. Contains `StackDropStartCard` with long descriptions ("Cobalt and amber mastery..."), `StackDropBoardCard` (Canvas), and overlays.
- `AppScaffold`: Contains `ArcadeScaffold`, `ArcadeCard`, `PremiumButton`, `HudPill`, `PremiumBadge`, `SectionHeader`, `HeroPanel`, `PremiumOverlayCard`.

## Current Theme/Color System
Located in `Theme.kt`:
- Defined colors like `MidnightBackground`, `MidnightSurface`, `MidnightCard`, `Indigo`, `Aqua`, etc.
- `ArcadeExtendedColors` provides gradients (`shellGradient`, `panelGradient`, `heroGradient`).
- Some texts (e.g. in `HeroPanel`) might be getting squeezed, leading to vertical stacking.
- Splash screen title uses `MaterialTheme.typography.displayMedium` with default text color against `shellGradient`, which may be low contrast.
- Contrast failures likely arise from `onSurfaceVariant` or `onPrimary` not contrasting enough with their respective backgrounds, especially on gradients.

## Issues Identified
1. **Vertical Text Stacking:** In `HeroPanel`, `Row` uses `Modifier.weight(1f)` for the text column, but depending on the trailing column width, the text gets crushed. It needs a responsive flow (e.g., vertical on compact screens).
2. **Clipping:** `GameEntryCard` and `ArcadeCard` might clip contents if fixed heights or insufficient padding is used, especially on small screens.
3. **Detail/Gameplay Mix:** `StackDropStartCard` contains long descriptions within the game screen. All game screens need splitting into `*Detail` and `*Game` screens.
4. **Lane Drift Bounds:** Gameplay objects spawn and move based on screen height, lacking strict `clipToBounds` bounds logic separating the board from HUD.
5. **Pulse Orbit Contrast:** The score values inside `HudPill` and text within `Canvas` might have contrast issues.
6. **Stack Drop Controls:** Currently purely gesture-based. Prompt requests visible controls (left, right, rotate, drop).