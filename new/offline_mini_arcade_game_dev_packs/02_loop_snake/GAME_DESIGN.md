# Loop Snake — Game Design

## Status
Strong production addition

## Role in Offline Mini Arcade
Classic arcade survival game

## One-line concept
A modern Snake-style game with a clean rounded arena. The snake collects orbs, grows, builds combo, avoids walls/body/obstacles, and chases high score.

## Why this game belongs
- Adds a distinct play pattern to the arcade.
- Supports quick repeatable short sessions.
- Works offline and can run on low-end Android devices.
- Can connect into existing high score, sessions, coins, daily challenges, and cosmetic themes.
- Gives the app more variety without turning it into a bloated mini-game dump.

## Target player feeling
Classic, familiar, polished, smooth, short-session survival.

## Core loop
- Snake moves continuously on a grid or soft-grid arena.
- Player changes direction with swipe or optional directional buttons.
- Collect regular orb to score and grow.
- Collect golden timed orb for bonus.
- Avoid arena edges, body collision, and later obstacles.
- Speed increases gradually.
- Game ends on collision.

## Controls
- Primary: swipe up/down/left/right.
- Optional accessibility: four simple direction buttons in settings or game overlay.
- Ignore direct reverse input if it would instantly collide with body.

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
- Do not make it look like a plain black old-phone Snake clone.
- Do not make controls feel delayed.
- Do not spawn food inside snake body or unreachable cells.
- Do not add complex mission UI into gameplay screen.
