#!/usr/bin/env bash
set -euo pipefail

: "${ADB:=adb}"
: "${ARTIFACT_ROOT:=artifacts}"

ui_dump_current() {
    local dump_path="${1:-${ARTIFACT_ROOT}/logs/ui-dump.xml}"
    mkdir -p "$(dirname "$dump_path")"
    "$ADB" shell uiautomator dump /sdcard/window_dump.xml >/dev/null
    "$ADB" pull /sdcard/window_dump.xml "$dump_path" >/dev/null
    echo "$dump_path"
}

ui_find_bounds_for_text() {
    local xml_path="$1"
    local needle="$2"
    local line
    line="$(grep -m1 -F "text=\"$needle\"" "$xml_path" || true)"
    if [[ -z "$line" ]]; then
        line="$(grep -m1 -F "content-desc=\"$needle\"" "$xml_path" || true)"
    fi
    if [[ -z "$line" ]]; then
        return 1
    fi

    local bounds
    bounds="$(printf '%s\n' "$line" | sed -n 's/.*bounds=\"\[\([0-9]\+\),\([0-9]\+\)\]\[\([0-9]\+\),\([0-9]\+\)\]\".*/\1 \2 \3 \4/p')"
    if [[ -z "$bounds" ]]; then
        return 1
    fi
    printf '%s\n' "$bounds"
}

ui_tap_text() {
    local needle="$1"
    local xml_path="${2:-}"
    if [[ -z "$xml_path" ]]; then
        xml_path="$(ui_dump_current)"
    fi
    local bounds
    bounds="$(ui_find_bounds_for_text "$xml_path" "$needle")"
    read -r x1 y1 x2 y2 <<< "$bounds"
    local x=$(((x1 + x2) / 2))
    local y=$(((y1 + y2) / 2))
    "$ADB" shell input tap "$x" "$y"
}

ui_wait_for_text() {
    local needle="$1"
    local timeout_seconds="${2:-20}"
    local elapsed=0
    while [[ "$elapsed" -lt "$timeout_seconds" ]]; do
        local xml_path
        xml_path="$(ui_dump_current)"
        if grep -q -F "text=\"$needle\"" "$xml_path" || grep -q -F "content-desc=\"$needle\"" "$xml_path"; then
            return 0
        fi
        sleep 1
        elapsed=$((elapsed + 1))
    done
    echo "Timed out waiting for UI text: $needle" >&2
    return 1
}

ui_wait_for_any_text() {
    local timeout_seconds="${1:-20}"
    shift
    local elapsed=0
    while [[ "$elapsed" -lt "$timeout_seconds" ]]; do
        local xml_path
        xml_path="$(ui_dump_current)"
        for needle in "$@"; do
            if grep -q -F "text=\"$needle\"" "$xml_path" || grep -q -F "content-desc=\"$needle\"" "$xml_path"; then
                printf '%s\n' "$needle"
                return 0
            fi
        done
        sleep 1
        elapsed=$((elapsed + 1))
    done
    echo "Timed out waiting for UI text set: $*" >&2
    return 1
}

ui_return_to_home() {
    local attempts="${1:-3}"
    local i=0
    while [[ "$i" -lt "$attempts" ]]; do
        local xml_path
        xml_path="$(ui_dump_current)"
        if grep -q -F "text=\"Lane Drift\"" "$xml_path" && grep -q -F "text=\"Pulse Orbit\"" "$xml_path" && grep -q -F "text=\"Stack Drop\"" "$xml_path"; then
            return 0
        fi
        "$ADB" shell input keyevent 4 || true
        sleep 1
        i=$((i + 1))
    done
    return 1
}
