# Play Store Readiness Checklist

- [x] Application ID (`com.vexel.offlinearcade`) is set.
- [x] App Label (`@string/app_name`) is set.
- [x] Version Code is 1.
- [x] Version Name is 0.1.0.
- [x] Min SDK is 24 (acceptable).
- [x] Target SDK is 35 (meets current Google Play requirement).
- [x] Compile SDK is 35.
- [x] Release build succeeds (`./gradlew assembleRelease`).
- [x] AAB is generated (`./gradlew bundleRelease`).
- [x] Signing Config is parameterized via `key.properties` / environment variables.
- [x] No debug signing committed into repository source (the generated `release.keystore` will not be committed).
- [x] Proguard/R8 shrinking is enabled.
