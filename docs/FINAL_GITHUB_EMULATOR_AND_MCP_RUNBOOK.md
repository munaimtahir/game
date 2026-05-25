# Final GitHub Emulator and MCP Runbook

## 1. Running GitHub Emulator Workflow from GitHub Web UI
1. Go to your repository on GitHub.
2. Click the **Actions** tab.
3. In the left sidebar, click **Android Emulator Gameplay CI**.
4. Click the **Run workflow** dropdown on the right side.

## 2. Workflow File Path
`.github/workflows/android-emulator-gameplay-ci.yml`

## 3. Workflow Inputs
- **game_target**: Choose the target (`all`, `lane_drift`, `pulse_orbit`, `stack_drop`).
- **test_level**: 
  - `smoke`: Basic app launch and crash check.
  - `full`: Complete Android tests (future support).
  - `screenshots_only`: Fast screenshot capturing.
- **api_level**: Default `34` (Recommended for KVM acceleration on GitHub Ubuntu runners).
- **retry_failed**: Set to `true` to retry flaky emulator boots.

## 4. Finding Artifacts
When a workflow completes, scroll to the bottom of the workflow run summary page to find the **Artifacts** section.

## 5. Downloading Screenshots & Logs
Download the artifact named `android-emulator-gameplay-ci-<run_number>`. Unzip it to view:
- `screenshots/`: Visual state captures.
- `logcat/`: Full system logs.
- `logs/`: Build and test logs.

## 6. Identifying Failures
- **Build Failure**: Look in `logs/assembleDebug.txt`.
- **Emulator Failure**: Look for timeout messages in workflow output or `logcat-final.txt`.
- **Gameplay/Test Failure**: Look in `test-results/` for unit test failures or `gameplay-smoke/adb-smoke.txt` for crashes.

## 7. Using MCP for this Project
Read `docs/MCP_SETUP.md`. Configure your client to point to this repository folder with strictly read permissions unless operating on an approved plan.

## 8. Running the Next One-Game Sprint
Follow `docs/MCP_AGENT_WORKFLOW.md`. Only target Pulse Orbit next. Do not touch Stack Drop.

## 9. What Not to Do
- **Do NOT** change multiple games in one sprint.
- **Do NOT** commit raw secrets.
- **Do NOT** bypass the emulator smoke checks.
- **Do NOT** rewrite existing `.github/workflows/completeadbtest.yml` as it may still be required by legacy systems.

## 10. How to Continue if GitHub Emulator Fails
If cloud instances hang due to KVM issues, pull the branch locally and run `./gradlew connectedAndroidTest` against a local emulator. 

## 11. Keeping Work One-Game-at-a-Time
Adhere to the `GAMEPLAY_ISSUE_MAP.md`. Do not perform global refactors that touch multiple games.
