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
javac --release 17 -d build/classes src/*.java
echo "Built build/classes"

if [[ "${1:-}" == "run" ]]; then
  exec java -cp build/classes Demo3D
fi
