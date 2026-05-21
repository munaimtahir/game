# Gravity Flip — Scoring and Difficulty

## Scoring model
- Distance: +1 per unit or per second.
- Star pickup: +10.
- Clean section bonus: +25.
- Near-miss bonus optional only if collision logic is reliable.
- Combo for consecutive star pickups without collision.

## Difficulty progression
- Start with wide safe paths.
- Introduce floor/ceiling alternation slowly.
- Add obstacle groups after 20 seconds.
- Increase scroll speed gradually.
- Use generated chunks with guaranteed safe route.
- Never spawn impossible transitions.

## Daily challenge candidates
- Reach 300 distance.
- Collect 25 stars.
- Complete 5 clean sections.
- Survive 60 seconds.
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
