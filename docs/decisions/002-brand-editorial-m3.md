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
- Estética: editorial, papel cálido, acento ocre con moderación.
- Tertiary (`#825513`) = acento de marca; **error** (`#B3261E`) = rojo profundo distinto a propósito.

### Color
- **Primary (gris-azul, jun 2026):** Light `#4A5864` · Dark `#B4BAC2` · `primaryContainer` Light `#E4E1DC` · Dark `#3A444C`.
- **Tertiary (ocre):** `#825513` Light · `#F8BB71` Dark (armonía MTB en Dark).
- **Error:** `#B3261E` — distinto de tertiary a propósito.
- Neutros seeds (si regeneras en MTB): `#8A8275` / `#847E72`.
- **Workflow:** Figma (Bridge) → `variables.json` → `Color.kt`. **No** MTB como bucle principal (re-harmoniza a azul).
- **Generate state layers = NO** (runtime only).
- Check: `surface` Light ≈ `#F4F0E8` (papel cálido).

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
- Figma: clon del Material 3 Design Kit tematizado con MTB (**solo librería** — doc en showcase, ADR 003).

### Gate `m3Canonical` (bloqueante)
1. Prohibido tokens de color por estado (`hover`/`pressed`/`focus`/`disabled`).
2. Estados = state layers en runtime (`onX` @ 8/10/10/16%); disabled 38%/12%; nunca color guardado.
3. UI solo roles semánticos M3; jamás primitivos/tonos.
4. Tertiary (`#825513`, marca) y Error (`#B3261E`, alerta) — no mezclar.
5. Errores: icono + texto; nunca solo color.
6. Fraunces (Display/Headline) + Inter (resto); serif solo arriba.

Detalle y gates complementarias: `docs/design-system/governance.md`.
Focus ring: capa separada (futuro ADR).

## Consecuencias
- Figma: [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=55141-14168)
- `Color.kt` / `Type.kt` se actualizan desde export Figma (`variables.json`).
- `docs/design-system/color.md` — tabla Light/Dark y workflow Bridge (jun 2026).
- Quality gates en `docs/design-system/governance.md`.

## Referencias
- ADR 001: `001-m3-native-ds.md`
- Handoff: brief de marca (junio 2026)

## Historial
- **2026-06-12:** Primary de tinta azulada (`#1F3A5F`) a gris-azul (`#4A5864`); workflow Figma-first. Detalle en Changelog DS (Notion).
