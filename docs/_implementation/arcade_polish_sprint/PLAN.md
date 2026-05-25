# Arcade Polish & Meta Progression Sprint Plan

**Date:** May 24, 2026

## Overview
This sprint tackles three critical pillars of transforming the MVP into a "store-ready" premium feeling mobile application:
1. **Option 1: The "Arcade Feel" (Haptics & Audio)**
2. **Option 2: Progression & Customization (The Meta Layer)**
3. **Option 4: Final Asset Integration (Visual Finalization)**

---

## 1. Option 1: Arcade Feel (Haptics & Audio)
**Goal:** Reach the "premium" feel of high-end mobile arcade games through tactile and auditory feedback, following Android best practices.

### Implementation Tasks
- **Rich Haptics (VibrationEffect):**
  - Update `ArcadeFeedback.kt` (in `app` module) to use modern Android `VibrationEffect` primitives (e.g., `PRIMITIVE_CLICK`, `PRIMITIVE_THUD`) instead of legacy legacy `vibrate(long)` functions.
  - Sync haptics tightly with high-impact events like damage or explosions.
  - Differentiate feedback hierarchy: Subtle for UI taps, sharp/strong for core gameplay success/failure.
- **Audio Polish:**
  - Standardize ToneGenerator or implement `SoundPool` for ultra-low latency, crisp arcade sounds (short attack, 100-300ms tail).
  - Add layered "sparkle" or "thud" logic to `ArcadeFeedback.kt`.
- **Game Feel ("Juice"):**
  - Integrate minimal **Screen Shake** utilities.
  - Ensure "squash & stretch" logic or hit-flashes exist on active game components (like the core in Shield Dash).

---

## 2. Option 2: Progression & Customization (The Meta Layer)
**Goal:** Increase long-term retention via "horizontal" unlocking. 

### Implementation Tasks
- **Shop UI:**
  - Create a new `Feature: Shop` or integrate it into `HomeScreen` / `SettingsScreen`. 
  - Allow players to spend `coins` (already earned at the end of runs) to unlock new Color Palettes/Skins.
- **Player Profile Extension:**
  - Update `PlayerProfile` data model to store `unlockedSkins` and `equippedSkin`.
- **Game Integration:**
  - Ensure the games read the `equippedSkin` state to override default colors or shapes (e.g., changing the Pulse Orbit ring color or the Gravity Flip ship shape).

---

## 3. Option 4: Final Asset Integration
**Goal:** Replace basic canvas rectangles and circles with polished, custom vector paths or icons.

### Implementation Tasks
- Replace basic geometric draw calls in games (e.g., `GravityFlipScreen` ship, `ShieldDash` core) with styled `Path` drawings or Vector Drawables that match the "Offline Mini Arcade" identity.
- Add simple particle bursts (confetti or sparks) upon run success/failure or line clears (Stack Drop).
