# Auditoría — paridad `MOTIcons` (Compose) ↔ Icons Figma

> **Cierre 2026-11-03.** Nuevos deltas paridad iconos → fila en [parity-log.md](../parity-log.md) (ámbito `icons`).

**Fecha:** 2026-11-03  
**Archivo:** MyOwnTrip_nativo - Design System (`zrGAL4v6MEMc9hzZemU432`)  
**Página:** Icons → sección **Material Symbols Sharp (w300)** (`55594:2485`)  
**Herramienta:** Figma Console MCP (`figma_execute`, `figma_take_screenshot`)

## Objetivo

Todo glifo referenciado por `MOTIcons` en Compose debe existir como **COMPONENT** en Figma con el **nombre Material** canónico y fill del vector enlazado a variable (no hex suelto en el glifo de catálogo).

## Estado previo

- **56** nombres Material únicos en `scripts/generate_material_symbols.py` (57 props Kotlin: `CameraAlt` + `PhotoCamera` → `photo_camera`).
- **20** nombres usados en app **sin** componente homónimo en Icons.

## Acción

Importados desde la misma URL que el generador Compose:

`https://fonts.gstatic.com/s/i/short-term/release/materialsymbolssharp/{name}/wght300/24px.svg`

Estructura clonada del componente `add`:

- COMPONENT 24×24, hijo `icon` 24×24, vector(es) con fill → `Schemes/On Surface` (`VariableID:54778:427`), coherente con el resto del grid existente.

| Componente creado / refrescado | Node id (post-create) |
|--------------------------------|------------------------|
| `account_balance_wallet` | `61337:28628` |
| `brush` | `61337:28634` |
| `calendar_month` | `61337:28640` |
| `check_circle` | `61337:28646` |
| `cloud` | `61337:28652` |
| `confirmation_number` | `61337:28658` |
| `draft` | `61337:28623` (refreshed) |
| `keyboard_arrow_left` | `61337:28667` |
| `keyboard_arrow_right` | `61337:28673` |
| `note_add` | `61337:28679` |
| `open_in_new` | `61337:28685` |
| `photo_library` | `61337:28691` |
| `qr_code` | `61337:28697` |
| `qr_code_scanner` | `61337:28703` |
| `receipt` | `61337:28709` |
| `save` | `61337:28715` |
| `sticky_note_2` | `61337:28721` |
| `stop` | `61337:28727` |
| `unarchive` | `61337:28733` |
| `wifi_off` | `61337:28739` |

Ubicación: filas nuevas bajo el grid existente (y ≈ 1739–1795 en la sección Sharp w300).

## Gate post-cambio

| Comprobación | Resultado |
|--------------|-----------|
| Símbolos Material de app presentes en Icons | **0 missing** (lista de 55 nombres únicos; `photo_camera` cubre `CameraAlt`/`PhotoCamera`) |
| Vectores de esos componentes sin variable en fill | **0** (`unboundVectorIcons: []`) |
| Componentes totales en página Icons | 176 |

## Documentación

Actualizado [`iconography.md`](../iconography.md): peso w300 en Compose, `MOTIcons`, flujo de regeneración y script auxiliar `scripts/build_figma_icon_execute.py`.

## Pendiente (fuera de alcance)

- Etiquetado visual en lámina Figma (filas “App catalog”) — opcional; los nombres de componente ya son el contrato.
- Chevron de `ExposedDropdownMenu` — sigue siendo M3 interno, no `MOTIcons`.
- Sustitutos kit (`wallet`, `done`, `calendar_today`) siguen en el archivo para otros contextos; **no** reemplazan los nombres Material de producto añadidos arriba.
