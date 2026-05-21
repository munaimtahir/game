# Shield Dash — Game Rules

## Game start
- Start from game detail screen or direct play button.
- Initialize score to 0.
- Initialize combo/streak according to game needs.
- Initialize difficulty level to beginner-safe baseline.
- Start in a ready state with a short instruction prompt.
- First input starts active play.

## Active play rules
- Core stays at center.
- Shield rotates around core.
- Projectiles/hazards approach from screen edges or circular spawn ring.
- Player rotates shield to face incoming hazard.
- Blocked hazards award score.
- Perfect block awards combo/bonus.
- Missed hazard hits core and ends run or removes one life depending MVP decision.
- Difficulty increases through speed, spawn angle variety, and pattern density.

## Player input rules
- Primary: drag around the core to set shield angle.
- Secondary fallback: tap left/right side to rotate shield incrementally.
- Optional accessibility: sensitivity slider later, not first sprint unless already supported.

## Failure rules
- Failure must feel fair and explainable.
- Collision/timing tolerance should be forgiving enough for mobile touch.
- Failure immediately transitions to result state.
- Result state must show score, best score, key secondary stat, and restart action.

## Pause rules
- Pause freezes gameplay simulation.
- Pause screen should show resume, restart, and exit/back.
- No progress should be counted while paused.

## Restart rules
- Restart must reset active game state only.
- Restart must not clear high score, sessions, coins, or daily challenge state.
- Restart should be available within one tap from game over.

## Fairness rules
- No impossible opening pattern.
- No invisible collision boxes.
- No sudden difficulty spike before player understands loop.
- No random generation that creates unavoidable failure.
