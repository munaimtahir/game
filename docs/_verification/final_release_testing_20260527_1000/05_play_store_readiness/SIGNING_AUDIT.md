# Signing Audit

- **Keystore status:** A local keystore was generated to build the application for test.
- **Git status:** `key.properties` and `.keystore` files MUST be added to `.gitignore` (which they likely are, or must not be staged).
- **CI Signing:** The GitHub CI generates a dummy keystore dynamically to verify the bundle builds successfully. For production release, CI will need to use Base64-encoded Keystore strings mapped via GitHub Secrets.
