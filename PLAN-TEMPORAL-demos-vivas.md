# PLAN TEMPORAL — Fichas con demos vivas, anatomía y medidas

> **Temporal.** Se borra al cerrar el último tramo. No es documentación del DS: es la hoja de ruta para retomar el trabajo si se corta una sesión.

## Objetivo

Cada ficha del showcase con:

1. **Ejemplo vivo**: el componente real de Compose (Wasm) con controles HTML (`data-p-*`).
2. **Anatomía** sobre el componente real: marcadores numerados con línea guía a cada pieza medida.
3. **Medidas** tipo inspector del navegador + tabla medida en Compose.
4. Ficha revisada contra las normas (Uso primero, prosa para diseño, sin node-ids ni `mot/*` en la ficha).

Referencia hecha: **Button** (`ds-showcase/src/pages/button.js`, `button.json` → `anatomyDiagram.live`).

## Definición de hecho (por componente)

- [ ] Specimen `<id>/playground` en `live-demos/.../embed/LiveSpecimens.kt` (parámetros desde la ficha).
- [ ] Specimen `<id>/anatomy` con piezas marcadas (`Modifier.part("…")`).
- [ ] JSON: `anatomyDiagram.live` (specimen, zoom, height, markers) alineado con `anatomy[]`.
- [ ] Página: ejemplo con `liveHostHtml` + `initLiveInspectors()` + `bootLiveSpecimens()`.
- [ ] Ficha limpia: sin sección «Figma ↔ Compose» (`sets`), sin jerga de Figma en el texto.
- [ ] Compila: `gradlew -p live-demos wasmJsBrowserDistribution` → `npm run live` → `vite build`.
- [ ] Revisado en navegador por Teresa.

## Tramos

### Tramo 1 — Componentes del kit o de Material 3, con ficha ya escrita
Se pueden hacer ya: todo está en `:designsystem` o es M3 directo.

- [x] Icon button (`MOTIconButton`) — falta revisión en navegador
- [x] Text field (M3 `OutlinedTextField`) — falta revisión en navegador
- [x] Filter chip (M3 `FilterChip`) — falta revisión en navegador
- [x] Snackbar (M3 `Snackbar`) — falta revisión en navegador
- [x] Basic dialog (M3 `AlertDialog` + `MOTTextButton`) — riesgo: el diálogo abre ventana propia; puede que haya que mostrar su contenido sin ventana. — falta revisión en navegador

### Tramo 2 — Fichas nuevas de piezas que ya están en `:designsystem` o son M3
- [x] Tabs (`MOTTab`)
- [x] Dividers (`MOTHorizontalDivider`)
- [x] Assist chip (M3 `AssistChip`) — demo con copia de `PlanPlacementSourceChip` (vive en :app)
- [x] FAB (M3) — en código NO es circular (esquina Large 16 dp); pendiente de decidir, en parity-log
- [x] App bar (M3 `TopAppBar`)

### Tramo 3 — Componentes de producto que viven en `:app` → se MUEVEN a `:designsystem`
**Decidido (2026-11-26):** se mueven al kit (KMP, `commonMain`), sin dependencias de Android. Compilar `:app` tras cada movimiento; mantener paquetes/imports o actualizar los consumidores.

- [x] Eyebrow label (`EyebrowLabel`) — movido; demo y anatomía reales
- [x] Stacked card · Horizontal card (`MotStackedCard`, `MotHorizontalCard`) — movidos; la imagen llega como Painter; cards de Home en `HomeStackedCards.kt` (app). Falta anatomía viva
- [x] TripHeroCard — parte visual al DS (+ `TripCoverPlaceholder`); de Trip a textos en `TripCards.kt` (app). Falta anatomía viva
- [x] List item — `MotListItem` + `MotListLeadingIcon` en el DS; `WalletDocumentRow` (app) traduce el documento. Corregido borde de aviso invisible
- [x] Swipeable list row — `MotSwipeableRow` en el DS (antes dos copias); menú anclado al botón ⋮ en ambas
- [ ] Date & time pickers — BLOQUEADO: 1.850 líneas sobre java.time (solo Android). Para el DS común hay que pasar a kotlinx-datetime y nombres en español propios. Decisión de Teresa
- [ ] Search bar + Menu (`HomeSearchAndFilter.kt`: separar barra y filas de menú genéricas de los filtros de viaje) · Bottom sheet (`HomeFabAddSheet`: producto, M3 ModalBottomSheet) · [x] Speed dial (`HomeSpeedDial`) movido

