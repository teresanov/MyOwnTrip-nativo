# Pipeline de diseño y documentación (MOT nativo)

**Fecha:** 2026-10-30 · **Estado:** vigente

Guía de qué se crea, en qué orden y dónde vive cada cosa. No describe herramientas: describe el proceso.

---

## 1. Principio: base en Figma, componentes en código

- **Dos fuentes según el tipo de elemento:**
  - **Tokens** (color, forma, tipografía, espaciado, elevación; primitivos y semánticos): se modifican **solo en las variables de Figma** (`zrGAL4v6`) y se exportan a código (`variables.json` → `Color.kt`, `Theme.kt`). Si Compose usa un token que no existe en Figma, se crea en Figma antes de darlo por cerrado.
  - **Comportamiento y componentes**: nacen en **Compose** (`designsystem/`, `app/.../ui/components/`), sobre la base M3 y los átomos existentes.
- **Figma** es la base de tokens y, para componentes, el espejo que reproduce Compose con paridad comprobada. El design-file `Vf2tNMXy` compone las pantallas con instancias.
- **El showcase es documentación** del componente, no una fuente: explica lo que existe en Compose y en Figma.
- **Notion registra el trabajo**: decisiones, cambios e historial. No contiene especificaciones.

### Sentido de «bidireccional» (acordado 2026-11-03)

| Tipo | Flujo habitual (SoT) | Sentido inverso (Figma → Compose) |
|------|----------------------|-----------------------------------|
| **Tokens de color** (y forma/tipo en variables Figma) | Variables Figma → export → `Color.kt` / tema | Igual: cambias en Figma → **avisas** → paridad en código. Siempre bidireccional en tokens. |
| **Componentes** | **Compose** (M3 del repo + lo tocado en Android Studio) → espejo en Figma. Variables bindables: si faltan en Figma, **se crean**; si existen, **se enlazan**. | Solo si el componente **ya está en Figma** como referencia del código y **decides cambiarlo en Figma** → **avisas explícitamente** («lo he cambiado, haz paridad en código») → entonces Compose se actualiza y se vuelve a cerrar paridad Figma ↔ código. **No** se interpreta un kit M3 importado sin aviso como mandato de producto. |

Sin aviso, **no** hay paridad inversa: Figma sigue al código.

---

## 2. Flujo por componente

| Paso | Dónde | Qué se hace | Salida |
|------|-------|-------------|--------|
| **1. Compose** (SoT) | `designsystem/`, `ui/components/` | Crear o ajustar el composable con tokens del tema: `colorScheme`, `MaterialTheme.shapes`, `MOTSpacing`, `AppTypography`. Añadir `@Preview` claro y oscuro. | Código compilando |
| **2. Figma** (espejo) | Librería `zrGAL4v6` | Component set con los mismos roles M3, variantes y estados. Bindings a variables y text styles. Pantallas en `Vf2tNMXy`: solo instancias **y** paddings/gaps del `phone` enlazados a `mot/spacing/*`. | Set publicado |
| **3. Paridad** | Compose ↔ Figma | Comprobar paridad. **Append** en [parity-log.md](../parity-log.md). No crear audit nuevo por discrepancia. | Entrada en bitácora; contrato solo si cambia el spec |
| **4. Showcase** (documentación) | `ds-showcase/` | Ficha del componente: variantes, estados, accesibilidad, ejemplos, versión y cambios. | `src/data/components/<id>.json` + página |
| **5. Notion** (registro) | Changelog DS / Diario del proyecto | Entrada con la decisión, el motivo y los enlaces a código, Figma y ficha. | Entrada fechada |

**Tokens:** un cambio de color, forma o tipografía empieza en las variables de Figma, con variables semánticas por alias (nunca hex sueltos). Después se exporta a `variables.json` y a Compose. Si cambias tokens en Figma, **avisas** y se cierra paridad en Kotlin. Sin paridad, el cambio no se cierra.

**Componente desde Figma hacia código:** solo tras tu aviso de cambio intencional en el set ya espejado; entonces se implementa en Compose (paso 1) y se revalida paridad (pasos 2–3).

### Documentación para personas (ficha de componente)

**Dónde vive:** los datos en `ds-showcase/src/data/components/<id>.json`; la web que los publica es
`ds-docs/` (Astro + Starlight), que sustituye al showcase anterior. En **Figma · descripción del
COMPONENT** basta un resumen y el enlace a la ficha.

Una ficha se lee de arriba abajo en este orden, y el orden es deliberado: primero se decide **si**
se usa el componente, y solo después de qué está hecho y cómo se implementa.

