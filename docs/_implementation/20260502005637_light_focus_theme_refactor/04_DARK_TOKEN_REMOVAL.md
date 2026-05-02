# 04 — Dark Token Removal

Timestamp (UTC): `20260502005637`

## Searches performed

- `PremiumBackground|PremiumSurface|PremiumCard|Midnight`
- `0xFF07090E|0xFF131A2A|0xFF1F293F|0xFF07111E|0xFF0D1726|0xFF122039`
- `Color.Black|Color.DarkGray`
- `0xFF0B1020|0xFF141B2D|0xFF05060A`

## Leftovers found

- None in `app/`, `core/`, `feature/`, or `game/` source after the refactor.

## Leftovers removed

- `GameplayScaffold` no longer hardcodes a black scrim and no longer dims normal gameplay (overlay scrim is token-driven and only shown when an overlay is actually active).
- Settings theme preview gradients were updated from near-black “midnight” stops to light-based preview gradients.
- Theme naming strings (“Midnight Glow”) were replaced with “Calm Focus Arcade”.

## Intentional dark usage retained

- None for normal screens/gameplay.
- Overlay dimming uses `OverlayScrim` (`#102033`) with moderate alpha only during pause/game-over states.

