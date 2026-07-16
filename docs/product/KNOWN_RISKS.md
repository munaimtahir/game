# Known Risks

## High Risk

- Current Room setup in code uses destructive migration fallback and does not satisfy the locked persistence policy.
- Current local-day clock implementation uses simple epoch-day math and does not satisfy local rollover requirements.
- Current build graph still includes extra legacy game modules, which can confuse scope and test expectations if not isolated carefully.

## Medium Risk

- Current settings storage uses `SharedPreferences`; it is acceptable for MVP if hardened, but migration pressure may reappear later.
- Current Stack Drop prototype uses gesture-first controls and will need control-layout revision to match the locked MVP spec.
- Current Pulse Orbit and Lane Drift implementations may differ slightly from the newly locked scoring and reward formulas.

## Low Risk

- Historical audit docs in the repo can still confuse future contributors unless they follow the new authoritative source chain.

## Mitigations

- Treat `LOCKED_DECISIONS.md` as the top authority.
- Keep Stage 1 focused on shared-system hardening before broader feature expansion.
- Add migration, challenge-day, and reward-ledger tests early.
