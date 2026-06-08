# MyOwnTrip — Flujos por Job To Be Done

Documento de trabajo para implementación: **happy path**, **edge cases** y **criterios de éxito** alineados con Notion (`myowntrip-ux-notion`) y con las reglas de desarrollo del proyecto (`.cursor/rules/myowntrip-development.mdc`). Repo activo: **MyOwnTrip_nativo**.

**Propósito de experiencia:** los flujos deben sonar a **cuaderno de viaje** (plan + recuerdo, poca fricción), no a gestión corporativa — ver [North Star en Notion](https://www.notion.so/33e6a48d93c881d8b9cce1418607f3d7) y `.cursor/skills/myowntrip-context/references/product.md`.

**Notas en Notion (lectura):** [JTBD](https://www.notion.so/7a016ea53800407fba8bfadc6f1197fd) · [Pain Points](https://www.notion.so/33d6a48d93c88122955bddbb1352baee) · [Wallet + importación](https://www.notion.so/33e6a48d93c881a0b6bde74c7b68a4b0) · [Hipótesis](https://www.notion.so/33d6a48d93c88170b63fd600083a8995)

**Modelo Room:** ver `.cursor/skills/myowntrip-context/references/data-model.md` (entidades `Trip`, `Day`, `WalletEntry`, `JournalNote`, `Expense`, `Photo`, `Restaurant`).

**One-pager visual:** [MyOwnTrip · JTBD Presentation](https://www.figma.com/board/FgYSO9p8dZfKIjcRnJ8nKZ/MyOwnTrip-%C2%B7-JTBD-Presentation?node-id=0-1) (FigJam).

**Diagramas en Figma (mismo contenido, vista de flujo):** [08 · JTBD — Flujos](https://www.figma.com/design/YRVsgi3oHM5mFlDsOUdS9F/MyOwnTrip-%C2%B7-Project-Definition?node-id=89-2) en *MyOwnTrip · Project Definition*.

---

## Convenciones transversales

| ID | Situación | Comportamiento |
|----|-----------|----------------|
| **EC-OFFLINE** | Sin conexión | Toda acción persiste en local; sync después; indicador sutil, sin bloquear por red. |
| **EC-NO-TRIPS** | Import / acción que exige viaje y no hay ninguno | Ofrecer **Crear viaje** y/o bandeja **Sin asignar** (según alcance MVP). |
| **EC-PARSE-FAIL** | PDF/imagen no clasificable | Selector manual de tipo de entrada + campos editables. |
| **EC-DUPLICATE** | Mismo archivo o misma reserva detectada | Aviso; opciones: ignorar, duplicar con nota, reemplazar. |
| **EC-NO-LOGIN** | Sync remoto sin sesión | Cola local; tras login, drenar pendientes. |
| **EC-LARGE-FILE** | Archivo muy grande (p. ej. >50 MB) | Aviso; no bloquear la app; opción cancelar o guardar solo referencia. |
| **EC-SYNC-FAIL** | Error de red o servidor en sync | Reintento con backoff; estado visible en registro; **no** pérdida local. |
| **EC-KILL** | App cerrada a mitad de flujo | Tras reabrir: datos ya guardados localmente deben estar; operaciones a medias recuperables o descartables con mensaje claro. |

**Orden sugerido de detección de tipo (import / parser):** vuelo → hotel → coche → evento → **other**. Palabras clave de referencia: vuelos (booking ref, IATA); hoteles (check-in, confirmation); coches (rental, pick-up); eventos (ticket, seat).

---

## JTBD 1 — Centralizar (PP1, H1)

**Intención:** Tener vuelos, hoteles y documentos en un solo sitio al planificar.

### Happy path

1. Usuario abre la app → **lista de viajes**.
2. Crea viaje (nombre, destino, fechas) o abre uno existente.
3. Entra a **Wallet** del viaje.
4. Añade entrada: **compartir** PDF/imagen desde otra app, **selector de archivos** o **entrada manual**.
5. Si hay archivo: **preview** + extracción/heurística de tipo y campos **pre-rellenados**.
6. Usuario **revisa y edita** todos los campos (H7: nunca guardar sin confirmación explícita).
7. Confirma → persistencia **local inmediata** (`WalletEntry` + archivo en almacenamiento accesible offline) → feedback (toast/snackbar).
8. Sync en background cuando haya red (sin bloquear UI).

### Edge cases prioritarios

- **EC-NO-TRIPS** al importar desde share target.
- **EC-PARSE-FAIL**, **EC-DUPLICATE**, **EC-LARGE-FILE**.
- **EC-OFFLINE** en todo el flujo (debe completarse hasta el paso 7 sin red).

### Criterios de éxito

- ≥3 entradas Wallet por viaje en uso real (métrica MVP en `myowntrip-ux-notion`).
- Ningún guardado “silencioso” sin pantalla de revisión para import.
- Onboarding o primer uso orientan a Wallet, no solo a itinerario vacío (H1).

---

## JTBD 2 — Sin cobertura (PP2, H2, H8)

**Intención:** Usar lo esencial del viaje sin Wi‑Fi o datos.

### Happy path (transversal)

1. Usuario sin red abre app → ve viajes y contenido ya sincronizado o creado en dispositivo.
2. Puede consultar Wallet (PDFs locales), itinerario, notas y gastos ya guardados.
3. Puede **crear y editar** entidades MVP acordadas (local first).
4. Indicador de estado de red/sync **no intrusivo**; al recuperar red, cola se procesa.

### Edge cases prioritarios

- **EC-OFFLINE** + **EC-SYNC-FAIL**: la UI no muestra error agresivo por “sin internet” en acciones puramente locales.
- **EC-KILL** durante sync: no corromper DB local; reintentar cola al arrancar.

### Criterios de éxito

- ≥1 sesión offline significativa por viaje (MVP).
- Crashes &lt; 0,5% por sesión (H8).
- Freemium: lo esencial offline no queda solo tras paywall (H9 / PP9).

---

## JTBD 3 — Improvisar con orden (PP4)

**Intención:** Reordenar el día o el viaje en destino sin fricción.

### Happy path

1. Usuario abre **detalle del viaje** → **día** concreto.
2. Ve bloques/actividades del día (itinerario).
3. **Arrastra** para reordenar; persistencia local inmediata.
4. Opcional: añadir bloque rápido desde el mismo día.

### Edge cases prioritarios

- **EC-OFFLINE**: reorder y edición locales sin bloqueo.
- Conflictos post-sync: estrategia documentada (p. ej. last-write-wins + aviso si aplica).

### Criterios de éxito

- Reordenar en ≤3–4 interacciones principales desde la vista del día.
- Sin pérdida de orden tras cerrar app (local first).

---

## JTBD 4 — Restaurantes (PP11, H3) — *Could Have*

**Intención:** Saber por sitio si está reservado / visitado / pendiente.

### Happy path

1. Usuario en viaje abre lista de **restaurantes** (o sección dentro del día).
2. Añade nombre (mínimo); opcional dirección, día, notas.
3. Cambia **estado** (p. ej. pendiente / reservado / visitado según enum de producto).
4. Guardado local inmediato.

### Edge cases prioritarios

- **EC-OFFLINE**: igual que arriba.
- Si MVP retrasa este módulo: sin bloquear JTBD 1–3 y 5–6.

### Criterios de éxito

- Validación H3: si se implementa, medir % de usuarios con &gt;3 restaurantes por viaje antes de ampliar alcance.

---

## JTBD 5 — Recordar el viaje (PP7)

**Intención:** Notas, fotos y contexto por día, no dispersos.

### Happy path

1. Usuario abre **día** del viaje → **diario**.
2. Añade nota de texto; opcional foto, audio, geo (según alcance).
3. Ve galería o línea temporal del día.
4. Persistencia local inmediata; media en almacenamiento accesible offline donde aplique.

### Edge cases prioritarios

- **EC-OFFLINE**: captura y lectura sin red.
- Permisos cámara/archivos: flujo claro si deniega.

### Criterios de éxito

- Crear nota en pocos pasos; sin formularios largos (H5 analogía).

---

## JTBD 6 — Controlar gasto (PP6, H5)

**Intención:** Registrar gasto en el momento, sin fricción.

### Happy path

1. Desde viaje o día → **Añadir gasto**.
2. Campos mínimos: importe (+ moneda/concepto según diseño); **un solo obligatorio** si es posible.
3. Confirmar → local inmediato; opcional foto de recibo.
4. Ver listado o resumen por categoría.

### Edge cases prioritarios

- **EC-OFFLINE**: registro completo.
- Importes decimales, cambio de moneda: reglas de validación en UI (no solo al submit del formulario largo).

### Criterios de éxito

- **≤3–4 taps** para el camino principal (H5); medir drop-off por paso.

---

## Share Target (Android)

Aceptar al menos: `application/pdf`, `image/*`, y valorar `message/rfc822` según complejidad. Acciones `SEND` y `SEND_MULTIPLE`. El flujo debe enlazar con JTBD 1 y **EC-NO-TRIPS**.

---

## Checklist rápido por feature

- [ ] JTBD y PP identificados en este doc
- [ ] Happy path implementado primero
- [ ] Edge cases de la tabla cubiertos o explícitamente diferidos con issue
- [ ] Offline y guardado local antes de sync
- [ ] Criterios de éxito comprobables (pasos, métricas, tests)

---

*Última actualización: abril 2026 — alineado con research Notion y reglas de desarrollo del repo.*
