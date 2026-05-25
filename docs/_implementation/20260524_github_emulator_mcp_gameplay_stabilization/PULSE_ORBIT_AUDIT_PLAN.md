# Pulse Orbit Audit & Plan

**Date**: May 24, 2026

## Current Audit
- **Core Loop**: Orb rotates at `initialRotationSpeed` (96). Player taps when center of orb is within `gapSize` (initially 82).
- **Collision**: `angularDistance <= gapSize / 2f`.
- **Difficulty**:
    - Rotation Speed: 96 to 222 (+4.3/pass).
    - Gap Size: 82 to 38 (-1.15/pass).
    - Gap Jump: 74 to 136 (+3.6/pass).
- **Visuals**: Basic canvas drawing. success/fail pulses exist.

## Identified Issues
1. **Hitbox Fairness**: The collision check requires the *center* of the orb to be in the gap. If the orb is moving fast, a player might tap when the orb *looks* like it's in the gap, but the center hasn't entered yet or has already left. 
2. **First Impression**: The initial speed (96) is okay, but could be slightly slower (e.g., 85) to feel even more "hero" and accessible.
3. **Combo Feedback**: The "Perfect timing" badge is good, but could be more impactful (e.g., color changes or size pulses).

## Improvement Plan
1. **Tolerance Buffer**: Add a ~3-5 degree grace threshold to the collision detection to make it feel "fair" even at higher speeds.
2. **Slower Start**: Reduce `initialRotationSpeed` from 96 to 85.
3. **Better Feedback**:
    - Add "Perfect!" text overlay near the orb when a combo occurs.
    - Increase the `successPulse` impact.
4. **Onboarding**: Pulse the "Tap to start" text when idle.
5. **Unit Tests**: Add tests for the new tolerance logic.
