# Final GitHub Emulator and MCP Runbook

## 1. Run the GitHub Emulator Workflow
1. Open the repository in GitHub.
2. Click **Actions**.
3. Select **Android Emulator Gameplay CI**.
4. Click **Run workflow**.

## 2. Workflow File
- [`.github/workflows/android-emulator-gameplay-ci.yml`](../.github/workflows/android-emulator-gameplay-ci.yml)

## 3. Inputs
- `game_target`
  - `all`
  - `lane_drift`
  - `pulse_orbit`
  - `stack_drop`
- `test_level`
  - `smoke`: build + JVM tests + lint + emulator smoke + screenshots
  - `full`: everything in smoke plus connected tests and bugreport on failure or full runs
  - `screenshots_only`: screenshot capture only
- `api_level`
  - defaults to `35`
- `emulator_profile`
  - use a reasonable Pixel profile if the workflow exposes it in future revisions
- `retry_failed`
  - `true` or `false`

## 4. Artifact Locations
- Build logs: `artifacts/logs/`
- Logcat: `artifacts/logcat/`
- Screenshots: `artifacts/screenshots/`
- Test results: `artifacts/test-results/`
- Lint outputs: `artifacts/lint/`
- Reports: `artifacts/reports/`
- UI dumps: `artifacts/ui-dumps/`
- Smoke evidence: `artifacts/gameplay-smoke/`

## 5. Downloading Evidence
- Open the workflow run in GitHub.
- Scroll to **Artifacts**.
- Download `android-emulator-gameplay-ci-<run_number>`.
- Inspect the log files before looking at the images; they usually explain why a screenshot or test is missing.

## 6. Failure Triage
- Build failure:
  - check `artifacts/logs/assembleDebug.txt`
- JVM test failure:
  - check `artifacts/logs/testDebugUnitTest.txt`
- Lint failure:
  - check `artifacts/logs/lintDebug.txt`
- Emulator boot failure:
  - check `artifacts/logs/wait-for-emulator.txt`
  - check `artifacts/logcat/logcat-final.txt`
- Smoke/runtime failure:
  - check `artifacts/gameplay-smoke/adb-smoke.txt`
  - check `artifacts/logcat/logcat-final.txt`
- Screenshot failure:
  - check `artifacts/logs/screenshot-capture.txt`

## 7. How to Use MCP Here
- Start with [`docs/MCP_SETUP.md`](./MCP_SETUP.md).
- Keep the repo root as the only filesystem boundary.
- Read [`docs/MCP_AGENT_WORKFLOW.md`](./MCP_AGENT_WORKFLOW.md) before writing code.
- Apply [`docs/MCP_SECURITY_GUARDRAILS.md`](./MCP_SECURITY_GUARDRAILS.md) before enabling write mode.

## 8. How to Run the Next One-Game Sprint
1. Pick only one game.
2. Read the relevant audit docs.
3. Write or update a test that defines the behavior.
4. Change the smallest code path possible.
5. Run `./gradlew testDebugUnitTest lintDebug`.
6. Run the GitHub emulator workflow and collect screenshots/logs.
7. Stop and write the before/after report.

## 9. What Not To Do
- Do not fix more than one game in the same sprint.
- Do not add new games.
- Do not put ads in active gameplay.
- Do not expose secrets.
- Do not assume a local adb device is part of the CI plan.

## 10. If GitHub Emulator Fails
- Re-run with the same branch and the same inputs.
- Check whether the failure is build, emulator boot, runtime, or screenshot capture.
- Use the local shell only to reproduce, not to change scope.
- If the issue is real gameplay behavior, move to the relevant game sprint rather than widening the CI job.

## 11. One-Game Discipline
- Lane Drift remains the next gameplay target.
- Pulse Orbit and Stack Drop should stay in audit-only mode until Lane Drift has a documented pass.
