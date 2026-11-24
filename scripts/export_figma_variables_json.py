#!/usr/bin/env python3
"""Exporta variables Figma → docs/design-system/variables.json (formato 1.0.4).

Modos:
  1) Bridge (por defecto): Desktop Bridge + plugin en el archivo DS.
  2) --input <json>: pegar salida de figma_execute / MCP guardada en disco.

Uso:
  python scripts/export_figma_variables_json.py
  python scripts/export_figma_variables_json.py --input path/to/mcp-result.json

Variables de entorno:
  FIGMA_FILE_KEY   default zrGAL4v6MEMc9hzZemU432 (Design System)
  FIGMA_BRIDGE_PORT  puerto WebSocket del Bridge (opcional)
"""
from __future__ import annotations

import argparse
import json
import os
import subprocess
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[1]
PLUGIN_JS = REPO / "scripts" / "figma-export-variables-plugin.js"
BRIDGE_RUNNER = REPO / "scripts" / "run-figma-bridge-execute.js"
DEFAULT_OUT = REPO / "docs/design-system/variables.json"
DEFAULT_FILE_KEY = "zrGAL4v6MEMc9hzZemU432"


def unwrap_export_payload(data: object) -> dict:
    """Normaliza respuesta MCP / Bridge a { version, metadata, collections }."""
    if not isinstance(data, dict):
        raise ValueError("JSON raíz debe ser un objeto")

    # Bridge: { success, result, ... }
    if "result" in data and isinstance(data["result"], dict):
        if "collections" in data["result"]:
            return data["result"]
        data = data["result"]

    if "collections" in data:
        return data

    # MCP anidado
    for key in ("data", "value"):
        inner = data.get(key)
        if isinstance(inner, dict) and "collections" in inner:
            return inner

    raise ValueError("No se encontró 'collections' en el JSON de entrada")


def to_repo_document(payload: dict) -> dict:
    payload = dict(payload)
    payload.pop("counts", None)
    metadata = payload.get("metadata") or {}
    if not isinstance(metadata, dict):
        metadata = {}
    return {
        "version": payload.get("version", "1.0.4"),
        "metadata": metadata,
        "collections": payload["collections"],
    }


def run_bridge(file_key: str) -> dict:
    if not BRIDGE_RUNNER.is_file():
        raise FileNotFoundError(BRIDGE_RUNNER)
    if not PLUGIN_JS.is_file():
        raise FileNotFoundError(PLUGIN_JS)

    env = os.environ.copy()
    env["FIGMA_FILE_KEY"] = file_key

    proc = subprocess.run(
        ["node", str(BRIDGE_RUNNER), str(PLUGIN_JS)],
        env=env,
        capture_output=True,
        text=True,
        cwd=str(REPO),
    )
    if proc.returncode != 0:
        sys.stderr.write(proc.stderr or "")
        sys.stderr.write(proc.stdout or "")
        raise RuntimeError(f"Bridge falló (exit {proc.returncode})")

    try:
        bridge_msg = json.loads(proc.stdout)
    except json.JSONDecodeError as exc:
        raise RuntimeError("Salida del Bridge no es JSON válido") from exc

    if not bridge_msg.get("success", True):
        raise RuntimeError("Bridge devolvió success=false: " + json.dumps(bridge_msg)[:500])

    return unwrap_export_payload(bridge_msg)


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--input",
        type=Path,
        help="Archivo JSON (salida MCP/Bridge) en lugar de llamar al Bridge",
    )
    parser.add_argument(
        "--file-key",
        default=os.environ.get("FIGMA_FILE_KEY", DEFAULT_FILE_KEY),
        help=f"Figma file key (default {DEFAULT_FILE_KEY})",
    )
    parser.add_argument(
        "-o",
        "--output",
        type=Path,
        default=DEFAULT_OUT,
        help="Ruta de salida variables.json",
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Solo imprimir resumen, no escribir archivo",
    )
    args = parser.parse_args()

    if args.input:
        raw = args.input.read_text(encoding="utf-8")
        payload = unwrap_export_payload(json.loads(raw))
    else:
        payload = run_bridge(args.file_key)

    doc = to_repo_document(payload)
    n_vars = sum(
        len(mode.get("variables", []))
        for coll in doc["collections"]
        for mode in coll.get("modes", [])
    )

    print(
        f"Colecciones: {len(doc['collections'])} · "
        f"entradas modo×variable: {n_vars} · file-key: {args.file_key}"
    )

    if args.dry_run:
        return 0

    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(
        json.dumps(doc, indent=2, ensure_ascii=False) + "\n",
        encoding="utf-8",
    )
    print(f"Escrito: {args.output} ({args.output.stat().st_size} bytes)")
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except (ValueError, RuntimeError, FileNotFoundError) as err:
        print(f"ERROR: {err}", file=sys.stderr)
        raise SystemExit(1) from err