# Stack Drop Control Model

**Date**: May 24, 2026

## Input Handling
- **Tap**: Rotate piece (uses Wall Kicks for fairness).
- **Swipe Left/Right**: Move piece horizontally.
- **Swipe Down**: Soft drop (advances piece by 1 row and awards 1 point).
- **Flick Down**: Hard drop (instantly locks piece at the bottom).

## Wall Kicks
When rotating, the engine tries the following offsets in order:
1. `(0, 0)`: Center
2. `(-1, 0)`: 1 cell Left
3. `(1, 0)`: 1 cell Right
4. `(0, -1)`: 1 cell Up (Floor Kick)
5. `(-2, 0)`: 2 cells Left
6. `(2, 0)`: 2 cells Right

This prevents the piece from getting "stuck" against walls or existing blocks during rotation.
