# Next Chat Bootstrap — Offline Mini Arcade

Use this file when starting a new chat to restore project context.

## Project summary
We are building **Offline Mini Arcade**, a lightweight Android offline-first mini-game bundle for quick repeatable short-break play.

## Locked MVP structure
- 3 games only
- shared progression
- daily challenges
- local stats
- minimal settings
- free download with restrained ads
- one-time lifetime ad-removal purchase
- no subscription in the first free release

## Locked games
1. Pulse Orbit — one-tap reflex hero game
2. Lane Drift — dodge-and-collect endless arcade game
3. Stack Drop — falling-block line-clear puzzle game

## Locked monetization
- all 3 games and core progression remain free
- no ads during active gameplay
- no app-open ads
- no banner ads initially
- no forced ads during the first 5 completed runs
- interstitial eligibility after every 3 completed runs
- minimum 120-second cooldown
- maximum 4 interstitials per day
- optional rewarded ads only for clearly disclosed bonus rewards
- one-time product `premium_lifetime` removes forced ads permanently
- entitlement states: `FREE` and `LIFETIME_AD_FREE`
- no legacy-user migration because there are currently no real users or purchasers
- subscription deferred until recurring premium content and a meaningful active-user base exist

## Design philosophy
- instant entry
- real offline play
- low-end Android friendly
- clean, bright, friendly UI
- no fake “100 games” positioning
- no aggressive monetization
- quality over quantity

## Development method
Use the minimum safe number of AI-led stages:
1. repository discovery and implementation contract
2. complete monetization implementation
3. release hardening and Play Console handoff

The coding agent owns analysis, implementation, testing, documentation and evidence capture. Human intervention is limited to unavoidable Play Console, AdMob, billing-account and legal-declaration actions.

## Instruction for future work
Treat all decisions above as finalized.
Do not reopen game selection, the free model, the initial ad cadence, legacy migration, or subscription timing unless explicitly requested.
Proceed using `BUILD_PLANNING_NEXT_STEPS.md` as the implementation sequence.
