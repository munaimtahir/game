# AI-Led Monetization Development Plan — Minimum Safe Stages

## Objective
Convert the existing paid Android game into a free, offline-first app with restrained ads and a one-time lifetime ad-removal purchase.

The work is deliberately limited to **3 stages**. No additional phase should be created unless a release gate exposes a genuine blocker.

## Locked implementation scope

### Include
- Google Mobile Ads SDK integration
- consent-management flow appropriate to target regions
- interstitial ads at controlled post-run transitions
- optional rewarded ads with explicit rewards
- Google Play Billing for one non-consumable product: `premium_lifetime`
- entitlement states: `FREE` and `LIFETIME_AD_FREE`
- purchase and restore flow
- premium/settings UI
- offline-safe failure handling
- essential monetization analytics
- privacy, Data Safety and Play Console handoff documentation
- automated tests and device evidence

### Exclude
- subscriptions
- legacy-paid-user migration
- app-open ads
- banner ads
- server backend or user accounts
- energy systems or paid retries
- new games or unrelated redesigns
- speculative monetization features

---

# Stage 1 — Repository Discovery and Implementation Contract

## AI coding-agent mission
Inspect the actual application repository and produce a verified, implementation-ready contract without changing production behavior.

## Required work
1. Identify the Android stack, modules, package/application ID, build variants and minimum/target SDK.
2. Map:
   - app startup
   - home/game-selection navigation
   - gameplay start and end events
   - result screens
   - retry flow
   - settings screen
   - current persistence layer
   - existing analytics, privacy and billing code
3. Find the safest shared insertion points for:
   - completed-run counter
   - interstitial eligibility
   - rewarded-ad offers
   - premium entitlement
   - privacy options
4. Confirm whether child-directed treatment, age declarations or family-policy restrictions are relevant from the current project configuration and listing documents.
5. Define exact files/classes to add or modify.
6. Define test strategy for unit, integration, UI and offline behavior.
7. Record all required external values as placeholders only:
   - AdMob app ID
   - interstitial ad-unit ID
   - rewarded ad-unit ID
   - Play Billing product ID
8. Create or update a concise implementation record under project documentation.

## Stage 1 output
- repository map
- implementation contract
- risk register
- exact change list
- verification command list
- no production ad IDs
- no unrelated refactor

## Release gate
Proceed only when:
- every monetization insertion point is tied to an actual file/class
- gameplay and retry flows are understood
- no legacy-user migration is included
- no subscription work is included
- the planned solution preserves full offline gameplay

---

# Stage 2 — Complete Monetization Implementation

## AI coding-agent mission
Implement the entire monetization system in one bounded sprint using test identifiers and fail-safe defaults.

## Required work

### A. Central monetization architecture
Create one centralized monetization layer responsible for:
- entitlement state
- ad eligibility
- run counters
- cooldown and daily cap
- consent readiness
- ad loading/showing
- billing connection
- purchase acknowledgement
- purchase restoration
- analytics events

Gameplay screens must not contain scattered direct SDK calls.

### B. Entitlement
Implement only:
- `FREE`
- `LIFETIME_AD_FREE`

Rules:
- `LIFETIME_AD_FREE` suppresses all forced ads
- entitlement is persisted locally
- Google Play ownership is rechecked when available
- a temporary billing failure never removes usable cached entitlement or blocks gameplay

### C. Interstitial rules
Implement launch defaults exactly:
- first 5 completed runs: no forced ads
- eligible after each subsequent block of 3 completed runs
- minimum 120-second cooldown
- maximum 4 impressions per local day
- natural post-run transition only
- never during gameplay
- never on app opening
- never on Play or Retry
- never after a very short accidental run
- never immediately after rewarded advertising
- unavailable ad: continue silently

Place the show decision after the result has been displayed and when the user leaves the result flow, not on the game-ending tap.

### D. Rewarded ads
Add only a small number of optional rewarded placements already supported by existing game systems.
Preferred order:
1. double normal post-run soft-currency reward
2. one challenge reroll, only if challenge rerolls already exist

