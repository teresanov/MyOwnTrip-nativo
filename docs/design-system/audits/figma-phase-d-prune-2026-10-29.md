# Fase D — Poda con evidencia Compose

**Fecha:** 2026-10-29  
**SoT uso:** `app/src/main/java` (+ wrappers `designsystem`)  
**Inventario:** [figma-prune-inventory.md](../figma-prune-inventory.md)

## Método

Contar consumidores reales (archivos / hits) de APIs M3 y wrappers MOT.  
Cruzar con tiers KEEP / Should / LATER / CUT del inventario.  
**Cortar en Figma solo** lo que el inventario ya marcaba CUT y seguía vivo.  
**Demover a LATER** (doc) sets Should sin pantalla Compose — no borrar.

## Matriz evidencia (resumen)

| Set / API | Compose | Tier previo | Decisión D |
|-----------|---------|-------------|------------|
| Button / tonal / outline / text / Icon button | alto (`MOT*`) | KEEP | **KEEP** |
| Filter chip | 7 files / 37 | KEEP | **KEEP** |
| Assist chip | 1 file | KEEP | **KEEP** |
| Suggestion / Input chip | **0** | KEEP | → **LATER** |
| Chip groups | **0** | Should | → **LATER** |
| Chips `Style=Elevated` | **0** (+ política Outlined) | CUT (regla) | **CUT aplicado** |
| OutlinedTextField / Dialog / Sheet / Snackbar / Tabs / Dividers | en uso | KEEP | **KEEP** |
| TopAppBar Small | 19 files | KEEP | **KEEP** |
| Center / Medium / Large TopAppBar | **0** | KEEP kit | **KEEP kit** (diseño; no CUT) |
| Bottom app bar | **0** | CUT | **CUT aplicado** |
| FAB | 5 files | KEEP | **KEEP** |
| Extended FAB | **0** | KEEP | → **LATER** |
| Segmented button | **0** | Should | → **LATER** |
| Icon togglable | 1 (Sketch) | Should | **KEEP** (journal) |
| Toggle button | bajo | Should | **KEEP** (morph ADR) |
| Button - elevated | ~0 wrapper | KEEP | **KEEP** diseño (poco código) |
| Sliders (3) | **0** | Should | → **LATER** |
| Carousel M3 | **0** (sí `HorizontalPager` scrapbook ≠ set) | LATER | **LATER** |
| Accordion | **0** | Should | → **LATER** |
| List Item - Swipe | sí | Should | **KEEP** |
| Badges | **0** | KEEP | → **LATER** |
| Checkbox | **0** | KEEP | **KEEP** (forms futuros; no CUT) |
| Radio / Switch | bajo | KEEP | **KEEP** |
| Navigation Bar | **0** Compose · sí design-file | KEEP | **KEEP** (shell Figma) |
| Side sheet / Search full-screen / List dialog | **0** | LATER | **LATER** |
| Tooltips | **0** | CUT página | OK ausente |
| Document attachment / TripHero / Eyebrow | sí | producto | **KEEP** |

## Acciones Figma (esta sesión)

| Acción | Detalle |
|--------|---------|
| CUT | **Bottom app bar** — el set ya estaba borrado; Fase D quitó instancias huérfanas. Queda **frame doc** con nota (humano) de por qué no está · boceto → Toolbars |
| CUT | Variantes **`Style=Elevated`**: Filter −48 · Assist −24 · Suggestion −24 (quedan Outlined-only) |

## No tocado (a propósito)

Segmented, Sliders, Carousel, Chip groups, Suggestion/Input, Extended FAB, Accordion, Badges — **siguen en archivo**, tier doc → LATER.  
No poda masiva del kit M3 “por si acaso”.

## Decisión producto (2026-10-29)

**Boceto / túnel de pintura** → **Toolbars** (+ icon togglable), no Bottom app bar.  
Compose ya: `SketchToolbar` en Diario. Bottom app bar sigue **CUT** (chrome Top+FAB); Toolbars **KEEP** para esa fase.

## Siguiente

- ~~Publicar librería tras chips Outlined-only~~ ✅ 2026-10-29 (con Fase E + visibility).
- Design-file: **Update library** en instancias (Home/Wallet/Diario…) para coger Outlined chips / strokes / visibility.
- Cuando toque la fase boceto: paridad Toolbars ↔ `SketchToolbar`.
