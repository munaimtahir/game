# Shared Responsive Layout Rules

## Components
1. **`ArcadeScaffold`**: Used for dashboard and meta screens. Handles safe areas and scrollable lists naturally.
2. **`GameplayScaffold`**: (NEW) A specialized layout specifically for active gameplay. It separates the top HUD from the main playfield, includes a slot for bottom controls, and handles safe insets while preventing scrolling so the board dominates the screen.
3. **`ArcadeCard`**: Updated to explicitly define its `contentColor` to ensure text is visible against `elevatedCardBackground`.
4. **`PremiumButton`**: Disabled styles and padding were updated for consistent height and clipping prevention.
5. **`HeroPanel`**: Updated with a `BoxWithConstraints`. If `maxWidth < 380.dp`, it switches from a `Row` to a `Column` to prevent the large display text from stacking vertically, a key issue on small phones.
6. **`SplashShell`**: Added correct padding, explicit `TextPrimary` and `TextSecondary` colors, and a drop shadow to the logo box.

## Responsive Rules
- **Hero Text**: Avoid stacking by utilizing `BoxWithConstraints` and switching to vertical layouts on narrow width.
- **Buttons**: Provide max lines and avoid long button labels on cards. 
- **In-Game Text**: Explicitly provided `TextPrimary`, `TextSecondary`, and `TextMuted` via `ArcadeTheme.colors` instead of implicitly trusting `MaterialTheme` surfaces.
- **Insets**: Handled directly in `GameplayScaffold` and `ArcadeScaffold` to ensure navigation gestures and status bars do not clip primary actions.
