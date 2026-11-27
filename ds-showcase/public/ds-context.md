# MyOwnTrip · Design System — contexto para agentes

> Generado el 2026-11-27 desde `ds-showcase/src/data/`. **No editar a mano:** los cambios se pierden en el siguiente build.
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
- El botón necesita separarse de un fondo con patrón o de una imagen → `Button de M3 + elevation` — El sistema no tiene versión propia del botón Elevated y la app casi no lo usa.

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
- La etiqueta debe entenderse fuera de contexto: «Guardar gasto», no «Aceptar». El lector de pantalla la lee sola.
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
- **Round** → `MOTIconButton(type = Round)`: Ya redondeado a 20 dp en reposo. No es un círculo.
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
- Acción principal flotante de la pantalla → FAB

### Elegir variante

- Acción común en app bar, fila, campo o card (casi todos los usos) → `MOTIconButton Square, Small` — Sin contenedor visible: bajo énfasis.
- El botón debe verse ya redondeado, sin esperar a la interacción → `MOTIconButton Round` — Mismo radio de 20 dp, en reposo.
- Necesitas separarlo de una imagen o de un fondo variable → `MOTIconButton con colors Filled o Tonal` — No hay componentes aparte: el contenedor se pide con IconButtonDefaults.filledIconButtonColors() o filledTonalIconButtonColors(). MOTIconButton no ofrece el estilo Outlined.
- Representa un estado activable (favorito, marcador) → `Pendiente` — MOTIconButton todavía no tiene modo seleccionable.

### Prohibido en este componente

- No abuses del estilo Filled: úsalo con moderación en acciones de alto énfasis (descargar, eliminar).
- No uses un icono ambiguo cuya acción no se entienda sin texto: en ese caso usa un Button.
- No usar IconButton de M3 suelto: se pierde el morph de forma.
- No dejar un icono como único contenido del botón sin nombre accesible en ningún sitio.
- No usar un círculo: Round es un radio de 20 dp, no una forma completamente redonda.

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
- **Outlined · Focused** → `OutlinedTextField`: Borde y etiqueta en primary.
- **Outlined · Error** → `OutlinedTextField`: Borde, texto de apoyo e icono en color error. Nunca solo el color.
- **Outlined · Disabled** → `OutlinedTextField`: 38 % contenido / 12 % contenedor.

### Cuándo usarlo

- Hay que escribir un dato (nombre, título, notas, importe).
- El valor se valida en el propio campo, no en un diálogo.
- Hace falta un teclado adecuado (texto, número, email).

### Cuándo NO usarlo

- Elegir una fecha o una hora → Date & time pickers
- Elegir una categoría de un conjunto pequeño → Filter chip

### Elegir variante

- Formulario de la app (viajes, Wallet, gastos) → `OutlinedTextField` — Única variante en flujos nuevos.
- El dato no valida → `isError + supportingText + icono` — Nunca solo el color del borde.

### Prohibido en este componente

- No uses Fraunces en label ni en el input.
- No inventes tokens por estado (hover, pressed).
- No simules campos con rectángulos en Figma.
- No usar el campo Filled en pantallas nuevas sin un motivo documentado.

### Accesibilidad

- La etiqueta va unida al campo: el lector de pantalla lee etiqueta, valor y error.
- Al enviar el formulario, el texto de error se anuncia en cuanto aparece.
- La tecla de acción del teclado pasa al campo siguiente y, en el último, termina el formulario.

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

- Estado: in-review · P0
- Para qué: Los campos de fecha y hora recogen cuándo empieza y acaba un viaje, y la fecha y la hora de un documento o de una actividad del plan. No usan tal cual el selector de fechas de Material 3: el design system tiene sus propios campos y diálogos (MotDate y MotTime), construidos sobre los de Material 3. El rango del viaje se escribe con el teclado (dd/mm/aaaa) o se elige en un calendario de un mes; Wallet usa una fecha y una hora de 24 h.
- Composable: `MotDateRangeField` — `com.myowntrip.app.ui.components.date.MotDateRangeField`
- Fuente: `designsystem/src/commonMain/kotlin/com/myowntrip/app/ui/components/date/MotDateFormFields.kt`

