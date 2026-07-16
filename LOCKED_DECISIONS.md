# Locked Decisions

This is the authoritative product decision log for the repository. If another document conflicts with this file, this file wins.

## Product Shape

- The public MVP release is one coherent offline arcade app, not three separate apps.
- The public MVP contains exactly three playable games:
  - Pulse Orbit
  - Lane Drift
  - Stack Drop
- Legacy or experimental modules such as Brick Volley, Loop Snake, Shield Dash, and design-pack prototypes are not public MVP scope.

## Platform And Product Rules

- Offline-first is mandatory.
- Core gameplay must remain fully functional without internet access.
- No account system, cloud save, leaderboard dependency, or online-only challenge logic is allowed.
- The app must launch quickly and remain usable on lower-end Android phones.
- Active gameplay must remain clear, readable, and free from casino-like presentation.
- Ads must never interrupt active gameplay.
- Rewards must not use deceptive urgency or manipulative pressure.

## Monetization

- The app is free to install.
- Monetization consists of restrained ads plus one optional one-time premium purchase.
- Premium removes ads.
- Premium may include additional cosmetic themes or skins.
- Premium must not change gameplay balance, scoring, difficulty, or fairness.
- No subscription model is allowed.
- No purchase may grant score boosts, easier challenge completion, extra continues, better spawn logic, or similar advantages.

## Shared Progression

- All three games share one local profile, one currency balance, one streak, one theme inventory, one challenge system, and one statistics model.
- No game may own an isolated progression economy or save silo.
- Reward balancing may stay configurable, but the persistence and entitlement architecture is locked now.

## Challenge System

- There is one offline daily challenge per game per local day.
- There is one bundle challenge across the arcade per local day.
- Daily challenges must be deterministically generated offline.
- Challenge claims must be idempotent and protected against duplicate reward grants.

## Persistence

- Shared structured game data uses Room.
- Lightweight user settings use `SharedPreferences` in Stage 1 unless migration to DataStore is explicitly justified later.
- Destructive Room migration is not acceptable for the release path.
- Corrupt save recovery must preserve as much valid data as possible and fall back to safe defaults when necessary.

## Architecture

- Jetpack Compose remains the UI and gameplay rendering approach.
- Navigation remains a single-app Compose navigation shell.
- Shared abstractions must exist for audio/haptics, ads, billing, rewards, and statistics.
- Game-specific logic belongs in `game/*`.
- Shared UI belongs in `core/ui`.
- Shared persistence and progression belong in `core/data`.

## Per-Game Controls

- Pulse Orbit uses one-tap input only.
- Lane Drift uses a single swipe-based lane shift system only for MVP.
- Stack Drop uses an on-screen control layout for MVP release reliability on compact Android devices.

## Release Claims

- Store-facing text may claim:
  - three games;
  - offline core gameplay;
  - daily challenges;
  - local stats;
  - restrained ads in the free version;
  - optional one-time premium ad removal.
- Store-facing text must not claim:
  - no ads at all;
  - subscriptions;
  - more than three MVP games;
  - any internet requirement for core play.
