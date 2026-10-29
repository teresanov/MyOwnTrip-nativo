# Auditoría — bind padding página Buttons (2026-10-29)

**Archivo:** `MyOwnTrip_nativo - Design System` (`zrGAL4v6MEMc9hzZemU432`)  
**Colección:** `MOT · Spacing` → alias `Primitives · Space`  
**Gate:** todo padding bindable del contrato de componente enlazado; 0 literales en State-layer con pad > 0.

## Resultado

| Familia | Sets | Estado |
|---------|------|--------|
| Button (filled/text/elevated/outline/tonal) | 5 | Bound `mot/padding/button/*` (sesión previa) |
| Toggle (+ elevated/outline/tonal) | 4 | Bound mismos tokens Button por Size + gap `component-*` |
| Icon button (+ styles + togglable) | 8 | Ya bound `mot/padding/icon-button/*` |
| FAB | 1 | Bound `mot/padding/fab/{default,medium,large}` |
| Extended FAB | 1 | Bound `mot/padding/extended-fab/*` + gaps |
| FAB menu + BB FAB/Segment | 7 | Bound (FAB pads / extended-fab small) |
| Segmented + BB start/middle/end | 4 | Bound `mot/padding/segmented/*` (+ `state-layer` lowercase) |

**Cierre:** 30/30 sets en página Buttons · **0 unbound**.

## Tokens nuevos (esta sesión)

Primitivos: `spacing/26`, `spacing/28`, `spacing/30`.

Semánticos MOT:

- `mot/padding/fab/default|medium|large`
- `mot/padding/extended-fab/{small,medium,large}-{horizontal,vertical}`
- `mot/spacing/extended-fab/gap-{small,medium,large}`
- `mot/padding/segmented/{horizontal,vertical}`
- `mot/spacing/segmented/gap`

## Notas

- Toggle reutiliza pads de Button (misma escala Size).
- Icon button Wide Medium sigue en `wide-horizontal/vertical` (32/16); resto mayormente `0-*` (touch por tamaño de frame).
- Compose: pads FAB/Extended/Segmented aún no expuestos en `MOTSpacing` (sets KEEP / M3 defaults hasta consumidor nativo).
