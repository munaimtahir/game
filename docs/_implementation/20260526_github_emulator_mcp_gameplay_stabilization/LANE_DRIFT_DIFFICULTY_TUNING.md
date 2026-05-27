# Lane Drift Difficulty Tuning

Date: 2026-05-26

## Current Tuning
- Initial speed: `100f`
- Max speed: `320f`
- Speed ramp: `3.0f` per second
- Initial spawn interval: `1.9f`
- Minimum spawn interval: `0.85f`
- Spawn interval ramp: `0.0045f` per second
- Grace window: `18s`
- Early spawn multiplier during grace: `1.24x`

## Why This Is Easier
- The first 20-30 seconds now stay readable longer.
- The player gets more time to learn lane changes before the traffic becomes dense.
- The center lane is protected from early blockers, which prevents the opening flow from feeling unfair on the default starting lane.

## Spawn Rules
- Early blockers avoid the center lane.
- Early blocker lane selection also avoids same-lane repetition for a short period after the grace window starts.
- Pickups still appear often enough to make early runs feel rewarding.

## Scoring / Reward Flow
- Base score increases over time.
- Pickups still grant bonus score and coins.
- The speed and spawn cadence improvements are intended to make the reward loop feel learnable instead of punishing.

## Difficulty Intent
- The game should feel:
  - immediate
  - fair
  - readable
  - short-session friendly
  - replayable
- It should not feel like a reflex trap in the opening seconds.

## What To Watch Next
- If the run becomes too easy after more playtesting, tighten:
  - spawn interval ramp
  - blocker early protection window
  - collision insets
- If the run still feels unfair, reduce art footprint and/or increase the overlap threshold slightly before changing core movement.
