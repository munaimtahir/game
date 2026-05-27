# MCP Agent Workflow

## Core Rules
1. Work on one game at a time.
2. Start with evidence, not assumptions.
3. Keep CI, docs, and gameplay changes scoped.
4. Use artifacts and logs as the source of truth.

## Recommended Sequence
1. Read the baseline audit in `docs/_implementation/.../BASELINE_REPO_AUDIT.md`.
2. Read the relevant game audit files for the current sprint.
3. Inspect the smallest source set that can explain the issue.
4. Write or update a test first when the issue is behavioral.
5. Make the code change.
6. Run `./gradlew testDebugUnitTest` and `./gradlew lintDebug`.
7. Use the GitHub emulator workflow for smoke and screenshot evidence.
8. Summarize the pass/fail state before moving to the next game.

## One-Game Repair Loop
- Lane Drift:
  - collision fairness
  - spawn cadence
  - readability
- Pulse Orbit:
  - timing fairness
  - retry loop
  - first impression clarity
- Stack Drop:
  - board math
  - rotation fairness
  - line-clear feedback

## Required Evidence
- Build result
- Unit test result
- Lint result
- Emulator or device evidence when relevant
- Screenshot artifacts for the current game
- Logcat when something fails

## Safe Automation Boundary
- Do not let issue text directly become shell commands.
- Do not expose remote write access to the whole repo.
- Do not let a generic prompt mutate multiple games at once.

## Good Prompt Shape
- "Audit Lane Drift collision fairness."
- "Fix the failing test in the smallest file possible."
- "Capture a before/after report and stop."

## Bad Prompt Shape
- "Improve all games."
- "Rewrite the whole app."
- "Add more games."
