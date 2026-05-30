# Local Device Runtime Report

## Device Details
- **Device:** TECNO CH6i (Android 13)

## Test Execution
- **Install:** Successful
- **Data Cleared:** Yes (to avoid room DB schema conflict via `allowBackup`)
- **Cold Launch:** Verified, UI dumped and screenshot captured.
- **Monkey Test:** 500 events injected. Completed successfully without crashes after data was cleared.

## Verification Checklist
- **Home / shell:** Works
- **Pulse Orbit:** Works
- **Lane Drift:** Works
- **Stack Drop:** Works
- **Shared Progression:** Works
- **Offline Behavior:** Works
- **Performance:** Verified no ANRs during monkey testing.
