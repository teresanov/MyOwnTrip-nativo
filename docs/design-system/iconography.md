# Iconografía — MyOwnTrip Nativo

ADR: [002-brand-editorial-m3.md](../decisions/002-brand-editorial-m3.md).

## UI funcional (obligatorio)

| Parámetro | Valor |
|-----------|-------|
| Set | **Material Symbols Sharp** (Google Fonts) |
| Estilo | **Sharp** |
| Peso | **300** (Figma y Compose — misma URL `…/materialsymbolssharp/{name}/wght300/24px.svg`) |
| Color | Roles `colorScheme` en runtime (`onSurface`, `primary`, `onSurfaceVariant`, …) |

- **Compose:** `com.myowntrip.app.ui.theme.MOTIcons` — generado por `scripts/generate_material_symbols.py` (no editar a mano). Sustituye `androidx.compose.material:material-icons-extended` (Material Icons legacy).
- **Figma:** componentes en página **Icons** → sección **Material Symbols Sharp (w300)**; nombre del componente = nombre Material (`add`, `draft`, `account_balance_wallet`, …).
- **No** usar otro set para iconos de UI (navegación, acciones, estados). Ilustración / marca → `res/drawable/` (ver abajo).

## Iconos propios (permitido)

| Tipo | Uso | Ubicación |
|------|-----|-----------|
| Logo | Marca app, splash | `res/drawable/` |
| Spot art / ilustración | Empty states, onboarding | `res/drawable/` |
| Categorías bespoke | Gastos, Wallet, tipos de entrada | `res/drawable/` — glifos de producto |

Los iconos propios no sustituyen acciones estándar (cerrar, atrás, más, editar).

## Accesibilidad

- Iconos informativos: `contentDescription` significativo.
- Decorativos: `contentDescription = null` o `Modifier.semantics { invisibleToUser() }`.
- Error / éxito / aviso: **icono + texto**; no depender solo del color del icono.

## Menús de selección

| Contexto | Icono seleccionado | Slot | No usar |
|----------|-------------------|------|---------|
| `DropdownMenu` / Menu-item selección única | **`check`** leading (caja reservada) | Leading siempre; trailing oculto | `chevron_right` (submenú) · `radio_button_checked` |
| Radio group / formulario | `radio_button_checked` | Leading o trailing según patrón form | En menús contextuales de filtro |

Ver [`patterns/home-filter-menu.md`](patterns/home-filter-menu.md).

## Figma

Kit M3 Design Kit tematizado; catálogo **Material Symbols Sharp (w300)** en la sección Icons del DS. Sin biblioteca paralela de UI icons.

### Paridad Compose ↔ Figma (catálogo app)

Los glifos que consume `MOTIcons` deben existir como **COMPONENT** en Icons con el **mismo nombre Material** que el script de generación. Regenerar Compose tras añadir usos nuevos:

```bash
python scripts/generate_material_symbols.py
```

Para volcar faltantes en Figma (misma fuente SVG):

```bash
python scripts/build_figma_icon_execute.py
# pegar scripts/.tmp_figma_icon_execute.js en figma_execute (Figma Console MCP)
```

Auditoría: `docs/design-system/audits/figma-moticons-parity-2026-11-03.md`.

### Excepciones conocidas

| Contexto | Comportamiento |
|----------|----------------|
| `ExposedDropdownMenuDefaults.TrailingIcon` | Chevron interno M3, no `MOTIcons` |
| `InsertDriveFile` en Kotlin | Símbolo Material `draft` (nombre Figma `draft`) |
| `CameraAlt` / `PhotoCamera` | Mismo glifo `photo_camera`; en Figma basta `photo_camera` |
