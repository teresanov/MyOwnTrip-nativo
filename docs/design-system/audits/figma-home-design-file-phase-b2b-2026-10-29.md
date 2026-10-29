# Fase B.2b — design-file Home · MOT · Spacing

**Fecha:** 2026-10-29  
**Archivo:** `MyOwnTrip_design file` (`Vf2tNMXyKAlJSV53A1v4Is`)  
**SoT Compose:** `HomeEmptyState` / `HomeTripsScreen` / `HomePageHero` + [spacing.md](../spacing.md)  
**Librería:** `zrGAL4v6` · colección **MOT · Spacing**

## Caps

| Cap | Node | Compose |
|-----|------|---------|
| Shell | `205:813` | flow Home |
| Cap 1 · vacío | `205:816` | `HomeEmptyState` |
| Cap 1b · solo pasados | `313:501` | `HomeTripsScreen` onlyPast |
| Cap 2 · con viajes | `205:1018` | `HomeTripsScreen` |
| Cap 3 · filtros | `228:8161` | + filter overlay |

## Hallazgo previo

No había binds a **LEGACY · Semantic Layout**. El chrome de **Body** usaba variables remotas de **`M3 Spacing & Layout`** (`spacing-layout-md` / `spacing-layout-lg` / `spacing-component-sm`) — colección del kit Android UI, no MOT.  
`paddingBottom` del Body estaba **sin bind** (literal 24).

## Cambios aplicados

### Import desde librería nativo

`mot/spacing/screen-horizontal` · `layout-md` · `layout-lg` · `component-sm` · `component-xs` · `screen-content-bottom` · `screen-content-bottom-with-fab`

### Body (4 caps)

| Prop | Antes | Después | Compose |
|------|-------|---------|---------|
| padding L/R | `spacing-layout-md` (M3 kit) | **`mot/spacing/screen-horizontal`** | `MOTSpacing.screenHorizontal` |
| padding Top | `spacing-layout-md` | **`mot/spacing/layout-md`** | `layoutMd` |
| padding Bottom cap1 | literal 24 | **`mot/spacing/screen-content-bottom`** | `screenContentBottom` |
| padding Bottom 1b/2/3 | literal 24 | **`mot/spacing/screen-content-bottom-with-fab`** | `screenContentBottomWithFab` |
| itemSpacing | `spacing-layout-lg` (24) | **`mot/spacing/layout-md`** (16) | `spacedBy(layoutMd)` |

### Hero header

| Cap | Gap |
|-----|-----|
| 1 / 1b | `mot/spacing/component-sm` (8) |
| 2 / 3 | `mot/spacing/component-xs` (4) |

### TripHeroCard instancias (cap 2/3)

`itemSpacing` del root instancia: `spacing-component-sm` (M3 kit) → **`mot/spacing/component-sm`**.

## No tocado (aceptable)

| Item | Nota |
|------|------|
| Interior de instancias (Status bar Android UI, Search bar state-layer, etc.) | Chrome de librería ajena / set DS |
| Gap 40 del frame `Screens` | Layout de documentación entre phones |
| Gap 8 de frames `cap *` | Separación phone chrome ↔ body |
| Hero con un solo `itemSpacing` | Compose usa xs + sm entre líneas; Figma aproxima con un gap |

## Verificación

- Auditoría chrome Body/Hero: **0 binds no-MOT** en pads/gaps de página.
- Screenshots Desktop Bridge: cap 1 `205:816`, cap 2 `205:1018` — márgenes y stack coherentes; botón Square (ADR 004).

## Tipografía (post-publish librería)

Textos de página (Hero / section / labels de cap) enlazados a styles publicados:

| Rol | Style | Caps |
|-----|-------|------|
| Eyebrow | `M3/label/large` (Inter) | 1, 1b, 2, 3 |
| Headline | `M3/display/small` (Fraunces Regular) | 1, 1b, 2, 3 |
| Sub | `M3/body/large` (Inter) | 1, 1b, 2, 3 |
| Section («Más viajes» / «Viajes anteriores») | `M3/title/large` (Inter) | 1b, 2, 3 |
| Cap doc labels | `M3/body/large` (Inter; antes Roboto stale) | todos |

## Color (semánticos M3 · paridad Compose)

| Rol | Compose | Figma fill |
|-----|---------|------------|
| Eyebrow | `colorScheme.tertiary` | `Schemes/Tertiary` |
| Headline / section | default / `onSurface` | `Schemes/On Surface` |
| Sub | `colorScheme.onSurfaceVariant` | `Schemes/On Surface Variant` |
| Cap doc labels | meta | `Schemes/On Surface Variant` |
| Body frame | surface pantalla | `Schemes/Surface` |

## Checklist salida

```
[x] Caps auditados
[x] 0 LEGACY en chrome Home; M3 Spacing & Layout sustituido en Body/Hero/TripHero gap
[x] Pads página → mot/spacing/*
[x] Instancias DS intactas (solo gap instancia TripHero re-apuntado)
[x] Text styles Hero = Compose/marca
[x] Screenshot cap 1 + cap 2
[ ] Contraste Xiaomi en dispositivo (humano)
```