Do not create major new progression systems merely to justify rewarded ads.
The reward must be granted exactly once after verified completion.

### E. Billing and premium UI
Implement:
- one-time product `premium_lifetime`
- buy flow
- restore purchases action
- pending, cancelled, failed and already-owned states
- acknowledgement of completed purchase
- Settings/Premium status display
- non-blocking “Remove Ads Forever” entry
- premium benefits description without misleading claims

### F. Consent and privacy controls
Implement:
- consent-information refresh
- consent form when required
- ad requests only when permitted
- persistent privacy-options entry when required
- safe non-personalized/limited behavior according to SDK result
- no gameplay block when consent services are unavailable

### G. Analytics
Track only essential events:
- run completed by game
- interstitial eligible
- interstitial shown
- interstitial failed/unavailable
- rewarded offer shown
- rewarded completed
- premium screen viewed
- purchase started/completed/cancelled/failed
- restore completed/empty/failed

Do not add an account, advertising profile or unnecessary personal-data collection.

### H. Tests and documentation
Add automated coverage for:
- first-5-run suppression
- every-3-runs eligibility
- cooldown
- daily cap
- premium suppression
- rewarded-to-interstitial suppression
- ad-load failure
- purchase-state mapping
- restore behavior
- offline startup and gameplay

Update technical documentation, privacy/data inventory and external configuration checklist.

## Stage 2 release gate
All must pass:
- clean build
- unit tests
- lint/static checks
- existing gameplay regression tests
- test ads display only at approved transitions
- no ad appears during gameplay or Retry
- premium entitlement suppresses forced ads
- purchase restoration works in test environment
- app remains fully playable with network disabled
- ad/billing/consent failures do not crash or block the app
- no subscription, banner, app-open or legacy migration code exists

---

# Stage 3 — Release Hardening and Store Handoff

## AI coding-agent mission
Verify the monetized build on real devices, prepare evidence, and produce a minimal human action list for Play Console and AdMob.

## Required work
1. Run device tests on at least:
   - one low-end or older Android device
   - one current supported Android version/emulator
2. Verify:
   - cold start
   - all three games
   - result and retry flows
   - first 5 runs without forced ads
   - eligible interstitial flow
   - daily cap and cooldown
   - rewarded completion and reward grant
   - lifetime purchase and restore in licensed test environment
   - offline gameplay
   - denied/limited consent paths where testable
   - orientation, back navigation and process recreation
3. Confirm no measurable gameplay stutter or unsafe startup delay from monetization initialization.
4. Produce release evidence:
   - command results
   - device matrix
   - screenshots or recordings of approved ad placements
   - purchase/restore evidence
   - offline evidence
   - known limitations
5. Prepare exact human-only checklist:
   - create/confirm AdMob app
   - create production interstitial and rewarded units
   - create `premium_lifetime` in Play Console
   - add licensed testers
   - provide production IDs through secure configuration
   - update privacy-policy URL/content
   - update Data Safety declaration
   - mark “Contains ads”
   - verify target-audience declarations
   - set country-specific price
   - change app price from paid to free only after closed-test GO
6. Produce final verdict:
   - `GO`
   - `CONDITIONAL GO`
   - `NO-GO`

## Final release gate
Public conversion to free is allowed only when:
- closed testing passes
- production identifiers are configured without being committed insecurely
- billing product is active and restorable
- privacy and Data Safety declarations match actual SDK behavior
- all game flows remain available offline
- no critical crash, ad-placement, purchase or policy blocker remains

---

# Human Work — Minimum Required

The user should only need to perform actions that an AI coding agent cannot safely perform without account authority:

1. Approve or create AdMob and Play Console products.
2. Supply production IDs through the agreed secure mechanism.
3. Approve privacy-policy and Data Safety declarations.
4. Configure licensed testers and closed-test track.
5. Approve the final country pricing.
6. After a documented GO, change the existing Play Store app from paid to free and release gradually.

Everything else belongs to the AI coding agent.

# Scope-Control Rule

During these three stages, reject unrelated work unless it is required to pass a release gate. Do not redesign the games, add a subscription, add new modes, or create a backend.
