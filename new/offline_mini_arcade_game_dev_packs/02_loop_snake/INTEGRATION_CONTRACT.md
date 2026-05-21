# Loop Snake — Integration Contract

## Navigation
Add game to existing game registry/list using the project's current architecture.

Required route/screen support:
- Game info/details screen.
- Play screen.
- Result or game-over state.
- Back navigation to arcade/home.

## Shared progression integration
Game must connect to:
- High score tracking.
- Session count.
- Local stats page.
- Daily challenge tracking.
- Soft currency reward system.
- Theme/cosmetic compatibility.
- Fast restart.

## Suggested game ID
`loop_snake`

## Suggested stats keys
- `loop_snake_best_score`
- `loop_snake_sessions`
- `loop_snake_total_play_time`
- `loop_snake_daily_progress`
- `loop_snake_secondary_stat`

## Data persistence
- Use existing local persistence method.
- No internet dependency.
- No new account requirement.
- No destructive migration.
- If adding schema/preferences keys, document them in `copilot_session.md`.

## Theming
- Use existing app color/theme system.
- Support default theme.
- Use semantic colors where possible:
  - Player/object
  - Hazard
  - Pickup/reward
  - Background
  - Accent/combo

## Monetization boundary
- No ad during active gameplay.
- Result-screen ad hooks only if existing app already has restrained cadence.
- Premium users must not see ads.
- Do not block restart behind ads.

## Compatibility
- Must compile with current Android/Gradle setup.
- Must not introduce heavy third-party dependencies without explicit justification.
- Must not break existing games.
