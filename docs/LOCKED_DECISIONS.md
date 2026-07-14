# Locked Decisions — Offline Mini Arcade

This file is the source of truth for currently finalized product decisions.

## Locked product identity
- Android casual game app
- standalone product
- offline-first
- lightweight
- designed for quick repeatable sessions
- aimed at low-friction short-break entertainment

## Locked product promise
The user should be able to:
- open the app fast
- play within seconds
- use it without internet
- enjoy it on low-end devices
- revisit it often for short satisfying sessions

## Locked MVP scope
The MVP contains:
- exactly 3 mini-games
- shared progression
- daily challenges
- local stats
- minimal settings
- free download with restrained advertising
- one-time lifetime ad-removal purchase

## Locked games
1. Pulse Orbit
2. Lane Drift
3. Stack Drop

## Locked game roles
### Pulse Orbit
- hero game
- first impression game
- one-thumb reflex score chaser

### Lane Drift
- movement-based arcade game
- “just one more run” energy
- dodge-and-collect survival

### Stack Drop
- familiarity / nostalgia anchor
- strategic speed-escalating puzzle mode
- falling-block line-clear gameplay

## Locked strategic stance
The product must NOT become:
- a fake “100 games” collection
- a noisy ad farm
- an account-based service
- a subscription-first product
- an internet-dependent live-ops game

## Locked progression philosophy
All games must feed into one shared meta layer:
- common currency
- common streak
- common challenge hub
- common cosmetic progression
- common stats identity

## Locked monetization model
### Free tier
- all 3 games remain available
- core progression, daily challenges and local stats remain available
- restrained interstitial ads are allowed only at natural post-run transitions
- optional rewarded ads may provide clearly disclosed bonus rewards
- no ad may interrupt active gameplay
- no energy limits, pay-to-retry gates, or locked core games

### Lifetime premium
- one-time non-consumable purchase
- working product ID: `premium_lifetime`
- permanently removes forced ads
- may include premium themes or cosmetic value
- optional rewarded ads may remain available only when deliberately requested

### Subscription
- not included in the first free release
- must not be implemented merely to remove ads
- may be reconsidered only after there is a meaningful active-user base and genuine recurring premium content

## Locked initial ad cadence
- no app-open ads
- no banner ads in the initial monetized release
- no forced ad during the first 5 completed runs
- interstitial eligibility after every 3 completed runs
- minimum 120-second interstitial cooldown
- maximum 4 interstitial impressions per user per day
- no interstitial after a very short accidental run
- no interstitial immediately after a rewarded ad
- ad-load failure must silently continue normal navigation

These are launch defaults. They may be tuned later only from real retention, ad-exit and revenue data.

## Locked migration decision
- the app currently has no real users or purchasers
- no legacy-user entitlement, grandfathering, or paid-user migration system will be built
- the entitlement model for the first free release contains only:
  - `FREE`
  - `LIFETIME_AD_FREE`

## Locked offline monetization behavior
- all core gameplay must work without internet
- no ad placeholder, loading screen or error may block gameplay
- failed ad requests must be ignored safely
- locally verified lifetime entitlement must remain usable offline
- purchase and restore actions may require Google Play connectivity, but failure must not affect free gameplay

## Locked development approach
Development will be performed in the minimum safe number of AI-led stages:
1. repository discovery and implementation contract
2. complete monetization implementation
3. release hardening and Play Console handoff

The AI coding agent should perform repository analysis, coding, tests, documentation and evidence capture. Human work should be limited to unavoidable external-console actions, account permissions, legal declarations and production identifiers.

## Locked UX principles
- fast boot
- instant retry
- clean menus
- low cognitive load
- bright but not noisy
- friendly arcade tone

## Locked technical philosophy
- offline must be real, not partial
- low-end Android compatibility matters
- responsiveness is more important than visual complexity
- content quantity should not beat quality
- monetization services must fail safely and independently of gameplay
