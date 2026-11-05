# Wallet · capturas SoT (app real)

**Gate de paridad design-file:** screenshot del frame `phone` (360×800) ≈ PNG de esta carpeta. Si `@Preview` y captura difieren, gana la **captura** ([§7.1](../../design-system/audits/disparity-remediation-plan-2026-11-03.md)).

**Media mientras no hay PNG:** en Figma dejar **slot** (no inventar foto). Avisar al cerrar el cap. Teresa rellena la imagen después. Los PNG de esta carpeta siguen siendo el SoT cuando existan.

## Convención de nombres

| Archivo | Cap Figma (node) | Preview Compose | Notas |
|---------|------------------|-----------------|-------|
| `cap-01.png` | `491:13876` | Captura dispositivo · `[Demo] Barcelona pasado` | Tab Wallet en viaje pasado · 20–22 mar 2026 · 3 documentos |
| `cap-02.png` | `491:13915` | `WalletScreenEmptyPreview` | Vacío «Tu maleta digital» |
| `cap-03.png` … `cap-09.png` | (crear/actualizar) | `WalletFormScreen.kt` previews | Import / manual |
| `cap-10.png` | `491:13987` | `WalletDetailPreview` | Vuelo + QR + PDF |
| `cap-10b.png` | `503:6332` | hotel import | `hotel-casa-bonay-reserva.pdf` |
| `cap-10c.png` | `503:6415` | drift plan | Sagrada en lista demo · Teatro solo en cap 10c script |
| `cap-11.png` | `491:13994` | `WalletDeleteDialogPreview` | Basic dialog |
| `cap-12.png` | `491:14001` | `WalletDiscardDialogPreview` | Basic dialog |
| `cap-13.png` | `503:6745` | `DocumentViewerScreen` | Visor PDF |

## Cómo añadir PNG al repo

1. Exportar desde dispositivo/emulador (360×800 lógico o crop centrado).
2. Guardar con el nombre de la tabla (`cap-NN.png`).
3. Actualizar columna `capture:` en [`figma-compose-bindings.md`](../../design-system/figma-compose-bindings.md) § Wallet.

Los PDFs de demo siguen en [`../`](../) (misma escena Barcelona).
