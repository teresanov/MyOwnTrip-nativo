# Color — roles M3 (MyOwnTrip Nativo)

Fuente en código: `app/.../ui/theme/Color.kt` → `Theme.kt`.  
ADR: [002-brand-editorial-m3.md](../decisions/002-brand-editorial-m3.md).

> **Estado:** seeds y overrides definidos. Tabla Light/Dark completa se rellena al importar export MTB (pendiente handoff).  
> **Figma:** [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=55141-14168)

## Material Theme Builder

| Parámetro | Valor |
|-----------|-------|
| Scheme | Tonal Spot |
| Contrast | Default (+ Medium/High cuando aplique) |
| Modos | Light + Dark |
| Generate state layers | **NO** |

### Seeds y overrides

| Input MTB | Hex | Notas |
|-----------|-----|-------|
| Source color (primary) | `#1F3A5F` | Azul profundo editorial |
| Secondary | `#3A4A63` | |
| Tertiary | `#D9382C` | Acento de marca (rojo señal) |
| Neutral | `#8A8275` | Taupe cálido → superficie papel |
| Neutral variant | `#847E72` | |
| Error | `#B3261E` | Rojo profundo; **distinto** de tertiary |

### Check de superficie

Tras generar Light, `surface` debe aproximarse a **papel cálido** `~#F4F0E8`.  
Si sale frío → subir Neutral a `#8C8472` y regenerar.

## Extensiones custom

Vía `LocalExtendedColors` en `Theme.kt`. No son roles M3.

| Token | Hex | Uso |
|-------|-----|-----|
| success | `#2E7D32` | Feedback positivo |
| warning | `#F9A825` | Avisos |
| info | `#1565C0` | Información (opcional) |

## Consumo en UI

- **Sí:** `MaterialTheme.colorScheme.primary`, `.onSurface`, `.error`, etc.
- **No:** constantes `AppColors.*` en pantallas (solo en `Theme.kt`).
- **No:** primitivos MTB ni tonos sueltos.
- **No:** tokens por estado (`primary.hover`, `text/disabled`, …).

## State layers (runtime, no tokens)

Aplicados por componentes M3 o `Modifier` compartido en custom.

| Estado | Opacidad sobre `on*` |
|--------|----------------------|
| Hover | 8% |
| Focus | 10% |
| Pressed | 10% |
| Dragged | 16% |
| Disabled contenido | 38% |
| Disabled contenedor | 12% |

## Error y acento

| Rol | Uso |
|-----|-----|
| `error` / `onError` | Validación, fallos — **siempre** con icono + texto |
| `tertiary` | Acento de marca puntual (CTA secundario editorial, énfasis) — no sustituye `error` |

## Tabla de roles (pendiente MTB)

Se completará al recibir export. Placeholder del tema anterior (v0.1.0) — **no usar en UI nueva**:

<details>
<summary>Tema v0.1.0 (obsoleto)</summary>

Primary `#3D63D1`, secondary `#219A60`, surface Light `#FDFBFF`.

</details>
