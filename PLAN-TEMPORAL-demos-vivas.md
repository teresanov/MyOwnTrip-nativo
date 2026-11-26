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
- [ ] List item (`WalletDocumentRow`, `internal` en Wallet)
- [ ] Swipeable list row (`SwipeableWalletDocumentRow`, `SwipeableTripListRow`)
- [ ] Date & time pickers (`Mot*` en `ui/components/date/`)
- [ ] Search bar + Menu (`HomeSearchBar`, `HomeFilterDropdownMenu`) · Bottom sheet (`HomeFabAddSheet`) · Speed dial (`HomeSpeedDial`)

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

1. Leer este archivo y marcar dónde se quedó.
2. Comandos (raíz del repo, PowerShell):
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
   .\gradlew -p live-demos wasmJsBrowserDistribution
   cd ds-showcase; npm run live; npm run build; npx vite preview
   # live-demos: usar `gradlew -p live-demos clean wasmJsBrowserDistribution` (el incremental falla)
   ```
3. Patrón a copiar: Button (Kotlin `ButtonAnatomy` / `ButtonPlayground`; JS `live-inspector.js`).

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
