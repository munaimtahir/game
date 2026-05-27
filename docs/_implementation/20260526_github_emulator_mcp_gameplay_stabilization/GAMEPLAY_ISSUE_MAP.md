# Gameplay Issue Map

Date: 2026-05-26

## Lane Drift
- Priority: 1
- Current focus:
  - collision fairness and perceived hitbox size
  - early-session readability
  - speed ramp and spawn cadence
  - clearer lane separation and obstacle affordance
  - deterministic debug/test behavior for screenshot and emulator evidence
- Current implementation notes:
  - collision is already factored into [`LaneDriftCollision.kt`](../../../game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftCollision.kt)
  - the screen uses insets and dp-to-px conversion to keep fairness consistent across densities
  - the update loop already applies a grace phase and slower early ramp
- Remaining risk:
  - even with a forgiving hitbox model, the visual composition still needs a stronger "what to avoid" hierarchy

## Pulse Orbit
- Priority: 2
- Current focus:
  - tap timing fairness
  - gap rhythm clarity
  - faster first-impression comprehension
  - score/combo presentation
  - instant retry and clean fail feedback
- Current implementation notes:
  - the timing model and retry loop are already implemented in the screen
  - the game is a good candidate for polish once Lane Drift has a clean pass

## Stack Drop
- Priority: 3
- Current focus:
  - board math stability
  - rotation / kick fairness
  - line-clear feedback
  - early-session accessibility
  - control readability
- Current implementation notes:
  - the engine is isolated and testable
  - board/rotation/lock behavior is already reasonably self-contained

## What Not To Do
- Do not split the next sprint across multiple games.
- Do not rewrite the home screen into a large bundle of extra modes.
- Do not add monetization inside active play.
- Do not treat the presence of extra legacy game modules as permission to widen scope.

## One-Game Rule
- The next gameplay change should stay on Lane Drift until it has a documented before/after report and screenshot evidence.
