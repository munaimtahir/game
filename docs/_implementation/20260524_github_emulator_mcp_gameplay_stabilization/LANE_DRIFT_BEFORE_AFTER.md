# Lane Drift Before / After

**Date**: May 24, 2026

## Before
- Difficulty ramped up very quickly (4.9 speed ramp/sec).
- High initial speed (142).
- Collision hitboxes were unforgiving (small insets, causing perceived unfair collisions).
- Tests did not account for actual collision boundary conditions logic explicitly.

## After
- Difficulty ramp is gentler (3.5 speed ramp/sec).
- Slower initial speed (110) makes the first 20 seconds much more accessible.
- Collision hitboxes have been significantly inset (player: 22% X, 18% Y; blocker: 20% X, 18% Y), meaning a visible gap definitely guarantees no collision.
- A dedicated `LaneDriftCollisionTest` covers the exact boundary logic.
