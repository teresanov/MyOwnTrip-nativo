# Date / time — contrato producto (Compose manda · Figma espejo)

**Fecha:** 2026-11-03 · **Estado:** vigente  
**Decisión:** lo que está en **`app/`** y se consume en pantallas es la única fuente de verdad. La página **Date & time pickers** y cualquier set del DS deben **reproducir exactamente** esos composables — no el kit M3 de catálogo Google si difiere. Si Figma no coincide, **se cambia Figma**.

**Bitácora (cambios):** [parity-log.md](../parity-log.md) — único registro del repo; no crear MD por sesión.

**Código:** `app/src/main/java/com/myowntrip/app/ui/components/date/`  
**Supersede** para producto: matriz «Input Range = cabecera + dos campos» en [figma-datetime-matrix-2026-10-30.md](figma-datetime-matrix-2026-10-30.md) (sigue válida como log de auditoría del kit antiguo, no como contrato).

### Si diseñas en Figma (léelo primero)

- **Ficha del componente:** en la descripción del COMPONENT en la librería (anatomía → estados → comportamiento UX). Mismo estilo que una guía de producto, no un dump de tokens.
- **La app manda:** Compose define interacción y límites; la lámina puede usar fechas de ejemplo o «hoy» solo como decoración.
- **Medidas y tokens:** tablas en este contrato y en `spacing.md` — no sustituyen la ficha humana del componente.

---

## 1. Composables en producto (inventario real)

| Composable | Pantallas | Rol |
|------------|-----------|-----|
| **`MotDateRangeField`** | `CreateTripScreen`, `EditTripScreen` | Campo rango viaje |
| **`MotDateField`** | `WalletFormScreen` | Fecha documento wallet |
| **`MotTimeField`** / **`MotTimeTextField`** | `WalletFormScreen`, plan (`DayPlanSchedule`, sheets…) | Hora |
| **`MotDateRangePickerDialog`** | (desde `MotDateRangeField`) | Modal rango |
| **`MotDatePickerDialog`** | (desde `MotDateField`) | Modal fecha única |
| **`MotTimePickerDialog`** | (desde `MotTimeField`) | Modal hora |

**No consumido en `app/` hoy:** `MotTripDatePickerDialog` (wizard año → mes → día). **No** documentar como patrón de producto en Figma hasta que una pantalla lo use. Opción DS: mover a `ARCHIVE · not in app` o borrar de la lámina de producto.

---

## 2. Patrón A — Campo de fecha / rango (formulario)

**Compose (`MotDateRangeField`):** fila con **dos** `OutlinedTextField` M3 editables (inicio / fin, `dd/MM/yyyy`, teclado) + **`MOTIconButton`** calendario → [MotDateRangePickerDialog]. Validación al perder foco; ventana **aceptada** al escribir: hoy → +10 años (`defaultMotMaxAcceptedTripDate`); ventana **calendario**: hoy → +2 años (`pickerMaxDate` / `defaultMotMaxSelectableDate`).

**Compose (`MotDateField`):** un `OutlinedTextField` read-only + icono → [MotDatePickerDialog].

| Aspecto | Valor en código |
|---------|-----------------|
| Control rango | 2× `OutlinedTextField` + icono calendario |
| Control fecha única | `OutlinedTextField` read-only + trailing icon |
| Error | `isError` + `supportingText` (parseo local o error de formulario) |

**Figma debe mostrar:** rango = instancias **`Text field` Outlined** (Date return del Input · Type=Range, `51954:18560`) + icono calendario; fecha única = un campo Outlined + icono. Referencia kit: [Input date picker · Range](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51954-18540) (ARCHIVE → promover a PRODUCT en lámina `61340:8809`).

#### Ficha diseñador — `MotDateRangeField` (Dev Mode · `61340:8809`)

| Tema | Detalle |
|------|---------|
| Anatomía | 2× Outlined «Fecha inicio» / «Fecha fin» + icono calendario |
| Entrada | `dd/mm/aaaa` con teclado |
| Error | Borde + supporting text |
| Espaciado fila | `mot/spacing/component-sm` entre controles |
| Ancho lámina | 360 ref. |
| Color icono | Schemes del modo (Light/Dark en frame de preview) |

#### Comportamiento (resumen)

