#!/usr/bin/env bash
# Copia los PDF de muestra al emulador/dispositivo (carpeta Descargas).
# Regenera antes: python3 scripts/generate-document-samples.py
set -euo pipefail
exec "$(dirname "$0")/push-document-samples.sh" "$@"
