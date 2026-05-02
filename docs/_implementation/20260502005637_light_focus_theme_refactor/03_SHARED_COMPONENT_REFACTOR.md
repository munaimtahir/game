# 03 — Shared Component Refactor (Light Theme)

Timestamp (UTC): `20260502005637`

## Components changed

- `core/ui/src/main/java/com/vexel/offlinearcade/core/ui/AppScaffold.kt`
  - `ArcadeScaffold`
  - `GameplayScaffold`
  - `PremiumButton` (primary/secondary/disabled mapping)

## Key token usage

- `ArcadeScaffold`
  - Background uses `ArcadeTheme.colors.background` (soft light app background).
  - Bottom + horizontal safe insets are applied to body content via `WindowInsets.safeDrawing`.
  - Added `resetScrollOnEnter` to force scroll-to-top on specific screens (used by detail screens).

- `GameplayScaffold`
  - Background uses `ArcadeTheme.colors.gameBackground`.
  - Overlay scrim uses `ArcadeTheme.colors.overlayScrim` with controlled alpha.
  - Normal gameplay no longer receives a scrim; callers now pass `overlay = null` unless pause/game-over is active.

- `PremiumButton`
  - Primary uses `MaterialTheme.colorScheme.primary` (cyan fill) and `onPrimary` (dark text).
  - Secondary uses `ArcadeTheme.colors.controlSurface` + `controlBorder` with dark text for light surfaces.
  - Disabled state uses muted light surfaces (`cardBackground` + `textMuted`) to avoid “inactive looks active”.

## Hardcoded colors removed / avoided

- Removed hardcoded normal-gameplay scrim behavior by ensuring overlays are only rendered when state requires it.
- Overlay scrim is no longer a hardcoded black; it is token-driven (`overlayScrim`).

