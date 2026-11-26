# MyOwnTrip · Design System — contexto para agentes

> Generado el 2026-11-26 desde `ds-showcase/src/data/`. **No editar a mano:** los cambios se pierden en el siguiente build.
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

- Estado: ready · P0
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

## Icon button

- Estado: ready · P1
- Para qué: Un icon button ejecuta una acción común y fácil de entender sin texto: volver, editar, borrar, abrir el menú de filtros. Es el control de la top app bar, de las filas de lista y de los campos con acción (calendario, borrar búsqueda).
- Composable: `MOTIconButton` — `com.myowntrip.app.ui.theme.MOTIconButton`
- Fuente: `designsystem/src/main/java/com/myowntrip/app/ui/theme/MOTButtons.kt`

### Variantes

- **Square** → `MOTIconButton(type = Square)`: Por defecto. Rectangular en reposo y redondeado a 20 dp al interactuar, como Button. App bars, filas, campos y cards.
- **Round** → `MOTIconButton(type = Round)`: Ya redondeado a 20 dp en reposo. No es un círculo: el único control circular es el FAB.
- **Estilo Standard** → `MOTIconButton()`: Sin contenedor visible: solo el icono; la capa de estado aparece al interactuar. Es el estilo de todos los usos actuales de la app.
- **Estilos Filled y Tonal** → `colors = IconButtonDefaults.filledIconButtonColors() / filledTonalIconButtonColors()`: Con contenedor de color. Es el estilo por defecto del set «Icon button» de Figma; en código se pide con colors.
- **Tamaños XSmall · Small · Medium · Large** → `size = MOTIconButtonSize.*`: Alto del contenedor 32 · 40 · 56 · 96 dp. Small (40) es el de producto.
- **Anchos Narrow · Default · Wide** → `width = MOTIconButtonWidth.*`: Más estrecho o más ancho que el cuadrado, para alinear con otros controles o dar más peso a la acción.

### Cuándo usarlo

- La acción tiene un icono inequívoco (arrow_back, edit, delete, close, tune).
- El espacio es escaso: top app bar, fila de lista, dentro de un campo o de una card.
- Se usan pocos a la vez; más de tres en la misma barra es señal de que sobra alguno.

### Cuándo NO usarlo

- La acción necesita un verbo para entenderse («Guardar gasto») → Button
- Elegir un filtro que se queda activo → Filter chip
- Acción principal flotante de la pantalla → FAB (circular, sin morph)

### Elegir variante

- Acción común en app bar, fila, campo o card (casi todos los usos) → `MOTIconButton Square, Small` — Sin contenedor visible: bajo énfasis.
- El botón debe verse ya redondeado, sin esperar a la interacción → `MOTIconButton Round` — Mismo radio de 20 dp, en reposo.
- Necesitas separarlo de una imagen o de un fondo variable → `MOTIconButton con colors Filled o Tonal` — No hay wrappers propios: el contenedor se pide con IconButtonDefaults.filledIconButtonColors() o filledTonalIconButtonColors(). Outlined no está disponible en el wrapper.
- Representa un estado activable (favorito, marcador) → `Pendiente` — El wrapper no expone hoy un modo seleccionable.

### Prohibido en este componente

- No abuses del estilo Filled: úsalo con moderación en acciones de alto énfasis (descargar, eliminar).
- No uses un icono ambiguo cuya acción no se entienda sin texto: en ese caso usa un Button.
- No usar IconButton de M3 suelto: se pierde el morph de forma.
- No dejar un icono como único contenido del botón sin nombre accesible en ningún sitio.
- No usar un círculo: Round es un radio de 20 dp, no Corner/Full. El único control circular es el FAB.

### Accesibilidad

- Todo icon button necesita un nombre accesible: «Volver», «Editar viaje», «Borrar búsqueda». El icono suelto no se anuncia.
- Área táctil mínima de 48 dp aunque el icono mida 24 dp.
- El estado seleccionado de un toggle no puede depender solo del color: cambia también el relleno o el peso del icono.
- El morph a 20 dp no es el indicador de foco; el foco necesita su propio anillo.

### Uso típico

```kotlin
MOTIconButton(
    onClick = { onFilterMenuExpandedChange(true) },
    modifier = Modifier.semantics { contentDescription = "Filtros y ordenación" },
) {
    Icon(
        imageVector = MOTIcons.Tune,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
```

## Text field

- Estado: ready · P0
- Para qué: Un campo de texto recoge lo que la persona escribe en un formulario: nombre del viaje, importes, títulos de documentos y notas. En pantallas nuevas usamos OutlinedTextField de Material 3, con el tema de la app y sin componente propio de MyOwnTrip.
- Composable: `OutlinedTextField` — `androidx.compose.material3.OutlinedTextField`
- Fuente: `androidx.compose.material3.OutlinedTextField`

### Variantes

