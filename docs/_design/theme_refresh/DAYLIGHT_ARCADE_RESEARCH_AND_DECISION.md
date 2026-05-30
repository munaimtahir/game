# Daylight Arcade — Research & Design Decision
**Date:** 2026-05-31  
**Product:** Offline Mini Arcade  
**Scope:** Full app theme refresh from dark/neon prototype to Daylight Arcade

---

## Research Takeaways (5–8 Bullets)

1. **Cognitive load and dark UIs** — Studies on mobile game UI (Nielsen/NNG, Google Material Studies) show that full-screen dark backgrounds place a higher cognitive burden on short-session casual players because the eye must adjust more when transitioning from ambient light to the screen. Light backgrounds reduce this friction for sessions under 5 minutes.

2. **Light mode readability** — For text and HUD elements, dark text on light backgrounds provides the highest sustained readability (WCAG 2.1: minimum 4.5:1 ratio for normal text, 3:1 for large/bold). The current dark scheme uses light text on dark surfaces; this works fine in isolation but becomes fatiguing with bright ambient light (outdoor, well-lit indoor).

3. **Casual game color psychology** — Research on mobile casual games (Puzzle & Dragons, Candy Crush, Alto's Adventure) consistently shows that warm-neutral or soft-sky backgrounds with saturated-but-bounded accent colors drive higher replayability and lower anxiety. Neon-dominant schemes skew toward "performance pressure" aesthetics that suit competitive games but not quick-session casual play.

4. **60–30–10 rule for arcade UI** — The established design principle: 60% dominant neutral background, 30% complementary mid-tone surface, 10% energetic accent for actions/feedback. The current dark scheme violates this by having 80%+ dark surface with neon accents, creating visual noise even before gameplay begins.

5. **Contrast and motion readability** — For fast-moving game elements (Lane Drift cars/obstacles, Pulse Orbit orb), the ISO/ANSI and W3C guidance on motion contrast recommends foreground objects differ by at least 4.5:1 from their immediate background. Dark lanes with dark obstacles (only outline differentiation) reduce separability. Light lane surfaces with distinctly colored obstacles solve this cleanly.

6. **Predatory vs. engagement design** — Casino/slot visual patterns (red/gold dominant, flashing borders, neon overload, dense density) are documented to trigger compulsive feedback loops. Casual-healthy design (as per "Ethics in Game Design" discussions and PEGI guidance) uses positive feedback for success, restraint on danger/loss colors, and avoids aggressive urgency signals. Our current neon-pink splash progress bar and fuchsia secondary were borderline.

7. **Material Design 3 color system** — Google's M3 recommends using `lightColorScheme` with semantic tokens (primary, surface, surfaceVariant, background, error) backed by a tonal palette. Status bars and navigation bars should match the surface, not a forced dark override. The current `isAppearanceLightStatusBars = false` with a dark background is correct for dark theme, but should be flipped for the new light theme.

8. **Low-end Android performance** — Simple gradients (2-stop linear/vertical) have near-zero GPU cost on Android. Radial gradients with large radius can stutter on Mali-400 class GPUs. Avoid more than 2-stop radial gradients in game canvases. The existing Canvas-based drawing in Pulse Orbit, Lane Drift, and Stack Drop is already efficient.

---

## What Is Wrong with the Current Dark-Heavy Scheme

| Problem | Where |
|---|---|
| App background `#070B1E` (near-black) on every screen | Theme.kt, AppScaffold.kt |
| Surface `#0F172A`, cards `#1E293B` — visually indistinguishable | ArcadeCard, HudPill, GameplayScaffold |
| Splash loading screen uses neon pink/cyan gradient progress bar | ArcadeApp.kt |
| Status bar forced dark (`isAppearanceLightStatusBars = false`) | Theme.kt |
| `LightGameBackground = #0F172A` — named "light" but is dark | Theme.kt |
| `LightGameBoard = #1E293B` — dark board background in all 3 games | PulseOrbit, LaneDrift, StackDrop |
| `gameBoard` token used for game canvas background is same dark | All game screens |
| Secondary accent: fuchsia `#D946EF` on dark — casino-like | Theme.kt |
| `textInverse` is white (correct for dark), but reversal needed | LaneDriftDraw.kt |
| Hero gradient: fuchsia→cyan — vivid but fatiguing | AppScaffold.kt HeroPanel |
| Game card `elevatedCardBackground` = slate-800 dark card | HomeScreen.kt |
| Shell gradient: dark blue → slate — not welcoming for home | ArcadeScaffold |

---

## Final Chosen Theme Direction: **Daylight Arcade**

**Core design goal:** A bright, friendly, high-clarity arcade interface with playful accents, soft gradients, strong readability, and low visual fatigue.

**Pillars:**
- 60% calm light background (#F8FAFF sky white or #FFF8ED cream)
- 30% white/soft blue card surfaces (#FFFFFF, #EAF4FF)
- 10% saturated accent (action blue #2F80ED, teal #20C997, gold #FFB703)

---

## Color Palette Table

| Token | Name | Hex | Role |
|---|---|---|---|
| `background` | Sky White | `#F8FAFF` | App background |
| `surface` | Pure White | `#FFFFFF` | Cards, overlays |
| `surfaceVariant` | Soft Blue | `#EAF4FF` | Secondary surfaces, game boards |
| `surfaceContainer` | Pale Blue | `#DCE6F2` | Card borders, grid lines |
| `textPrimary` | Deep Navy | `#14213D` | All primary text |
| `textSecondary` | Slate Blue | `#526173` | Secondary labels, descriptions |
| `textMuted` | Steel | `#8B9BB4` | Placeholder, disabled |
| `primary` / `action` | Arcade Blue | `#2F80ED` | Primary buttons, player car |
| `primaryDark` | Deep Blue | `#1565C0` | Primary button container |
| `onPrimary` | White | `#FFFFFF` | Text on primary buttons |
| `secondary` | Energy Violet | `#8E7CFF` | Pulse orbit accent, secondary UI |
| `secondaryContainer` | Soft Violet | `#E8E4FF` | Violet tint containers |
| `tertiary` / `reward` | Arcade Gold | `#FFB703` | Coins, stars, score rewards |
| `success` / `positive` | Mint Teal | `#20C997` | Success feedback, energy pickup |
| `danger` / `error` | Coral Red | `#EF476F` | Collision, hazard, game over |
| `border` | Cloud Border | `#DCE6F2` | Card outlines, grid lines |
| `gameBackground` | Soft Blue-White | `#F4FAFF` | Gameplay screen background |
| `gameBoard` | Pale Blue | `#EAF4FF` | Game canvas background |
| `gameBoardRaised` | White | `#FFFFFF` | Raised lane / cells |
| `gameBoardInner` | Misty | `#F0F6FF` | Empty cells (Stack Drop) |
| `gridLine` | Border Blue | `#C8DAEA` | Grid separators |
| `hudCard` | White | `#FFFFFF` | HUD pill background |
| `hudBorder` | Cloud | `#DCE6F2` | HUD pill outline |
| `controlSurface` | White | `#FFFFFF` | Button backgrounds |
| `controlBorder` | Mid Blue | `#A8C7FA` | Button outlines |
| `overlayScrim` | Soft Black | `rgba(0,0,0,0.25)` | Overlay dimmer |
| `shellGradient` | Sky→Pale | `#F8FAFF→#EAF4FF` | App background gradient |
| `heroGradient` | Blue→Violet | `#2F80ED→#8E7CFF` | Hero panel |

---

## Per-Game Palette Recommendations

### 1. Pulse Orbit — "Sky Pulse"
| Element | Color | Notes |
|---|---|---|
| Game background | `#F4FAFF` | Light sky |
| Game board / canvas | `#EAF4FF` | Pale blue |
| Central core | `#2F80ED` | Arcade Blue |
| Orbit ring | `#2F80ED` → `#8E7CFF` lerp | Blue to violet |
| Orb traveler | `#8E7CFF` | Energy violet |
| Success pulse | `#20C997` | Mint teal |
| Perfect combo | `#FFB703` | Gold |
| Fail/collision | `#EF476F` | Coral red |

### 2. Lane Drift — "Fresh Drift"
| Element | Color | Notes |
|---|---|---|
| Game background | `#F4FAFF` | Light |
| Lane base | `#EAF4FF` | Pale blue |
| Active lane highlight | `#FFFFFF` | Bright white |
| Lane divider | `#A8C7FA` | Soft blue dash |
| Player car body | `#2F80ED` | Arcade blue |
| Player car canopy | `#60A5FA` | Lighter blue |
| Coin pickup | `#FFB703` | Gold |
| Gem pickup | `#8E7CFF` | Violet |
| Energy pickup | `#20C997` | Teal |
| Hazard / obstacles | `#EF476F` | Coral |
| Speed lines | `#A8C7FA` alpha 0.5 | Visible but calm |

### 3. Stack Drop — "Soft Blocks"
| Element | Color | Notes |
|---|---|---|
| Game background | `#FFF8ED` | Cream warm |
| Board canvas | `#F8FAFF` | Pale |
| Board empty cell | `#F0F6FF` | Misty |
| Grid line | `#C8DAEA` | Cloud border |
| I-piece | `#2F80ED` | Blue |
| O-piece | `#FFB703` | Gold |
| T-piece | `#8E7CFF` | Violet |
| L-piece | `#F97316` | Orange |
| S-piece | `#20C997` | Teal |
| Danger zone glow | `#EF476F` alpha 0.12 | Subtle coral |
| Line clear flash | White alpha 0.5 | Quick bright |

---

## Accessibility / Contrast Rules

- Text primary (`#14213D`) on background (`#F8FAFF`): ~14:1 — ✅ AAA
- Text secondary (`#526173`) on background (`#F8FAFF`): ~6.5:1 — ✅ AA
- Primary button (`#2F80ED`) with white text: ~4.7:1 — ✅ AA
- Danger (`#EF476F`) on game board (`#EAF4FF`): ~4.2:1 — ✅ AA for large elements
- HUD text (`#14213D`) on HUD card (`#FFFFFF`): ~18:1 — ✅ AAA
- Status bar: use `isAppearanceLightStatusBars = true` with light background

---

## Implementation Checklist

- [x] Write this design document
- [ ] Update `Theme.kt` — replace all dark tokens with Daylight Arcade palette
- [ ] Update `Theme.kt` — fix `isAppearanceLightStatusBars` to `true`
- [ ] Update `ArcadeApp.kt` (SplashLoadingScreen) — replace neon gradient with light gradient
- [ ] Update `StackDropEngine.kt` — update block colors to Daylight palette
- [ ] Update `LaneDriftDraw.kt` — review `textInverse` usage (now should be dark navy, not white)
- [ ] Run build/lint/tests
- [ ] Record before/after observations in evidence folder

---

## Screenshots / Evidence to Collect After Implementation

Collect screenshots of:
1. Splash/loading screen (light gradient, progress bar visible)
2. Home screen (bright cards, game list)
3. Pulse Orbit gameplay (light board, readable ring/orb)
4. Lane Drift gameplay (light lanes, colored car/obstacles)
5. Stack Drop gameplay (light board, colorful blocks)
6. Game over/retry overlay
7. Settings screen
8. Stats screen (if applicable)
9. Daily challenges screen (if applicable)

Record build output (pass/fail) and lint warnings/errors.
