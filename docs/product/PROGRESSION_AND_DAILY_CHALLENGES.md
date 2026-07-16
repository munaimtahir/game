# Progression And Daily Challenges

## Shared Progression

- One profile across all three games
- One coin balance
- One streak
- One theme inventory
- One cosmetic skin inventory
- One milestone system

## Run Rewards

- Each game awards coins on run completion using its game-specific formula.
- Reward formulas are intentionally modest to keep challenge and milestone rewards meaningful.

## Theme Unlock Pacing

- Default theme is free.
- Two free-track unlockable themes ship in MVP.
- Premium may add a small number of premium-only cosmetic variants.
- The catalogue remains intentionally small for MVP clarity.

## Currency Sinks

- shared shell themes
- one or two cosmetic variants per game

## Grind Safeguards

- No mechanic requires marathon daily play.
- No “use it today or lose it” reward that removes earned inventory.
- Challenge rewards supplement run rewards; they do not gate core progression.

## Daily Challenges

Each local day includes:

- one Pulse Orbit challenge
- one Lane Drift challenge
- one Stack Drop challenge
- one arcade bundle challenge

## Deterministic Offline Generation

- Challenge generation is deterministic from:
  - local `dayKey`
  - game id
  - challenge tier inputs
- The generated challenge instance is persisted once activated for the day.

## Difficulty Tiers

- Daily difficulty tier rotates deterministically:
  - Calm
  - Standard
  - Sharp
- Tiers change target values and reward ranges only, not gameplay rules.

## Challenge Templates

- Pulse Orbit:
  - reach score target
- Lane Drift:
  - collect pickup target
- Stack Drop:
  - clear line target
- Bundle:
  - complete `2` of the `3` game challenges

## Completion Validation

- Completion is validated from committed run results only.
- Partial progress can span multiple runs within the same day.
- Bundle challenge progress derives from game-challenge completion state, not independent run metrics.

## Claim State And Duplicate Prevention

- Challenge reward claim is automatic on first completion write.
- Reward writes use idempotency keys and ledger records.
- Reopening the app must never duplicate challenge rewards.

## Challenge History Retention

- Keep the most recent `30` day records for:
  - generated targets
  - completion state
  - reward claim state
- Older records may be compacted but not in a way that permits duplicate rewards.

## Replacement Rules

- Once a day’s challenge set is activated, it is fixed for that day.
- Offline clock rollback does not replace the active set.
- A new set appears only when a later valid local day becomes active.

## Reward Ranges

- Calm: modest reward
- Standard: medium reward
- Sharp: slightly higher reward
- Bundle: always higher than a single game challenge

## Premium And Free Boundaries

- Premium may unlock cosmetic inventory but not challenge advantages.
- Free users can complete all daily gameplay and claim all normal rewards.

## Balance Review Rule

- Stage 3 includes simulation-based validation of average reward rates, unlock pacing, and exploit resistance.
