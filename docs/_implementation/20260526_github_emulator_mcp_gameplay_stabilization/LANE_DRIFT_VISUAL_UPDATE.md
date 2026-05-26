# Lane Drift Visual Update

Date: 2026-05-26

## Visual Direction
- Bright arcade road flow.
- Clean lane separation.
- Obvious danger vs reward contrast.
- Simple low-cost vector-like shapes rather than heavy assets.

## What Changed
- Added clearer lane separators and lane edge markers.
- Current lane now reads more clearly as the player’s safe track.
- The early game is visually quieter because the traffic is slower and more spaced out.
- The game now uses a deterministic debug seed in debug builds, which helps keep screenshot evidence consistent.

## Player Readability
- The player silhouette is a compact hover-car style shape with a clear outline.
- Hazards are intentionally more distinct than before:
  - cone
  - barrier
  - crate
  - barrel
  - pothole
- Pickups are visually distinct and reward-coded:
  - coin
  - star
  - energy
  - gem
  - fuel

## Board Readability
- Lane separators reduce the chance of misreading the open path.
- The center lane highlight helps the player understand the starting position.
- Speed lines are kept subtle so they do not compete with blockers and pickups.

## UI Readability
- Ready, active, paused, and game-over states remain separate.
- The top HUD continues to show score and pickups.
- The existing overlay cards still communicate retry and quit actions clearly.

## Accessibility / Performance Notes
- Visual changes stay lightweight enough for low-end devices.
- No new bitmap assets were introduced.
- The drawing work remains Compose canvas based and offline-only.
