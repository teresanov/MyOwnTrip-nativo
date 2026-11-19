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
| 6 | Cola activa disparidades (**R6** resto P1; date/time **cerrado** 2026-11-19) | [disparity-remediation-plan-2026-11-03.md](audits/disparity-remediation-plan-2026-11-03.md) |

Audits `audits/*-phase-*` y `*-2026-*` antiguos = **historial cerrado**; no ampliar salvo corrección puntual.

---

## Pendiente

Cola de módulos cerrada. Event bookmark no entra por el + del Diario.

**Corregir en Figma · state layers de Button** (hallado 2026-11-19 en `Button - tonal` `58651:11237` · XSmall; revisar el resto de Button, Icon button y Chips antes de dar por buenos):
- [ ] **Pressed** (`58651:11546`): la capa `State-layer` usa `State Layers/On Secondary Container/Opacity-08` y un vector `Ripple` (`58651:12062`) usa `Opacity-10`. Contrato (`governance.md` regla 2): pressed = **10 %**.
- [ ] **Disabled** (`58651:11666`): el contenedor (`Content`) usa `State Layers/On Surface/Opacity-10`. Contrato: disabled contenedor = **12 %**. En la colección `State Layers` **no existe `Opacity-12`** (solo 08 · 10 · 16): crearla y enlazar.
- [ ] **Disabled · contenido:** el 38 % está como opacidad de la capa `State-layer` (`opacity` 0,38 escrita en el nodo, sin variable). Contrato: 38 %. Decidir si se deja así o se resuelve con variable.
- [ ] Repetir la comprobación en `Button` filled/outlined/text/elevated, `Icon button`, `Filter chip` y `Assistive chip` (mismos estados Enabled · Hovered · Focused · Pressed · Disabled).
- [ ] Cuando se corrija: dejar fila en **Registro** y quitar este bloque.

**Corregir en documentación · valor de `error`** (hallado 2026-11-19): Figma (`Schemes/Error` Light) y código (`Color.kt` `errorLight`) coinciden en **`#904A42`**; estos documentos siguen con **`#B3261E`**:
- [ ] `docs/decisions/002-brand-editorial-m3.md` (líneas 18, 23 y 49)
- [ ] `.cursor/rules/m3-native-ui.mdc` (regla 4)
- [ ] `docs/design-system/brand.md` (línea 52 cita ambos valores; dejar solo `#904A42`)
- [ ] Al cerrar: dejar fila en **Registro** y quitar este bloque.

**Corregir en Compose · nombres de iconos ≠ Material / Figma** (hallado 2026-11-19). Norma: el componente Figma y la propiedad de `MOTIcons` usan el **nombre Material**. Lo que hoy figura como «excepción conocida» en `iconography.md` es **fallo**, no regla. Showcase ya no las documenta.
- [ ] `InsertDriveFile` → `Draft` (`draft`). Usos: `WalletScreen`, `DocumentAttachmentCard`. Mapa en `scripts/generate_material_symbols.py`; regenerar `MOTIcons.kt`.
- [ ] `CameraAlt` duplica `PhotoCamera` (`photo_camera`). Dejar una sola propiedad `PhotoCamera`. Uso: `TakePhotoScreen`.
- [ ] `ExposedDropdownMenuDefaults.TrailingIcon` (chevron interno M3, no catálogo). Sustituir por `MOTIcons` (`KeyboardArrowDown` o el símbolo que use el Text field / dropdown en Figma). Usos: `HomeFabAddSheet`, `UpdateDocumentPlanSheet`, `WalletFormScreen`, `MoveBlockDayDialog`.
- [ ] Quitar el apartado **Excepciones conocidas** de `docs/design-system/iconography.md` (no recatalogar el fallo).
- [ ] Al cerrar: dejar fila en **Registro** y quitar este bloque.

---

## Registro (más reciente arriba)

