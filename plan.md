# UI Improvement Plan

Scope: locked 3-game MVP only
- Pulse Orbit
- Lane Drift
- Stack Drop

## Release Version Ledger

### Current ongoing release
- `versionName`: `1.0.9`
- `versionCode`: `9`
- Status: current in-progress release

### Release 1.0.9 focus
- Release 1.0.8 is complete and was uploaded as `versionCode 8` / `versionName 1.0.8`.
- Final release-polish work for `1.0.9` is now implemented and verified in the current worktree.

### Fixed items included in `1.0.8`
- Added How-to-Play guidance for Pulse Orbit, Lane Drift, and Stack Drop.
- Added tutorial-seen local persistence with manual replay access.
- Improved local progression foundations for high scores, sessions, daily challenges, achievements, and stats.
- Added arcade-style challenge, achievement, and progress UI updates using existing theme components.
- Prepared the current Play Console upload version as `versionCode 8` / `versionName 1.0.8`.

### Fixed items included in `1.0.7`
- Shared scaffold accessibility improvements
- Home screen simplification and faster game discovery
- Edge-to-edge handling for Android 15 and later
- Removed deprecated system-bar color writes that caused Android 15 edge-to-edge warnings
- Release note / Play Store prep documentation sync
- Release bundle generation for the new build

### Next release rule
- Always increment `versionCode` for the next Play Store upload.
- Suggested next build after more fixes: `versionName` `1.1.0`, `versionCode` `10`.
- Do not reuse `versionCode 9` for another upload, or Play Console will reject it as a duplicate/conflicting release.

## Completed

### Shared scaffold and accessibility
- Increased the shared back button target to 48dp.
- Added explicit accessibility semantics and a clear `Back` label.

### Home screen simplification
- Reduced hub clutter by moving the 3 game cards closer to the top.
- Reframed the hero copy to emphasize the 3-game MVP.
- Moved secondary actions into a cleaner quick-actions section.
- Added a dedicated marketplace entry label/tag for clarity.

### Release and versioning
- Bumped app release version to `versionCode 8`.
- Bumped app release version name to `1.0.8`.
- Updated release notes and Play Store prep docs to match the new release.
- Release bundle verification is tracked in the final build report.

### Release 1.0.9 planning
- Next sprint is now targeting `versionCode 9` / `versionName 1.0.9`.
- Keep the locked 3-game MVP scope unchanged while continuing polish and run-behavior work.

### Release 1.0.9 UI polish completed
- Made the home game cards read clearly as launch targets with a visible play affordance.
- Reworked Stack Drop compact controls so the layout stays usable on smaller phones.
- Verified compact-width HUD and bottom-control behavior on debug/device validation runs.
- Gave Pulse Orbit, Lane Drift, and Stack Drop distinct detail-screen identities while keeping the shared arcade design language.

### Run behavior updates
- Added one-time tutorials for all 3 MVP games with manual replay access.
- Added completion summaries after game-over to surface high score and progression updates.
- Wired daily challenge, achievement, and stats updates into the run-result flow.
- Added per-game challenge play/continue actions from the challenge screen.
- Slightly increased Lane Drift and Stack Drop pacing to keep short runs brisk without changing core rules.
- Added clearer Lane Drift lane buttons and slightly more forgiving swipe thresholds for gesture-navigation devices.
- Stack Drop already has a next-piece preview in the current screen layout.
- Kept gameplay rules unchanged while improving onboarding and progress feedback.

### Build warning cleanup
- Migrated all in-game back icons to the AndroidX `AutoMirrored` versions to remove Android deprecation warnings.
- Removed the last release-build warning from the shared theme helper.
- Silenced remaining unused-parameter warnings on public screen entry points and the Marketplace card helper.
- Upgraded Android Gradle Plugin to `8.6.0`, which is the documented minimum for `compileSdk 35`.
- Removed the temporary `gradle.properties` suppression for the compileSdk advisory.

### Must Fix Before Final Release
- Made the home game cards more explicit as launch targets with stronger play affordance cues.
- Reworked Stack Drop compact controls so they feel less cramped on smaller phones.
- Verified layout behavior on compact-width devices so no HUD or bottom control gets crowded.
- Gave each of the 3 game detail screens a more distinct visual identity so they do not feel like copies of the same template.

### Should Fix If Time Allows
- Improve the visual distinction between success, failure, and reward states across all three games.
- Add stronger empty-state / first-run cues for any game that starts in a ready state.
- Review whether any remaining icon-only actions need text labels on smaller screens.
- Review whether additional accessibility labels are needed on gameplay overlays and result cards.
- Review whether the new run-summary popup needs a slightly lighter presentation on very short sessions.

### Nice to Have
- Add more game-specific polish assets or micro-illustrations for the detail pages.
- Consider a more opinionated home-screen hierarchy if future meta systems grow larger.

## Notes

- The current priority order should be:
  1. Shared accessibility and scaffold safety
  2. Home screen clarity and game discovery
  3. Per-game onboarding and playability polish
  4. Compact-device layout verification
- Keep the UI bright, clean, and readable. Avoid clutter, noisy motion, and casino-like reward pressure.
