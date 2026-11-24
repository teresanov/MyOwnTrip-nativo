# MyOwnTrip · Design System — contexto para agentes

> Generado el 2026-11-24 desde `ds-showcase/src/data/`. **No editar a mano:** los cambios se pierden en el siguiente build.
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

- **Standard · Square** → `MOTIconButton`: Único estilo con wrapper. App bars, filas, campos, cards.
- **Filled / Tonal / Outlined**: Publicados en Figma (×4 con standard). Sin wrapper específico en Compose.
- **Togglable (×4)**: Publicados en Figma. En Compose el estado activo se fuerza con forceActive en rememberMOTButtonShape.

### Cuándo usarlo

- La acción tiene un icono inequívoco (arrow_back, edit, delete, close, tune).
- El espacio es escaso: top app bar, fila de lista, dentro de un campo o de una card.
- Se usan pocos a la vez; más de tres en la misma barra es señal de que sobra alguno.

### Cuándo NO usarlo

- La acción necesita un verbo para entenderse («Guardar gasto») → Button
- Elegir un filtro que se queda activo → Filter chip
- Acción principal flotante de la pantalla → FAB (circular, sin morph)

### Elegir variante

- Acción común en app bar, fila o campo (todos los usos actuales) → `MOTIconButton` — Estilo standard: sin contenedor, bajo énfasis.
- Necesitas separar el botón de una imagen o de un fondo variable → `Icon button Filled / Tonal / Outlined (set de Figma)` — Hoy sin wrapper propio en Compose: se pasa por el parámetro colors.
- Representa un estado activable (favorito, marcador) → `Icon button togglable (set de Figma)` — Icono outline sin seleccionar y relleno al seleccionar.

### Prohibido en este componente

- No abuses del estilo Filled: úsalo con moderación en acciones de alto énfasis (descargar, eliminar).
- No uses un icono ambiguo cuya acción no se entienda sin texto: en ese caso usa un Button.
- No usar IconButton de M3 suelto: se pierde el morph de forma.
- No dejar un icono como único contenido del botón sin nombre accesible en ningún sitio.
- No usar Round en reposo: el Round de Figma es el destino del morph (20 dp), no un círculo.

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

## Date & time pickers

- Estado: ready · P0
- Para qué: Entrada de fechas y horas en formularios de viaje, Wallet y plan. El producto usa composables propios en `ui/components/date/` (campos Outlined + modales M3 adaptados), no los sets M3 de catálogo archivados en Figma. El rango de viaje permite teclado `dd/MM/yyyy` y calendario acotado; Wallet usa fecha única y hora 24 h.
- Composable: sin documentar en la ficha

### Variantes

- **MotDateRangeField**: Crear y editar viaje — fechas inicio/fin.
- **MotDateRangePickerDialog · Surface**: Days (mes con flechas), Year, Month — selección rango.
- **MotDateField**: Wallet — fecha del documento.
- **MotDatePickerDialog**: Picker día único desde MotDateField.
- **MotTimeField**: Wallet y plan — hora 24 h.
- **MotTimePickerDialog**: Teclado horario por defecto; reloj opcional.

### Prohibido en este componente

- No uses el kit M3 «Input date picker» / «Modal date picker» del ARCHIVE en design-file.
- No sustituyas el calendario de un mes por DateRangePicker de lista vertical.
- No enlaces tokens kit-only (`header-gap`, `field-top`, `label-gap`, `time-input/*`) en láminas PRODUCT.
- No documentes MotTripDatePickerDialog como patrón de producto hasta que una pantalla lo use.
- No dibujar campos con frames sueltos: Text field Outlined + icon slot.
- No promover Keyboard picker `52949:28053` como SoT de producto.
- No ampliar MOTSpacing.DatePicker.* de kit sin acuerdo explícito.

### Accesibilidad

- Campos read-only: contentDescription con label + valor formateado.
- Iconos trailing: descripción «Abrir calendario» / «Abrir selector de hora».
- Errores de parseo anunciados en supporting text al perder foco.
- Modales: dismiss con back; Aceptar deshabilitado hasta rango completo (fin sin elegir).

## Filter chip

- Estado: ready · P0
- Para qué: Un filter chip elige una opción dentro de un conjunto pequeño y la deja activa. En MyOwnTrip vive en filas horizontales con scroll —Activos / Archivados / Todos en Wallet— y en formularios con pocas categorías. El runtime es el FilterChip de Material 3, sin wrapper MOT.
- Composable: `FilterChip` — `androidx.compose.material3.FilterChip`
- Fuente: `app/src/main/java/com/myowntrip/app/ui/features/wallet/WalletScreen.kt`

### Variantes

- **Outlined · Unselected** → `FilterChip`: Opciones no activas en la fila (p. ej. «Archivados»).
- **Outlined · Selected** → `FilterChip`: Filtro activo. M3 pone el check al inicio; no hace falta un leadingIcon propio.

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
- La opción está activa → `FilterChip selected = true` — Fill secondaryContainer y check leading de M3. En Wallet solo hay una seleccionada por eje.

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
- No redibujar el swipe con frames sueltos: instanciar List Item - Swipe del DS.

### Accesibilidad

- customActions en semantics de la fila (TalkBack: acciones personalizadas).
- Iconos de fondo con contentDescription («Archivar», «Eliminar», «Restaurar»).
- Menú ⋮: contentDescription «Acciones del viaje» / «Acciones del documento».
- Eliminar en menú: icono + texto error — nunca solo color.
- Ver checklist manual: docs/ux/talkback-checklist-swipe.md

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
