#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
ARTIFACT_ROOT="$ROOT_DIR/docs/_implementation/brick_volley_finalization/device_artifacts"
STAMP="$(date +%Y%m%d_%H%M%S)"
OUT_DIR="$ARTIFACT_ROOT/playability_${STAMP}"
mkdir -p "$OUT_DIR/screenshots"

ADB_BIN="${ADB_BIN:-adb}"
SERIAL="${ANDROID_SERIAL:-}"
APP_ID="com.vexel.offlinearcade"
APP_APK="$ROOT_DIR/app/build/outputs/apk/debug/app-debug.apk"

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
log() { printf '%s\n' "$*" | tee -a "$OUT_DIR/test_output.txt"; }

capture_screen() {
  local name="$1"
  "${ADB[@]}" exec-out screencap -p > "$OUT_DIR/screenshots/${name}.png"
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
        targets.append((cx,cy))
    if n.attrib.get("clickable")=="true":
        clicks.append((cx,cy))
if not targets or not clicks:
    print("NOT_FOUND")
    sys.exit(0)
best=None
for tx,ty in targets:
    for cx,cy in clicks:
        dy=cy-ty
        if dy < 20 or dy > 700:
            continue
        if abs(cx-tx) > 320:
            continue
        score=dy + abs(cx-tx)*0.4
        if best is None or score<best[0]:
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

play_turn() {
  local size W H SX SY EX EY
  size="$("${ADB[@]}" shell wm size | tr -d '\r' | awk -F': ' '/Physical size/{print $2}')"
  W="${size%x*}"
  H="${size#*x}"
  SX=$((W/2))
  SY=$((H-140))
  EX=$((W/2-80))
  EY=$((H-18))
  "${ADB[@]}" shell input swipe "$SX" "$SY" "$EX" "$EY" 220
}

open_game_from_home() {
  local game="$1"
  for _ in 1 2 3 4; do
    "${ADB[@]}" shell input swipe 540 980 540 1950 220 || true
  done
  local c="NOT_FOUND"
  for _ in 1 2 3 4 5 6 7 8; do
    c="$(game_play_coords "$game")"
    if [[ "$c" == "NOT_FOUND" ]]; then
      c="$(tap_text "$game")"
    fi
    if [[ "$c" != "NOT_FOUND" ]]; then
      break
    fi
    "${ADB[@]}" shell input swipe 540 1950 540 980 260 || true
    sleep 1
  done
  if [[ "$c" == "NOT_FOUND" ]]; then
    log "FAIL: unable to locate $game on home"
    return 1
  fi
  "${ADB[@]}" shell input tap ${c}
  sleep 2
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

log "Build + install debug APK"
( cd "$ROOT_DIR" && ./gradlew :app:assembleDebug --no-daemon --console=plain ) >> "$OUT_DIR/test_output.txt" 2>&1
"${ADB[@]}" install -r -t "$APP_APK" >> "$OUT_DIR/test_output.txt" 2>&1
"${ADB[@]}" shell pm path "$APP_ID" > "$OUT_DIR/installed_apk_info.txt"

log "Launch app"
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
  exit 1
fi
"${ADB[@]}" shell input tap ${coords}
sleep 2
capture_screen "02_detail"

start_coords="NOT_FOUND"
for _ in 1 2 3 4 5; do
  start_coords="$(tap_text 'Start Game')"
  if [[ "$start_coords" != "NOT_FOUND" ]]; then
    break
  fi
  "${ADB[@]}" shell input swipe 540 1950 540 980 260 || true
  sleep 1
done
if [[ "$start_coords" == "NOT_FOUND" ]]; then
  log "FAIL: Start Game not found on detail screen"
  exit 1
fi
"${ADB[@]}" shell input tap ${start_coords}
sleep 2
for _ in 1 2 3 4 5 6 7 8 9 10; do
  root_state="$(ui_query 'Brick Volley Root' state)"
  aim_present="$(ui_query 'Brick Volley Aim Area' center)"
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

capture_screen "03_ready_gameplay_canvas"

before_state="$root_state"
read -r AX AY <<<"$(ui_query 'Brick Volley Aim Area' bounds)"
size="$("${ADB[@]}" shell wm size | tr -d '\r' | awk -F': ' '/Physical size/{print $2}')"
W="${size%x*}"
H="${size#*x}"
SX=$((W/2))
SY=$((H-180))
EX=$((W/2-110))
EY=$((H-40))
log "Performing launch gesture"
"${ADB[@]}" shell input swipe "$SX" "$SY" "$EX" "$EY" 260

after_state="$before_state"
for _ in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15; do
  after_state="$(ui_query 'Brick Volley Root' state)"
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

round_text="$(ui_query 'Brick Volley Round' text)"
if [[ "$round_text" == "NOT_FOUND" ]]; then
  log "FAIL: round text missing in gameplay"
  dump_ui
  exit 1
fi
ROUND_NUM="$(python3 - "$round_text" <<'PY'
import re,sys
m=re.search(r'(\d+)', sys.argv[1])
print(m.group(1) if m else "")
PY
)"
if [[ -z "$ROUND_NUM" ]]; then
  log "FAIL: could not parse initial round number"
  dump_ui
  exit 1
fi

for turn in 1 2 3; do
  log "Turn $turn launch"
  "${ADB[@]}" shell input swipe "$SX" "$SY" "$EX" "$EY" 260
  next_round=$((ROUND_NUM + 1))
  current_round=""
  round_text=""
  for _ in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18; do
    round_text="$(ui_query 'Brick Volley Round' text)"
    current_round="$(python3 - "$round_text" <<'PY'
import re,sys
m=re.search(r'(\d+)', sys.argv[1])
print(m.group(1) if m else "")
PY
)"
    if [[ "$current_round" == "$next_round" ]]; then
      break
    fi
    if [[ "$(ui_query 'Brick Volley Restart' center)" != "NOT_FOUND" ]]; then
      break
    fi
    sleep 1
  done
  if [[ "$current_round" != "$next_round" ]]; then
    if [[ "$(ui_query 'Brick Volley Restart' center)" != "NOT_FOUND" ]]; then
      c="$(tap_text 'Restart')"
      if [[ "$c" == "NOT_FOUND" ]]; then
        log "FAIL: restart button visible but could not be tapped"
        dump_ui
        exit 1
      fi
      "${ADB[@]}" shell input tap ${c}
      sleep 1
      capture_screen "restart_after_gameover"
      break
    fi
    log "FAIL: expected round $next_round after turn $turn, saw '$round_text'"
    dump_ui
    exit 1
  fi
  ROUND_NUM="$current_round"
  capture_screen "turn_${turn}"
done

log "Verifying existing games still open"
"${ADB[@]}" shell am force-stop "${APP_ID}" >/dev/null 2>&1 || true
"${ADB[@]}" shell am start -n "${APP_ID}/.MainActivity" >/dev/null 2>&1 || true
sleep 3

for game in "Pulse Orbit" "Lane Drift" "Stack Drop"; do
  "${ADB[@]}" shell am force-stop "${APP_ID}" >/dev/null 2>&1 || true
  "${ADB[@]}" shell am start -n "${APP_ID}/.MainActivity" >/dev/null 2>&1 || true
  sleep 2
  open_game_from_home "$game"
  capture_screen "open_${game// /_}"
done

"${ADB[@]}" shell uiautomator dump >/dev/null
"${ADB[@]}" pull /sdcard/window_dump.xml "$OUT_DIR/ui_dump.xml" >/dev/null
"${ADB[@]}" logcat -d > "$OUT_DIR/logcat.txt" || true
rm -f "$OUT_DIR/_ui_tmp.xml"

log "PASS: Brick Volley playability run completed"
log "Artifacts: $OUT_DIR"
