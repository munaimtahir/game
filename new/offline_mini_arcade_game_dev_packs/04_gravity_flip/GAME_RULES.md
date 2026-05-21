# Gravity Flip — Game Rules

## Game start
- Start from game detail screen or direct play button.
- Initialize score to 0.
- Initialize combo/streak according to game needs.
- Initialize difficulty level to beginner-safe baseline.
- Start in a ready state with a short instruction prompt.
- First input starts active play.

## Active play rules
- Character moves forward automatically.
- Player changes gravity to move between floor and ceiling or between upward/downward pull.
- Avoid spikes, blocks, gates, lasers, and gaps.
- Collect stars/energy along the path.
- Distance increases score.
- Speed and obstacle density increase over time.
- Collision ends the run.

## Player input rules
- Preferred anti-overlap control: hold to rise / release to fall, or hold to invert gravity and release to normalize.
- Alternative: tap to flip gravity, but only if it does not feel too close to Pulse Orbit.
- Input must be forgiving with short grace periods.

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
