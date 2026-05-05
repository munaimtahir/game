# Game-Feel and UI Upgrade Plan
**Date**: May 2026

## A. Home Screen / Arcade Shell
- **Goal**: Transition from a basic utility dashboard to a vibrant, premium arcade launcher.
- **Actions**:
  - Enhance the hero area with dynamic background gradients or subtle animations (e.g., slow panning or glowing elements) to establish a distinct mood without heavy layout calculations.
  - Make daily missions and streak/coin status visually pop with more pronounced iconography and glowing highlights.
  - Simplify layout spacing and leverage stronger drop shadows for game cards to simulate depth and interactivity.

## B. Game Cards
- **Goal**: Establish a strong visual identity for each game card that hints at the gameplay.
- **Actions**:
  - **Pulse Orbit**: Add neon reflex themes, circular glowing accents, and high-contrast primary text on the card.
  - **Lane Drift**: Implement slanted speed lines or motion trails in the card background, alongside glowing shard elements.
  - **Stack Drop**: Utilize blocky, structured background patterns with a puzzle-like glow or grid aesthetic.

## C. Reward Feedback
- **Goal**: Inject dopamine-inducing moments during key achievements without intrusive popups.
- **Actions**:
  - Implement a subtle but satisfying scale-up or glowing pulse animation on the UI when hitting a new high score.
  - Add explicit checkmark or burst animations to the daily challenge bar upon completion.
  - Use brief "coin earned" visual floating elements or stat increments for coin rewards, combo milestones, and level-ups.

## D. Per-Game Polish
- **Goal**: Heighten moment-to-moment gameplay satisfaction.
- **Actions**:
  - **Pulse Orbit**: Add a satisfying ring burst on perfect combos and a strong visual flash/pulse on success. Introduce clearer red/danger visual feedback on failures.
  - **Lane Drift**: Implement lane motion lines to increase the perception of speed. Add shard sparkle animations on pickup and near-miss highlight indicators.
  - **Stack Drop**: Introduce a white/cyan horizontal line-clear flash. Add a subtle danger glow at the top when the stack gets too high.

## E. Sound/Haptic Opportunities
- **Goal**: Provide tactile feedback while respecting settings.
- **Actions**:
  - Integrate light haptics for meaningful events (e.g., line clears, combos, completing challenges).
  - Ensure all new haptics and visual flashes respect the `SettingsState` (e.g., `vibrationEnabled`, `reducedEffects`).

## F. Performance Guardrails
- **Goal**: Maintain 60fps+ on low-end offline devices.
- **Actions**:
  - Use Jetpack Compose `Canvas` and `Animatable` state for lightweight micro-interactions instead of expensive Lottie files.
  - Avoid infinite layout jank by isolating animations to non-layout-impacting modifiers (e.g., `graphicsLayer` `scale`, `alpha`, `rotation`).
  - Limit particle lifespans and counts to stay well within CPU budgets.