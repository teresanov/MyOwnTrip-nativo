# Bridge — Fijar gris-azul (bypass MTB)

**Fecha:** 2026-06-11  
**Archivo Figma:** `zrGAL4v6MEMc9hzZemU432` · [Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System)  
**Script:** `.cursor/skills/myowntrip-ds-audit/scripts/figma-fix-gris-azul-colors.js`  
**Fuente valores:** `M3_MOTrip.json` (corregido en repo, no re-importar MTB)

## Problema

Material Theme Builder **re-harmoniza** `Primary` a azul (`#276389`) y Dark a celeste (`#98CCF9`) aunque `secondary` y papel estén en gris-azul. La decisión de producto (jun 2026) es **gris-azul editorial**:

| Modo | `Schemes/Primary` |
|------|-------------------|
| Light | `#4A5864` |
| Dark | `#B4BAC2` |

`tertiary` ocre `#825513` (Light) — sin tocar.

## Solución

Escribir variables **directamente en Figma** vía Desktop Bridge (`figma_execute`), sin pasar por MTB.

Alcance del script (6 modos × ~118 vars/modo ≈ **708** actualizaciones):

- `Schemes/*` (completo)
- `State Layers/*Primary*` y `*Secondary*`
- `Palettes/Primary *`

## Ejecutar

1. Abrir **MyOwnTrip_nativo — Design System** en Figma Desktop.
2. **Plugins → Development → Figma Desktop Bridge** → Run.
3. Pedir al agente: *«ejecuta figma-fix-gris-azul»* (o `figma_execute` con el script).

## Aplicado en Figma (Bridge 2026-06-12)

| Paso | Resultado |
|------|-----------|
| Light + Dark (script) | 4 + 4 vars (`Primary` + state layers) |
| Light High Contrast | 32 vars (schemes + layers + secondary) |
| Light/Dark MC + DHC (patch + batch) | 15 schemes + 24 state layers |
| **Total aprox.** | ~79 actualizaciones (resto ya alineado) |

Verificación Bridge (`Schemes/Primary`):

| Modo | Valor |
|------|-------|
| Light | `#4A5864` ✓ |
| Dark | `#B4BAC2` ✓ |
| Light HC / LM / Dark HC / DMC | gris-azul ✓ |

Verificación post-fix manual:

- [x] Button **Filled** + **Tonal** en Light y Dark (revisión visual jun 2026 — OK)
- [ ] Publicar librería si hay cambios sin publicar
- [ ] **No** re-exportar a MTB salvo para inspección — MTB puede volver a fastidiar

## Repo alineado

| Archivo | Estado |
|---------|--------|
| `M3_MOTrip.json` | Corregido (swap azules MTB) |
| `docs/design-system/variables.json` | Sincronizado (6 modos canónicos) |
| `app/.../Color.kt` | Gris-azul (sin cambio) |

## Workflow recomendado

```
Figma (Bridge fija variables) → export variables.json / M3_MOTrip.json → Color.kt
```

**Evitar:** `MTB import → export → Figma` como bucle principal de color.
