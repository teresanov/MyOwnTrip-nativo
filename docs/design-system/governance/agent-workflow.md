# Flujo agente — dos frases (+ token nuevo)

**Matriz completa:** [`design-pipeline.md`](design-pipeline.md) §0. **Bitácora:** [`parity-log.md`](../parity-log.md) (sí existe en `docs/design-system/parity-log.md`).

---

## 1. Pantallas (caso habitual ~95 %)

**Compose define la pantalla; Figma espeja con instancias del DS y enlaza variables que ya existen.**

- Reglas: [`figma-instances-only.mdc`](../../../.cursor/rules/figma-instances-only.mdc), [`figma-app-flows.mdc`](../../../.cursor/rules/figma-app-flows.mdc)
- Mapa: [`figma-compose-bindings.md`](../figma-compose-bindings.md)
- Cierre: fila en [`parity-log.md`](../parity-log.md) si hubo delta; bindings si nueva cap

No inventar controles en Figma. No Pencil. No bajar una pantalla “nueva” desde Figma sin Compose previo.

---

## 2. Token nuevo (caso excepcional)

**Si no hay variable enlazable, se define en Figma, se exporta a `variables.json` y el espejo en Compose debe existir antes de cerrar la tarea** (misma tanda; no dejar variables huérfanas).

| Familia | Dónde se nombra primero (práctica) | Espejo obligatorio antes de cerrar |
|---------|-----------------------------------|-------------------------------------|
| **Color / forma tipográfica en variables M3, Shape, Typescale** | **Figma** (`zrGAL4v6`) | `variables.json` → `Color.kt` / `Theme.kt` / `shapes` · `diff-variables-kotlin.py` **0 drift** |
| **`mot/spacing/*`, `mot/padding/*`** | Acuerdo de nombre (suele partir de **`MOTSpacing.kt`** o del cap en Compose) | **MOTSpacing** (o KDoc «Reservado») **y** variable Figma **mismo nombre** · export · sin `mot/*` solo en Figma |
| **Pantalla ya usa literal `16.dp`** | Refactor → token | Sustituir literal en Compose + crear/enlazar variable Figma en la misma tanda |

Pasos comunes al cerrar:

1. Variable en librería Figma + binds en sets/caps que aplican  
2. `python scripts/export_figma_variables_json.py` (o MCP + `--input`)  
3. Espejo Kotlin si aplica (`MOTSpacing`, `Color.kt`, …)  
4. `python .cursor/skills/myowntrip-ds-audit/scripts/diff-variables-kotlin.py` para color  
5. **Append** [`parity-log.md`](../parity-log.md)

Detalle spacing: [`spacing.md`](../spacing.md). Color: [`color.md`](../color.md).

---

## 3. Lo que no es vigente

- Pencil, ARC, `ds-audit` / `ds-index` con `components.index.json`
- `SemanticColors` / `text/*` en UI Compose
- Skills legado sin §0: usar **`myowntrip-ds-parity`** y **`myowntrip-ds-audit`**