#!/usr/bin/env bash
# Build (and optionally run) Zombie Hunt with JDK 17 or newer.
# Run from anywhere; resources in files/ are resolved from the project root.
set -euo pipefail
cd "$(dirname "$0")"

if ! command -v javac >/dev/null 2>&1; then
  echo "javac was not found. Install a JDK 17 or newer and try again." >&2
  exit 1
fi

mkdir -p build/classes
# MyMath.java has a Windows-1255 comment. ASCII sources are unchanged by that encoding.
javac --release 17 -encoding Cp1255 -d build/classes \
  src/*.java Zombie/*.java Gun/*.java Obsticales/*.java

if [[ "$(uname -s)" == "Darwin" ]] && command -v clang >/dev/null 2>&1; then
  jdk_home="${JAVA_HOME:-$(java -XshowSettings:properties -version 2>&1 | sed -n 's/^[[:space:]]*java.home = //p' | head -n 1)}"
  if [[ -f "$jdk_home/include/jni.h" ]]; then
    clang -dynamiclib -O2 -mmacosx-version-min=11.0 \
      -I "$jdk_home/include" -I "$jdk_home/include/darwin" \
      native/mac_mouse.c -framework ApplicationServices \
      -o build/classes/libzombie_mouse.dylib
  else
    echo "Mac mouse capture requires JDK headers; set JAVA_HOME to your JDK." >&2
  fi
fi
echo "Built build/classes"

if [[ "${1:-}" == "run" ]]; then
  exec java -cp build/classes Demo3D
fi
