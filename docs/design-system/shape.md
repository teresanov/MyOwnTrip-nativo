# Shape — MyOwnTrip Nativo

ADR: [004-button-shape-morph.md](../decisions/004-button-shape-morph.md) · Preview: [`previews/button-shape-comparison.html`](previews/button-shape-comparison.html)

## Principio

- **Botones:** recto en reposo (**0dp**), morph a **20dp** en interacción.
- **Cards y superficies:** radio fijo editorial (**12dp**).
- **Chips:** radio fijo (**8dp**).
- Tokens en Figma colección **M3** → modo **Shape**; código en `Shapes.kt`.

## Tokens de corner (Figma `M3` · Shape)

| Token | Valor | Uso MyOwnTrip |
|-------|-------|----------------|
| `Corner/None` | 0 | Botones Square **reposo** |
| `Corner/Extra-small` | 4 | Reservado M3 (poco uso MVP) |
| `Corner/Small` | 8 | Chips outlined |
| `Corner/Medium` | 12 | Cards outlined/elevated, sheets |
| `Corner/Large` | 16 | M3 default; no botones MVP |
| `Corner/Large-increased` | **20** | Botones **interactivos** (hover/focus/pressed/selected) |
| `Corner/Full` | 1000 | FAB circular |

Fuente exportada: [`variables.json`](variables.json).

## Botones — morph 0 → 20

```
Reposo          Hover / Focus / Pressed / Selected
┌──────────┐    ╭──────────────╮
│ Guardar  │ →  │   Guardar    │   ~520ms, emphasized decelerate
└──────────┘    ╰──────────────╯
  0dp                 20dp
```

| Plataforma | Estados con morph |
|------------|-------------------|
| Android táctil | pressed, focus, selected |
| Desktop / preview | + hover |
| Accesibilidad | `TRANSITION_ANIMATION_SCALE = 0` → sin animación |

**No** animar solo con color: el morph de esquina va **junto** a state layers M3 (8/10/10%).

## Compose

```kotlin
// Theme.kt — shapes de superficie (no botones)
MaterialTheme(
  shapes = MOTThemeShapes,
  motionScheme = MotionScheme.standard(),
  …
)

// Botón con morph
Button(
  onClick = { },
  shape = rememberMOTButtonShape(),
  …
)
```

| API | Archivo |
|-----|---------|
| `MOTCorner`, `MOTThemeShapes` | `ui/theme/Shapes.kt` |
| `rememberMOTButtonShape()` | `ui/theme/Shapes.kt` |
| `AppMotion.DurationShapeMorph` (520ms) | `ui/theme/Motion.kt` |

`MaterialTheme.shapes` **no** sustituye el shape de botones: siempre `rememberMOTButtonShape()` o equivalente con `InteractionSource`.

## Binding Figma (checklist manual)

Ejecutar en [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=55141-14168) tras publicar variables.

### 1. Variables

- [ ] Colección **M3** publicada con `Corner/None` = 0 y `Corner/Large-increased` = 20 (ya en `variables.json`).
- [ ] Sin variables de radio por estado (`*/hover`, `*/pressed`).

### 2. Button · Square (`55141:14168`)

| Variante | Propiedad | Variable |
|----------|-----------|----------|
| Type=Square · todos los Size | Corner radius | `Corner/None` |
| State=Disabled | Corner radius | `Corner/None` (igual que reposo) |

- [ ] XSmall, Small, Medium, Large — outlined, filled, tonal, text, elevated.
- [ ] **No** instancias Type=Round en reposo (CUT según inventario).

### 3. Toggle · Icon togglable · Segmented

| Variante | Reposo | Selected / pressed (diseño estático de referencia) |
|----------|--------|-----------------------------------------------------|
| Type=Square | `Corner/None` | — |
| Type=Round (destino morph) | — | `Corner/Large-increased` (20), no `Corner/Full` |

- [ ] KEEP variantes Round como **geometría destino** del morph, no como default de pantalla.

### 4. Icon button · Square

- [ ] Corner radius → `Corner/None` en reposo.

### 5. Cards · Chips (sin morph de botón)

| Componente | Variable |
|------------|----------|
| Card Outlined / Elevated | `Corner/Medium` (12) |
| Chip Outlined | `Corner/Small` (8) |

### 6. Verificación

- [ ] Ejemplo en `Example layout`: botones rectos al instanciar; preview interactivo en showcase / HTML.
- [ ] Contraste con card 12dp: botón reposo más «cortante», coherente con cuaderno editorial.

## Relación con M3 Expressive

- Kit Figma: **M3 oficial** (no Expressive completo).
- **Subset Expressive adoptado:** shape morph espacial en botones + `MotionScheme` en Compose.
- Dirección morph: **siempre** None → Large-increased; nunca al revés en reposo.

## Historial de exploración

| Opción | Radios reposo | Estado |
|--------|---------------|--------|
| A M3 kit | 12/12/16/28 | Descartada |
| B Fijo 8 | 8 todo | Descartada |
| C Híbrido | 8/8/8/12 | Descartada (jun 2026) |
| D None estático | 0 todo | Descartada |
| **E Morph** | **0 → 20 interactivo** | **Aceptada (ADR 004)** |
