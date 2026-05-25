# Arcade UX Research Summary

**Date**: May 24, 2026

## Extracted Principles
1. **Onboarding**: No tutorials; intuitive gameplay driven by simple visual cues and clear affordances.
2. **Retry Loop**: Instant restart (<0.5 seconds). No loading screens or interstitial ads on every death.
3. **Feedback**: Satisfying visual and audio feedback (e.g., slight screen shake, clean particle burst for pickups).
4. **Low-end Performance**: Simple vector/canvas graphics. Avoid heavy shaders or complex textures. Maintain consistent 60fps.
5. **Collision Readability**: Hitboxes should be slightly smaller than the visual representation to feel "fair". Players get frustrated if they perceive a near-miss as a collision.

## Proposed Visual Direction for Offline Mini Arcade
- **Light Theme Friendly**: Clean, bright colors. High contrast between the player object, obstacles, and pickups.
- **One-Thumb Gameplay**: Core mechanics should center around a single interaction type (tap, hold, or swipe) placed in the lower-middle half of the screen.

## Guardrails
- **No Monetization Creep**: Interstitial ads only during natural break points (e.g., after 3 deaths, never during play).
- **Offline First**: No requirements to connect to play core modes.
