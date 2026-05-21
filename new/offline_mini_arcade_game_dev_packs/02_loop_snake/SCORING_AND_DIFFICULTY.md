# Loop Snake — Scoring and Difficulty

## Scoring model
- Regular orb: +10.
- Golden orb: +50.
- Combo: collecting next orb within target time adds +5, +10, +15 scaling bonus.
- Survival bonus: +1 per second.
- Obstacle near-miss bonus optional: +2, only if easy to implement safely.
- Coins: milestone-based, not every orb if inflation is a concern.

## Difficulty progression
- Start slow and readable.
- Increase speed every 5 orbs.
- Spawn first static obstacle after 12 orbs.
- Increase obstacle count slowly.
- Golden orb appears every 5-7 regular orbs and expires after 3-4 seconds.
- Do not make early game punishing.

## Daily challenge candidates
- Collect 20 orbs.
- Reach length 15.
- Collect 3 golden orbs.
- Survive 60 seconds.
- Score 300 without wall hit.

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
