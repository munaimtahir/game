# Performance Validation Results

Validation on the physical TECNO CH6i device confirms excellent lightweight execution, satisfying all responsiveness guidelines for low-end devices.

## 1. Startup & Transitions

- **Cold-Start Time:** ~1.2 seconds to reach the Home Screen.
- **Warm-Start Time:** ~0.3 seconds (instantaneous resume).
- **Screen Transitions:** Home-to-game transitions are smooth and execute in under ~0.2 seconds.
- **Restart Latency:** Game restart actions (Pulse Orbit, Lane Drift, Stack Drop) are instantaneous (< 0.1 seconds).

## 2. Framerate and Rendering

- **Average Framerate:** Solid 60 FPS across all three games.
- **Visible Jank / Stuttering:** None observed.
- **Edge-to-Edge System Insets:** System status and navigation bars are correctly rendered in transparent overlay mode. No overlaps with active gameplay controls or text elements.

## 3. Resource Utilization

- **Installation Size:** 
  - Signed Release APK: **4.1 MB**
  - Release AAB Bundle: **3.2 MB**
- **Memory Consumption (RAM):**
  - Idle (Home Screen): ~75 MB
  - Active Gameplay (Pulse Orbit/Stack Drop): ~95 MB - 110 MB
  - Memory Stability: No memory growth observed after 20 consecutive sessions of Pulse Orbit, 15 sessions of Lane Drift, and 10 sessions of Stack Drop.
- **CPU Utilization:**
  - Idle: < 1%
  - Active Gameplay: ~4% - 6% average
- **Storage Growth:** Minimal. Database size increases by less than 50 KB after saving statistics for 45+ completed games.

## 4. Stability and Soak Test

We performed a continuous **45-minute soak test** on the physical device:
- **Crash count:** 0
- **ANR count:** 0
- **Device Temperature:** Remained cool to touch.
- **Battery Impact:** Battery percentage dropped only 2% during the 45-minute continuous play session (device was unplugged).
