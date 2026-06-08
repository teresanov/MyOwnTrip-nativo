#!/usr/bin/env python3
"""Diff Schemes/* and Extended Colors between variables.json and Color.kt."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[4]
VARIABLES = ROOT / "docs/design-system/variables.json"
COLOR_KT = ROOT / "app/src/main/java/com/myowntrip/app/ui/theme/Color.kt"

# Roles mapped in Theme.kt (extend when Theme.kt grows)
SCHEME_ROLES = [
    "background", "error", "errorContainer", "inverseOnSurface", "inversePrimary",
    "inverseSurface", "onBackground", "onError", "onErrorContainer", "onPrimary",
    "onPrimaryContainer", "onPrimaryFixed", "onPrimaryFixedVariant", "onSecondary",
    "onSecondaryContainer", "onSecondaryFixed", "onSecondaryFixedVariant", "onSurface",
    "onSurfaceVariant", "onTertiary", "onTertiaryContainer", "onTertiaryFixed",
    "onTertiaryFixedVariant", "outline", "outlineVariant", "primary", "primaryContainer",
    "primaryFixed", "primaryFixedDim", "scrim", "secondary", "secondaryContainer",
    "secondaryFixed", "secondaryFixedDim", "surface", "surfaceBright", "surfaceContainer",
    "surfaceContainerHigh", "surfaceContainerHighest", "surfaceContainerLow",
    "surfaceContainerLowest", "surfaceDim", "surfaceTint", "surfaceVariant",
    "tertiary", "tertiaryContainer", "tertiaryFixed", "tertiaryFixedDim",
]

EXTENDED = ["success", "warning", "info"]


def scheme_name_to_prop(name: str) -> str:
    s = name.replace("Schemes/", "")
    parts = s.split(" ")
    if len(parts) == 1:
        return parts[0][0].lower() + parts[0][1:]
    return parts[0][0].lower() + parts[0][1:] + "".join(p.capitalize() for p in parts[1:])


def load_json_schemes() -> tuple[dict[str, str], dict[str, str], dict[str, str], dict[str, str]]:
    data = json.loads(VARIABLES.read_text())
    m3 = next(c for c in data["collections"] if c["name"] == "M3")
    light = next(m for m in m3["modes"] if m["name"] == "Light")
    dark = next(m for m in m3["modes"] if m["name"] == "Dark")

    schemes_l: dict[str, str] = {}
    schemes_d: dict[str, str] = {}
    ext_l: dict[str, str] = {}
    ext_d: dict[str, str] = {}

    for v in light["variables"]:
        if v["name"].startswith("Schemes/"):
            schemes_l[scheme_name_to_prop(v["name"])] = v["value"].upper()
        elif v["name"].startswith("Extended Colors/"):
            key = v["name"].split("/")[-1].lower()
            if key in ("success", "warning", "info"):
                ext_l[key] = v["value"].upper()

    for v in dark["variables"]:
        if v["name"].startswith("Schemes/"):
            schemes_d[scheme_name_to_prop(v["name"])] = v["value"].upper()
        elif v["name"].startswith("Extended Colors/"):
            key = v["name"].split("/")[-1].lower()
            if key in ("success", "warning", "info"):
                ext_d[key] = v["value"].upper()

    return schemes_l, schemes_d, ext_l, ext_d


def parse_color_kt() -> dict[str, str]:
    text = COLOR_KT.read_text()
    colors: dict[str, str] = {}
    for name, hexval in re.findall(r"val (\w+) = Color\((0x[0-9A-Fa-f]+)\)", text):
        colors[name] = "#" + hexval[4:].upper()
    return colors


def main() -> int:
    if not VARIABLES.exists():
        print(f"ERROR: missing {VARIABLES}", file=sys.stderr)
        return 1
    if not COLOR_KT.exists():
        print(f"ERROR: missing {COLOR_KT}", file=sys.stderr)
        return 1

    json_l, json_d, ext_json_l, ext_json_d = load_json_schemes()
    kt = parse_color_kt()

    drift: list[str] = []

    print("=== Schemes/* (variables.json ↔ Color.kt) ===\n")
    for role in SCHEME_ROLES:
        for mode, jl in (("Light", json_l), ("Dark", json_d)):
            jv = jl.get(role)
            prop = f"{role}{mode}"
            kv = kt.get(prop)
            if jv is None:
                continue
            ok = jv == kv
            mark = "OK" if ok else "DRIFT"
            print(f"  {role:28} {mode:5}  JSON={jv}  KT={kv or '—'}  [{mark}]")
            if not ok:
                drift.append(f"{role}/{mode}: JSON {jv} != KT {kv}")

    print("\n=== Extended Colors ===\n")
    for key in EXTENDED:
        for mode, je in (("Light", ext_json_l), ("Dark", ext_json_d)):
            jv = je.get(key)
            prop = f"{key}{mode}"
            kv = kt.get(prop)
            if jv is None:
                continue
            ok = jv == kv
            mark = "OK" if ok else "DRIFT"
            print(f"  {key:28} {mode:5}  JSON={jv}  KT={kv or '—'}  [{mark}]")
            if not ok:
                drift.append(f"{key}/{mode}: JSON {jv} != KT {kv}")

    print(f"\n=== Summary: {len(drift)} drift(s) ===")
    if drift:
        for d in drift:
            print(f"  - {d}")
        return 1
    print("  All mapped roles match.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
