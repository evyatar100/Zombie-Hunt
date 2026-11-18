#!/usr/bin/env python3
"""Build a self-contained Zombie Hunt app on the current operating system."""

import argparse
import os
import platform
import re
import shutil
import subprocess
import sys
import tarfile
import zipfile
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
APP_NAME = "Zombie Hunt"
SOURCE_DIRS = ("src", "Zombie", "Gun", "Obsticales")
ASSET_DIRS = ("images", "fonts", "sounds")


def run(*args: str | Path) -> None:
    subprocess.run([str(arg) for arg in args], cwd=ROOT, check=True)


def jdk_tool(name: str, jdk: Path | None) -> str:
    executable = name + (".exe" if sys.platform == "win32" else "")
    if jdk:
        candidate = jdk / "bin" / executable
        if candidate.is_file():
            return str(candidate)
        raise SystemExit(f"{executable} was not found under {jdk}")
    found = shutil.which(executable)
    if found:
        return found
    raise SystemExit(f"{executable} was not found; install JDK 21+ and set JAVA_HOME")


def target() -> tuple[str, str, str]:
    system = {"darwin": "macos", "win32": "windows", "linux": "linux"}.get(sys.platform)
    machine = platform.machine().lower()
    architecture = {"arm64": "arm64", "aarch64": "arm64", "x86_64": "x64", "amd64": "x64"}.get(machine)
    if not system or not architecture:
        raise SystemExit(f"Unsupported platform: {sys.platform} {machine}")
    suffix = ".tar.gz" if system == "linux" else ".zip"
    return system, architecture, suffix


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--version", required=True, help="Package version, for example 0.1.0")
    parser.add_argument("--jdk", type=Path, help="JDK home; defaults to JAVA_HOME or PATH")
    parser.add_argument("--output", type=Path, default=ROOT / "build" / "dist")
    args = parser.parse_args()

    version = args.version.removeprefix("v")
    if not re.fullmatch(r"\d+\.\d+\.\d+", version):
        parser.error("--version must be MAJOR.MINOR.PATCH")
    # jpackage requires a positive first component even for a portable app image.
    app_version = "1.0.0" if version.startswith("0.") else version
    system, architecture, suffix = target()
    jdk = args.jdk or (Path(os.environ["JAVA_HOME"]) if os.environ.get("JAVA_HOME") else None)
    javac = jdk_tool("javac", jdk)
    jar = jdk_tool("jar", jdk)
    jpackage = jdk_tool("jpackage", jdk)

    compiler_version = subprocess.run([javac, "-version"], capture_output=True, text=True, check=True)
    match = re.search(r"javac (\d+)", compiler_version.stdout + compiler_version.stderr)
    if not match or int(match.group(1)) < 21:
        raise SystemExit("Packaging requires JDK 21 or newer")

    work = ROOT / "build" / "package-work"
    if work.exists():
        shutil.rmtree(work)
    classes = work / "classes"
    input_dir = work / "input"
    image_dir = work / "image"
    for directory in (classes, input_dir, image_dir):
        directory.mkdir(parents=True)

    sources = [source for directory in SOURCE_DIRS for source in sorted((ROOT / directory).glob("*.java"))]
    run(javac, "--release", "17", "-encoding", "Cp1255", "-d", classes, *sources)
    for directory in ASSET_DIRS:
        shutil.copytree(ROOT / "files" / directory, classes / "assets" / directory)

    app_jar = input_dir / "zombie-hunt.jar"
    run(jar, "--create", "--file", app_jar, "--main-class", "Demo3D", "-C", classes, ".")

    icon = ROOT / "packaging" / {"macos": "icon.icns", "windows": "icon.ico", "linux": "icon.png"}[system]
    command = [jpackage, "--type", "app-image", "--name", APP_NAME,
               "--app-version", app_version, "--input", input_dir, "--main-jar", app_jar.name,
               "--main-class", "Demo3D", "--icon", icon, "--dest", image_dir]
    if system == "macos":
        command.extend(("--mac-package-identifier", "com.evyatar.zombiehunt"))
    run(*command)

    images = list(image_dir.iterdir())
    if len(images) != 1 or not images[0].is_dir():
        raise SystemExit(f"Expected one application image in {image_dir}; found {images}")
    app_image = images[0]
    if system == "macos":
        launcher = app_image / "Contents" / "MacOS" / APP_NAME
        runtime = app_image / "Contents" / "runtime"
    elif system == "windows":
        launcher = app_image / f"{APP_NAME}.exe"
        runtime = app_image / "runtime"
    else:
        launcher = app_image / "bin" / APP_NAME
        runtime = app_image / "lib" / "runtime"
    if not launcher.is_file() or not runtime.is_dir():
        raise SystemExit(f"Application image is missing its launcher or runtime: {app_image}")

    output = args.output.resolve()
    output.mkdir(parents=True, exist_ok=True)
    artifact = output / f"Zombie-Hunt-v{version}-{system}-{architecture}{suffix}"
    if artifact.exists():
        artifact.unlink()
    if system == "macos":
        run("ditto", "-c", "-k", "--sequesterRsrc", "--keepParent", app_image, artifact)
    elif system == "windows":
        with zipfile.ZipFile(artifact, "w", compression=zipfile.ZIP_DEFLATED) as archive:
            for file in sorted(app_image.rglob("*")):
                if file.is_file():
                    archive.write(file, file.relative_to(app_image.parent))
    else:
        with tarfile.open(artifact, "w:gz") as archive:
            archive.add(app_image, arcname=app_image.name)
    print(f"Created {artifact}")


if __name__ == "__main__":
    main()
