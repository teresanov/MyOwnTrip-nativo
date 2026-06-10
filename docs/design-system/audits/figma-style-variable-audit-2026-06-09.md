# Auditoría Figma — Color styles vs variables M3

**Archivo:** [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432) · `zrGAL4v6MEMc9hzZemU432`  
**Fecha:** 2026-06-09  
**Herramienta:** figma-console (Desktop Bridge) · script de barrido recursivo en 27 páginas activas  
**Alcance:** nodos dentro de `COMPONENT` / `COMPONENT_SET` (no instancias de producto)

---

## Resumen ejecutivo

| Métrica | Valor |
|---------|------:|
| Páginas escaneadas | 27 |
| Component sets con fills vía **style** | 153 |
| Nodos de fill vía **paint style** | 20 408 |
| Nodos de fill vía **variable** directa | 9 855 |
| Nodos de fill **hex crudo** (sin style ni var) | 908 |
| Paint styles totales en archivo | 2 271 |
| **Styles usados por componentes** | **103** |
| └ variable-backed (fill enlazado a var) | 62 |
| └ **hex-fixed** (prioridad FASE A) | **41** |
| Styles usados con **drift hex ≠ variable** | **0** |

**Hallazgo principal:** no hay desincronización de *valores* en los styles activos (hex coincide con `Schemes/*` en Light). El problema es **arquitectural**: ~67 % de los fills de componentes pasan por paint styles; **41 styles activos** no están enlazados a variable (aunque el hex sea correcto hoy). Existe un **par duplicado crítico** de state-layer por convención de nombre (`/` con espacios vs compacto).

---

## 1. Component sets con fills enganchados a STYLES

Ordenados por nodos con `fillStyleId` (top 40). Columna `% style` = share style vs variable en ese set.

| Component set | Nodos style | Nodos var | % style |
|---------------|------------:|----------:|--------:|
| Centered slider | 1 940 | 0 | 100 % |
| Standard slider | 1 720 | 0 | 100 % |
| Range slider | 1 080 | 0 | 100 % |
| List item/List Item: 0 Density (baseline) | 995 | 655 | 60 % |
| List item/List Item: -2 Density (baseline) | 907 | 599 | 60 % |
| List item/List Item: -4 Density (baseline) | 820 | 541 | 60 % |
| Split button | 590 | 360 | 62 % |
| Keyboard | 577 | 80 | 88 % |
| Icon button togglable | 540 | 600 | 47 % |
| Icon button togglable - tonal | 538 | 602 | 47 % |
| Standard button group | 490 | 630 | 44 % |
| List | 457 | 184 | 71 % |
| Text field | 436 | 80 | 84 % |
| Icon button togglable - outline | 389 | 600 | 39 % |
| Menu | 378 | 182 | 68 % |
| Toggle button - tonal | 282 | 101 | 74 % |
| Toggle button | 280 | 100 | 74 % |
| Toggle button - elevated | 280 | 100 | 74 % |
| Icon button | 270 | 150 | 64 % |
| Icon button - tonal | 270 | 150 | 64 % |
| Modal date picker | 263 | 17 | 94 % |
| Tabs | 258 | 51 | 83 % |
| Filter chip | 248 | 120 | 67 % |
| Toggle button - outline | 240 | 100 | 71 % |
| Icon button togglable - standard | 240 | 600 | 29 % |
| Extended FAB | 216 | 72 | 75 % |
| Input chip | 166 | 16 | 91 % |
| Button | 140 | 50 | 74 % |
| Button - elevated | 140 | 50 | 74 % |
| Button - tonal | 140 | 50 | 74 % |
| Assistive chip | 132 | 24 | 85 % |
| Suggestion chip | 124 | 24 | 84 % |
| FAB | 144 | 72 | 67 % |
| Search bar | (en top 40 extendido) | — | — |
| Snackbar | (mixto) | — | — |

