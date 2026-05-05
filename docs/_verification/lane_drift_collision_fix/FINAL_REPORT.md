# Lane Drift — Collision Fairness & Richness Fix (Final Report)

Date (UTC): 2026-05-05

## 1) Files changed
- `game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftScreen.kt`
- `game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftCollision.kt` (new)
- `game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftDraw.kt` (new)
- `game/lanedrift/src/test/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftLogicTest.kt`
- `docs/_discovery/lane_drift_collision_fix/CURRENT_COLLISION_LOGIC.md`
- `docs/_verification/lane_drift_collision_fix/COMMANDS_RUN.md`

## 2) Current collision problem found
Pre-fix collision detection was:
- **lane equality + item.y within a fixed window around `playerZoneY`** (i.e., not rect intersection).

This caused unfair crashes because:
- The check used **only the item’s top Y** and ignored **player/obstacle heights**, so near-misses could still be inside the window.
- Movement used a **fixed logical board height** (`600.dp`) while rendering used the **actual canvas height**, allowing visual/logical drift where a visible gap still registered as collision.

Details: `docs/_discovery/lane_drift_collision_fix/CURRENT_COLLISION_LOGIC.md`

## 3) Collision logic fix summary
- Replaced y-window checks with **deterministic rect overlap** in px:
  - Build **visual rects** for player and each item using the same placement math as drawing.
  - Apply **forgiving hitbox insets** (hitboxes smaller than visuals).
  - Require **minimum overlap threshold** (edge-touching / near-touching does not crash).
- Collision is now based on **clear visible overlap**, not lane cell bounds or y-band approximations.
- Pickup collection remains **separate** and uses its own overlap threshold.

Implementation: `game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftCollision.kt`

## 4) New hitbox values
Configured in `LaneDriftTuning`:
- Player hitbox inset: X = **18%**, Y = **12%**
- Blocker hitbox inset: X = **16%**, Y = **10%**
- Pickup hitbox inset: X = **10%**, Y = **10%**
- Crash min overlap threshold: **8.dp**
- Pickup min overlap threshold: **6.dp**

## 5) Difficulty values before/after
Before (pre-fix):
- `initialSpeed = 186f`, `maxSpeed = 372f`, `speedRampPerSecond = 6.4f`
- `initialSpawnInterval = 0.94f`, `minimumSpawnInterval = 0.56f`, `spawnIntervalRampPerSecond = 0.011f`

After (this change):
- `initialSpeed = 142f`, `maxSpeed = 352f`, `speedRampPerSecond = 4.9f`
- `initialSpawnInterval = 1.22f`, `minimumSpawnInterval = 0.66f`, `spawnIntervalRampPerSecond = 0.0085f`
- Added a **0–15s grace period**:
  - Speed eases in (`0.86x → 1.0x` multiplier across 15s)
  - Spawn interval is slower early (`1.18x` multiplier across 15s)

This makes the first ~10–15 seconds tutorial-like, then ramps gradually.

## 6) Visual object changes
Replaced prototype-like bars with lightweight Compose drawings (no new heavy assets):
- Player: stylized **hover-car** silhouette.
- Obstacles: themed road hazards (**cone / barrier / crate / barrel / pothole**) selected per spawn.
- Pickups: rewarding items (**coin / star / energy cell / gem / fuel token**) selected per spawn.

Implementation: `game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftDraw.kt`

## 7) Tests added/updated
Added collision fairness tests in:
- `game/lanedrift/src/test/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftLogicTest.kt`

New cases covered:
- No collision with a small visible gap.
- No collision on edge-touch.
- Collision on clear overlap.
- Collision requires same lane.
- Pickup collision remains separate / blocker prioritized.
- Stability across densities via dp→px scaling.

## 8) Commands run and results
See: `docs/_verification/lane_drift_collision_fix/COMMANDS_RUN.md`

## 9) Remaining risks
- No `connectedAndroidTest` run here (no emulator/device attached).
- Gameplay feel (readability + perceived fairness) should still be sanity-checked on at least one small phone and one high-density device.

## 10) Final verdict
GO — Collision is now overlap-based with smaller hitboxes + minimum overlap threshold, difficulty starts significantly easier, and visuals are richer while keeping performance-friendly Compose drawing.
