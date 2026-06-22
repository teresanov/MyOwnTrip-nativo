#!/usr/bin/env bash
# Copia samples al emulador/dispositivo (Descargas).
# Regenera antes: python3 scripts/generate-document-samples.py
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DEST="${1:-/sdcard/Download/MyOwnTrip-samples}"
DOWNLOAD="/sdcard/Download"

resolve_adb() {
  if command -v adb >/dev/null 2>&1; then
    command -v adb
    return 0
  fi
  if [[ -n "${ANDROID_HOME:-}" && -x "${ANDROID_HOME}/platform-tools/adb" ]]; then
    echo "${ANDROID_HOME}/platform-tools/adb"
    return 0
  fi
  if [[ -n "${ANDROID_SDK_ROOT:-}" && -x "${ANDROID_SDK_ROOT}/platform-tools/adb" ]]; then
    echo "${ANDROID_SDK_ROOT}/platform-tools/adb"
    return 0
  fi
  local props="${ROOT}/local.properties"
  if [[ -f "$props" ]]; then
    local sdk_dir
    sdk_dir="$(sed -n 's/^sdk\.dir=//p' "$props" | head -1)"
    if [[ -n "$sdk_dir" && -x "${sdk_dir}/platform-tools/adb" ]]; then
      echo "${sdk_dir}/platform-tools/adb"
      return 0
    fi
  fi
  return 1
}

ADB="$(resolve_adb)" || {
  echo "No se encontró adb." >&2
  echo "Instala Android SDK Platform-Tools o añade platform-tools al PATH." >&2
  echo "Ejemplo: export PATH=\"\$PATH:\$HOME/Library/Android/sdk/platform-tools\"" >&2
  exit 1
}

media_scan_file() {
  local device_path="$1"
  "$ADB" shell am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE \
    -d "file:///storage/emulated/0${device_path#/sdcard}" >/dev/null 2>&1 || true
}

echo "Subiendo samples organizados a ${DEST}..."
"$ADB" shell mkdir -p "$DEST/wallet" "$DEST/expenses"

for f in "$ROOT"/docs/samples/wallet/*; do
  [[ -f "$f" ]] || continue
  name="$(basename "$f")"
  "$ADB" push "$f" "$DEST/wallet/"
  media_scan_file "$DEST/wallet/$name"
  # Copia plana en Descargas: más visible en Samsung / picker del sistema
  "$ADB" push "$f" "$DOWNLOAD/mot-sample-$name"
  media_scan_file "$DOWNLOAD/mot-sample-$name"
done

for f in "$ROOT"/docs/samples/expenses/*; do
  [[ -f "$f" ]] || continue
  name="$(basename "$f")"
  "$ADB" push "$f" "$DEST/expenses/"
  media_scan_file "$DEST/expenses/$name"
  "$ADB" push "$f" "$DOWNLOAD/mot-sample-$name"
  media_scan_file "$DOWNLOAD/mot-sample-$name"
done

echo ""
echo "Listo (${DOWNLOAD})."
echo ""
echo "Cómo importarlos en MyOwnTrip:"
echo "  1. Wallet → Importar → Descargas → archivos mot-sample-*.pdf"
echo "  2. Gastos → foto → Galería → mot-sample-ticket-*.jpg"
echo ""
echo "Si Samsung «Mis archivos» muestra carpetas vacías, es normal con adb:"
echo "  usa el botón Importar de la app (no el explorador del móvil)."
echo ""
echo "Alternativa fiable (build debug): Wallet vacío → «Cargar samples de prueba»."
