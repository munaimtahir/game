# Lane Drift — Current Collision Logic (pre-fix)

## Where the logic lives
- Gameplay state + update loop: `game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftScreen.kt`
  - `LaneDriftState` (player lane, items, timers, speed)
  - `DriftItem` (`lane`, `y`, `type`)
  - `LaunchedEffect(state.playing)` frame loop (movement, spawning, collision)
- Rendering:
  - Same file, `Canvas { ... }` block draws lanes, items, and player.

## What counts as a collision today
Collision is **lane index + a Y “window” test on the item’s top coordinate**.

### Blocker (crash)
A crash triggers when there exists an item such that:
- `item.type == BLOCKER`
- `item.lane == playerLane`
- `item.y` is inside a fixed window around `playerZoneY`

Code shape (simplified):
- Window: `(playerZoneY - blockerCollisionWindow) .. (playerZoneY + blockerCollisionWindow)`
- Constants:
  - `playerZoneY = 0.88f`
  - `blockerCollisionWindow = 0.062f`

### Pickup (collect)
Pickup collection uses the same approach (lane equality + Y-window), with a slightly larger window:
- `pickupCollisionWindow = 0.078f`

There is also a guardrail: pickups are not collected on the same frame as a blocker collision (pickup check only runs if `blockerHit == null`).

## What collision method is this?
- **Lane index + y-range overlap** only.
- It does **not** use:
  - full lane cell bounds
  - full visual object bounds
  - rounded-rect bounds intersection
  - center-point distance
  - player/obstacle rect overlap

## Why “tiny visible gaps” can still crash
There are two main reasons:

1. **The collision check ignores object heights.**
   - `item.y` represents the item’s *top* position as a fraction of canvas height (`top = canvasHeight * item.y`).
   - The check treats “within window” as “colliding”, even if the blocker’s bottom edge is still above the player (or vice-versa).

2. **Gameplay Y uses a fixed logical board height, but rendering uses actual canvas size.**
   - Movement updates `item.y` using `boardHeightPx = 600.dp.toPx()`.
   - Drawing uses `top = size.height * item.y`.
   - If the actual canvas height differs from the fixed `600.dp`, the visual and logical positions can drift, increasing the odds of “looks like a gap” but still inside the collision window.

## Summary of current behavior
- Collision is strict because it is effectively “if blocker top crosses a band around the player’s Y”, not “if the player and blocker shapes overlap”.
- This produces crashes on near-misses, edge touches, and even some frames where there is still visible separation.
