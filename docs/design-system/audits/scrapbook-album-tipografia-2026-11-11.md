# Tipografía del álbum — inventario y paso a roles

**Fecha:** 2026-11-11
**Alcance:** `app/src/main/java/com/myowntrip/app/ui/features/journal/spread/ScrapbookAlbum.kt` (978 líneas)
**Motivo:** la pantalla nunca pasó por paridad con Figma y construye su tipografía con literales.
**Regla aplicada:** no hay excepciones. Cada casuística se resuelve creando el rol, no un literal en pantalla.

## Situación

29 declaraciones tipográficas con valores literales. No consume `MaterialTheme.typography` en
ningún punto, lo que incumple el gate `m3Canonical` §«Pantallas: solo MaterialTheme.colorScheme /
typography».

Tamaños en uso: **9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 20, 22, 24, 26 sp**.
De esos, seis existen en la escala M3 (11, 12, 14, 16, 22, 24); los otros ocho no.

Tracking en uso: 0,3 · 1,0 · 1,2 · 1,6 · 1,8 sp. La escala llega a 0,5.

## No hay conflicto con el ADR 001

El ADR 001 restringe la capa **de color** (prohíbe semánticos propios tipo `text/primary`). No dice
nada de tipografía. Y `Theme.kt` ya monta `MaterialExpressiveTheme`, así que los roles
`*Emphasized` de M3 Expressive están disponibles sin salirse de M3 canónico.

Conviene preferirlos antes que inventar nombres propios: `small-emphasized`, el estilo que se creó
en Figma, es vocabulario de M3 Expressive, no un invento del equipo.

## Inventario

### A — Ya encajan en un rol base: sustitución directa

| Línea | Hoy | Rol propuesto |
|---|---|---|
| 285, 589 | Inter 14 / lh 20 | `bodyMedium` (14/20) — coincide exacto |
| 335 | 12 | `bodySmall` (12/16) |
| 827 | 12 Medium | `labelMedium` (12/16 Medium) — coincide |
| 267 | Inter 14, texto de botón «Entendido» | `labelLarge` (14/20 Medium) |

### B — Encajan con un ajuste a decidir

| Línea | Hoy | Rol propuesto | Qué cambia |
|---|---|---|---|
| 222 | Inter 11, ls 0,3 | `labelSmall` (11/16, ls 0,5) | el tracking sube de 0,3 a 0,5 |
| 807 | 12 SemiBold | `labelMediumEmphasized` | el rol base es Medium (500); aquí hace falta 600 |

### C — Fraunces editorial: siete tamaños que hay que consolidar

| Línea | Hoy | Para qué sirve |
|---|---|---|
| 330 | Fraunces SemiBold 26 | Título del día (portada de capítulo) |
| 252 | Fraunces SemiBold 22 | Título de la hoja de ayuda |
| 926 | Fraunces 20 | Vacío general («Aún no hay diario») |
| 378, 880 | Fraunces 18 | Vacío de hoja · cabecera de lectura |
| 341, 895 | Fraunces 16 | Pie de hoja · vacío de hoja |
| 754 | Fraunces SemiBold 15 | Ciudad en el sello |
| 695 | Fraunces 14 / lh 18 | Texto de recuerdo |

Siete tamaños para un mismo papel editorial es la raíz del problema. La escala M3 ofrece
`headlineSmall` (24), `titleLarge` (22) y `headlineMedium` (28) en ese tramo, pero `titleLarge` es
Inter en este sistema.

**Decisión pendiente:** o se consolidan estos siete en dos o tres roles de titular Fraunces —
reutilizando `headlineSmall` / `headlineMedium` y añadiendo sus variantes `Emphasized`— o el álbum
necesita roles propios que habría que añadir a `AppTypography` y publicar en Figma.

### D — Patrón «eyebrow»: ya existe componente para esto

| Línea | Hoy | Nota |
|---|---|---|
| 325 | Fraunces SemiBold 11, ls 1,8, ocre | «DÍA N» |
| 675 | SemiBold 10, ls 1,0, mayúsculas | Etiqueta de sección |
| 759 | SemiBold 9, ls 1,6 | Fecha del sello |

Los tres son el patrón **Eyebrow label**, que ya es un componente del sistema con ficha propia.
Deberían instanciarlo en lugar de reconstruirlo con literales. Si el álbum necesita un tamaño que
el componente no tiene, se añade como variante del componente.

### E — La «pluma»: tres tamaños para un solo papel

