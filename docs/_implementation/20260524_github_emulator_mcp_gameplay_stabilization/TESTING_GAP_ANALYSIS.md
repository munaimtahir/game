# Testing Gap Analysis

**Date**: May 24, 2026

## Current State
- UI tests exist but mainly cover screen presence (`completeadbtest.yml` does raw adb tapping).
- Lint fails due to newly introduced API requirements (now patched).
- Unit tests are minimal or non-existent for the core gameplay math (e.g., collision, difficulty formulas).

## Gaps
1. **Game Logic Unit Tests**: `LaneDriftCollision.kt` needs tests for collision boundaries and overlap formulas.
2. **Visual Snapshot Tests**: No automated visual regression tests exist.
3. **Gameplay Determinism**: There's no debug mode with a fixed seed for reliable reproduction.
