# Product Specification

Authoritative references:

- `LOCKED_DECISIONS.md`
- `PRODUCT_RULES_AND_GUARDRAILS.md`

## Product Overview

Offline Mini Arcade is a single Android arcade app built around fast, replayable offline sessions. The MVP release contains exactly three games:

- Pulse Orbit
- Lane Drift
- Stack Drop

The product identity is one calm, readable arcade shell with shared progression, daily challenges, local stats, and optional cosmetics.

## Audience

- Players who want short offline play sessions
- Players on lower-end or older Android phones
- Players who prefer low-friction mobile games without accounts or online dependencies

## Non-Goals

- competitive online ranking
- cloud saves
- social features
- subscriptions
- pay-to-win boosts
- game-count expansion during MVP delivery

## Shared Shell

- Home screen is the arcade launcher and current-day overview.
- Each game has:
  - detail screen
  - gameplay screen
  - first-run tutorial overlay
  - pause state
  - result state
- Shared non-gameplay screens:
  - challenges hub
  - stats
  - themes / cosmetics
  - settings

## Session Design

- A run should begin quickly after game entry.
- Failure-to-retry time should stay minimal.
- A run result should explain score, earned currency, and progress updates without unnecessary taps.
- Returning home should preserve arcade context and updated progression state.

## Monetization Summary

- Free install
- Restrained ads outside active gameplay only
- Optional one-time premium purchase removes ads
- Premium may include cosmetic-only extras

Detailed monetization policy: `docs/product/MONETIZATION_POLICY.md`