**1. Uso** — para qué sirve, cuándo usarlo, **cuándo no** (con el componente alternativo), qué
variante elegir, y Do/Don't separando la guía de M3 de los acuerdos del equipo. Es el bloque más
importante: es el único que responde a la pregunta con la que llega alguien que está diseñando una
pantalla.

**2. Anatomía y estados** — las partes que lo componen y cómo se llaman, más su comportamiento
(reposo, selección, foco, pulsado, deshabilitado).

> La anatomía fija el vocabulario: que diseño y desarrollo digan «la capa de estado» y entiendan lo
> mismo es lo que hace posible un traspaso sin negociación.
>
> **Diagrama:** se añade cuando las partes no son evidentes a simple vista — una capa de estado, un
> área táctil mayor que el contenedor, un slot reservado. No es obligatorio en todos los
> componentes. Si se genera desde Figma con un plugin de specs, se exporta **solo el diagrama de
> anatomía**: el volcado de propiedades y de layout no va en la ficha.

**3. Accesibilidad** — área táctil, foco visible, contraste, lectura con TalkBack, y la regla de
que un error se comunica con icono y texto.

**4. Código** — el composable, su módulo y archivo, los parámetros y los tokens que consume. Un
fragmento representativo, no la implementación entera.

**5. Procedencia** — estado de publicación, nodo de Figma, por qué existe el componente, qué
decisión lo respalda y qué ha cambiado.

Escribir para **diseño y producto** (español), en prosa. Una línea al final puede enlazar al
contrato técnico del repo.

**No** sustituir la ficha por listas de `mot/*`, node-ids, volcados de propiedades ni jerga de
agente. Eso va en `parity-log`, bindings y contratos.

**Lo mismo vale para los patrones.** Un patrón explica qué problema resuelve, qué se decidió, por
qué, y qué no se hace. Las variantes de instancia, los ids de nodo y las rutas de scripts son
material de montaje y viven en el repo, no en la documentación.

---

## 3. Gates de salida (por componente)

Un componente solo pasa a **Ready** cuando cumple todo:

- **Compose:** compila (`./gradlew :app:compileDebugKotlin`) y tiene `@Preview` en claro y oscuro.
- **Tokens:** la UI consume solo roles semánticos (`colorScheme`, `shapes.*`, `MOTSpacing`). Sin primitivos ni hex sueltos.
- **Forma:** `shapes.*` o la regla de botones de [ADR 004](../../decisions/004-button-shape-morph.md).
- **Paridad Figma:** 0 variables LEGACY · Semantic Layout, 0 literales bindables en el set, 0 inventos en pantallas (solo instancias). Verificación con `resolveVariables` en `false` y `true`.
- **Accesibilidad:** contraste AA, objetivo táctil de 48dp, foco visible (capa `Focus ring`), y en errores icono + texto, nunca solo color.
- **Showcase:** ficha completa con versión `vX.Y.Z`, motivo del cambio y enlace a la auditoría.
- **Notion:** entrada registrada.

---

## 4. Dónde vive cada cosa

| Qué | Fuente de verdad | Espejo | Registro |
|-----|------------------|--------|----------|
| Tokens base (color, forma, tipografía, espaciado) | **Figma** (variables `zrGAL4v6`) | `variables.json`, `Color.kt`, `Theme.kt` | Repo |
| Comportamiento de componentes | **Compose** (código) | — | Repo |
| Componentes visuales | Compose | Librería Figma `zrGAL4v6` | Showcase |
| Pantallas | Compose | Design-file Figma `Vf2tNMXy` (instancias) | Notion (flujos) |
| Fichas de componente | Compose | — | **Showcase** (`ds-showcase/`) |
| Decisiones e historial | — | — | **Notion** (Changelog DS, Diario) |
| Auditorías y contratos | — | — | Repo: contratos estables + **[parity-log.md](../parity-log.md)**; audits `*-phase-*` históricos |

---

## 5. Qué no es este pipeline

- Figma **no** es fuente de componentes (son espejo de Compose), pero sí es fuente de los tokens base.
- El showcase **no** define comportamiento: documenta.
- Notion **no** documenta componentes ni especificaciones.
- Pencil **no** forma parte del flujo.
- El repo archivo `MyOwnTrip` **no** es referencia.

---

## 6. Versionado

- Cada componente lleva versión `vX.Y.Z` en su ficha del showcase.
- Cada entrada incluye: qué cambió, por qué, y evidencia (commit, auditoría, nodo de Figma).
- El cambio general se registra en Changelog DS.

---

## 7. Puntos abiertos

