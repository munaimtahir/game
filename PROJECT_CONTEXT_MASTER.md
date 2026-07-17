# Project Context Master

This file is the entry point for the locked Stage 0 design package for `Offline Mini Arcade`.

Authoritative source order:

1. `LOCKED_DECISIONS.md`
2. `PRODUCT_RULES_AND_GUARDRAILS.md`
3. `docs/product/PRODUCT_SPECIFICATION.md`
4. `docs/product/SHARED_SYSTEMS_SPEC.md`
5. `docs/product/PULSE_ORBIT_SPEC.md`
6. `docs/product/LANE_DRIFT_SPEC.md`
7. `docs/product/STACK_DROP_SPEC.md`
8. `docs/product/PROGRESSION_AND_DAILY_CHALLENGES.md`
9. `docs/product/TECHNICAL_ARCHITECTURE.md`
10. `docs/product/IMPLEMENTATION_BACKLOG.md`

Scope lock:

- Public MVP release scope is exactly three games:
  - Pulse Orbit
  - Lane Drift
  - Stack Drop
- The repository still contains legacy or prototype modules outside the MVP scope. They are non-authoritative and must not drive product decisions for Stages 1 to 4.

Monetization lock:

- The app is free to install.
- Ads are restrained and never shown during active gameplay.
- Premium is a one-time purchase that removes ads and may unlock additional cosmetic themes.
- Premium must not change scores, difficulty, rewards fairness, or competitive outcomes.

Repository reality at Stage 0:

- Current code already includes Compose navigation, Room persistence, `SharedPreferences` settings, and partial implementations for the three MVP games.
- Current production architecture is not yet fully aligned with the release target in several areas:
  - persistence still uses destructive Room fallback;
  - settings still use `SharedPreferences`;
  - local-day handling currently uses a simple epoch-day clock;
  - extra legacy modules remain in the build graph.
- Stage 0 locks the target architecture and behavior without starting implementation work that belongs to later stages.

Primary deliverables for this stage:

- Complete product specification
- Locked monetization policy
- Shared save/progression architecture
- Implementation-ready specs for the three MVP games
- Offline daily challenge design
- Technical architecture
- Stage 1 to 4 backlog with acceptance criteria
- Test strategy, decision log, and risk register

Use this file first, then follow the linked documents above.
