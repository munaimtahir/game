# Gravity Flip — UI / UX Spec

## Visual direction
- Light, clean, rounded, friendly arcade style.
- Use existing app theme tokens where possible.
- High contrast between player, hazards, pickups, and background.
- Avoid dark muddy gameplay unless app theme requires it.
- Avoid excessive glow/noise that makes playfield unclear.
- Keep text large enough for low-end/small Android screens.

## Required screens/states
1. Game info screen
2. Ready state
3. Active play
4. Pause state
5. Game over/result state

## Game info screen content
- Game title: Gravity Flip
- One short description.
- Best score card.
- Sessions card.
- How-to-play card.
- Play button.
- Optional: daily challenge card if existing system supports it.

## Gameplay HUD
- Back button or exit affordance.
- Score card.
- One game-specific stat card.
- Pause button.
- Keep HUD outside critical play area when possible.

## Feedback
- Small haptic feedback on successful important action if vibration enabled.
- Distinct feedback for score, combo, and failure.
- Keep sound optional and respect global sound/music/vibration settings.
- Avoid long animations before retry.

## Accessibility basics
- Readable contrast.
- Touch targets large enough for mobile.
- No essential information conveyed by color only.
- Pause available where appropriate.
- Gameplay should remain understandable without audio.
