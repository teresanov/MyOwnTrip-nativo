---
title: Android Compose UX (runtime)
description: Normativa de motion, escalado de texto, semántica TalkBack, edge-to-edge, gestos, formularios y back en pantallas de la app
status: active
lastUpdated: 2026-06-11
---

# MyOwnTrip — Android Compose UX (runtime)

## Objetivo

Definir comportamiento de **UI en runtime** en Jetpack Compose que no cubren las reglas del DS en Figma ni los gates de tokens/binding.

## Alcance y límites (obligatorio)

**Esta policy cubre:** motion M3, escalado de fuente del sistema, semántica TalkBack, edge-to-edge / insets, alternativas a gestos, formularios con teclado, back con cambios sin guardar.

**No sustituye ni duplica** (consultar la fuente indicada):

| Tema | Fuente canónica |
|------|-----------------|
| Color / state layers | `docs/design-system/color.md`, `.cursor/rules/m3-native-ui.mdc` |
| Quality gates DS | `docs/design-system/governance.md` |
| Tipografía (Fraunces / Inter) | `docs/design-system/typography.md` |
| Iconos UI | `docs/design-system/iconography.md` |
| Componentes M3 vs wrappers | `docs/design-system/components.md` |
| Touch target 48dp y foco visible | WCAG 2.2; focus ring (futuro ADR) |
| Offline-first, datos, JTBD | `.cursor/rules/myowntrip-development.mdc`, `docs/product/jtbd-flows.md` |

**Gate del agente en Cursor:** `.cursor/rules/android-compose-ux.mdc` (obligatorio al tocar `app/src/main/**` y tests UI de Android).

**Prioridad en conflicto:** `docs/design-system/governance.md` (source of truth).

### Dónde documentar (flujo MyOwnTrip)

No hace falta **prototipar ni tokenizar motion en Figma** si el equipo no diseña animaciones allí.

| Canal | Uso |
|-------|-----|
| **Esta policy + `Motion.kt`** | Fuente canónica para dev e IA (duraciones, tipos de transición, accesibilidad). |
| **Notion** | Specs de producto/pantalla: qué se anima, por qué, tipo M3 (fade, slide…), ritmo (Short / Medium / Long). |
| **Showcase externo** | Documentación de componentes + demos interactivas — ver `docs/design-system/showcase.md`. |

Figma = **librería** (variables + component sets); **no** documentación en canvas. Motion en runtime: Android + esta policy.

---

## 1. Motion (Material 3)

Las reglas de binding y tokens no cubren motion. Usar **solo** las duraciones del objeto canónico `ui/theme/Motion.kt` (`MOTMotion`):

```kotlin
object MOTMotion {
    const val Short1  =  50  // ms — micro-interacciones (ripple, feedback inmediato)
    const val Short2  = 100  // ms — cambios de estado en componentes pequeños
    const val Medium1 = 200  // ms — transiciones de componente (expansión, colapso)
    const val Medium2 = 300  // ms — transiciones de pantalla (el más usado)
    const val Long1   = 400  // ms — transiciones complejas (bottom sheet, modal)
    const val Long2   = 500  // ms — máximo recomendado
}
```

### Tipos de transición por contexto

| Contexto | Tipo M3 | API Compose |
|----------|---------|-------------|
| Navegar entre tabs (NavigationBar) | Top level | `fadeIn` + `scaleIn(0.92f)` |
| Navegar hacia adelante/atrás en jerarquía | Forward/backward | `slideInHorizontally` / `slideOutHorizontally` |
| Elementos que aparecen o desaparecen | Enter/exit | `fadeIn` / `fadeOut` |
| Cambio de contenido (loading → datos) | Persistent | `AnimatedContent` con `ContentTransform` |

### Reglas

- **Respetar `LocalReduceMotion`** (Material 3): si el usuario activó reducir animaciones, usar duración `0`.
- **No animar texto directamente** — animar siempre el contenedor.
- **No animar elementos fuera del viewport**.

