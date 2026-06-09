# Checklist — Poda manual Figma (producto + M3 creativo)

**Archivo de trabajo:** [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System)  
**CS (solo referencia, no tocar):** `uWmxOSQfjOxlEJ8k1yzOSX`

Poda **a mano** en el archivo de trabajo. Objetivo: librería ágil **sin renunciar** a una app atractiva que exprima M3 y la marca editorial (ADR 002).

---

## Norte (antes de borrar nada)

Pregunta para cada set: *¿ayuda a diseñar pantallas que se sientan MyOwnTrip (editorial, cálida, tipografía protagonista) o solo añade peso del kit genérico?*

| Sí conservar si… | Sí podar si… |
|------------------|--------------|
| Aparece (o aparecerá pronto) en un flujo JTBD | Es desktop, XR o patrón que la app no usará en 12 meses |
| Permite composiciones ricas (cards, sheets, chips, listas) | Es página de documentación del kit (TOC, Examples, Getting started) |
| Inspira micro-interacciones creíbles en Compose | Duplica otro set con el mismo job (p. ej. dos grupos de botones para lo mismo) |
| Los BB (`.` ocultos) sostienen instancias publicadas | Solo demos rotas sin component set local |

**Marca en Figma:** `tertiary` = acento con moderación · `error` ≠ marca · Fraunces solo Display/Headline en diseños de pantalla (la librería puede usar el kit; las pantallas no).

---

## 1. Foundations — siempre KEEP

- [ ] Colección variables **M3** (Light/Dark) enlazada a `Palettes/*`
- [ ] Página **Icons** (Material Symbols Sharp)
- [ ] **Styles** mínimo (`.Shape`, swatches útiles)
- [ ] Sin páginas de doc de componentes en canvas (eso va al showcase)

---

## 2. Core visual — lo que hace la app “premium”

Conservar sets que dan **jerarquía, ritmo y personalidad** sin salir de M3.

| Área | KEEP | Por qué (producto / creatividad) |
|------|------|----------------------------------|
| **Cards** | Stacked, Horizontal + BB states (Outlined/Elevated/Filled) | Piezas editoriales: viaje, restaurante, entrada wallet |
| **App bars** | App bar + BB (flat, on-scroll, contenidos) | Cabeceras con carácter; búsqueda integrada |
| **Buttons** | filled, tonal, outline, text, **elevated** | Jerarquía de acciones; elevated para CTAs con relieve suave |
| **Icon button** | 4 estilos | Barras, formularios, acciones compactas |
| **FAB** | FAB + Extended FAB | Acción primaria flotante (añadir gasto, nota, lugar) |
| **Chips** | Filter, Assist, Suggestion, Input + **Chip groups** | Filtros de viaje/gasto; grupos para barras de filtro densas |
| **Lists** | List, List item, densidades + **Accordion**, **Swipe**, BB | Itinerario, wallet, restaurantes; swipe para acciones rápidas |
| **Tabs** | Tabs + BB primary/secondary | Cambio de vista (día / mapa / lista) |
| **Sheets** | Bottom sheet + BB | Flujos contextuales sin romper el hilo |
| **Search** | Search bar (mínimo) | Entrada universal; layouts full-screen → ver §4 |
| **Sliders** | Standard, Centered, Range | Presupuesto, distancia, preferencias — gesto M3 muy expresivo |
| **Date pickers** | Input + Modal + BB calendario | Planificación de viaje |
| **Dialogs** | Basic + **Scrollable list dialog** | Confirmaciones y listas largas |
| **Snackbar** | Snackbar + BB | Feedback ligero post-acción |
| **Loading** | Linear/Circular + Loading indicator + BB | Estados de carga con pulido |
| **Text fields** | Text field | Formularios viaje / wallet |
| **Navigation** | Bar H/V + BB | Nav inferior; sin rail salvo cambio de arquitectura |

---

## 3. Capa “diseñador feliz” — Should creativo

Sets que **no son P0 en código** pero enriquecen exploración en Figma. Mantener si tienes espacio y usas la sección.

