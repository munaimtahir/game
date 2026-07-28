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

## Consent & Privacy Management

- Google User Messaging Platform (UMP) SDK `2.2.0` handles user consent.
- Advertising requests are strictly gated by `ConsentInformation.canRequestAds()`.
- A Privacy Options button is provided in Settings whenever consent status allows or requires consent modification.
- Privacy Policy URL: `https://vexel.pk/apps/offline-mini-arcade/privacy/`
- Manifest explicitly declares `com.google.android.gms.permission.AD_ID`.

## Allowed Ad Placements

- Non-gameplay banner placement on the Marketplace screen (`MarketplaceAdBanner.kt`) when `!premiumUnlocked` and consent permits.
- Non-gameplay banner placement is also available on Home and Stats when the layout has room.
- Restrained interstitial placement on post-run exit navigation (`ArcadeInterstitialController.kt`) subject to `ArcadeAdPolicy`.

## Disallowed Ad Placements

- during gameplay
- on top of controls
- on every failure
- before first play / application launch
- on pause overlays
- on reward claims

## Ad Frequency Policy

- Central policy component (`ArcadeAdPolicy.kt`) decides interstitial eligibility:
  - Grace Period: First 5 completed runs show zero ads.
  - Cadence: Interstitials allowed every 3 completed runs.
  - Cooldown: Minimum 120 seconds between interstitials.
  - Daily Cap: Maximum 4 interstitials per day.
  - Duration: Minimum 8-second run length required.
- Ads are skipped if:
  - device offline
  - premium active
  - onboarding active
  - result flow not fully completed
  - frequency cap or cooldown not met
  - consent not granted / `canRequestAds == false`

## Rewarded placement

- The initial voluntary placement is in Marketplace: 50 Arcade Coins per earned reward.
- Rewarded ads are unavailable to premium users.
- Rewards are granted only from the earned-reward callback and are persisted through the shared Room repository.
