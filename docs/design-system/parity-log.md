# Bitácora de paridad DS (Compose ↔ Figma)

**Único registro de cambios de paridad.** Append aquí (fila en **Registro** o ítem en **Pendiente**).  
**No crear** `figma-*-YYYY-MM-DD.md` ni logs por componente ni por discrepancia.

**Alcance:** solo **Compose ↔ Figma ↔ showcase** (espejo visual del DS). **No** es plan de producto MVP ni fases de entrega de features — eso vive aparte (p. ej. [`docs/product/jtbd-flows.md`](product/jtbd-flows.md), Notion).

## Qué consultar (orden fijo)

| # | Documento | Rol |
|---|-----------|-----|
| 1 | [agent-workflow.md](governance/agent-workflow.md) → [design-pipeline.md](design-pipeline.md) §0 | Dos frases (pantalla / token nuevo) + matriz agentes |
| 2 | [figma-compose-bindings.md](figma-compose-bindings.md) | Sets Figma ↔ composables |
| 3 | **Este archivo** | Pendiente + historial de cambios |
| 4 | Contrato dominio (solo si aplica) | [figma-date-product-contract-2026-11-03.md](audits/figma-date-product-contract-2026-11-03.md) (date/time PRODUCT) |

Índice mínimo: [audits/README.md](audits/README.md). **No** recrear planes ni snapshots `figma-*-phase-*`; cierre = este Registro.

---

## Pendiente (paridad DS)

*Ningún ítem abierto.* Regresión o nuevo delta → nueva fila en Registro.

**Hecho (no reabrir salvo regresión):** librería DS **publicada** (state layers nov 2026). Colección **`LEGACY · Semantic Layout`:** **ausente** en Figma live (8 colecciones). **Paridad tokens Figma → `variables.json` → `Color.kt`:** cerrada 2026-11-24 — export live 597 vars · `diff-variables-kotlin.py` **0 drift** en `Schemes/*` + Extended.

**Chequeo tokens (cuando toques variables en Figma):** `python scripts/export_figma_variables_json.py` (Bridge en DS) **o** MCP `figma_execute` + `python scripts/export_figma_variables_json.py --input <mcp.json>` → `python .cursor/skills/myowntrip-ds-audit/scripts/diff-variables-kotlin.py` → fila en Registro si hubo cambio.

**Fuera de esta bitácora (producto):** event bookmark no entra por el + del Diario.

**Registro:** filas **Resuelto** y posteriores a 2026-11-17 = trabajo cerrado salvo regresión. Hallazgos intermedios duplicados no se conservan; el cierre vive en la fila **Resuelto** del mismo ámbito.

---

## Registro (más reciente arriba)

