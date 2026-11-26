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
- [ ] Tabs (`MOTTab`)
- [ ] Dividers (`MOTHorizontalDivider`)
- [ ] Assist chip (M3 `AssistChip`)
- [ ] FAB (M3, circular, sin morph)
- [ ] App bar (M3 `TopAppBar`)

### Tramo 3 — Componentes de producto que viven en `:app` → se MUEVEN a `:designsystem`
**Decidido (2026-11-26):** se mueven al kit (KMP, `commonMain`), sin dependencias de Android. Compilar `:app` tras cada movimiento; mantener paquetes/imports o actualizar los consumidores.

- [ ] Eyebrow label (`EyebrowLabel`)
- [ ] Stacked card · Horizontal card (`MotStackedCard`, `MotHorizontalCard`)
- [ ] TripHeroCard
- [ ] List item (`WalletDocumentRow`, `internal` en Wallet)
- [ ] Swipeable list row (`SwipeableWalletDocumentRow`, `SwipeableTripListRow`)
- [ ] Date & time pickers (`Mot*` en `ui/components/date/`)
- [ ] Search bar + Menu (`HomeSearchBar`, `HomeFilterDropdownMenu`) · Bottom sheet (`HomeFabAddSheet`) · Speed dial (`HomeSpeedDial`)

### Tramo 4 — Piezas muy de producto (decidir si llevan ficha)
Plan day card · Plan activity · Plan timeline block · Wallet (offline, QR, highlight, parse error) · Document attachment · Spread piece · Scrapbook page · Journal recording dialog.

### Pendientes detectados en revisión
- [ ] Fraunces no se aplica en las demos: el título del Basic dialog (headlineSmall) sale en sans. Revisar carga de `fraunces.ttf` / `MotSkiaFonts.fraunces`.

### Tramo 5 — Cierre
- [ ] Quitar del renderizador lo que ya no usa ninguna ficha (API heredada de `component-page.js`, `renderFigmaMap`).
- [ ] GitHub Pages: compilar `live-demos` en el workflow (hoy la web publicada no tiene `public/live/`).
- [ ] Decidir `ds-docs/` (abandonado): borrar.
- [ ] Borrar este archivo.

## Cómo retomar

1. Leer este archivo y marcar dónde se quedó.
2. Comandos (raíz del repo, PowerShell):
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
   .\gradlew -p live-demos wasmJsBrowserDistribution
   cd ds-showcase; npm run live; npm run build; npx vite preview
   ```
3. Patrón a copiar: Button (Kotlin `ButtonAnatomy` / `ButtonPlayground`; JS `live-inspector.js`).

## Registro

| Fecha | Tramo | Hecho | Pendiente / notas |
|-------|-------|-------|-------------------|
| 2026-11-26 | 0 | Button: demo viva, anatomía y medidas sobre el componente real | Icono real 20 dp (el JSON decía 18) |
| 2026-11-26 | 1 | Icon button: demo viva (tamaño, ancho, tipo), anatomía y medidas reales; ficha alineada con la API (Square/Round, tamaños, anchos) | Sin modo seleccionable en el wrapper (marcado «Pendiente») |
| 2026-11-26 | 1 | Text field, Filter chip, Snackbar, Basic dialog: demos vivas, anatomía y medidas reales; tokens de espaciado con nombres de `MOTSpacing` (sin `mot/*`) | Filter chip: corregido que M3 no añade el check. Dialog: el panel vive en su propia capa (posible desajuste de coordenadas, revisar). Inspector: filas de medidas declaradas por ficha (`live.rows`) |
| 2026-11-26 | 1 | Correcciones tras revisión: superficie del tema bajo cada demo (icono negro), estilo Standard/Filled/Tonal en Icon button, Snackbar sin ancho fijo (rompía su maquetación), marcadores que no se salen del recuadro | Revisados en navegador: Snackbar y Basic dialog |
