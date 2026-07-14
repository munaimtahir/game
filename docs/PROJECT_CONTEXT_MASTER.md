# Offline Mini Arcade — Master Project Context

## Project identity

Offline Mini Arcade is a **standalone Android casual game app** built as a **small, dependable, offline-first bundle of quick micro-games** for short, repeatable breaks.

The product promise is:

- open the app and start playing within seconds
- work fully offline
- run well on low-end Android phones
- avoid accounts, waiting, heavy downloads, and aggressive monetization
- provide short but satisfying sessions that encourage replay

This project is **not** meant to be a deep premium single game.  
It is meant to become a **durable keep-on-phone arcade app** that people revisit because it is fast, reliable, lightweight, and pleasant.

## Core product thesis

The app should succeed through:

- low friction
- instant entry
- strong replay loops
- local score chasing
- clean presentation
- offline trust
- small but polished variety

The product should feel like a **curated arcade**, not a random pile of mini-games.

## Strategic positioning

The app wins by packaging a few very approachable game loops into one trustworthy offline product.

It is best positioned for:
- students
- commuters
- casual users
- weak-device users
- weak-internet or no-internet situations
- short waiting-time entertainment

## Experience pillars

1. Instant entry with zero friction
2. Reliable offline play
3. Good performance on low-end devices
4. Short sessions that still feel rewarding
5. Clean, friendly visual identity
6. No casino-style overload
7. No aggressive monetization pressure

## MVP shape

The MVP is locked as:

- 3 mini-games
- 1 shared progression layer
- daily challenge system
- local stats
- minimal settings
- free download with restrained post-run advertising
- one-time lifetime ad-removal purchase
- subscription deferred until genuine recurring premium content exists

## Visual / tonal direction

- bright but not noisy
- rounded UI
- fast transitions
- clear score feedback
- friendly arcade tone
- lightweight and cheerful presentation
- every screen should feel simple and ready to play

## Commercial direction

Free version:
- all three games and core progression remain free
- limited interstitial ads only at natural post-run transitions
- optional rewarded ads for clearly disclosed bonus rewards
- no app-open ads or gameplay interruption ads
- no banner ads in the initial monetized release

Lifetime premium:
- one-time non-consumable purchase
- permanently removes forced ads
- may include premium themes or cosmetic content

Subscription:
- deferred from the first free release
- may be reconsidered only after a meaningful active-user base exists and the app can provide genuine recurring content

The product should avoid:
- subscription-first monetization
- pressure tactics
- fake “100 games / 1000 games” style marketing
- deceptive offline claims
- any monetization failure that blocks offline gameplay

There are currently no real users or purchasers, so no legacy-paid-user migration or grandfathering system is required.

## Locked MVP game slate

### 1) Pulse Orbit
Role: hero game  
Type: one-tap reflex score chaser

A small orb rotates around a core. The player taps at the right moment to burst through the opening in an outer ring. Clean passes increase score and combo. Mistiming causes collision and ends the run.

### 2) Lane Drift
Role: flowing arcade mode  
Type: endless dodge-and-collect survival

The player shifts left and right across lanes while moving forward automatically, avoiding blockers and collecting energy shards. A combo system should reward good pathing and risk-reward decisions.

### 3) Stack Drop
Role: classic mastery mode  
Type: falling block line-clear puzzle

Shaped blocks fall from the top. The player repositions and rotates them to complete horizontal lines. Completed lines clear. Difficulty rises over time through faster block descent and increasingly punishing board management.

## Why these 3 were chosen

This trio gives strong audience coverage:

- Pulse Orbit = instant reflex precision
- Lane Drift = flowing motion survival
- Stack Drop = strategic mastery / nostalgia familiarity

This creates variety without making the app feel unfocused.

## Shared progression layer

This is mandatory for product cohesion.

Shared systems:
- one soft currency (example: Arcade Coins)
- daily challenge hub
- global play streak
- local stats page
- theme / cosmetic unlock system
- per-game high scores and milestones

Without this layer, the app becomes 3 disconnected games.
With this layer, it feels like one coherent arcade product.

## Daily challenge philosophy

Daily challenges should exist from MVP.

Structure:
- one challenge per game per day
- one extra bundle challenge for completing any 2 or all 3
- fully offline-capable seeded or rule-based challenge generation
- no internet dependency required for daily use

## High-level monetization build order

The existing game is now moving to a free monetization model through three AI-led stages:

1. Repository discovery and implementation contract
2. Complete monetization implementation: AdMob, consent, billing, entitlement, UI, analytics and tests
3. Release hardening, closed-test evidence and Play Console handoff

Human work is limited to unavoidable external-console configuration, production identifiers and policy declarations.

## Non-negotiables

- core experience must work fully offline
- game startup and restart must feel fast
- weak phones must be treated as the baseline target
- UI should remain clean and readable
- the app must not feel like a cheap cloned bundle
- each game must be polished enough to justify its place

## Open items for later discussion

These are not yet locked:
- final game names
- art style specifics
- music and sound identity
- exact scoring formulas
- exact daily challenge rules
- premium price point
- future fourth game
