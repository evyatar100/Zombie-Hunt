#!/usr/bin/env bash
# Start Zombie Hunt. Default: virtual display + noVNC on :6080.
# Host X11: docker run -e ZH_DISPLAY_MODE=host -e DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix ...
set -euo pipefail
cd /app

run_game() {
  exec java -cp build/classes Demo3D
}

if [[ "${ZH_DISPLAY_MODE:-vnc}" == "host" ]]; then
  if [[ -z "${DISPLAY:-}" ]]; then
    echo "ZH_DISPLAY_MODE=host requires DISPLAY (e.g. -e DISPLAY)." >&2
    exit 1
  fi
  run_game
fi

export DISPLAY="${DISPLAY:-:99}"
SCREEN_SIZE="${SCREEN_SIZE:-1280x800x24}"

Xvfb "${DISPLAY}" -screen 0 "${SCREEN_SIZE}" -ac +extension RENDER -noreset >/tmp/xvfb.log 2>&1 &
for _ in $(seq 1 50); do
  if xdpyinfo -display "${DISPLAY}" >/dev/null 2>&1; then
    break
  fi
  sleep 0.1
done
xdpyinfo -display "${DISPLAY}" >/dev/null

fluxbox >/tmp/fluxbox.log 2>&1 &
x11vnc -display "${DISPLAY}" -forever -shared -rfbport 5900 -nopw -xkb -bg -o /tmp/x11vnc.log
websockify --web=/usr/share/novnc/ 6080 localhost:5900 >/tmp/novnc.log 2>&1 &

echo "Zombie Hunt noVNC: http://localhost:6080/vnc.html"
run_game
