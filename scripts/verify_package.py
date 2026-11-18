#!/usr/bin/env python3
"""Check a native download without needing to run it on another operating system."""

import io
import re
import sys
import tarfile
import zipfile
from pathlib import Path


REQUIRED_ASSETS = (
    "assets/images/backGround.png",
    "assets/images/Cursor.PNG",
    "assets/fonts/door.TTF",
    "assets/fonts/Playbill.ttf",
    "assets/sounds/GunFire.wav",
    "assets/sounds/Back_Sounds/Sound0.wav",
)


def verify(path: Path) -> None:
    match = re.fullmatch(r"Zombie-Hunt-v\d+\.\d+\.\d+-(macos|windows|linux)-(arm64|x64)\.(zip|tar\.gz)", path.name)
    if not match:
        raise ValueError(f"Unexpected package name: {path.name}")
    system, architecture, extension = match.groups()
    if system == "linux" and extension != "tar.gz" or system != "linux" and extension != "zip":
        raise ValueError(f"Wrong archive format for {system}: {path.name}")

    root = "Zombie Hunt.app/" if system == "macos" else "Zombie Hunt/"
    if system == "macos":
        launcher = root + "Contents/MacOS/Zombie Hunt"
        runtime = root + "Contents/runtime/Contents/Home/lib/modules"
        jar = root + "Contents/app/zombie-hunt.jar"
        native_mouse = root + "Contents/app/libzombie_mouse.dylib"
    elif system == "windows":
        launcher = root + "Zombie Hunt.exe"
        runtime = root + "runtime/lib/modules"
        jar = root + "app/zombie-hunt.jar"
    else:
        launcher = root + "bin/Zombie Hunt"
        runtime = root + "lib/runtime/lib/modules"
        jar = root + "lib/app/zombie-hunt.jar"

    if extension == "zip":
        with zipfile.ZipFile(path) as archive:
            names = set(archive.namelist())
            required = {launcher, runtime, jar}
            if system == "macos":
                required.add(native_mouse)
            missing = required - names
            if missing:
                raise ValueError(f"Missing from {path.name}: {sorted(missing)}")
            if system == "macos" and not archive.getinfo(launcher).external_attr >> 16 & 0o111:
                raise ValueError(f"Mac launcher lost executable permission: {path.name}")
            jar_data = archive.read(jar)
    else:
        with tarfile.open(path, "r:gz") as archive:
            names = set(archive.getnames())
            missing = {launcher, runtime, jar} - names
            if missing:
                raise ValueError(f"Missing from {path.name}: {sorted(missing)}")
            if not archive.getmember(launcher).mode & 0o111:
                raise ValueError(f"Linux launcher lost executable permission: {path.name}")
            jar_data = archive.extractfile(jar).read()

    with zipfile.ZipFile(io.BytesIO(jar_data)) as app_jar:
        names = set(app_jar.namelist())
        missing = set(REQUIRED_ASSETS) - names
        if missing or "Demo3D.class" not in names:
            raise ValueError(f"App JAR is incomplete in {path.name}: {sorted(missing)}")
        if "assets/scores.dat" in names:
            raise ValueError(f"Writable high score was bundled in {path.name}")

    print(f"Verified {path.name}: {system} {architecture}, launcher, runtime, assets")


if __name__ == "__main__":
    if len(sys.argv) < 2:
        raise SystemExit("Usage: python scripts/verify_package.py ARCHIVE...")
    for name in sys.argv[1:]:
        verify(Path(name))
