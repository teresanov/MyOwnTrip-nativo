# Governance — Design System

## Source of truth

| Capa | Fuente | Consumo |
|------|--------|---------|
| Color roles | `variables.json` → `Color.kt` → `Theme.kt` | `MaterialTheme.colorScheme` |
| Tipografía | `res/font/` → `Type.kt` | `MaterialTheme.typography` |
| Librería visual | Figma DS (variables + component sets) | Diseño de pantallas |
| **Documentación componentes** | **Showcase externo** | Equipo + handoff dev |
| Implementación | M3 Compose | `androidx.compose.material3.*` |
| UX runtime | `docs/ux/android-compose-ux.md` | Pantallas `app/` |
| Producto | `docs/product/jtbd-flows.md` | Features |

Figma **no** documenta componentes (sin láminas Do/Don't en el archivo DS). ADR [003](../decisions/003-figma-library-showcase-docs.md).

Repo archivado `MyOwnTrip`: consulta histórica, **no** fuente operativa.

---

## Gate: `m3Canonical` (bloqueante)

Aplica a **todo** cambio de UI, Figma o tema. Si falla una regla, la tarea no se cierra.

| # | Regla | Verificación |
|---|-------|----------------|
| 1 | **Prohibido** tokens de color por estado (`hover`, `pressed`, `focus`, `disabled` como color guardado) | Figma: sin variables `*/hover`, `*/pressed`, … · Código: sin constantes ni `Color` por estado |
| 2 | Estados = **state layers en runtime** sobre `on*` (8% hover · 10% focus · 10% pressed · 16% dragged); disabled = opacidad 38%/12%, nunca un color guardado | Componentes M3 o `Modifier` compartido · MTB: Generate state layers = NO |
| 3 | UI consume **solo roles semánticos M3** (`primary`, `onSurface`, `error`, …); **jamás** primitivos/tonos MTB ni `AppColors` fuera de `Theme.kt` | Pantallas usan `MaterialTheme.colorScheme.*` |
| 4 | **`tertiary`** (acento marca, ocre en `variables.json`) y **`error`** (alerta) son roles distintos — no mezclar usos | Validación → `error` · Acento editorial → `tertiary` |
| 5 | Errores: **icono + texto**; nunca solo color | `isError` + `supportingText` con icono, o patrón equivalente |
| 6 | Tipografía: **Fraunces** (Display/Headline) + **Inter** (Title/Body/Label); serif solo arriba | `Type.kt` + sin Fraunces en botones/campos/chips |

### Checklist rápida (agente)

```
[ ] Sin tokens */hover|pressed|focus|disabled en Figma ni código
[ ] State layers solo en runtime (8/10/10/16%; disabled 38%/12%)
[ ] Pantallas: solo MaterialTheme.colorScheme / typography
[ ] tertiary ≠ error en uso semántico
[ ] Campos con error: icono + texto visible
[ ] Display/Headline = Fraunces; resto = Inter
```

---

## Quality gates (complementarias)

Ejecutar **después** de pasar `m3Canonical`.

| Gate | ID | Criterio | Cuándo |
|------|-----|----------|--------|
| Consumo semántico | `semanticTokenOnlyConsumption` | Mismo que `m3Canonical` §3; grep sin `AppColors` en `ui/features/` | Cada PR UI |
| Cadena Figma | `figmaAliasChainValid` | Variables aliasan a roles M3; sin tokens por estado; **sin páginas de doc** en Figma | Cambio en librería Figma |
| Ficha showcase | `showcaseDocComplete` | Componente nuevo/actualizado tiene ficha en showcase (plantilla en `showcase.md`) | Cada componente |
| Mapeo Compose | `composeMaterial3MappingValid` | `lightColorScheme`/`darkColorScheme` cubren roles usados; custom solo `LocalExtendedColors` | Cambio de tema |
| Accesibilidad | `a11yCheck` | TalkBack, contraste, touch 48dp, escalado 200%; incluye `m3Canonical` §5 | Cada pantalla |
| Regresión visual | `visualRegression` | Showcase vs emulador (subset MVP) | Tras cambio visual mayor |

**Orden:** `m3Canonical` → `semanticTokenOnlyConsumption` → resto según alcance.

---

## State layers (referencia)

| Estado | Opacidad sobre color `on*` |
|--------|---------------------------|
| Hover | 8% |
| Focus | 10% |
| Pressed | 10% |
| Dragged | 16% |
| Disabled contenido | 38% |
| Disabled contenedor | 12% |

Detalle: [color.md](color.md).

## Focus ring

Capa visual separada (no token de estado). Implementación futura; documentar en ADR al añadirse.

## Cierre de tarea UI (agente)

1. Pasar gate **`m3Canonical`** (checklist arriba).
2. Leer `m3-native-ui.mdc` y `android-compose-ux.mdc`.
3. Ejecutar gates complementarias según tabla.
4. Si cambian seeds o roles → actualizar [color.md](color.md) y `Color.kt`.
