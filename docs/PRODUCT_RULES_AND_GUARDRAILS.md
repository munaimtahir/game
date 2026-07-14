# Product Rules and Guardrails — Offline Mini Arcade

## What this product must feel like
- fast
- dependable
- lightweight
- friendly
- replayable
- clean

## What this product must never feel like
- deceptive
- bloated
- spammy
- noisy
- casino-like
- cloned and lazy
- fake-offline

## Core guardrails

### 1. Offline truth
All core play must function without internet.
Ad, billing or consent-service failure must never block gameplay or navigation.

### 2. Low-end priority
Design and performance decisions must assume weaker Android phones matter.
Advertising SDK integration must not create avoidable startup delay, memory pressure or gameplay stutter.

### 3. Quantity restraint
Do not add games just to inflate content count.
A small polished bundle is the strategy.

### 4. Shared identity
This is one arcade product, not separate unrelated mini-apps.

### 5. Honest marketing
Do not exaggerate game count, features, rewards, premium benefits or offline capability.

### 6. Ad discipline
- no advertisement during active gameplay
- no app-open advertisement
- no banner advertisement in the initial monetized release
- no forced advertisement during the first 5 completed runs
- initial interstitial cadence: after every 3 completed runs, with a 120-second minimum cooldown and maximum 4 impressions per day
- no interstitial after a very short accidental run
- no interstitial immediately after a rewarded advertisement
- rewarded advertising must be optional and clearly describe the reward before playback
- failure to load an advertisement must silently continue the intended user flow

### 7. Premium discipline
- all three games remain free
- no energy system, pay-to-retry gate or premium lock on core progression
- lifetime ad removal is a one-time non-consumable purchase
- no subscription in the initial free release
- a subscription may be considered only for genuine recurring content

### 8. Fast flow
Open -> choose -> play -> retry should be extremely fast.
Monetization must not interfere with the Retry action or insert ads immediately after tap-heavy gameplay.

### 9. Accessibility basics
Minimal settings should still include:
- sound on/off
- music on/off
- vibration on/off
- pause where appropriate
- readable contrast-safe UI choices
- a clear privacy-options entry where consent rules require it

### 10. Data restraint
Collect only analytics needed to evaluate acquisition, retention, gameplay, advertising and purchase performance.
Do not require an account for monetization or progression.
