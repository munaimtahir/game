# Game Test Matrix

Date: 2026-05-26

## Shared Evidence Rules
- Keep every test offline.
- Keep each test repeatable.
- Collect at least:
  - launch evidence
  - ready state evidence
  - active state evidence
  - failure or restart evidence
  - logcat on failure
  - screenshots for the current game state

## Lane Drift
- Launch route:
  - detail: `Routes.LaneDriftDetail`
  - game: `Routes.LaneDriftGame`
- Ready state check:
  - detail screen visible
  - `LaneDriftStartButton` visible
- Active state check:
  - `LaneDriftBoard` visible
  - semantics state description contains lane and item count
- Score visible check:
  - HUD shows score and pickups
- Collision / failure check:
  - game over overlay appears
  - `state.gameOver == true`
- Restart check:
  - restart button returns to active play immediately
- Screenshot states:
  - home
  - lane drift detail / ready
  - lane drift active
  - lane drift game over
- Crash filters:
  - `FATAL EXCEPTION`
  - `ANR in`
  - `Process crashed`
- Stability indicators:
  - items keep moving
  - state description changes over time
  - no unexpected pause loops

## Pulse Orbit
- Launch route:
  - detail: `Routes.PulseOrbitDetail`
  - game: `Routes.PulseOrbitGame`
- Ready state check:
  - detail screen visible
  - start button visible
- Active state check:
  - board visible
  - orbit animation is running
- Score visible check:
  - HUD shows score and combo
- Collision / failure check:
  - failure overlay appears when tap timing misses the gap
- Restart check:
  - retry button returns to a clean run immediately
- Screenshot states:
  - home
  - pulse orbit detail / ready
  - pulse orbit active
  - pulse orbit game over
- Crash filters:
  - `FATAL EXCEPTION`
  - `ANR in`
  - `Process crashed`
- Stability indicators:
  - gap angle advances smoothly
  - no jank at launch
  - retry state resets the run timer

## Stack Drop
- Launch route:
  - detail: `Routes.StackDropDetail`
  - game: `Routes.StackDropGame`
- Ready state check:
  - detail screen visible
  - start button visible
- Active state check:
  - board visible
  - active piece visible
- Score visible check:
  - HUD shows score and level/lines
- Collision / failure check:
  - game over overlay appears when spawn or stack collision occurs
- Restart check:
  - restart button returns to a fresh board
- Screenshot states:
  - home
  - stack drop detail / ready
  - stack drop active
  - stack drop game over
- Crash filters:
  - `FATAL EXCEPTION`
  - `ANR in`
  - `Process crashed`
- Stability indicators:
  - rotation and line-clear logic remain deterministic
  - board math does not clip pieces unexpectedly

## Automation Mapping
- JVM tests:
  - collision math
  - scoring formulas
  - board logic
- Instrumentation tests:
  - screen launch
  - start button reachability
  - back navigation
- ADB smoke:
  - app launch
  - home visible
  - no immediate crash
  - basic game entry
- Screenshot capture:
  - current baseline now
  - deterministic debug states later if exact game-over screenshots are needed