- **Outlined · Enabled** → `OutlinedTextField`: Estado por defecto.
- **Outlined · Focused** → `OutlinedTextField`: Borde y label en primary; state layer en runtime.
- **Outlined · Error** → `OutlinedTextField`: Borde + supporting text + icono. Nunca solo color.
- **Outlined · Disabled** → `OutlinedTextField`: 38 % contenido / 12 % contenedor.

### Cuándo usarlo

- Hay que escribir un dato (nombre, título, notas, importe).
- El valor se valida en el propio campo, no en un diálogo.
- Hace falta un teclado adecuado (texto, número, email).

### Cuándo NO usarlo

- Elegir una fecha o una hora → Date & time pickers
- Elegir una categoría de un conjunto pequeño → Filter chip

### Elegir variante

- Formulario de producto (viajes, Wallet, gastos) → `OutlinedTextField` — Única variante en flujos nuevos.
- El dato no valida → `isError + supportingText + icono` — Nunca solo el color del borde.

### Prohibido en este componente

- No uses Fraunces en label ni en el input.
- No inventes tokens por estado (hover, pressed).
- No simules campos con rectángulos en Figma.
- No TextField Filled en flujos nuevos sin gap explícito.

### Accesibilidad

- Label asociado: TalkBack lee label + valor + error.
- Supporting text de error en liveRegion tras submit.
- ImeAction Next / Done coherente con el formulario.

### Uso típico

```kotlin
OutlinedTextField(
    value = state.name,
    onValueChange = viewModel::onNameChange,
    label = { Text("Nombre del viaje") },
    isError = state.nameError != null,
    supportingText = state.nameError?.let { { Text(it) } },
    modifier = Modifier.fillMaxWidth(),
)
```

## Date & time pickers

- Estado: ready · P0
- Para qué: La entrada de fechas y horas no usa el DatePicker de catálogo M3 tal cual. El producto tiene composables Mot* en ui/components/date/: campos Outlined y modales adaptados. El rango de viaje admite teclado dd/MM/yyyy y un calendario de un mes; Wallet usa fecha única y hora 24 h.
- Composable: `MotDateRangeField` — `com.myowntrip.app.ui.components.date.MotDateRangeField`
- Fuente: `app/src/main/java/com/myowntrip/app/ui/components/date`

### Variantes

- **MotDateRangeField** → `MotDateRangeField`: Inicio y fin de viaje. Teclado + calendario.
- **MotDateRangePickerDialog** → `MotDateRangePickerDialog`: Un mes (Days · Year · Month).
- **MotDateField** → `MotDateField`: Fecha única en Wallet.
- **MotDatePickerDialog** → `MotDatePickerDialog`: Día único, sin mode toggle.
- **MotTimeField** → `MotTimeField`: Hora 24 h.
- **MotTimePickerDialog** → `MotTimePickerDialog`: Teclado horario; reloj opcional.

### Cuándo usarlo

- Hay que recoger inicio y fin de un viaje.
- Hay que recoger la fecha o la hora de un documento o de un bloque del plan.

### Cuándo NO usarlo

- Texto libre que no es una fecha → Text field

### Elegir variante

- Crear o editar viaje → `MotDateRangeField` — Dos OutlinedTextField + icono calendario → MotDateRangePickerDialog (un mes, no lista vertical).
- Fecha de un documento (Wallet) → `MotDateField` — Campo read-only + MotDatePickerDialog.
- Hora (Wallet, plan) → `MotTimeField` — TimeInput 24 h por defecto; reloj M3 opcional.

### Prohibido en este componente

- No uses el DateRangePicker de lista vertical.
- No dibujes campos con frames: son Text field Outlined + icon button.
- No promover el Keyboard picker de archivo como SoT.

### Accesibilidad

- Campos read-only: contentDescription con label + valor.
- Trailing: «Abrir calendario» / «Abrir selector de hora».
- Errores de parseo en supporting text al perder foco.
- Aceptar deshabilitado hasta rango completo.

### Uso típico

```kotlin
MotDateRangeField(
    startDate = state.start,
    endDate = state.end,
    onRangeChange = viewModel::onDatesChange,
    modifier = Modifier.fillMaxWidth(),
)
```

## Filter chip

- Estado: ready · P0
- Para qué: Un filter chip elige una opción dentro de un conjunto pequeño y la deja activa. En MyOwnTrip vive en filas horizontales con scroll —Activos / Archivados / Todos en Wallet— y en formularios con pocas categorías. Es el FilterChip de Material 3, sin componente propio de MyOwnTrip.
- Composable: `FilterChip` — `androidx.compose.material3.FilterChip`
- Fuente: `app/src/main/java/com/myowntrip/app/ui/features/wallet/WalletScreen.kt`

### Variantes

- **Outlined · Unselected** → `FilterChip`: Opciones no activas en la fila (p. ej. «Archivados»).
- **Outlined · Selected** → `FilterChip`: Filtro activo. La selección se ve en el relleno; la app no añade check.

### Cuándo usarlo

