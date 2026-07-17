# Shared Systems Specification

## Save Data Model

Structured data lives in Room. Lightweight toggles live in `SharedPreferences` for the MVP implementation path.

### Room Tables

- `player_profile`
  - singleton row keyed by `profileId = 0`
  - coins
  - selected theme id
  - selected cosmetic skin ids per game
  - premium entitlement cache fields
  - streak state
  - tutorial seen flags
  - aggregate challenge counters
  - lifecycle recovery markers
- `game_stats`
  - one row per `GameId`
  - high score
  - sessions played
  - total play time
  - total score
  - game-specific counters
- `theme_unlocks`
- `skin_unlocks`
- `challenge_instances`
  - one row per generated challenge instance
  - day key
  - seed inputs
  - difficulty tier
  - target values
  - progress
  - completion state
  - claim state
- `reward_ledger`
  - idempotency key
  - source type
  - source id
  - coin delta
  - unlock delta
  - created time
- `daily_activity`
  - day key
  - first session time
  - last session time
  - streak contribution status
- `migration_markers`
  - schema health version
  - repair version
  - last successful recovery snapshot metadata

### Settings Keys

- sound enabled
- music enabled
- vibration enabled
- reduced effects
- high contrast
- preferred text density if later added
- ad personalization consent state if legally required in Stage 4

## Schema Versioning And Migration

- Room schema version increments for every structured data change.
- Production database uses explicit migrations only.
- Destructive fallback is not allowed on the release path.
- Migration tests must verify:
  - previous-version profile loads
  - stats survive
  - challenge claim history survives
  - entitlement cache survives
- If an old save is unreadable after migration attempt, use selective recovery:
  - preserve intact tables;
  - reset only corrupt tables;
  - log recovery event locally if debug build;
  - never crash at startup because of save corruption.

## Corrupt Or Partial Save Recovery

- If `player_profile` is missing or invalid, recreate with safe defaults.
- If one game’s stats row is corrupt, rebuild only that game’s stats row.
- If challenge state is corrupt for one day, regenerate the challenge set for that day and preserve ledger-backed claimed rewards.
- If unlock tables are inconsistent with reward ledger, reward ledger wins.
- If premium cache is missing, default to free behavior until billing refresh succeeds in Stage 4.

## Statistics

### Per-Game Stats

- high score
- sessions played
- total play time
- total score
- best combo where applicable
- game-specific metric:
  - Pulse Orbit: total passes, perfect passes, best combo
  - Lane Drift: total pickups, near misses, best combo, total distance
  - Stack Drop: total lines, best single-run lines, highest level reached

### Global Stats

- total sessions across all games
- total play time
- total coins earned
- total coins spent
- total daily challenges completed
- current streak days
- best streak days

## Session Counting

- Every completed run counts as one session.
- A started run that is abandoned before first playable input does not count.
- A run interrupted by process death counts only if a result is committed.

## High Score Handling

- High score updates only on committed run completion.
- High score ties keep the earliest achieved score timestamp.
- High score writes happen in the same transaction as stats and reward writes.

## Soft Currency

- Currency name in UI: `Coins`
- Earn sources:
  - completed runs
  - daily challenges
  - milestones
- Spend sources:
  - cosmetic themes
  - cosmetic skins
- No spend may affect game outcome fairness.
- All reward and spend events write to `reward_ledger`.

## Duplicate Reward Prevention

- Every rewardable event gets an idempotency key:
  - run result key
  - challenge claim key
  - milestone key
  - purchase sync key
- Ledger insertion is atomic with balance change.
- If the same key is seen again, no duplicate reward is granted.

## Streak Rules

- Streak increments on the first completed run of a new local day.
- Multiple sessions on the same day do not increase streak beyond one day.
- Missing one or more days resets only the current streak count.
- Purchased or earned items are never removed because of a missed streak day.

## Daily Rollover And Offline Date Handling

- The canonical progression day uses device local date from the system time zone.
- Daily challenge generation uses a `dayKey` of `YYYY-MM-DD` plus the active zone id.
- The app snapshots the active challenge day when first opened for that local day.
- Rollover occurs when the app next resumes after local date has changed.
- If the clock moves backward within the same previously seen day window, the current active challenge set remains active and is not regenerated.

## Clock Manipulation Safeguards

- Never punish legitimate users with hard lockouts.
- Safeguards:
  - retain the most recently activated day key;
  - do not grant the same day’s rewards twice;
  - do not instantly regenerate a new day set solely because the clock moved backward;
  - if time jumps forward multiple days, generate only the current local day on next resume;
  - keep challenge history so duplicate old-day claims cannot be repeated.
- The system may temporarily defer streak advancement when the clock oscillates across days until a completed run resolves the current day state.

## Theme Inventory And Unlock Rules

- One default arcade theme is free and always available.
- Additional themes are cosmetic unlocks only.
- Premium-only cosmetics must be clearly labeled.
- All themes must pass contrast checks across shell and gameplay UI.

## Settings

- sound
- music
- vibration
- reduced effects
- high contrast

## Audio, Music, And Vibration Behavior

- Sound and vibration apply only when enabled.
- Music defaults on, but must pause on app background and on audio-focus loss.
- Haptics use short, readable cues:
  - success
  - failure
  - line clear
  - pickup / perfect timing

## Pause, Resume, Lifecycle, And Process Death

- On background or `ON_PAUSE`, active runs enter paused state.
- Result state is never auto-skipped on resume.
- If process death occurs mid-run, the game restores into paused resumable state only if the game-specific snapshot is complete and valid.
- If resumable state is incomplete or corrupt, the game returns to ready state without awarding a result.

## Premium Entitlement Model

- Fields:
  - entitlement state: unknown, free, premium
  - last verified time
  - source: local cache or billing sync
  - pending purchase flag
- Free behavior is the safe default if verification is missing.

## Ad Eligibility State Model

- Fields:
  - premium active
  - online capability
  - onboarding active
  - active gameplay flag
  - completed sessions since last ad
  - elapsed time since last ad
  - capped-for-day flag if introduced later
- Ads can be requested only when all gating conditions pass in the centralized policy layer.
