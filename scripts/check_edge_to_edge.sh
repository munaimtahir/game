#!/bin/bash
# Regression guard script to ensure deprecated status/navigation bar and layout cutout flags are not reintroduced.

FORBIDDEN_WORDS=(
    "statusBarColor"
    "navigationBarColor"
    "setStatusBarColor"
    "setNavigationBarColor"
    "getStatusBarColor"
    "getNavigationBarColor"
    "navigationBarDividerColor"
    "LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES"
    "windowLayoutInDisplayCutoutMode"
    "SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN"
    "SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION"
    "SYSTEM_UI_FLAG_FULLSCREEN"
    "SYSTEM_UI_FLAG_IMMERSIVE"
    "SYSTEM_UI_FLAG_IMMERSIVE_STICKY"
    "fitsSystemWindows"
)

# Allowlist file path
ALLOWLIST_FILE="scripts/edge_to_edge_allowlist.txt"

# Directories to search
SEARCH_DIRS=("app" "core" "feature" "game")

FAILED=0

# Create allowlist if it does not exist
if [ ! -f "$ALLOWLIST_FILE" ]; then
    touch "$ALLOWLIST_FILE"
fi

echo "Checking for legacy/deprecated system UI / edge-to-edge usages..."

for WORD in "${FORBIDDEN_WORDS[@]}"; do
    # Search in directories, filtering only .kt, .java, and .xml files
    # Exclude build directories and allowlist hits
    RESULTS=$(grep -rnw --include=\*.{kt,java,xml} --exclude-dir=build "$WORD" "${SEARCH_DIRS[@]}" 2>/dev/null)
    
    if [ ! -z "$RESULTS" ]; then
        # Read line by line and filter based on allowlist
        while IFS= read -r LINE; do
            FILE_PATH=$(echo "$LINE" | cut -d: -f1)
            LINE_NUM=$(echo "$LINE" | cut -d: -f2)
            CONTENT=$(echo "$LINE" | cut -d: -f3-)
            
            # Check if this exact file and line or match is in allowlist
            IS_ALLOWED=0
            if grep -q "$FILE_PATH" "$ALLOWLIST_FILE" 2>/dev/null; then
                IS_ALLOWED=1
            fi
            
            if [ $IS_ALLOWED -eq 0 ]; then
                echo "ERROR: Deprecated reference '$WORD' found at $FILE_PATH:$LINE_NUM -> $CONTENT"
                FAILED=1
            fi
        done <<< "$RESULTS"
    fi
done

if [ $FAILED -ne 0 ]; then
    echo "Check FAILED. Please remove deprecated APIs or update $ALLOWLIST_FILE if an exception is strictly required."
    exit 1
else
    echo "Check PASSED. No deprecated edge-to-edge usages found."
    exit 0
fi
