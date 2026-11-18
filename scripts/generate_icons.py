#!/usr/bin/env python3
"""Regenerate the desktop icons (requires Pillow; iconutil on macOS)."""

import subprocess
import tempfile
from pathlib import Path

from PIL import Image, ImageDraw


ROOT = Path(__file__).resolve().parents[1]
OUTPUT = ROOT / "packaging"
OUTPUT.mkdir(exist_ok=True)

icon = Image.new("RGBA", (1024, 1024), (0, 0, 0, 0))
draw = ImageDraw.Draw(icon)
draw.rounded_rectangle((28, 28, 996, 996), radius=210, fill="#101923")
draw.ellipse((560, 110, 890, 440), fill="#d3e9e8")
draw.ellipse((596, 142, 858, 404), fill="#b7d7da")
draw.rectangle((50, 790, 974, 950), fill="#253831")

edge = "#172419"
green = "#a8bd48"
light = "#c2d56b"
red = "#db332c"

draw.rectangle((350, 790, 477, 938), fill=green, outline=edge, width=14)
draw.rectangle((547, 790, 674, 938), fill=green, outline=edge, width=14)
draw.rectangle((332, 908, 490, 951), fill=red, outline=edge, width=12)
draw.rectangle((534, 908, 692, 951), fill=red, outline=edge, width=12)
draw.rectangle((280, 480, 744, 824), fill=green, outline=edge, width=18)
draw.polygon(((295, 500), (160, 548), (185, 661), (310, 625)), fill=light)
draw.line(((295, 500), (160, 548), (185, 661), (310, 625)), fill=edge, width=16, joint="curve")
draw.polygon(((729, 500), (864, 548), (839, 661), (714, 625)), fill=light)
draw.line(((729, 500), (864, 548), (839, 661), (714, 625)), fill=edge, width=16, joint="curve")
draw.rectangle((135, 535, 235, 678), fill=red, outline=edge, width=14)
draw.rectangle((789, 535, 889, 678), fill=red, outline=edge, width=14)
draw.rectangle((337, 205, 687, 535), fill=green, outline=edge, width=19)
draw.rectangle((355, 225, 668, 272), fill=light)
draw.rectangle((408, 345, 462, 406), fill=edge)
draw.rectangle((562, 345, 616, 406), fill=edge)
draw.rectangle((472, 450, 552, 471), fill=edge)

icon.save(OUTPUT / "icon.png")
icon.save(OUTPUT / "icon.ico", format="ICO", sizes=[(size, size) for size in (16, 32, 48, 64, 128, 256)])

with tempfile.TemporaryDirectory() as temporary:
    iconset = Path(temporary) / "icon.iconset"
    iconset.mkdir()
    for size in (16, 32, 128, 256, 512):
        icon.resize((size, size), Image.Resampling.LANCZOS).save(iconset / f"icon_{size}x{size}.png")
        icon.resize((size * 2, size * 2), Image.Resampling.LANCZOS).save(iconset / f"icon_{size}x{size}@2x.png")
    subprocess.run(["iconutil", "-c", "icns", "-o", str(OUTPUT / "icon.icns"), str(iconset)], check=True)

print(f"Generated icons in {OUTPUT}")