### Shape morph (M3 Expressive · marca · ADR 004)

- **Reposo = 0dp** (`Corner/None`) — rectángulo editorial.
- **Morph a 20dp** (`Corner/Large-increased`) en `hover` (si aplica), `focus`, `pressed` y `selected`.
- **Móvil:** sin hover → morph en pressed, focus y selected (tabs, toggles, segmented).
- **Duración:** `AppMotion.DurationShapeMorph` (**520ms**); curva **emphasized decelerate**; `LocalReduceMotion` → 0ms.
- **Prohibido** radio redondeado en reposo que vuelva a 0 al deseleccionar (dirección siempre None → 20).
- **FAB:** circular — excepción; cards 12dp y chips 8dp fijos (sin morph de botón).

Detalle tokens y Figma: `docs/design-system/shape.md`.

```kotlin
import com.myowntrip.app.ui.theme.AppMotion
import com.myowntrip.app.ui.theme.rememberMOTButtonShape

Button(
  onClick = { },
  shape = rememberMOTButtonShape(),
  // colors = ButtonDefaults… — state layers M3 en runtime
)
```

Toggle / `FilterChip` selected: mismo par 0→20 vía `InteractionSource` o `selected = true` según componente.

---

## 2. Escalado de texto (runtime)

`typography-guardrails.md` cubre fuente y roles del DS; aquí solo el comportamiento con **escala del sistema**.

```kotlin
// ❌ INCORRECTO — rompe con escalado al 200%
Box(modifier = Modifier.height(24.dp)) { Text(text = title) }

// ✅ CORRECTO — rol tipográfico + truncado, sin altura fija en dp para el texto
Text(
    text = title,
    style = MaterialTheme.typography.titleMedium,
    maxLines = 2,
    overflow = TextOverflow.Ellipsis
)

// ❌ INCORRECTO — deshabilita escalado del sistema
val size = with(LocalDensity.current) { 16.sp.toDp() }

// ❌ INCORRECTO — tamaño suelto sin rol (ver typography-guardrails)
Text(fontSize = 16.sp)
```

**Probar siempre con escala al 200%:** Ajustes → Accesibilidad → Tamaño de fuente → Máximo. Ningún texto debe quedar cortado.

---

## 3. Semántica TalkBack en Compose

`wcag-focus-touch-target.entry.json` cubre target táctil y foco visible; aquí la **semántica** de nodos Compose.

### Agrupar elementos relacionados

```kotlin
Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
    Text(restaurant.name)
    Text(restaurant.address)
    StatusBadge(restaurant.status)
}
```

### Anunciar cambios de estado dinámicos

```kotlin
Card(
    modifier = Modifier.semantics {
        stateDescription = when (status) {
            RestaurantStatus.WITHOUT_RESERVATION -> "Sin reserva"
            RestaurantStatus.RESERVED            -> "Reservado"
            RestaurantStatus.VISITED             -> "Visitado"
        }
    }
)

// Cambios en lista sin navegación
modifier = Modifier.semantics {
    liveRegion = LiveRegionMode.Polite
}
```

### Decorativos

```kotlin
// Decorativo — TalkBack lo ignora
Box(modifier = Modifier.semantics { invisibleToUser() }) { ... }
```

### Iconos de producto

Aplicar **`docs/design-system/iconography.md`** (`contentDescription` en acciones; `null` solo si es decorativo dentro de un control que ya tiene descripción). Color vía `colorScheme`, no tokens custom.

### Orden de foco

- Sigue el orden visual: arriba → abajo, izquierda → derecha.
- BottomSheets y modales capturan el foco al abrirse (automático en Compose).
- Al cerrar un modal, el foco vuelve al elemento que lo abrió.

---

## 4. Edge-to-edge y WindowInsets

Obligatorio para Android 15+.

