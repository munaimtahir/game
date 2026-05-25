# Stack Drop Board Rules

**Date**: May 24, 2026

## Dimensions
- **Width**: 10 cells.
- **Height**: 22 cells.

## Spawn Logic
Pieces spawn at the top center (X=5, Y=1). If the spawn position is blocked, the game is immediately over.

## Line Clear Logic
- A row is cleared if all 10 cells are occupied.
- Rows are shifted down from top to bottom.
- Score Bonuses:
    - 1 line: 100
    - 2 lines: 250
    - 3 lines: 450
    - 4 lines (Stack Drop): 700

## Game Over Condition
The game ends when a newly spawned piece collides with existing blocks on the board.
The "Danger Glow" in the UI starts flashing when blocks reach the top 4 rows.