- Hay un conjunto cerrado de categorías y la persona cambia de una a otra con un toque.
- El valor se queda visible en la misma pantalla (no dispara una acción que cierra un flujo).
- Las etiquetas caben en una fila: sustantivos cortos, en español, 20 caracteres o menos.

### Cuándo NO usarlo

- La acción confirma, guarda o cierra un flujo → Button
- Filtro de viajes en Home → Menu — el patrón acordado no es chip
- Una sola etiqueta de metadato (tipo, fecha, lugar) → Eyebrow label
- Mensaje breve que no pide una decisión → Snackbar

### Elegir variante

- La opción no está activa → `FilterChip selected = false` — Outline sobre el fondo; label onSurface.
- La opción está activa → `FilterChip selected = true` — Relleno secondaryContainer, sin check: la app no pasa icono inicial. En Wallet solo hay una seleccionada por eje.

### Prohibido en este componente

- No sustituyas acciones principales por chips: avanzar o retroceder va en botones.
- No muestres un chip suelto.
- No mezcles selección única y múltiple entre conjuntos de la misma pantalla.
- No eleves los chips sobre la página ni uses la elevación para el pulsado.
- No reintroduzcas Elevated.
- No uses esquinas Extra-small.
- No dibujes chips con frames sueltos en el design-file.
- No sustituyas el menú de filtro de Home por FilterChip.

### Accesibilidad

- Cada chip es un toggle: anunciar selected / unselected (aria-pressed en este HTML; estado de FilterChip en Compose).
- La fila puede hacer scroll; el mismo filtro tiene que ser alcanzable sin gesto horizontal (p. ej. Menu en Home).
- Área táctil ≥ 48 dp de alto vía padding M3, aunque el contenedor visible mida 32 dp.

### Uso típico

```kotlin
Row(
    modifier = Modifier.horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
) {
    FilterChip(
        selected = filterPhase == WalletDocumentFilterPhase.Active,
        onClick = { onFilterPhaseChange(WalletDocumentFilterPhase.Active) },
        label = { Text("Activos") },
    )
}
```

## Basic dialog

- Estado: ready · P0
- Para qué: Un diálogo básico interrumpe la tarea para pedir confirmación o advertir de una pérdida de datos. En MyOwnTrip cubre guardar en Wallet, eliminar una entrada, descartar cambios y salir de un formulario sin guardar. El runtime es AlertDialog de Material 3 con MOTTextButton en las acciones.
- Composable: `AlertDialog` — `androidx.compose.material3.AlertDialog`
- Fuente: `app/src/main/java/com/myowntrip/app/ui/features/wallet/WalletDialogs.kt`

### Variantes

- **Sin icono** → `AlertDialog`: Confirmación neutra. Ejemplo: Confirmar guardado (Cancelar / Guardar).
- **Con icono** → `AlertDialog`: Riesgo. Ejemplo: ¿Eliminar documento? con MOTIcons.Error y Eliminar en error.

### Cuándo usarlo

- Hay que confirmar una acción irreversible o que pierde datos.
- La persona tiene que decidir ahora: no puede seguir con la pantalla de detrás hasta responder.
- Bastan un titular, un texto de apoyo y como máximo dos acciones.

### Cuándo NO usarlo

- Aviso de prioridad baja o media que no bloquea → Snackbar
- La persona tiene que rellenar campos, no solo confirmar → Quédate en la pantalla; no metas el formulario en el diálogo
- Tres o más acciones, o «Más información» → Saca al usuario del diálogo y deja la tarea resuelta en la pantalla

### Elegir variante

- Confirmación neutra (guardar, continuar) → `AlertDialog sin icono` — WalletConfirmDialog: Cancelar / Guardar. El cuerpo puede llevar un Switch de «Añadir al plan» porque Compose lo tiene; no es un eje del set.
- Riesgo o pérdida (eliminar, descartar) → `AlertDialog + icono error` — Confirm destructivo en color error. WalletDeleteEntryDialog, WalletDiscardDialog, DeleteTripDialog.

### Prohibido en este componente

- No uses un diálogo para prioridad baja o media: usa un snackbar.
- No empieces el titular con disculpas, alarma ni «¿Estás seguro?».
- No coloques el descarte a la derecha de la confirmación.
- No añadas una tercera acción como «Más información».
- No mezclar el radio de cards (Medium) en el diálogo.
- No bloquear el dismiss sin copy claro (Seguir editando / Cancelar).
- No documentar el diálogo con node-ids de caps: la ficha vive aquí.

### Accesibilidad

- Foco atrapado; Escape / Back cierran cuando aplique.
- Headline se lee antes que el supporting text (alertdialog + labelledby / describedby).
- El icono es decorativo: contentDescription = null si el título ya nombra el riesgo.
- El botón destructivo no se entiende solo por el color: el texto de apoyo lleva la consecuencia.

### Uso típico

