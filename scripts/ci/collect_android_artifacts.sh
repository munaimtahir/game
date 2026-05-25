#!/usr/bin/env bash
set -e
mkdir -p artifacts/test-results artifacts/reports artifacts/lint artifacts/apk
find . -path "*/build/reports/*" -type f -o -path "*/build/outputs/*" -type f | sed 's#^\./##' > artifacts/reports/report-file-index.txt || true
find . -path "*/build/reports/tests/*" -type f -exec cp --parents {} artifacts/test-results/ \; || true
find . -path "*/build/reports/androidTests/*" -type f -exec cp --parents {} artifacts/test-results/ \; || true
find . -path "*/build/reports/lint-results*" -type f -exec cp --parents {} artifacts/lint/ \; || true
find . -path "*/build/outputs/apk/debug/*.apk" -type f -exec cp {} artifacts/apk/ \; || true
