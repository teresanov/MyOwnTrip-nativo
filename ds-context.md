# MyOwnTrip · Design System — contexto para agentes

> Generado el 2026-11-13 desde `ds-showcase/src/data/`. **No editar a mano:** los cambios se pierden en el siguiente build.
> Fuente de verdad del comportamiento: el código de `designsystem/` y `app/`.

## Principios

- Material 3 canónico: no hay capa semántica propia de color ni de espaciado de componente.
- La identidad está en la tipografía (Fraunces + Inter), en el color con cuentagotas y en el morph del botón.
- Figma es librería visual; la documentación de componentes vive en el showcase.
- Las pantallas no inventan UI: ensamblan composables del sistema más MaterialTheme.

## Obligatorio

- Consumir MaterialTheme.colorScheme (roles M3: primary, onSurface, error…).
- Consumir MaterialTheme.typography, mapeado a Fraunces en Display y Headline e Inter de Title hacia abajo.
- Estados interactivos como state layers en runtime: hover 8 %, focus 10 %, pressed 10 %, dragged 16 %.
- Disabled: contenido al 38 % y contenedor al 12 %.
- Errores: icono y texto, nunca solo color.
- Iconos de UI: Material Symbols Sharp peso 300, vía MOTIcons.
- Espaciado de pantalla: MOTSpacing. El padding interno de los componentes M3 se deja en sus defaults.
- Botones: usar los wrappers MOT (MOTButton, MOTTonalButton, MOTOutlinedButton, MOTTextButton, MOTIconButton), que aplican el morph de forma del ADR 004.
- Respetar LocalReduceMotion en cualquier animación.

## Prohibido

