# Device Test Plan (Mandatory Release Gate)

**DEVICE TESTING IS REQUIRED BEFORE PLAY STORE UPLOAD.**

## Status: PENDING

### Target Devices
- [ ] Compact Android Phone (e.g. Pixel 4a size)
- [ ] Tall Android Phone (e.g. Pixel 8 Pro / S24 Ultra)

### Manual Checklist

#### 1. Visual Layout
- [ ] **HUD Alignment:** Score/Pause elements at the top do not overlap the status bar or camera notch.
- [ ] **Game Board Clipping:** Game area is fully visible and not clipped by the navigation bar or rounded corners.
- [ ] **Stack Drop Board:** Board remains a perfect square grid regardless of screen aspect ratio.

#### 2. Navigation & Gestures
- [ ] **Gesture Back:** Swiping from the edge during active play triggers PAUSE, not exit.
- [ ] **3-Button Back:** Pressing physical back during active play triggers PAUSE.
- [ ] **Ready State Back:** Pressing back before starting (ready screen) returns to Arcade Hub.
- [ ] **Game Over Back:** Pressing back from results returns to Arcade Hub.

#### 3. App Lifecycle
- [ ] **Backgrounding:** Minimizing the app during a high-speed run and reopening results in a PAUSED state.
- [ ] **Resume:** Resuming from pause does not cause visual glitch or frame drops.

#### 4. Game Integrity
- [ ] **Pulse Orbit:** Tap sensitivity is consistent across the full screen.
- [ ] **Lane Drift:** Swipes are responsive and don't conflict with system back gestures.
- [ ] **Stack Drop:** Drag/Drop gestures feel precise.
