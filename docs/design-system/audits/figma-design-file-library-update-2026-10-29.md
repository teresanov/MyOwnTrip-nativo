# Design-file · pasada Update library (2026-10-29)

**SoT app/Compose:** `MyOwnTrip_nativo`  
Archivo: `MyOwnTrip_design file` (`Vf2tNMXyKAlJSV53A1v4Is`)  
Librería: `MyOwnTrip_nativo - Design System` (`zrGAL4v6MEMc9hzZemU432`)

## Hallazgo principal

La UI de Figma **no ofrece Update** porque los sets relevantes del DS ya están en `publishStatus: CURRENT` y el design-file ya consume esos keys. No hace falta Update de componentes en Home / Día.

## Variables

Reimportadas (idempotente) al design-file desde la librería:

| Token | Key |
| --- | --- |
| `mot/stroke/hairline` | `b8255c5abfa4b28af07296bc5c1827f0df8eb9f4` |
| `mot/stroke/emphasis` | `ffc95666395e29dcfa5149764f662b2840203b60` |
| `mot/stroke/focus` | `be90232fb38871ec3fcde2570237a48418c96580` |

Publicación DS: `CURRENT` (colección `MOT · Spacing`).

## Verificación por página

### 01 · Home — OK

- 213 instancias remotas; 0 chips Elevated; 0 Bottom app bar.
- Caps: `205:816`, `313:501`, `205:1018`, `228:8161`.

### 04 · Wallet — reparada

- Página corrupta (`Unknown node type`) → frame `Wallet · flujo de app` (`491:13870`) movido a página limpia; rota eliminada.
- Página actual: `04 · Wallet` (`491:14473`).
- 12 caps alineadas a previews en `ui/features/wallet` (`WalletScreenPreview`, `WalletFormImportReviewPreview`, …).
- **189** instancias remotas; **0** chips Elevated; **0** Bottom app bar.
- **B.3 FilterChip (2026-10-29):** caps 1–2 con fila `Filter chips` (Activos / Archivados / Todos) = instancias set Outlined; pads fila `mot/spacing/*`. Ver [figma-filterchip-phase-b3-2026-10-29.md](figma-filterchip-phase-b3-2026-10-29.md).
- **B.3 Dialog (2026-10-29):** caps 7/11/12 Body inventado → instancias `Basic dialog` + glyph `error` en Eliminar/Descartar. Ver [figma-dialog-phase-b3-2026-10-29.md](figma-dialog-phase-b3-2026-10-29.md).

### CTA Confirmar (caps 5–6)

- Norma Mobbin (review → confirm): CTA anclado abajo.
- Compose SoT (`WalletImportReviewStep`): `MOTButton { Text("Confirmar") }` **sin** leading icon (el `stars` de Figma era default del swap).
- Figma + Kotlin: CTA abajo; icono oculto en Figma para paridad.

### 05 · Día & itinerario — OK

- 505 instancias remotas; 0 chips Elevated; Tabs Secondary; Filter chips Outlined.

## Gate

- [x] Home / Día al día sin Update UI
- [x] `mot/stroke/*` importados en design-file
- [x] Wallet reparada y re-auditada
- [x] Nota migrada a este repo (SoT)