### Variantes

- **MotDateRangeField** → `MotDateRangeField`: Inicio y fin del viaje, con el teclado o con el calendario.
- **MotDateRangePickerDialog** → `MotDateRangePickerDialog`: Calendario de un mes, con vistas de días, años y meses.
- **MotDateField** → `MotDateField`: Fecha única en Wallet.
- **MotDatePickerDialog** → `MotDatePickerDialog`: Un solo día, sin botón para cambiar al teclado.
- **MotTimeField** → `MotTimeField`: Hora en formato de 24 h.
- **MotTimePickerDialog** → `MotTimePickerDialog`: Hora escrita con el teclado; esfera de reloj opcional.
- **MotTimeTextField** → `MotTimeTextField`: Hora del plan, guardada como texto.

### Cuándo usarlo

- Hay que recoger inicio y fin de un viaje.
- Hay que recoger la fecha o la hora de un documento o de un bloque del plan.

### Cuándo NO usarlo

- Texto libre que no es una fecha → Text field

### Elegir variante

- Crear o editar viaje → `MotDateRangeField` — Dos campos de texto y un botón de calendario que abre MotDateRangePickerDialog: un mes cada vez, no una lista vertical de meses.
- Fecha de un documento (Wallet) → `MotDateField` — Campo de solo lectura que abre MotDatePickerDialog.
- Hora (Wallet, plan) → `MotTimeField` — Hora de 24 h escrita con el teclado; la esfera de reloj de Material 3 es opcional.
- Hora que la pantalla guarda como texto (plan del día) → `MotTimeTextField` — El mismo campo de hora, pero recibe y devuelve «19:30» en vez de una hora.

### Prohibido en este componente

- No uses el DateRangePicker de lista vertical.
- No dibujes campos con frames: son Text field Outlined + icon button.
- No tomar como referencia el selector por teclado archivado en Figma.

### Accesibilidad

- Los campos de solo lectura se anuncian con su etiqueta y su valor.
- El icono del final tiene nombre: «Abrir calendario», «Abrir selector de hora».
- Si la fecha escrita no es válida, el error aparece en el texto de apoyo al salir del campo.
- Aceptar está deshabilitado hasta que el rango tiene inicio y fin.

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

- La opción no está activa → `FilterChip selected = false` — Borde outline sobre el fondo; etiqueta onSurface.
- La opción está activa → `FilterChip selected = true` — Relleno secondaryContainer, sin check: la app no pasa icono inicial. En Wallet solo hay una seleccionada por eje.

### Prohibido en este componente

- No sustituyas acciones principales por chips: avanzar o retroceder va en botones.
- No muestres un chip suelto.
- No mezcles selección única y múltiple entre conjuntos de la misma pantalla.
- No eleves los chips sobre la página ni uses la elevación para el pulsado.
- No vuelvas a usar la variante Elevated.
- No bajes el radio a 4 dp (Extra-small).
- No dibujes chips a mano en los diseños de pantalla: usa el componente de la librería.
- No sustituyas el menú de filtro de Home por FilterChip.

### Accesibilidad

- Cada chip se activa y se desactiva: el lector de pantalla anuncia si está seleccionado o no. FilterChip ya lo hace.
- La fila puede hacer scroll; el mismo filtro tiene que ser alcanzable sin gesto horizontal (p. ej. Menu en Home).
- Área táctil de al menos 48 dp de alto, que añade Material 3, aunque el contenedor visible mida 32 dp.

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

## Menu

- Estado: in-review · P1
- Para qué: Un menú de selección muestra, encima de la pantalla, grupos de opciones de las que se elige una. En MyOwnTrip lo abre el botón de filtros de la barra de búsqueda de Home, con dos grupos: «Mostrar» (qué viajes) y «Ordenar» (en qué orden). Elegir una opción la aplica y cierra el menú. Lo forman piezas del design system: MotMenuDropdown, MotMenuGroup, MotMenuSectionLabel y MotMenuItem.
- Composable: `MotMenuDropdown` — `com.myowntrip.app.ui.components.MotMenuDropdown`
- Fuente: `designsystem/src/commonMain/kotlin/com/myowntrip/app/ui/components/MotMenu.kt`

