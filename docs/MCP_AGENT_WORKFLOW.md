# MCP Agent Workflow

## Core Philosophy
1. **One game at a time**: Never attempt to fix multiple games in the same prompt.
2. **Empirical testing**: Base changes on `run_adb_smoke.sh` output, unit tests, and CI artifacts.

## Standard Repair Loop
1. Agent reads `GAMEPLAY_ISSUE_MAP.md`.
2. Agent reads target game files (e.g., `LaneDriftCollision.kt`).
3. Agent writes a `GAME_BEFORE_AFTER.md` plan.
4. User approves plan.
5. Agent modifies code and immediately writes a matching test.
6. Agent runs local CI tools (`./gradlew testDebugUnitTest`).
7. Agent triggers GitHub CI or asks user to trigger it.
8. Validate screenshots and test reports.
