# P1 · Date / time — matriz y bindings (30 oct 2026)

**Plan:** [compose-figma-parity-plan-2026-10-29.md](compose-figma-parity-plan-2026-10-29.md) · P1 Date/time (parcial en fase C, pendiente matriz de estados y quirks de kit).  
**Fuente de verdad (Compose):** `app/.../ui/components/date/` · `MotDatePickerDialogs.kt`, `MotDateFormFields.kt`, `MotCalendarPicker.kt`, `MotTripDatePickerDialog.kt`, `MotTimePickerDialog.kt`.  
**Espejo (Figma):** `Modal date picker` `51954:18136` · `Input date picker` `51954:18540`, librería `zrGAL4v6`.  
**Herramienta:** Figwright, `get_nodes_info` sobre ambos sets (1 434 nodos).

---

## 1. Compose (SoT)

| Aspecto | Estado |
|---------|--------|
| Padding y gaps | Tokenizados con `MOTSpacing` (`layoutMd`, `componentSm`, `componentXs`, `screenHorizontal`, `DatePicker.cell`, `DatePicker.dayGridGap`) |
| Radio del diálogo | `MaterialTheme.shapes.extraLarge` (28) |
| Radio de celda de año | `MaterialTheme.shapes.medium` |
| Literales restantes | `heightIn` 220/240/280/320/44 dp · icono 18 dp · borde 1 dp · `MotTimePickerMinWidth` 280 dp |

Los literales restantes son **geometría de componente**, no espaciado. No hay token de Figma para ellos.

## 2. Bindings en Figma

Recorrido de todos los nodos con `layout` (padding e `itemSpacing`):

| Set | Valores de layout | Enlazados | Sin enlazar |
|-----|------------------:|----------:|------------:|
| Modal date picker | 1 514 | 1 508 | **6** |
| Input date picker | 101 | 96 | **5** |

Residuales (coinciden con la auditoría de fase C):

| Set | Valor | Nodos | Nota |
|-----|-------|------:|------|
| Modal | `itemSpacing` 36 | 2 (`Label and Date`) | Kit |
| Modal | `itemSpacing` 74 | 2 (`Local Selection Row`) | Kit |
| Modal | `itemSpacing` 244 | 1 (`Frame 2608508`) | Kit |
| Modal | `paddingLeft` 64 | 1 (`Labe, Date & Icon Button`) | Kit |
| Input | `itemSpacing` 36 | 2 (`Header`) | Kit |
| Input | `paddingTop` 18 | 2 (`Date return`, `Date`) | Kit |
| Input | `itemSpacing` 1 | 1 (`Date`) | Kit |

**Decisión (política del plan):** estos valores no tienen espejo en `MOTSpacing` ni en `shapes.*`. Según la regla «no inventar `mot/*` sin espejo Kotlin», se **documentan como residual de kit** y no se enlazan.

## 3. Matriz de variantes

| Set | Variantes en Figma | Equivalente Compose |
|-----|--------------------|---------------------|
| Modal date picker | `Type=Full-screen (range)` | `MotDateRangePickerDialog` |
| Modal date picker | `Type=Year` | Selector de año (`MotCalendarPicker` / `MotTripDatePickerDialog`) |
| Modal date picker | `Type=Day` | `MotDatePickerDialog` |
| Input date picker | `Type=Range` | `MotDateRangeField` |
| Input date picker | `Type=Single input` | `MotDateField` |
| — | **Sin variante** | `MotTimePickerDialog` (hora) |

## 4. Matriz de estados

**Corrección:** la versión anterior de esta sección decía que Figma no tenía estados de celda. Es incorrecto. Las celdas de día son instancias de `.Building Blocks/Local M3 calendar cell` (21 variantes usadas en los dos sets), que sí modela los estados:

| Propiedad | Valores encontrados en las instancias | Compose |
|-----------|----------------------------------------|---------|
| `Type` | `Default`, `Null`, `Today`, `Selected`, `Selected (Middle)` | `today`, `selected` (y rango) |
| `State` | `Enabled` (en todas las instancias usadas) | `enabled` |
| `Show focus indicator`, `Start range`, `End range`, `Extra left/right` | Booleanos | Rango y foco |

