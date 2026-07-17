# Technical Architecture

## Selected Stack

- Android app with Kotlin
- Jetpack Compose for shell and gameplay rendering
- Compose Navigation for app routing
- Room for structured local persistence
- `SharedPreferences` settings store for MVP unless later migration is justified
- Coroutines and flows for state updates

## Module Boundaries

- `app`
  - application shell
  - navigation
  - dependency assembly
- `core:model`
  - shared models and catalogs
- `core:data`
  - persistence
  - challenge generation
  - repository
- `core:common`
  - clock
  - dispatchers
  - support abstractions
- `core:ui`
  - theme system
  - shared scaffolds
  - gesture and overlay helpers
- `feature:*`
  - home
  - challenges
  - stats
  - settings
  - marketplace / cosmetics
- `game:*`
  - isolated game detail screen, gameplay screen, engine, tests

## Navigation

- Start destination: home
- Each game uses:
  - detail route
  - gameplay route
- Shared routes:
  - challenges
  - stats
  - settings
  - cosmetics

## State Management

- Single app-level `ArcadeViewModel` exposes `ArcadeSnapshot`
- Shared repository owns persistence-facing business state
- Each game owns transient run state locally in gameplay screen plus engine/helpers
- Pure game logic should be isolated into testable helpers or engine classes

## Game Loop Strategy

- Pulse Orbit:
  - frame-driven loop using Compose frame clock
- Lane Drift:
  - frame-driven loop with seeded spawn generator
- Stack Drop:
  - engine tick loop using gravity interval plus input commands

## Persistence Strategy

- Room tables as defined in `SHARED_SYSTEMS_SPEC.md`
- Explicit migrations only
- Atomic reward/stats/profile updates
- Save health and repair path for corrupt partial data

## Dependency Injection

- Current repo uses manual dependency assembly in `ArcadeDependencies`
- MVP can continue with manual DI
- Abstractions must exist for:
  - repository
  - clock
  - dispatchers
  - audio/haptics
  - ads
  - billing

## Audio And Haptics

- Introduce a shared feedback abstraction at app level
- Game screens emit semantic events, not raw platform calls

## Ads

- One ad policy component decides whether any ad request is allowed
- UI screens ask the abstraction, never the SDK directly

## Billing

- One billing abstraction owns:
  - product query
  - purchase flow
  - acknowledgement
  - restore
  - cached entitlement

## Testing Structure

- unit tests
  - game engines
  - scoring
  - generators
  - repository logic
  - migrations
- UI / instrumentation tests
  - launch
  - navigation
  - tutorial flow
  - play-fail-retry
  - settings persistence
  - process recreation where practical

## Performance Instrumentation

- cold start timing
- warm start timing
- navigation-to-game timing
- repeated restart stability
- frame pacing spot checks
- memory sampling during prolonged sessions

## Build Variants

- `debug`
  - testing hooks allowed
  - seeded debug configs allowed
- `release`
  - no destructive migration fallback
  - no debug-only routes
  - production-ready shrink/sign pipeline

## Release Configuration

- Keep current signing approach via `key.properties` or environment variables
- Harden manifest, permissions, and packaging in Stage 4

## Repository Scope Note

- Extra game modules currently present in the build graph are legacy/prototype artifacts, not public MVP architecture commitments.