### Tramo 3b — Patrones con demo viva (flujo completo y su motion)
Cada ficha de componente dice qué abre y en qué patrón aparece (enlace). La página de Patrones enseña el flujo entero en Compose real.

- [ ] Elegir fechas: campo → icono calendario → selector → campo relleno (tras mover date pickers)
- [ ] Añadir desde el FAB: FAB menu del Diario (M3, botón a círculo) · HomeSpeedDial (tras moverlo)
- [ ] Elegir de una lista: campo desplegable → menú (chevron gira)
- [ ] Confirmar antes de borrar: app bar → Basic dialog → snackbar con deshacer
- [ ] Filtrar en Home: search bar → menú (ya documentado, falta demo; tras mover)

### Tramo 4 — Piezas muy de producto (decidir si llevan ficha)
Plan day card · Plan activity · Plan timeline block · Wallet (offline, QR, highlight, parse error) · Document attachment · Spread piece · Scrapbook page · Journal recording dialog.

### Pendientes detectados en revisión
- [x] Fuentes en las demos: no se cargaba NINGUNA (ni Inter ni Fraunces): la cadena de promesas de `loadFont` fallaba con un cast. Arreglado (promesas anidadas + precarga).

### Tramo 5 — Cierre
- [ ] Quitar del renderizador lo que ya no usa ninguna ficha (API heredada de `component-page.js`, `renderFigmaMap`).
- [x] GitHub Pages: compilar `live-demos` en el workflow (adelantado, commit bc5b39d; primer despliegue verificado en la web publicada)
- [ ] Decidir `ds-docs/` (abandonado): borrar.
- [ ] Borrar este archivo.

## Cómo retomar

