# Permissions Audit

## Permissions found in `AndroidManifest.xml`
- `android.permission.VIBRATE`: Required for in-game haptics. Justified and acceptable.
- *No internet permission found*. The application is entirely offline and will function properly.

## Data Safety Notes
- No tracking SDKs.
- No network connections.
- Data is stored purely locally (DataStore / Room). No remote sync.
- The app should be marked as "Does not collect data" in Google Play Data Safety form.
