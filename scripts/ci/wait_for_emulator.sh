#!/usr/bin/env bash
set -e
echo "Waiting for emulator..."
timeout 300 adb wait-for-device shell 'while [[ -z $(getprop sys.boot_completed) ]]; do sleep 2; done'
echo "Emulator boot complete."
