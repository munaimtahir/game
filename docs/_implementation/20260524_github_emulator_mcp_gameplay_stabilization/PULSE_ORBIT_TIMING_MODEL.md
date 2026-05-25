# Pulse Orbit Timing Model

**Date**: May 24, 2026

## Precision vs Fairness
Pulse Orbit is a rhythm game. At high speeds, a 16ms frame (60fps) corresponds to multiple degrees of rotation. If the collision check is pixel-perfect against the angular gap, the "correct" tap window might be only 2-3 frames long.

## The Grace Buffer
By adding `collisionToleranceDegrees` (4.5), we expand the tap window by ~9 degrees total.
This compensates for:
- Input latency (Bluetooth/Touch screen lag).
- Human reaction variability.
- Frame timing jitter.

The logic now is:
`distance <= (gapSize / 2) + tolerance`

This ensures that if the orb *looks* like it's in the gap, it counts as a hit.
