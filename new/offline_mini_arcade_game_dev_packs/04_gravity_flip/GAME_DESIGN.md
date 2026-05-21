# Gravity Flip — Game Design

## Status
Prototype only until compared with Shield Dash

## Role in Offline Mini Arcade
Movement survival prototype and alternative Lane Drift replacement

## One-line concept
A small runner moves through a side-scrolling tunnel. The player flips gravity or uses hold/release gravity control to avoid hazards and collect stars.

## Why this game belongs
- Adds a distinct play pattern to the arcade.
- Supports quick repeatable short sessions.
- Works offline and can run on low-end Android devices.
- Can connect into existing high score, sessions, coins, daily challenges, and cosmetic themes.
- Gives the app more variety without turning it into a bloated mini-game dump.

## Target player feeling
Movement flow, path reading, environmental navigation, not circular timing.

## Core loop
- Character moves forward automatically.
- Player changes gravity to move between floor and ceiling or between upward/downward pull.
- Avoid spikes, blocks, gates, lasers, and gaps.
- Collect stars/energy along the path.
- Distance increases score.
- Speed and obstacle density increase over time.
- Collision ends the run.

## Controls
- Preferred anti-overlap control: hold to rise / release to fall, or hold to invert gravity and release to normalize.
- Alternative: tap to flip gravity, but only if it does not feel too close to Pulse Orbit.
- Input must be forgiving with short grace periods.

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
- Do not make the core mechanic 'tap at exact timing window' like Pulse Orbit.
- Do not use circular gap visuals.
- Do not make early obstacle spacing too hard.
- Do not let collision boxes feel unfair.
