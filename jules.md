# Project Context: Offline Mini Arcade

## Project Overview
Offline Mini Arcade is an offline Android arcade app. The baseline MVP scope is locked and strictly defined.

### Core Features:
- Exactly 3 MVP games:
  - Pulse Orbit
  - Lane Drift
  - Stack Drop
- Offline-first only (No accounts, cloud sync, subscriptions, or leaderboards)
- Shared progression (Coins, streaks, themes)
- Daily challenges
- Local stats (High score, sessions played, total playtime, etc.)
- Minimal settings
- One-time premium unlock allowed

## Development Setup
- **Requirements:** Java 17, Android SDK / Android Studio, `adb` on `PATH`, and a physical Android device with USB debugging enabled for validation.
- **Bootstrap command:** `./scripts/bootstrap_laptop_android.sh`

## Module Architecture
The project follows a multi-module architecture:
- `app`: The main application module, wiring up the navigation and overall app setup.
- `core`:
  - `core:model`: Shared data models (GameId, PlayerProfile, GameStats, etc.)
  - `core:data`: Repository layer handling Room (stats, unlocks, challenge progress) and DataStore (sound, music, vibration, lightweight preferences)
  - `core:common`: Utility functions and shared helpers
  - `core:ui`: Common Compose UI components, themes, and design system elements
- `feature`:
  - `feature:home`: Home screen UI
  - `feature:challenges`: Daily challenges UI
  - `feature:stats`: Local stats tracking UI
  - `feature:settings`: App settings UI
- `game`:
  - `game:pulseorbit`: Logic and UI for Pulse Orbit
  - `game:lanedrift`: Logic and UI for Lane Drift
  - `game:stackdrop`: Logic and UI for Stack Drop

## Persistence & State
- **Room Database:** Used for saving game stats, unlocked items, and daily challenge progress.
- **DataStore:** Used for saving lightweight app preferences like sound, music, vibration, and reduced effects settings.
- **Shared Systems:** The project unifies elements across games, such as coins, streak logic, themes, local stats, and offline daily challenges.

## Implementation Order
According to the implementation plan, the progress is structured as:
1. Foundation
2. Pulse Orbit
3. Lane Drift
4. Stack Drop
5. Shared meta completion
6. Monetization and hardening

## Current Status
The project establishes a solid foundation with Compose, a clear multi-module separation, and unit/instrumentation tests. Development focuses on ensuring robust offline-first functionality without any cloud or server dependencies.
