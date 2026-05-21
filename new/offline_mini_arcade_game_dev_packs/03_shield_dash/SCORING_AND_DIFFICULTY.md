# Shield Dash — Scoring and Difficulty

## Scoring model
- Basic block: +10.
- Perfect block: +20.
- Streak bonus every 5 blocks: +25.
- Combo multiplier after 5, 10, 20 successful blocks.
- Survival bonus: +1 per second.
- Coins: awarded by score milestones and daily completion.

## Difficulty progression
- First 10 hazards from cardinal/intercardinal directions at slow speed.
- After 10 blocks, speed rises gently.
- After 20 blocks, introduce paired hazards with safe timing gap.
- After 30 blocks, introduce feint warning but keep fairness.
- Never spawn impossible simultaneous hazards in MVP.
- Use telegraph markers before projectile movement.

## Daily challenge candidates
- Block 30 hazards.
- Get 10 perfect blocks.
- Reach a 15-block streak.
- Survive 45 seconds.
- Score 500.

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
