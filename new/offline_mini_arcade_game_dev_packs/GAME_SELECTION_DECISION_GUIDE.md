# Game Selection Decision Guide

## Goal
Use this guide after running the separate dev packs/prototypes to decide which games should enter the final arcade set.

## Preferred final set
1. Pulse Orbit
2. Stack Drop
3. Shield Dash
4. Brick Volley
5. Loop Snake

## Why this set is strongest
- Pulse Orbit: timing/reflex hero game.
- Stack Drop: strategy/falling-block familiarity.
- Shield Dash: defensive reflex identity and best Lane Drift replacement.
- Brick Volley: aim/physics clearing loop.
- Loop Snake: classic arcade survival loop.

This avoids heavy overlap and gives five distinct play styles.

## Gravity Flip decision rule
Promote Gravity Flip only if:
- It feels like movement navigation, not tap-timing.
- It does not feel like Pulse Orbit in a different skin.
- Collision boxes are fair.
- Generated obstacles are readable and not frustrating.
- It performs well on low-end devices.

Reject or defer Gravity Flip if:
- It feels like another one-tap reflex timing game.
- It depends too much on exact timing windows.
- It visually or mechanically overlaps Pulse Orbit.
- It becomes too difficult too early.

## Lane Drift decision rule
Replace Lane Drift if:
- Shield Dash or Gravity Flip feels more memorable.
- Lane Drift remains visually generic.
- Collision still feels unfair.
- The lane game does not create a strong identity.

Keep Lane Drift only if:
- It is redesigned into a genuinely richer pickup/dodge game.
- Collision logic is fixed.
- It becomes visually distinctive.
- User testing shows better retention than Shield Dash/Gravity Flip.

## Production priority
1. Brick Volley — almost certainly add.
2. Loop Snake — almost certainly add.
3. Shield Dash — recommended replacement for Lane Drift.
4. Gravity Flip — prototype only.