**MVP Must afectados** (según `figma-prune-inventory.md`): Button*, Text field, Filter/Assist/Suggestion/Input chip, List item*, Menu, Tabs, Icon button*, FAB/Extended FAB, Modal date picker, Snackbar, Switch, Checkboxes, Navigation Bar*, Bottom sheet, Search bar.

**Sets 100 % style (candidatos fuertes FASE B):** Centered/Standard/Range slider, Keyboard (88 %).

---

## 2. Inventario de styles usados por componentes (103)

### 2.1 Por binding del fill del style

| Binding | Count | Nodos afectados |
|---------|------:|----------------:|
| `variable-backed` | 62 | ~19 500 |
| `hex-fixed` | 41 | ~900 |

### 2.2 Top 10 styles por uso

| Style | Binding | Variable / mapeo | Usos |
|-------|---------|------------------|-----:|
| `M3 / sys / light / on-surface` | variable-backed | `Schemes/On Surface` | 2 903 |
| `M3 / sys / light / on-secondary-container` | variable-backed | `Schemes/On Secondary Container` | 2 375 |
| `M3 / sys / light / on-surface-variant` | variable-backed | `Schemes/On Surface Variant` | 2 239 |
| `M3 / sys / light / primary` | variable-backed | `Schemes/Primary` | 1 630 |
| `M3 / sys / light / secondary-container` | variable-backed | `Schemes/Secondary Container` | 1 406 |
| `M3 / sys / light / surface` | variable-backed | `Schemes/Surface` | 990 |
| `M3 / sys / light / on-primary` | variable-backed | `Schemes/On Primary` | 846 |
| `M3 / state-layers / light / onSurface / opacity-0.10` | variable-backed | `State Layers/On Surface/Opacity-10` | 604 |
| `M3 / state-layers / light / onPrimary / opacity-0.08` | variable-backed | `State Layers/On Primary/Opacity-08` | 505 |
| `M3 / state-layers / light / onSurfaceVariant / opacity-0.10` | variable-backed | `State Layers/On Surface Variant/Opacity-10` | 477 |

---

## 3. Prioridad — styles hex-fixed usados por componentes

**Sin drift de valor** respecto a la variable M3 equivalente en Light (riesgo = no se actualizan al cambiar tema).

### P0 — alto impacto

| Style | Usos | Variable M3 objetivo (FASE A) | Notas |
|-------|-----:|-------------------------------|-------|
| `M3/state-layers/light/onSurfaceVariant/opacity-0.08` | **437** | `State Layers/On Surface Variant/Opacity-08` | Duplicado compacto sin binding; existe paralelo variable-backed con espacios (`M3 / state-layers / light / onSurfaceVariant / opacity-0.08`) |
| `M3 / ref / neutral / neutral100` | 44 | `Palettes/Neutral 100` | Carousel; primitivo ref, no `Schemes/*` |

### P1 — familia duplicada `M3/sys/light/*` (compacta, sin binding)

Convención **sin espacios**; espejo de `M3 / sys / light / *` que sí está variable-backed. Cada uno ~1–19 usos en nodos sueltos (building blocks / páginas auxiliares).

| Style compacto | Usos | `Schemes/*` equivalente |
|----------------|-----:|------------------------|
| `M3/sys/light/on-surface` | 19 | `Schemes/On Surface` |
| `M3/sys/light/surface` | 7 | `Schemes/Surface` |
| `M3/sys/light/on-primary-container` | 5 | `Schemes/On Primary Container` |
| `M3/sys/light/inverse-surface` | 5 | `Schemes/Inverse Surface` |
| `M3/sys/light/primary` (+ 20 roles más) | 3 c/u | ver rol homólogo en `Schemes/*` |

