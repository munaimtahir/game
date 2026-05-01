# Device Test Plan: Full-Screen Game Navigation

## Verification Checklist

### 1. Device Types
- [ ] **Small Android Phone (< 360dp width, short screen):** Verify game boards fit without clipping top HUD or bottom interactions. Stack Drop board should use compact heights. Pulse Orbit and Lane Drift should use fallback compact board heights.
- [ ] **Tall Android Phone (e.g., 20:9 ratio):** Verify play area is centered/bottom-aligned cleanly and fills the available space naturally without awkward stretching.

### 2. Navigation System
- [ ] **Gesture Navigation:** Verify `WindowInsets.safeDrawing` prevents controls (like the Stack Drop start button or pause menu) from being covered by the bottom gesture bar. Verify swiping from edge triggers back navigation or is handled correctly.
- [ ] **3-Button Navigation:** Verify the navigation bar doesn't overlap the bottom of the game boards.

### 3. Core Flow
- [ ] **App Launch -> Home:** Verify Home screen displays game cards but no active gameplay.
- [ ] **Home -> Game:** Tap "Play" on Pulse Orbit. Verify immediate transition to full-screen view.
- [ ] **Restart Loop:** Play a game, intentionally fail, tap "Retry instantly". Verify game restarts smoothly without recreating the entire screen layout.
- [ ] **Game Over Flow:** Ensure final score, best score logic, and coins earned are processed cleanly and displayed in the summary overlay.

### 4. Back Button Behavior
- [ ] **On Ready Screen:** Pressing back should return to Home.
- [ ] **During Active Gameplay:** Pressing back should **pause** the game, NOT exit.
- [ ] **While Paused:** Pressing back should return to Home.
- [ ] **On Game Over:** Pressing back should return to Home.

### 5. App Lifecycle (Background/Resume)
- [ ] **Background During Play:** Start a game (e.g., Pulse Orbit). Put app in background (Home button). Bring app to foreground. Verify the game is now in the **Paused** state, preventing an unfair death.
- [ ] **Offline Mode:** Turn off Wi-Fi/cellular. Verify all transitions and gameplay loops function perfectly.

### 6. Specific Game Checks
- [ ] **Pulse Orbit:** Verify single-tap control works anywhere on the screen. Verify HUD sits cleanly at the top.
- [ ] **Lane Drift:** Verify the 3 lanes fill the lower portion of the screen, and touch/swipe zones are clear of the system gesture area.
- [ ] **Stack Drop:** Verify the square grid doesn't distort. Ensure the HUD adapts to compact/regular screen width.