```kotlin
AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
        Icon(MOTIcons.Error, contentDescription = null,
            tint = MaterialTheme.colorScheme.error)
    },
    title = { Text("¿Eliminar documento?") },
    text = { Text("Se borrará «$entryTitle»…") },
    confirmButton = {
        MOTTextButton(onClick = onConfirmDelete) {
            Text("Eliminar", color = MaterialTheme.colorScheme.error)
        }
    },
    dismissButton = {
        MOTTextButton(onClick = onDismiss) { Text("Cancelar") }
    },
)
```

## Snackbar

- Estado: ready · P0
- Para qué: Una snackbar confirma que una acción se completó o ofrece deshacerla, sin sacar a la persona de la pantalla. Aparece en Day Hub, Diario, Gastos y Documentos. En Compose vive en el SnackbarHost del Scaffold, con el Snackbar de Material 3.
- Composable: `SnackbarHost / Snackbar` — `androidx.compose.material3.SnackbarHost`
- Fuente: `androidx.compose.material3.SnackbarHost`

### Variantes

- **Solo texto** → `Snackbar`: Ack sin acción.
- **Texto + acción** → `Snackbar`: Deshacer o reintentar.
- **Two lines** → `Snackbar`: Cuando el mensaje ocupa dos líneas.

### Cuándo usarlo

- Hay que acusar un guardado, un archivo o un cambio menor.
- Se puede deshacer en el momento, con un solo verbo (Deshacer, Ver).
- El mensaje no bloquea: la persona sigue usando la pantalla de detrás.

### Cuándo NO usarlo

- Hay que confirmar una pérdida de datos o una eliminación → Basic dialog
- Un campo del formulario no valida → Text field con isError + supporting text + icono

### Elegir variante

- Ack simple (Gasto guardado) → `Snackbar sin acción` — Desaparece sola a los 4–10 s.
- Se puede revertir (documento archivado) → `Snackbar + acción Deshacer` — Permanece hasta actuar o descartar.
- El copy no cabe en una línea → `Two lines` — No recortes a One line.

### Prohibido en este componente

- No lo uses para información crítica: ahí va un diálogo.
- No añadas iconos ni botones Filled.
- No apiles snackbars ni los pongas delante de la navegación.
- No sustituir confirmaciones H7 Wallet.
- No tokens snackbar/hover.

### Accesibilidad

- SnackbarHostState anuncia el mensaje; la acción es focusable.
- No dependas solo del esquema inverse para el significado.
- Si hay gesto de dismiss, tiene que haber tap equivalente.

### Uso típico

```kotlin
val snackbarHostState = remember { SnackbarHostState() }
Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
) { /* … */ }
scope.launch {
    snackbarHostState.showSnackbar("Gasto guardado", actionLabel = "Deshacer")
}
```

## Stacked card

- Estado: ready · P1
- Para qué: Una stacked card agrupa un tema en vertical: titular, subtítulo y, si hace falta, media o un dismiss. Outlined en listas y empty states; Elevated cuando hay que destacarla. Filled está cortado. El runtime es MotStackedCard; Home vacío usa HomeEmptyStackedCard.
- Composable: `MotStackedCard` — `com.myowntrip.app.ui.components.MotStackedCard`
- Fuente: `app/src/main/java/com/myowntrip/app/ui/components/StackedCard.kt`

### Variantes

- **Outlined · Media & text** → `MotStackedCard`: Home vacío y cards editoriales con imagen.
- **Outlined · Text only** → `MotStackedCard`: Mensajes inline (filtro vacío).
- **Elevated · Text only** → `MotStackedCard`: Avisos destacados, dismiss opcional.

### Cuándo usarlo

- Hay un bloque editorial (empty state, aviso, pieza de feed) que no es una fila de lista.
- El contenido es un solo tema: header + subhead, con o sin imagen.

### Cuándo NO usarlo

- Portada del viaje destacado en Home → TripHeroCard
- Documento del Wallet en una lista → List item

### Elegir variante

- Empty state con imagen (Home sin viajes) → `MotStackedCard Outlined · Media & text` — HomeEmptyStackedCard: imagen bundled, sin red.
- Mensaje inline en una lista (filtro vacío) → `MotStackedCard Outlined · Text only`
- Aviso destacable y descartable → `MotStackedCard Elevated · Text only` — onDismiss mapea a Show secondary action.

### Prohibido en este componente

- No fuerces contenido en cards cuando un titular y un hueco bastan.
- No uses Stacked card para la portada de Home: eso es TripHeroCard.
- No Filled.
- No OutlinedCard ad hoc si encaja este layout.

### Accesibilidad

- Dismiss con contentDescription.
- Imagen informativa con alt; si es decorativa, contentDescription null y el header lo dice todo.

### Uso típico

```kotlin
MotStackedCard(
    style = MotCardStyle.Outlined,
    headerText = "Sin viajes todavía",
    subheadText = "Planea tu primera aventura",
    media = { /* imageRes home_empty_map */ },
)
```

## TripHeroCard

