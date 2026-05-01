# Current Navigation Map

## Routes and Destinations
The app uses Jetpack Compose Navigation via `NavHost` defined in `ArcadeNavHost.kt`.
The defined routes include:
- `Routes.Home` (Start Destination)
- `Routes.PulseOrbit`
- `Routes.LaneDrift`
- `Routes.StackDrop`
- `Routes.Challenges`, `Routes.Stats`, `Routes.Settings`

## Home Screen
The `HomeScreen` (in `feature/home/src/.../HomeScreen.kt`) does **not** host active gameplay. It is already functioning as a launcher and dashboard. 
It displays `GameEntryCard`s for each game. When a card's "Play" or "Continue" button is tapped, it invokes a callback (`onPulseOrbit`, etc.) which triggers `navController.navigate(Routes.PulseOrbit)`.

## The Core Issue
The prompt asks to ensure games open into a dedicated full-screen gameplay screen instead of playing inside the home screen or shared screen area.
While the games *are* on separate routes technically, they do not appear as full-screen dedicated experiences because `PulseOrbitScreen`, `LaneDriftScreen`, and `StackDropScreen` are all wrapped in `ArcadeScaffold`.
`ArcadeScaffold` (in `AppScaffold.kt`) is a shared UI component that provides a `TopAppBar` (with a back button and title) and injects significant padding (`horizontal = spacing.lg, vertical = spacing.md`) around the content. 
This causes the games to feel like they are rendering inside a generic app content area rather than a dedicated full-screen game canvas.

## Refactor Target
We must remove `ArcadeScaffold` from the three game screens and replace it with a true full-screen layout (e.g., `Box` with `Modifier.fillMaxSize()`), while properly handling `WindowInsets` for safe areas, implementing custom pause/back controls, and managing the `BackHandler` for back navigation logic.