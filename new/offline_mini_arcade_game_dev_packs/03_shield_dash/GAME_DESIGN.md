# Shield Dash — Game Design

## Status
Recommended production replacement for Lane Drift

## Role in Offline Mini Arcade
Defensive reflex game and Lane Drift replacement candidate

## One-line concept
A central core is attacked by incoming hazards. The player rotates a shield around the core to block attacks. Perfect blocks and streaks build score and combo.

## Why this game belongs
- Adds a distinct play pattern to the arcade.
- Supports quick repeatable short sessions.
- Works offline and can run on low-end Android devices.
- Can connect into existing high score, sessions, coins, daily challenges, and cosmetic themes.
- Gives the app more variety without turning it into a bloated mini-game dump.

## Target player feeling
Defensive, tense, satisfying, unique, readable, one-thumb friendly.

## Core loop
- Core stays at center.
- Shield rotates around core.
- Projectiles/hazards approach from screen edges or circular spawn ring.
- Player rotates shield to face incoming hazard.
- Blocked hazards award score.
- Perfect block awards combo/bonus.
- Missed hazard hits core and ends run or removes one life depending MVP decision.
- Difficulty increases through speed, spawn angle variety, and pattern density.

## Controls
- Primary: drag around the core to set shield angle.
- Secondary fallback: tap left/right side to rotate shield incrementally.
- Optional accessibility: sensitivity slider later, not first sprint unless already supported.

## MVP scope
- One playable endless mode.
- Local high score.
- Session count.
- Pause and restart.
- Result screen.
- Game info/how-to-play screen.
- Daily challenge progress hooks.
- Soft currency reward hooks.
- Theme-compatible visuals.

## Out of scope for first sprint
- Online leaderboard.
- Account login.
- Heavy animations.
- Complex upgrade systems.
- Excessive power-up variety.
- Ads during active play.

## Must not do
- Do not make it another Pulse Orbit timing-gap game.
- Do not require exact pixel-perfect angle matching.
- Do not flood the screen with too many hazards early.
- Do not use effects that make incoming direction hard to read.
