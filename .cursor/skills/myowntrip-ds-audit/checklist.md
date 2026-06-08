# Checklist — Auditoría M3 nativo

## A. variables.json (baseline)

- [ ] Versión export actualizada en `docs/design-system/variables.json`
- [ ] Colección `M3` con modos Light y Dark
- [ ] `Schemes/*` completos (49 roles por modo)
- [ ] `Extended Colors/Success|Warning|Info` presentes Light/Dark
- [ ] `State Layers/*` existen pero marcados **Figma-only** (no van a Kotlin)

## B. Paridad Kotlin (`composeMaterial3MappingValid`)

- [ ] `python3 .cursor/skills/myowntrip-ds-audit/scripts/diff-variables-kotlin.py` sin drift en roles del tema
- [ ] `Theme.kt` mapea roles M3 usados en `app/`
- [ ] `LocalExtendedColors` usa valores Light/Dark del JSON
- [ ] Sin `AppColors` en `ui/features/`

## C. Gate `m3Canonical` (bloqueante)

- [ ] Sin tokens de color por estado en Figma ni código
- [ ] State layers solo runtime (8/10/10/16%; disabled 38%/12%)
- [ ] UI: solo `MaterialTheme.colorScheme` y `typography`
- [ ] `tertiary` ≠ `error` en uso semántico
- [ ] Errores: icono + texto
- [ ] Fraunces solo Display/Headline; Inter resto

## D. Figma (si aplica — `figmaAliasChainValid`)

- [ ] Swatches de paleta enlazados a `Palettes/*` (no hex fijo)
- [ ] Styles activos en componentes enlazados a `Schemes/*`
- [ ] Sin paint styles legacy huérfanos (kit M3 rosa, duplicados hardcoded)
- [ ] Sin páginas de documentación de componentes en canvas DS
- [ ] `State Layers/*` en librería OK; no se exigen en código

## E. Documentación

- [ ] `color.md` referencia `variables.json`
- [ ] `ds-showcase/src/data/tokens.json` alineado (si aplica)
- [ ] Ficha showcase si componente nuevo (`showcase.md`)

## F. Legado — no debe aparecer

- [ ] `SemanticColors`, `LayoutTokens`, `MyOwnTripButton` del repo archivo
- [ ] Referencias a Pencil / ARC / `ds-audit` del proyecto anterior
- [ ] Primitivos MTB (`Palettes/*`) en pantallas Kotlin

---

## Plantilla de informe

```markdown
# Auditoría DS M3 — [fecha]

## Resumen
- Baseline: `variables.json` v[X]
- Alcance: [JSON only | + Kotlin | + Figma]
- Resultado: [PASS | PASS con deuda | FAIL]

## Paridad variables.json ↔ Color.kt
| Rol | JSON Light | Kotlin | OK |
|-----|------------|--------|-----|
| tertiary | … | … | ✓/✗ |

Drift total: N roles

## m3Canonical
- [ ] Regla 1–6 (detallar fallos)

## Figma (si aplica)
- Paint styles sospechosos: N
- Swatches sin binding: N
- Componentes con style legacy: …

## Acciones
1. …
2. …

## Descartado (no aplica a este DS)
- Pencil / semánticos custom / State Layers en Kotlin
```
