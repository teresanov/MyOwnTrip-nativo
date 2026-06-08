# MyOwnTrip — Producto

## Qué es
App nativa Android que actúa como **libreta de viaje digital**. Cubre todo el ciclo: planificación en casa → vuelo de ida → en destino → vuelta → recuerdos y cierre.

## North Star — El cuaderno del viaje (Moleskine)

**Frase-norte:** *Un solo cuaderno del viaje: del plan al recuerdo, en el bolsillo, aunque no haya red.*

La **magia** del producto no es el algoritmo ni la lista infinita de features: es **experiencia sin fricción** para vivir el viaje y **atesorar** lo vivido, como en una **libreta Moleskine** — planning, hints, direcciones, entradas, restaurantes, notas, reseñas propias, dibujos y recuerdos tangibles (hoy: imágenes, audio, PDFs, eventos en contexto por día).

**Prueba de producto:** antes de añadir una pantalla o un requisito, preguntarse: *¿esto huele a cuaderno personal o a software de oficina?* Si aleja del tono *libreta*, hay que justificarlo con research (PP/H) o posponerlo.

**Traducción metáfora → módulos**

| En el cuaderno físico | En MyOwnTrip |
|------------------------|----------------|
| Planning por días, tachones, reordenar | Itinerario por día + drag & drop |
| Pegar entradas, recortes, direcciones | Wallet (PDF/imagen/enlace) + revisión manual (H7) |
| Notas del momento, reseñas propias, garabatos | Diario por día: texto, foto, audio, dibujo |
| Llevar el cuaderno siempre | Offline-first; local como fuente de verdad (H2, H8) |
| Privacidad del papel | Bloqueo, cifrado, control de datos |

**Notion (fuente viva):** [North Star — El cuaderno del viaje](https://www.notion.so/33e6a48d93c881d8b9cce1418607f3d7) dentro de *MyOwnTrip · UX Research*.

## Objetivo
- Uso personal real
- Proyecto de portfolio UX/producto

## Usuario objetivo
Viajero independiente que planifica con criterio propio. Perfiles y pain points detallados (entrevistas sintéticas, reviews, hipótesis): skill **`myowntrip-ux-notion`**.

## Propuesta de valor
**Un solo lugar** (como una libreta) para documentos y reservas (wallet), **itinerario vivo** por día y **diario** con contexto — con **offline-first real** y **fiabilidad** por encima del número de features. La sensación buscada: **anotar y atesorar**, no rellenar un ERP de viaje.

## Diferenciador
Combinación nativa Android: wallet con PDFs + itinerario con drag-and-drop + diario con multimedia, **pensada offline-first** y con **entrada manual verificable** en wallet (evitar el patrón de auto-import poco fiable que aparece en reviews de TripIt y similares).

## Pilares (prioridad MVP según research abril 2026)
1. **Wallet centralizada** — dispersión (PP1) es el problema #1; import **manual** con verificación, no depender de sync mágico desde email como núcleo del MVP.
2. **Offline-first** — lo esencial usable **sin pago** (lección Wanderlog: offline/documentos tras paywall genera abandono y malas reseñas — H9).
3. **Itinerario** — reordenar en destino sin fricción (drag & drop).
4. **Diario** — notas y multimedia con contexto (recuerdos — PP7).
5. **Gastos simples** — registro rápido; si el flujo es largo, se abandona (H5).

**Depriorizado para MVP (Could Have):** estados de restaurante (H3 — no emerge como prioridad en entrevistas sintéticas). **v1.1:** sugerencias con IA solo con datos frescos y mecanismo de corrección (H6).

## Benchmark — competidores analizados
| App | Fortaleza principal | Lección para MyOwnTrip |
|-----|---------------------|-------------------------|
| Wanderlog | Muy completa | Offline/valor en free; IA con datos viejos frustra |
| TripIt | Documentos, offline | Auto-import con errores; sync que falla |
| Day One | Multimedia, privacidad | Referencia diario/cifrado |
| Lambus | Gastos, equipaje | Referencia gastos/listas |
| Notion | Flexibilidad | Sin estructura de viaje |

## Decisiones de diseño vigentes
- **Wallet:** flujo principal = usuario **añade y revisa** entradas (PDF, enlaces, tipos); sin “conectar buzón” como requisito del MVP.
- **Restaurantes:** modelo `SIN_RESERVA → RESERVADO → VISITADO` y campos opcionales de reserva — **implementación MVP opcional** (Could Have); no bloquea el resto del producto.
- **Multimedia** (diario): fotos, audio, dibujos en notas — inspiración Day One.
- **Privacidad:** cifrado, biometría / código — alineado con viajeros que desconfían del servidor puro.
- **Gastos y equipaje:** inspiración Lambus; gastos **muy pocos pasos**; equipaje Should Have.
- **IA ciudad:** solo v1.1 y solo si hay fecha de actualización visible + reporte de errores.

## Métricas de producto (MVP)
Ver criterios revisados en **`myowntrip-ux-notion`** (retención D7, uso offline por viaje, crashes, Wallet, Play Store).
