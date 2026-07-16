# Implementation Backlog

## Stage 1

### 1. Foundation Hardening

Dependencies:

- locked shared systems spec
- locked architecture

Acceptance criteria:

- app starts into home reliably
- three MVP games are the only public launcher entries
- Room schema migration path exists without destructive release fallback
- settings persist safely
- pause/resume contract is common across games
- shared reward, stats, and result interfaces exist

### 2. Pulse Orbit Vertical Slice

Dependencies:

- foundation hardening

Acceptance criteria:

- full ready, active, paused, failed, and result flow implemented
- score, combo, gap generation, and failure logic match spec
- high score and coins write correctly
- restart path is immediate and stable
- tutorial and accessibility options work

### 3. Stage 1 Testing

Acceptance criteria:

- unit tests cover scoring, combo, difficulty, gap generation, collision, save/restore, malformed local data, and deterministic hooks
- one end-to-end UI journey exists for launch to retry to home

## Stage 2

### 1. Lane Drift

Dependencies:

- shared foundation proven by Pulse Orbit

Acceptance criteria:

- lane control, safety constraints, pickups, near misses, scoring, pause, result, and rewards match spec
- deterministic obstacle generation is testable

### 2. Stack Drop

Dependencies:

- shared foundation proven by Pulse Orbit

Acceptance criteria:

- board engine, rotation, kicks, lock delay, clears, scoring, levels, controls, pause, and rewards match spec
- presentation remains original and non-branded

### 3. Integration

Acceptance criteria:

- all three games use the same stats, settings, reward, and navigation contracts
- interruption and process recreation are safe across all three

## Stage 3

### 1. Shared Progression

Acceptance criteria:

- per-game and global stats complete
- coins, unlocks, streaks, and challenge history persist
- duplicate rewards are blocked by ledger rules

### 2. Daily Challenges

Acceptance criteria:

- deterministic local-day generation works offline
- claim state is idempotent
- challenge history retention and rollover work

### 3. Balance Validation

Acceptance criteria:

- reward rates simulated and documented
- no one game dominates currency earnings
- grind pressure remains modest

## Stage 4

### 1. Ads

Acceptance criteria:

- central eligibility policy implemented
- no active-play ads
- offline and ad-load-failure paths are safe

### 2. Premium

Acceptance criteria:

- one-time purchase works
- restore works
- cached offline entitlement works
- premium only affects ads/cosmetics

### 3. Release Hardening

Acceptance criteria:

- release build succeeds
- declarations and docs match shipped behavior
- final QA and performance reports are complete
