# Pulse Orbit Specification

## Core Fantasy

Minimal one-tap timing under increasing rotational pressure.

## Screens And States

- detail screen
- tutorial overlay
- ready state
- active state
- paused state
- failed state
- result state

## Gameplay Objects

- one fixed center ring
- one moving orb on the ring path
- one traversable ring gap

## Input Rules

- Input type: single tap anywhere on gameplay surface
- In ready state:
  - first tap starts the run
- In active state:
  - tap attempts a pass at the orb’s current angle
- Repeated taps after resolution of a failed state do nothing until retry

## Orb And Ring Behavior

- Orb rotates clockwise only in MVP.
- Ring stays visually fixed; the gap changes position after successful passes.
- No MVP direction reversals.

## Gap Generation

- Initial gap size: `88°`
- Minimum gap size: `40°`
- Gap shrink per successful pass: `1.2°`
- Gap center offset from previous gap:
  - base step `72°`
  - step growth `3.5°` per pass
  - max step `140°`
- Offset sign alternates or is seed-driven to avoid trivial repetition.
- New gap center must not equal previous center.

## Pass Resolution

- A tap succeeds if the orb center is within half the current gap width plus fairness tolerance.
- Collision fairness tolerance: `4.5°`
- On success:
  - increment passes
  - update score
  - move gap
  - increase speed
- On failure:
  - enter failed state immediately

## Perfect Pass Definition

- Perfect pass occurs when orb center is within `6°` of gap center at tap time.

## Scoring Formula

- Clean pass, non-perfect: `+1`
- Perfect pass: `+2`
- Every fifth combo step adds `+1` extra bonus on that pass

## Combo Formula

- Combo increases by `1` only on perfect passes.
- Non-perfect successful pass resets combo to `0`.
- Failure resets combo to `0`.
- `bestCombo` tracks the highest combo reached in the run.

## Difficulty Progression

- Rotation speed starts at `85°/s`
- Speed increases by `4.2°/s` per successful pass
- Maximum speed: `230°/s`
- Gap size and gap relocation both scale with pass count.

## Failure Detection

- Failure occurs when a tap resolves outside the valid gap window.
- No passive failure without input.

## Pause And Resume

- App background or explicit pause opens paused state.
- Resume continues from the same orb angle, gap center, gap size, score, and combo.
- Timer accumulation excludes paused duration for reward purposes.

## Restart Timing

- Failed state should expose instant retry on the same screen.
- Target player path:
  - fail
  - see score/result
  - retry with one tap

## Reward Calculation

- Run coins: `score + bestCombo`
- Challenge and milestone rewards are added separately by shared systems.

## First-Run Guidance

- Show once before first run:
  - tap when orb aligns with the opening
  - perfect timing builds combo
  - speed rises as passes increase

## Visual Identity

- clean circular arena
- bright orb accent against calm board
- timing-focused minimal HUD
- readable success and fail flashes

## Sound And Haptics

- tap: light confirmation
- perfect pass: brighter success cue
- combo threshold: stronger pulse cue
- fail: short low fail cue
- haptics:
  - short tick on success
  - stronger pulse on perfect threshold
  - failure bump on miss

## Accessibility

- reduced effects disables repeated ready pulsing and lowers burst intensity
- high contrast strengthens gap edge and orb readability
- score and combo remain text-readable without relying on color

## Deterministic Testing Hooks

- injectable seed for gap movement sequence
- fixed-step update mode
- direct pass-resolution helper with orb angle and gap inputs
- exported tuning constants for tests
