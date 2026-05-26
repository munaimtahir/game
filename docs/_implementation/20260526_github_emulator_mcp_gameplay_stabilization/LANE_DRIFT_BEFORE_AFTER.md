# Lane Drift Before / After

Date: 2026-05-26

## Before
- Early runs felt too strict when blockers visually looked close but should have been survivable.
- The first seconds ramped faster than the screen readability justified.
- The lane background and traffic accents were not distinct enough for a fast one-thumb read.
- Screenshot and smoke evidence were too generic to reliably show the game state transitions.

## After
- Lane Drift now uses a more forgiving collision envelope and a slightly larger minimum overlap threshold before a crash is registered.
- Early difficulty is gentler:
  - slower starting speed
  - slower speed ramp
  - slower spawn cadence
  - a longer grace window
  - center lane blockers are suppressed in the opening seconds
- The board now has clearer lane separators and a stronger current-lane highlight.
- The debug build gets a deterministic Lane Drift seed, which makes emulator evidence more repeatable.
- ADB smoke and screenshot helpers now drive screens by visible text instead of hard-coded tap coordinates.

## What This Fix Is
- A fairness pass, not a rewrite.
- It keeps the original game loop and one-thumb control model.
- It stays offline and low-end friendly.

## What This Fix Is Not
- It is not a new game mode.
- It is not a monetization change.
- It is not a release-only behavioral change beyond normal tuning.

## Evidence Status
- Local build, unit tests, and lint pass.
- GitHub Actions emulator evidence still needs an actual run to produce artifact-backed screenshots.

## Current Verdict
- Lane Drift is now ready for focused design polish and emulator evidence capture.
