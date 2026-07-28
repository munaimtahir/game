# Monetization Implementation

Arcade Trio uses a centralized, fail-safe monetization layer in `app/src/main/java/com/vexel/offlinearcade/monetization`.

## Startup and consent

`GoogleUmpConsentManager` requests updated consent information on every fresh app launch, loads the published form when required, and reads `ConsentInformation.canRequestAds()` afterward. Mobile Ads initialization is process-scoped and happens only when that value allows ad requests. Consent failures update local state and never block the splash, navigation, or gameplay.

Settings exposes `Privacy choices` only when UMP reports that privacy options are required. The existing Privacy Policy link remains separate.

Debug-only UMP geography testing uses `UMP_DEBUG_GEOGRAPHY_EEA=true` and an optional `UMP_DEBUG_TEST_DEVICE_HASH`. These values are not enabled automatically in release builds.

## Ad placements and policy

- Anchored adaptive banners are allowed on Home, Marketplace, and Stats.
- Banners are not rendered on active game screens, splash, consent, or gameplay controls.
- Interstitials are considered only when leaving a completed run.
- The first five completed runs are ad-free; after that, an interstitial is considered every third completed run.
- A successful interstitial requires a 120-second cooldown, a maximum of four impressions per local day, consent, connectivity, a valid activity, and no premium entitlement.
- The run counter is reset only after the ad actually shows. Failed or unavailable ads immediately continue navigation.
- A rewarded ad records a full-screen timestamp and suppresses an interstitial during the same cooldown window.
- App-open ads, subscriptions, rewarded interstitials, gameplay interruptions, and automatic rewarded ads are not implemented.

## Rewarded ads

Marketplace contains one voluntary placement: `Watch one ad to receive 50 Arcade Coins`. The button is disabled while no rewarded ad is ready and is hidden from premium users. Coins are added through the existing Room-backed `ArcadeRepository`; the earned-reward callback is protected by an exactly-once gate.

## Premium and billing

The non-consumable product is `premium_lifetime`. Play Billing reconciles purchases and acknowledges qualifying purchases. A successful cached entitlement remains effective offline and immediately disables/removes banner and full-screen ad objects. Billing unavailability does not revoke a cached premium entitlement.

Premium users receive no banners, interstitials, or rewarded offers.

## Build configuration

Debug variants always use Google’s official test IDs. Release variants require these environment variables and reject blank values or Google demo IDs:

```text
ADMOB_APP_ID
ADMOB_BANNER_AD_UNIT_ID
ADMOB_INTERSTITIAL_AD_UNIT_ID
ADMOB_REWARDED_AD_UNIT_ID
PLAY_PREMIUM_PRODUCT_ID              # defaults to premium_lifetime
```

Production identifiers are intentionally not committed to source or public documentation. The manifest receives the selected app ID through the `resolvedAdMobAppId` placeholder.

## Verification

```bash
./gradlew testDebugUnitTest lintDebug
git diff --check
```

For release validation, provide signing properties and production AdMob environment variables, then run `assembleRelease` and `bundleRelease`. Do not click ads. Live Play Billing restoration and physical-device consent/ad behavior require a licensed test account and device.

Known limitation: automated tests do not depend on live UMP forms, AdMob fill, or Play Billing callbacks; those remain manual validation scenarios.
