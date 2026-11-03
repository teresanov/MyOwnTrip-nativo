# Date / time — contrato producto (Compose manda · Figma espejo)

**Fecha:** 2026-11-03 · **Estado:** vigente  
**Decisión:** lo que está en **`app/`** y se consume en pantallas es la única fuente de verdad. La página **Date & time pickers** y cualquier set del DS deben **reproducir exactamente** esos composables — no el kit M3 de catálogo Google si difiere. Si Figma no coincide, **se cambia Figma**.

**Código:** `app/src/main/java/com/myowntrip/app/ui/components/date/`  
**Supersede** para producto: matriz «Input Range = cabecera + dos campos» en [figma-datetime-matrix-2026-10-30.md](figma-datetime-matrix-2026-10-30.md) (sigue válida como log de auditoría del kit antiguo, no como contrato).

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

**Compose:** un solo **`OutlinedTextField`** M3 (read-only) + **`MOTIconButton`** trailing (`CalendarMonth`) → abre modal.

| Aspecto | Valor en código |
|---------|-----------------|
| Control | `OutlinedTextField` (padding = **M3 por defecto**, no `MOTSpacing.TextField.*` explícito) |
| Error | `isError` + `supportingText` |
| Rango copy | `formatMotDateRange` en el value |

**Figma debe mostrar:** instancia **`Text field` Outlined** (set publicado) + icono calendario — **no** el set legacy **Input date picker** con Header + Date + Date return.

**Tokens que NO son contrato de producto** (solo existían en kit M3 importado):  
`mot/spacing/date-picker/header-gap`, `mot/padding/date-picker/field-top`, gaps 74/244 del modal M3 completo, etc. En Figma: **desenlazar del doc de producto** o archivar en sección «M3 reference · not shipped».

---

## 3. Patrón B — Modal rango / fecha única (`MotDateRangePickerDialog`, `MotDatePickerDialog`)

**Compose:** `Dialog` + `Surface` (`shapes.extraLarge`, `surfaceContainerHigh`) + cabecera custom + **`MotBoundedMonthCalendar`** + acciones.

### Contenedor

| Propiedad | Token / rol |
|-----------|-------------|
| Shape | `MaterialTheme.shapes.extraLarge` (28) |
| Color | `colorScheme.surfaceContainerHigh` |
| Margen H dialog | `MOTSpacing.screenHorizontal` |

### Cabecera (`MotPickerDialogHeader`)

| Padding | Token |
|---------|--------|
| start | `mot/spacing/layout-md` (16) |
| end | `mot/spacing/component-sm` (8) |
| top | `mot/spacing/component-sm` (8) |
| bottom | `mot/spacing/component-xs` (4) |
| gap título | `mot/spacing/component-xs` (4) |

Tipografía: `labelLarge` (supporting), `titleLarge` (headline). Colores: `DatePickerDefaults.colors()` → `titleContentColor` / `headlineContentColor`.

### Calendario (`MotBoundedMonthCalendar`)

| Propiedad | Token |
|-----------|--------|
| Nav mes | flechas + `titleLarge` mes |
| Padding nav | `componentXs` H |
| Cabecera días semana | `layoutMd` H, `componentSm` V |
| Gap entre días | **`mot/spacing/date-picker/day-grid-gap` (2)** |
| Celda día | círculo, `bodyLarge`, colores `DatePickerColors` / `colorScheme` |

**Comportamiento:** un mes visible, flechas mes anterior/siguiente; **sin** scroll infinito de años. Límites: viaje **hoy → +2 años** (`resolveMotTripDateBounds`); documento wallet **±10 años** (`resolveMotDocumentDateBounds`).

### Acciones

| Propiedad | Token |
|-----------|--------|
| Padding | `componentSm` H, `componentXs` V |
| Botones | `MOTTextButton` — «Cancelar» / «Aceptar» |

**Figma debe mostrar:** una lámina **«Product · Range calendar dialog»** y **«Product · Single calendar dialog»** con esta anatomía — **no** `Modal date picker` `Type=Full-screen (range)` del kit M3 si el árbol de nodos no coincide.

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

**Figma debe mostrar:** este layout — **no** el set completo **Keyboard picker** `52949:28053` como contrato de producto salvo que coincida pixel/anatomía con el composable (hoy no es equivalente 1:1).

**Tokens `mot/spacing/time-input/*` y `keyboard-picker/gap`:** solo contrato si están enlazados a nodos que **repliquen este composable**. Si no, archivar como kit reference.

---

## 5. Qué hacer en Figma (checklist)

1. **Renombrar secciones** en «Date & time pickers»:  
   - `PRODUCT · MotDateRangeField` + `PRODUCT · MotDateRangePickerDialog`  
   - `PRODUCT · MotDateField` + `PRODUCT · MotDatePickerDialog`  
   - `PRODUCT · MotTimeField` + `PRODUCT · MotTimePickerDialog`  
   - `ARCHIVE · M3 kit (not in app)` → sets `Input date picker` `51954:18540`, `Modal date picker` `51954:18136` completos, `Keyboard picker` si no coincide con C.
2. **Bindings:** en láminas PRODUCT, solo tokens listados en §2–4 (+ `colorScheme` / text styles). Quitar binds de `header-gap`, `field-top`, `label-gap`, `block-gap` en doc de producto.
3. **`MOTSpacing`:** mantener en Kotlin solo tokens **§3–4**; propiedades `DatePicker.headerGap`, `fieldTop`, `labelGap`, … son **deuda de kit** — no ampliar; opcional eliminarlas cuando Figma archive el kit.
4. **Design-file `Vf2tNMXy`:** Crear/editar viaje instancia **PRODUCT** (campo + modal), no Input date picker Range del archivo DS antiguo.
5. **Showcase / bindings.md:** actualizar fila date/time a este contrato (no node-id del kit Input).

---

## 6. Gates de paridad (date/time)

- [ ] Cada fila de §1 tiene lámina PRODUCT en Figma trazable a línea Compose.
- [ ] 0 nodos en láminas PRODUCT con binds a tokens §5.2 (kit-only).
- [ ] Pantallas Crear/Editar viaje y Wallet form usan instancias PRODUCT.
- [ ] Auditoría `resolveVariables` false/true en subárbol PRODUCT.

---

## Referencias

- `MotDateFormFields.kt`, `MotDatePickerDialogs.kt`, `MotCalendarPicker.kt`, `MotTimePickerDialog.kt`
- `docs/design-system/spacing.md` (generales + `day-grid-gap`)
- `docs/design-system/governance/design-pipeline.md` §10
