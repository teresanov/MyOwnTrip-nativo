# ADR 002 — Marca editorial + tema M3

**Estado:** Aceptado  
**Fecha:** 2026-06-08  
**Supersede:** parcialmente ADR 001 §8 (seeds MTB)

## Contexto

MyOwnTrip apunta a un viajero urbano organizado (escapadas de ciudad, transporte, hoteles de diseño — no outdoorsy). Dirección estética **editorial** (Monocle / Wallpaper): sobriedad, tipografía protagonista, color con cuentagotas, neutros papel cálido.

ADR 001 fijó M3 estructural; este ADR fija identidad de marca y reglas de consumo que no cambian con cada iteración de hex.

## Decisión

### Marca
- Persona: viajero urbano organizado.
- Estética: editorial, papel cálido, acento rojo señal con moderación.
- Tertiary (`#D9382C`) = acento de marca; **error** (`#B3261E`) = rojo profundo distinto a propósito.

### Color (MTB)
- Scheme: **Tonal Spot** · Contrast: Default (+ Medium/High cuando aplique).
- Source color (primary): `#1F3A5F`.
- Core overrides: secondary `#3A4A63`, tertiary `#D9382C`, neutral `#8A8275`, neutral variant `#847E72`, error `#B3261E`.
- Custom: success `#2E7D32`, warning `#F9A825`, info `#1565C0` (info opcional).
- **Generate state layers = NO** en MTB (runtime only).
- Check: `surface` Light ≈ `#F4F0E8` (papel cálido). Si sale frío → neutral `#8C8472` y regenerar.

### Tipografía
- **Fraunces** (400/500/600): Display + Headline.
- **Inter** (400/500/600): Title + Body + Label.
- Bundled en `res/font/`; Roboto solo fallback.
- Serif solo Display/Headline; sans de Title hacia abajo.

### Iconos
- UI funcional: **Material Symbols Sharp**, peso ligero — nativos M3.
- Sin set propio de iconos de UI.
- Custom permitido: logo, spot art, glifos de categorías.

### Componentes
- MVP: M3 puro (`androidx.compose.material3.*`).
- Wrapper `MyOwnTrip*` solo con justificación en ADR o `docs/design-system/components.md`.
- Figma: clon del Material 3 Design Kit tematizado con MTB.

### Gate `m3Canonical` (bloqueante)
1. Prohibido tokens de color por estado (`hover`/`pressed`/`focus`/`disabled`).
2. Estados = state layers en runtime (`onX` @ 8/10/10/16%); disabled 38%/12%; nunca color guardado.
3. UI solo roles semánticos M3; jamás primitivos/tonos.
4. Tertiary (`#D9382C`, marca) y Error (`#B3261E`, alerta) — no mezclar.
5. Errores: icono + texto; nunca solo color.
6. Fraunces (Display/Headline) + Inter (resto); serif solo arriba.

Detalle y gates complementarias: `docs/design-system/governance.md`.
Focus ring: capa separada (futuro ADR).

## Consecuencias
- `Color.kt` / `Type.kt` se actualizan al recibir export MTB + fuentes.
- `docs/design-system/color.md` documenta seeds hasta tener tabla completa Light/Dark.
- Quality gates en `docs/design-system/governance.md`.

## Referencias
- ADR 001: `001-m3-native-ds.md`
- Handoff: brief de marca (junio 2026)
