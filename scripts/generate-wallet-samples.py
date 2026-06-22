#!/usr/bin/env python3
"""Alias: regenera todos los samples (Wallet + Gastos)."""
from pathlib import Path
import runpy

if __name__ == "__main__":
    runpy.run_path(
        str(Path(__file__).resolve().parent / "generate-document-samples.py"),
        run_name="__main__",
    )
