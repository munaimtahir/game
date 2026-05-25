# MCP Security Guardrails

## Rules for Agents
- **NO SECRETS**: Never read or modify `key.properties` or keystore files.
- **NO REMOTE EXECUTION**: Do not start generic remote shells or download arbitrary scripts.
- **RESTRICTED WRITE**: Do not modify CI workflow permissions to escalate privileges.
- **LIMITED INTERNET**: Do not download external assets or connect to third-party endpoints outside of standard Gradle build resolutions.
- **ISOLATED ADB**: Do not run ADB commands against physical user devices if connected; only use emulators during CI.
