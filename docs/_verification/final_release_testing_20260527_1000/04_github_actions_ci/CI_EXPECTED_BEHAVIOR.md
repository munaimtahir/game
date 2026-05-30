# CI Expected Behavior

- **Triggers**: All three new workflows run on `push` to `main`, `pull_request` to `main`, and `workflow_dispatch`.
- **Concurrency**: Workflows are grouped by ref and will cancel in-progress runs to save resources.
- **Secrets**: Currently, release CI generates a dummy debug keystore. For production Play Store uploading, GitHub Actions Secrets would need to be populated and referenced, but the current configuration is enough to prove release compilation works.
- **Outputs**:
  - `android-code-ci`: Uploads `test-results` and `lint-results`.
  - `android-runtime-emulator-ci`: Uploads `connected-test-results`.
  - `android-release-readiness`: Uploads `release-bundle` (.aab) and `release-apk` (.apk).
