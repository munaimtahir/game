# 01 — Current Theme Audit (Pre Light Refactor)

Timestamp (UTC): `20260502005637`

## Files inspected

- `core/ui/src/main/java/com/vexel/offlinearcade/core/ui/Theme.kt`
- `core/ui/src/main/java/com/vexel/offlinearcade/core/ui/AppScaffold.kt`
- `feature/home/src/main/java/com/vexel/offlinearcade/feature/home/HomeScreen.kt`
- `feature/settings/src/main/java/com/vexel/offlinearcade/feature/settings/SettingsScreen.kt`
- `core/model/src/main/java/com/vexel/offlinearcade/core/model/Models.kt`
- `game/pulseorbit/src/main/java/com/vexel/offlinearcade/game/pulseorbit/PulseOrbitDetailScreen.kt`
- `game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftDetailScreen.kt`
- `game/stackdrop/src/main/java/com/vexel/offlinearcade/game/stackdrop/StackDropDetailScreen.kt`
- `game/pulseorbit/src/main/java/com/vexel/offlinearcade/game/pulseorbit/PulseOrbitScreen.kt`
- `game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftScreen.kt`
- `game/stackdrop/src/main/java/com/vexel/offlinearcade/game/stackdrop/StackDropScreen.kt`

## Current dark-heavy tokens found

Located primarily in `Theme.kt`:

- `PremiumBackground` `#07090E`
- `PremiumSurface` `#131A2A`
- `PremiumCard` `#1F293F`
- Text:
  - `TextPrimary` `#F8FAFC`
  - `TextSecondary` `#CBD5E1`
  - `TextMuted` `#94A3B8`
- Gameplay tokens:
  - `gameBackground` `#07111E`
  - `gameBoard` `#0D1726`
  - `gameBoardRaised` `#122039`
  - `hudCard` `#132033`
- Controls:
  - `controlSurface` `#101B2D`
  - `controlBorder` `#3A4F73`

## Hardcoded dark / dimming usages discovered

- `core/ui/.../AppScaffold.kt`
  - `GameplayScaffold` overlay scrim uses `Color.Black.copy(alpha = 0.75f)` (hardcoded).
- `feature/settings/.../SettingsScreen.kt`
  - Theme preview gradients include near-black stops (e.g. `Color(0xFF0B1020)`, `Color(0xFF141B2D)`).
- `core/model/.../Models.kt` + `feature/home/.../HomeScreen.kt` + `feature/settings/.../SettingsScreen.kt`
  - Theme naming and strings reference “Midnight Glow / Midnight Glow Arcade”.

## Gameplay contrast / readability problems (as-is)

- Global palette is optimized around a near-black shell; gameplay boards and HUD are also dark, causing low figure-ground separation on gameplay screens.
- Gameplay boards use `gameBoard` / `gameBoardRaised` values that are too close to each other and to the screen background, making playfields feel “dim” and visually fatiguing.
- HUD pills (`HudPill`) are dark and blend into the overall dark gameplay environment.
- Lane Drift:
  - Lanes are rendered with `hudCard` vs `gameBoardRaised` on a `gameBoard` backdrop; in the current palette the differences are subtle.
  - Player is cyan (good), but the scene still sits on a dark board that reduces scan comfort.
- Stack Drop:
  - Board is dark; empty cells use `gameBoardRaised` which is still dark, lowering grid readability and making the game feel muddy/low-contrast.
- Pulse Orbit:
  - Ring and orb have color, but the board background is dark and the instruction text sits on a low-luminance scene.

## Screens/components needing token migration

- `Theme.kt`: replace full palette + Material3 color scheme to a light system (“Calm Focus Arcade”), keeping gameplay semantic colors vivid.
- `ArcadeScaffold`: move from dark shell gradient to light `AppBackground` system.
- `GameplayScaffold`: use light `GameBackground`; overlay scrim only during pause/game-over using theme token (not hardcoded black).
- `ArcadeCard`, `PremiumButton`, `HudPill`, `PremiumOverlayCard`, `HeroPanel`, settings theme preview blocks: update surfaces/borders/text for light theme.
- All gameplay boards: switch from dark boards to light gameplay board tokens; ensure hazards/pickups/player/pieces remain distinct.

## Risks before implementation

- Theme catalog exists (`ArcadeThemeCatalog`) and settings UI previews depend on theme strings/gradients; the refactor must keep those screens consistent and light-based.
- Some gameplay visuals use hardcoded `Color.White` for highlight strokes; this may need evaluation against light board surfaces to avoid disappearing.
- `ArcadeScaffold` uses a remembered scroll state; depending on navigation state retention, detail screens might re-open mid-scroll unless scroll reset is enforced for those routes.

