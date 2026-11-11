# Bitácora de paridad DS (Compose ↔ Figma)

**Único registro de cambios de paridad.** Append aquí (fila en **Registro** o ítem en **Pendiente**).  
**No crear** `figma-*-YYYY-MM-DD.md` ni logs por componente ni por discrepancia.

**Alcance:** solo **Compose ↔ Figma ↔ showcase** (espejo visual del DS). **No** es plan de producto MVP ni fases de entrega de features — eso vive aparte (p. ej. [`docs/product/jtbd-flows.md`](product/jtbd-flows.md), Notion).

## Qué consultar (orden fijo)

| # | Documento | Rol |
|---|-----------|-----|
| 1 | [design-pipeline.md](governance/design-pipeline.md) | Proceso y gates |
| 2 | [figma-compose-bindings.md](figma-compose-bindings.md) | Sets Figma ↔ composables |
| 3 | **Este archivo** | Pendiente + historial de cambios |
| 4 | Contratos de dominio (solo spec, no bitácora) | p. ej. [figma-date-product-contract-2026-11-03.md](audits/figma-date-product-contract-2026-11-03.md) |
| 5 | Plan global (mapa P0–P3, fases A/B/C) | [compose-figma-parity-plan-2026-10-29.md](audits/compose-figma-parity-plan-2026-10-29.md) |
| 6 | Cola activa disparidades (date/time PRODUCT + R6 resto P1) | [disparity-remediation-plan-2026-11-03.md](audits/disparity-remediation-plan-2026-11-03.md) |

Audits `audits/*-phase-*` y `*-2026-*` antiguos = **historial cerrado**; no ampliar salvo corrección puntual.

---

## Pendiente

| Ámbito | Tarea |
|--------|--------|
| Home swipe revelado | Cap 2 Home sigue en **Horizontal card** (`TripListCard`); no hay cap List Item-Swipe Revealed en «Más viajes». Compose `SwipeableTripListRow` envuelve esa card. |

---

## Registro (más reciente arriba)