Año: `.Building Blocks/Year` tiene `Selected=False/True`.

Compose maneja además `error` en campos de fecha (`isError`), que no aparece en los sets de fecha.

**Huecos reales:**
1. **Estado `Disabled`** de la celda: no aparece en las instancias usadas. Hay que comprobar si existe en el set `Local M3 calendar cell`.
2. **Error en campos de fecha:** `Input date picker` no tiene variante de error.
3. **Hora** (`MotTimePickerDialog`): sin set en la librería.

---

## 5. Cambios y cierre (30 oct 2026)

| Elemento | Resultado |
|----------|-----------|
| **Estados de celda** | No hacía falta añadirlos al set Modal: `Local M3 calendar cell` ya tiene `Type` (Default, Null, Today, Selected, Selected (Middle)) y `State` (Enabled, Disabled). Confirmado `State=Disabled` en `Type=Default` y `Type=Today`. |
| **Selector de hora** | `Keyboard picker` (`52949:28053`) ya es un COMPONENT_SET con variantes `Format=12 hour` y `Format=24 hour`. Compose solo usa 24 h. El modo reloj (`Picker`) de Compose sigue sin set en Figma. |
| **Input date picker · error** | Convención A: `State=Enabled, Type=…` y `State=Error, Type=…` (Range y Single input). Las variantes de error usan las instancias oficiales `Text field` `Style=Outlined, State=Error` (`52798:24783` y `52798:25673`) y muestran el texto de apoyo, como `isError` + `supportingText` en Compose. |

**Comprobación de bindings (Input date picker, 4 variantes):**

| Variante | Layout enlazado | Layout sin enlazar | Paints sólidos sin enlazar | LEGACY |
|----------|----------------:|-------------------:|---------------------------:|-------:|
| State=Enabled, Type=Range | 53 | 3 | 0 | 0 |
| State=Enabled, Type=Single input | 43 | 2 | 0 | 0 |
| State=Error, Type=Range | 53 | 3 | 0 | 0 |
| State=Error, Type=Single input | 43 | 2 | 0 | 0 |

Los valores sin enlazar son solo los residuales de kit ya documentados (36, 1 y 18). Familias de variables usadas: `mot/*`, `Static/*`, `Corner/*` y `Schemes/*`.

**Verificación con el MCP de Figma (`use_figma`, solo lectura, sesión `dc5f37e9-…`):**
- *Raw (`resolveVariables=false`):* mismos residuales de kit (36, 1 y 18); 0 paints sólidos sin enlazar en las 4 variantes.
- *Resuelto (`resolveVariables=true`):* 45 variables distintas enlazadas, todas resuelven; 0 faltantes; 0 de LEGACY. Colecciones: M3 (8), MOT · Spacing (15), Typescale (20), Shape (2).

**Estado del set: radios cerrados; layout con 10 residuales de kit.** La comprobación anterior no revisaba radios de esquina ni los átomos anidados.

**Enlazado (Figwright, 30 oct 2026):**
- 4 variantes de diálogo → `cornerRadius` = `Corner/Extra-large` (`VariableID:55064:15592`).
- 8 `Content` de icon button (instancias) → `Corner/Full` (`VariableID:55064:15593`).
- Raíz del set → `Corner/Large` (`VariableID:55064:15591`). *Supuesto:* el valor 16 coincide con `Corner/Large`; falta confirmar el rol de la raíz.
- Verificación: radios 38/38 enlazados.

**Residual de layout (10):** gap 36 en `Header`, gap 1 en `Date`, `paddingTop` 18 en `Date` y `Date return`. No hay variable de Figma para esos valores ni espejo en Compose.

**Radios sin enlazar (lectura de `cornerRadius` y `topLeft/…Radius`, incluidos átomos):**

| Nodo | Valor | Problema |
|------|------:|----------|
| Set `Input date picker` | 16 | Sin binding; decidir a qué rol corresponde |
| 4 variantes (raíz de diálogo) | 28 | Sin binding; en Compose es `MaterialTheme.shapes.extraLarge` → `Corner/Extra-large` |
| `Content` del icon button (átomo `Icon button - standard`, 6 instancias) | 100 | Sin binding; es el círculo completo → `Corner/Full` |

