# Theme ID Fix and Light Refresh Report

**Date:** 2026-05-01
**Status:** COMPLETE

## Root Cause Analysis
The previous theme implementation failed to visibly change themes because the `themeId` parameter in `OfflineMiniArcadeTheme` was not wired into the actual color selection logic. Kotlin correctly reported this with an `unused parameter` warning. Additionally, MaterialTheme and ArcadeTheme extended colors were partially misaligned.

## Implementation Details
1.  **Wired `themeId`**: Updated `OfflineMiniArcadeTheme` to pass `themeId` to `getThemeColorScheme` and `getExtendedColors`.
2.  **Explicit Selection Logic**: Implemented `when(themeId)` blocks to handle "default", "sunset_shift", and "ice_grid", all currently mapping to the high-contrast light foundation per user request.
3.  **Light Theme Refresh**: Aligned the palette with the requested "Soft Arcade Light" baseline:
    -   Background: #F8FAF7
    -   Surface: #FFFFFF
    -   Primary: #236A76
    -   Text: #17252B (Slate 900)
4.  **System Bar Fix**: Added a `SideEffect` in the theme to force light status and navigation bars with dark icons using `WindowCompat`.
5.  **HUD and Overlays**: Updated `GameplayScaffold` and `HudPill` to use theme-aware tokens (`gameBackground`, `hudCard`, `overlayScrim`). Overlays now use a lighter scrim (alpha 0.35) suitable for light mode.
6.  **Dependency Added**: Added `androidx.core:core-ktx` to `core:ui` module to support `WindowCompat` and `WindowInsetsControllerCompat`.

## Verification Results
-   **Kotlin Warning**: The `Parameter 'themeId' is never used` warning is **gone** from `core:ui` compilation.
-   **Build Status**: `BUILD SUCCESSFUL` confirmed with clean re-run.
-   **Default Theme**: "default" correctly maps to Soft Arcade Light.
-   **Persistence**: Confirmed `selectedThemeId` defaults to "default" in `Models.kt`.
-   **Hardcoded Colors**: Cleaned up remaining dark-theme hardcodings in shared components.

## Remaining Warnings
-   Unused parameters `settings` in some `*Screen.kt` files persist but are outside the scope of the theme engine wiring fix.
