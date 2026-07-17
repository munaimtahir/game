# Deferred Device Validation Checklist

This checklist accumulates all runtime checks intentionally deferred during Stages 1–4.

Status values:

- `DEFERRED`
- `PASS`
- `FAIL`

## Application Shell

- `DEFERRED` Fresh install
- `DEFERRED` First launch
- `DEFERRED` Warm launch
- `DEFERRED` Home navigation
- `DEFERRED` Settings navigation
- `DEFERRED` Background and foreground
- `DEFERRED` Process recreation
- `DEFERRED` Force-stop and reopen
- `DEFERRED` Rotation if supported
- `DEFERRED` Increased font scale
- `DEFERRED` Gesture navigation
- `DEFERRED` Three-button navigation
- `DEFERRED` Edge-to-edge overlap inspection
- `DEFERRED` Cutout inspection

## Pulse Orbit

- `DEFERRED` Guidance flow
- `DEFERRED` Tap response
- `DEFERRED` Scoring
- `DEFERRED` Combo and perfect-pass behavior
- `DEFERRED` Pause and background pause
- `DEFERRED` Rapid restart
- `DEFERRED` Multi-touch handling
- `DEFERRED` Sound behavior
- `DEFERRED` Haptic behavior
- `DEFERRED` Result persistence
- `DEFERRED` High-score persistence

## Lane Drift

- `DEFERRED` Guidance flow
- `DEFERRED` Swipe response
- `DEFERRED` No accidental multi-lane shift
- `DEFERRED` Obstacle-route fairness
- `DEFERRED` Near-miss behavior
- `DEFERRED` Speed progression
- `DEFERRED` Collision fairness
- `DEFERRED` Rapid restart
- `DEFERRED` Lifecycle handling

## Stack Drop

- `DEFERRED` Guidance flow
- `DEFERRED` On-screen control target size
- `DEFERRED` Compact-screen usability
- `DEFERRED` Move left and right
- `DEFERRED` Rotation
- `DEFERRED` Soft drop
- `DEFERRED` Hard drop
- `DEFERRED` Line clearing
- `DEFERRED` Pause and resume
- `DEFERRED` High-speed behavior
- `DEFERRED` Game-over detection

## Shared Progression

- `DEFERRED` Session count
- `DEFERRED` Statistics accuracy
- `DEFERRED` Currency persistence
- `DEFERRED` Theme unlock and application
- `DEFERRED` Streak behavior
- `DEFERRED` Daily challenge rollover
- `DEFERRED` Bundle challenge progress
- `DEFERRED` Duplicate-claim prevention on runtime
- `DEFERRED` Offline day behavior

## Monetization

- `DEFERRED` No ad during active gameplay
- `DEFERRED` Frequency caps
- `DEFERRED` Ad-failure safety
- `DEFERRED` Offline gameplay with ads unavailable
- `DEFERRED` Premium purchase success path
- `DEFERRED` Premium cancelled purchase path
- `DEFERRED` Premium pending purchase path where testable
- `DEFERRED` Premium entitlement restore
- `DEFERRED` Premium removes advertisements
- `DEFERRED` No subscription paths or wording in runtime UI