- Tokens de color por estado: primary.hover, *.pressed, text/disabled, semantic/color/*.
- Primitivos de color o tonos sueltos en pantallas; AppColors solo dentro de Theme.kt.
- SemanticColors, LayoutTokens y wrappers del repo archivo MyOwnTrip.
- Fraunces en labels de botón, campos o chips.
- Un segundo set de iconos para UI funcional (navegación, acciones estándar).
- Documentar componentes dentro de Figma: ni láminas, ni Do/Don't, ni changelog en canvas.
- Pasar MaterialTheme.shapes.* como shape de un botón: el morph vive fuera de esa escala.
- La colección Figma legado Semantic · Layout en pantallas o binds nuevos.
- Un segundo semántico de forma tipo MOTCorner.

## Roles de color clave

| Rol | Light | Nota |
| --- | --- | --- |
| `primary` | `#4A5864` | Gris-azul tinta |
| `tertiary` | `#825513` | Acento ocre de marca, UI |
| `error` | `#904A42` | Validación — distinto de tertiary a propósito |
| `surface` | `#FFF8F2` | Papel cálido |

## State layers (runtime, no tokens)

- hover: 8%
- focus: 10%
- pressed: 10%
- dragged: 16%
- disabledContent: 38%
- disabledContainer: 12%

## Gates

- `m3Canonical` **(bloqueante)** — Antes de cerrar cualquier UI
- `semanticTokenOnlyConsumption` — Cada PR de UI
- `showcaseDocComplete` **(bloqueante)** — Cada componente nuevo o modificado
- `a11yCheck` — Cada pantalla
- `figmaAliasChainValid` — Cambio en la librería Figma
- `visualRegression` — Tras un cambio visual grande

## Componentes documentados

## Button

- Estado: in-review · P0
- Para qué: Un botón ejecuta una acción en el sitio donde está: confirmar, guardar, continuar. En MyOwnTrip son crear un viaje, guardar un gasto y confirmar una entrada del Wallet.
- Composable: `MOTButton` — `com.myowntrip.app.ui.theme.MOTButton`
- Fuente: `designsystem/src/main/java/com/myowntrip/app/ui/theme/MOTButtons.kt`

### Variantes

- **Filled** → `MOTButton`: Acción principal de la pantalla (una por vista).
- **Tonal** → `MOTTonalButton`: Acción secundaria con énfasis moderado (secondaryContainer).
- **Outlined** → `MOTOutlinedButton`: Acciones alternativas o cancelar.
- **Text** → `MOTTextButton`: Acciones terciarias, bajo peso visual.

### Cuándo usarlo

- La persona ejecuta algo que cambia datos o cierra un flujo: guardar, confirmar, crear, eliminar.
- La acción necesita una etiqueta con verbo para que se entienda sin contexto.
- Hay que marcar jerarquía entre dos o tres acciones que conviven en la misma pantalla.

### Cuándo NO usarlo

- Filtrar o alternar un valor que se queda activo → Filter chip
- Acción compacta en la top app bar o dentro de un campo → MOTIconButton
- Navegar a un detalle desde una lista o una tarjeta → List item o Card — la fila entera es el área pulsable
- Llevar a otro sitio desde dentro de un párrafo → Un enlace, no un Text button

### Elegir variante

- Es la acción que completa el flujo (Guardar, Confirmar, Hecho) → `MOTButton` — Una sola por pantalla.
- Acción de apoyo que necesita más peso que un outlined (Siguiente en onboarding) → `MOTTonalButton` — Padding XSmall: es visualmente más compacto.
- Acción importante que no es la principal, o salir del flujo → `MOTOutlinedButton` — También para «dar la opción de cambiar de opinión».
- Varias opciones de baja prioridad juntas, o acciones de un diálogo → `MOTTextButton` — El label es siempre labelLarge; no escala por tamaño.
- El botón necesita separarse de un fondo con patrón o de una imagen → `Button de M3 + elevation` — No hay wrapper MOT para elevated; poco uso en Compose.

### Prohibido en este componente

- No coloques un botón debajo de otro si hay espacio para ponerlos uno al lado del otro.
- No uses Elevated salvo que el botón necesite separarse visualmente de un fondo con patrón: evita acumular sombras.
- No usar tertiary para acciones destructivas — usar error más confirmación.
- No crear tokens button/hover ni similares.
- No usar Fraunces en el label del botón.
- No usar MaterialTheme.shapes.* para dar forma a un botón: el morph vive fuera de esa escala.

### Accesibilidad

- Área táctil mínima de 48 dp de alto, aunque el contenedor visible sea más bajo (Tonal con padding XSmall queda en 32 dp visibles).
- El morph a 20 dp no es el indicador de foco: el foco necesita su propio anillo visible, separado del contenido.
- Disabled se comunica con opacidad (38 % contenido, 12 % contenedor), nunca con un color distinto.
- La etiqueta debe entenderse fuera de contexto: «Guardar gasto», no «Aceptar». TalkBack la lee sola.
- Si la acción falla, el error se comunica con icono y texto; nunca solo con color.

### Uso típico

```kotlin
MOTButton(
    onClick = { onSave() },
    modifier = Modifier.fillMaxWidth(),
    enabled = !state.isSaving,
) {
    Text("Guardar gasto")
}
```

## Text field

- Estado: ready · P0
- Para qué: Los campos de texto recogen la información que el usuario escribe en formularios: nombre del viaje, importes, títulos de documentos y notas. En pantallas nuevas usamos siempre la variante Outlined de Material 3 (`OutlinedTextField`), enlazada al tema de la app sin wrapper MOT adicional.
- Composable: sin documentar en la ficha

### Variantes

- **Outlined · Enabled**: Estado por defecto en pantallas de formulario.
- **Outlined · Focused**: Borde y label en `primary`; state layer M3 en runtime.
- **Outlined · Error**: Validación inline: borde + supporting text + icono de error (nunca solo color).
- **Outlined · Disabled**: Opacidad 38% contenido / 12% contenedor; p. ej. campos bloqueados tras guardar viaje.

### Prohibido en este componente

- No añadas TextField Filled en flujos nuevos salvo que documentes un gap explícito.
- No inventes tokens por estado (`hover`, `pressed`) ni primitivos legacy en UI.
- No uses Fraunces en label ni en el texto del input.
- No simules campos con rectángulos sueltos en Figma: instancia el set Outlined.

### Accesibilidad

- Label asociado al campo (TalkBack lee label + valor + error).
- Supporting text de error en `liveRegion` cuando cambia tras submit.
- Teclado: `ImeAction` coherente (Next / Done).
- Contraste label/input ≥ 4.5:1 sobre surface.

## Filter chip

- Estado: ready · P0
- Para qué: Los filter chips permiten elegir una opción dentro de un conjunto pequeño y cambiarla con un solo toque. En MyOwnTrip los usamos en filas horizontales con scroll —por ejemplo, Activos / Archivados / Todos en Wallet— y en formularios cuando hay pocas categorías. Implementamos el componente Material 3 `FilterChip` sin wrapper propio; en Home el filtro de viajes sigue el patrón Menu, no chips.
- Composable: sin documentar en la ficha

### Variantes

- **Outlined · Selected=False**: Opciones no activas en la fila (p. ej. «Archivados»).
- **Outlined · Selected=True**: Filtro activo (p. ej. «Activos» en Wallet).

### Prohibido en este componente

- No sustituyas acciones principales por chips: las acciones que avanzan o retroceden en un paso van siempre como botones.
- No uses chips para terminar o avanzar una tarea; el último paso de una tarea es un botón.
- No muestres un chip suelto: deben aparecer en un conjunto.
- No mezcles selección única y múltiple entre los conjuntos de chips de una misma pantalla.
- No eleves los chips cuando están directamente sobre la página, ni uses la elevación para indicar el estado pulsado (usa el ripple).
- Evita frases negativas en los filter chips, como «Excluir imágenes».
- No reintroduzcas la variante Elevated del set (fue eliminada en la Fase D de la librería).
- No uses esquinas Extra-small en chips: ese radio pertenece al Text field.
- No dibujes chips con frames sueltos en el design-file; instancia siempre el set publicado.
- No sustituyas el menú de filtro de Home por FilterChip: el patrón acordado es Menu.

### Accesibilidad

- Cada chip: rol toggleable; anunciar selected/unselected.
- Alternativa sin scroll horizontal forzado: mismo filtro accesible vía Menu donde aplique.
- Touch target ≥ 48dp vía padding M3.

## Basic dialog

- Estado: ready · P0
- Para qué: Los diálogos básicos interrumpen la tarea para pedir confirmación o advertir de una pérdida de datos. Los usamos al guardar documentos en Wallet (hipótesis H7), al eliminar entradas, al descartar cambios o al salir de un formulario sin guardar. En Compose combinamos `AlertDialog` de Material 3 con `MOTTextButton` en las acciones.
- Composable: sin documentar en la ficha

### Variantes

- **Icon=False**: Confirmación neutra — cap 7 Wallet «Confirmar guardado» (Figma resume copy; Compose puede incluir Switch plan).
- **Icon=True**: Riesgo o pérdida — caps 11 eliminar, 12 descartar (glyph error).

### Prohibido en este componente

- No uses un diálogo para información de prioridad baja o media: usa un snackbar.
- No empieces el titular con disculpas («Perdona la interrupción»), alarma («¡Atención!») ni ambigüedad («¿Estás seguro?»).
- No coloques las acciones de descarte a la derecha de las de confirmación.
- Evita opciones poco claras: «Cancelar» no tiene sentido si no se propone una acción concreta.
- No añadas una tercera acción como «Más información»: saca al usuario del diálogo y deja la tarea sin terminar.
- No BODY con TEXT suelto encima del dialog en Figma.
- No mezclar `Corner/Medium` de cards — dialog = Extra-large.
- No añadir Switch suelto en cap Figma si no existe en la variante del set.
- No bloquear dismiss sin copy claro (Seguir editando / Cancelar).

### Accesibilidad

- Foco atrapado en dialog; dismiss con back donde aplique.
- Headline leído antes que supporting text.
- Botón destructivo anunciado con consecuencia en supporting text.
- Icon decorativo: `contentDescription = null` si el título ya comunica el riesgo.

## Snackbar

- Estado: ready · P0
- Para qué: Las snackbars confirman que una acción se completó o ofrecen una vía rápida para deshacerla, sin sacar al usuario de la pantalla. Aparecen en Day Hub, Diario, Gastos y Documentos cuando guardamos o revertimos cambios menores. En Compose van dentro de un `SnackbarHost` en el `Scaffold`, usando el componente `Snackbar` de Material 3.
- Composable: sin documentar en la ficha

### Variantes

- **Text only**: Ack simple sin acción.
- **Text + action**: Deshacer o reintentar.
- **Two lines**: Obligatoria cuando el mensaje ocupa dos líneas (p. ej. Wallet cap 1i «archivado» + Deshacer). No forzar One line y dejar que el texto se corte o desborde.

### Prohibido en este componente

- No lo uses para información crítica o que exige acción inmediata: ahí va un diálogo.
- No añadas iconos; si el mensaje necesita un icono, usa otro componente, como un diálogo.
- No uses texto con estilos ni enlaces en línea: si hace falta un enlace, usa un botón.
- No uses botones Filled ni Elevated en un snackbar: atraen demasiada atención.
- No lo coloques delante de la navegación, de elementos de uso frecuente ni delante o detrás de un FAB.
- No apiles snackbars ni los pongas uno junto a otro.
- No hagas del snackbar la única vía para acceder a un caso de uso esencial.
- No usar snackbar para errores críticos — preferir dialog o inline error con icono.
- No sustituir confirmaciones H7 Wallet.
- No tokens snackbar/hover.
- No usar One line si el texto del preview/cap es de dos líneas.

### Accesibilidad

- `SnackbarHostState` anuncia mensaje; acción focusable.
- No depender solo del color inverse para significado.
- Alternativa por tap si el gesto de dismiss no es obvio.

## Stacked card

- Estado: ready · P1
- Para qué: Card vertical editorial: Outlined en listas/feeds, Elevated en destacados. Layouts Media & text (avatar + media + copy) y Text only (header + supporting + dismiss). Filled está CUT.
- Composable: sin documentar en la ficha

### Variantes

- **Outlined · Media & text**: Home vacío, cards editoriales en feed.
- **Elevated · Text only**: Avisos descartables o mensajes destacados (si el producto los necesita).
- **Outlined · Text only**: Mensajes inline en listas (filtro vacío).

### Prohibido en este componente

- No fuerces contenido en cards cuando el espaciado, los titulares o los divisores darían una jerarquía más simple.
- Evita poner texto o iconos sobre imágenes si no es necesario.
- No Filled (CUT).
- No OutlinedCard ad hoc en pantallas si encaja un layout de este set.
- No icono decorativo arbitrario en Text only — copy + dismiss si aplica.

### Accesibilidad

- Dismiss con contentDescription.
- Imagen media con alt cuando es informativa.
- Avatar decorativo si el header ya se anuncia.

## TripHeroCard

- Estado: ready · P1
- Para qué: Portada del viaje destacado en Home: Background con image fill + scrim editorial, Eyebrow label de fase, countdown opcional, título y meta; CTA tonal «Ver detalles» debajo de la portada (única acción). Patrón MyOwnTrip — no es variante de Stacked card.
- Composable: sin documentar en la ficha

### Variantes

- **Elevated**: Default en Home — viaje destacado.
- **Outlined**: Misma anatomía con marco Outlined.

### Prohibido en este componente

- No usar Stacked · Media only para este patrón.
- No Assist chip ni Filter chip en la portada.
- No hacer clickable la portada ni el eyebrow.
- No duplicar el saludo/displaySmall del hero de página.
- No avatar ni menú en la portada.

### Accesibilidad

- Único control interactivo: CTA «Ver detalles» (48dp).
- Eyebrow y textos de portada: incluir fase y countdown en contentDescription del bloque o del CTA según implementación.
- stateDescription según fase (próximo / en curso / pasado).
- Imagen decorativa — sin alt aparte.
- Show countdown=false oculta la línea de countdown.

## Eyebrow label

- Estado: ready · P1
- Para qué: Etiqueta informativa no interactiva para contexto editorial (fase del viaje, categoría, aviso breve). Sustituye al Assist chip cuando el fondo es variable (fotos, scrims) y Outlined no garantiza contraste. Los chips del DS siguen reservados a filtros, selección y acciones.
- Composable: sin documentar en la ficha

### Variantes

- **Color=Tertiary · Size=Medium**: Default en TripHeroCard y portadas sobre media.
- **Color=Tertiary · Size=Small**: Portadas compactas o overlays densos.
- **Color=Surface · Size=Medium**: Etiqueta sobre surface o cards sin foto.
- **Color=Surface · Size=Small**: Variante compacta en listas.
- **Color=Secondary · Size=Medium**: Acento suave en superficies claras.
- **Color=Secondary · Size=Small**: Misma función en espacios reducidos.

### Prohibido en este componente

- No usar como sustituto de Filter chip ni Assist chip interactivo.
- No añadir sombra Elevated ni borde Outlined — no es chip.
- No hacer clickable en producto.
- No usar Assist chip Elevated (CUT) para este patrón.

### Accesibilidad

- Decorativo en sí: el texto se anuncia en el bloque padre (card o sección).
- Modifier.semantics { invisibleToUser() } si el padre ya incluye la fase en contentDescription.
- Nunca role Button ni onClick.
- Contraste mínimo 4.5:1 entre label y container en todos los pares Color.

## Swipeable list row

- Estado: in-review · P1
- Para qué: Fila de lista con acciones laterales tipo Gmail: deslizar para archivar o eliminar, con menú ⋮ equivalente por tap. Patrón Compose compartido en Home («Más viajes») y Wallet (lista plana). No usar en el hero destacado ni en carruseles.
- Composable: sin documentar en la ficha

### Variantes

- **Lista activa**: Swipe derecha → Archivar; swipe izquierda → Eliminar (con diálogo de confirmación).
- **Lista archivados**: Swipe derecha → Restaurar; swipe izquierda → Eliminar.

### Prohibido en este componente

- No envolver el viaje destacado (TripHeroCard) ni filas del carrusel Wallet «Próximos».
- No usar swipe como única vía para eliminar sin alternativa por tap.
- No colores primitivos en fondos de swipe — solo roles M3 del colorScheme.
- No crear component set en Figma para este patrón; documentar en showcase + referencia Compose.

### Accesibilidad

- customActions en semantics de la fila (TalkBack: acciones personalizadas).
- Iconos de fondo con contentDescription («Archivar», «Eliminar», «Restaurar»).
- Menú ⋮: contentDescription «Acciones del viaje» / «Acciones del documento».
- Eliminar en menú: icono + texto error — nunca solo color.
- Ver checklist manual: docs/ux/talkback-checklist-swipe.md

## Componentes sin ficha

Existen en Figma o en código, pero nadie ha escrito cuándo usarlos.
No inventes criterios de uso para estos: pregunta o consulta el código.

- **Icon button** (draft): Acciones compactas en top app bar y formularios.
- **List item** (draft): Filas en listas de viajes, wallet y restaurantes.
