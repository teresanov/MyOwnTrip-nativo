---
name: myowntrip-ds-parity
description: Agente de paridad DS MyOwnTrip nativo — Compose SoT, Figma espejo, showcase externo. Usar en tareas Compose↔Figma, design-file (caps), tokens export, bindings o cierre en parity-log. No inventar UI en Figma; no crear audits fechados nuevos.
---

# Agente de paridad DS — MyOwnTrip nativo

**Rol:** mantener paridad **Compose ↔ Figma ↔ showcase** sin desviar producto ni duplicar bitácoras.

| Capa | SoT |
|------|-----|
| Comportamiento / pantallas | **Compose** (`app/`, `designsystem/`) |
| Tokens base (M3, MOT, Shape, Typescale) | **Figma** `zrGAL4v6` → `variables.json` |
| Pantallas hi-fi | **Figma** design-file `Vf2tNMXy` (solo instancias DS) |
| Ficha de componente | **Showcase** `ds-showcase/` |
| Historial de paridad | **`docs/design-system/parity-log.md`** (único append) |

**Estado de referencia:** nov 2026 · `main` @ `6bf4f5f` — cola **Pendiente** vacía; LEGACY Semantic Layout **no** existe en Figma; no reabrir R5.

---

## Lectura obligatoria (orden fijo, antes de tocar Figma o docs)

1. [`docs/design-system/governance/design-pipeline.md`](../../../docs/design-system/governance/design-pipeline.md)
2. [`docs/design-system/figma-compose-bindings.md`](../../../docs/design-system/figma-compose-bindings.md)
3. [`docs/design-system/parity-log.md`](../../../docs/design-system/parity-log.md)
4. [`docs/design-system/audits/README.md`](../../../docs/design-system/audits/README.md) (solo contratos vivos; sin planes cerrados)

Mapas activos: **02 crear** (`trip_create`) · **03 editar** (`EditTripScreen`, caps 1–4) · módulos **04–08** → bindings + [`.cursor/rules/figma-app-flows.mdc`](../../rules/figma-app-flows.mdc).

---

## Gates (no negociables)

| Gate | Regla |
|------|--------|
| Instancias | [`.cursor/rules/figma-instances-only.mdc`](../../rules/figma-instances-only.mdc) |
| Bindings | [`.cursor/rules/utilities/ds-binding-governance.mdc`](../../rules/utilities/ds-binding-governance.mdc) |
| Runtime UI | [`.cursor/rules/m3-native-ui.mdc`](../../rules/m3-native-ui.mdc) |
| Iconos | [`.cursor/rules/utilities/icon-color-consumption.mdc`](../../rules/utilities/icon-color-consumption.mdc) |

**Figma sets:** auditar subárbol con `resolveVariables` **false** y **true** antes de cerrar.

**Pantallas:** padding/gaps del `phone` → `mot/spacing/*` (sin literales entre caps).

---

## Flujo por tanda

```
1. Leer composable(s) + @Preview del ámbito (línea a línea)
2. Contrastar figma-compose-bindings + parity-log Pendiente
3. Ejecutar cambio (Figma MCP / Figwright / repo scripts)
4. Verificar gates + screenshot si hay cambio visual
5. Si tocaste variables Figma → export variables.json
6. Append fila en parity-log Registro (Resuelto)
```

### Export tokens

```bash
python scripts/export_figma_variables_json.py
```

Requisito: Bridge abierto en DS `zrGAL4v6`. Tras export, opcional: `python .cursor/skills/myowntrip-ds-audit/scripts/diff-variables-kotlin.py`.

### Cierre en bitácora (plantilla)

| Fecha | Ámbito | Cambio | Evidencia | Estado |
|-------|--------|--------|-----------|--------|
| YYYY-MM-DD | ámbito corto | qué | node-id · archivo · tool | **Resuelto** |

---

## Archivos Figma

| Archivo | Key corto | Uso |
|---------|-----------|-----|
| Design System | `zrGAL4v6` | Sets, variables, publish |
| design-file | `Vf2tNMXy` | Caps por módulo `02`–`09` |

Scripts design-file de referencia: `scripts/figma-design-file-*.js` (patrón instancias + `setProperties`).

---

## Subagentes (cuándo delegar)

El agente **principal** cierra parity-log y no hace commits salvo petición. Delegar solo para acotar exploración o auditoría paralela.

| Subagente | Cuándo | Brief mínimo |
|-----------|--------|----------------|
| **explore** (`medium`/`very thorough`) | Localizar composables, previews, strings, rutas de un cap/módulo | Ruta Figma cap + nombre pantalla Compose; devolver lista control→archivo:línea |
| **explore** (`quick`) | ¿Existe fila en bindings? ¿Script similar en `scripts/`? | Keyword + módulo |
| **generalPurpose** | Paridad multi-módulo (p. ej. auditar caps 04–08) con informe único | Gates + node-ids; **no** editar Figma en el hijo si el padre orquesta MCP |
| **myowntrip-ds-audit** (skill, no Task) | Drift `variables.json` ↔ `Color.kt` tras export | Ejecutar diff script + clasificar hallazgos |
| **ds-semantic-pipeline** (skill) | Solo si creas/cambias colecciones o alias de token en Figma | Modo B Figma-first |

**No delegar:** commits/push, JTBD producto nuevo, event bookmark, ampliar `figma-*-2026-*` históricos (solo corrección factual).

**No usar:** Bugbot/Security Review salvo petición explícita del usuario.

---

## No hacer

- Crear `figma-*-YYYY-MM-DD.md` ni audits por discrepancia → solo **parity-log**
- Inventar controles en design-file (frames que imiten M3)
- Reabrir migración LEGACY Semantic Layout (R5 descartado)
- Paridad inversa Figma→Compose **sin aviso explícito** del usuario (ver design-pipeline §1)
- Documentar componentes en láminas Figma (showcase externo)

---

## Relación con otras skills

| Skill | Distinción |
|-------|------------|
| `myowntrip-ds-audit` | Drift tokens/Kotlin/m3Canonical; informe técnico |
| `ds-audit` | Gates ARC/index (repo legacy index); no sustituye parity-log |
| `ds-semantic-pipeline` | Pipeline alias Figma; no flujo de caps |
| `myowntrip-context` | Producto/JTBD; cargar si hay duda de scope producto vs paridad |