- [ ] **Segmented button** + BB (start/middle/end) — toggles Mapa \| Lista \| Día
- [ ] **FAB menu** + BB — menú de acciones rápidas
- [ ] **Toggle button** (4 estilos) — filtros multi-selección visuales
- [ ] **Icon button togglable** (4) — favoritos, vistas, pins
- [ ] **Button groups** (Connected / Standard) — solo si diseñas barras de acción compuestas; si no, podar la sección entera
- [ ] **Badges**, **Checkbox**, **Radio**, **Switch**, **Menu** + BB

**Variantes en Figma (no recortar Type/State):** conservar ejes completos para diseño; en Compose los estados interactivos van por state layers (`m3Canonical`).

**Tamaños:** podar **XLarge** a mano cuando moleste; conservar XSmall → Large.

---

## 4. “Más adelante” — no borrar sin pensar

No son MVP, pero pueden hacer la app **más atractiva** en v1.1. Opciones:

| Set | Idea de uso creativo | Decisión |
|-----|----------------------|----------|
| Search full-screen / docked | Búsqueda inmersiva tipo “explorar ciudad” | [ ] KEEP en librería · [ ] Podar |
| Side sheet | Panel de detalle lateral (lugar, gasto) | [ ] KEEP · [ ] Podar |
| Navigation drawer | Solo si cambiáis arquitectura nav | [ ] Podar por ahora |
| Time pickers (Dial, etc.) | Recordatorios, hora de vuelo | [ ] Podar (móvil usa modal/input) |

---

## 5. Podar sin miedo (ruido del kit)

- [ ] Getting started, Table of contents, Examples, Shape, Utilities, `---`
- [ ] Carousel, Toolbars, Tooltips, Avatars (genéricos del kit)
- [ ] **XR/***, Navigation rail*, Bottom app bar
- [ ] Split button (caso muy nicho)
- [ ] List dialog (no scrollable) si tenéis Scrollable list dialog
- [ ] Time pickers desktop (Dial, Keyboard, Hour, Period…)
- [ ] Demos sueltas / instancias rotas tras mover sets
- [ ] Duplicados huérfanos tras restaurar desde CS

---

## 6. Ritual de poda manual (orden sugerido)

1. [ ] **CS cerrada** o Bridge solo en archivo de trabajo
2. [ ] Copiar **component sets** desde CS (no instancias demo) → pegar en trabajo → **Swap library** si hace falta
3. [ ] Por **página**: Header → Component set → Labels → Building blocks (`.` en BB)
4. [ ] Borrar **páginas** de doc (§5) antes que sets sueltos
5. [ ] **XLarge** a mano, de abajo arriba en la sección (no mover sets con script)
6. [ ] Comprobar **Go to main component** → siempre archivo de trabajo, nunca CS
7. [ ] Publicar librería del archivo de trabajo
8. [ ] Actualizar inventario en `figma-mobile-subset.md` si cambia el acuerdo

---

## 7. Creatividad M3 × MyOwnTrip (ideas para pantallas)

Usar la librería podada para explorar:

| Patrón M3 | Idea MyOwnTrip |
|-----------|----------------|
| Card Elevated + headline Fraunces | Ficha de escapada / hotel diseño |
| Segmented + Filter chips | Día del viaje + categoría de gasto |
| Bottom sheet + List | Detalle de lugar sin pantalla nueva |
| Range slider | Rango de presupuesto del viaje |
| FAB menu | Añadir: gasto, nota, lugar, foto |
| List swipe | Archivar gasto, marcar visitado |
| Modal date picker | Fechas del viaje con calendario M3 |
| Tonal buttons en fila | Acciones secundarias sin competir con primary |

**Límite creativo (Compose):** color solo `colorScheme`; acento `tertiary` con moderación; errores siempre icono + texto.

---

## 8. Cuando termines

- [ ] Librería publicada desde archivo de trabajo
- [ ] CS desactivada como librería en archivos de pantalla
- [ ] Inventario aproximado de sets (nota en `audits/` si quieres trazabilidad)
- [ ] Fichas showcase priorizadas: P0 Button, TextField, FilterChip → P1 Card, ListItem → P2 resto usado

---

## Referencias

- Subset técnico: [`figma-mobile-subset.md`](figma-mobile-subset.md)
- Marca: [`002-brand-editorial-m3.md`](../decisions/002-brand-editorial-m3.md)
- Gates: [`governance.md`](governance.md) · `m3Canonical`
