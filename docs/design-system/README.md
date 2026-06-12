# Design System — MyOwnTrip Nativo

M3 canónico con identidad editorial. Sin capa semántica propia ni pipeline Pencil.

## Dirección de marca

| Dimensión | Decisión |
|-----------|----------|
| Persona | Viajero urbano organizado (ciudad, transporte, hoteles de diseño) |
| Estética | Editorial (Monocle / Wallpaper): sobriedad, tipografía protagonista, color con cuentagotas, neutros papel cálido |
| Acento | Ocre editorial (`tertiary`) con moderación; error visualmente distinto |
| Logo | Sistema W4 · MOT · C1 — ver [`brand.md`](brand.md) y Notion Brand |

## Documentación

| Doc | Contenido |
|-----|-----------|
| [color.md](color.md) | Gris-azul, workflow Figma→repo, roles M3, state layers |
| [shape.md](shape.md) | Corner tokens, botones 0→20 morph, binding Figma |
| [typography.md](typography.md) | Fraunces + Inter, mapeo a roles M3 |
| [iconography.md](iconography.md) | Material Symbols Sharp + iconos propios |
| [components.md](components.md) | M3 puro vs wrappers `MyOwnTrip*` |
| [showcase.md](showcase.md) | Documentación de componentes (showcase externo) |
| [governance.md](governance.md) | Gate `m3Canonical` + quality gates, source of truth |
| [breakpoints.md](breakpoints.md) | Window size classes, foldables Samsung, frames Figma, iOS futuro |
| [figma-prune-inventory.md](figma-prune-inventory.md) | KEEP/CUT librería Figma (móvil + foldable, sin tablet) |
| [../decisions/003-figma-library-showcase-docs.md](../decisions/003-figma-library-showcase-docs.md) | Figma = librería; showcase = docs |
| [../decisions/001-m3-native-ds.md](../decisions/001-m3-native-ds.md) | ADR estructura M3 |
| [../decisions/002-brand-editorial-m3.md](../decisions/002-brand-editorial-m3.md) | ADR marca editorial |
| [../decisions/004-button-shape-morph.md](../decisions/004-button-shape-morph.md) | ADR shape botones 0→20 |

## Pipeline activo

```text
Figma (variables · Desktop Bridge) → variables.json → Compose
         ↓
    Showcase externo (documentación de componentes)
```

Color: **no** MTB como bucle principal (re-harmoniza `primary`). Ver [color.md](color.md).

**Figma:** solo variables y component sets — **no** láminas ni guías en el archivo DS.  
**Showcase:** fichas de componente, variantes, a11y, ejemplos — ver [showcase.md](showcase.md).

- Repo DS histórico: [MyOwnTrip](https://github.com/teresanov/MyOwnTrip) (solo consulta).
- Figma: [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=55141-14168)
- Notion: [Proyecto](https://www.notion.so/3796a48d93c8819486cfe3a7fd3f624e) · [Design System](https://www.notion.so/3796a48d93c88168b7dcf9d7e81f9bfa)

## Reglas para agentes

1. Consumir `MaterialTheme.colorScheme` y `MaterialTheme.typography`.
2. Prohibido tokens de color por estado; state layers en runtime.
3. Prohibido portar `SemanticColors`, `LayoutTokens` o wrappers del repo archivo.
4. Error = icono + texto, nunca solo color.
5. Ver `.cursor/rules/m3-native-ui.mdc`.