Total: 13 de 38 nodos con radio > 0 sin enlazar.

**Paddings y gaps sin enlazar (residual de kit):** 4 `paddingTop` de 18 y 6 `itemSpacing` (36 y 1). Siguen sin espejo en Kotlin; se documentan como residual.

**Átomo `Direct Input (keyboard) input` · `Selected=True` (`52949:28122`), enlazado con Figwright:**

| Nodo | Propiedad | Valor | Decisión | Enlace |
|------|-----------|------:|----------|--------|
| `Input Field` (`52949:28123`) | `cornerRadius` (4 esquinas) | 8 | Corner/Small | `55064:15589` |
| `Input Field` | `paddingLeft` / `paddingRight` | 16 | `mot/padding/text-field/horizontal` | `61289:28812` |
| `Input Field` | `paddingTop` / `paddingBottom` | 9 → **4** | `mot/padding/text-field/vertical` (decisión final; antes se propuso 8 con `component-sm`) | `61289:28813` |
| `Input Field` | `strokeWeight` (4 lados) | 2 | `mot/stroke/emphasis` (supuesto) | `61298:28855` |
| `Input Field` | `itemSpacing` | 1 | Se deja: default M3, sin token | — |
| Raíz `Selected=True` | `itemSpacing` | 7 → **8** | Ajuste de valor aprobado: `mot/spacing/component-sm` | `61282:28767` |
| `Cursor` | `width` | 2 | Se deja | — |

Verificación: `Input Field` y raíz con los bindings anteriores (lectura con `get_node`).

**`Selected=False` (`52949:28127`):** mismos criterios. Gap de la raíz 7 → 8 (`component-sm`); `Input Field` con padding vertical 9 → 4 (`61289:28813`, `mot/padding/text-field/vertical`), horizontal 16 (`61289:28812`) y radio 8 (`Corner/Small`). Gap del campo (1) se deja. **Sin trazo en el Input Field a propósito** (no seleccionado); el trazo de 2 px con `mot/stroke/emphasis` solo existe en `Selected=True`.

El resto del set `Direct Input (keyboard) input` no está revisado.

**Nota:** durante el cierre, el responsable de Figma enlazó variables adicionales directamente en el archivo con el mismo criterio. Esta matriz no las ha re-comprobado; la cifra de residuales de layout de arriba puede haber cambiado.

**Actualización (3 nov 2026):**

| Elemento | Cambio |
|----------|--------|
| `Direct Input` · Input Field (ambas variantes) | Padding vertical → `mot/padding/text-field/vertical` (4), anulando la propuesta de 8. Gap interno (1) → `mot/spacing/time-input/field-gap`. |
| `Direct Input` · raíz (ambas variantes) | Gap 7 → 8 (`component-sm`). |
| `.Building Blocks/Input` (`52949:28114`) | Radios de `selected`, `default` y raíz de `Selected=False` → `Corner/Small`. Gap 7 desenlazado y puesto a 0 en `Selected=True` (`52949:28118`), `selected` (`52949:28119`) y `Selected=False` (`52949:28115`): el gap no separaba nada (un solo hijo visible). La variable `mot/spacing/time-input/label-top` (7, `MOTSpacing.TimeInput.labelTop`) se conserva para un futuro label bajo el campo. |
| Tokens nuevos | `mot/spacing/time-input/label-top` (7) y `mot/spacing/time-input/field-gap` (1), con espejo en `MOTSpacing.TimeInput`. Espejo de `text-field` en `MOTSpacing.TextField` (16 / 4). |
| Trazo de foco del campo | `mot/stroke/emphasis` (2), decisión tuya. |
| Verificación M3 | Fuente `material3-1.5.0-alpha12` (versión de `gradle/libs.versions.toml`): `TimeFieldContainerShape = CornerSmall`, alto 72 y ancho 96, `SupportLabelTop = 7`, `TimeInputBottomPadding = 8`, `TimeFieldFocusOutlineWidth = 2`. |

**Gap 10 de `default` (`52949:28116`):** enlazado a `mot/spacing/time-input/content-gap` (10), con espejo `MOTSpacing.TimeInput.contentGap`. Sin origen en M3; token propio.

