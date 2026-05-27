# Arcade UX Research Summary

Date: 2026-05-26

## Sources Reviewed
- Google Play ads policy, disruptive ads and interstitial guidance: [Ads - Play Console Help](https://support.google.com/googleplay/android-developer/answer/9857753?hl=en)
- Android accessibility principles: [Principles for improving app accessibility](https://developer.android.com/guide/topics/ui/accessibility/principles)
- Android quality expectations: [Core app quality guidelines](https://developer.android.com/docs/quality-guidelines/core-app-quality)
- FTUE / casual onboarding patterns: [Best practices for a successful FTUE](https://www.gamedeveloper.com/design/best-practices-for-a-successful-ftue-first-time-user-experience-)
- Casual game design principles: [GDC: Meretzky Talks Casual Game Design Principles](https://www.gamedeveloper.com/game-platforms/gdc-meretzky-talks-casual-game-design-principles)
- Short-session retention and habit loops: [Designing Habit-Forming Games](https://www.gameanalytics.com/blog/designing-habit-forming-games)
- Short-play-session framing: [Getting and Keeping Players: Designing for Engagement](https://www.gamedeveloper.com/design/getting-and-keeping-players-designing-for-engagement)
- Feedback and reward clarity: [Feedback in games - how to design rewards and punishments?](https://www.gamedeveloper.com/game-platforms/feedback-in-games-how-to-design-rewards-and-punishments)
- One-button / one-thumb interaction framing: [One Button Games](https://www.gamedeveloper.com/design/one-button-games)
- Near-miss caution: [Liberty Bell, Liberty Bell…Cherry: Gaming, Gambling and the Near-Miss](https://www.gamedeveloper.com/design/liberty-bell-liberty-bell-cherry-gaming-gambling-and-the-near-miss)

## Extracted Principles
- Fast entry matters more than long onboarding.
- Players should understand the first loop within seconds, not minutes.
- Short sessions are a feature, not a bug, for mobile arcade games.
- Feedback must be immediate and readable.
- Near-miss feedback can be satisfying, but it must stay skill-based and not drift into slot-machine style manipulation.
- One-thumb or one-button input is a strong fit for lightweight mobile arcade play.
- Accessibility requires labels, state clarity, non-color cues, and supported semantics.
- Ads, if present at all, should never interrupt active play.

## What Applies to Offline Mini Arcade
- Keep the home screen instantly scannable.
- Keep each game understandable from its detail screen in one glance.
- Keep retry immediate and keep failure screens short.
- Keep score, combo, pickups, and line-clear feedback obvious.
- Use a bright light arcade palette with strong contrast and simple shapes.
- Preserve offline-first play with no gating on network or account state.

## What Must Be Avoided
- No ads during active gameplay.
- No unexpected interstitial interruptions at the beginning of play.
- No casino-like near-miss loops or manipulative reward cadence.
- No cluttered home screen that hides the three MVP games.
- No visual noise that makes collisions, hazards, or pickups ambiguous.
- No gameplay loop that depends on network calls, account sync, or server timing.

## Proposed Visual Direction
- Bright light arcade palette with calm backgrounds and strong, saturated accent colors.
- Simple geometric game objects with a little character, not overloaded detail.
- Clear hierarchy:
  - top line = what game is this
  - middle = what should the player do
  - bottom = score, pause, retry, and short hints
- Distinct danger, reward, and neutral states.

## Proposed Feedback Rules
- Reward the exact action the player just took.
- Use one clear signal for success, one for failure, one for collection, and one for line clears.
- Keep animation short enough to preserve immediate retry.
- Use motion sparingly on low-end devices and avoid continuous decorative motion that blocks readability.
- Make near-miss feedback separate from collision feedback.

## Proposed Difficulty Principles
- Start easier than the team thinks is necessary.
- Delay ramp-up until the player understands the control loop.
- Increase speed or density gradually, not abruptly.
- Keep the first 20 to 30 seconds forgiving.
- Favor predictable patterns early, then add variability later.

## Monetization-Safe Natural Break Points
- After a failed run, on the score/result screen.
- After a completed daily challenge claim.
- In settings or theme unlock screens, not during a run.
- On the home screen or detail screen, not during active play.
- If ads are ever added, they should be restrained and never interrupt active gameplay.

## Low-End / Offline Guardrails
- Keep gameplay local and deterministic where possible.
- Keep rendering simple enough for low-end Android devices.
- Prefer one-thumb gestures and minimal UI chrome.
- Avoid heavy asset pipelines that make startup slow.
- Keep all critical flows functional without internet access.

## Practical Translation for This Repo
- Lane Drift should get the first serious pass because collision fairness and readability are the most gameplay-breaking issues.
- Pulse Orbit should emphasize instant comprehension and rapid retry once Lane Drift is clean.
- Stack Drop should stay stable and readable, not over-refined into visual noise.
