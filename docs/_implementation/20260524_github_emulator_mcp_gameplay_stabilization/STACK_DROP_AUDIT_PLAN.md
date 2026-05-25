# Stack Drop Audit & Plan

**Date**: May 24, 2026

## Current Audit
- **Core Loop**: Standard falling block puzzle logic.
- **Rotation**: Simple rotation without "Wall Kick" or "Floor Kick" logic. If a rotation causes a collision (e.g., against a wall), it just fails.
- **Board**: 10x18 grid.
- **Difficulty**:
    - Speed increases every 6 lines (`1 + totalLines / 6`).
    - Base drop interval: 700ms.
    - Speed ramp: -45ms per level.
    - Minimum drop interval: 180ms.
- **Visuals**: Basic canvas rendering of the grid and active piece.

## Identified Issues
1. **Rotation Frustration**: Without "Wall Kicks", rotating a piece near the edge or against other blocks often fails silently, which feels "clunky" or "broken" to players.
2. **First Impression**: The initial speed (700ms) is standard, but the difficulty ramp might be too aggressive for a casual arcade (-45ms/level).
3. **Board Height**: 18 rows is slightly short for a modern mobile screen; could be 20-22 for a better "premium" feel.
4. **Visual Feedback**: Line clear feedback is just a state change. No animation or "flash" is evident in the logic (though it might be in the Screen).
5. **Score Fairness**: Line clear bonuses (100, 250, 450, 700) are okay but could be scaled for higher levels.

## Improvement Plan
1. **Basic Wall Kicks**: When rotating, if the new position collides, try shifting the piece left, right, or up by 1-2 cells before giving up.
2. **Gentler Ramp**: Reduce speed ramp from -45ms to -35ms per level to extend the session length.
3. **Board Height Update**: Increase height from 18 to 22 for better verticality.
4. **Collision Grace (Lock Delay)**: Add a small grace period (e.g., 500ms) where a piece can still move/rotate after touching the bottom before it locks. This is a standard "Tetris" feel improvement.
5. **Unit Tests**: Add tests for Wall Kicks and Lock Delay.
