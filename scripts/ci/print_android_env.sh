#!/usr/bin/env bash
set -e

echo "=== System Env ==="
uname -a
echo "=== Java Version ==="
java -version
echo "=== Gradle Version ==="
./gradlew --version
echo "=== Android SDK ==="
echo "ANDROID_HOME=$ANDROID_HOME"
