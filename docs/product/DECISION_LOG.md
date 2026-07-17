# Decision Log

## 2026-07-14 Stage 0 Lock

- Created the missing authoritative product document set expected by the phased program.
- Locked public MVP scope to exactly three games.
- Classified extra repository game modules as legacy/prototype and non-authoritative.
- Locked monetization to:
  - free install
  - restrained ads
  - one-time premium ad removal
  - cosmetic-only premium benefits
- Preserved the existing technical stack direction:
  - Compose
  - Room
  - manual dependency assembly
  - settings store remains lightweight
- Rejected subscriptions and online-dependent progression.
- Locked one control scheme per game for MVP to reduce implementation ambiguity:
  - Pulse Orbit: tap
  - Lane Drift: swipe lane shift
  - Stack Drop: on-screen controls
- Chose local-date deterministic challenge generation with anti-duplication safeguards rather than punitive anti-time-travel rules.
