# Baseline Repository Map
**Date**: May 2026
**Project**: Offline Mini Arcade / OfflineArcade

## Module Graph
The repository is modularized with the following structure:
- `app`: Main entry point and composition root.
- `core`: Shared foundational layers:
  - `core:common`
  - `core:model`
  - `core:data` (Repository, State, persistence)
  - `core:ui` (Shared UI components)
- `feature`: App-level screens:
  - `feature:home`
  - `feature:challenges`
  - `feature:stats`
  - `feature:settings`
- `game`: The mini-games:
  - `game:lanedrift`
  - `game:pulseorbit`
  - `game:stackdrop`

## Application Info
- **Package Name**: `com.vexel.offlinearcade`
- **Application ID**: `com.vexel.offlinearcade`
- **Architecture**: Jetpack Compose, single-activity, offline-first.

## Important Files
- **Navigation**:
  - `app/src/main/java/com/vexel/offlinearcade/ArcadeNavHost.kt`
  - `app/src/main/java/com/vexel/offlinearcade/ArcadeRoutes.kt`
- **Shared State & Progression**:
  - `core/data/src/main/java/com/vexel/offlinearcade/core/data/OfflineArcadeRepository.kt`
  - `app/src/main/java/com/vexel/offlinearcade/ArcadeViewModel.kt`
- **Daily Challenge Logic**:
  - `core/data/src/main/java/com/vexel/offlinearcade/core/data/ChallengeGenerator.kt`
  - `feature/challenges/src/main/java/com/vexel/offlinearcade/feature/challenges/ChallengesScreen.kt`
- **Game Screens**:
  - `game/pulseorbit/src/main/java/com/vexel/offlinearcade/game/pulseorbit/PulseOrbitScreen.kt`
  - `game/lanedrift/src/main/java/com/vexel/offlinearcade/game/lanedrift/LaneDriftScreen.kt`
  - `game/stackdrop/src/main/java/com/vexel/offlinearcade/game/stackdrop/StackDropScreen.kt`

## Existing Test Suite
- **Unit Tests**:
  - `ArcadeViewModelTest.kt`, `DailyChallengeGeneratorTest.kt`, `OfflineArcadeRepositoryPersistenceTest.kt`
  - Game logic tests (`LaneDriftLogicTest.kt`, `PulseOrbitTuningTest.kt`, `StackDropEngineTest.kt`)
- **Instrumentation Tests**:
  - `NavigationSmokeTest.kt`, `BackNavigationTest.kt`
  - `GameplayDeviceSmokeTest.kt`, `SettingsPersistenceSmokeTest.kt`, `LifecyclePauseTest.kt`

## Existing CI Workflows
Located in `.github/workflows/`:
- `adb-emulator-testing.yml`
- `adbtesting.yaml`
- `android-emulator-screenshots.yml`

## Risks Before Changes
- Breaking the already working `Pulse Orbit` daily challenge progress logic.
- Adding online dependencies unintentionally (breaks offline-first guarantee).
- Introducing performance jank with new UI polish on low-end devices.
- Breaking existing screenshot or UI test suite by modifying essential UI semantics.