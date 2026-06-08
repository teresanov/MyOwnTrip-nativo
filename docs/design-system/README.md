# Design System — MyOwnTrip Nativo

M3 canónico con identidad editorial. Sin capa semántica propia ni pipeline Pencil.

## Dirección de marca

| Dimensión | Decisión |
|-----------|----------|
| Persona | Viajero urbano organizado (ciudad, transporte, hoteles de diseño) |
| Estética | Editorial (Monocle / Wallpaper): sobriedad, tipografía protagonista, color con cuentagotas, neutros papel cálido |
| Acento | Rojo señal (`tertiary`) con moderación; error visualmente distinto |

## Documentación

| Doc | Contenido |
|-----|-----------|
| [color.md](color.md) | MTB, roles M3, extensiones, state layers |
| [typography.md](typography.md) | Fraunces + Inter, mapeo a roles M3 |
| [iconography.md](iconography.md) | Material Symbols Sharp + iconos propios |
| [components.md](components.md) | M3 puro vs wrappers `MyOwnTrip*` |
| [governance.md](governance.md) | Gate `m3Canonical` + quality gates, source of truth |
| [../decisions/001-m3-native-ds.md](../decisions/001-m3-native-ds.md) | ADR estructura M3 |
| [../decisions/002-brand-editorial-m3.md](../decisions/002-brand-editorial-m3.md) | ADR marca editorial |

## Pipeline activo

```text
Material Theme Builder → Figma (M3 Design Kit tematizado) → Compose
```

- Repo DS histórico: [MyOwnTrip](https://github.com/teresanov/MyOwnTrip) (solo consulta).
- Figma objetivo: **MyOwnTrip · Design System (M3 Native)**.

## Reglas para agentes

1. Consumir `MaterialTheme.colorScheme` y `MaterialTheme.typography`.
2. Prohibido tokens de color por estado; state layers en runtime.
3. Prohibido portar `SemanticColors`, `LayoutTokens` o wrappers del repo archivo.
4. Error = icono + texto, nunca solo color.
5. Ver `.cursor/rules/m3-native-ui.mdc`.