| Fecha | Ámbito | Cambio | Evidencia (Figma / código) | Estado |
|-------|--------|--------|----------------------------|--------|
| 2026-11-19 | date/time · **cola cerrada** | **Resuelto:** remediación **R1–R4** (contrato 2026-11-03, gates §6) + post-cierre **T1** smoke PRODUCT, **T2** showcase `date-time-pickers`, **T3** inventario ARCHIVE vs producto; cambios en repo commit/push. Sin trabajo abierto en date/time salvo mantenimiento (regresión = nueva fila). Opcional T4 KDoc Kotlin **no** aplicado. | Contrato · bindings · showcase · `tokens-inventory-complete-2026-11-03.md` · filas 2026-11-18 smoke/T2/T3 · `disparity-remediation-plan` cubo A | **Resuelto** |
| 2026-11-19 | icons · nombres | **Alias Kotlin y chevron M3 ≠ nombre Material.** `MOTIcons.InsertDriveFile` es `draft`; `CameraAlt` duplica `PhotoCamera` (`photo_camera`); dropdowns usan `ExposedDropdownMenuDefaults.TrailingIcon` en vez del catálogo. Figma ya nombra `draft` / `photo_camera`. Showcase Iconografía ya no lista excepciones. | `generate_material_symbols.py` · `MOTIcons.kt` · `iconography.md` §Excepciones · `TakePhotoScreen` · `WalletScreen` · `HomeFabAddSheet` | **Pendiente (corregir en Compose)** |
| 2026-11-19 | color · error | **Valor de `error` desactualizado en docs.** Light: Figma `Schemes/Error` = `#904A42` = `Color.kt` `errorLight`; `002-brand-editorial-m3.md`, `m3-native-ui.mdc` y `brand.md` mantienen `#B3261E`. Dark Figma `#FFB4AA` = `errorDark`. Figma y código **OK**; la discrepancia es solo documental. El showcase (`foundations/color`) ya muestra `#904A42` desde `tokens.json`. | Figwright `get_variable_defs` (`Schemes/Error`) · `Color.kt` L11/L61 | **Pendiente (corregir docs)** |
| 2026-11-19 | button · state layers | **Discrepancia Figma ≠ contrato** en `Button - tonal` `58651:11237` (XSmall): Pressed = capa Opacity-08 + `Ripple` Opacity-10 (contrato 10 %); Disabled contenedor = `On Surface/Opacity-10` (contrato 12 %, falta variable `Opacity-12`); Disabled contenido 38 % como opacidad del nodo, no variable. Colección `State Layers` solo tiene Opacity-08/10/16, nombradas por rol y opacidad (no por estado: cumple regla 1). Estados Enabled/Hovered/Focused: **OK**. Showcase y `governance.md` documentan el contrato (8/10/10/16; 38/12); no se ajustan a Figma. | Figwright · `Button - tonal` `57994:2296` · `58651:11306` · `58651:11426` · `58651:11546` · `58651:11666`; `get_variable_defs` | **Pendiente (corregir en Figma)** |
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
| 2026-11-11 | R6.3 · Plan | Parte 1: caps 2 / 2b / 2c / 2d de `TripPlanTab` en `03`. | `261:941` · `573:13289` · `573:13390` · `573:13443` | En curso |
| 2026-11-11 | R6.3 · Diario | Parte 2: caps 5 / 5b / 5c (`TripJournalTab` + FAB menú). | `574:13966` · `574:14129` · `574:14292` | Hecho |
| 2026-11-11 | R6.3 · Gastos | Parte 3: caps 3 / 3b de `ExpensesTab`. | `261:944` · `576:14844` | En curso |
| 2026-11-11 | R6.3 · Sitios | Parte 4: caps 4 / 4b de `RestaurantsTab`. | `261:947` · `576:15050` | En curso |
| 2026-11-11 | R6.3 · Sitios | Cap 4b: quitar mini mapa; listado = cards. | `576:15050` | Hecho |
| 2026-11-11 | R6.3 · Sitios | Producto: tab Sitios = cards; Maps en `RestaurantFormScreen` (FAB). | Compose `PlacesMiniMap` en `RestaurantsTab` | Disparidad código |
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
