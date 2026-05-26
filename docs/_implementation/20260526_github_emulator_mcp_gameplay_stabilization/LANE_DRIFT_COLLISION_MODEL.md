# Lane Drift Collision Model

Date: 2026-05-26

## Geometry Inputs
- Board width and height come from the Compose canvas size, with a fallback size used when the board is not yet measured.
- The board is divided into 3 lanes.
- The player sits in one lane at a fixed vertical zone near the bottom of the board.
- Blockers and pickups spawn above the visible area and move downward in normalized board coordinates.

## Player Bounds
- Visual player size:
  - width: `0.64 * laneWidth`
  - height: `84.dp`
- Collision player size:
  - same visual rectangle, then inset on both axes
  - current insets:
    - X: `24%`
    - Y: `20%`

## Blocker Bounds
- Visual blocker width is lane-relative and drawn as a compact hazard object rather than a full-lane bar.
- Collision blocker width is the visual blocker rect, then inset on both axes.
- Current blocker collision tuning:
  - X inset: `24%`
  - Y inset: `22%`
  - minimum overlap: `10.dp`

## Pickup Bounds
- Pickups use a narrower visual footprint than blockers.
- Pickup collision uses the same visual rect with more forgiving insets.
- Current pickup collision tuning:
  - X inset: `18%`
  - Y inset: `18%`
  - minimum overlap: `8.dp`

## Overlap Rule
- Collision is not based on simple rectangle contact alone.
- A hit only counts when both the X overlap and Y overlap exceed the configured minimum overlap threshold.
- This avoids one-pixel contact turning into a crash.

## Movement Timing
- Lane changes are instant snaps between lanes.
- There is no interpolation-based lane transition in the current implementation.
- That keeps the collision model deterministic and easier to reason about.

## Update Tick
- Frame updates are driven by `withFrameNanos`.
- Each tick:
  - advances elapsed time
  - moves existing items downward
  - spawns new blockers/pickups on a timer
  - checks collision after the move step

## Density Conversion
- Visual sizes and overlap thresholds are defined in dp where practical.
- They are converted to px at runtime so collision behavior stays similar across densities.

## Visual vs Physics Mismatch
- The game now intentionally keeps the art compact and the collision model slightly smaller than the art silhouette.
- That reduces the chance that a visible gap still feels like a crash.

## Fairness Guardrails
- Center-lane blockers are delayed at the start of a run.
- Early traffic is slower and less dense.
- Pickups are kept out of unavoidable blocker paths.

## Test Coverage
- Gap tests cover visible non-contact cases.
- Contact tests cover deeper overlap cases.
- Lane isolation tests confirm that off-lane items do not collide.
- Pickup tests confirm predictable collection.