### Variantes

- **Desplegable** → `MotMenuDropdown`: Se abre desde un botón, encima de la pantalla.
- **Panel** → `MotMenuPanel`: Sin ventana propia, colocado a mano.

### Cuándo usarlo

- Hay varias opciones excluyentes y no hace falta tenerlas siempre a la vista.
- Las opciones se agrupan por lo que cambian (qué se muestra, en qué orden).

### Cuándo NO usarlo

- Acciones sobre un elemento (archivar, eliminar) → Menú de acciones de Material 3, como el ⋮ de la fila deslizable
- Pocas opciones que conviene ver siempre → Filter chip
- Elegir un valor dentro de un formulario → Campo desplegable (Text field con chevron)

### Elegir variante

- Se abre desde un botón → `MotMenuDropdown` — Anclado al botón; se cierra al tocar fuera.
- Hay que colocarlo a mano o enseñarlo quieto → `MotMenuPanel` — El mismo menú sin ventana propia.

### Prohibido en este componente

- No pongas un chevron en una opción que no abre otro nivel.
- No uses un botón de radio para marcar la opción activa: eso es un control de formulario.
- No mezclar en el mismo menú opciones de filtro y acciones sobre un elemento.

### Accesibilidad

- La opción seleccionada se anuncia con «Seleccionado».
- Los títulos de grupo se leen como texto y no se pueden pulsar.
- Cada opción tiene 44 dp de alto más el margen: el área táctil llega a 48 dp.

### Uso típico

```kotlin
MotMenuDropdown(expanded = open, onDismissRequest = { open = false }) {
    MotMenuGroup {
        MotMenuSectionLabel("Mostrar")
        MotMenuItem("Todos los viajes", selected = phase == All, onClick = { onPhase(All) })
        MotMenuItem("En curso", selected = phase == Current, onClick = { onPhase(Current) })
    }
}
```

## Search bar

- Estado: in-review · P1
- Para qué: La barra de búsqueda filtra una lista mientras se escribe y, con el botón del final, abre el menú para filtrar y ordenar esa lista. En MyOwnTrip está encima de la lista de viajes de Home: se escribe el nombre o el destino del viaje, y el botón de filtros abre el menú con «Mostrar» y «Ordenar». Es MotSearchBar, del design system.
- Composable: `MotSearchBar` — `com.myowntrip.app.ui.components.MotSearchBar`
- Fuente: `designsystem/src/commonMain/kotlin/com/myowntrip/app/ui/components/MotSearchBar.kt`

### Variantes

- **Vacía** → `MotSearchBar(query = "")`: Tarjeta con borde, lupa y placeholder.
- **Con texto** → `MotSearchBar(query = "Cracovia")`: Relleno surfaceContainerHigh y botón de borrar.

### Cuándo usarlo

- Una lista crece lo bastante como para que buscar sea más rápido que desplazarse.
- La misma lista se puede filtrar u ordenar, y esas opciones no tienen por qué estar siempre a la vista.

### Cuándo NO usarlo

- Escribir un dato de un formulario → Text field
- Pocas categorías que conviene ver siempre → Filter chip

### Elegir variante

- La barra está vacía → `Con borde y lupa al principio` — Placeholder «Buscar viajes».
- Hay texto escrito → `Relleno surfaceContainerHigh, sin lupa` — Aparece el botón «Borrar búsqueda» antes del de filtros.

### Prohibido en este componente

- No pongas los filtros como chips fijos debajo de la barra en Home: van en el menú.
- No añadir un botón de buscar: la lista ya responde al escribir.

### Accesibilidad

- El placeholder no sustituye a una etiqueta: la barra se anuncia como campo de texto con «Buscar viajes».
- Los dos botones tienen nombre: «Borrar búsqueda» y «Filtros y ordenación».
- Los botones tienen un área táctil de 48 dp.