**Gap 8 de `Direct Input` (raíz de `Selected=True` `52949:28122` y `Selected=False` `52949:28127`, entre el Input Field y la etiqueta "Time label"):** token propio `mot/spacing/time-input/label-gap` (8, `VariableID:61322:8945`), espejo `MOTSpacing.TimeInput.labelGap`. No es `TimeInputBottomPadding` de M3 (padding inferior de la fila de campos, `TimePicker.kt` línea 1124), ni `SupportLabelTop` (7 dp, desplazamiento del texto de apoyo). Origen del valor: propio del kit, sin correspondencia en M3.

Pendiente de poner en la descripción del componente en Figma (`52949:28121`): requiere el plugin *Figma Desktop Bridge* abierto; hoy no está conectado.

**Pendiente:** alto y ancho del campo (aplazados).

**Icon button · `Icon button - standard` (`58663:30358`), uso actual:**

- Compose usa un único tamaño: `MOTIconButton` (`MOTButtons.kt`) envuelve el `IconButton` de M3 con su tamaño por defecto de 40 dp y la forma `rememberMOTButtonShape` (0 en reposo, 20 en hover, focus y pressed).
- Variante equivalente en Figma: `Type=Square, Size=Small, Width=Default` (`Content` 40 × 40). Coincide con el tamaño y la forma del código.
- Las demás variantes (otros Size, Width Wide/Narrow y Type=Round) se **mantienen en Figma** para uso futuro, sin emparejar con Compose hoy.
- Enlaces verificados: radios del `Content` (0 y 20) y paddings del `State-layer` (0 y wide) enlazados a tokens; iconos y `Focus indicator` enlazados; único residual, trazo morado del marco de documentación del set.

**Set Input de fecha (`51954:18540`), cerrado en esta pasada:**

| Valor | Nodos enlazados | Token | Espejo Compose |
|-------|-----------------|-------|----------------|
| Gap 36 de `Header` | `51954:18555`, `51954:18542`, `61307:1919`, `61307:1961` | `mot/spacing/date-picker/header-gap` (36, `VariableID:61322:8991`) | `MOTSpacing.DatePicker.headerGap` |
| PaddingTop 18 de `Date` y `Date return` | `51954:18560`, `51954:18548`, `61307:1924`, `61307:1966` | `mot/padding/date-picker/field-top` (18, `VariableID:61322:8992`) | `MOTSpacing.DatePicker.fieldTop` |

No están en M3 (`DatePicker`, `1.5.0-alpha12`): los 36 que aparecen son `SelectionYearContainerHeight` del modal, no este gap. Los tokens siguen la regla de valores que se repiten en el set.

**Modal date picker (`51954:18136`), cerrado en esta pasada:**

| Valor | Nodos enlazados | Token | Espejo Compose |
|-------|-----------------|-------|----------------|
| Gap 36 de `Label and Date` | `51954:18218`, `51954:18139` | `mot/spacing/date-picker/label-gap` (36, `VariableID:61322:9118`) | `MOTSpacing.DatePicker.labelGap` |
| Gap 74 de `Local Selection Row` | `51954:18222`, `51954:18143` | `mot/spacing/date-picker/selection-row-gap` (74, `VariableID:61322:9119`) | `MOTSpacing.DatePicker.selectionRowGap` |
| Gap 244 de `Frame 2608508` | `51954:18256` | `mot/spacing/date-picker/block-gap` (244, `VariableID:61322:9120`) | `MOTSpacing.DatePicker.blockGap` |
| PaddingLeft 64 de `Labe, Date & Icon Button` | `51954:18259` | Sin token: valor **interno de M3** (`DateRangePickerTitlePadding`, `start = 64.dp`, privado en `DateRangePicker.kt`) | — |

Los tres gaps son propios del kit y están reservados en Compose, sin consumidor, igual que `headerGap` y `fieldTop`. El 64 se documenta como valor fijo de M3.

