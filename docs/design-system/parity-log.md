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
| date/time · R1 | Láminas PRODUCT: `MotDateField` + modal, `MotTimeField` + modal |
| date/time · R3 | Filas date/time en `figma-compose-bindings.md` (node-ids láminas PRODUCT) |
| date/time · R4 | Design-file Crear/editar viaje + Wallet (instancias PRODUCT) |

---

## Registro (más reciente arriba)

| Fecha | Ámbito | Cambio | Evidencia (Figma / código) | Estado |
|-------|--------|--------|----------------------------|--------|
| 2026-11-04 | date/time | **Fichas diseñador** (Dev Mode + contrato §2–3): `MotDateRangeField` / `MotDateRangePickerDialog` — anatomía, UX, tokens (`mot/spacing/*`, Schemes, celdas); sin decisiones de implementación Figma | Figma Console · `figma-date-product-contract-2026-11-03.md` | Hecho |
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
