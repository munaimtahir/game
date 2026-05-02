# 02 — Light Theme Tokens (“Calm Focus Arcade”)

Timestamp (UTC): `20260502005637`

This sprint replaces the prior midnight/dark palette with a soft light, gameplay-first system.

## Base surfaces

- `AppBackground`: `#F4F8FB`
- `SurfacePrimary`: `#FFFFFF`
- `SurfaceSecondary`: `#EEF4F8`
- `SurfaceTertiary`: `#E7EEF5`
- `CardSurface`: `#FFFFFF`
- `ElevatedCard`: `#F7FAFD`

## Text

- `TextPrimary`: `#102033`
- `TextSecondary`: `#4E6278`
- `TextMuted`: `#708399`
- `TextInverse`: `#FFFFFF`

## Borders / dividers

- `SoftBorder`: `#C9D7E4`
- `StrongBorder`: `#AFC2D4`

## Actions

- `PrimaryAction`: `#1CCFE2`
- `PrimaryActionPressed`: `#12AFC4`
- `PrimaryOnAction`: `#06121D`
- `SecondaryAction`: `#6C63FF`
- `SupportAccent`: `#4A7DFF`

## Gameplay surfaces

- `GameBackground`: `#F4F8FB`
- `GameBoard`: `#DDE7F0`
- `GameBoardInner`: `#F7FAFD`
- `GameBoardRaised`: `#EAF1F7`
- `GridLine`: `#C6D3DF`
- `HudCard`: `#FFFFFF`
- `HudBorder`: `#C9D7E4`

## Semantic gameplay colors

- `PlayerCyan`: `#1CCFE2`
- `PlayerBlue`: `#4A7DFF`
- `PlayerViolet`: `#6C63FF`
- `DangerBlocker`: `#E85D75`
- `DangerBlockerDark`: `#C9435D`
- `PickupMint`: `#28C7A8`
- `RewardAmber`: `#F2B94B`
- `ComboViolet`: `#7C6BFF`

## Overlay

- `OverlayScrim`: `#102033` (alpha applied only during pause/game-over)
- `OverlayCard`: `#FFFFFF`
- `OverlayBorder`: `#C9D7E4`

## Where this is implemented

- `core/ui/src/main/java/com/vexel/offlinearcade/core/ui/Theme.kt`
  - Material 3 `lightColorScheme` is now the base.
  - `ArcadeExtendedColors` contains the app/gameplay semantic tokens.

## Old dark tokens replaced

Replaced the prior midnight palette:

- `PremiumBackground` `#07090E` → `AppBackground` `#F4F8FB`
- `PremiumSurface` `#131A2A` → `SurfacePrimary/Secondary`
- `PremiumCard` `#1F293F` → `CardSurface/ElevatedCard`
- Gameplay dark boards (`#07111E`, `#0D1726`, `#122039`, etc.) → light gameplay surfaces above

## Intentionally retained concepts

- Decorative gradients remain (light-based) for dashboard/detail hero presentation.
- Overlay scrim remains dark-navy based, but is only applied during pause/game-over states.