- Estado: ready · P1
- Para qué: La TripHeroCard es la portada del viaje destacado en Home: imagen a sangrado, scrim, eyebrow de fase, countdown opcional, título, meta y un único CTA tonal «Ver detalles» debajo. No es una variante de Stacked card.
- Composable: `TripHeroCard` — `com.myowntrip.app.ui.components.TripHeroCard`
- Fuente: `app/src/main/java/com/myowntrip/app/ui/components/TripHeroCard.kt`

### Variantes

- **Elevated** → `TripHeroCard`: Default en Home.
- **Outlined** → `TripHeroCard`: Misma anatomía con marco.

### Cuándo usarlo

- Home tiene un viaje que merece la portada (el destacado).
- Hace falta foto, fase y un solo paso: ver el detalle.

### Cuándo NO usarlo

- Empty state sin viajes → Stacked card (HomeEmptyStackedCard)
- Resto de viajes en lista → Swipeable list row (Horizontal card)

### Elegir variante

- Viaje destacado en Home → `TripHeroCard Elevated` — Default.
- Misma anatomía con marco → `TripHeroCard Outlined` — Poco uso en Compose hoy.

### Prohibido en este componente

- No Stacked · Media only para este patrón.
- No chip en la portada.
- No hacer clickable la foto ni el eyebrow.

### Accesibilidad

- Único control: Ver detalles (48 dp).
- Fase y countdown van en el contentDescription del bloque o del CTA.
- Imagen decorativa: sin alt aparte.

### Uso típico

```kotlin
TripHeroCard(
    trip = featured,
    today = today,
    onClick = { onTripClick(featured.id) },
)
```

## Eyebrow label

- Estado: ready · P1
- Para qué: Un eyebrow label marca fase o contexto («Próximo viaje», «En destino», «Recuerdo») sin ser pulsable. Sustituye al Assist chip cuando el fondo es una foto o un scrim y el outline no garantiza contraste. Los chips siguen siendo filtros y acciones.
- Composable: `EyebrowLabel` — `com.myowntrip.app.ui.components.EyebrowLabel`
- Fuente: `app/src/main/java/com/myowntrip/app/ui/components/EyebrowLabel.kt`

### Variantes

- **Tertiary · Medium** → `EyebrowLabel`: Default en TripHeroCard.
- **Tertiary · Small** → `EyebrowLabel`: Overlays densos.
- **Surface · Medium** → `EyebrowLabel`: Cards sin foto.
- **Secondary · Medium** → `EyebrowLabel`: Acento suave en superficie clara.

### Cuándo usarlo

- Hay que nombrar la fase de un viaje o una categoría editorial.
- El fondo es variable (portada, scrim) o una superficie plana de card.
- El texto se anuncia en el bloque padre: el label no es un control.

### Cuándo NO usarlo

- Filtrar o seleccionar una opción → Filter chip
- Ejecutar una acción → Button

### Elegir variante

- Sobre foto o scrim (TripHeroCard) → `EyebrowLabelColor.Tertiary` — tertiaryFixedDim + onTertiaryContainer. Size Medium por defecto.
- Sobre surface, sin foto → `EyebrowLabelColor.Surface o Secondary` — Small si el espacio es corto.

### Prohibido en este componente

- No sustituir Filter chip.
- No hacerlo clickable.
- No Assist chip Elevated (CUT).

### Accesibilidad

- Decorativo: semantics hideFromAccessibility si el padre incluye la fase.
- Nunca role Button.
- Contraste ≥ 4.5:1 label sobre contenedor en los tres colores.

### Uso típico

```kotlin
EyebrowLabel(
    text = "Próximo viaje",
    color = EyebrowLabelColor.Tertiary,
)
```

## Swipeable list row

- Estado: in-review · P1
- Para qué: Una fila swipeable guarda las acciones laterales tipo Gmail: deslizar para archivar o eliminar, y el mismo par en un menú ⋮ por tap. El patrón es Compose compartido: SwipeableTripListRow en Home («Más viajes») y SwipeableWalletDocumentRow en Wallet. No envuelve el hero ni los carruseles.
- Composable: `SwipeableTripListRow` — `com.myowntrip.app.ui.components.home.SwipeableTripListRow`
- Fuente: `app/src/main/java/com/myowntrip/app/ui/components/home/SwipeableTripListRow.kt`

### Variantes

- **Lista activa** → `SwipeableWalletDocumentRow`: Archivar / Eliminar.
- **Lista archivados** → `SwipeableWalletDocumentRow`: Restaurar / Eliminar.

### Cuándo usarlo

- La colección es una lista vertical de viajes o documentos.
- Archivar y eliminar son acciones frecuentes, no el CTA de la pantalla.

### Cuándo NO usarlo

- Viaje destacado de Home → TripHeroCard
- Carrusel de próximos en Wallet → La card del carrusel, sin swipe

### Elegir variante

- Lista activa → `showArchivedActions = false` — Derecha Archivar · izquierda abre el diálogo de eliminar.
- Lista de archivados → `showArchivedActions = true` — Derecha Restaurar · izquierda eliminar.