**Keyboard picker (`52949:28053`), radios enlazados en esta pasada:**
- `Format=12 hour` (`52949:28054`) y `Format=24 hour` (`52949:28069`): radio 28 → `Corner/Extra-large` (`55064:15592`). Coincide con `shapes.extraLarge` de `MotTimePickerDialog`.
- `.Building Blocks/Period Selector` (`52949:28063`): radio 8 → `Corner/Small` (`55064:15589`). Coincide con `TimeFieldContainerShape` / `PeriodSelectorContainerShape` de M3 (CornerSmall).
- Cuatro `Content` de icon button (`I52949:28067`, `I52949:28068`, `I52949:28080`, `I52949:28081`): radio 100 → `Corner/Full` (`55064:15593`). Con el tamaño del contenedor, ambos valores dibujan un círculo.

**Keyboard picker, pendiente de decidir:**
- `itemSpacing` 20 en los dos `Format` → `mot/spacing/keyboard-picker/gap` (alias de `spacing/20`, `VariableID:61322:9129`), espejo `MOTSpacing.KeyboardPicker.gap`, reservado. El 20 existe como primitivo; faltaba el semántico `mot/spacing/*`.
- `paddingBottom` 20 y `itemSpacing` 121 en `Actions` (`52949:28077`, `52949:28064`): el 20 coincide con `Title` de M3 (`padding(bottom = 20.dp)`, privado), pero el nodo es `Actions`, no el título. El 121 no aparece en M3.
- Radio 10 de `Focus indicator` (instancia de librería): sin token; es la geometría del indicador de la librería.
- Radio 16 de la raíz del set (`52949:28053`): contenedor de documentación, igual que el resto de sets.

**Paridad de botones (3 nov 2026):** `mot/padding/button/small-vertical` pasa de 10 a 8 en Figma (alias de `spacing/8`), igual que el padding vertical de M3 `SmallButtonContentPadding` y `ButtonDefaults.ContentPadding`. Espejo `MOTSpacing.ButtonSmall.vertical` = 8. Medium (16) y Large (32) ya coinciden con M3. Pendiente: los horizontales de Medium y Large sin verificar.

**Decisión (3 nov 2026, código manda):** `mot/padding/button/small-horizontal` pasa de 16 a 24 en Figma (alias de `spacing/24`), igual que el padding por defecto de `MOTButton` (M3 `ContentPadding`: 24 horizontal, 8 vertical). Por tanto, el tamaño Small de Figma es 24/8 y ya no coincide con M3 `SmallButtonContentPadding` (16/8). Espejo `MOTSpacing.ButtonSmall` = 24/8. No cambia el aspecto de la app.

**Uso en app:** Small es el tamaño de `MOTButton`, que ahora lo recibe como `contentPadding` (`MOTButtonSmallContentPadding`). Verificado en fuente M3: el padding por defecto de `Button` es `BaselineButtonTokens.LeadingSpace` (24) y vertical 8, así que el aspecto no cambia. `MOTOutlinedButton` también lo recibe como `contentPadding` (por defecto `MOTButtonSmallContentPadding`). M3 usa el mismo `ButtonDefaults.ContentPadding` en `OutlinedButton`, así que el aspecto no cambia. `MOTTextButton` no se toca: M3 le asigna otro padding (12 horizontal).

**Consumo en Compose: ninguno todavía.** `MOTSpacing.DatePicker.headerGap` y `fieldTop` están definidos como reservados. No se aplicaron a `MotDateRangeField`, porque ese composable es un único `OutlinedTextField` y no tiene la estructura de cabecera más dos campos del set Input; aplicarlos cambiaría la pantalla sin coincidir con Figma. Se consumirán cuando exista un composable con esa estructura.

Pendiente en este set: gap 1 de `Date` (`51954:18557`, `61307:1921`), sin enlazar; y paddingBottom 16 de `Date` y `Date return`, sin revisar.

**Pendiente de decisión:** átomos del kit (Icon button, Text field, Button…) que usa el set. Su revisión la estás haciendo tú; no he tocado Figma.

**Pendiente, fuera de este cierre:**
- Modal date picker: sigue con 6 residuales de kit (36, 74, 244, 64). Sin cambios.
- Keyboard picker: no se ha comprobado su bindings en esta pasada.
- Modo reloj (`Picker`) de Compose: sin set en Figma.