| Línea | Hoy |
|---|---|
| 549, 637 | Hand Italic 16 |
| 600 | Hand Italic 17 / lh 22 |
| 702 | Hand Italic 15 |

15, 16 y 17 sp hacen el mismo trabajo. Propuesta: **un solo rol** para la nota manuscrita, más otro
si de verdad hacen falta dos tamaños. `Type.kt` ya documenta que la pluma vive a ≥16sp.

### F — Texto de apoyo a 13 sp

Líneas 382, 890, 930. No existe rol de 13. El vecino es `bodySmall` (12/16).

### G — Esto no es tipografía

| Línea | Hoy | Qué debería ser |
|---|---|---|
| 621 | `Text("▶", 10.sp)` | `Icon` con `MOTIcons` |
| 841 | `Text("≡", 18.sp)` | `Icon` con `MOTIcons` |
| 852 | `Text("+", 24.sp)` | `Icon` con `MOTIcons` |

Son glifos usados como iconos. Además de no ser un problema de escala tipográfica, es un problema
de accesibilidad: TalkBack lee el carácter, no la acción. `Text("≡")` se anuncia como el símbolo,
no como «menú».

## Dos comprobaciones pendientes

**1. Los roles `Emphasized` no están definidos.** `AppTypography` construye `Typography(...)` con
los 15 roles base. Si se empieza a consumir `labelMediumEmphasized` u otro `*Emphasized`, hay que
comprobar si heredan la familia del sistema o caen al valor por defecto de M3 — que no es Fraunces
ni Inter. Si caen, hay que declararlos en `Type.kt` antes de usarlos. **Sin verificar:** requiere
inspeccionar `material3 1.5.0-alpha12`.

**2. La itálica estaba pintando en Black — confirmado y corregido (2026-11-11).**
`designsystem/src/main/res/font/fraunces_italic.ttf`:

| Evidencia | Valor |
|---|---|
| Eje `wght` | min 100 · **default 900** · max 900 |
| `name` ID 4 / 17 | «Fraunces 9pt Black Italic» / «Black Italic» |
| `OS/2.usWeightClass` | 900 |

Sin `FontVariation.weight`, Compose ignora el `FontWeight.Medium` del constructor y pinta el default del eje. Se añadió `frauncesItalicFont()` análoga a `frauncesFont()` (`wght` + `opsz` 9) y se usa en `FrauncesFamily` (todos los pesos) y `HandFamily` (Medium). La pluma y cualquier Fraunces itálica pasan de Black Italic a Medium Italic: es el único cambio visual intencional fuera del grupo D (chip del eyebrow).

Es la misma causa que obligó a enlazar `small-emphasized` a `Static/weight/black` en Figma.

## Orden sugerido

1. Resolver la comprobación 2 (la itálica), porque cambia cómo se ve la pluma hoy.
2. Sustituir el grupo **A** — es mecánico y no cambia nada visualmente.
3. Pasar el grupo **D** a instancias de Eyebrow label.
4. Convertir el grupo **G** en iconos.
5. Decidir la consolidación de **C** y **E**, que es trabajo de diseño, y llevarla a Figma.
6. Cerrar **B** y **F** con la decisión que salga de 5.

Los pasos 2, 3 y 4 se pueden hacer ya. El 5 es el que necesita la pasada de paridad.

## Cierre parcial (2026-11-11)

| Grupo | Estado | Notas |
|---|---|---|
| **0 / itálica** | Cerrado | Confirmado Black por defecto; `frauncesItalicFont()` en `Type.kt`. |
| **A** | Cerrado | `bodyMedium`, `bodySmall`, `labelMedium`, `labelLarge`. Sin literales. |
| **D** | Cerrado | Masthead «DÍA N», etiqueta de evento y fecha del sello → `EyebrowLabel` Small. El componente trae contenedor (`tertiaryFixedDim`) + Inter `labelSmall`; deja de ser texto suelto Fraunces/ocre. Pendiente de paridad visual si diseño quiere una variante sin chip o un tamaño 9/10. |
| **G** | Cerrado | `MOTIcons.PlayArrow` / `Menu` / `Add` con `contentDescription`. |
| **H** | Cerrado | `ScrapbookAlbumPreview` usa `ScrapbookAlbum` con datos de ejemplo. `ScrapbookAlbumDemo` y `ScrapbookCanvasDemo.kt` eliminados (quedaban sin uso). |
| **B, C, E, F** | Abierto | Sin tocar; consolidación Fraunces / pluma / 13sp / tracking de `labelSmall` con Figma delante. |