### Prohibido en este componente

- No envolver TripHeroCard ni el carrusel Wallet «Próximos».
- No colores primitivos en los fondos.

### Accesibilidad

- customActions de TalkBack: Archivar/Restaurar y Eliminar.
- Menú ⋮: «Acciones del viaje» / «Acciones del documento».
- Eliminar: icono + texto error, nunca solo color.

### Uso típico

```kotlin
SwipeableTripListRow(
    trip = trip,
    today = today,
    showArchivedActions = filterPhase == Archived,
    onClick = { onTripClick(trip.id) },
    onArchive = { onArchiveTrip(trip.id) },
    onUnarchive = { onUnarchiveTrip(trip.id) },
    onDeleteRequest = { requestDelete(trip.id) },
)
```

## List item

- Estado: ready · P1
- Para qué: Una fila de lista muestra un elemento discreto de una colección vertical: un documento del Wallet con su tipo, título, fecha y estado. La fila entera es el área pulsable y lleva a su detalle.
- Composable: `WalletDocumentRow` — `com.myowntrip.app.ui.features.wallet.WalletDocumentRow`
- Fuente: `app/src/main/java/com/myowntrip/app/ui/features/wallet/WalletScreen.kt`

### Variantes

- **Leading=Icon** → `WalletDocumentRow`: Fila de documento con icono por tipo.
- **Show drift = true** → `WalletPlanDriftLeadingIcon`: Icono con punto de aviso cuando el plan no coincide con el documento.
- **List Item - Swipe** → `SwipeableWalletDocumentRow`: Fila con acciones de archivar y eliminar por swipe.

### Cuándo usarlo

- Hay una colección de elementos del mismo tipo que se recorre en vertical (documentos del Wallet).
- Cada elemento tiene un titular y, como mucho, una o dos líneas de apoyo.
- Se necesita un icono que ayude a escanear el tipo de elemento (vuelo, hotel, coche, entrada).

### Cuándo NO usarlo

- Elemento con portada o foto grande (viajes en Home) → Horizontal card
- Destacar un solo elemento con imagen y CTA → TripHeroCard
- Elegir una opción de un conjunto pequeño → Filter chip

### Elegir variante

- Fila de documento sin acciones laterales → `WalletDocumentRow` — ListItem de M3 con container surface.
- La fila se archiva o elimina con swipe → `SwipeableWalletDocumentRow` — Envuelve WalletDocumentRow; ver Swipeable list row.
- El plan ha cambiado respecto al documento → `WalletDocumentRow con planPlacement` — Borde izquierdo en warning y punto de drift en el icono.

### Prohibido en este componente

- No varíes la posición de los elementos dentro de una lista.
- Evita colocar visuales en el centro de la fila: dificulta el escaneo.
- No pintar el punto de drift dentro del glifo de 24 dp: va en la esquina del círculo.
- No poner el color warning como único indicador de drift: va también el texto de apoyo.
- No crear filas con Card y Text a mano que repliquen esta fila.

### Accesibilidad

- contentDescription de la fila: «tipo, título, fecha, drift, disponibilidad offline». TalkBack lo lee de una vez.
- El punto de drift y el borde no son el único aviso: el texto de apoyo incluye la frase de drift.
- Los iconos internos son decorativos (contentDescription = null); el significado va en la descripción de la fila.
- La fila entera es el área táctil; el ListItem de M3 garantiza una altura mínima superior a 48 dp.

### Uso típico

```kotlin
WalletDocumentRow(
    entry = entry,
    onClick = { onEntryClick(entry.id) },
    planPlacement = placement,
    trailingActions = { MOTIconButton(onClick = onMenu) { Icon(MOTIcons.MoreVert, contentDescription = "Acciones del documento") } },
)
```

## Tabs

- Estado: ready · P1
- Para qué: Las pestañas reparten el contenido de una pantalla en vistas del mismo nivel y permiten pasar de una a otra sin salir de ella. En MyOwnTrip ordenan las secciones de un viaje (Wallet, Plan, Diario, Gastos, Sitios) y las dos caras de un día (Plan y Diario). Cada pestaña es MOTTab, sobre la fila de pestañas de Material 3.
- Composable: `MOTTab` — `com.myowntrip.app.ui.theme.MOTTab`
- Fuente: `designsystem/src/commonMain/kotlin/com/myowntrip/app/ui/theme/MOTTab.kt`

### Variantes

- **Fila fija** → `TabRow { MOTTab(…) }`: Pocas pestañas que caben a lo ancho.
- **Fila con desplazamiento** → `ScrollableTabRow(edgePadding = MOTSpacing.none) { MOTTab(…) }`: Cinco pestañas o nombres largos.

### Cuándo usarlo

- El contenido se divide en vistas hermanas que se alternan o se comparan.
- Todas las vistas comparten el mismo contexto (un viaje, un día) y la misma barra superior.
- Los nombres caben en una palabra.

### Cuándo NO usarlo

