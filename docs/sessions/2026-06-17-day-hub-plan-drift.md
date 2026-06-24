# Sesión 2026-06-17 (tarde) — Day Hub, drift Plan↔Wallet y cuadrante horario

> **Notion:** [Sesión 2026-06-17 (tarde) — Day Hub, drift Plan↔Wallet y cuadrante](https://www.notion.so/3896a48d93c8817c93c1c20ea6ae85b6)  
> **Repo:** [MyOwnTrip-nativo](https://github.com/teresanov/MyOwnTrip-nativo)

## Resumen ejecutivo

Caso de producto: el **documento en Wallet** (PDF del vuelo, reserva) puede quedar **desactualizado**, pero el viajero necesita **corregir cuándo ocurre en su día** sin editar Wallet. Implementamos **dos capas de verdad** (documento vs plan), **cuadrante horario** en Day Hub, **bottom sheet «Corregir horario»** para actividades vinculadas a Wallet, y **señales visuales + accesibles** de drift en Wallet lista y detalle.

---

## Problema de usuario (JTBD 3 + JTBD 1)

| Pain | Comportamiento deseado |
|------|------------------------|
| El vuelo cambió; el PDF no | Corregir hora/día en el **plan**; Wallet intacto |
| No sé si la hora es del billete o mía | Etiqueta clara en cuadrante y en Wallet |
| Arrastrar un vuelo «rompe» el sentido del documento | Sin drag en bloques vinculados; edición explícita |
| Offline en destino | Todo local; sin bloqueo de red |

---

## Decisiones de producto y UX

### Dos fuentes de verdad

| Capa | Qué es | ¿Editable desde plan? |
|------|--------|------------------------|
| **Wallet** | Archivo / metadatos del documento | No (desde esta feature) |
| **Plan** | Dónde y cuándo vive la actividad en el viaje | Sí — override de hora y/o día |

**Drift** = el plan diverge del documento (hora distinta y/o día distinto al de la fecha del documento).

### Cuadrante horario (Day Hub · tab Plan)

| Tema | Decisión | Por qué |
|------|----------|---------|
| Vista | Rejilla vertical estilo agenda (6:00–23:00) | Reordenar «en el tiempo», no solo lista |
| Actividades **manuales** | Long press + **drag** para cambiar hora | Patrón familiar (Calendar, Maps timeline) |
| Actividades **vinculadas a Wallet** | **Sin drag** nunca | Evitar corrección accidental; el gesto de drag confunde con «mover el vuelo» |
| Edición Wallet en cuadrante | **Bottom sheet** «Corregir horario» | Contexto + día + hora + copy que explica que Wallet no cambia |
| Vías al sheet (redundancia) | **Toque**, **pulsación larga** y **menú ⋮ → Corregir horario** | No depender de un solo gesto; política repo: alternativa al drag |
| Copy menú | «Corregir horario» (no «Actualizar en el plan») | El usuario ya está en el plan; el verbo es corregir respecto al documento |
| Copy sheet | Título «Corregir horario» · CTA «Guardar cambio» · dismiss «Usar hora del documento» | Acción clara + salida segura sin cambios |
| Hint cuadrante | Si hay Wallet: *«Mantén pulsado… o usa el menú ⋮…»* | Descubrimiento del gesto que antes era drag |
| Card en cuadrante | **Título primero** (1 línea) · meta `09:15 · Del documento` en una línea | Evitar clipping cuando aparece etiqueta de drift |
| Altura mínima card Wallet | 68 dp (vs 44 dp manual) | Legibilidad con dos líneas de texto |
| Borde card | `tertiary` si vinculada a Wallet | Señal visual de «anclada a documento» |
| Etiquetas en card | «Del documento» / «Actualizada en el plan» | Metadato de procedencia de la hora mostrada |
| Actividades manuales | Menú: Editar hora (dialog) + Mover a otro día (dialog) | Flujo ligero sin sheet |

### Wallet — señal de drift

| Superficie | Señal | Color / token |
|------------|-------|----------------|
| **Lista** | Borde izquierdo 4 dp + dot 10 dp en icono leading | `LocalExtendedColors.warning` |
| **Lista** | Sufijo supporting: `· Actualizada en el plan · Día N · HH:mm` | `warning` en parte drift |
| **Lista offline** | Opacidad 0,72 **solo** sin drift | Con drift: fila a opacidad plena (prioridad aviso) |
| **Detalle** | Chip `PlanPlacementSourceChip` | warning 14% container, borde 35% |
| **Navegación** | Chip / enlace al día del plan cuando hay drift | Resolver compartido dominio |

### Viaje pasado (`Past`)

| Área | Comportamiento |
|------|----------------|
| Day Hub Plan | Solo lectura: sin drag, sin FAB, sin sheet |
| Plan tab | Copy «Así quedó tu plan»; CTA recuerdos / documentos |
| Wallet | Lista única (archivados incluidos); sin chips ni swipe |

---

## Decisiones de accesibilidad (TalkBack)

| Elemento | Implementación |
|----------|----------------|
| Fila Wallet con drift | `contentDescription` incluye frase de drift (`accessibilityDriftPhrase`) |
| Dot warning en icono | `invisibleToUser()` — decorativo; no duplicar en árbol |
| Card cuadrante | `contentDescription`: título + hora + etiqueta fuente |
| Bloque Wallet en cuadrante | Custom action **«Corregir horario»** (sin acciones de ±15 min) |
| Bloque manual | Custom actions: ±15 min, Editar hora |
| Errores / drift | Nunca solo color: icono/dot + texto en supporting o chip |
| Semantics `customActions` | Definidas **fuera** de bloques `semantics {}` anidados que lean `customActions` en runtime (crash Day Hub corregido) |

Checklist: `docs/ux/android-compose-ux.md` — gate swipe/drag con alternativa por tap/menú ✓

---

## Decisiones técnicas (resumen)

| Pieza | Ubicación |
|-------|-----------|
| Drift dominio | `PlanPlacementDrift.kt` — `hasPlanTimeDrift`, `hasPlanDayDrift`, `resolveWalletPlanPlacement()` |
| Hora efectiva en cuadrante | `effectivePlanTime()` respeta override en plan |
| Recálculo día | `recalculateDaySchedule()` preserva override con drift |
| Mover día + hora | `ItineraryRepository.moveBlockToDay()` |
| Layouts timeline | `DayPlanScheduleLogic.kt` |
| UI cuadrante | `DayPlanSchedule.kt` |
| Sheet corrección | `UpdateDocumentPlanSheet.kt` |
| Dialog mover (solo manual) | `MoveBlockDayDialog.kt` |
| Chip detalle | `PlanPlacementSourceChip.kt` |
| Icono lista drift | `WalletPlanDriftLeadingIcon.kt` |

### Rutas

- `day_hub/{tripId}/{dayId}?tab=plan|journal` — Day Hub (cuadrante en tab Plan)
- Plan → «Reordenar día» → Day Hub

### Tests añadidos

- `PlanPlacementDriftTest.kt`
- `DayPlanScheduleLogicTest.kt`
- Ampliación `PlanPlacementLogicTest.kt`

---

## Componentes Compose nuevos / relevantes

| Componente | Ruta |
|------------|------|
| DayPlanSchedule | `ui/features/plan/DayPlanSchedule.kt` |
| UpdateDocumentPlanSheet | `ui/features/plan/UpdateDocumentPlanSheet.kt` |
| MoveBlockDayDialog | `ui/features/plan/MoveBlockDayDialog.kt` |
| PlanPlacementSourceChip | `ui/features/plan/PlanPlacementSourceChip.kt` |
| WalletPlanDriftLeadingIcon | `ui/features/wallet/WalletPlanDriftLeadingIcon.kt` |

---

## Opciones descartadas

| Opción | Motivo |
|--------|--------|
| Snackbar + «Cambiar hora» tras long press en documento | Acción no cableada; UX frustrante |
| AlertDialog para corrección Wallet | Poco contexto; sin explicación documento vs plan |
| Drag en documento hasta primer override | Usuario no distingue; guardaba hora sin intención |
| «Actualizar en el plan» en menú | Redundante estando en plan |
| Editar Wallet al corregir plan | Rompe modelo offline-first / archivo inmutable |

---

## Pendiente / deuda

- [ ] Figma: cuadrante Day Hub + sheet «Corregir horario» + estados drift Wallet
- [ ] Showcase ficha patrón plan-drift (opcional)
- [ ] Test de UI Compose DayPlanSchedule (opcional)
- [ ] Verificar copy «Actualizada en el plan» en card vs «Hora corregida» (más corto) en iteración

---

## Enlaces

- JTBD flows: `docs/product/jtbd-flows.md`
- UX Compose: `docs/ux/android-compose-ux.md`
- Color warning: `docs/design-system/color.md` · `LocalExtendedColors`
- Sesión mañana (Home DS): `docs/sessions/2026-06-17-home-ds-figma.md`
- [MyOwnTrip · Proyecto (Notion)](https://www.notion.so/3796a48d93c8819486cfe3a7fd3f624e)