### Uso típico

```kotlin
MotSearchBar(
    query = query,
    onQueryChange = onQueryChange,
    placeholder = "Buscar viajes",
    onFilterClick = { menuOpen = true },
    menu = {
        MotMenuDropdown(
            expanded = menuOpen,
            onDismissRequest = { menuOpen = false },
            modifier = Modifier.align(Alignment.TopEnd),
        ) { /* grupos y opciones */ }
    },
)
```

## Basic dialog

- Estado: ready · P0
- Para qué: Un diálogo básico interrumpe la tarea para pedir confirmación o advertir de una pérdida de datos. En MyOwnTrip cubre guardar en Wallet, eliminar una entrada, descartar cambios y salir de un formulario sin guardar. Es el AlertDialog de Material 3, con MOTTextButton en las acciones.
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

- Confirmación neutra (guardar, continuar) → `AlertDialog sin icono` — Confirmar guardado en Wallet: Cancelar / Guardar. El texto puede llevar un interruptor «Añadir al plan»; es contenido de esa pantalla, no una variante del diálogo.
- Riesgo o pérdida (eliminar, descartar) → `AlertDialog + icono error` — La acción que borra o descarta va en color error. Se usa al eliminar un documento o un viaje y al descartar cambios.

### Prohibido en este componente

- No uses un diálogo para prioridad baja o media: usa un snackbar.
- No empieces el titular con disculpas, alarma ni «¿Estás seguro?».
- No coloques el descarte a la derecha de la confirmación.
- No añadas una tercera acción como «Más información».
- No usar en el diálogo el radio de las cards (12 dp, Medium).
- No impedir cerrar el diálogo: la salida lleva un texto claro («Seguir editando», «Cancelar»).
- No poner más de dos acciones: si hacen falta más, la tarea va en una pantalla.

### Accesibilidad

- El foco no sale del diálogo mientras está abierto; Escape o Atrás lo cierran.
- El lector de pantalla lee el título antes que el texto de apoyo.
- El icono es decorativo: no se anuncia cuando el título ya nombra el riesgo.
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
- El texto no cabe en una línea → `Two lines` — No recortes a One line.

### Prohibido en este componente

- No lo uses para información crítica: ahí va un diálogo.
- No añadas iconos ni botones Filled.
- No apiles snackbars ni los pongas delante de la navegación.
- No usarlo en lugar de un diálogo de confirmación de Wallet.
- No crear colores propios para sus estados.

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
- Para qué: Una stacked card agrupa en un bloque vertical la información de un solo tema: un título, un subtítulo y, según el caso, un texto de apoyo o una imagen a todo el ancho. En MyOwnTrip aparece en Home cuando no hay viajes que mostrar: al empezar (con el mapa), cuando solo quedan viajes pasados y cuando una búsqueda no encuentra nada. La card no se pulsa; si hace falta una acción, va en un botón aparte. Tiene dos estilos: con borde (Outlined), que es el que usa la app, y con sombra (Elevated).
- Composable: `MotStackedCard` — `com.myowntrip.app.ui.components.MotStackedCard`
- Fuente: `designsystem/src/commonMain/kotlin/com/myowntrip/app/ui/components/StackedCard.kt`

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
- No usar la variante Filled.
- No construir a mano una card con borde si esta disposición sirve.

### Accesibilidad

- El título y el subtítulo se leen en orden. La card no se anuncia como botón porque no se pulsa.
- La imagen lleva descripción cuando aporta información (en Home vacío: «Mapa con chincheta de viaje»); si fuera decorativa, iría sin descripción.
- El botón de cerrar, si se usa, tiene nombre accesible («Cerrar» por defecto).

### Uso típico

```kotlin
MotStackedCard(
    style = MotCardStyle.Outlined,
    headerText = "Sin viajes todavía",
    subheadText = "Planea tu primera aventura",
    image = painterResource(R.drawable.home_empty_map),
    imageContentDescription = "Mapa con chincheta de viaje",
)
```

## TripHeroCard

