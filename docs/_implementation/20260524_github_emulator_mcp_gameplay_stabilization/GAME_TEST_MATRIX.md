# Game Test Matrix

**Date**: May 24, 2026

## Lane Drift
- **Launch Route**: Home -> Select Lane Drift -> Game Screen
- **Ready State**: Player character visible, 3 lanes drawn, waiting for input.
- **Active State**: Score increases, obstacles spawn, lanes change smoothly.
- **Collision Rules**: 
  - Visual gap = no collision.
  - Generous horizontal/vertical insets for player vs blockers.
- **Restart Check**: Tapping 'Retry' instantly resets score to 0 and clears items.

## Pulse Orbit
- **Ready State**: Orbit ring visible, single tap to start.
- **Active State**: Node rotates.
- **Collision Rules**: Timing of tap must align with gap. Fair tolerance required.

## Stack Drop
- **Ready State**: Empty grid, next piece preview visible.
- **Active State**: Blocks drop, controls respond to horizontal shift and rotation.
- **Collision Rules**: No unfair clipping into landed blocks. Full lines clear correctly.
