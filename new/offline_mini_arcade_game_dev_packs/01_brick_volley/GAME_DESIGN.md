# Brick Volley — Game Design

## Status
High-confidence production addition

## Role in Offline Mini Arcade
Aim + physics + block-clearing game

## One-line concept
The player drags to aim and releases a volley of balls upward. Balls bounce, damage numbered bricks, and return. After each turn, bricks descend. The run ends when bricks reach the danger line.

## Why this game belongs
- Adds a distinct play pattern to the arcade.
- Supports quick repeatable short sessions.
- Works offline and can run on low-end Android devices.
- Can connect into existing high score, sessions, coins, daily challenges, and cosmetic themes.
- Gives the app more variety without turning it into a bloated mini-game dump.

## Target player feeling
Satisfying ricochet clearing, clean physics, high readability, one-more-shot tension.

## Core loop
- Player aims from bottom launcher.
- Player releases a volley of balls.
- Balls bounce off walls and bricks.
- Each brick has HP shown as a number.
- Each hit reduces brick HP by 1.
- Destroyed bricks award score and possible coins.
- After all balls return or expire, the brick field advances downward.
- New brick row spawns at the top.
- Game ends if any brick reaches the bottom danger zone.

## Controls
- Drag from launcher to aim.
- Show clear aiming guide line.
- Release to fire.
- Cancel shot if finger returns close to launcher before release.

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
- Do not create excessive particles that hurt low-end devices.
- Do not add complex power-ups in first sprint.
- Do not make the board visually noisy.
- Do not require internet or server-generated levels.