| Fecha | Ámbito | Cambio | Evidencia (Figma / código) | Estado |
|-------|--------|--------|----------------------------|--------|
| 2026-11-11 | Snackbar | Copy de **dos líneas** → variante `# of lines=Two lines`. Cap 1i corregido en Figma; no usar One line. | `534:8881` · bindings · showcase | Hecho |
| 2026-11-11 | R6.2 | Wallet cap 1g: `List Item - Swipe` Revealed, copy IB 3254, leading `flight`+círculo, 2 acciones `archive`/`delete` (SVG locales `_library imports`). Home: sin cap swipe revelado (Horizontal card). | `534:12667` · `534:13012` | Hecho |
| 2026-11-11 | R6.3 | Página `03 · Detalle de viaje`: 4 phones con set **Tabs** Scrollable Primary (Wallet/Plan/Diario/Gastos/Sitios). Cap 1 clona Wallet cap 1. Cap 2–4 chrome + tab selected; cuerpo Plan/Gastos/Sitios **gap** (no Day Hub). Day hub Plan\|Diario ya en `05` `477:2043`. | `261:937` | Hecho (cuerpo 2–4 pendiente) |
| 2026-11-11 | R6.4 | List items Wallet: 0 `Icon` → MotWallet 40. Cap 1e ocultos IB/Hotel/AVE → glifos tipo + círculo. Leftover `544:8181` hidden. | `04 · Wallet` | Hecho |
| 2026-11-11 | R6.1 | Fila **Home · Search + Menu** en bindings (cap 2/3). Tabs y swipe documentados; instancias Figma = R6.2–.4. | `figma-compose-bindings.md` | Hecho |
| 2026-11-11 | Wallet · SoT | Casuística = Compose `@Preview` + `WalletPreviewData`, no capturas de dispositivo. Gate PNG **retirado**. Si preview ≠ captura, gana el preview. | README caps · bindings · §7.1 | Hecho |
| 2026-11-10 | Wallet · design-file | List items: `Icon` swap por tipo (`flight`/`hotel`/`directions_car`/`confirmation_number`) + `Show circle`. Ya no MotWallet 40 (avión en todas las filas). | `04 · Wallet` caps 1/1b/1d/1e/1g/1h/1i/14 | Hecho |
| 2026-11-10 | Wallet · DS | **flight_drift:** el `Drift dot` del glifo 24 debe quedar **visible** (ojito abierto). Si el layer está hidden, el swap no pinta el punto. | `zrGAL4v6` `61433:3879` | Hecho |
| 2026-11-10 | Wallet · DS | **List item Leading=Icon:** property `Leading icon` (swap) en 40 variantes. Preferidos: `MotWalletListLeadingIcon` (set) + `person` + glifos MOT. Default sigue `person`. | `zrGAL4v6` `51964:63037` · `Leading icon#61423:7` | Hecho (falta publicar) |
| 2026-11-10 | Wallet · design-file | **Highlight instanciado:** 20 frames → `Wallet highlight card` (5 tiras Próximos). Cap 8 banner → `Wallet parse error card`. | `04 · Wallet` · keys `3f17b985…` / `84816aa6…` | Hecho |
| 2026-11-10 | Wallet · DS | **Opción B:** Highlight/QR/Error/Switch fuera de Stacked card. Stacked = 8 layouts publicados. Nuevos COMPONENT: Wallet highlight card `61419:27164`, Wallet parse error card `61419:27178`. QR y Switch siguen siendo Wallet QR card / offline storage. | `zrGAL4v6` Cards | Hecho |
| 2026-11-10 | Wallet · design-file | **QR card instanciado:** 10 frames gap → instancias Default/Compact (caps 5/7b/10/10c y clones). `wifi_off` en filas «Sin conexión» (PDF). «QR sin conexión» sigue `qr_code` (= Compose). Leading Drift no inyectable en List item. Stacked Highlight sigue sin publicar. | `04 · Wallet` · import keys QR card | Hecho |
| 2026-11-10 | Wallet · DS publish | **Paso 1.1 live:** QR card, leading Drift, MotDateField, MotTimeField, offline storage y `wifi_off` **sí importan** en design-file. Stacked **Highlight/QR/Error/Switch** no (set publicado = 8 layouts, archivo DS = 12). Design-file: 0 instancias QR card / leading. | `importComponentByKeyAsync` · `04 · Wallet` | Hecho (check) |
| 2026-11-06 | Wallet · design-file | **13c** foto de entrada maquetada (Teresa) · `cap-13c.png`. | `534:12865` | Hecho |
| 2026-11-06 | Wallet · design-file | **7b** Switch Añadir al plan + MotTimeField · **13c** visor imagen = slot (no PDF). | `534:10017` · `534:12865` | Hecho |
| 2026-11-06 | Wallet · design-file | Casuística restante: 1e–1i, 5b, 7b–7d, 9c–9f, 10d, 12b, 13c–13d, 14/14b. Previews Compose añadidos. Copy viaje pasado quitado de 1b/1c/1d. | `04 · Wallet` · bindings | Hecho |
| 2026-11-06 | Wallet · design-file | Guía del tablero **sin flechas de conector**: overlay/huecos fuera; texto «Cómo leer» + tiras/caps. | `04 · Wallet` `491:13870` · bindings | Hecho |
| 2026-11-06 | Wallet · design-file | Casuística listado: **cap 1b** viaje en curso (Próximos + chips) `532:6435` · **1c** Archivados `533:7343` · **1d** Todos `533:7746` · **9b** Escanear QR `533:7611`. Previews Compose `WalletScreenInProgressPreview` + `isPastTrip` en cap 1. | `04 · Wallet` tira Añadir · bindings | Hecho |
| 2026-11-05 | Wallet · design-file | **Prep cap 1:** gate captura documentado · `docs/samples/wallet/caps/README.md` · `figma-design-file-wallet-cap1.js` (página `491:14473`, phone `491:13876`, chips clonados, 4 highlights, sin trailing delete) · bindings + §7.1 columna `capture:` | scripts/docs · Figma pendiente (Bridge off) | En curso |
| 2026-11-04 | date/time | **Year/Month autolayout (re-fix):** Surface/Calendar HUG · filas 52px · años 2021–2035 · meses 4×3 · acciones visibles · set sin clip `61348:28897` | Figma Console | Hecho |
| 2026-11-04 | Wallet · design-file | **Part A hecho:** cap **10** detalle vuelo `491:13987` + cap **2** vacío `491:13915` vía `figma-design-file-wallet-flows-parity-part-a.js` (1× `figma_execute`, fileKey design-file) | Figma verify: cap10 hijos Status bar/App bar/Body · cap2 Status bar/Body · §7.1 | Hecho |
| 2026-11-04 | Wallet · design-file | **Part B hecho:** caps **10b** hotel `503:6332`, **10c** Teatro/drift `503:6415`, **13** visor PDF `503:6745` (`figma-design-file-wallet-flows-parity-part-b.js` + troceos `.cursor/exec-b64-pb*`; fix find flow `/Wallet.*flujo de app/`) | Figma verify · §7.1 | Hecho |
| 2026-11-04 | Wallet · design-file | Script completo `figma-design-file-wallet-flows-parity.js` alineado (find flow regex); Part A/B canónicos en `*-part-a.js` / `*-part-b.js` | scripts · §7.1 | Referencia |
| 2026-11-04 | Wallet · auditoría | **Brecha app ↔ design-file** documentada (tipografía manual cap 10, sin Document attachment / visor PDF, cap 1 ≠ empty «Tu maleta digital») | capturas usuario · §7.1 disparity plan | Abierto |
| 2026-11-04 | date/time · R4 | Design-file **Crear/editar viaje** (`261:926`, `261:929`): formulario con instancia **MotDateRangeField** (+ Text fields, App bar). Wallet manual **bloqueado** hasta publish `MotDateField` / `MotTimeField` | `scripts/figma-design-file-r4-date-time-product.js` | Supersedido |
| 2026-11-04 | date/time | **R1 + R2 cerrados:** 6 COMPONENT PRODUCT alineados a app; 0 instancias Input Range kit en secciones PRODUCT; 0 binds kit-only (`label-gap`, `selection-row-gap`, `time-input/*`, …) en maestros; `MotDatePickerDialog` hereda modo | Figma Console audit · contrato §6 | Hecho |
| 2026-11-04 | date/time | **R3:** filas single date/time en `figma-compose-bindings.md` | docs | Hecho |
| 2026-11-04 | date/time | **Layout PRODUCT** modal rango: rejilla días columnas iguales (gap 2); Year/Month con pie **Cancelar/Aceptar** visible; preview Light/Dark en frame | Figma Console | Hecho |
| 2026-11-04 | date/time | **`MotDateRangeField` `61340:8809`:** 2× `Text field` Outlined (Fecha inicio/fin, `dd/mm/aaaa`) + `Icon button` calendario; fila H gap **`mot/spacing/component-sm`**; ancho ref. **360** (= Compose) | Figwright · `MotDateFormFields.kt` | Hecho |
| 2026-11-04 | date/time | ~~Política **M3 puro** rango = `DateRangePicker` + instancias Modal kit~~ **Supersedida:** rango = calendario acotado Compose; Figma PRODUCT = set `61348:28897` | contrato §3 · bindings | Supersedida |
| 2026-11-04 | tokens | Figma: `component-md` → `spacing/12`; **`component-lg` → `spacing/16`**; Header dialog `paddingTop` → `component-lg` | Figma Console | Hecho |
| 2026-11-04 | tokens | Escala componente: **`mot/spacing/component-lg` (16)** nuevo; **`component-md` restaurado a 12**; Header dialog → `component-lg`; Compose `MOTSpacing.componentLg` | Figma + Kotlin | Hecho |
| 2026-11-04 | date/time | **`MotDateRangePickerDialog` `61340:8808`:** eliminado **modo M3 Light fijado** en 201 nodos del subárbol (Surface, textos, celdas, botones); el maestro **hereda** modo de la instancia — corrige texto ilegible en preview Dark | Figma Console | Hecho |
| 2026-11-04 | date/time | `MotDateRangePickerDialog` `61340:8808`: ancho **360 ref.** + `padding` H `mot/spacing/screen-horizontal` (16); `Surface` interior stretch (= Compose `Dialog` + margen + `Surface` fillMaxWidth) | Figma Console | Hecho |
| 2026-11-04 | date/time | `MotDateRangePickerDialog` `61340:8808`: modo **M3 · Light** explícito en raíz + subárbol; fill raíz resuelve `#F1E7D9` (`surfaceContainerHigh` = `Color.kt` / `variables.json`); celdas Default → `Schemes/Surface`; sección PRODUCT → `Schemes/Background` | Figma Console · captura post-fix | Hecho |
| 2026-11-04 | date/time | BB `51954:18817` · `Selected (Middle)`: franjas kit ocultas; disco `container`+`state-layer` `Corner/Full` + `Secondary Container` (= Compose círculo rango) | Figma Console | Hecho |
| 2026-11-04 | date/time | Acciones: label `M3/label/large` 14/20 verificado en `Button - text` Small | Instancias Cancelar/Aceptar | OK |
| 2026-11-04 | repo | Bitácora única `parity-log.md`; obsoletos `figma-date-product-figma-*` → redirect | docs | Hecho |
| 2026-11-03 | date/time | Calendar Compose-like: pad 0, nav 4 H, grid 16/8/2, copy modal | `61337:28433` | Hecho |
| 2026-11-03 | date/time | Campo rango + trailing `calendar_month` Square | `61337:28409` | Hecho |
| 2026-11-03 | date/time | Kit Input/Modal/Keyboard → ARCHIVE | `51954:18540`, `51954:18136`, `52949:28053` | Hecho |
| 2026-11-03 | date/time | BB celda + Text field: LEGACY → `mot/*` en lámina rango | `51954:18817`, `52798:24373` | Hecho |
