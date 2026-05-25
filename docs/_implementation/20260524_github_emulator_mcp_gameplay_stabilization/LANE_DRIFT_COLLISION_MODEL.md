# Lane Drift Collision Model

**Date**: May 24, 2026

## Concept
Lane Drift relies on a fraction-based inset collision model. Objects have a "visual rect" based on their lane and vertical coordinate. The "hitbox" is shrunk inwards by a percentage defined in `LaneDriftTuning`.

## New Tuning
To ensure "fair" feeling gameplay:
- **Player Hitbox**: Shrunk horizontally by 22% and vertically by 18%.
- **Blocker Hitbox**: Shrunk horizontally by 20% and vertically by 18%.
- **Pickup Hitbox**: Shrunk by 15% symmetrically.
- **Overlap Thresholds**: Collision only counts if the *already shrunk* hitboxes overlap by more than 8dp (blocker) or 6dp (pickup).

This effectively creates a large visual grace margin. Players can visually clip slightly into an obstacle without dying.
