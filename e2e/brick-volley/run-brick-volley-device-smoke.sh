#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
ARTIFACT_ROOT="$ROOT_DIR/docs/_implementation/brick_volley_finalization/device_artifacts"
STAMP="$(date +%Y%m%d_%H%M%S)"
OUT_DIR="$ARTIFACT_ROOT/smoke_${STAMP}"
mkdir -p "$OUT_DIR/screenshots"

ADB_BIN="${ADB_BIN:-adb}"
SERIAL="${ANDROID_SERIAL:-}"

pick_device() {
  if [[ -n "$SERIAL" ]]; then
    echo "$SERIAL"
    return
  fi
  "$ADB_BIN" devices | awk 'NR>1 && $2=="device" {print $1; exit}'
}

DEVICE="$(pick_device)"
if [[ -z "$DEVICE" ]]; then
  echo "FAIL: no connected adb device"
  exit 1
fi

ADB=( "$ADB_BIN" -s "$DEVICE" )
APP_ID="com.vexel.offlinearcade"
APP_APK="$ROOT_DIR/app/build/outputs/apk/debug/app-debug.apk"

log() { printf '%s\n' "$*" | tee -a "$OUT_DIR/test_output.txt"; }

capture_screen() {
  local name="$1"
  "${ADB[@]}" exec-out screencap -p > "$OUT_DIR/screenshots/${name}.png"
}

dump_ui() {
  "${ADB[@]}" shell uiautomator dump >/dev/null
  "${ADB[@]}" pull /sdcard/window_dump.xml "$OUT_DIR/ui_dump.xml" >/dev/null
}

