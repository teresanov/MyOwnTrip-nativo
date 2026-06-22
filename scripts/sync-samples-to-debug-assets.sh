#!/usr/bin/env bash
# Copia docs/samples → assets de debug (para importar sin depender del explorador del móvil).
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SRC="${ROOT}/docs/samples"
DEST="${ROOT}/app/src/debug/assets/samples"
rm -rf "$DEST"
mkdir -p "$DEST"
cp -R "$SRC/wallet" "$SRC/expenses" "$DEST/"
echo "Samples sincronizados en app/src/debug/assets/samples/"
