# Existing CI Review

Date: 2026-05-26

## What Already Exists
- A dedicated workflow already exists at [`.github/workflows/android-emulator-gameplay-ci.yml`](../../../.github/workflows/android-emulator-gameplay-ci.yml).
- The workflow already covers:
  - checkout
  - Java 17 setup
  - Gradle cache setup
  - debug assemble
  - JVM unit tests
  - lint
  - emulator boot via `reactivecircus/android-emulator-runner`
  - ADB smoke and screenshot scripts
  - artifact upload on completion
- The repo also already contains reusable CI helpers under [`scripts/ci/`](../../../scripts/ci/).

## Current Strengths
- Manual `workflow_dispatch` support exists.
- `pull_request` support to `main` exists.
- Artifact upload is already wrapped in `if: always()`.
- Emulator animations are disabled.
- The CI path is already split into Gradle build/test/lint, then emulator work, which is the right shape for reliability.

## Gaps Observed Before This Pass
- `api_level` default had been hardcoded to `34` in the job environment even though the workflow input defaulted to `35`.
- `retry_failed` was present as an input but was not wired into the emulator task script.
- The emulator task script was too thin:
  - it did not explicitly wait for boot completion
  - it did not retry a failed connected test
  - it did not clearly branch on `test_level`
  - it did not reliably capture bugreport data
- Screenshot capture existed, but the script was still a generic baseline and not yet a full state matrix for all three games.

## Current State After This Pass
- The workflow now uses `API_LEVEL=35` by default and pushes to `main`.
- `retry_failed` is threaded into the job environment.
- `scripts/ci/run_emulator_tasks.sh` now:
  - waits for boot
  - respects `TEST_LEVEL`
  - retries connected tests once when configured
  - collects logcat and UI dump artifacts
  - captures bugreport on failures or `full` runs

## Remaining CI Improvements
- Add stronger per-game screenshot state automation if the current generic capture script is not sufficient in GitHub Actions.
- Decide whether `screenshots_only` should still run a minimal smoke step or only render/capture state.
- Consider adding a separate workflow job for screenshot-only runs if the combined job becomes too slow.

## CI Conclusion
- The project already had the right workflow entrypoint.
- The biggest value in this pass was hardening the script layer and correcting the workflow defaults so GitHub Actions can become the primary evidence source instead of a local-device fallback.
