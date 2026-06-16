#!/usr/bin/env bash
# Copia los PDF de muestra al emulador/dispositivo (carpeta Descargas).
# Regenera antes si cambiaste el boarding pass: python3 scripts/generate-wallet-samples.py
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DEST="${1:-/sdcard/Download/MyOwnTrip-samples}"
adb shell mkdir -p "$DEST"
adb push "$ROOT/docs/samples/wallet/boarding-pass-ib3254-madrid-barcelona.pdf" "$DEST/"
adb push "$ROOT/docs/samples/wallet/hotel-casa-bonay-reserva.pdf" "$DEST/"
echo "Listo. Abre el selector de archivos en Importar y busca en: $DEST"
