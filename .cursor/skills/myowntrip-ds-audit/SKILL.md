---
name: myowntrip-ds-audit
description: Audita el design system M3 nativo de MyOwnTrip (Figma ↔ variables.json ↔ Kotlin). Detecta drift de color, bindings, violaciones m3Canonical y legado del repo archivado. Usar al auditar DS, revisar variables, cerrar tareas UI, o cuando el usuario pida auditoría Figma/Compose sin Pencil ni semánticos custom.
---

# MyOwnTrip — Auditoría DS M3 nativo

## Qué es este DS (y qué NO es)

- **Sí:** M3 canónico adaptado a marca editorial (Figma Bridge → `variables.json` → Compose).
- **Sí:** Roles `Schemes/*`, extensiones `Extended Colors/*`, componentes `androidx.compose.material3.*`.
- **No:** Semánticos custom (`SemanticColors`, `text/primary`, `border/strong`).
- **No:** Pencil, ARC, pipeline `ds-audit` del repo **`../MyOwnTrip`**.
- **No:** Importar `State Layers/*` a Kotlin — Compose aplica opacidades en runtime.

## Fuente canónica

| Capa | Archivo |
|------|---------|
| Variables | `docs/design-system/variables.json` |
| Tema Kotlin | `app/.../ui/theme/Color.kt` → `Theme.kt` |
| Reglas | `docs/design-system/governance.md`, `.cursor/rules/m3-native-ui.mdc` |
| Figma | [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System) |

Si `variables.json` y otra doc discrepan, **gana `variables.json`**.

---

## Cuándo ejecutar

- Tras exportar variables desde Figma → actualizar `variables.json`.
- Tras cambiar seeds en MTB o paleta en Figma.
- Antes de cerrar tarea UI o PR con cambios visuales.
- Cuando el usuario reporte colores “que no se actualizaron” (típico: paint styles hardcoded vs variables).

---

## Flujo de auditoría (orden fijo)

```
1. variables.json (baseline)
2. Color.kt / Theme.kt (paridad Kotlin)
3. app/ (consumo m3Canonical)
4. Figma opcional (figma-console MCP, Desktop Bridge conectado)
5. Informe + clasificación de hallazgos
```

### Paso 1 — Baseline JSON

Leer `variables.json` · colección `M3` · modos **Light** y **Dark**.

**Importan a código:**
- `Schemes/*` → `colorScheme`
- `Extended Colors/Success|Warning|Info` → `LocalExtendedColors`

**Solo Figma (no exportar a Kotlin):**
- `State Layers/*`
- `Palettes/*` (referencia; swatches deben enlazar variables)
- `Static/*`, `Corner/*`, modos contrast/Pink/Blue… (salvo requisito explícito)

Ejecutar diff automático:

```bash
python3 .cursor/skills/myowntrip-ds-audit/scripts/diff-variables-kotlin.py
```

### Paso 2 — Paridad Kotlin

Verificar:
- `lightColorScheme` / `darkColorScheme` cubren roles usados en `app/`.
- `AppColors` solo en `Theme.kt` / `Color.kt`.
- Extensiones Light/Dark en `MyOwnTripTheme`.

Si el script reporta drift → regenerar `Color.kt` desde `Schemes/*` del JSON.

### Paso 3 — Gate `m3Canonical` (código)

```bash
# AppColors fuera de tema
rg "AppColors\." app/src/main/java --glob "*.kt" | rg -v "ui/theme/"

# Primitivos / hex sueltos en UI (revisar manualmente falsos positivos)
rg "Color\(0x" app/src/main/java/com/myowntrip/app/ui/features

# Tokens de estado prohibidos
rg -i "hover|pressed|focus|disabled" app/src/main/java --glob "*Color*"
```

Checklist completa: [checklist.md](checklist.md).

### Paso 4 — Figma (opcional)

Requiere **figma-console** + Desktop Bridge en el archivo DS.

Auditar según alcance:

| Objetivo | Qué revisar |
|----------|-------------|
| Paleta tonal | Rectángulos enlazados a `Palettes/*`, no hex fijo |
| Componentes activos | Styles `M3/sys/light/*` **variable-bound** a `Schemes/*` |
| Legado | Paint styles huérfanos (hardcoded de kit M3 rosa, duplicados) |
| State layers en librería | Variables `State Layers/*` usadas en Figma; **no** exigirlas en Kotlin |

Scripts ligeros por lote (evitar `loadAllPagesAsync` global — cuelga el plugin).

### Paso 5 — Informe

Usar plantilla de [checklist.md](checklist.md) § Informe.

Clasificar cada hallazgo:

| Severidad | Criterio |
|-----------|----------|
| **Bloqueante** | Falla `m3Canonical`; drift `Schemes/*` en código activo |
| **Deuda Figma** | Hardcoded bindable; style legacy sin uso |
| **Doc** | `color.md`, `tokens.json`, `governance.md` desalineados con JSON |
| **OK Figma-only** | `State Layers/*` solo en librería |

---

## Prohibido en esta auditoría

- Citar o importar contratos de `../MyOwnTrip` (salvo consulta humana puntual).
- Exigir paridad con Pencil o `components.index.json`.
- Recomendar tokens `primary.hover` o variables de opacidad en `Color.kt`.
- Tratar `Palettes/Tertiary 40` como rol de UI (usar `Schemes/Tertiary`).

---

## Cierre de tarea UI

1. `diff-variables-kotlin.py` → 0 drift en `Schemes/*` usados.
2. Checklist `m3Canonical` pasada.
3. Gates complementarias según `governance.md` (alcance del cambio).
4. Si cambió el tema → `variables.json` + `Color.kt` + `color.md` sincronizados.

---

## Referencias

- Checklist detallada: [checklist.md](checklist.md)
- ADR estructura: `docs/decisions/001-m3-native-ds.md`
- ADR marca: `docs/decisions/002-brand-editorial-m3.md`
- UX runtime: `docs/ux/android-compose-ux.md`
