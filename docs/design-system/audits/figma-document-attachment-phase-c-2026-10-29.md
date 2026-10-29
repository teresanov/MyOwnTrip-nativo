# Fase C — Document attachment

**Fecha:** 2026-10-29  
**Figma:** set [Document attachment](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61258-28665) `61258:28665` (Kind=Image|File)  
**Compose:** `DocumentAttachmentCard`

## Compose SoT

| Rol | Kotlin |
|-----|--------|
| Shape | `MaterialTheme.shapes.medium` (12) |
| Fill | `colorScheme.surfaceContainerLow` |
| Pad card | `MOTSpacing.layoutMd` (16) |
| Gap columna / file body | `componentSm` (8) · hint top `componentSm` · icon top `componentXs` |
| Preview H | `MOTSpacing.DocumentAttachment.previewHeight` (180) |
| Footer icon | `Icons.Default.Visibility` |
| File leading | `Icons.AutoMirrored.Filled.InsertDriveFile` |

## Figma (esta pasada)

| Cambio | Detalle |
|--------|---------|
| Pads/gaps LEGACY | `semantic/spacing/gutter` · `stack/sm` → **`mot/spacing/layout-md`** · **`component-sm`** |
| Radio card | `Corner/Medium` (ya) |
| Media preview radio | Era `Corner/Small` (8) — Compose sin clip interior → **`Corner/None`** |
| File body pads V | → `mot/spacing/layout-md` |

## Gaps / no cerrado

| Item | Nota |
|------|------|
| Footer glyph | ✅ Icons `visibility` (`61298:28859`) · footer Image/File → instancia `visibility` · fill `Schemes/Tertiary` |
| Preview height 180 | Literal de producto en Figma (altura frame); espejo Compose tokenizado. |

## Checklist

```
[x] Compose shape medium + previewHeight token
[x] 0 LEGACY Semantic Layout en pads/gaps del set
[x] Media sin radio inventado
[x] Glyph visibility en Icons + footer Document attachment
[x] Fila bindings
```
