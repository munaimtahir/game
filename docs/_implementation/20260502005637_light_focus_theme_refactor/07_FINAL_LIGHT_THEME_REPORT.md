# 07 — Final Light Theme Report (“Calm Focus Arcade”)

Timestamp (UTC): `20260502005637`

## 1) Summary

Offline Mini Arcade has been refactored from a dark-heavy “midnight” visual system into a calm, premium LIGHT theme designed for gameplay clarity and low cognitive load. Gameplay screens no longer sit on near-black surfaces and no longer apply a normal-play scrim/dimming layer.

## 2) Final light palette

See: `docs/_implementation/20260502005637_light_focus_theme_refactor/02_LIGHT_THEME_TOKENS.md`

## 3) Components updated

- `core/ui/src/main/java/com/vexel/offlinearcade/core/ui/Theme.kt`
  - Replaced the midnight palette with “Calm Focus Arcade” light tokens + `lightColorScheme`.
  - Added gameplay + overlay semantic tokens (board inner, grid lines, overlay card/scrim/border).
- `core/ui/src/main/java/com/vexel/offlinearcade/core/ui/AppScaffold.kt`
  - `ArcadeScaffold` uses light app background + safe insets.
  - Added `resetScrollOnEnter` for “open at top” behavior.
  - `GameplayScaffold` overlay scrim is token-driven and only shown when overlay content is present.
  - `PremiumButton` aligns primary/secondary/disabled styling to the light system.

See: `docs/_implementation/20260502005637_light_focus_theme_refactor/03_SHARED_COMPONENT_REFACTOR.md`

## 4) Home / dashboard changes

- Home now renders on a light app background while preserving a premium gradient hero panel.
- Updated branding string to “Calm Focus Arcade”.

## 5) Detail screen changes

- `PulseOrbitDetailScreen`, `LaneDriftDetailScreen`, `StackDropDetailScreen`
  - Force scroll-to-top on navigation via `resetScrollOnEnter = true`.
  - Light surfaces + dark text via shared tokens.
  - Start Game button benefits from safe bottom inset padding in `ArcadeScaffold`.

## 6) Pulse Orbit gameplay changes

- Board/background now use light gameplay surfaces (`gameBackground`, `gameBoard`, `gameBoardRaised`).
- Ring/orb remain vivid against a light board; instruction text is dark + readable.
- Pause/game-over overlays no longer dim normal play; scrim appears only when overlay is active.

## 7) Lane Drift gameplay changes

- Lanes and active lane are rendered using light surfaces for clearer separation.
- Player/hazards/pickups remain vivid with improved figure-ground comfort.
- Removed normal-play dimming (overlay scrim only on pause/game-over).

## 8) Stack Drop gameplay changes

- Board is light; empty cells now use `gameBoardInner`.
- Added subtle per-cell grid separation using `gridLine` for faster board readability.
- Controls use light secondary buttons with clear borders.
- Removed normal-play dimming (overlay scrim only on pause/game-over).

## 9) Overlay/state changes

- All gameplay screens now pass `overlay = null` unless paused/game-over is active.
- Scrim uses `OverlayScrim` (`#102033`) at controlled alpha; modal content remains light and readable.

## 10) Dark token removal summary

See: `docs/_implementation/20260502005637_light_focus_theme_refactor/04_DARK_TOKEN_REMOVAL.md`

## 11) Visual checklist

See: `docs/_implementation/20260502005637_light_focus_theme_refactor/05_LIGHT_THEME_VISUAL_CHECKLIST.md`

## 12) Build/test result

See: `docs/_implementation/20260502005637_light_focus_theme_refactor/06_BUILD_AND_TEST_REPORT.md`

## 13) GO / NO-GO

- GO for merge (build/test/lint pass; app is fully light-themed; gameplay screens are no longer dark-dominant or dimmed during normal play).
- NO-GO only if screenshot artifacts are required immediately; the ADB screenshot script needs a connected device/emulator.

