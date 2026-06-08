# MyOwnTrip Nativo — Contexto para agentes

## Qué es
App Android offline-first · libreta de viaje digital. Repo activo de producto.

## Repo archivo DS
El design system custom (Pencil, Figma viejo, ARC) está en **`../MyOwnTrip`**. No actualizar ni importar desde allí salvo consulta humana puntual.

## Stack
- Compose + Material 3 nativo
- Room + Hilt + Navigation
- Supabase preparado en Gradle; sync fuera del MVP

## Documentación
- JTBD: `docs/product/jtbd-flows.md`
- UX runtime: `docs/ux/android-compose-ux.md`
- ADR M3: `docs/decisions/001-m3-native-ds.md`
- Colores: `docs/design-system/color.md`
- Skills: `.cursor/skills/myowntrip-context/`, `myowntrip-ux-notion/`

## Reglas
- `.cursor/rules/myowntrip-development.mdc`
- `.cursor/rules/m3-native-ui.mdc` (always apply)
- `.cursor/rules/android-compose-ux.mdc`

## Código
- `app/src/main/java/com/myowntrip/app/`
- Features: `ui/features/{trips,wallet,expenses,journal}/`
- Datos: `data/local/`, `data/repository/`

## MVP actual (v0.1.0)
- JTBD 1: viajes + Wallet (manual + share target + confirmación H7)
- JTBD 5: diario por día
- JTBD 6: gastos rápidos

## Al implementar UI
No crear wrappers DS. Usar Material 3. No generar índices ARC ni documentación Pencil/Figma salvo actualizar `docs/design-system/color.md` si cambian roles M3.
