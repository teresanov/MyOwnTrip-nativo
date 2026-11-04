# Plan de remediación — disparidades Compose ↔ Figma

**Fecha:** 2026-11-03  
**Relación con el plan general:** no sustituye [compose-figma-parity-plan-2026-10-29.md](compose-figma-parity-plan-2026-10-29.md). Es la **cola activa** cuando el kit Figma (p. ej. date pickers M3) **no** coincide con Compose: prioriza **R1–R4 date/time PRODUCT**; **R6** retoma P1 del plan de paridad (Home menu, swipe, tabs…). Ejecución día a día → [parity-log.md](../parity-log.md).

**Pipeline:** [design-pipeline.md](../governance/design-pipeline.md)  
**Contrato date/time:** [figma-date-product-contract-2026-11-03.md](figma-date-product-contract-2026-11-03.md)

---

## 1. Diagnóstico (tres cubos)

| Cubo | Síntoma | Dirección del arreglo |
|------|---------|------------------------|
| **A. Date/time** | Sets **Input / Modal / Keyboard** M3 ≠ `MotDateRangeField`, `MotDate*Dialog`, `MotTimePickerDialog` | **Figma** (láminas PRODUCT + archivar kit) |
| **B. Tokens huérfanos / kit** | `mot/date-picker/header-gap`, `field-top`, modal gaps 74/244 enlazados al kit; `MOTSpacing` con espejos sin uso en `app/` | **Figma** desenlazar de PRODUCT; opcional limpiar Kotlin más adelante |
| **C. Resto P1** | Home Menu+Search, design-file caps, filas bindings incompletas | Mixto; mayoría **Figma** + auditoría instancias `Vf2tNMXy` |

**No tocar** (salvo aviso tuyo): comportamiento en Kotlin que ya está en producción.

---

## 2. Orden recomendado

```text
R0  Gobernanza en repo (1 h)     → bindings + plan enlazados
R1  Date/time PRODUCT (Figma)   → mayor disparidad; página ya abierta
R2  Tokens date en PRODUCT       → crear/enlazar solo § contrato
R3  figma-compose-bindings      → filas PRODUCT date/time
R4  Design-file pantallas        → Crear/Editar viaje, Wallet form
R5  LEGACY Semantic Layout      → 0 binds en frames producto (al tocar)
R6  P1 restante                 → Menu+Search fila, swipe row, tabs…
```

Cada fase cierra con **auditoría** (`resolveVariables` false/true en subárbol) + entrada en Notion si aplica.

---

## 3. R0 — Repo (hecho / mantener)

- [x] Pipeline § bidireccional acordado
- [x] [figma-date-product-contract-2026-11-03.md](figma-date-product-contract-2026-11-03.md)
- [ ] Actualizar fila date en [figma-compose-bindings.md](../figma-compose-bindings.md) (R3)
- [ ] Showcase `mot-date-*` cuando existan láminas PRODUCT (opcional R3)

---

## 4. R1 — Figma · página «Date & time pickers»

**Objetivo:** lo que ve diseño = lo que corre en app.

### 4.1 Crear sección `PRODUCT` (componentes publicados + lámina doc)

Cada patrón = **`COMPONENT`** en la librería (instanciable en design-file), no solo frame suelto. Átomos internos = instancias de sets P0 (Text field, Button - text, BB calendario…).

| Lámina | Contenido (instancias / composición) | Trazabilidad Compose |
|--------|--------------------------------------|----------------------|
| **PRODUCT · MotDateRangeField** | 1× `Text field` Outlined + label «Fechas del viaje» + trailing icon calendar | `MotDateFormFields.kt` |
| **PRODUCT · MotDateRangePickerDialog** | Surface extraLarge + cabecera (2 textos) + rejilla mes (nav + 7×6) + Cancelar/Aceptar | `MotDatePickerDialogs.kt` + `MotCalendarPicker.kt` |
| **PRODUCT · MotDateField** | Outlined + `MotDatePickerDialog` (single) | Wallet |
| **PRODUCT · MotTimeField + MotTimePickerDialog** | Outlined + dialog: título + TimeInput + toggle + botones | `MotTimePickerDialog.kt` |

Copiar **medidas** del contrato (padding `layout-md`, `component-sm`, `day-grid-gap` 2, `screen-horizontal`, `shapes.extraLarge`).