1. Leer este archivo: «Para mañana» primero, luego el Registro.
2. Comandos (raíz del repo, PowerShell):
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
   .\gradlew -p live-demos clean wasmJsBrowserDistribution   # SIEMPRE con clean: el incremental de Kotlin/Wasm falla (ArrayIndexOutOfBounds interno)
   cd ds-showcase; npm run live; npm run build               # build OBLIGATORIO: vite preview sirve dist/
   npx vite preview --port 4179                              # http://localhost:4179/MyOwnTrip-nativo/
   .\gradlew testDebugUnitTest :app:assembleDebug            # tras mover código al DS (assemble detecta clases duplicadas)
   ```
3. Patrón a copiar: Button (`ButtonAnatomy` / `ButtonPlayground`) y, para piezas internas, `Modifier.motPart("…")` dentro del componente del DS.

### Para mañana (orden propuesto)

0. ~~**Showcase como work in progress legible**~~ HECHO 2026-11-27 (ver Registro) (el portfolio lo presenta como WIP; que el estado se entienda solo):
   - Usar las etiquetas de estado con criterio en el índice y en cada ficha: «Listo» = demo real + anatomía + medidas + revisada por Teresa; «En revisión» = hecha, sin revisar; «En construcción» = réplica HTML o sin demo (Date pickers, Patrones). Definir los tres en la página de Gobernanza o en Empezar.
   - Quitar lo que parece descuido y no proceso: node-id de Figma en la cabecera de las fichas (dejar el enlace «Ver en Figma») y rutas de código largas (solo el nombre del archivo).
   - Limpiar jerga en button, icon-button, text-field, filter-chip, basic-dialog, list-item y date-time-pickers («wrapper», «runtime», «drift», «TalkBack», «SoT»).
1. **Búsqueda y menú de Home** (`app/.../components/home/HomeSearchAndFilter.kt`): separar al DS la barra de búsqueda (`HomeSearchBarRow`) y las piezas del menú (`HomeFilterMenuList`, `…SectionLabel`, `…ItemRow` con su check) como componentes genéricos; los filtros de viaje (`TripFilterPhase`, `TripSortOrder`) se quedan en la app. Ficha nueva o la del patrón «Filtrar en Home» con demo.
2. **Date & time pickers** — esperando decisión de Teresa. Propuesta: pasarlos al DS común con `kotlinx-datetime` (nombres de meses/días en español propios, conversión java.time ↔ kotlinx en la frontera con la app), por fases y compilando la app en cada una. Alternativa: DS solo `androidMain` (rápido, sin demo).
3. **Tramo 3b — patrones con demo viva**: «Confirmar antes de borrar» y «Elegir de una lista» ya se pueden; «Añadir desde el FAB» (FAB menu del Diario es M3; HomeSpeedDial ya está en el DS).
4. Revisión de Teresa de List item y Swipeable list row.
5. Figma (parity-log, Pendiente): enlazar `Primary Fixed` (título/fechas de TripHeroCard) y `On Tertiary Fixed` (Eyebrow Tertiary); Eyebrow sin repetición en el Diario (a11y); `"$date · 2026"` fijo en ScrapbookAlbum (año escrito a mano, revisar).

### Lecciones técnicas (no repetir)

- **Vista previa de T3 sin fotogramas** cuando la ventana no está visible: Compose no mide nada. Verificar con Chrome headless:
  `chrome --headless=new --window-size=1280,12000 --virtual-time-budget=25000 --dump-dom URL` (tablas de medidas) o `--screenshot` sobre una página de prueba temporal en `dist/` (borrarla después).
- **Fuentes de las demos**: `loadFont` usa promesas anidadas (encadenadas, Kotlin tipaba Promise y el cast fallaba: no se cargaba ninguna fuente) + precarga en `LocalFontFamilyResolver`.
- **Props de las demos**: los atributos `data-p-*` llegan en minúsculas (`errortext`, no `errorText`).
- **Inspector**: solo dibuja/mide las piezas que declara la ficha (`markers` + `rows`); `redlines: true` pinta cotas fijas; `note` va bajo la tabla; usar zoom 1,5 (o 2) para que 1 dp no redondee.
- **Mover al DS**: mismo paquete (la app no cambia imports); `@Preview` a `androidMain`; si la parte de app conserva un archivo, darle otro nombre (clase `XxxKt` duplicada); nada de java.time, Coil ni `R` en `commonMain`.
- **No inventar** en demos ni fichas: textos, iconos y variantes salen de un uso real (`grep` en `app/`).

## Registro

| Fecha | Tramo | Hecho | Pendiente / notas |
|-------|-------|-------|-------------------|
| 2026-11-26 | 0 | Button: demo viva, anatomía y medidas sobre el componente real | Icono real 20 dp (el JSON decía 18) |
| 2026-11-26 | 1 | Icon button: demo viva (tamaño, ancho, tipo), anatomía y medidas reales; ficha alineada con la API (Square/Round, tamaños, anchos) | Sin modo seleccionable en el wrapper (marcado «Pendiente») |
| 2026-11-26 | 1 | Text field, Filter chip, Snackbar, Basic dialog: demos vivas, anatomía y medidas reales; tokens de espaciado con nombres de `MOTSpacing` (sin `mot/*`) | Filter chip: corregido que M3 no añade el check. Dialog: el panel vive en su propia capa (posible desajuste de coordenadas, revisar). Inspector: filas de medidas declaradas por ficha (`live.rows`) |
| 2026-11-26 | 1 | Correcciones tras revisión: superficie del tema bajo cada demo (icono negro), estilo Standard/Filled/Tonal en Icon button, Snackbar sin ancho fijo (rompía su maquetación), marcadores que no se salen del recuadro | Revisados en navegador: Snackbar y Basic dialog |
| 2026-11-26 | 1 | Text field: sin X inventada, iconos reales (calendario, chevron), texto de apoyo opcional; anatomía a 220 dp para que quepa. Filter chip: hueco entre chips en Medidas, nota en claro. Implementación: «Viene de… / Está definido en…» en vez de «Módulo · fuente» | Alineación del texto de apoyo verificada: 16 dp, igual que el texto (M3) |
| 2026-11-26 | 1 | Text field: cabecera y Bien/Mal con el componente real (antes HTML inventado: label encima y texto de apoyo pegado a la caja). Nota de cabecera distingue real / recreación | Quedan réplicas HTML en cabecera y Bien/Mal de Button, Icon button, Filter chip, Snackbar y Basic dialog |
| 2026-11-26 | 1 | Cabecera y Bien/Mal con el componente real en Button, Icon button, Filter chip, Snackbar y Basic dialog (specimens fijos `button/set`, `filter-chip/single`, `snackbar/static`, `basic-dialog/static`); textos reales de la app; confirmación destructiva en color error; Filter chip sin «check» en ninguna parte | Pendiente: Espaciado (spacing.js) aún usa botones HTML con icono y texto inventados («Abrir cuaderno»). Fraunces sin aplicar en las demos |
| 2026-11-26 | 2 | Fichas nuevas Tabs, Divider, Assist chip, FAB, App bar: demo, anatomía y medidas reales; categoría Navegación | Falta revisión de Teresa. Corregido de paso: el Filter chip medía 48 dp (área táctil) en vez de 32 dp |
| 2026-11-26 | 3 | Movidos al DS: EyebrowLabel, MotStackedCard (+MotCardStyle), MotHorizontalCard, TripHeroCard (visual) y TripCoverPlaceholder; app compila, tests y assembleDebug OK. Demos reales en sus fichas. Fuentes arregladas en todas las demos | live-demos: build siempre con `clean` (fallo interno del compilador Wasm en builds incrementales). Falta: List item, filas deslizables, date pickers, búsqueda/menú/hoja/speed dial. Revisión visual de las 3 fichas pendiente |
| 2026-11-26 | 3 | Revisión de Teresa: piezas con nombre en el DS (`Modifier.motPart`, inerte en la app) → anatomía y medidas reales de Eyebrow, Stacked card y TripHeroCard; tabla de tokens con valores leídos del código (claro/oscuro); Implementación con datos separados; textos reescritos | Pendientes de decisión (parity-log): texto de TripHeroCard en oscuro, Eyebrow Tertiary en oscuro, Eyebrow sin repetición en el Diario. Sin verificación visual (preview sin fotogramas) |
| 2026-11-26 | 3 | Medidas con cotas fijas (rellenos y huecos) en Stacked card y TripHeroCard, sin tamaños que dependen del texto; nota de qué varía. Tema oscuro corregido en código: TripHeroCard (título/fechas → primaryFixed) y Eyebrow Tertiary (texto → onTertiaryFixed). Verificado con Chrome headless | Falta enlazar esos roles en Figma (parity-log) |
| 2026-11-26 | 3 | List item y Swipeable list row al DS con fichas reescritas y demo real; HomeSpeedDial movido. App: tests y assembleDebug OK | Pendiente decisión: date pickers (java.time). Siguiente: búsqueda y menú de Home |
| 2026-11-27 | 0 | Estados con criterio (Listo = ejemplo real + anatomía + medidas + revisión de diseño; En revisión = falta revisión; En construcción = falta algo) en Gobernanza, Empezar, portada e índice; Patrones «En construcción». Cabecera: «Ver en Figma» sin node-id y solo nombre de archivo. Jerga limpiada en 12 fichas (wrapper, runtime, drift, TalkBack, SoT, scrim, CTA, node-ids en Decisiones). Corregido «el único control circular es el FAB» en Button e Icon button | Listo: Button, Icon button, Text field, Filter chip, Snackbar, Basic dialog, Eyebrow, Stacked card, TripHeroCard. En revisión: Tabs, Divider, Assist chip, FAB, App bar, List item. En construcción: Swipeable (sin anatomía viva), Date pickers |
