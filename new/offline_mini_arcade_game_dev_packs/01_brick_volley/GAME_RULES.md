# Brick Volley — Game Rules

## Game start
- Start from game detail screen or direct play button.
- Initialize score to 0.
- Initialize combo/streak according to game needs.
- Initialize difficulty level to beginner-safe baseline.
- Start in a ready state with a short instruction prompt.
- First input starts active play.

## Active play rules
- Player aims from bottom launcher.
- Player releases a volley of balls.
- Balls bounce off walls and bricks.
- Each brick has HP shown as a number.
- Each hit reduces brick HP by 1.
- Destroyed bricks award score and possible coins.
- After all balls return or expire, the brick field advances downward.
- New brick row spawns at the top.
- Game ends if any brick reaches the bottom danger zone.

## Player input rules
- Drag from launcher to aim.
- Show clear aiming guide line.
- Release to fire.
- Cancel shot if finger returns close to launcher before release.

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