- Filtrar una lista sin cambiar de vista → Filter chip
- Ir a una pantalla interior (un detalle, un formulario) → Navegar a esa pantalla; su App bar trae el botón de volver

### Elegir variante

- Dos o tres pestañas que caben a lo ancho (Plan · Diario) → `TabRow + MOTTab` — Las pestañas se reparten el ancho a partes iguales.
- Cuatro o más pestañas, o nombres que no caben (secciones del viaje) → `ScrollableTabRow + MOTTab` — La fila se desplaza en horizontal; edgePadding = 0, la primera pestaña arranca pegada al borde.

### Prohibido en este componente

- No uses pestañas para una secuencia de pasos.
- No anides una fila de pestañas dentro de otra.
- No pasar el texto por el parámetro text de Tab de Material 3: añade su propio espaciado interno.

### Accesibilidad

- Cada pestaña se anuncia como pestaña, con su estado de seleccionada.
- La etiqueta es el nombre accesible: no hace falta contentDescription.
- Toda la pestaña es área táctil, con 48 dp de alto.

### Uso típico

```kotlin
var tabIndex by rememberSaveable { mutableIntStateOf(0) }
TabRow(selectedTabIndex = tabIndex) {
    listOf("Plan", "Diario").forEachIndexed { index, title ->
        MOTTab(
            selected = tabIndex == index,
            onClick = { tabIndex = index },
            label = title,
        )
    }
}
```

## Divider

- Estado: ready · P2
- Para qué: Un divisor es una línea fina que separa grupos de contenido. En MyOwnTrip separa las filas de una lista (Wallet, revisión de un viaje) y los bloques de un formulario (por ejemplo, la zona de peligro de Editar viaje). Es MOTHorizontalDivider: el divisor horizontal de Material 3 con el color outlineVariant y una opción de sangría.
- Composable: `MOTHorizontalDivider` — `com.myowntrip.app.ui.theme.MOTHorizontalDivider`
- Fuente: `designsystem/src/commonMain/kotlin/com/myowntrip/app/ui/theme/MOTDivider.kt`

### Variantes

- **A todo el ancho** → `MOTHorizontalDivider()`: Bloques de formulario y secciones.
- **Con sangría** → `MOTHorizontalDivider(inset = true)`: Filas de una lista.

### Cuándo usarlo

- Hay que separar filas de una lista que no tienen contenedor propio.
- Un formulario o una pantalla cambia de bloque.

### Cuándo NO usarlo

- Cada elemento ya va en una card → La card ya separa: Stacked card
- Basta con aire entre bloques → El espaciado del sistema

### Elegir variante

- Separar filas de una lista → `MOTHorizontalDivider(inset = true)` — 16 dp de sangría a cada lado, alineada con el texto de las filas.
- Separar bloques de un formulario o secciones de una pantalla → `MOTHorizontalDivider()` — Línea a todo el ancho disponible.

### Prohibido en este componente

- No los uses como decoración ni en exceso: el espacio en blanco también separa.

### Accesibilidad

- Es decorativo: los lectores de pantalla lo ignoran.
- No transmite información por sí solo; los grupos tienen que entenderse sin él.

### Uso típico

```kotlin
LazyColumn {
    items(entries) { entry ->
        WalletDocumentRow(entry)
        MOTHorizontalDivider(inset = true)
    }
}
```

## Assist chip

- Estado: ready · P2
- Para qué: Un assist chip es un chip con una acción ligada al contenido que acompaña. En MyOwnTrip hay un solo uso: en el detalle de un documento de Wallet avisa de que su sitio en el plan ha cambiado («Actualizada en el plan · Día 2») y, al tocarlo, abre ese día. Es el AssistChip de Material 3 con el color de aviso (warning) del sistema.
- Composable: `PlanPlacementSourceChip` — `com.myowntrip.app.ui.features.plan.PlanPlacementSourceChip`
- Fuente: `app/src/main/java/com/myowntrip/app/ui/features/plan/PlanPlacementSourceChip.kt`

### Variantes

- **Aviso** → `PlanPlacementSourceChip(label, onClick)`: Con un día del plan al que ir.
- **Deshabilitado** → `PlanPlacementSourceChip(label)`: Sin destino.

### Cuándo usarlo

- Hay que señalar un cambio relacionado con el contenido y ofrecer ir a verlo.
- La acción es secundaria: la pantalla se entiende sin tocar el chip.

### Cuándo NO usarlo

- Elegir una categoría o filtrar → Filter chip
- La acción completa o confirma algo → Button
- Solo hay que etiquetar, sin acción → Eyebrow label

### Elegir variante

- El documento cambió de sitio en el plan y hay un día al que ir → `PlanPlacementSourceChip(label, onClick)` — Estilo de aviso: relleno y borde del color warning.
- No hay día al que ir → `PlanPlacementSourceChip(label) sin onClick` — Se ve deshabilitado, en gris y sin borde.

### Prohibido en este componente

- No uses un chip para la acción principal de la pantalla.