Escribir fechas o abrir calendario → modal de rango. Validación al perder foco. Teclado: hoy → +10 años; calendario del modal: hoy → +2 años.

#### Ficha diseñador — `MotDateField` (Dev Mode · `61348:29097`)

| Tema | Detalle |
|------|---------|
| Anatomía | 1× Outlined solo lectura + trailing calendario |
| Placeholder | «Sin fecha» (ejemplo doc) |
| Comportamiento | Abre **MotDatePickerDialog** · wallet |

**Tokens que NO son contrato de producto** (solo existían en kit M3 importado):  
`mot/spacing/date-picker/header-gap`, `mot/padding/date-picker/field-top`, gaps 74/244 del modal M3 completo, etc. En Figma: **desenlazar del doc de producto** o archivar en sección «M3 reference · not shipped».

---

## 3. Patrón B — Modal rango (`MotDateRangePickerDialog`) y fecha única (`MotDatePickerDialog`)

### Rango — `MotDateRangePickerDialog`

**Compose:** shell **`DatePickerDialog` M3** + cuerpo **`MotBoundedMonthCalendar`** (`MotCalendarPicker.kt`): **un mes** visible con flechas, selección inicio/fin, y tres superficies (`MotCalendarPickerSurface`):

| Superficie | UI |
|------------|-----|
| **Days** | Nav mes + cabeceras L–D + rejilla 7×6 |
| **Year** | Nav atrás + título «Elige el año» + `MotYearPickerGrid` (3 columnas) |
| **Month** | Nav atrás + título = año + `MotMonthPickerGrid` (3×4, meses `es`) |

**No** se usa `DateRangePicker` M3 (lista vertical de todos los meses). Límites: `SelectableDates` / `coerceInRange` (calendario ~hoy→+2 años; teclado del campo hasta +10 años).

| Propiedad | Token / rol |
|-----------|-------------|
| Shape / color | `DatePickerDialog` + `DatePickerDefaults.colors(containerColor = surfaceContainerHigh)` |
| Margen H | `MOTSpacing.screenHorizontal` (16) |
| Cabecera | `supportingText` + headline (`inputHeadline` o `formatMotDateRange`) |
| Acciones | `MOTTextButton` Cancelar / Aceptar |

**Figma PRODUCT:** [`MotDateRangePickerDialog`](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-28897) · propiedad **`Surface`**: `Days` · `Year` · `Month`. Ficha completa en la descripción del componente (Dev Mode). Preview Light/Dark: modo en el **frame** de documentación, no fijado en el maestro.

#### Ficha diseñador — `MotDateRangePickerDialog` (Dev Mode · `61348:28897`)

| Tema | Detalle |
|------|---------|
| **Surface · Days** | Un mes; flechas; título mes+año (salta a año); L–D; rejilla 7 col.; rango inicio/fin/medio en celdas |
| **Surface · Year** | Atrás; «Elige el año»; rejilla años 3 col. |
| **Surface · Month** | Atrás; año; meses abrev. (ene…dic) 3×4 |
| Cabecera | Supporting + titular (rango o copy por defecto) |
| Pie | Cancelar · Aceptar (Aceptar inactivo sin fecha fin) |
| Contenedor | Extra large · **Schemes / surface container high** |
| Margen H diálogo | `mot/spacing/screen-horizontal` (16) |
| Rejilla días | Gap `mot/spacing/date-picker/day-grid-gap` (2); pad H `layout-md`; pad V `component-sm` |
| Nav mes | Pad H `mot/spacing/component-xs` (4) |
| Celdas día | `.Building Blocks/Local M3 calendar cell` — Default, Null, Today, Selected, Selected (Middle) |

#### Comportamiento (resumen)

Primer día = inicio; segundo = fin. Mes+año → año → mes → vuelve al mes. Flechas = mes anterior/sigiente. Atrás en año/mes sube nivel. Cancelar cierra sin cambios. Sin scroll de lista de meses.

### Fecha única — `MotDatePickerDialog`

**Compose:** `DatePickerDialog` + **`DatePicker` M3** (`DisplayMode.Picker`, `showModeToggle = false`, `SelectableDates`).

**Figma PRODUCT:** [`MotDatePickerDialog`](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-29373) (modal M3 día). Ficha en Dev Mode.

#### Ficha diseñador — `MotDatePickerDialog` (`61348:29373`)

