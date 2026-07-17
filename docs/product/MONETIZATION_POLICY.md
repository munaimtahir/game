# Monetization Policy

Authoritative status:

- This file defines the live MVP monetization policy.
- Historical audits that describe a fully ad-free build are snapshots of older repository state, not current policy.

## Locked MVP Policy

- The app is free to install.
- The free version may show restrained advertisements.
- Ads are never shown during active gameplay.
- Ads are never required to start a run.
- Ads are never required to claim normal run rewards or daily challenge rewards.
- Ads are never shown immediately after every run.
- Ads are never shown during first-run onboarding.
- Ads are never shown when the device is offline.
- Ad load failure must never block navigation or progression.

## Premium

- Premium is a one-time purchase.
- Premium removes advertisements.
- Premium may include additional cosmetic themes or skins.
- Premium does not unlock score, difficulty, reward, or challenge advantages.
- Premium restoration must work after reinstall.
- Cached entitlement must allow offline operation after a successful purchase sync.

## Fairness Rules

- Purchases must not alter:
  - score calculations
  - obstacle generation
  - gap timing
  - piece generation
  - drop speed
  - challenge targets
  - run rewards
  - milestone thresholds
- Cosmetic-only differences must keep readability and equivalent gameplay clarity.

## Allowed Ad Placements

- Optional non-gameplay placement on a suitable shell screen
- Occasional interstitial after a completed result flow and only if frequency caps allow

## Disallowed Ad Placements

- during gameplay
- on top of controls
- on every failure
- before first play
- on pause overlays
- on reward claims

## Ad Frequency Policy

- Central policy component decides eligibility.
- Eligibility uses both completed-session count and elapsed time since the last ad.
- Ads must be skipped if:
  - device offline
  - premium active
  - onboarding active
  - result flow not fully completed
  - frequency cap not met