- Estado: ready · P1
- Para qué: La TripHeroCard es la portada del viaje destacado en Home: foto a sangre con un degradado, la fase del viaje, la cuenta atrás si la hay, el título, las fechas y un único botón tonal «Ver detalles» debajo. No es una variante de Stacked card.
- Composable: `TripHeroCard` — `com.myowntrip.app.ui.components.TripHeroCard`
- Fuente: `designsystem/src/commonMain/kotlin/com/myowntrip/app/ui/components/TripHeroCard.kt`

### Variantes

- **Única** → `TripHeroCard`: Una por Home, para el viaje destacado.

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

- No usar en su lugar una Stacked card solo con imagen.
- No poner chips sobre la portada.
- No hacer pulsables por separado la foto ni la etiqueta superior.

### Accesibilidad

- Toda la card se anuncia desde el botón «Ver detalles»: su descripción junta la fase, la cuenta atrás, el nombre, las fechas y la acción, y su estado dice la fase del viaje.
- La portada, la etiqueta y los textos sobre la foto se ocultan a los lectores de pantalla para no leerse dos veces.
- Es el único elemento que se pulsa.
- El título y las fechas usan un color claro fijo (primaryFixed) sobre el degradado: contraste de 12,9:1 o más en los dos temas.

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
- Para qué: Un eyebrow label marca fase o contexto («Próximo viaje», «En destino», «Recuerdo») sin ser pulsable. Sustituye al Assist chip cuando el fondo es una foto o un degradado y el borde no garantiza el contraste. Los chips siguen siendo filtros y acciones.
- Composable: `EyebrowLabel` — `com.myowntrip.app.ui.components.EyebrowLabel`
- Fuente: `designsystem/src/commonMain/kotlin/com/myowntrip/app/ui/components/EyebrowLabel.kt`

### Variantes

- **Tertiary · Medium** → `EyebrowLabel`: Default en TripHeroCard.
- **Tertiary · Small** → `EyebrowLabel`: Overlays densos.
- **Surface · Medium** → `EyebrowLabel`: Cards sin foto. Existe en el código, pero ninguna pantalla lo usa.
- **Secondary · Medium** → `EyebrowLabel`: Acento suave en superficie clara. Existe en el código, pero ninguna pantalla lo usa.

### Cuándo usarlo

- Hay que nombrar la fase de un viaje o una categoría editorial.
- El fondo es variable (portada, degradado) o una superficie plana de card.
- El texto se anuncia en el bloque padre: el label no es un control.

### Cuándo NO usarlo

- Filtrar o seleccionar una opción → Filter chip
- Ejecutar una acción → Button

### Elegir variante

- Sobre foto o degradado (TripHeroCard) → `EyebrowLabelColor.Tertiary` — tertiaryFixedDim + onTertiaryContainer. Size Medium por defecto.
- Sobre surface, sin foto → `EyebrowLabelColor.Surface o Secondary` — Small si el espacio es corto.

### Prohibido en este componente

- No usarlo en lugar de un filter chip: no se puede pulsar.
- No hacerlo pulsable.
- No sustituirlo por un assist chip con sombra.

### Accesibilidad

- Los lectores de pantalla no lo leen: el componente se oculta a propósito (hideFromAccessibility) para que la misma información no se anuncie dos veces.
- Por eso quien lo usa tiene que repetir su texto en la descripción de lo que lo contiene. TripHeroCard lo hace: el botón «Ver detalles» se anuncia como «Próximo viaje. Sale en 12 días. Cracovia finde…».
- Pendiente: en el Diario (álbum y piezas de un evento) ningún contenedor repite el texto, así que el título y la hora del evento no se anuncian.
- No se pulsa: nunca lleva rol de botón.
- Contraste del texto sobre su fondo: Tertiary 10,07:1 en los dos temas; Surface 7,66:1 en claro y 8,49:1 en oscuro; Secondary 9,41:1 y 7,63:1.

### Uso típico

```kotlin
EyebrowLabel(
    text = "Próximo viaje",
    color = EyebrowLabelColor.Tertiary,
)
```

## Swipeable list row