| Tema | Detalle |
|------|---------|
| Cabecera | Supporting + titular (fecha o copy) |
| Cuerpo | Date picker M3 (mes + selector mes/año) |
| Pie | Cancelar · Aceptar |
| Specs | Ancho 360 · margen H `screen-horizontal` · rejilla gap `day-grid-gap` (2) |

---

## 4. Patrón C — Modal hora (`MotTimePickerDialog`)

**Compose:** `Dialog` + `Surface` (`extraLarge`, `surfaceContainerHigh`) + título + **`TimeInput`** (24 h, modo por defecto) ↔ toggle **`TimePickerDialogDefaults.DisplayModeToggle`** ↔ **`TimePicker`** + fila Cancelar/Aceptar.

| Zona | Tokens |
|------|--------|
| Surface margin H | `screenHorizontal` |
| Column pad top/bottom | `layoutMd` / `componentSm` |
| Título | `layoutMd` H · `labelMedium` · `onSurfaceVariant` |
| TimeInput / TimePicker pad | `layoutMd` H, `componentSm` V (Input) · `componentSm` H (Picker) |
| Fila acciones | `componentSm` start, `layoutMd` end · gap `componentSm` |

**Figma debe mostrar:** este layout — lámina PRODUCT [`MotTimePickerDialog`](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-29338) + campo [`MotTimeField`](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-29337). El set **Keyboard picker** ARCHIVE (`52949:28053`) no es SoT de producto.

#### Ficha diseñador — `MotTimeField` / `MotTimePickerDialog`

| Componente | Node | Resumen |
|------------|------|---------|
| **MotTimeField** | `61348:29337` | Outlined + trailing reloj · 24 h |
| **MotTimePickerDialog** | `61348:29338` | Título + Time input + toggle + Cancelar/Aceptar |

**Tokens `mot/spacing/time-input/*` y `keyboard-picker/gap`:** solo contrato si están enlazados a nodos que **repliquen este composable**. Si no, archivar como kit reference.

---

## 5. Qué hacer en Figma (checklist)

1. **Renombrar secciones** en «Date & time pickers»:  
   - `PRODUCT · MotDateRangeField` + **`MotDateRangePickerDialog`** COMPONENT_SET (`61340:8809` / `61348:28897`, eje `Surface`)  
   - Igual para `MotDateField`, `MotDatePickerDialog`, `MotTimeField`, `MotTimePickerDialog` (componentes, no frames huérfanos)  
   - `ARCHIVE · M3 kit (not in app)` → sets `Input date picker` `51954:18540`, `Modal date picker` `51954:18136` completos, `Keyboard picker` si no coincide con C.
2. **Bindings:** en láminas PRODUCT, solo tokens listados en §2–4 (+ `colorScheme` / text styles). Quitar binds de `header-gap`, `field-top`, `label-gap`, `block-gap` en doc de producto.
3. **`MOTSpacing`:** mantener en Kotlin solo tokens **§3–4**; propiedades `DatePicker.headerGap`, `fieldTop`, `labelGap`, … son **deuda de kit** — no ampliar; opcional eliminarlas cuando Figma archive el kit.
4. **Design-file `Vf2tNMXy`:** Crear/editar viaje instancia **PRODUCT** (campo + modal), no Input date picker Range del archivo DS antiguo.
5. **Showcase / bindings.md:** actualizar fila date/time a este contrato (no node-id del kit Input).

---

## 6. Gates de paridad (date/time)

- [x] Cada fila de §1 tiene lámina PRODUCT en Figma trazable a línea Compose.
- [x] 0 nodos en láminas PRODUCT (6 maestros) con binds kit-only §5.2.
- [x] Pantallas Crear/Editar viaje y Wallet form usan instancias PRODUCT (design-file · R4). Caps `261:926`, `261:929`, `491:13980`.
- [x] Auditoría variables en subárbol PRODUCT (forbidden binds 0; modos explícitos 0 en `MotDatePickerDialog`).

---

## Referencias

- `MotDateFormFields.kt`, `MotDatePickerDialogs.kt`, `MotCalendarPicker.kt`, `MotTimePickerDialog.kt`
- `docs/design-system/spacing.md` (generales + `day-grid-gap`)
- `docs/design-system/governance/design-pipeline.md` §10
