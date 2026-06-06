# Repository Map

## Android Project Structure
- Application ID: `com.vexel.offlinearcade`
- Min SDK: 24
- Target SDK: 35
- Compile SDK: 35
- Version Code: 3
- Version Name: 1.0.2

## Gradle Modules
- `app`
- `core:common`
- `core:model`
- `core:data`
- `core:ui`
- `feature:home`
- `feature:challenges`
- `feature:stats`
- `feature:settings`
- `game:pulseorbit`
- `game:lanedrift`
- `game:stackdrop`
- `game:brickvolley`
- `game:loopsnake`
- `game:shielddash`
- `game:gravityflip`

## Games Included
- Pulse Orbit
- Lane Drift
- Stack Drop
- Brick Volley
- Loop Snake
- Shield Dash
- Gravity Flip

## Entry Points
- `MainActivity` exported and acting as the main launcher.

## Signing Configuration
- Currently set up using `key.properties` for `release` builds.

## SDKs
- Compose, Navigation, Room, DataStore. No external ad/billing SDKs present yet, except possibly standard compose components.