Lista completa P1 (todos hex-fixed, 3 usos salvo los de tabla):  
`primary`, `on-primary`, `primary-container`, `secondary`, `on-secondary`, `secondary-container`, `on-secondary-container`, `error`, `on-error`, `error-container`, `on-error-container`, `inverse-on-surface`, `primary-fixed`, `on-primary-fixed`, `primary-fixed-dim`, `on-primary-fixed-variant`, `secondary-fixed`, `on-secondary-fixed`, `secondary-fixed-dim`, `on-secondary-fixed-variant`, `on-surface-variant`, `outline`, `outline-variant`, `shadow`, `scrim`, `inverse-primary`, `surface-dim`, `surface-bright`, `surface-container-lowest`, `surface-container-low`, `surface-container`, `surface-container-high`, `surface-container-highest`.

### P2 — primitivos / utilidad

| Style | Usos | Variable objetivo |
|-------|-----:|-------------------|
| `M3/white` | 2 | `Static/White` o primitivo (revisar colección) |
| ~~`M3 / black`~~ | 0 | Migrado a `Palettes/Neutral 0` (2026-06-09) |

---

## 4. Drift hex vs variable (prioridad bloqueante)

**Resultado: 0 styles usados con drift** en modo Light.

Los 41 hex-fixed tienen el mismo hex que su variable `Schemes/*` o `State Layers/*` homóloga **hoy**. El riesgo es **futuro** (cambio de tema sin propagación), no inconsistencia visual actual.

---

## 5. Patrón raíz detectado

Google/MTB mantuvo **dos convenciones de nombre** en el mismo archivo:

| Canónico (variable-backed) | Duplicado (hex-fixed) |
|----------------------------|------------------------|
| `M3 / sys / light / primary` | `M3/sys/light/primary` |
| `M3 / state-layers / light / onSurfaceVariant / opacity-0.08` | `M3/state-layers/light/onSurfaceVariant/opacity-0.08` |

Los componentes consumen mayoritariamente la forma **con espacios** (ya variable-backed). La forma **compacta** queda como trampa para diseñadores y para FASE B.

---

## 6. Plan en 2 fases

### FASE A — Styles → variable-backed (no tocar componentes)

**Objetivo:** cada paint style usado por componentes tiene su fill enlazado a la variable M3 del rol correcto.

| Paso | Acción |
|------|--------|
| A.1 | Snapshot / branch Figma antes de tocar |
| A.2 | **P0:** vincular `M3/state-layers/light/onSurfaceVariant/opacity-0.08` → `State Layers/On Surface Variant/Opacity-08` |
| A.3 | **P1:** batch sobre familia `M3/sys/light/*` → `Schemes/{Rol}` homólogo a `M3 / sys / light / {rol}` |
| A.4 | **P0 ref:** `M3 / ref / neutral / neutral100` → `Palettes/Neutral 100` (o alias a primitivo acordado) |
| A.5 | Verificar con re-ejecución de esta auditoría: `hex-fixed` usados → 0 (excepto excepciones P2 documentadas) |
| A.6 | **No borrar** ningún style en FASE A |

**Herramientas sugeridas:** CodeFig (*Replace style variable bindings*), Styles to Variables Converter (backing), o `figma_execute` batch por lista P0/P1.

**Mapeo rol style → variable:**

```text
M3 / sys / light / {kebab-role}  →  Schemes/{Title Case Role}
M3/sys/light/{kebab-role}        →  Schemes/{Title Case Role}   (misma regla)
M3/state-layers/light/{role}/opacity-0.XX  →  State Layers/{Title Case Role}/Opacity-XX
M3 / state-layers / light / {role} / opacity-0.XX  →  (idem)
```

### FASE B — Componentes: style → variable directa (bloque)

**Objetivo:** fills de componentes referencian `Schemes/*` / `State Layers/*` directamente; styles quedan huérfanos hasta verificación.

| Paso | Acción |
|------|--------|
| B.1 | Orden sugerido por impacto MVP: Button* → Text field → Chips → List item* → Icon button* → Tabs → Menu → FAB → Snackbar |
| B.2 | Por component set: swap `fillStyleId` y `strokeStyleId` → `boundVariables` en paint apuntando a la variable ya usada por el style (post FASE A) |
| B.3 | Sets 100 % style primero: Sliders, Keyboard |
| B.4 | Validar Light/Dark en subset (Button, TextField, FilterChip, ListItem) |
| B.5 | Solo tras B.4 OK: marcar styles sin uso para poda (no antes) |

