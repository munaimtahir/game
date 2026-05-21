# Brick Volley — Scoring and Difficulty

## Scoring model
- Brick hit: +1 point.
- Brick destroyed: +5 points.
- Full row cleared before descent: +10 bonus.
- Board cleanup bonus: +25 if no bricks remain after a volley.
- Combo: consecutive turns with at least 3 bricks destroyed gives +combo bonus.
- Coins: 1 coin per 50 score, capped per run if app has reward caps.

## Difficulty progression
- Start with 3-4 bricks per new row.
- Brick HP starts 1-3.
- Every 5 turns increase average HP by 1.
- Every 8 turns increase row density slightly.
- After score milestone, introduce guarded bricks only if performance remains stable.
- Danger line should be visually clear and fair.

## Daily challenge candidates
- Clear 40 bricks.
- Survive 12 turns.
- Destroy 5 bricks in one volley.
- Score 250 points.
- Clear the board once.

## Reward hooks
- Award soft currency based on score milestones.
- Award bonus currency for daily challenge completion.
- Award no currency during failed tutorial/zero-action sessions.
- Keep rewards modest to avoid inflation.
- Do not require internet to validate daily challenges.

## Balancing targets
- First-time player should survive at least 20-40 seconds after understanding controls.
- Early failure should feel like "I can do better", not "this is unfair".
- Difficulty should increase gradually.
- Score curve should support short runs and high-score chasing.
- Daily challenges should be possible in 2-5 attempts for average users.

## Telemetry/local stats to store
- Best score.
- Total sessions.
- Total play time.
- Total runs completed.
- Game-specific secondary stat.
- Daily challenge progress.
- Last played timestamp.