### Accesibilidad

- La etiqueta es el nombre accesible y dice qué cambió.
- Deshabilitado, se anuncia como no disponible.
- La altura visible es de 32 dp, pero el área táctil llega a 48 dp.

### Uso típico

```kotlin
planPlacement?.let { placement ->
    PlanPlacementSourceChip(
        label = placement.detailChipLabel,
        onClick = { onOpenPlanDay(placement.tripId, placement.dayId) },
    )
}
```

## FAB

- Estado: ready · P1
- Para qué: El botón de acción flotante (FAB) lleva la acción principal de una pantalla y flota sobre el contenido, abajo a la derecha. En MyOwnTrip añade contenido en las pestañas del viaje (documento, actividad, gasto, sitio) y en el día, y dispara la cámara. Es el FloatingActionButton de Material 3 sin personalizar: ni forma ni colores propios. Cuando el FAB abre varias opciones se convierte en un menú (FAB menu), que es un componente aparte.
- Composable: `FloatingActionButton` — `androidx.compose.material3.FloatingActionButton`
- Fuente: `app/src/main/java/com/myowntrip/app/ui/features/trips/TripDetailScreen.kt`

### Variantes

- **FAB** → `FloatingActionButton`: Una acción principal.
- **FAB menu** → `FloatingActionButtonMenu + ToggleFloatingActionButton`: Varias acciones de añadir (Diario). El botón abierto es circular.
- **FAB pequeño** → `SmallFloatingActionButton`: Solo en las opciones del menú de Home.

### Cuándo usarlo

- La pantalla tiene una acción principal clara, casi siempre añadir.
- La acción tiene que estar a mano mientras se desplaza el contenido.

### Cuándo NO usarlo

- La acción cierra un flujo (guardar, confirmar) → Button
- Acción secundaria o de la barra superior → Icon button

### Elegir variante

- Una sola acción: añadir en la pestaña o pantalla actual → `FloatingActionButton + icono Añadir` — Sin texto: el nombre va en contentDescription («Añadir gasto»).
- Varias formas de añadir en la misma pantalla (Diario) → `FAB menu: FloatingActionButtonMenu + ToggleFloatingActionButton` — Al abrir el menú, el botón pasa de esquina redondeada a círculo y el icono de Añadir a Cerrar.
- Acciones rápidas de Home → `HomeSpeedDial` — Menú propio de la app sobre un FAB normal: no cambia de forma.

### Prohibido en este componente

- No lo uses para acciones menores ni destructivas.

### Accesibilidad

- No tiene texto visible: el nombre va en contentDescription y dice la acción concreta («Añadir gasto», no «Añadir»).
- 56 dp: supera el área táctil mínima de 48 dp.

### Uso típico

```kotlin
Scaffold(
    floatingActionButton = {
        FloatingActionButton(
            onClick = onAddExpense,
            modifier = Modifier.semantics { contentDescription = "Añadir gasto" },
        ) { Icon(MOTIcons.Add, contentDescription = null) }
    },
) { /* … */ }
```

## App bar

- Estado: ready · P1
- Para qué: La barra superior (top app bar) da título a la pantalla y reúne la navegación hacia atrás y sus acciones. En MyOwnTrip todas las pantallas interiores la usan con el mismo esquema: flecha de volver, título y, a veces, una acción (editar, borrar, guardar). Es el TopAppBar pequeño de Material 3, con MOTIconButton en los iconos.
- Composable: `TopAppBar` — `androidx.compose.material3.TopAppBar`
- Fuente: `app/src/main/java/com/myowntrip/app/ui/features/trips/EditTripScreen.kt`

### Variantes

- **Pequeña** → `TopAppBar`: Todas las pantallas interiores. No se usan las variantes centrada, mediana ni grande.

### Cuándo usarlo

- La pantalla es interior: se llega desde otra (un detalle, un formulario, un editor).
- La pantalla necesita un título y una forma de volver.

### Cuándo NO usarlo

- Cambiar entre vistas hermanas de la misma pantalla → Tabs, debajo de la barra
- La acción principal es añadir → FAB

### Elegir variante

- Pantalla interior (todas las de la app) → `TopAppBar + volver` — Título a la izquierda, junto a la flecha.
- La pantalla edita o borra lo que muestra → `+ una acción en actions` — Editar viaje, Eliminar documento, Borrar gasto o sitio, Guardar boceto, Abrir con otra app.

### Prohibido en este componente

- No llenes la barra de acciones.

### Accesibilidad

- La flecha de volver lleva contentDescription «Volver».
- Cada acción lleva su nombre: «Editar viaje», «Eliminar documento».
- Los iconos tienen un área táctil de 48 dp.

### Uso típico

```kotlin
TopAppBar(
    title = { Text("Editar viaje") },
    navigationIcon = {
        MOTIconButton(onClick = onBack) {
            Icon(MOTIcons.ArrowBack, contentDescription = "Volver")
        }
    },
)
```
