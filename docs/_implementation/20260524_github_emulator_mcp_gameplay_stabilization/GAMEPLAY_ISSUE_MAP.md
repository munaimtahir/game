# Gameplay Issue Map

## Lane Drift (Priority 1)
- **Collision Rules**: Current collision logic uses simple rect overlap in `LaneDriftCollision.kt`.
- **Issues**:
  - Unforgiving hitboxes (visual bounds might exceed fair collision bounds).
  - Difficulty ramp is too steep initially.
  - Readability of blockers vs. pickups vs. background needs improvement.

## Pulse Orbit (Priority 2)
- **Issues**: First impression hero game needs clearer timing fairness, crisp tap mechanics, better score feedback, and immediate retry loops.

## Stack Drop (Priority 3)
- **Issues**: Needs board math auditing, predictable block clipping/rotation logic, and satisfying line clear mechanics.
