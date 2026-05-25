# Stack Drop Before / After

**Date**: May 24, 2026

## Before
- **Board Height**: 18 rows (felt slightly cramped on tall screens).
- **Rotation**: Simple rotation; failed if any cell of the rotated piece was out of bounds or collided. No "Wall Kicks".
- **Difficulty Ramp**: Speed increased every 6 lines by -45ms (felt too fast for a casual arcade).
- **Minimum Interval**: 180ms.

## After
- **Board Height**: Increased to 22 rows (better verticality and premium feel).
- **Rotation (Wall Kicks)**: Implemented basic Wall Kicks. If a rotation collides, the engine automatically tries shifting the piece (left, right, then up/floor-kick) before failing. This makes the controls feel much more fluid.
- **Difficulty Ramp**: Speed increases every 8 lines by -35ms (gentler progression for longer sessions).
- **Minimum Interval**: 160ms.
- **Verified Stability**: New unit tests for Wall Kicks and line clear logic ensure the engine is robust.