| Fecha | Ámbito | Cambio | Evidencia (Figma / código) | Estado |
|-------|--------|--------|----------------------------|--------|
| 2026-11-26 | FAB · forma | **Discrepancia:** `brand.md`, `components.md`, `figma-compose-bindings.md` y el set de Figma dicen **FAB circular**; el código usa `FloatingActionButton` sin `shape` → forma por defecto de M3 (`FabBaselineTokens.ContainerShape = CornerLarge` → `shapes.large`, **16 dp**). Comprobado en la demo viva del showcase. Decidir cuál manda: o el código pasa `shape = CircleShape` (todas las llamadas en `TripDetailScreen`, `DayHubScreen`, `TakePhotoScreen`, `HomeSpeedDial`) o la documentación y Figma pasan a Large. | Código · showcase `components/fab.html` | **Pendiente** |
| 2026-11-26 | gobernanza · pipeline | **Matriz única agentes:** §0 en `governance/design-pipeline.md`; atajo `design-pipeline.md`; skills `ds-audit`/`ds-index`/`ds-semantic-pipeline` acotados o legado; reglas index/metadata alineadas a Compose→Figma. | `AGENTS.md` · skills · rules | **Resuelto** |
| 2026-11-24 | tokens · cierre | **Paridad tokens cerrada:** re-export DS (`zrGAL4v6`) 8 colecciones · 597 variables; `diff-variables-kotlin.py` → **0 drift** Light/Dark `Schemes/*` + Extended. Post-barrido audits; sin cola tokens salvo regresión. | MCP export · `variables.json` · `Color.kt` | **Resuelto** |
| 2026-11-24 | docs · audits | **Archivo cerrado:** eliminados planes y snapshots (`disparity-remediation-plan`, `compose-figma-parity-plan`, fases `figma-*`, inventarios históricos). Vivientes: **parity-log** + contrato date/time + gap scrapbook tipografía. | `audits/README.md` | **Resuelto** |
| 2026-11-24 | design-file · 02/03 | **02 solo crear** · **03 solo editar** — **8 caps** (crear 1/1b/2/3 · editar 1–4); **MotDateRangeField** en formulario; tabla Viajes en bindings. | `Vf2tNMXy` · `figma-compose-bindings.md` · `figma-app-flows` | **Resuelto** |
| 2026-11-24 | tokens · export | **`variables.json` regenerado** desde Figma DS (597 variables · 8 colecciones). Incluye `mot/opacity/disabled-content`, `*/Opacity-12` state layers nuevos. | `figma_execute` · `docs/design-system/variables.json` | **Resuelto** |
| 2026-11-24 | tokens · Figma | **Verificado:** DS sin colección `LEGACY · Semantic Layout` (597 vars · M3 · MOT · Spacing · Shape · Typescale · Brand · Primitives). Ya no hay acción P1. | `figma_get_variables` · `zrGAL4v6` | **Resuelto** |
| 2026-11-24 | gobernanza | **R5 descartado:** sin MVP no se conserva LEGACY (docs/repo sin migración). Publish DS cerrado (ya publicada). | `spacing.md` · `disparity-remediation-plan` | **Resuelto** |
| 2026-11-24 | bitácora | Higiene: sección **Pendiente** actualizada; eliminadas filas hallazgo duplicadas; reconciliados `En curso`/`Abierto` de noviembre. | `parity-log.md` · `disparity-remediation-plan` · `compose-figma-parity-plan` | **Resuelto** |
| 2026-11-24 | icons · nombres | **Resuelto (Compose):** alias iconos alineados (`Draft`, `PhotoCamera` único); menús expuestos → `MotExposedDropdownTrailingIcon`. Script regenerado; `iconography.md` actualizado. | `generate_material_symbols.py` · audits moticons · 4 dropdowns | **Resuelto** |
| 2026-11-24 | color · error | **Resuelto (docs):** alineado hex canónico `error` Light **`#904A42`** (y Dark `#FFB4AA` en ADR/brand donde aplica) en `002-brand-editorial-m3.md`, `m3-native-ui.mdc` y `brand.md`. Sin cambio en Figma, `Color.kt` ni showcase. | `Color.kt` `errorLight` · `Schemes/Error` · `color.md` | **Resuelto** |
| 2026-11-19 | chips · state layers | **Resuelto (Figma):** `Filter chip` + `Suggestion chip` disabled **selected** → `Secondary Container/Opacity-12` (antes `On Surface Variant/Opacity-10`). **Assistive chip** `Style=Warning` disabled → `State Layers/Warning/Opacity-12` (desde `Extended Colors/Warning`). Outlined disabled sin fill contenedor: solo contenido 38 % (ya enlazado). `Input chip` / `Day pill`: sin variante Disabled — sin cambio. | Figwright `55141:14174` · sets `53923:28270` · `28089` · `28679` | **Resuelto** |
| 2026-11-19 | button · state layers | **Resuelto (Figma):** página `Buttons` — sets `Button` / tonal / outline / text / elevated, `Toggle button` (×4 estilos), `Icon button` (×4) y togglable (×4). Disabled contenedor **12 %** por rol (`Primary` / `Secondary Container` / `Secondary` / `Surface Container Low` / `On Surface` → `*/Opacity-12`); contenido **`mot/opacity/disabled-content` (38)** en `State-layer`. Variables nuevas: `Secondary/Opacity-12`, `Surface Container Low/Opacity-12`. ~640 enlaces Figwright (`batch`). Chips **fuera** de esta tanda. | Figwright `55141:14168` · `scripts/audit_button_disabled_figma.py` · smoke post-fix sin `On Surface/Opacity-10` en disabled | **Resuelto** |
| 2026-11-19 | date/time · **cola cerrada** | **Resuelto:** remediación **R1–R4** (contrato 2026-11-03, gates §6) + post-cierre **T1** smoke PRODUCT, **T2** showcase `date-time-pickers`, **T3** inventario ARCHIVE vs producto; cambios en repo commit/push. Sin trabajo abierto en date/time salvo mantenimiento (regresión = nueva fila). Opcional T4 KDoc Kotlin **no** aplicado. | Contrato · bindings · showcase · `tokens-inventory-complete-2026-11-03.md` · filas 2026-11-18 smoke/T2/T3 · `disparity-remediation-plan` cubo A | **Resuelto** |
| 2026-11-18 | date/time · inventario T3 | `tokens-inventory-complete-2026-11-03.md` regenerado: 10 tokens kit date/time → **ARCHIVE · kit M3** (no «reserva producto»); apartado PRODUCT activos; script `generate_tokens_inventory.py`. | `scripts/generate_tokens_inventory.py` · parity-log smoke 2026-11-18 | Resuelto (↑ cierre 2026-11-19) |
| 2026-11-18 | date/time · showcase T2 | Ficha **Ready** [date-time-pickers](../ds-showcase/components/date-time-pickers.html): 6 patrones PRODUCT + tabla ARCHIVE; índice `components.json` · `vite.config.js` · `npm run context`. Bindings filas date/time → enlace showcase + contrato. | `ds-showcase/src/data/components/date-time-pickers.json` · [figma-compose-bindings.md](figma-compose-bindings.md) | Resuelto (↑ cierre 2026-11-19) |
| 2026-11-18 | date/time · smoke PRODUCT | **Cierre menor post contrato:** 6 maestros (`61340:8809`, `61348:28897`, `61348:29097`, `61348:29373`, `61348:29337`, `61348:29338`) → **0** binds kit-only (`header-gap`, `field-top`, `label-gap`, `selection-row-gap`, `block-gap`, `time-input/*`; 9 variables rastreadas). Spot tokens contrato §2–4: `component-sm`, `day-grid-gap`, `screen-horizontal`, `layout-md` presentes. **ARCHIVE** DS página `61337:28407`: kit `51954:18540` (Input) · `51954:18136` (Modal) · `52949:28053` (Keyboard picker set). **Design-file:** caps R4 `261:926`/`261:929` → instancia **MotDateRangeField**; `491:13980` → **MotDateField** + **MotTimeField**; 0 `Input date picker` en subárboles caps y en páginas `02 · Viajes` / `04 · Wallet`. | Figwright · `get_nodes_info` · contrato [§6](audits/figma-date-product-contract-2026-11-03.md) | Resuelto (↑ cierre 2026-11-19) |
| 2026-11-18 | Gastos · bindings | Caps **4–4g** listadas (`ExpenseFormScreen` / `TakePhotoScreen`). Índice `07` ya no se queda en 3/3b. | `figma-compose-bindings.md` · `593:12952`…`593:14490` | Hecho |
| 2026-11-18 | Diario · chrome | Spread album chrome = contador + pills. Sin Actions en maestro ni caps. FAB captura es overlay. | DS `61262:28962` · cap 5 `622:49969` · `AlbumChrome` | Hecho |
| 2026-11-18 | Diario · voz | Detener usa `stop` w300 fill1 (cuadrado sólido). Outline no se leía en el filled. | `MOTIcons.Stop` · DS `stop` `61337:28727` · 6c1 `679:36634` | Hecho |
| 2026-11-18 | Diario · masthead | Fecha una sola vez: kicker DÍA N + `headlineSmall`. Sin `Date subtitle`. | `AlbumMasthead` · DS `61265:29035` · cap 5 `574:13966` | Hecho |
| 2026-11-17 | Diario · evento | Evento fuera del FAB y de caps 6e/6e2. El + solo pega recuerdos. | `JournalCaptureFabMenu` · `574:14292` | Hecho |
| 2026-11-17 | Diario · voz | 6c1/6c2 instancian el set publicado `Journal recording dialog`. | `679:36634` · `674:33110` | Hecho |
| 2026-11-17 | DS · publish | Unused properties: Icon button×8 sin `Show focus indicator`; Horizontal card sin TEXT duplicados; progress `Show track` bindeado al Track. | `zrGAL4v6` Buttons / Cards / Loading | Hecho |
| 2026-11-17 | DS · voz | Journal recording dialog: quitados TEXT props no aplicados (bloqueaban publish). Queda State. | `zrGAL4v6` `61531:34355` | Hecho |
| 2026-11-17 | Gastos · 4d/4e | Clip del phone: el scroll negativo ya no pinta Importe negro sobre el chrome. | `593:13678` · `593:13951` | Hecho |
| 2026-11-17 | Diario · voz | **6c** hoja · **6c1** grabando · **6c2** revisar. Set DS `61531:34355` aún no importable por key. | `618:33462` · `679:36634` · `674:33110` | Hecho |
| 2026-11-17 | R6.3 · Diario | Caps **6–7**: Write/Photo/Audio/Sticker + Boceto. Chip HAND; overlay audio 800; un Grabar voz. | `617:32975` · `618:33254` · `618:33462` · `620:35581` · `620:39326` | Hecho |
| 2026-11-17 | Diario · hojas | Una hoja por día. Chrome caps: sin hamburguesa ni 2.º FAB (`Show reading`/`Show add sheet` false). | `buildAlbumPages` · `574:13966` | Hecho |
| 2026-11-17 | R6.3 · Diario | Caps **5 / 5b / 5c**: álbum, vacío, FAB menú. Chrome `Día 1 · 1/2`. Body 5c = spacing 8/16 de **5**. | `574:13966` · `574:14129` · `574:14292` | Hecho |
| 2026-11-17 | DS · invalid assets | 0 fuentes missing (Google Sans → Inter). Toggle tonal: 80 variantes. `flight_drift` reinstanciado. | `zrGAL4v6` `58653:17539` · `61433:3885` | Hecho |
| 2026-11-17 | Plan · day hub | Caps **3i–3r** sobre cuadrante **3** (Lisboa **3m/3o**). | `593:16000` … `599:20494` | Hecho |
| 2026-11-17 | Plan · day hub | Cap **3h** menú Wallet: Corregir horario · Vincular. Grid = **3**. | `593:15839` | Hecho |
| 2026-11-17 | Plan · day hub | Cap **3g** menú actividad libre (3 ítems + iconos). Grid = **3**. | `593:15660` | Hecho |
| 2026-11-17 | Plan · day hub | Cap **3f** `MoveBlockDayDialog` sobre cuadrante **3**. Day+time: chevron + schedule. | `593:15294` · DS `61470:28626` | Hecho |
| 2026-11-17 | Plan · day hub | Cap **3b** = Compose: Outlined vacíos, copy Wallet completo, list item sin fill `Surface`. | `593:14876` · DS Content `61473:28744` | Hecho |
| 2026-11-17 | Plan · day hub | Caps **3d/3e** `WalletLinkDialog` sobre cuadrante **3**. | `593:15068` · `593:15181` | Hecho |
| 2026-11-13 | Sitios · código | Pin Maps: más patrones URL/HTML + geocode **solo del nombre** si el enlace no trae pin. Tab **4b**: sin `PlacesMiniMap`. | `PlaceShareParser` · `PlaceCoordinateResolver` · `RestaurantsTab` | Hecho |
| 2026-11-13 | Sprint | **Cerrado** Home **2b** + Sitios **4–4h** (CTA pack-start, `Slot_master` 4e, swipe Horizontal card). Caps Figma de esta cola: 0 abiertas. | `642:31056` · `08 · Sitios` | Cerrado |
| 2026-11-13 | Home · R6.2 | Cap **2b** swipe revelado. Wrap sin fill blanco. | `642:31056` · `642:31159` | Hecho |
| 2026-11-13 | List Item - Swipe | Eje **Content** = List item \| Horizontal card (4 estados). Home cap 2b → instancia tras **publicar** DS. | `zrGAL4v6` `59106:13321` · Revealed card `61510:28168` | Hecho |
| 2026-11-13 | Sitios · 4d/4e | Body FILL pack-start: CTA hug tras campos (`layout-md`), no spacer/footer. Secciones Categoría / Viaje `component-sm`. | `581:15447` · `642:31174` | Hecho |
| 2026-11-11 | IA Figma | Páginas: `05 · Plan` · `06 · Diario` · `07 · Gastos` · `08 · Sitios` · `09 · Transversal`. Mapa `00` actualizado. | `00` `261:920` | Hecho |
| 2026-11-11 | IA Figma | `03` solo ficha de viaje. Plan/Diario → `05`; Gastos → `06`; Sitios → `07`; Wallet ya en `04`. | `583:16744` · `583:16747` · `261:1087` · `261:1247` | Hecho |
| 2026-11-11 | R6.3 · Sitios | Caps **4f/4g/4h** detalle al pulsar card (`RestaurantDetailScreen` + diálogo borrar). | `583:15790` · `583:15942` · `583:16095` | Hecho |
| 2026-11-11 | R6.3 · Sitios | Cap **4d**: quitar `Viajes` duplicado; queda el caso sin pin. **4e** éxito+mapa pendiente. | `581:15447` | Hecho |
| 2026-11-11 | R6.3 · Sitios | Cap **4c**: icono del outline = location (no import); width **Fill**. Corrección en Figma. | `581:15264` | Hecho |
| 2026-11-11 | R6.3 · Sitios | Caps **4c** Nuevo sitio y **4d** Guardar sitio (sin pin). Pin Maps a pendientes. | `581:15264` · `581:15447` | Hecho |
| 2026-11-11 | R6.3 · Plan | Parte 1: caps 2 / 2b / 2c / 2d de `TripPlanTab` en `03`. | `261:941` · `573:13289` · `573:13390` · `573:13443` | Hecho (↑ day hub `05` nov 17–18) |
| 2026-11-11 | R6.3 · Diario | Parte 2: caps 5 / 5b / 5c (`TripJournalTab` + FAB menú). | `574:13966` · `574:14129` · `574:14292` | Hecho |
| 2026-11-11 | R6.3 · Gastos | Parte 3: caps 3 / 3b de `ExpensesTab`. | `261:944` · `576:14844` | Hecho (↑ caps 4–4g bindings 2026-11-18) |
| 2026-11-11 | R6.3 · Sitios | Parte 4: caps 4 / 4b de `RestaurantsTab`. | `261:947` · `576:15050` | Hecho (↑ sprint 4–4h cerrado 2026-11-13) |
| 2026-11-11 | R6.3 · Sitios | Cap 4b: quitar mini mapa; listado = cards. | `576:15050` | Hecho |
| 2026-11-11 | R6.3 · Sitios | Producto: tab Sitios = cards; Maps en `RestaurantFormScreen` (FAB). | Compose `PlacesMiniMap` en `RestaurantsTab` | Disparidad código |
| 2026-11-11 | Snackbar | Copy de **dos líneas** → variante `# of lines=Two lines`. Cap 1i corregido en Figma; no usar One line. | `534:8881` · bindings · showcase | Hecho |
| 2026-11-11 | R6.2 | Wallet cap 1g: `List Item - Swipe` Revealed, copy IB 3254, leading `flight`+círculo, 2 acciones `archive`/`delete` (SVG locales `_library imports`). Home: sin cap swipe revelado (Horizontal card). | `534:12667` · `534:13012` | Hecho |
| 2026-11-11 | R6.3 | Página `03 · Detalle de viaje`: 4 phones con set **Tabs** Scrollable Primary (Wallet/Plan/Diario/Gastos/Sitios). Cap 1 clona Wallet cap 1. Cap 2–4 chrome + tab selected; cuerpo Plan/Gastos/Sitios en módulos `05`–`08` (no Day Hub aquí). Day hub Plan\|Diario en `05` `477:2043`. | `261:937` | Hecho |
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
| 2026-11-05 | Wallet · design-file | **Prep cap 1:** gate captura documentado · `docs/samples/wallet/caps/README.md` · `figma-design-file-wallet-cap1.js` (página `491:14473`, phone `491:13876`, chips clonados, 4 highlights, sin trailing delete) · bindings + §7.1 columna `capture:` | scripts/docs · SoT = `@Preview` (sin gate PNG) | Histórico |
| 2026-11-04 | date/time | **Year/Month autolayout (re-fix):** Surface/Calendar HUG · filas 52px · años 2021–2035 · meses 4×3 · acciones visibles · set sin clip `61348:28897` | Figma Console | Hecho |
| 2026-11-04 | Wallet · design-file | **Part A hecho:** cap **10** detalle vuelo `491:13987` + cap **2** vacío `491:13915` vía `figma-design-file-wallet-flows-parity-part-a.js` (1× `figma_execute`, fileKey design-file) | Figma verify: cap10 hijos Status bar/App bar/Body · cap2 Status bar/Body · §7.1 | Hecho |
| 2026-11-04 | Wallet · design-file | **Part B hecho:** caps **10b** hotel `503:6332`, **10c** Teatro/drift `503:6415`, **13** visor PDF `503:6745` (`figma-design-file-wallet-flows-parity-part-b.js` + troceos `.cursor/exec-b64-pb*`; fix find flow `/Wallet.*flujo de app/`) | Figma verify · §7.1 | Hecho |
| 2026-11-04 | Wallet · design-file | Script completo `figma-design-file-wallet-flows-parity.js` alineado (find flow regex); Part A/B canónicos en `*-part-a.js` / `*-part-b.js` | scripts · §7.1 | Referencia |
| 2026-11-04 | Wallet · auditoría | **Brecha app ↔ design-file** documentada (tipografía manual cap 10, sin Document attachment / visor PDF, cap 1 ≠ empty «Tu maleta digital») | capturas usuario · §7.1 disparity plan | Cerrado parcial (↑ caps 1–14 y part A/B nov 4–10; resto = regresión puntual) |
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
