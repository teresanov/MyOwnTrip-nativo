# Tipografía — MyOwnTrip Nativo

**SoT Compose:** `designsystem/.../Type.kt` → `AppTypography`  
**SoT Figma:** text styles locales en `zrGAL4v6` (publicado a librería)

## Marca (no Roboto)

| Familia | Roles Compose |
|---------|----------------|
| **Fraunces** | `display*` · `headline*` |
| **Inter** | `title*` · `body*` · `label*` |

No hay escala M3 “stock” (Roboto) en el DS. Se eliminó el 2026-10-29.

## Mapa Compose ↔ Figma text style

| Compose (`AppTypography`) | Figma style | Familia | Size / weight |
|---------------------------|-------------|---------|---------------|
| `displayLarge` | `M3/display/large` | Fraunces | 57 Regular |
| `displayMedium` | `M3/display/medium` | Fraunces | 45 Regular |
| `displaySmall` | `M3/display/small` | Fraunces | 36 Regular |
| `headlineLarge` | `M3/headline/large` | Fraunces | 32 Regular |
| `headlineMedium` | `M3/headline/medium` | Fraunces | 28 Regular |
| `headlineSmall` | `M3/headline/small` | Fraunces | 24 Regular |
| `titleLarge` | `M3/title/large` | Inter | 22 Semi Bold |
| `titleMedium` | `M3/title/medium` | Inter | 16 Medium |
| `titleSmall` | `M3/title/small` | Inter | 14 Medium |
| `bodyLarge` | `M3/body/large` | Inter | 16 Regular |
| `bodyMedium` | `M3/body/medium` | Inter | 14 Regular |
| `bodySmall` | `M3/body/small` | Inter | 12 Regular |
| `labelLarge` | `M3/label/large` | Inter | 14 Medium |
| `labelMedium` | `M3/label/medium` | Inter | 12 Medium |
| `labelSmall` | `M3/label/small` | Inter | 11 Medium |

**15 estilos UI.** Sin variantes `-emphasized` ni `button-*` (Compose no las expone).

## Variables (cadena de binds)

```
Text style M3/{role}
  → Typescale / Static/{Role}/{Font|Size|Weight|Line Height|Tracking}
    → Font theme (Font/Brand=Fraunces, Font/Plain=Inter, Weight/*)
```

Cada uno de los 15 estilos tiene las **5 props** bindeadas a su rol Typescale (no literales).  
`Static/Title Large/Weight` → `Font theme / Static/Weight/Bold` (**SemiBold**), alineado a Compose `titleLarge`.

## Brand page (excepción)

Estilos `Brand/*` (wordmark, meta de lámina) viven solo para documentación de marca. **No** usar en UI de producto / design-file de pantallas.

## Limpieza 2026-10-29

- Borrados 15 estilos **Roboto** duplicados del kit M3.
- Borrados `-emphasized` y `label/button-*`.
- Renombrados paths con espacios (`M3 / body / large` → `M3/body/large`).
- Nodos del DS rebindeados a Fraunces/Inter antes del delete.

**Publicar** la librería Figma tras este cambio para que el design-file actualice estilos remotos.