### 4.2 Mover kit importado

- Renombrar bloque existente `Input date picker` / `Modal date picker` / `Keyboard picker` → **`ARCHIVE · M3 kit (not in app)`**.
- Descripción del set: «No usar en design-file; referencia histórica».

### 4.3 `MotTripDatePickerDialog`

- Sin uso en `app/` → **no** lámina PRODUCT; dejar en ARCHIVE o nota «future» hasta pantalla.

### 4.4 Gate R1

- [ ] 4 láminas PRODUCT publicadas en la página
- [ ] 0 instancias PRODUCT que apunten a variantes del kit Input Range (Header + 2 campos)
- [ ] Gate: screenshot + auditoría subárbol — **registrar en** [parity-log.md](../parity-log.md)

---

## 5. R2 — Variables (solo PRODUCT)

En láminas **PRODUCT**, binds permitidos (crear variable si falta, alias a primitivo):

| Token | Valor | Uso PRODUCT |
|-------|------:|---------------|
| `mot/spacing/screen-horizontal` | 16 | margen dialog |
| `mot/spacing/layout-md` | 16 | cabecera, grid H |
| `mot/spacing/component-sm` | 8 | cabecera, acciones |
| `mot/spacing/component-xs` | 4 | gaps pequeños |
| `mot/spacing/date-picker/day-grid-gap` | 2 | rejilla días |
| Shape / color | `Corner/Extra-large`, `Schemes/*` | Surface, textos |

**Desenlazar en PRODUCT** (dejar solo en ARCHIVE si se quiere):  
`header-gap`, `field-top`, `label-gap`, `selection-row-gap`, `block-gap`, `keyboard-picker/gap`, `time-input/*` salvo que la lámina C replique exactamente el composable.

**Kotlin:** no hace falta añadir `headerGap`/`fieldTop` en app; opcional deprecar en `MOTSpacing.kt` cuando Figma ARCHIVE esté cerrado.

---

## 6. R3 — Bindings + inventario

1. Sustituir fila única «Modal / Input date picker» en `figma-compose-bindings.md` por **4 filas PRODUCT** (sin node-id del kit como SoT).
2. Marcar [tokens-inventory-complete-2026-11-03.md](tokens-inventory-complete-2026-11-03.md) tokens kit-only como **ARCHIVE**, no «reserva producto».
3. Regenerar inventario si cambia variable count: `python scripts/generate_tokens_inventory.py`.

---

## 7. R4 — Design-file `Vf2tNMXy`

| Pantalla | Sustituir | Por |
|----------|-----------|-----|
| Crear / editar viaje | Cualquier Input date picker Range del DS | Instancia doc **PRODUCT · MotDateRangeField** (+ modal en prototipo o cap aparte) |
| Wallet form | Idem fecha/hora | PRODUCT · MotDateField / MotTimeField |

Gate: cada control trazable a línea Compose ([figma-instances-only.mdc](../../.cursor/rules/figma-instances-only.mdc)).

---

## 8. R5 — LEGACY · Semantic Layout (55)

Política existente: al abrir un frame de producto, migrar binds a `mot/*` o M3.  
Auditoría puntual: grep en plugin / reporte «frames con LEGACY» en design-file (tarea aparte).

---

## 9. R6 — Otros gaps P1 (después de date)

Del [compose-figma-parity-plan-2026-10-29.md](compose-figma-parity-plan-2026-10-29.md) §4 P1:

1. Fila **Search bar + Menu** en bindings (Home)
2. **List Item - Swipe** ↔ `SwipeableTripListRow`
3. **Tabs** ↔ `MOTTab` / Trip detail
4. Revisar caps Wallet ya auditados vs instancias actuales

---

## 10. Cierre global

Paridad cerrada cuando:

1. Inventario PRODUCT date/time = 0 binds pendientes en auditoría.
2. Design-file flujos P0 usan instancias PRODUCT o sets P0 ya auditados (Button, Text field, Dialog…).
3. `figma-compose-bindings.md` refleja la tabla sin referencias engañosas al kit date.
4. Tokens de color: export `variables.json` al día si tocaste M3 en Figma.

---

## 11. Si cambias algo en Figma (bidireccional)

Aviso explícito → actualizar Compose → revalidar R1–R4 para ese componente. Sin aviso, no aplicar paridad inversa.
