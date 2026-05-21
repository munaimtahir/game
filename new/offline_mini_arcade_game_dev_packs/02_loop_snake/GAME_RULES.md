# Loop Snake — Game Rules

## Game start
- Start from game detail screen or direct play button.
- Initialize score to 0.
- Initialize combo/streak according to game needs.
- Initialize difficulty level to beginner-safe baseline.
- Start in a ready state with a short instruction prompt.
- First input starts active play.

## Active play rules
- Snake moves continuously on a grid or soft-grid arena.
- Player changes direction with swipe or optional directional buttons.
- Collect regular orb to score and grow.
- Collect golden timed orb for bonus.
- Avoid arena edges, body collision, and later obstacles.
- Speed increases gradually.
- Game ends on collision.

## Player input rules
- Primary: swipe up/down/left/right.
- Optional accessibility: four simple direction buttons in settings or game overlay.
- Ignore direct reverse input if it would instantly collide with body.

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
