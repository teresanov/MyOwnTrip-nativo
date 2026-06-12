# Color — roles M3 (MyOwnTrip Nativo)

**Fuente canónica:** [`variables.json`](variables.json) (colección M3 · modos Light/Dark).  
**Código:** `app/.../ui/theme/Color.kt` → `Theme.kt` (importado desde `variables.json`).  
ADR: [002-brand-editorial-m3.md](../decisions/002-brand-editorial-m3.md).

> **Figma:** [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=55141-14168) — misma colección M3 que `variables.json`.

## Workflow (fuente de verdad)

```
Figma (Desktop Bridge fija variables) → export → variables.json → Color.kt
```

**No** usar Material Theme Builder como bucle principal de color: re-harmoniza `primary` a azul (`#276389`) y Dark a celeste (`#98CCF9`). MTB solo para exploración inicial o neutros; **bloquear ocre** `tertiary` `#825513` si generas.

Script Bridge: `.cursor/skills/myowntrip-ds-audit/scripts/figma-fix-gris-azul-colors.js` · Auditoría: `docs/design-system/audits/figma-color-gris-azul-bridge-2026-06-11.md`

## Material Theme Builder (parámetros)

| Parámetro | Valor |
|-----------|-------|
| Scheme | Tonal Spot |
| Contrast | Default (+ Medium/High en Figma) |
| Modos | Light + Dark |
| Generate state layers | **NO** |
| Neutros seeds (si regeneras) | `#8A8275` / `#847E72` |

### Roles Light (desde `variables.json` · `Schemes/*`)

| Rol | Hex | Notas |
|-----|-----|-------|
| `primary` | `#4A5864` | Gris-azul tinta (decisión jun 2026) |
| `primaryContainer` | `#E4E1DC` | Tonal — gris cálido, sin celeste |
| `onPrimaryContainer` | `#2E363D` | Carbón sobre contenedor |
| `tertiary` | `#825513` | Acento UI (botones, chips) |
| `Brand/ocre` | `#C48328` | Logo/cinta — Palettes/Tertiary 60 (ver [`brand.md`](brand.md)) |
| `error` | `#904A42` | Alerta; **distinto** de tertiary |
| `surface` | `#FFF8F2` | Papel cálido |
| `onSurface` | `#1F1B13` | |

Dark: `primary` `#B4BAC2`, `primaryContainer` `#3A444C`, `tertiary` `#F8BB71`, `surface` `#17130B` — ver `variables.json`.

### Paleta gris-azul (jun 2026)

Decisión de producto tras laboratorio [`previews/color-ink-comparison.html`](previews/color-ink-comparison.html): sustituir celeste pastel por **gris-azul** en roles `primary*`, conservar **ocre** en `tertiary*`. Aplicado en Figma vía Bridge (2026-06-12).

### Check de superficie

Tras generar Light, `surface` debe aproximarse a **papel cálido** `~#F4F0E8`.  
Si sale frío → subir Neutral a `#8C8472` y regenerar.

## Extensiones custom

Vía `LocalExtendedColors` en `Theme.kt`. No son roles M3.

| Token | Light | Dark | Uso |
|-------|-------|------|-----|
| success | `#0D631B` | `#88D982` | Feedback positivo |
| warning | `#815611` | `#F6BC70` | Avisos |
| info | `#405F90` | `#A9C7FF` | Información (opcional) |

Valores de `Extended Colors/*` en `variables.json`. No importar `State Layers/*` a código.

## Consumo en UI

- **Sí:** `MaterialTheme.colorScheme.primary`, `.onSurface`, `.error`, etc.
- **No:** constantes `AppColors.*` en pantallas (solo en `Theme.kt`).
- **No:** primitivos MTB ni tonos sueltos.
- **No:** tokens por estado (`primary.hover`, `text/disabled`, …).

## State layers (runtime, no tokens)

Aplicados por componentes M3 o `Modifier` compartido en custom.

| Estado | Opacidad sobre `on*` |
|--------|----------------------|
| Hover | 8% |
| Focus | 10% |
| Pressed | 10% |
| Dragged | 16% |
| Disabled contenido | 38% |
| Disabled contenedor | 12% |

## Error y acento

| Rol | Uso |
|-----|-----|
| `error` / `onError` | Validación, fallos — **siempre** con icono + texto |
| `tertiary` | Acento de marca puntual (CTA secundario editorial, énfasis) — no sustituye `error` |

## Tabla completa

49 roles `Schemes/*` por modo en [`variables.json`](variables.json). Regenerar `Color.kt` tras cada export de Figma.
