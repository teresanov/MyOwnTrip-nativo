# Sesión 2026-06-17 — Home DS + Figma + Compose

> **Notion:** pegar este bloque en [MyOwnTrip · Proyecto](https://www.notion.so/3796a48d93c8819486cfe3a7fd3f624e) o en el diario de diseño DS. El MCP de Notion no estaba conectado al cerrar la sesión.

## Resumen

Cierre del patrón **TripHeroCard** y **Eyebrow label** en librería DS + Compose; limpieza de chips decorativos en Home; diseño de **cap 3** (búsqueda + menú filtros) en design-file clonando cap 2.

## Decisiones

| Tema | Decisión |
|------|----------|
| Eyebrow en portada | Componente **Eyebrow label** (no Assist chip) · `Color=Tertiary` · `Size=Medium` · HUG |
| TripHeroCard | Imagen en `Background` · CTA tonal «Ver detalles» **única acción** · portada no clickable |
| Home chips metadata | **Eliminados** («3 viajes», «Offline», «Wallet») — redundantes con subhead y Wallet banner |
| Filtros Home | **Menu** `Groups=2` al pulsar `tune` — no Filter chips en fila |
| Ítem seleccionado en menú | Fondo Selected + icono **`check`** — **no** chevron (submenú) |
| Caps design-file | cap 1 vacío · cap 2 con viajes · cap 3 = **clone cap 2** + menú (cap 2 intacto) |

## Componentes / variantes creados o actualizados (librería DS)

| Componente | Ubicación Figma | Variantes | Estado |
|------------|-----------------|-----------|--------|
| **TripHeroCard** | [61199:7862](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61199-7862) | `Style=Elevated` · `Outlined` | Ready · showcase |
| **Eyebrow label** | [61202:16834](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61202-16834) | 3×2 Color×Size | Ready · showcase |
| Search bar | kit M3 (existente) | Enabled/Pressed · avatar off · 2nd trailing `tune` | Instanciado en design-file |
| Menu | kit M3 `Groups=2` | Standard | Instanciado en cap 3 |

Scripts DS: `scripts/figma-trip-hero-card.js` · `scripts/figma-eyebrow-label.js`

## Design-file — Shell Home · flow

| Cap | Node | Contenido |
|-----|------|-----------|
| cap 1 | [205:816](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-816) | Home vacío + Search bar |
| cap 2 | [205:1018](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-1018) | Home con viajes (TripHeroCard, Wallet, Más viajes) |
| cap 3 | [228:8161](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=228-8161) | Clone cap 2 · Search Pressed «Barcelona» · Filter menu overlay |

Scripts design-file:
- `scripts/figma-design-file-home-cap3-search-filters.js` — **usar este** (duplica cap 2)
- `scripts/figma-design-file-remove-home-quick-chips.js`
- `scripts/figma-design-file-trip-hero-instance.js`
- `scripts/figma-design-file-rebuild-home-flow.js` — rebuild completo (conserva cap 2 si existe)
- `scripts/figma-design-file-home-cap2-search-filters.js` — **deprecated**

## Compose alineado

| Archivo | Cambio |
|---------|--------|
| `TripListScreen.kt` | TripHeroCard (ElevatedCard + EyebrowLabel + FilledTonalButton) · sin `HomeQuickChips` |

## Documentación repo

| Archivo | Qué |
|---------|-----|
| `docs/design-system/patterns/home-filter-menu.md` | Patrón menú filtros (nuevo) |
| `docs/design-system/figma-prune-inventory.md` | § TripHeroCard · Eyebrow · Menu selección · Shell Home |
| `docs/design-system/components.md` | Enlaces TripHeroCard · Eyebrow · patrón menú |
| `ds-showcase/…/trip-hero-card.json` | Ficha ready |
| `ds-showcase/…/eyebrow-label.json` | Ficha ready |

## Pendiente

- [ ] **DS Figma:** trailing Selected en Menu-item → icono `check` (hoy `radio_button_checked` en kit)
- [ ] **Compose:** `DropdownMenu` filtros/orden en `TripListScreen` (ViewModel + estado)
- [ ] **Showcase:** ficha patrón `home-filter-menu` (opcional; hoy solo `docs/`)
- [ ] **Publicar** librería DS tras cambio Menu-item (si se toca el set)
- [ ] **Verificar** TripHeroCard import en design-file (clave publicada; falló una vez por nodo huérfano)

## Enlaces rápidos

- [Design-file Home flow](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-813)
- [DS TripHeroCard](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61199-7862)
- [DS Eyebrow label](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61202-16834)