```kotlin
// MainActivity — antes de setContent
enableEdgeToEdge()

// ❌ INCORRECTO — contenido detrás de NavigationBar del sistema
Scaffold { _ -> LazyColumn { ... } }

// ✅ CORRECTO
Scaffold { paddingValues ->
    LazyColumn(contentPadding = paddingValues) { ... }
}
```

`paddingValues` del `Scaffold` incluye la barra de la app y la del sistema con `enableEdgeToEdge()`. Nunca ignorarlo.

---

## 5. Gestos alternativos (accesibilidad)

Swipe y drag & drop no son accesibles con TalkBack. Siempre requieren **alternativa por tap**.

```kotlin
StatusBadge(
    status = restaurant.status,
    modifier = Modifier
        .clickable { showStatusMenu = true }
        .semantics {
            contentDescription = "Estado: ${restaurant.status.label}. Toca para cambiar"
        }
)

IconButton(
    onClick = { showMoveMenu = true },
    modifier = Modifier.semantics { contentDescription = "Reordenar bloque" }
) {
    Icon(
        imageVector = Icons.Sharp.Menu,
        contentDescription = null,
        modifier = Modifier.size(24.dp),
    )
}
```

**Regla:** todo gesto de swipe o drag debe tener alternativa accesible por tap. No usar gestos de dos dedos para acciones principales.

---

## 6. Formularios accesibles con teclado

Usar **`OutlinedTextField`** o **`TextField`** M3 en formularios. Errores con icono + texto (no solo color):

```kotlin
OutlinedTextField(
    value = name,
    onValueChange = { name = it },
    label = { Text("Nombre del restaurante") },
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions = KeyboardActions(
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    ),
)

OutlinedTextField(
    value = notes,
    onValueChange = { notes = it },
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    keyboardActions = KeyboardActions(
        onDone = { focusManager.clearFocus(); onSave() }
    ),
)

OutlinedTextField(
    value = name,
    onValueChange = { name = it },
    isError = nameError != null,
    supportingText = {
        if (nameError != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Sharp.Error, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(nameError)
            }
        }
    },
)
```

- Errores **bajo el campo** (`supportingText`), no solo en `AlertDialog`.
- Alineado con `myowntrip-development.mdc`: evitar validación solo al submit en formularios largos.

---

## 7. Back handler con datos sin guardar

```kotlin
var hasUnsavedChanges by remember { mutableStateOf(false) }
var showDiscardDialog by remember { mutableStateOf(false) }

BackHandler(enabled = hasUnsavedChanges) {
    showDiscardDialog = true
}

if (showDiscardDialog) {
    AlertDialog(
        onDismissRequest = { showDiscardDialog = false },
        title = { Text("¿Descartar cambios?") },
        text = { Text("Los cambios no guardados se perderán.") },
        confirmButton = {
            TextButton(onClick = { showDiscardDialog = false; navController.navigateUp() }) {
                Text("Descartar")
            }
        },
        dismissButton = {
            TextButton(onClick = { showDiscardDialog = false }) {
                Text("Seguir editando")
            }
        }
    )
}
```

**Regla:** interceptar back **solo** cuando hay datos sin guardar reales. No interceptar en pantallas de solo lectura. El diálogo debe ser **descartable** (coherente con `myowntrip-development.mdc`).

---

## Checklist de cierre (pantallas / features)

- [ ] Gate **`m3Canonical`** pasada (`docs/design-system/governance.md`).
- [ ] Motion respeta `LocalReduceMotion` y duraciones M3.
- [ ] Texto probado al 200% de escala del sistema sin cortes.
- [ ] Semántica TalkBack: agrupación, estados, decorativos; iconos según `docs/design-system/iconography.md`.
- [ ] `enableEdgeToEdge` + `paddingValues` del `Scaffold` aplicados.
- [ ] Swipe/drag con alternativa por tap.
- [ ] Formularios: `ImeAction`, errores inline, componente canónico de campo cuando exista.
- [ ] `BackHandler` solo con cambios sin guardar reales.
