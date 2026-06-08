# MyOwnTrip Nativo — Contexto para agentes

## Qué es
App Android offline-first · libreta de viaje digital · **viajero urbano organizado**, estética editorial (papel cálido, tipografía protagonista).

## Repo archivo DS
El design system custom (Pencil, Figma viejo, ARC) está en **`../MyOwnTrip`**. No actualizar ni importar desde allí salvo consulta humana puntual.

## Stack
- Compose + Material 3 nativo (Fraunces + Inter, MTB editorial)
- Room + Hilt + Navigation
- Supabase preparado en Gradle; sync fuera del MVP

## Design System
- Índice: `docs/design-system/README.md`
- Figma = librería · Showcase = docs componentes — ADR `003-figma-library-showcase-docs.md`
- Showcase: `docs/design-system/showcase.md`
- ADR marca: `docs/decisions/002-brand-editorial-m3.md`
- Color: `docs/design-system/color.md` (seeds `#1F3A5F`; tabla completa pendiente handoff MTB)
- Tipografía: `docs/design-system/typography.md`
- Governance / quality gates: `docs/design-system/governance.md`

## Documentación producto
- JTBD: `docs/product/jtbd-flows.md`
- UX runtime: `docs/ux/android-compose-ux.md`
- Notion: [Proyecto](https://www.notion.so/3796a48d93c8819486cfe3a7fd3f624e) · [Design System](https://www.notion.so/3796a48d93c88168b7dcf9d7e81f9bfa)
- Skills: `.cursor/skills/myowntrip-context/`, `myowntrip-ux-notion/`

## Reglas
- `.cursor/rules/m3-native-ui.mdc` (always apply)
- `.cursor/rules/myowntrip-development.mdc`
- `.cursor/rules/android-compose-ux.mdc`

## Código
- `app/src/main/java/com/myowntrip/app/`
- Features: `ui/features/{trips,wallet,expenses,journal}/`
- Tema: `ui/theme/{Color,Type,Theme}.kt`

## MVP actual (v0.1.0)
- JTBD 1: viajes + Wallet (manual + share target + confirmación H7)
- JTBD 5: diario por día
- JTBD 6: gastos rápidos

## Al implementar UI
1. M3 directo; wrappers `MyOwnTrip*` solo si `docs/design-system/components.md` lo autoriza.
2. Roles M3 + state layers; cero tokens por estado.
3. Error = icono + texto.
4. Actualizar `color.md` si cambian roles tras handoff MTB.
