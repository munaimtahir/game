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
6. Monetization and Hardening (performed in 3 AI-led stages; see the authoritative [BUILD_PLANNING_NEXT_STEPS.md](file:///home/munaim/Documents/github/game/docs/BUILD_PLANNING_NEXT_STEPS.md) for details):
   - **Stage 1 — Repository Discovery & Contract Verification** (Architecture mapping, no behavior changes)
   - **Stage 2 — Consolidated Monetization Implementation** (AdMob, consent, billing, settings UI, and offline checks)
   - **Stage 3 — Release Hardening & Store Handoff** (Device verification, Play Console checks, and final release gates)
