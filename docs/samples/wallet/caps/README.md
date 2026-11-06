# Wallet · capturas SoT (app real)

**Gate de paridad design-file:** screenshot del frame `phone` (360×800) ≈ PNG de esta carpeta. Si `@Preview` y captura difieren, gana la **captura** ([§7.1](../../design-system/audits/disparity-remediation-plan-2026-11-03.md)).

**Media mientras no hay PNG:** en Figma dejar **slot** (no inventar foto). Avisar al cerrar el cap. Teresa rellena la imagen después. Los PNG de esta carpeta siguen siendo el SoT cuando existan.

## Convención de nombres

| Archivo | Cap Figma (node) | Preview Compose | Notas |
|---------|------------------|-----------------|-------|
| `cap-01.png` | `491:13876` | Captura dispositivo · `[Demo] Barcelona pasado` | Tab Wallet · viaje **pasado** · lista plana · FAB `+` → Añadir a Wallet |
| `cap-01b.png` | `532:6435` | `WalletScreenInProgressPreview` | Viaje **en curso** · chips · carrusel Próximos · Documentos activos |
| `cap-01c.png` | `533:7343` | filtro Archivados | Sin Próximos · vacío archivados |
| `cap-01d.png` | `533:7746` | filtro Todos | Sin Próximos · Todos los documentos |
| `cap-01e.png` | `533:8118` | `WalletScreenArchivedPreview` | Archivados con fila |
| `cap-01f.png` | `533:8406` | `WalletScreenActiveEmptyPreview` | Activos vacío |
| `cap-01g.png` | `534:13012` | swipe Archivar | List Item - Swipe |
| `cap-01h.png` | `534:9746` | menú ⋮ | Archivar / Eliminar |
| `cap-01i.png` | `534:8881` | snackbar archivado | Deshacer |
| `cap-02.png` | `491:13915` | `WalletScreenEmptyPreview` | Vacío «Tu maleta digital» |
| `cap-03.png` | `491:13928` | `WalletAwaitingPickerPreview` | Importar · selector del sistema abierto · Rellenar → cap 9 |
| `cap-04.png` … `cap-08.png` | (crear/actualizar) | `WalletFormScreen.kt` previews | Analizar / revisar / parse fail |
| `cap-09.png` | `491:13980` | `WalletFormManualPreview` | Añadir a Wallet · llega de FAB, Añadir o Rellenar |
| `cap-09b.png` | `533:7611` | `QrScanScreen` | Escanear QR · slot cámara · hint de apuntar |
| `cap-09c.png` | `534:10563` | `TakePhotoScreen` | Hacer foto · slot + FAB |
| `cap-09d.png` | `534:10630` | formulario con adjunto | Quitar adjunto |
| `cap-09e.png` | `534:10849` | sin viajes | Crear viaje |
| `cap-09f.png` | `534:13289` | título obligatorio | Error |
| `cap-10.png` | `491:13987` | `WalletDetailPreview` | Vuelo + QR + PDF |
| `cap-10b.png` | `503:6332` | hotel import | `hotel-casa-bonay-reserva.pdf` |
| `cap-10c.png` | `503:6415` | drift plan | Sagrada en lista demo · Teatro solo en cap 10c script |
| `cap-10d.png` | `534:12770` | detalle con precio | Registrar como gasto |
| `cap-11.png` | `491:13996` | `WalletDeleteDialogPreview` | Diálogo Eliminar sobre el detalle (papelera del app bar) |
| `cap-12.png` | `491:14001` | `WalletDiscardDialogPreview` | Basic dialog |
| `cap-12b.png` | `534:10506` | descartar sin adjunto | Copy distinta |
| `cap-13.png` | `503:6745` | `DocumentViewerScreen` | Visor PDF · página de muestra boarding pass |
| `cap-13b.png` | `530:6002` | `DocumentViewerScreen` | Visor PDF · entrada Teatro Real · Las bodas de Fígaro |
| `cap-13c.png` | `534:12865` | visor imagen | Foto de entrada (maqueta Teresa) · `entrada-teatro.jpg` |
| `cap-13d.png` | `534:12938` | archivo no encontrado | Mensaje de error |
| `cap-14.png` | `534:13526` | `WalletLinkDialog` | Desde el plan |
| `cap-14b.png` | `534:14423` | vincular vacío | Añadir documento a Wallet |

## Cómo añadir PNG al repo

1. Exportar desde dispositivo/emulador (360×800 lógico o crop centrado).
2. Guardar con el nombre de la tabla (`cap-NN.png`).
3. Actualizar columna `capture:` en [`figma-compose-bindings.md`](../../design-system/figma-compose-bindings.md) § Wallet.

Los PDFs de demo siguen en [`../`](../) (misma escena Barcelona).