- **Tokens base: decidido (30 oct 2026).** Nacen en las variables de Figma y se exportan a código (`variables.json` → `Color.kt`), coherente con el Changelog DS (12 jun 2026) y con la skill `ds-semantic-pipeline` (Modo B, Figma-first).
- **Índice de componentes** (`docs/design-system/index/components.index.json`): citado por las skills, pero no existe todavía en el repo.

---

## 8. Backlog de reconciliación (Compose sin Figma)

**Contexto:** durante varios meses Figma no se usó para diseñar. Los tokens se importaron de las variables de Figma a Kotlin y las librerías M3 de Jetpack se usaron directamente, así que muchos componentes y pantallas existen en Compose sin espejo en Figma.

**Regla:** este backlog se trata como cualquier otro cambio. Cada token o componente de Compose que falte en Figma se crea en Figma, y después se comprueba la paridad.

**Espejo de tokens (plan de paridad, 29 oct 2026):** cada `mot/*` de Figma tiene su espejo en `MOTSpacing` (o `shapes.*`). Un `mot/*` sin espejo es huérfano. El backlog se trabaja **dentro de la fase en curso** de [compose-figma-parity-plan-2026-10-29.md](../audits/compose-figma-parity-plan-2026-10-29.md), no en paralelo: primero se define el objeto en Compose y después se enlaza en Figma.

**Orden de trabajo:**
1. **Inventario de tokens:** listar los tokens usados en `Color.kt`, `Theme.kt` y `MOTSpacing` que no existan como variables en `zrGAL4v6`. Crearlos en Figma como variables semánticas por alias a primitivos.
2. **Inventario de componentes:** listar los composables de `ui/components/` y `designsystem/` sin set en la librería. Priorizar los que usan las pantallas en uso.
3. **Pantallas:** llevar cada pantalla al design-file `Vf2tNMXy` con instancias de la librería; si falta un componente, primero se crea en la librería.
4. **Paridad y ficha:** para cada elemento, comprobar paridad, documentar en el showcase y registrar en Notion (paso 5 del flujo).

**Salida:** cuando el inventario quede en cero pendientes, el sistema es bidireccional y se aplica solo el flujo normal de las secciones 1–6.

---

## 9. Componentes de Material 3 y valores fijos

Un componente puede ser **propio** (nace en `designsystem/` o `ui/components/`) o **M3** (se usa la librería de Material 3 tal cual, con su versión fijada en `gradle/libs.versions.toml`).

- **Qué se tokeniza:** todo lo que Compose recibe como parámetro o token (colores del tema, `MaterialTheme.shapes.*`, `MOTSpacing`, `contentPadding`). Figma es fuente de esos tokens.
- **Qué queda fijo:** los valores privados de la librería de M3 (constantes internas). Desde Figma no se pueden cambiar sin cambiar el código de la librería o sustituir el componente.
- **Cómo se documenta:** en la ficha del showcase (`ds-showcase/src/data/components/<id>.json`, bloque `origin`), con `type`, `m3Version`, `fixedValues` y `pending` (lo que aún no está verificado). En Figma, en la descripción del componente o set, con la misma información.
- **Revisión:** en cada subida de versión de `material3`, se comparan de nuevo los valores fijos contra la fuente.
- **Showcase solo con lo usado:** la ficha recoge los tokens que el componente consume en código. Los tokens reservados (sin consumidor en Compose) viven en Figma y en `MOTSpacing` con la marca «Reservado» en su KDoc, y no aparecen en el showcase hasta que un composable los use.

Ejemplo: el campo de hora (`.Building Blocks/Input`, `Direct Input (keyboard) input`) es M3. Valores fijos: gap de 8 px (`TimeInputBottomPadding`), gap de 7 px (`SupportLabelTop`), alto 72 px y ancho 96 px del campo, verificados contra `material3-1.5.0-alpha12`.

---

## 10. Date / time pickers — espejo desde Compose (2026-11-03)

**Regla habitual:** el componente nace en **Compose** (M3 Gradle + `app/.../date/`). Figma reproduce **exactamente** lo consumido en pantallas. Variables: **crear** si faltan, **enlazar** si existen.

**Bidireccional (componentes):** solo si cambias en Figma un set **PRODUCT** ya espejado del código y **avisas** — entonces paridad hacia Kotlin.

El kit M3 importado (**Input / Modal / Keyboard picker**) que **no** coincide con el composable **no** manda por sí solo; se alinea al código o se archiva. Contrato: [figma-date-product-contract-2026-11-03.md](../audits/figma-date-product-contract-2026-11-03.md). Cambios: [parity-log.md](../parity-log.md).

**`MotTripDatePickerDialog`:** sin pantalla consumidora → no PRODUCT en Figma hasta uso en app.
