# Hardcoded Color Review

This document summarizes the findings of a review of hardcoded colors in the codebase after the "Soft Arcade Light" theme implementation.

## Search Queries

The following search queries were used to find hardcoded colors:
- `Color(0x`
- `Color.Black`
- `Color.White`
- `#" ` in XML files

## Findings

### `Color(0x...`

No instances of hardcoded colors using the `Color(0x...` format were found in the application source code. The only previous instances in `StackDropEngine.kt` were updated as part of this theme refactoring.

### `Color.Black`

The only usage of `Color.Black` is in `core/ui/src/main/java/com/vexel/offlinearcade/core/ui/Theme.kt` for the `LightOverlayScrim`:

```kotlin
private val LightOverlayScrim = Color.Black.copy(alpha = 0.38f)
```

This is an intentional use to create a translucent black overlay, as specified in the project requirements. This is **acceptable**.

### `Color.White`

`Color.White` is used in `core/ui/src/main/java/com/vexel/offlinearcade/core/ui/AppScaffold.kt` for text on the `HeroPanel` and `SplashShell`. These components use a dark `heroGradient` background, so white text is necessary for contrast.

- `HeroPanel`: Text color is `Color.White` for high contrast against the `heroGradient`.
- `SplashShell`: Text color is `Color.White` for the "OMA" text in the logo box, which has a `heroGradient` background.

These usages are **acceptable** as they ensure readability on dark, high-contrast backgrounds.

### Hardcoded Hex Values in XML

No hardcoded hex color values (e.g., `#FFFFFF`) were found in XML files within the `res` directory. All colors are referenced via `@color` resources.

## Conclusion

The review found that the codebase is largely free of hardcoded colors. The remaining instances of `Color.Black` and `Color.White` are intentional and necessary for the desired UI design and readability. No further action is required at this time.
