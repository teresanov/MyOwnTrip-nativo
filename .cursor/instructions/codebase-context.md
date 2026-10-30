# MyOwnTrip Nativo — Contexto para agentes

## Qué es
App Android offline-first · libreta de viaje digital · **viajero urbano organizado**, estética editorial (papel cálido, tipografía protagonista).

## SoT (este repo)
**`MyOwnTrip_nativo`** = app + Compose + librería Figma nativa + docs/audits de paridad + **rules/skills de agente**.

`../MyOwnTrip` = archivo legacy (Pencil / ARC custom). Solo consulta humana puntual; no contrastar pantallas ni componentes de producto allí.

## Stack
- Compose + Material 3 nativo (Fraunces + Inter, MTB editorial)
- Room + Hilt + Navigation
- Supabase: sync post-MVP; dependencias retiradas de Gradle hasta que se implemente (ver historial git)

## Design System
- **Variables canónicas:** `docs/design-system/variables.json` (colección M3 · Light/Dark)
- Índice: `docs/design-system/README.md`
- Figma librería: `zrGAL4v6` · design-file: `Vf2tNMXyKAlJSV53A1v4Is`
- Showcase: `docs/design-system/showcase.md`
- Color: `docs/design-system/color.md` · Spacing: `docs/design-system/spacing.md`
- Audits paridad: `docs/design-system/audits/`
- Skills DS: `myowntrip-ds-audit/`, `ds-index/`, `ds-audit/`, `ds-semantic-pipeline/`

## Documentación producto
- JTBD: `docs/product/jtbd-flows.md`
- UX runtime: `docs/ux/android-compose-ux.md`
- Notion: [Proyecto](https://www.notion.so/3796a48d93c8819486cfe3a7fd3f624e) · [Design System](https://www.notion.so/3796a48d93c88168b7dcf9d7e81f9bfa)
- Skills: `.cursor/skills/myowntrip-context/`, `myowntrip-ux-notion/`

## Reglas (agente)
- `.cursor/rules/m3-native-ui.mdc` (always apply)
- `.cursor/rules/figma-instances-only.mdc` (always apply — Compose SoT = este repo)
- `.cursor/rules/figma-app-flows.mdc`
- `.cursor/rules/myowntrip-development.mdc`
- `.cursor/rules/android-compose-ux.mdc`
- `.cursor/rules/utilities/*` (tokens, bindings, icon color)
- `.cursor/rules/components/*` (composición, metadata)

## Código
- `app/src/main/java/com/myowntrip/app/`
- Features: `ui/features/{trips,wallet,expenses,journal}/`
- Tema / MOT*: `designsystem/` + `ui/theme/`

## MVP actual (v0.1.0)
- JTBD 1: viajes + Wallet (manual + share target + confirmación H7)
- JTBD 5: diario por día
- JTBD 6: gastos rápidos

## Al implementar UI
1. M3 directo; wrappers `MyOwnTrip*` solo si `docs/design-system/components.md` lo autoriza.
2. Roles M3 + state layers; cero tokens por estado.
3. Error = icono + texto.
4. Actualizar `color.md` si cambian roles tras handoff MTB.
