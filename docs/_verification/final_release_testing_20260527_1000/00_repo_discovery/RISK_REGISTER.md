# Risk Register

## Application Risks
- **Crash Risks:** Games running continuously might cause OOM, memory leaks.
- **Gameplay Logic Risks:** Frame drops could cause skipped collisions.
- **Offline Risks:** Shared progression / DataStore could fail to save or sync if device reboots abruptly.
- **Persistence Risks:** Datastore corruption.
- **Play Store Rejection Risks:** Missing privacy policy, missing permissions justification, or crash on launch.
- **CI Instability Risks:** Emulator tests on GitHub actions might be flaky.
- **Low-End Performance Risks:** Compose UI animations may drop frames.
- **Release Signing Risks:** Missing keys or key properties might block release builds.
