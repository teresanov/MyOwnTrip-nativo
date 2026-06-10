# Breakpoints y window size classes — MyOwnTrip

Define **qué anchos de pantalla** diseñamos e implementamos, con referencia Samsung foldable y puerta abierta a iOS.

| | |
|---|---|
| **Estado** | Activo — norte para Figma, Compose y poda DS |
| **Última revisión** | 2026-06-10 |
| **Relacionado** | [`figma-prune-inventory.md`](figma-prune-inventory.md) · [`android-compose-ux.md`](../ux/android-compose-ux.md) |

---

## 1. Alcance de dispositivo (producto)

| Categoría | ¿En scope? | Notas |
|-----------|------------|-------|
| Teléfono (portrait / landscape) | **Sí** | Target principal MVP |
| Plegable en el bolsillo (Flip, Fold, futuros) | **Sí** | Misma persona, mismo uso que un móvil |
| Tablet dedicada (10"+, iPad, Tab S) | **No** | No es el JTBD del viajero urbano con app en el bolsillo |
| Web / desktop / XR | **No** | Fuera de producto y de librería Figma |

**Regla práctica:** si el usuario lo lleva **plegado en el bolsillo**, entra. Si lo lleva en una funda o mochila como tablet, no.

**Foldable ≠ tablet:** la pantalla interior de un Fold puede superar 600dp o incluso 840dp de ancho, pero la UI sigue siendo **móvil-first** (nav inferior en Compact; en anchos mayores, **nav adaptativa** v1.1+ — ver §4.4).

---

## 2. Breakpoints oficiales Android (Material 3)

Google agrupa el espacio disponible en **window size classes** por ancho y alto por separado. Los umbrales son fijos ([Android Developers](https://developer.android.com/develop/ui/views/layout/use-window-size-classes)):

### Ancho (el que más nos importa)

| Clase | Breakpoint | Representación típica |
|-------|------------|------------------------|
| **Compact** | ancho &lt; **600dp** | ~99,96% móviles en portrait |
| **Medium** | **600dp** ≤ ancho &lt; **840dp** | Fold interior en portrait; tablet pequeña en portrait |
| **Expanded** | **840dp** ≤ ancho &lt; **1200dp** | Tablet landscape; Fold interior en landscape |
| **Large** | **1200dp** ≤ ancho &lt; **1600dp** | Tablets grandes |
| **Extra-large** | ancho ≥ **1600dp** | Desktop |

### Alto

| Clase | Breakpoint |
|-------|------------|
| **Compact** | alto &lt; **480dp** |
| **Medium** | **480dp** ≤ alto &lt; **900dp** |
| **Expanded** | alto ≥ **900dp** |

Las clases son **dinámicas**: split-screen, rotación o plegar/desplegar cambian la clase sin cambiar de dispositivo.

---

## 3. Referencia Samsung (estándar foldable)

Dispositivos de referencia para frames Figma y pruebas ([One UI — large screen](https://developer.samsung.com/one-ui/largescreen-and-foldable/large_screen_layout.html), [Z Fold6 / Z Flip6 specs](https://www.gsmarena.com/samsung_galaxy_z_fold6-13147.php)):

### Galaxy Z Fold 6

| Postura | Pantalla | px (aprox.) | Clase ancho típica | Clase alto típica |
|---------|----------|-------------|-------------------|-------------------|
| Cerrado (cover) | 6,3" | 968 × 2376 | **Compact** (~378dp) | Medium |
| Abierto portrait | 7,6" interior | 1856 × 2160 | **Medium** (~794dp) | Expanded (~924dp) |
| Abierto landscape | 7,6" interior | 2160 × 1856 | **Expanded** (~924dp) | Medium |

### Galaxy Z Flip 6

| Postura | Pantalla | px (aprox.) | Clase ancho típica |
|---------|----------|-------------|-------------------|
| Cerrado (cover) | 3,4" | 720 × 748 | **Compact** |
| Abierto | 6,7" | 1080 × 2640 | **Compact** (~406dp) |

**Lectura:** el Flip se comporta como móvil en todas las posturas. El Fold es el que introduce **Medium** (y a veces **Expanded** en landscape interior).

Samsung recomienda considerar “large screen layout” desde **600dp** de ancho — es decir, mejoras opcionales (más columnas, menos pantallas intermedias), **no** obligar patrones de tablet de 10".

---

## 4. Tiers MyOwnTrip (qué soportamos cuándo)

### Tier A — MVP (obligatorio)

| Clase ancho | Posturas | Comportamiento UI |
|-------------|----------|-------------------|
| **Compact** | Teléfono portrait; Flip abierto; Fold **plegado** (cover); landscape móvil | Layout por defecto: una columna, `NavigationBar`, scroll vertical |
| **Medium** | Fold interior portrait (~600–840dp) | **Referencia en Figma** (`[LATER]` para diseño); en código, mismo chrome móvil hasta v1.1 |

**Compose MVP:** lógica **Compact-first**; medir con `currentWindowAdaptiveInfo()` y preparar hooks, sin list-detail ni rail.

### Guía diseñador (Figma · Examples)

Nota en sección `Layout grid · mobile` · node `60954:132843` — misma tabla que §5.

### Tier B — v1.1+ (acordado; priorizar según uso real)

| Mejora | Clase | Ejemplo JTBD | Estado |
|--------|-------|----------------|--------|
| **Grids 2 columnas** en home (y pantallas densas) | Medium | Más viajes visibles al desplegar Fold | **Sí** — mantener en diseño |
| List-detail en una sola superficie | Medium | Viaje → día sin back stack extra | Cuando una pantalla lo pida |
| Teclado landscape en mockups | Compact alto / landscape | Formularios wallet en Fold horizontal | Mockups / QA |
| **Nav adaptativa** (`NavigationSuiteScaffold`: barra inferior → rail lateral) | Medium / Expanded | Ergonomía Fold abierto a dos manos ([M3 large screen](https://m3.material.io/foundations/layout/applying-layout), [Samsung foldable](https://developer.samsung.com/one-ui/largescreen-and-foldable/large_screen_layout.html)) | **Puerta abierta v1.1+** — ver §4.4 |

### Tier C — Fuera de scope (no diseñar ni podar “para tablet”)

| Patrón | Por qué |
|--------|---------|
| Navigation **rail fijo** como único chrome en todas las pantallas | Tablet 10"+ / desktop |
| **Drawer** permanente + arquitectura tablet | Fuera de JTBD MVP |
| **Side sheet** persistente + 3 paneles | Tablet |
| Layouts **Expanded / Large / XL** como target principal | iPad, Tab S, ChromeOS ventana grande |
| Examples `-Web`, window class tablet en Figma | Ver [`figma-prune-inventory.md`](figma-prune-inventory.md) |

**Nota:** Fold interior en **landscape** puede caer en Expanded por dp. MVP: **no romper** + reflow (2 columnas); **no** rediseñar todo el shell con rail hasta datos de uso (§4.4).

### 4.4 Reconciliación M3 large screen (acordado 2026-06-10)

Google / Samsung recomiendan acercar la navegación al pulgar y, en pantalla ancha tipo “libro”, pasar de barra inferior a **riel lateral**. Eso es correcto en ergonomía; MyOwnTrip **no lo ignora** — lo **secuencia**.

| Postura / ancho | M3 / Samsung (resumen) | MyOwnTrip **ahora** (MVP) | MyOwnTrip **v1.1+** (si el uso lo confirma) |
|-----------------|------------------------|---------------------------|---------------------------------------------|
| Cerrado, una mano (&lt;600dp) | Bottom bar | `NavigationBar` abajo | Igual |
| Abierto portrait (600–840dp) | Más columnas; a veces rail | Mismo nav abajo + **2 columnas** en contenido | Nav adaptativa opcional |
| Abierto **apaisado** (≥840dp) | Rail lateral; evitar hinge | **Smoke** 840×673 — no romper; nav abajo; 2 columnas | Nav adaptativa; restaurar sets **rail** desde CS si hace falta en Figma |
| Flex / tabletop | Controles en mitad inferior | No MVP (`FoldingFeature`, no solo ancho) | Post-MVP si hay JTBD |

**Disparador para activar nav adaptativa + breakpoint de apaisado dedicado:**

1. Métricas o pruebas en Fold (p. ej. % sesiones interior abierto + apaisado).
2. Si el uso es marginal → mantener bottom bar + 2 columnas.
3. Si es alto → implementar `NavigationSuiteScaffold` (o equivalente) y, en Figma, sección **LATER · Fold expanded** con rail restaurado desde backup CS (`uWmxOSQfjOxlEJ8k1yzOSX`) — sin reimportar kit tablet completo.

**Reglas que no cambian:** nada interactivo sobre el pliegue; acciones contextuales en **app bar** (derecha) o toolbar horizontal — no toolbar vertical de tablet; Flex Window fuera de MVP.

---

## 5. Frames Figma (librería y pantallas)

**Component set publicado:** `Layout grid · mobile` · node `60955:133047` en página Examples (sección `55343:13515`: guía `60954:132843` + contenedor de 6 frames) — sustituye `Examples/Layout grid` (M3 completo, eliminado 2026-06-09).

**Example layouts** · sección `56554:638` — 7 pantallas demo en **3 filas** Compact: Phone base 360×800 · Z Fold plegado 344×880 · **landscape smoke 800×360** (status + app bar + cuerpo horizontal en columnas; sin Navigation Rail ni Device frame en Examples — ver `Utilities`/`Reference` si hace falta mock de dispositivo). Sin fila `-Web`.

### Tabla canónica (diseño)

| Nombre del frame | Tamaño (dp) | Qué representa | MVP |
|------------------|-------------|----------------|-----|
| **Compact · Phone (base)** | **360 × 800** | Lienzo principal. Diseña aquí por defecto | **Sí** |
| **Compact · Z Flip abierto** | **360 × 880** | Stress de **altura** (22:9). Verifica scroll y barras ancladas | **Sí** (QA) |
| **Compact · Z Fold plegado** | **344 × 880** | Stress de **ancho mínimo**. Nada se corta a 344dp | **Sí** (QA) |
| **Compact · Phone grande** | **412 × 915** | Límite alto de Compact (Pixel, Galaxy grandes) | **Sí** |
| **Medium · Z Fold abierto (LATER)** | **673 × 841** | Solo referencia; 2 paneles, no MVP | **LATER** |
| **Compact · landscape (smoke)** | **800 × 360** | Rotación / teclado; no rediseñar layouts | Smoke |
| **Flex Window** | **260 × 272** | Cover del Z Flip — ver decisión abajo | **CUT MVP** |

**Notas:**

- El Fold **cerrado** real ronda **~378dp** de ancho; el frame **344×880** es piso de stress (peor caso Compact), no el tamaño literal del cover del Fold 6.
- El Flip **abierto** ronda **~406dp** de ancho; **360×880** estresa alto en ancho mínimo; **412×915** cubre el techo de Compact.
- **Flex Window:** cover Z Flip plegado (260×272). **CUT MVP** — demasiado pequeño para JTBD viaje; Flip **abierto** = Compact (Examples 360×880). Ver guía en página `Reference`.

### Referencia adicional (no en component set)

Página **`Reference`** · guía diseñador `61053:230239` (sección `61053:230238`) — texto explicativo en Figma; no publicar en subset MVP.

| Frame | Tamaño | Uso |
|-------|--------|-----|
| **Flex Window** `60955:132903` | **260 × 272** | Cover **Z Flip plegado** — **CUT MVP** (no app completa; post-MVP solo widget/glance si producto lo pide) |
| **Frame A** `61053:230253` | **840 × 673** | **Viajes / list-detail** — 2 cols (v1.1); MVP puede ser 1 columna |
| **Frame B** `61053:230281` | **840 × 673** | **Mapa** — 1 col + nav rail adaptativa + acciones en borde (exploratorio v1.1+) |

**CUT en Figma:** frames tablet 10"+, `-Web`, Navigation rail, Examples Medium/Expanded/Large/XL orientados a tablet.

**Utilities teclado:** Portrait + Landscape **KEEP**; Floating **CUT** (multitarea tablet).

---

## 6. Implementación Compose (resumen)

```kotlin
// androidx.compose.material3.adaptive / WindowSizeClass
// Umbrales width: 600, 840, 1200, 1600 dp
val adaptiveInfo = currentWindowAdaptiveInfo()
val widthClass = adaptiveInfo.windowSizeClass.windowWidthSizeClass
```

| Fase | Estrategia |
|------|------------|
| **MVP** | `Scaffold` + `NavigationBar`; contenido `Compact`; `paddingValues` edge-to-edge |
| **Fold Medium** | Mismos componentes; `LazyVerticalGrid` / **2 columnas** donde aplique; opcional `ListDetailPaneScaffold` en 1–2 flujos |
| **v1.1+ (si uso apaisado lo justifica)** | `NavigationSuiteScaffold` — barra inferior en Compact → rail en Medium/Expanded; sin drawer por defecto |
| **Tests** | Previews 360×800, 344×880, 360×880, smoke 840×673; emulador Fold 6 / Flip 6 API 34+ |

---

## 7. iOS futuro (sin comprometer Android)

Puerta abierta sin duplicar trabajo:

| Concepto Android | Equivalente iOS (futuro) |
|------------------|---------------------------|
| Compact &lt; 600dp | iPhone estándar (~390–430 **pt** ancho) |
| Medium 600–840dp | Fold interior / iPhone landscape ancho; **futuro foldable Apple** (TBD) |
| Expanded+ | **iPad — fuera de scope** (misma regla que tablet Android) |

**Política:** diseñar en **buckets de ancho** (compact / medium), no en modelos concretos. Un futuro iPhone plegable debería mapear al bucket **Medium** con las mismas reglas que Fold interior: más espacio, mismo chrome móvil.

SwiftUI / Compose Multiplatform (si algún día): compartir breakpoints lógicos en documentación, no en tokens de color.

---

## 8. Impacto en poda Figma (checklist rápido)

| Mantener | Podar |
|----------|-------|
| Device frame móvil + fold | Device frame tablet |
| Keyboard Portrait + Landscape | Keyboard Floating |
| Componentes M3 Compact | bottom app bar, XR |
| 1–2 refs fold interior en `Reference`; rail en **LATER** si nav adaptativa | Examples web, grids tablet XL; rail publicado en MVP |
| Variantes que existen en teléfono | `Context=Tablet`, densidades solo tablet |

---

## 9. Fuentes

- [Use window size classes — Android Developers](https://developer.android.com/develop/ui/views/layout/use-window-size-classes)
- [WindowSizeClass API](https://developer.android.com/reference/androidx/window/core/layout/WindowSizeClass) — `WIDTH_DP_MEDIUM_LOWER_BOUND = 600`, `WIDTH_DP_EXPANDED_LOWER_BOUND = 840`
- [Layout design for large screens — Samsung Developer](https://developer.samsung.com/one-ui/largescreen-and-foldable/large_screen_layout.html)
- [Material 3 — Applying layout](https://m3.material.io/foundations/layout/applying-layout)
- [Compose Material 3 Adaptive](https://developer.android.com/jetpack/androidx/releases/compose-material3-adaptive)
