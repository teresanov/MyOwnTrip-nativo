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

El flujo es **bidireccional**. Un cambio puede empezar en Figma (exploración), pero no se da por cerrado hasta que está en Compose y la paridad se ha comprobado.

---

## 2. Flujo por componente

| Paso | Dónde | Qué se hace | Salida |
|------|-------|-------------|--------|
| **1. Compose** (SoT) | `designsystem/`, `ui/components/` | Crear o ajustar el composable con tokens del tema: `colorScheme`, `MaterialTheme.shapes`, `MOTSpacing`, `AppTypography`. Añadir `@Preview` claro y oscuro. | Código compilando |
| **2. Figma** (espejo) | Librería `zrGAL4v6` | Component set con los mismos roles M3, variantes y estados. Bindings a variables y text styles. Pantallas solo con instancias en `Vf2tNMXy`. | Set publicado |
| **3. Paridad** | Compose ↔ Figma | Comprobar estados, variantes, forma, color por rol, espaciado y tipografía. Registrar en auditoría. | `docs/design-system/audits/YYYY-MM-DD_*.md` |
| **4. Showcase** (documentación) | `ds-showcase/` | Ficha del componente: variantes, estados, accesibilidad, ejemplos, versión y cambios. | `src/data/components/<id>.json` + página |
| **5. Notion** (registro) | Changelog DS / Diario del proyecto | Entrada con la decisión, el motivo y los enlaces a código, Figma y ficha. | Entrada fechada |

**Tokens:** un cambio de color, forma o tipografía empieza en las variables de Figma, con variables semánticas por alias (nunca hex sueltos). Después se exporta a `variables.json` y a Compose. Sin paridad, el cambio no se cierra.

Si el cambio es de componente y nace en Figma, se traslada a Compose y luego se vuelve al paso 2 para cerrar la paridad.

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
| Auditorías y contratos | — | — | Repo (`docs/design-system/audits/`) |

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
