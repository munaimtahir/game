# Theme Propagation Audit — Offline Mini Arcade

## Current Theme Entry Point

**`app/src/main/java/com/vexel/offlinearcade/ArcadeApp.kt`**

```kotlin
OfflineMiniArcadeTheme(
    themeId = snapshot.profile.selectedThemeId,
    highContrast = snapshot.settings.highContrastEnabled,
    reducedEffects = snapshot.settings.reducedEffects,
) { ... }
```

The `themeId` comes from `PlayerProfile.selectedThemeId` which defaults to `"default"` (stored in Room DB).

---

## Root Composable Where Theme Is Applied

`ArcadeApp.kt` → `OfflineMiniArcadeTheme` (in `core/ui/Theme.kt`)

This wrapper applies:
1. `MaterialTheme` with the color scheme
2. `CompositionLocalProvider` for `LocalArcadeExtendedColors` and `LocalArcadeSpacing`

All screens are children of this composable via `ArcadeNavHost`.

---

## All Theme / Color Files

| File | Role |
|---|---|
| `core/ui/src/main/java/…/core/ui/Theme.kt` | Primary color palette, `OfflineMiniArcadeTheme`, `ArcadeExtendedColors`, `LocalArcadeExtendedColors` |
| `core/ui/src/main/java/…/core/ui/AppScaffold.kt` | All shared UI components (ArcadeCard, HudPill, PremiumButton, GameplayScaffold, etc.) consuming colors |
| `app/src/main/res/values/themes.xml` | Android XML theme (system bar colors, parent theme) |
| `core/model/src/main/java/…/core/model/Models.kt` | `ArcadeThemeCatalog` with theme definitions and default theme ID |
| `feature/settings/src/main/java/…/feature/settings/SettingsScreen.kt` | `themePreviewBrush()` / `themeAccentBrush()` — hardcoded color previews |

---

## Custom Color Systems

### `LocalArcadeExtendedColors` / `ArcadeTheme.colors`
Defined in `Theme.kt`. A `staticCompositionLocalOf` holding `ArcadeExtendedColors`. Used in nearly all components.  
**Every screen and component uses `ArcadeTheme.colors.*` instead of `MaterialTheme.colorScheme.*` for most color values.**  
This is the authoritative source for dark colors that were propagating everywhere.

No `LocalSkinColors` or other named custom wrappers exist beyond `LocalArcadeExtendedColors`.

---

## All Hardcoded Colors in Screens / Components

### `core/ui/Theme.kt`
- Entire dark palette hardcoded (`0xFF07090E`, `0xFF131A2A`, `0xFF1F293F`, `0xFF00E5FF`, etc.)
- `darkColorScheme()` used always — `themeId` param is **completely ignored**
- `gameBackground = Color(0xFF07111E)`, `gameBoard = Color(0xFF0D1726)`, etc.

### `core/ui/AppScaffold.kt`
- `GameplayScaffold` overlay: `Color.Black.copy(alpha = 0.75f)` — hardcoded dark scrim
- `HeroPanel`: `Color.White.copy(alpha = 0.85f)` / `Color.White` for text on hero gradient (acceptable, gradient ensures contrast)
- `SplashShell`: `Color.White` on hero gradient (acceptable)

### `feature/settings/SettingsScreen.kt`
- `themePreviewBrush("default")`: `listOf(Color(0xFF0B1020), Color(0xFF7C5CFF), Color(0xFF35D6D0))` — dark preview gradient
- `themeAccentBrush("default")`: `listOf(Color(0xFF7C5CFF), Color(0xFF35D6D0))` — dark accent
- Other brushes for `"sunset_shift"` and `"ice_grid"` also dark (these are alternative themes, acceptable)

### `feature/home/HomeScreen.kt`
- `adaptiveTextColor()`: `Color(0xFF0F172A)` / `Color(0xFFF8FAFC)` — safe adaptive logic
- "Midnight Glow Arcade" brand text label (visual string, not a color)

### Game screens (PulseOrbit, LaneDrift, StackDrop)
- No hardcoded `Color(0xFF…)` in screen files — all use `ArcadeTheme.colors.*` ✓
- All game Canvas rendering uses `colors.gameBoard`, `colors.gameBoardRaised`, `colors.primaryCyan`, etc.

### `game/stackdrop/StackDropEngine.kt`
- `PieceType.I.color = 0xFF4FD7FF` (bright cyan — designed for dark bg)
- `PieceType.O.color = 0xFFFFD54F` (bright yellow)
- `PieceType.T.color = 0xFFB388FF` (soft violet)
- `PieceType.L.color = 0xFFFF8A65` (coral orange)
- `PieceType.S.color = 0xFF81C784` (light green)  
These are rendered directly as `Color(int)` on the canvas. On light backgrounds they will be low-contrast.

---

## MaterialTheme.colorScheme Usage Audit

| Location | colorScheme used? | Notes |
|---|---|---|
| Home screen | Minimal (`typography` only) | Uses `ArcadeTheme.colors.*` for everything |
| Game cards | Minimal | Uses `ArcadeTheme.colors.*` |
| Buttons (`PremiumButton`) | `primary`, `onPrimary`, `secondaryContainer` | Rest from `ArcadeTheme.colors` |
| Top bars | No | `ArcadeTheme.colors.textPrimary` |
| Game play areas | No | All `ArcadeTheme.colors.*` + hardcoded ints in StackDrop |
| Pause overlays | No | `ArcadeCard` → `ArcadeTheme.colors.elevatedCardBackground` |
| Game over overlays | No | Same |
| Score HUD | No | `ArcadeTheme.colors.hudCard/hudBorder` |
| Settings screen | `onSurfaceVariant`, `onPrimary` | Partially |
| Stats/Challenges | Minimal | Uses `ArcadeTheme.colors.*` |

---

## Exact Reason Previous Color Changes Were Not Visible

**There are five compounding causes:**

1. **`themeId` is ignored**: `OfflineMiniArcadeTheme` accepts `themeId` but always calls `premiumColorScheme()` and `premiumExtendedColors()` — functions that return the same dark palette regardless of the argument. Changing the theme in settings stores the preference but produces zero visual effect.

2. **`darkColorScheme()` always used**: The Material3 color scheme is always constructed as a dark scheme. Even if someone changed palette values, the dark color scheme defaults would override unset values.

3. **`ArcadeExtendedColors` always dark**: All 30+ color tokens in `ArcadeExtendedColors` are hardcoded to dark navy/neon values. Because nearly all components use `ArcadeTheme.colors.*` (not `MaterialTheme.colorScheme`), replacing the Material3 scheme alone would have no visible effect.

4. **`themes.xml` forces black nav bar**: `android:navigationBarColor` is set to `@android:color/black`. No `windowLightStatusBar` or `windowLightNavigationBar` flags are set. System UI stays dark.

5. **`DayNight` parent overrides on dark devices**: The theme parent `Theme.Material3.DayNight.NoActionBar` follows system dark mode. On a device in dark mode, even light-intended colors could be overridden by the Material3 dark night variant.

6. **`GameplayScaffold` overlay hardcoded**: `Color.Black.copy(alpha = 0.75f)` for pause/game-over overlay is not derived from the theme system.

7. **StackDrop piece colors in engine**: Colors are baked as ARGB ints into `PieceType` enum — unreachable by the theme system.
