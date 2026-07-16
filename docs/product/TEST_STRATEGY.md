# Test Strategy

## Stage 0 Purpose

This file defines the test plan that later stages must satisfy. No gameplay implementation work is performed in this stage.

## Unit Test Priorities

- save schema migrations
- corrupt data recovery
- reward ledger idempotency
- streak rules
- daily challenge generation determinism
- per-game scoring and failure logic
- deterministic engine hooks

## UI And Integration Test Priorities

- cold launch to home
- navigate to each MVP game detail screen
- tutorial visibility and persistence
- play, fail, result, retry, and return flow
- settings persistence across recreate
- process recreation on non-gameplay screens and paused gameplay where feasible

## Long-Run Stability Tests

- repeated rapid retry
- long simulated Lane Drift spawn sequences
- long Stack Drop engine sessions
- repeated save/load cycles
- low-memory and process-death recovery checks

## Performance Checks

- cold startup
- warm startup
- home to game transition
- repeated retry timing
- memory growth across long sessions
- compact-width layout verification

## Release-Stage Specific Tests

- offline first launch
- ad load failure
- purchase cancel / success / pending / restore
- entitlement cache behavior
- Android 15 edge-to-edge checks

## Evidence Rules

- Each stage report must include:
  - commands run
  - automated results
  - screenshots or device evidence where available
  - unresolved gaps
