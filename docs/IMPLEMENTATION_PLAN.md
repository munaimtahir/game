# Implementation Plan

## Locked baseline
- Exactly 3 MVP games
- Offline-first
- Shared progression
- Daily challenges
- Local stats
- Minimal settings
- One-time premium unlock allowed
- No accounts, cloud sync, subscriptions, or leaderboards

## Module graph
- app
- core:model
- core:data
- core:common
- core:ui
- feature:home
- feature:challenges
- feature:stats
- feature:settings
- game:pulseorbit
- game:lanedrift
- game:stackdrop

## Navigation flow
Home -> Game / Challenges / Stats / Settings
Game over -> Retry / Home / Challenges

## Persistence plan
Room:
- stats
- unlocks
- challenge progress

DataStore:
- sound
- music
- vibration
- lightweight app preferences

## Shared systems
- coins
- streak
- themes
- local stats
- offline daily challenges

## Implementation order
1. Foundation
2. Pulse Orbit
3. Lane Drift
4. Stack Drop
5. Shared meta completion
6. Monetization and hardening