- Estado: in-review · P1
- Para qué: Una fila deslizable añade a una fila de lista dos acciones rápidas: deslizar hacia la derecha para archivar (o restaurar, en la lista de archivados) y hacia la izquierda para eliminar. Las mismas acciones están siempre en el menú ⋮ de la fila, para quien no puede o no sabe deslizar. En MyOwnTrip se usa en los documentos de Wallet y en la lista de viajes de Home. Es MotSwipeableRow, del design system.
- Composable: `MotSwipeableRow` — `com.myowntrip.app.ui.components.MotSwipeableRow`
- Fuente: `designsystem/src/commonMain/kotlin/com/myowntrip/app/ui/components/MotSwipeableRow.kt`

### Variantes

- **Lista activa** → `MotSwipeableRow(archived = false, …)`: Archivar a la derecha, eliminar a la izquierda.
- **Lista de archivados** → `MotSwipeableRow(archived = true, …)`: Restaurar a la derecha, eliminar a la izquierda.

### Cuándo usarlo

- Archivar y eliminar son acciones frecuentes en esa lista.
- Cada fila es un elemento independiente.

### Cuándo NO usarlo

- La portada del viaje destacado o un carrusel → TripHeroCard, sin deslizamiento

### Elegir variante

- Lista activa → `archived = false` — Derecha Archivar · izquierda abre el diálogo de eliminar.
- Lista de archivados → `archived = true` — Derecha Restaurar · izquierda eliminar.

### Prohibido en este componente

- No hacer deslizables la TripHeroCard ni el carrusel «Próximos» de Wallet.
- No pintar los fondos con colores fijos: usan roles del tema.

### Accesibilidad

- Los lectores de pantalla ofrecen las dos acciones sobre la propia fila: «Archivar documento» (o «Restaurar documento») y «Eliminar documento»; en viajes, «… viaje».
- El botón ⋮ se anuncia como «Acciones del documento» o «Acciones del viaje».
- Eliminar no depende solo del color: lleva icono y texto.

### Uso típico

```kotlin
MotSwipeableRow(
    archived = showArchivedActions,
    itemNoun = "documento",
    onArchive = onArchive,
    onUnarchive = onUnarchive,
    onDeleteRequest = onDeleteRequest,
) { menuButton ->
    WalletDocumentRow(entry, onClick, trailingActions = { menuButton(Modifier) })
}
```

## List item

- Estado: in-review · P1
- Para qué: Una fila de lista presenta un elemento de una colección vertical para poder recorrerla de un vistazo y abrir uno. En MyOwnTrip es la fila de los documentos de Wallet: un icono según el tipo, el título y, debajo, el tipo y la fecha. Toda la fila se pulsa y lleva al detalle. Es MotListItem, del design system, sobre el ListItem de Material 3.
- Composable: `MotListItem` — `com.myowntrip.app.ui.components.MotListItem`
- Fuente: `designsystem/src/commonMain/kotlin/com/myowntrip/app/ui/components/MotListItem.kt`

### Variantes

- **Normal** → `MotListItem(title, supportingText, icon, …)`: Documento sin avisos.
- **Con aviso del plan** → `warningSuffix = " · Actualizada en el plan · Día 2"`: El documento cambió de sitio en el plan: borde, punto y aviso en color warning.
- **Guardado en el móvil** → `dimmed = true + indicador al final`: Disponible sin conexión y sin nada pendiente: la fila se atenúa.
- **Con menú ⋮** → `trailing = { … }`: Lo añade la fila deslizable.

### Cuándo usarlo

- Hay que listar elementos del mismo tipo para recorrerlos y abrir uno.
- Cada elemento cabe en un título y una línea de apoyo.

### Cuándo NO usarlo

- El elemento necesita imagen o portada → Horizontal card
- Hay que elegir opciones o filtrar la lista → Filter chip

### Elegir variante

- Lista de documentos → `MotListItem` — El icono indica el tipo de documento.
- Hay que poder archivar o eliminar desde la lista → `MotListItem dentro de MotSwipeableRow` — Ver Swipeable list row.

