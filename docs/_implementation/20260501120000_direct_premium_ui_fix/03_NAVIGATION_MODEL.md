# Navigation Model

## New Route Structure
- `Home`
- `PulseOrbitDetail` -> `PulseOrbitGame`
- `LaneDriftDetail` -> `LaneDriftGame`
- `StackDropDetail` -> `StackDropGame`
- `Challenges`, `Stats`, `Settings` remain global meta routes.

## Flow: Home -> Detail -> Gameplay
1. **Home Screen**: Acts as the launcher. Tapping "Play" on a game card navigates to that game's *Detail* screen.
2. **Game Detail Screen**: Shows game title, premium hero/art, best stats, and a short "How to Play". A prominent "Start" button navigates to the *Gameplay* screen.
3. **Gameplay Screen**: Minimal HUD, large playfield, controls. No descriptive text or cards.

## Back Behavior
- **Gameplay (playing):** Pressing Back pauses the game.
- **Gameplay (paused/game over):** Pressing Back returns to the *Detail* screen (or Home, if following Android conventions, but we will return to Home to match previous fix). *Wait, prompt says "A gameplay Back button should return to the game detail screen unless current app convention clearly routes home."* Let's route to the Detail screen to support the new flow better.
- **Detail Screen:** Pressing Back returns to Home.

## Content Separation
- **Detail Screens:** Contain all marketing text, "How to play", best score cards, and the main entry point to start.
- **Gameplay Screens:** Only contain score, pause button, and the game board/controls. No scrolling detail cards.
