# Sesión 2026-06-17 — Home DS + Figma + Compose (cierre)

> **Notion:** [Sesión 2026-06-17 — Home DS, filtros, portadas y calendario](https://www.notion.so/) (hijo de MyOwnTrip · Proyecto)

## Resumen

Cierre del patrón **TripHeroCard** y **Eyebrow label** en librería DS + Compose; **Home cap 3** (búsqueda + menú filtros) clonando cap 2; extracción de componentes Home a `ui/components/home`; **portadas de destino** con caché Wikimedia; **calendario** año → mes → día en crear viaje.

## Decisiones

| Tema | Decisión |
|------|----------|
| Eyebrow en portada | Componente **Eyebrow label** (no Assist chip) · `Color=Tertiary` · `Size=Medium` · HUG |
| TripHeroCard | Imagen en `Background` · CTA tonal «Ver detalles» **única acción** · portada no clickable |
| Home chips metadata | **Eliminados** («3 viajes», «Offline», «Wallet») — redundantes con subhead y Wallet banner |
| Filtros Home | **Menu** `Groups=2` al pulsar `tune` — overlay 328×362dp · top 192dp · `#FCF2E5` |
| Ítem seleccionado en menú | Leading slot + **`check`** 20dp al seleccionar · sin submenú |
| Portadas destino | **Wikimedia Commons** + caché local en `files/destination-covers/` · ADR 003 |
| Calendario crear viaje | **`MotTripDatePickerDialog`**: año (rejilla) → mes (rejilla) → día · sin flechas mes a mes |
| M3 Expressive PickerGroup | Solo **Wear OS** — no aplicable a phone; custom dialog con `DatePickerDefaults.colors()` |

## Componentes Compose nuevos

| Componente | Ruta |
|------------|------|
| TripHeroCard | `ui/components/TripHeroCard.kt` |
| EyebrowLabel | `ui/components/EyebrowLabel.kt` |
| StackedCard / HorizontalCard | `ui/components/StackedCard.kt`, `HorizontalCard.kt` |
| Home (search, filter, hero, wallet) | `ui/components/home/` |
| MotTripDatePickerDialog | `ui/components/date/MotTripDatePickerDialog.kt` |
| DestinationCoverRepository | `data/cover/` + `domain/cover/` |

## Design-file — Shell Home · flow

| Cap | Node | Contenido |
|-----|------|-----------|
| cap 1 | [205:816](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-816) | Home vacío + Search bar |
| cap 2 | [205:1018](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-1018) | Home con viajes |
| cap 3 | [228:8161](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=228-8161) | Clone cap 2 · Search Pressed · Filter menu overlay |

## Documentación repo

| Archivo | Qué |
|---------|-----|
| `docs/design-system/patterns/home-filter-menu.md` | Patrón menú filtros |
| `docs/design-system/figma-compose-bindings.md` | Bindings Figma ↔ Compose |
| `docs/decisions/003-destination-cover-cache.md` | ADR portadas destino |
| `ds-showcase/…/trip-hero-card.json` | Ficha ready |
| `ds-showcase/…/eyebrow-label.json` | Ficha ready |
| `ds-showcase/…/stacked-card.json` | Ficha ready |

## Pendiente (mañana)

- [ ] Verificar menú filtros con insets reales (offset 192dp vs status bar)
- [ ] Showcase ficha patrón `home-filter-menu` (opcional)
- [ ] Entrada por teclado en date picker (opcional)
- [ ] Publicar librería DS tras cambio Menu-item en Figma

## Enlaces rápidos

- [Design-file Home flow](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-813)
- [DS TripHeroCard](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61199-7862)
- [GitHub — MyOwnTrip-nativo](https://github.com/teresanov/MyOwnTrip-nativo)
