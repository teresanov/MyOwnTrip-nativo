# Shape — MyOwnTrip Nativo

**SoT único (superficies):** roles M3 `MaterialTheme.shapes.*` ↔ Figma **Shape** `Corner/*`.  
**No** existe un segundo semántico Compose tipo `MOTCorner`.

Valores en tema: `MOTThemeShapes` (`Shapes.kt`). Botones: `rememberMOTButtonShape()` (ADR 004).

## Paridad

| Figma | dp | Compose (consumo) | Uso |
|-------|---:|-------------------|-----|
| `Corner/Extra-small` | 4 | `MaterialTheme.shapes.extraSmall` | Text field |
| `Corner/Small` | 8 | `MaterialTheme.shapes.small` | Chips, eyebrow… |
| `Corner/Medium` | 12 | `MaterialTheme.shapes.medium` | Cards, hero |
| `Corner/Large` | 16 | `MaterialTheme.shapes.large` | Superficies large |
| `Corner/Extra-large` | 28 | `MaterialTheme.shapes.extraLarge` | Dialog / sheets |
| `Corner/None` → `Corner/Large-increased` | 0→20 | `rememberMOTButtonShape()` | Botón morph — **fuera** de `shapes.*` |

Los dp viven solo dentro de `MOTThemeShapes` (primitivos del tema). UI y Figma hablan el **mismo rol** (`extraSmall` / `Extra-small`).

**Dev Mode:** `codeSyntax` ANDROID de `Corner/*` canónicos = `MaterialTheme.shapes.*` (botón: `rememberMOTButtonShape`).