### Prohibido en este componente

- No varíes la posición de los elementos dentro de una lista.
- Evita colocar visuales en el centro de la fila: dificulta el escaneo.
- No pintar el punto de aviso encima del icono: va en la esquina del círculo.
- No usar el color warning como único indicador del aviso: el texto de apoyo también lo dice.
- No construir a mano filas que imiten esta.

### Accesibilidad

- La fila se anuncia de una vez, con una frase que junta el tipo, el título, la fecha, el aviso del plan y la disponibilidad: «Vuelo, IB 3254 · Madrid → Barcelona, 20 nov 2026 · 10:30, actualizada en el plan, día 2».
- El aviso no depende solo del color: además del borde y el punto, el texto de apoyo lo dice.
- Los iconos no se anuncian por separado: su significado ya está en la frase de la fila.
- Toda la fila es pulsable y mide más de 48 dp de alto.

### Uso típico

```kotlin
MotListItem(
    title = entry.title,
    supportingText = "Vuelo · 20 nov 2026 · 10:30",
    icon = MOTIcons.Flight,
    contentDescription = "Vuelo, IB 3254 · Madrid → Barcelona, 20 nov 2026 · 10:30",
    onClick = { onEntryClick(entry.id) },
    warningSuffix = planPlacement?.listDriftSuffix,
)
```

## Tabs

- Estado: in-review · P1
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

- Estado: in-review · P2
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

- Estado: in-review · P2
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

- Estado: in-review · P1
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

- Estado: in-review · P1
- Para qué: La barra superior da título a la pantalla y reúne la vuelta atrás y las acciones sobre lo que se está viendo. En MyOwnTrip todas las pantallas interiores la llevan, siempre con la flecha de volver y el título. Las acciones dependen de la pantalla: los formularios no tienen ninguna y los detalles tienen una o dos sobre el elemento que muestran. Es el TopAppBar pequeño de Material 3, con MOTIconButton en los iconos.
- Composable: `TopAppBar` — `androidx.compose.material3.TopAppBar`
- Fuente: `app/src/main/java/com/myowntrip/app/ui/features/trips/EditTripScreen.kt`

### Variantes

- **Pequeña** → `TopAppBar`: Todas las pantallas interiores. No se usan las variantes centrada, mediana ni grande.

### Cuándo usarlo

- La pantalla es interior: se llega desde otra (un detalle, un formulario, un editor, la cámara).
- La pantalla necesita un título y una forma de volver.

### Cuándo NO usarlo

- Cambiar entre vistas hermanas de la misma pantalla → Tabs, debajo de la barra
- La acción principal de la pantalla es añadir → FAB
- Guardar un formulario → Button al final del formulario

### Elegir variante

- Formulario o tarea: crear, editar, escanear, hacer una foto → `Volver + título, sin acciones` — Nuevo viaje, Editar viaje, Nueva nota, Añadir gasto, Escanear QR, Hacer foto. Guardar va en un botón dentro del formulario, no en la barra.
- Detalle de un elemento → `Volver + nombre del elemento + una acción` — Detalle del viaje: Editar viaje. Documento de Wallet: Eliminar documento. Sitio: Borrar sitio. Visor de archivo: Abrir con otra app.
- Detalle con dos acciones sobre el mismo elemento → `Volver + título + dos acciones` — Recuerdo del Diario: Editar recuerdo y Eliminar recuerdo. Es el máximo en la app.
- Editor que guarda lo que hay en pantalla → `Volver + título + Guardar` — Boceto: Guardar boceto.
- La pantalla tiene vistas hermanas → `Tabs debajo de la barra` — Detalle del viaje (Plan, Wallet, Gastos…) y Día del plan.

### Prohibido en este componente

- No llenes la barra de acciones: lo demás va en la pantalla o en un menú.
- No pongas en la barra la acción de guardar un formulario: va en un botón al final.

### Accesibilidad

- La flecha se anuncia como «Volver».
- Cada acción lleva el nombre completo de lo que hace: «Editar viaje», «Eliminar documento», «Abrir con otra app».
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
