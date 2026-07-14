# UI Improvement Plan

Scope: locked 3-game MVP only
- Pulse Orbit
- Lane Drift
- Stack Drop

## Release Version Ledger

### Current ongoing release
- `versionName`: `1.1.1`
- `versionCode`: `11`
- Status: crash-recovery upload candidate

### Rollback note
- The repository was temporarily rolled back to the `1.0.8` baseline for validation.
- The upload target is now back on `1.1.1` after passing the release checks.

### Fixed items included in `1.0.8`
- Added How-to-Play guidance for Pulse Orbit, Lane Drift, and Stack Drop.
- Added tutorial-seen local persistence with manual replay access.
- Improved local progression foundations for high scores, sessions, daily challenges, achievements, and stats.
- Added arcade-style challenge, achievement, and progress UI updates using existing theme components.
- Prepared the current Play Console upload version as `versionCode 11` / `versionName 1.1.1`.

### Fixed items included in `1.0.7`
- Shared scaffold accessibility improvements
- Home screen simplification and faster game discovery
- Edge-to-edge handling for Android 15 and later
- Removed deprecated system-bar color writes that caused Android 15 edge-to-edge warnings
- Release note / Play Store prep documentation sync
- Release bundle generation for the new build

### Next release rule
- Always increment `versionCode` for the next Play Store upload.
- Suggested next build after more fixes: `versionName` `1.1.2`, `versionCode` `12`.
- Do not reuse `versionCode 11` for another upload, or Play Console will reject it as a duplicate/conflicting release.
- Follow the dedicated crash-recovery checklist in [docs/RELEASE_1.1.0_CHECKLIST.md](docs/RELEASE_1.1.0_CHECKLIST.md).

### Needs update next
- Refresh `docs/PLAY_STORE_RELEASE.md` for the next upload.
- Re-run unit and device checks after any reintroduced gameplay or UI changes.
- Clean up stale generated artifacts if they are not part of the intended baseline.

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
- Bumped app release version to `versionCode 11`.
- Bumped app release version name to `1.1.1`.
- Updated release notes and Play Store prep docs to match the new release.
- Release bundle verification is tracked in the final build report.

### Build warning cleanup
- Migrated all in-game back icons to the AndroidX `AutoMirrored` versions to remove Android deprecation warnings.
- Removed the last release-build warning from the shared theme helper.
- Silenced remaining unused-parameter warnings on public screen entry points and the Marketplace card helper.
- Upgraded Android Gradle Plugin to `8.6.0`, which is the documented minimum for `compileSdk 35`.
- Removed the temporary `gradle.properties` suppression for the compileSdk advisory.

## Pending

### High priority
- Give each of the 3 game detail screens a more distinct visual identity so they do not feel like copies of the same template.
- Make the home game cards more explicit as launch targets with stronger play affordance cues.
- Improve Pulse Orbit first-run guidance so the tap timing loop is more obvious on entry.
- Add safe-gesture protection or clearer gesture handling for Lane Drift so it plays better on gesture-navigation devices.
- Add Stack Drop next-piece preview so the player can plan ahead.
- Rework Stack Drop compact controls so they feel less cramped on smaller phones.

### Medium priority
- Add stronger empty-state / first-run cues for any game that starts in a ready state.
- Improve the visual distinction between success, failure, and reward states across all three games.
- Verify layout behavior on compact-width devices to make sure no HUD or bottom control gets crowded.
- Review whether any remaining icon-only actions need text labels on smaller screens.

### Lower priority
- Add more game-specific polish assets or micro-illustrations for the detail pages.
- Consider a more opinionated home-screen hierarchy if future meta systems grow larger.
- Review whether additional accessibility labels are needed on gameplay overlays and result cards.

## Notes

- The current priority order should be:
  1. Shared accessibility and scaffold safety
  2. Home screen clarity and game discovery
  3. Per-game onboarding and playability polish
  4. Compact-device layout verification
- Keep the UI bright, clean, and readable. Avoid clutter, noisy motion, and casino-like reward pressure.
- For the `1.1.0` upload, treat launch-crash verification on a clean install as a hard gate before Play Console submission.
