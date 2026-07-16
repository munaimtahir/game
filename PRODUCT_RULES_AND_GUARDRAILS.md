# Product Rules And Guardrails

This file defines product behavior rules. `LOCKED_DECISIONS.md` remains the final authority when wording differs.

## Core Promise

- The app must feel like a polished offline mini arcade built around short, replayable runs.
- The product must be understandable in seconds on first launch.
- Progression should reward return play without creating anxiety or obligation.

## UX Guardrails

- No ad appears during active gameplay.
- No ad appears immediately after every run.
- First-run onboarding is ad-free.
- Premium messaging is factual and calm.
- Reward presentation must avoid fake scarcity, countdown pressure, flashing jackpot visuals, or deceptive “almost won” framing.

## Accessibility Guardrails

- Text and HUD contrast must remain readable in every shipped theme.
- Sound, music, vibration, reduced-effects, and high-contrast options are first-class settings.
- Game-over, pause, and retry actions must stay reachable on compact phones.
- Moment-to-moment cues must not depend on audio alone.

## Data Guardrails

- Save data must survive normal restarts.
- Process death must not corrupt profile, stats, or entitlement state.
- A malformed or partial save must recover to the last valid state where possible; otherwise it falls back to safe defaults without crashing.
- Duplicate reward grants must be prevented with persisted idempotency records.

## Engineering Guardrails

- Reuse shared systems instead of building per-game copies.
- Avoid architecture churn for preference alone.
- Keep gameplay logic deterministic where tests need it.
- Keep rendering and animation lightweight enough for lower-end devices.
