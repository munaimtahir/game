# Color Contrast Audit - Gameplay Readability

**Date:** 2026-05-01
**Sprint Focus:** Color, contrast, luminance, gameplay visibility.

## Files Inspected
- `core/ui/src/main/java/com/vexel/offlinearcade/core/ui/Theme.kt`
- `core/ui/src/main/java/com/vexel/offlinearcade/core/ui/AppScaffold.kt` (GameplayScaffold, HudPill)
- `game/pulseorbit/src/main/java/com/vexel/offlinearcade/game/pulseorbit/PulseOrbitScreen.kt`
- `game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftScreen.kt`
- `game/stackdrop/src/main/java/com/vexel/offlinearcade/game/stackdrop/StackDropScreen.kt`
- `game/stackdrop/src/main/java/com/vexel/offlinearcade/game/stackdrop/StackDropEngine.kt`

## Root Causes of Dim Gameplay
1. **Dark Shell Background:** The `shellGradient` used in `GameplayScaffold` is very dark (`PremiumBackground` = #07090E to `PremiumSurface` = #131A2A). While premium, it provides a very low-luminance backdrop for everything else.
2. **Subtle Game Board Separation:** Game boards use `PremiumSurface` (#131A2A) which is almost identical to the bottom part of the shell gradient. This makes the playfield blend into the background.
3. **HUD Contrast:** `HudPill` uses `PremiumCard` (#1F293F) for background and `OutlineColor` (#334155) for border. The contrast between these is low.
4. **Lane Drift Invisibility:** Inactive lanes use `PremiumSurface`, and the board uses `PremiumSurface`. Active lane uses `PremiumCard`. The difference is only ~12 units of hex value (0x13 vs 0x1F), which is extremely subtle on OLED/high-contrast screens and potentially invisible on lower-quality LCDs.
5. **Pulse Orbit Dimming:** The ring uses `PremiumAction` (#00E5FF) which is bright, but the orb uses `PremiumCobalt` (#2979FF) which is a medium-dark blue. On a near-black background, it might not pop enough.
6. **Overlay Scrim:** The `GameplayScaffold` uses a 70% black alpha scrim for overlays. This is appropriate for focus but if the underlying content is already very dark, it might make the overlay cards feel "heavy".

## Components Using Weak Colors
- **HudPill Border:** Uses `OutlineColor` (#334155).
- **HudPill Background:** Uses `PremiumCard` (#1F293F).
- **Lane Drift Board/Inactive Lanes:** Uses `PremiumSurface` (#131A2A).
- **Pulse Orbit Core:** Uses `primaryContainer` (`PremiumCard`).
- **Pulse Orbit Orb:** Uses `tertiary` (`PremiumCobalt`).
- **Stack Drop Empty Cells:** Uses `surfaceVariant` (`PremiumCard`).
- **Status/Instruction Text:** Uses `textSecondary` (#CBD5E1) or `textMuted` (#94A3B8) in some places.

## Accidental Dimming Check
- **GameplayScaffold:** No global alpha reduction during normal play.
- **Lane Drift:** Player glow is only 25% alpha, making it look dim.
- **Pulse Orbit:** Ring has no glow/outer stroke, just a single arc.

## List of Fixes to Apply
1. **Update Theme:** Introduce `GameBackground`, `GameBoard`, `GameBoardRaised`, `HudCard`, `HudBorder` tokens.
2. **Brighten HUD:** Ensure `HudPill` is clearly separate from background.
3. **Fix Pulse Orbit:**
    - Brighten Orb color.
    - Make Ring more prominent.
    - Use `GameBoard` for board background.
4. **Fix Lane Drift:**
    - Use `GameBoard` and `GameBoardRaised` for visible lanes.
    - Saturated colors for blockers and pickups.
    - Brighter player object.
5. **Fix Stack Drop:**
    - Use `GameBoard` for background.
    - Visible grid lines.
    - Fix "Rotate" label clipping.
6. **Safe Area:** Ensure "Start Game" buttons are not clipped by navigation bars.
