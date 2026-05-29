# Experimental Games Scope Lock

The release build is limited strictly to the locked MVP 3 games: Pulse Orbit, Lane Drift, and Stack Drop. 
The three experimental games were handled as follows:

* **Brick Volley:** Hidden from public UI. Isolated inside `BuildConfig.DEBUG` route configuration in `ArcadeNavHost.kt`. Code remains as experimental.
* **Loop Snake:** Hidden from public UI. Isolated inside `BuildConfig.DEBUG` route configuration in `ArcadeNavHost.kt`. Code remains as experimental.
* **Shield Dash:** Hidden from public UI. Isolated inside `BuildConfig.DEBUG` route configuration in `ArcadeNavHost.kt`. Code remains as experimental.

All references in `GameId` enum, `ArcadeSkinCatalog` list, daily challenge generators, and statistics cards have been trimmed to contain only the 3 MVP games.