tap_text() {
  local text="$1"
  "${ADB[@]}" shell uiautomator dump >/dev/null
  "${ADB[@]}" pull /sdcard/window_dump.xml "$OUT_DIR/_ui_tmp.xml" >/dev/null
  python3 - "$OUT_DIR/_ui_tmp.xml" "$text" <<'PY'
import re,sys,xml.etree.ElementTree as ET
path,needle=sys.argv[1],sys.argv[2].lower()
root=ET.parse(path).getroot()
best=None
for n in root.iter("node"):
    txt=(n.attrib.get("text") or "").strip()
    desc=(n.attrib.get("content-desc") or "").strip()
    blob=f"{txt} {desc}".lower()
    if needle not in blob:
        continue
    b=n.attrib.get("bounds","")
    m=re.match(r"\[(\d+),(\d+)\]\[(\d+),(\d+)\]",b)
    if not m:
        continue
    x1,y1,x2,y2=map(int,m.groups())
    if x2 <= x1 or y2 <= y1:
        continue
    area=(x2-x1)*(y2-y1)
    if best is None or area<best[0]:
        best=(area,(x1+x2)//2,(y1+y2)//2)
if best is None:
    print("NOT_FOUND")
else:
    print(f"{best[1]} {best[2]}")
PY
}

game_play_coords() {
  local game="$1"
  "${ADB[@]}" shell uiautomator dump >/dev/null
  "${ADB[@]}" pull /sdcard/window_dump.xml "$OUT_DIR/_ui_tmp.xml" >/dev/null
  python3 - "$OUT_DIR/_ui_tmp.xml" "$game" <<'PY'
import re,sys,xml.etree.ElementTree as ET
path,game=sys.argv[1],sys.argv[2].lower()
root=ET.parse(path).getroot()
def parse_bounds(b):
    m=re.match(r"\[(\d+),(\d+)\]\[(\d+),(\d+)\]",b or "")
    return tuple(map(int,m.groups())) if m else None

targets=[]
clicks=[]
for n in root.iter("node"):
    b=parse_bounds(n.attrib.get("bounds",""))
    if not b:
        continue
    x1,y1,x2,y2=b
    cx=(x1+x2)//2
    cy=(y1+y2)//2
    txt=(n.attrib.get("text") or "").strip().lower()
    desc=(n.attrib.get("content-desc") or "").strip().lower()
    if game in f"{txt} {desc}":
        targets.append((cx,cy,x1,y1,x2,y2))
    if n.attrib.get("clickable")=="true":
        clicks.append((cx,cy,x1,y1,x2,y2))
if not targets or not clicks:
    print("NOT_FOUND")
    sys.exit(0)

best=None
for tx,ty,tx1,ty1,tx2,ty2 in targets:
    for cx,cy,_,_,_,_ in clicks:
        dy=cy-ty
        if dy < 20 or dy > 700:
            continue
        if abs(cx-tx) > 320:
            continue
        score=dy + abs(cx-tx)*0.4
        if best is None or score < best[0]:
            best=(score,cx,cy)
if best is None:
    print("NOT_FOUND")
else:
    print(f"{int(best[1])} {int(best[2])}")
PY
}

ui_query() {
  local label="$1"
  local field="${2:-center}"
  "${ADB[@]}" shell uiautomator dump >/dev/null
  "${ADB[@]}" pull /sdcard/window_dump.xml "$OUT_DIR/_ui_tmp.xml" >/dev/null
  python3 - "$OUT_DIR/_ui_tmp.xml" "$label" "$field" <<'PY'
import re,sys,xml.etree.ElementTree as ET
path,label,field=sys.argv[1],sys.argv[2].lower(),sys.argv[3]
root=ET.parse(path).getroot()
def parse_bounds(b):
    m=re.match(r"\[(\d+),(\d+)\]\[(\d+),(\d+)\]",b or "")
    return tuple(map(int,m.groups())) if m else None
for n in root.iter("node"):
    txt=(n.attrib.get("text") or "").strip().lower()
    desc=(n.attrib.get("content-desc") or "").strip().lower()
    res=(n.attrib.get("resource-id") or "").strip().lower()
    blob=f"{txt} {desc} {res}"
    if label not in blob:
        continue
    if field == "text":
        val = (n.attrib.get("text") or "").strip()
        if val:
            print(val)
            sys.exit(0)
    elif field == "state":
        val = (n.attrib.get("state-description") or n.attrib.get("stateDescription") or n.attrib.get("content-desc") or "").strip()
        if val:
            print(val)
            sys.exit(0)
    elif field == "bounds":
        b = parse_bounds(n.attrib.get("bounds", ""))
        if b:
            print(f"{(b[0]+b[2])//2} {(b[1]+b[3])//2}")
            sys.exit(0)
    else:
        print("FOUND")
        sys.exit(0)
print("NOT_FOUND")
PY
}

log "Device: $DEVICE"
{
  "${ADB[@]}" shell getprop ro.product.model
  "${ADB[@]}" shell getprop ro.build.version.release
} > "$OUT_DIR/device_info.txt"

log "Preparing device settings (best effort)"
"${ADB[@]}" shell svc power stayon true || true
"${ADB[@]}" shell settings put system screen_off_timeout 1800000 || true
"${ADB[@]}" shell input keyevent KEYCODE_WAKEUP || true
"${ADB[@]}" shell input keyevent 82 || true
"${ADB[@]}" shell settings put global window_animation_scale 0 || true
"${ADB[@]}" shell settings put global transition_animation_scale 0 || true
"${ADB[@]}" shell settings put global animator_duration_scale 0 || true

log "Building debug apk"
( cd "$ROOT_DIR" && ./gradlew :app:assembleDebug --no-daemon --console=plain ) >> "$OUT_DIR/test_output.txt" 2>&1

log "Installing app"
"${ADB[@]}" install -r -t "$APP_APK" >> "$OUT_DIR/test_output.txt" 2>&1
"${ADB[@]}" shell pm path "$APP_ID" > "$OUT_DIR/installed_apk_info.txt"

log "Launching app"
"${ADB[@]}" shell am start -n "${APP_ID}/.MainActivity" >> "$OUT_DIR/test_output.txt" 2>&1
sleep 3
capture_screen "01_home"

for _ in 1 2 3 4; do
  "${ADB[@]}" shell input swipe 540 980 540 1950 220 || true
done

coords="NOT_FOUND"
for _ in 1 2 3 4 5 6 7 8; do
  coords="$(game_play_coords 'Brick Volley')"
  if [[ "$coords" != "NOT_FOUND" ]]; then
    break
  fi
  "${ADB[@]}" shell input swipe 540 1950 540 980 260 || true
  sleep 1
done
if [[ "$coords" == "NOT_FOUND" ]]; then
  log "FAIL: Could not find Brick Volley entry"
  dump_ui
  exit 1
fi
"${ADB[@]}" shell input tap ${coords}
sleep 2
capture_screen "02_brick_volley_detail"

coords="NOT_FOUND"
for _ in 1 2 3 4 5 6 7 8 9 10; do
  coords="$(tap_text 'brick_volley_start_button')"
  if [[ "$coords" != "NOT_FOUND" ]]; then
    break
  fi
  "${ADB[@]}" shell input swipe 540 1950 540 980 260 || true
  sleep 1
done
if [[ "$coords" == "NOT_FOUND" ]]; then
  log "FAIL: Could not find Start Game button"
  dump_ui
  exit 1
fi
"${ADB[@]}" shell input tap ${coords}
sleep 2
for _ in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20; do
  root_state="$(ui_query 'BrickVolleyRoot' state)"
  aim_present="$(ui_query 'brick_volley_aim_area' center)"
  if [[ "$root_state" != "NOT_FOUND" && "$aim_present" != "NOT_FOUND" ]]; then
    break
  fi
  sleep 1
done
if [[ "$root_state" == "NOT_FOUND" || "$aim_present" == "NOT_FOUND" ]]; then
  log "FAIL: BrickVolleyRoot/BrickVolleyAimArea not present after Start Game; likely still on Game Info/detail page"
  dump_ui
  exit 1
fi
if [[ "$root_state" == *"Game Info"* || "$root_state" == *"detail"* || "$root_state" == *"Detail"* ]]; then
  log "FAIL: gameplay markers indicate detail page instead of gameplay"
  dump_ui
  exit 1
fi

capture_screen "03_ready_gameplay_canvas"

read -r AX AY <<<"$(ui_query 'brick_volley_aim_area' bounds)"
if [[ -z "${AX:-}" || -z "${AY:-}" ]]; then
  log "FAIL: could not resolve aim area center"
  dump_ui
  exit 1
fi
size="$("${ADB[@]}" shell wm size | tr -d '\r' | awk -F': ' '/Physical size/{print $2}')"
W="${size%x*}"
H="${size#*x}"
SX=$((W/2))
SY=$((H-180))
EX=$((W/2-110))
EY=$((H-40))

before_state="$root_state"
log "Performing launch gesture from aim area"
"${ADB[@]}" shell input swipe "$SX" "$SY" "$EX" "$EY" 260

after_state="$before_state"
for _ in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15; do
  after_state="$(ui_query 'BrickVolleyRoot' state)"
  if [[ "$after_state" != "$before_state" ]]; then
    break
  fi
  sleep 1
done
if [[ "$after_state" == "$before_state" ]]; then
  log "FAIL: Brick Volley gameplay state did not change after launch"
  dump_ui
  exit 1
fi
if [[ "$after_state" == *"Game Info"* ]]; then
  log "FAIL: Game Info page remained visible after launch"
  dump_ui
  exit 1
fi

capture_screen "04_after_launch_active_gameplay"

round_before="$(ui_query 'Round:' text)"
if [[ "$round_before" == "NOT_FOUND" ]]; then
  log "FAIL: round text missing in gameplay"
  dump_ui
  exit 1
fi

python3 - "$round_before" <<'PY' > "$OUT_DIR/_round_start.txt"
import re,sys
m=re.search(r'(\d+)', sys.argv[1])
print(m.group(1) if m else "")
PY
ROUND_NUM="$(cat "$OUT_DIR/_round_start.txt")"
if [[ -z "$ROUND_NUM" ]]; then
  log "FAIL: could not parse initial round number"
  dump_ui
  exit 1
fi

for turn in 1 2 3; do
  log "Turn $turn launch"
  "${ADB[@]}" shell input swipe "$SX" "$SY" "$EX" "$EY" 260
  next_round=$((ROUND_NUM + 1))
  round_text=""
  for _ in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18; do
    round_text="$(ui_query 'Round:' text)"
    current_round="$(python3 - "$round_text" <<'PY'
import re,sys
m=re.search(r'(\d+)', sys.argv[1])
print(m.group(1) if m else "")
PY
)"
    if [[ "$current_round" == "$next_round" ]]; then
      break
    fi
    sleep 1
  done
  if [[ "$current_round" != "$next_round" ]]; then
    log "FAIL: expected round $next_round after turn $turn, saw '$round_text'"
    dump_ui
    exit 1
  fi
  ROUND_NUM="$current_round"
  capture_screen "turn_${turn}"
done

dump_ui
"${ADB[@]}" logcat -d > "$OUT_DIR/logcat.txt" || true
rm -f "$OUT_DIR/_ui_tmp.xml" "$OUT_DIR/_round_start.txt"

log "PASS: Brick Volley smoke run completed"
log "Artifacts: $OUT_DIR"
