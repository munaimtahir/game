# Baseline Repo Audit

**Date**: May 24, 2026

## Project Structure
- **Root**: `com.vexel.offlinearcade`
- **Modules**: `:app`, `:core:common`, `:core:model`, `:core:data`, `:core:ui`, `:feature:home`, `:feature:challenges`, `:feature:stats`, `:feature:settings`, `:game:pulseorbit`, `:game:lanedrift`, `:game:stackdrop`, and others.
- **Languages**: Kotlin (1.9.24)
- **UI Framework**: Jetpack Compose (BOM 2024.06.00, compiler 1.5.14)
- **Build System**: Gradle 8.5.2 (using KTS)
- **JDK Requirements**: Java 17

## API Levels
- **compileSdk**: 35
- **targetSdk**: 35
- **minSdk**: 24

## Static Analysis
- **Lint**: Lint issues exist (e.g., `removeLast()` requiring API 35). Currently fixed to stabilize CI baseline.
- **Tests**: Robolectric, JUnit4, Espresso setup present but coverage unknown.

## Known Architecture
- Modular architecture focusing on core UI, data, features, and individual games.
- Games use Compose-based state loops and custom canvas/rendering, rather than a full engine like LibGDX.
