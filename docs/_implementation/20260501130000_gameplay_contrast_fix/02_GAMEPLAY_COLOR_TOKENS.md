# Gameplay Color Tokens

**Date:** 2026-05-01

## Final Gameplay Palette
- **GameBackground:** #07111E (Deep navy blue)
- **GameBoard:** #0D1726 (Darker blue-gray)
- **GameBoardRaised:** #122039 (Slightly lighter blue-gray)
- **HudCard:** #132033 (Deep blue card)
- **HudBorder:** #2E4668 (Slate blue border)
- **TextPrimary:** #F8FAFC (Near-white)
- **TextSecondary:** #D7DEE9 (Light gray)
- **TextMuted:** #AAB4C2 (Muted gray)
- **PrimaryCyan:** #38E8FF (Electric cyan)
- **PrimaryOnCyan:** #06121D (Dark contrast)
- **AccentViolet:** #8B5CF6 (Vivid violet)
- **RewardAmber:** #FBBF24 (Golden amber)
- **DangerCoral:** #FF4D6D (Saturated coral)
- **PickupMint:** #22F59C (Vibrant mint)
- **StackCobalt:** #3B82F6 (Vivid cobalt)
- **ControlSurface:** #101B2D (Dark control background)
- **ControlBorder:** #3A4F73 (Control border)

## Added Tokens in `ArcadeExtendedColors`
- `gameBackground`
- `gameBoard`
- `gameBoardRaised`
- `hudCard`
- `hudBorder`
- `controlSurface`
- `controlBorder`
- `primaryCyan`
- `primaryOnCyan`
- `accentViolet`
- `dangerCoral`
- `pickupMint`

## Usage Strategy
- **`gameBackground`**: Applied to `GameplayScaffold` root background.
- **`gameBoard`**: Applied to the main `Box` containing the `Canvas` in game screens.
- **`gameBoardRaised`**: Used for lanes (Lane Drift) or elevated board elements.
- **`hudCard` / `hudBorder`**: Applied to `HudPill`.
- **`controlSurface` / `controlBorder`**: Applied to gameplay control buttons.
- **`primaryCyan` / `accentViolet`**: Applied to player/ring/active objects.
- **`dangerCoral` / `pickupMint`**: Applied to blockers and pickups.
