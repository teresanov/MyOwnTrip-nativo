# Sesión 4–5 nov 2026 — Cards, Wallet y Spread

> **Notion:** [Changelog DS](https://www.notion.so/3796a48d93c88146afc8cd35f80e6e98) (5 nov 2026). Regla de cards también en [Para diseño](https://www.notion.so/3796a48d93c8812f8939f94ebc11e6cd).

Dos tardes en la librería Figma (`zrGAL4v6`). Compose no se tocó. Objetivo: que las cards de Wallet existan de verdad en el DS, que Outlined/Elevated se entiendan, y que el diario no viva en un cajón llamado Journal dentro de Cards.

---

## Qué hicimos

### Wallet en Stacked card

Las piezas que la app ya pinta (Próximos, QR, error de PDF, guardar en el teléfono) no tenían layout en el set. Se añadieron como **Layout** de Stacked card, solo **Elevated**:

- Highlight · QR · Error · Switch

Outlined de esos cuatro se creó y se **borró**: en Wallet no hay borde. El resto de layouts de Home (Media & text, Text only, etc.) se dejó en el set como catálogo.

Assistive chip ganó **Style = Warning** (plan / fuente del documento).

### Outlined vs Elevated (la pregunta que había que zajar)

No es jerarquía («importante» vs «secundario») ni «seleccionado».

La pantalla es el **papel del cuaderno**.

- **Outlined** — escrito en el papel. Huecos, filtros, lista de viajes.
- **Elevated** — recorte pegado: documento, QR, aviso, interruptor. Objeto que se puede guardar, enseñar o mover.

**Seleccionado** es otra cosa: fill + contenido (Filter chip, Toggle). Si Elevated significara «elegido», no podrías levantar un recorte sin que parezca activo.

Home hoy: Outlined. Wallet hoy: Elevated. El viaje destacado no es Horizontal Elevated: es **TripHeroCard**.

### Specs en Figma

Teresa tiene Specs (beta). El Bridge no puede crear el nodo Spec: se pega con **D**. La Spec sirve para **uso** (dónde, de qué está compuesto, cómo se comporta), no para inventario ni para el rastro de cómo organizamos el archivo.

### Ordenar Cards y dejar de fingir un módulo Journal

La sección Cards se reorganizó (sets en rejilla, huérfanos fuera).

Había un cajón **Journal · Diario** en la misma página: pills, toolbar de boceto, FAB menu, chrome del álbum, scrapbook. Eso no es Cards.

Criterio: **la librería va por tipo de control**, no por pantalla de la app.

| Pieza | Dónde quedó |
|-------|-------------|
| Day pill | Chips |
| Sketch swatches + toolbar | Toolbars |
| Day stamp | Labels |
| Sticker shape choice | Radio |
| **Journal capture FAB menu** | **Eliminado.** Era un wrapper. El set es **FAB menu** (Buttons); menú abierto = instancia con 5 segmentos visibles |
| Scrapbook page + Spread piece | Cards · marco **Spread · special cards** |
| Album chrome | App bars — **no es una card** (contador + Day pills) |

Chips se reordenó: Filter ya no pisa Assistive; Day pill en rejilla Selected True/False.

### Spec de Spread (en curso)

Ficha para diseñadores: álbum de hojas, piezas pegadas, no lista. Anatomía de Scrapbook page (Empty/Full) y de Spread piece (Photo, Text, Audio, Sketch, Sticker, Event bookmark). Comportamiento: toque abre, long-press mueve, snap, FAB crea.

Tokens en la Spec: **variables y effect styles**, no hex. Hoja = Brand/`paper`; escritorio = Surface Dim; sombras = Elevation Light 2–4; al arrastrar **no** cambia la sombra, crece al 106 %. Snap 18 dp del lienzo (aún sin `mot/spacing` propio).

---

## Decisiones

| Tema | Decisión |
|------|----------|
| Outlined / Elevated | Papel vs recorte. No énfasis, no selected. |
| Selected | Fill + contenido. Elevated se reserva a objetos que se mueven. |
| Layouts Wallet Outlined | No. Borrados. |
| Catálogo Home no usado | No borrar. |
| FAB de captura | Instancia de **FAB menu**, no componente Journal. |
| Spread | Cards especiales = hoja + pieza. Chrome del álbum = App bars. |
| Specs | Uso y anatomía. Sin «lo que movimos de página». |
| Color en Specs | Semánticos (Brand/paper, Schemes/*, Elevation). |

---

## Enlaces Figma

- [Cards](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=55412-13979)
- [Stacked card](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=52346-27573)
- [Spread · special cards](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61370-27708)
- [FAB menu](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=57998-42882)
- [Chips](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=55412-15758)
- [Spread album chrome](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61262-28962)

---

## Repo

- `docs/design-system/figma-compose-bindings.md` — filas Spread piece, Scrapbook page, Spread album chrome
- Fichas de componente (descripción Dev Mode) en Stacked y Horizontal

---

## Pendiente

- [ ] Publicar la librería DS (Wallet layouts, Warning chip, FAB doc, Spread)
- [ ] Spec Spread pegada en el canvas (D) junto al marco
- [ ] Design-file Wallet: instancias, no generar hasta publish
- [ ] Sustituir en pantallas cualquier instancia vieja de «Journal capture FAB menu» por FAB menu
- [x] Pegar esta entrada en Notion Changelog DS
- [ ] (Opcional) `mot/spacing` para el grid 18 del lienzo Spread
- [ ] (Opcional) Alinear el set Spread piece de Figma al polaroid real del álbum Compose
