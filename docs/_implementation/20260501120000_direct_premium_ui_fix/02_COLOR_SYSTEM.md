# Color System

## Final Palette
A new "Midnight Arcade" premium color system has been established in `Theme.kt`.

- **Backgrounds:**
  - `PremiumBackground`: `#07090E` (very deep navy / near black)
  - `PremiumSurface`: `#131A2A` (dark blue-gray for main surface)
  - `PremiumCard`: `#1F293F` (slightly lighter blue-gray for elevated cards)

- **Primary & Actions:**
  - `PremiumAction`: `#00E5FF` (electric cyan/teal, primary actions)
  - `PremiumAccent`: `#B140FF` (violet / soft magenta, secondary/premium accents)
  - `PremiumCobalt`: `#2979FF` (cobalt blue for Stack Drop accents)

- **Status & Feedback:**
  - `PremiumReward`: `#FFB800` (warm amber / gold)
  - `PremiumDanger`: `#FF3D71` (coral / red for collisions/failures)
  - `PremiumSuccess`: `#00E676` (mint / green-cyan for pickups/success)

- **Typography & Outlines:**
  - `TextPrimary`: `#F8FAFC` (near-white)
  - `TextSecondary`: `#CBD5E1` (cool light gray)
  - `TextMuted`: `#94A3B8` (slate gray)
  - `OutlineColor`: `#334155` (slate gray outline)

## Replacements
- Replaced ambiguous colors like `MidnightBackground`, `MidnightCard` and standard `Indigo`, `Aqua` with the strict `Premium` prefixed equivalents.
- Switched default `MaterialTheme.typography` to a custom `PremiumTypography` with tighter letter spacing and adjusted line heights.
- Removed reliance on `onPrimary` behaving nicely on dark surfaces by explicitly providing high-contrast text (`TextPrimary`, `TextSecondary`).

## Contrast Fixes
- `darkColorScheme` was explicitly provided with high-contrast text colors (`TextPrimary`, `TextSecondary`) instead of letting it fall back.
- `onSurfaceVariant` was adjusted to `TextSecondary` (or `TextPrimary` in high contrast) instead of `MidnightSubtext` which was often unreadable.
- `ArcadeExtendedColors` explicitly specifies `textPrimary`, `textSecondary`, `cardBackground` to ensure components can reference them without resorting to Material defaults.
- All disabled states use `DisabledSurface` and `DisabledText` explicitly.
