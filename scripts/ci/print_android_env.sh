#!/usr/bin/env bash
set -euo pipefail

echo "=== System Env ==="
uname -a
echo "=== Java Version ==="
java -version
echo "=== Gradle Version ==="
./gradlew --version
echo "=== Android SDK ==="
echo "ANDROID_HOME=$ANDROID_HOME"
echo "ANDROID_SDK_ROOT=${ANDROID_SDK_ROOT:-}"
echo "=== ADB Version ==="
adb version || true