**Herramientas sugeridas:** Apply variables, StilTausch (match por nombre tras normalizar), MTB v21 *variable-backed swap*, o script `figma_execute` por set.

**Regla:** no eliminar paint style hasta que `usedByComponents === 0` en auditoría.

---

## 7. Criterios de cierre

- [x] **FASE A ejecutada 2026-06-09** — 192 styles hex-fixed enlazados a variable (script `figma-phase-a-bind-styles.js`). P0 `onSurfaceVariant/opacity-0.08` → `State Layers/On Surface Variant/Opacity-08` ✓
- [x] FASE A (formal): 0 styles **usados por nodos** con `binding: hex-fixed` — **cumplido** (`M3 / black` migrado a `Palettes/Neutral 0`)
- [x] **FASE B ejecutada 2026-06-09** — `setFillStyleIdAsync('')` en árboles `COMPONENT` (script `figma-phase-b-detach-fill-styles.js`). Post-check componentes: **30 260** fills vía variable directa; **908** hex crudo sin style (inalterado).
- [x] **`M3 / black` → `Palettes/Neutral 0` 2026-06-09** — **7** nodos migrados (favicon mask + labels doc); **0** usos restantes del style. Paint styles `M3 / black` / `M3/black` candidatos a poda FASE E.
- [x] **FASE B (strokes) 2026-06-09** — `setStrokeStyleIdAsync('')` en árboles `COMPONENT` (mismo script, ampliado). Input chip: **36** strokes desenganchados (`M3/sys/light/outline-variant` → variable `Schemes/Outline Variant` directa). Barrido global: **1 410** strokes; **0** `strokeStyleId` restantes en componentes.
- [x] **FASE B (instancias + doc Chips) 2026-06-09** — Overrides en `INSTANCE` (p. ej. `label-text` en Assistive chip · Examples) y textos de documentación en página Chips (`Label`/`Sub label` con `M3/sys/light/on-surface` · `M3/sys/light/primary`). **31** nodos doc + **261** overrides de instancia; **0** usos `on-surface`/`primary` compactos en contexto chip.
- [x] FASE B (formal): `nodesWithStyleFill` → 0 en component sets MVP Must — **cumplido**
- [ ] FASE B (formal): `nodesWithStyleStroke` → 0 en component sets MVP Must — **cumplido**
- [ ] Dark mode: cambio en `Schemes/Primary` propaga a Button + Text field sin Swap manual
- [ ] Informe de drift re-ejecutado: 0 bloqueantes
- [x] **FASE T1 tipografía 2026-06-09** — `Static/Font/Brand` → Fraunces · `Static/Font/Plain` → Inter · `Static/Title Large/Font` re-alias a Plain. Post-check: **0** styles con font variable → Roboto; `variables.json` alineado.
- [x] **Button label typography 2026-06-09** — variables `Static/Button Label Medium|Large/*` (Inter/Plain); text styles `M3 / label / button-medium` (16) · `M3 / label / button-large` (32/40, alineado M3 default). Mapeo por `Size=` en Buttons.
- [ ] **FASE T2** — consolidar duplicados text style (`M3 / label / large` vs `M3/label/large`)
- [ ] **FASE T3** — enlazar `fontWeight` en text styles; desenganchar `textStyleId` en nodos (opcional)
- [ ] **Estilos compuestos (efectos)** — fuera del alcance paint/T1

---

## Referencias

- Inventario KEEP/CUT: [`figma-prune-inventory.md`](../figma-prune-inventory.md)
- Variables canónicas: [`variables.json`](../variables.json)
- Skill auditoría: [`.cursor/skills/myowntrip-ds-audit/SKILL.md`](../../../.cursor/skills/myowntrip-ds-audit/SKILL.md)
