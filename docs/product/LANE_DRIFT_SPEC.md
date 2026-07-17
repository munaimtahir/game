# Lane Drift Specification

## Core Fantasy

Readable three-lane dodge flow with compact-screen one-hand control.

## Control System

- Single control scheme for MVP: horizontal swipe only
- Left swipe moves one lane left
- Right swipe moves one lane right
- No tap, hold, or tilt control in MVP

## Lane Model

- Lane count: `3`
- Player starts in center lane

## Movement Interpolation

- Logical lane changes immediately for collision rules.
- Visual car slides to the new lane center over `90ms`.
- Ignore inputs that would move outside lanes `0..2`.

## States

- detail screen
- tutorial overlay
- ready state
- active state
- paused state
- failed state
- result state

## Obstacle Families

Cosmetic families only; all block movement equally:

- cone
- barrier
- crate
- barrel
- pothole

## Pickup Types

Cosmetic families only; all grant the same gameplay effect in MVP:

- coin
- star
- energy
- gem
- fuel

## Generation Safety Constraints

- At most one blocker spawn row at a time.
- A blocker row always leaves at least two safe lanes, or one safe lane plus an offset pickup lane during later difficulty.
- No blocker and pickup may spawn in the same lane and row.
- Early grace phase lasts `18s`.
- During the first `4s`, the center lane is never chosen for blockers.
- During grace phase, spawn interval is lengthened and speed scaled down.
- The same blocker lane may not repeat more than twice in a row after grace.

## Impossible-Pattern Prevention

- Never fill all three lanes simultaneously.
- Never generate an unavoidable blocker immediately after a forced lane change.
- Maintain minimum vertical spacing between successive blocker rows based on current speed and minimum human reaction window.
- Near-miss windows must not overlap actual collision windows.

## Collision Rules

- Collision only with blockers in the player’s current lane.
- Use forgiving hitboxes smaller than visuals.
- Blockers take precedence over pickups on the same frame.
- Pickup contact grants reward and removes that pickup.

## Near-Miss Definition

- A near miss occurs when a blocker passes within the configured visual band of the player in the same lane without meeting blocker collision overlap thresholds.
- Each blocker can award at most one near miss.

## Distance And Score

- Distance accumulates continuously from run start based on scroll speed.
- Display score:
  - `floor(distanceMeters / 10)`
  - `+ 10` per pickup
  - `+ 6` per near miss
  - `+ combo bonus` on event scoring

## Combo Rules

- Combo increases by `1` on each pickup or near miss.
- Combo resets if `2.5s` pass without a combo event.
- Event bonus on pickup or near miss:
  - `min(10, comboBeforeEvent)`

## Speed And Difficulty Curve

- Initial speed: `92`
- Speed ramp: `+2.7` per second
- Max speed: `300`
- Initial spawn interval: `2.05s`
- Minimum spawn interval: `0.90s`

## Rush Sections

- Rush sections are implicit difficulty bands, not separate modes.
- At higher elapsed time, blockers may appear with tighter spacing and more frequent pickups for risk/reward pacing.

## Reward Calculation

- Run coins:
  - `floor(score / 20) + pickups`

## First-Run Guidance

- swipe left or right to change lanes
- avoid coral hazards
- collect cyan pickups
- close calls can help score

## Visual Identity

- light roadway with lane readability
- cyan player vehicle
- coral hazard family
- mint and gold pickup accents
- speed lines kept lightweight

## Sound And Haptics

- lane shift: light tap cue
- pickup: bright short cue
- near miss: sharper tension-release cue
- crash: short fail cue
- haptics:
  - light pulse on pickup
  - medium pulse on near miss
  - stronger pulse on crash

## Accessibility

- reduced effects lowers motion-line density and flash intensity
- high contrast strengthens lane separators and hazard outlines
- controls must remain effective on gesture-navigation phones

## Deterministic Testing Hooks

- injectable random seed
- spawn-sequence generator test entry point
- collision and near-miss helpers isolated from Compose rendering
- exported tuning constants for speed, spacing, and grace phase
