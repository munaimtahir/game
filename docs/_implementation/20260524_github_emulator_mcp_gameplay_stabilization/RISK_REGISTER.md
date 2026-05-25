# Risk Register

**Date**: May 24, 2026

1. **Emulator Instability**: Cloud emulators can be flaky (timeouts, boot failures).
   - *Mitigation*: Add retry mechanisms and explicit wait checks in the CI scripts.
2. **Performance**: CI environment uses nested virtualization (KVM); could be slow.
   - *Mitigation*: Run smoke tests minimally, disable animations, and use swiftshader_indirect.
3. **Testing Gameplay Programmatically**: AI/agents cannot "play" the game.
   - *Mitigation*: Focus on math unit tests (collision logic) and static ADB visual smoke checks.
