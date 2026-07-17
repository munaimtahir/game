# Stack Drop Specification

## Core Fantasy

Original falling-block play with clear controls, readable board state, and no branded borrowing.

## Board

- visible width: `10`
- total height: `22`
- top `2` rows act as spawn buffer

## Piece Set

Five original block families with neutral naming:

- Bar
- Square
- Tee
- Hook
- Zig

Implementation may keep internal short identifiers, but UI and design docs should avoid branded mimicry.

## Spawn Rules

- Spawn horizontally centered where possible.
- Initial spawn row uses the upper buffer area.
- Next-piece preview is shown in the HUD.
- Initial bag uses deterministic seeded random with uniform distribution over the five-piece set.

## Movement Rules

- left move: one column
- right move: one column
- rotate: clockwise only in MVP
- soft drop: accelerate descent while held
- hard drop: immediate placement

## Rotation And Wall Kicks

- Use a simple custom kick table:
  - origin
  - left
  - right
  - up
  - double left
  - double right
- If all kick attempts fail, rotation is rejected.

## Collision

- A piece collides if any occupied cell would move outside board bounds or overlap a filled cell.

## Lock Delay

- Base lock delay: `450ms`
- Lock timer starts when a grounded piece can no longer fall.
- Up to `2` movement or rotation resets are allowed before forced lock.

## Line Clearing

- Completed horizontal rows clear immediately after lock.
- Rows above fall downward in the same resolution step.

## Scoring

- soft-drop cell: `+1`
- hard drop: `+2` per dropped row
- single line: `+100`
- double line: `+250`
- triple line: `+450`
- four-line clear: `+700`

## Levels And Speed

- Level starts at `1`
- Level increases every `8` cleared lines
- Base gravity interval:
  - level 1: `700ms`
  - subtract `35ms` per level
  - clamp minimum to `160ms`

## Game Over

- Game over when a newly spawned piece immediately collides.
- Also game over if a locked stack occupies the visible top boundary after resolution.

## Pause And Resume

- Backgrounding pauses active play.
- Resume restores board, active piece, next piece, score, level, lock state, and timers.
- Corrupt in-progress board state falls back to ready state instead of awarding results.

## Touch Control Layout

- MVP uses on-screen controls, not gesture-only play.
- Bottom dock:
  - left
  - right
  - rotate
  - soft drop
  - hard drop
- Buttons must stay reachable in portrait on compact phones.

## Compact-Screen Behavior

- Board scales first.
- Next-piece preview compresses before controls do.
- HUD can collapse into two rows above the board.

## Reward Calculation

- Run coins:
  - `linesCleared * 4 + floor(score / 40)`

## First-Run Guidance

- move pieces left and right
- rotate to fit gaps
- clear full rows
- stack too high ends the run

## Visual Identity

- bright block palette on calm board
- subtle grid separation
- clean line-clear flash
- danger emphasis near top without copying legacy puzzle-game branding

## Sound And Haptics

- move: soft click
- rotate: sharper click
- line clear: satisfying multi-tone cue
- hard drop: heavier impact cue
- game over: short fail cue

## Accessibility

- reduced effects shortens flash duration and removes nonessential background motion
- high contrast strengthens block outlines and grid lines
- controls require both icon and text or tooltip-level semantics in tests/accessibility layers

## Deterministic Testing Hooks

- injectable seed for piece order
- pure engine API for move/rotate/drop/tick/lock
- serializable board snapshot for save/restore tests
- exported scoring and gravity tables
