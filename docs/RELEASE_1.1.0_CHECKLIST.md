# Release Checklist

Scope: crash-recovery upload for the locked 3-game MVP.
Current target build: `versionName 1.1.4`, `versionCode 14`.

## Versioning
- [x] Confirm `app/build.gradle.kts` is set to `versionCode 14`.
- [x] Confirm `app/build.gradle.kts` is set to `versionName 1.1.4`.
- [ ] Confirm `versionCode 14` has never been uploaded to Play Console.

## Crash Recovery Gate
- [ ] Install the build on a physical device after uninstalling the previous app.
- [x] Cold-launch from the launcher and confirm the app reaches Home.
- [x] Verify there is no immediate crash on first open.
- [x] Verify logcat contains no `fatal exception`, `ANR in`, or `Process crashed` entries during launch.
- [x] Repeat the launch check on at least one representative device class.

## Gameplay Sanity
- [x] Open Pulse Orbit and confirm the screen loads normally.
- [x] Open Lane Drift and confirm the screen loads normally.
- [x] Open Stack Drop and confirm the screen loads normally.
- [x] Confirm the three MVP games remain the only public release scope.

## Device Suite
- [x] Run `./scripts/run_adb_device_suite.sh`.
- [x] Confirm the suite passes without crash or ANR failures.
- [x] Archive the generated artifacts under `artifacts/device-test/<timestamp>/`.

## Local Validation
- [x] Run `./gradlew testDebugUnitTest`.
- [x] Run `./gradlew lintDebug`.
- [x] Run `./gradlew :app:bundleRelease`.
- [x] Confirm the release bundle is generated successfully.

## Play Store Submission
- [ ] Confirm release signing values are configured on the upload machine.
- [ ] Confirm the Play Store listing assets are ready outside the repo.
- [ ] Confirm the privacy policy URL is available.
- [ ] Upload only after the clean-launch gate passes.

## Notes
- The rejected build was treated as a launch-crash issue, so first-open stability is the top release gate.
- If any gameplay or UI changes are reintroduced before upload, rerun the device suite and cold-launch checks.
- The device suite is now aligned to the current app id: `com.vexel.arcadetrio` / `com.vexel.arcadetrio.test`.
- The attached validation device previously had `versionName 1.1.3` / `versionCode 13` installed with a last-update timestamp of July 4, 2026, so release evidence must come from a fresh uninstall/reinstall run rather than the preexisting install.
