# Device Test Results: Full-Screen Game Navigation

## Status
**PENDING - REQUIRED BEFORE RELEASE**

## Test Environment
- **Device:** [To be filled: e.g. Pixel 7]
- **Android Version:** [To be filled: e.g. 14]
- **Navigation Mode:** [To be filled: Gesture / 3-Button]

## Manual Checklist

### 1. Layout & Clipping
- [ ] **Pulse Orbit:** HUD is fully visible at top. Canvas feels centered. Tap anywhere works.
- [ ] **Lane Drift:** HUD is fully visible. 3 lanes fill the screen. No overlap with bottom gesture bar.
- [ ] **Stack Drop:** Grid is square and fully visible. Control buttons (if any) or gesture hints are not clipped.

### 2. Navigation & Back Behavior
- [ ] **Ready Screen:** Pressing back returns to Home.
- [ ] **Active Gameplay:** Pressing back displays "Run paused" overlay. Game loop stops.
- [ ] **Paused State:** Pressing back returns to Home.
- [ ] **Game Over:** Pressing back returns to Home.

### 3. Lifecycle
- [ ] **Backgrounding:** While playing, press Home button. Return to app. Game must be in "Paused" state.

### 4. Regression
- [ ] **Scoring:** Complete a run. Verify score is recorded and coins are awarded.
- [ ] **Retry:** Tap "Retry" on Game Over. Verify instant restart works without UI flicker.

## Evidence Required
- [ ] Screenshot: Home Screen
- [ ] Screenshot: Pulse Orbit (Active)
- [ ] Screenshot: Lane Drift (Active)
- [ ] Screenshot: Stack Drop (Active)
- [ ] Screenshot: Pause Overlay
- [ ] Screenshot: Game Over Summary
