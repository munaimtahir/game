# Monetization And Billing Status (Pre-Device)

## Implemented

- Centralized ad eligibility through `ArcadeAdPolicy`
- Offline-cached premium entitlement through `MonetizationPreferences`
- One-time in-app premium product flow through `ArcadeBillingManager`
- Google UMP Consent Management (`GoogleUmpConsentManager`) and Privacy Options form
- Connected Marketplace banner slot (`MarketplaceAdBanner.kt`) in `ArcadeNavHost.kt`
- Premium purchase, restore, and privacy options entry points in Settings and Marketplace

## Policy Alignment

- No ad placement exists in active gameplay.
- No ad is required to start a game.
- No subscription implementation was added.
- Premium is modeled as one-time only.
- Premium removes ads by disabling eligibility at the shell layer.

## Pending Stage 5

- Real purchase flow verification
- Restore flow verification
- Pending purchase verification where testable
- Cancelled purchase verification
- Runtime ad frequency-cap observation
- Offline runtime ad suppression confirmation
