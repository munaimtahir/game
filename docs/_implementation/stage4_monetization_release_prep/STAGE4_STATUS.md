# Stage 4 Status

## Scope Summary

- Added centralized billing and advertisement abstractions in the app layer.
- Premium is modeled as a one-time entitlement with local caching for offline suppression of ads.
- Advertisement eligibility is centralized and currently limited to a non-gameplay marketplace banner slot.
- Stage 4 non-device hardening is complete; real-device validation remains deferred to Stage 5.

## Implemented Changes

- Added `ArcadeBillingManager` for Play Billing connection, product lookup, purchase handling, acknowledgement, entitlement caching, and restore/refresh flows.
- Added `MonetizationPreferences` for persisted premium state and ad-throttle state.
- Added `ConnectivityMonitor` and `ArcadeAdPolicy` to centralize ad eligibility.
- Added `MarketplaceAdBanner` as the only reachable ad placement and kept gameplay screens ad-free.
- Added premium purchase and restore actions to Settings and Marketplace.
- Added manifest permissions and build-time configuration for billing and AdMob identifiers.
- Pinned the ads SDK to a Kotlin-1.9-compatible version after verifying official AdMob release constraints.

## Verification Evidence

- `./gradlew testDebugUnitTest --no-daemon --console=plain` — PASS
- `./gradlew lintDebug --no-daemon --console=plain` — PASS
- `./gradlew :app:assembleDebug :app:assembleDebugAndroidTest :app:compileReleaseKotlin --no-daemon --console=plain` — PASS

## Acceptance Review

### Passed

- No advertisement placement exists in active gameplay.
- Premium is modeled as one-time only; no subscription code path was added.
- Premium state is cached locally for offline ad suppression.
- Ad eligibility is centralized instead of scattered across game screens.
- Ad and billing failures are modeled to fail safe in the shell layer.
- Debug build, Android test APK compilation, lint, and release compilation pass after monetization integration.

### Remaining Before Release Approval

- Physical-device and connected-test validation has not yet been executed.
- Real purchase, restore, cancellation, and pending-flow validation is still pending Stage 5.
- Real ad-loading behavior, frequency-cap observation, and offline runtime suppression are still pending Stage 5.
- A signed release artifact is not produced in this environment because signing credentials are not available.

## Current Verdict

- `IMPLEMENTATION COMPLETE — DEVICE VALIDATION PENDING`

Reason:

- All required non-device implementation and build checks for Stages 1–4 are complete, but the final release gate cannot be considered until the Stage 5 device-validation package is executed on hardware.
