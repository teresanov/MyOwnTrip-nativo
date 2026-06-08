---
name: myowntrip-ux-notion
description: Resúmenes de investigación UX de MyOwnTrip desde Notion — persona, pain points (reviews abril 2026), JTBD, entrevistas sintéticas basadas en reseñas reales, hipótesis revisadas y criterios MVP. Usar al diseñar o priorizar UX, copy, flujos, MVP o freemium; complementa myowntrip-context sin MCP de Notion.
---

# MyOwnTrip — UX Research (resumen desde Notion)

**Hub Notion producto:** [MyOwnTrip · Proyecto](https://www.notion.so/3796a48d93c8819486cfe3a7fd3f624e) · **Research:** [MyOwnTrip · UX Research](https://www.notion.so/33d6a48d93c881b7be98c0bc33bbdcf1). Copia resumida para el agente; **si hay discrepancia, manda Notion**.

**Sincronización skill:** abril 2026 (pain points, hipótesis, entrevistas sintéticas v2, priorización MVP).

**North Star (cuaderno del viaje / Moleskine):** [North Star — El cuaderno del viaje](https://www.notion.so/33e6a48d93c881d8b9cce1418607f3d7).

**Benchmark competitivo:** [Benchmark competitivo (sp)](https://www.notion.so/3796a48d93c8816fb31ec55f4cf4b23e) · slide en [Figma Project Definition](https://www.figma.com/design/YRVsgi3oHM5mFlDsOUdS9F/MyOwnTrip-%C2%B7-Project-Definition?node-id=0-1).

**Página Notion MoSCoW + MVP 1.0:** [MVP 1.0 — Features (MoSCoW)](https://www.notion.so/33e6a48d93c8813ea599f53ce21ff2b0) (hija de [MyOwnTrip · UX Research](https://www.notion.so/33d6a48d93c881b7be98c0bc33bbdcf1)).

**Relación:** Cargar con el skill `myowntrip-context` para producto técnico y modelo de datos.

**Implementación:** Flujos detallados (happy path, edge cases EC-*, criterios por JTBD) en el repo: **`docs/myowntrip-jtbd-flows.md`**. Reglas de desarrollo (offline-first, H7/H8, checklist): **`.cursor/rules/myowntrip-development.mdc`**.

---

## User persona (Teresa / Marcos)

- **Perfil:** 28–40 años, profesional urbano, ingresos medios-altos; 2–4 viajes/año (alguno largo); viajes exploratorios e independientes (no paquetes).
- **Dispositivos:** Android principal; PC para planificar. Maps, Instagram, TripAdvisor, notas.
- **Antes / durante / después:** dispersión multi-app; búsqueda urgente de PDFs y confirmaciones; gastos sin registro; deseo de recuerdo organizado.
- **Cita:** *«Tengo los vuelos en el correo, los restaurantes en Google Maps, las notas en el móvil y el itinerario en un Word. Y aun así siempre pierdo algo.»*
- **Motivaciones:** organización sin agobio; un gesto desde el móvil; revivir el viaje; **control sin depender del Wi‑Fi**.

**Complemento:** Cinco perfiles de [entrevistas sintéticas](https://www.notion.so/33e6a48d93c8811d84fcf647f7dca31e) (Laura, Marcos, Sofía, Daniel, Carmen) afinan matices (auto-import, paywall offline, complejidad, onboarding, privacidad).

_Páginas:_ [User Persona (sp)](https://www.notion.so/33d6a48d93c88198a4e5f4905a213d36) · [User Persona (en)](https://www.notion.so/1346a48d93c88250b9140114dd5dbcc5)

---

## Entrevistas de usuario sintéticas (v2, abril 2026)

**Qué son:** Perfiles y fragmentos reconstruidos a partir de **citas de reseñas reales** (Wanderlog, TripIt, Day One, Lambus; Trustpilot, JustUseApp, G2, Reddit r/solotravel / r/digitalnomad, etc.). Cada entrevista en Notion enlaza evidencia verificable. Contradicciones con hipótesis iniciales están documentadas.

**Metodología:** v1 = suposiciones; **v2 = texto de reviews** como fuente primaria.

| # | Perfil | Pain validado (resumen) |
|---|--------|-------------------------|
| 1 | Laura, 34 — profesional | **Auto-import erróneo** (experiencia tipo TripIt) |
| 2 | Marcos, 28 — mochilero | **Offline tras paywall**; no pagará ~50 USD/año |
| 3 | Sofía, 41 — organizada | **Sobrecarga / overkill** → abandona la app |
| 4 | Daniel, 26 — espontáneo | **Onboarding con fricción** → cierra si pide demasiado |
| 5 | Carmen, 38 — experiencial | **Fiabilidad y privacidad**; desconfianza servidor |

**Hallazgos transversales (sí aparece en todas las entrevistas):** offline útil y **gratis**; **fiabilidad > número de features**; **control manual** (desconfianza auto-import); **simplicidad**.

**No priorizado espontáneamente en entrevistas:** estados de restaurante; IA (solo Carmen, con reservas por datos viejos); lista de equipaje.

**Mapeo rápido research → hipótesis:** H1 dispersión ✅ · H2 offline ✅ · H5 gasto >3 taps ✅ · H3 restaurantes ⚠️ Could Have · H6 IA ⚠️ riesgo si datos obsoletos · **H7** import manual vs auto-import 🆕 · **H8** fiabilidad > features 🆕.

_Página índice:_ [Entrevistas de Usuario Sintéticas](https://www.notion.so/33e6a48d93c8811d84fcf647f7dca31e)  
_Detalle:_ [E1 Laura](https://www.notion.so/33e6a48d93c881e5a59ae5e8b81dc357) · [E2 Marcos](https://www.notion.so/33e6a48d93c8810ab0bcce075d03caf3) · [E3 Sofía](https://www.notion.so/33e6a48d93c88158b0a0d274500e1801) · [E4 Daniel](https://www.notion.so/33e6a48d93c8812a969cc3a2cd621f83) · [E5 Carmen](https://www.notion.so/33e6a48d93c881c0b279d40cedc9fb5e)

---

## Pain points (sp) — numeración abril 2026

Actualizados con reviews reales (Wanderlog, TripIt, Lambus). En Notion hay citas textuales por PP.

**Críticos**

| ID | Problema | Respuesta MyOwnTrip |
|----|----------|---------------------|
| PP1 | Información dispersa (correo, Maps, notas, WhatsApp, PDFs, capturas) | Wallet centralizada |
| PP2 | Sin conexión las apps no sirven | Offline-first + sync cuando hay red |
| PP3 | **Sync que falla** y pérdida de datos cuando más importa | Datos locales como fuente de verdad; sync tipo push claro |

**Importantes**

| ID | Problema | Respuesta MyOwnTrip |
|----|----------|---------------------|
| PP4 | Itinerario rígido; hay que reordenar en destino | Itinerario por día, **drag & drop** |
| PP5 | **Auto-import de email poco fiable** | Import **manual** con verificación visual |
| PP6 | Gastos invisibles hasta el extracto | Gastos/recibos por categoría en el viaje |
| PP7 | Recuerdos sin narrativa (fotos/notas sueltas) | Diario con notas, fotos, audio, geo por día |
| PP8 | **IA con información desactualizada** | v1.1: fecha de actualización + reporte de error |

**Secundarios**

| ID | Problema | Respuesta MyOwnTrip |
|----|----------|---------------------|
| PP9 | **Lo esencial tras paywall** (offline, documentos) | Freemium con valor real en free |
| PP10 | Export a Maps **rompe orden y contexto** | Integración que preserve orden y metadata |
| PP11 | Restaurantes sin contexto (reserva / visitado) | Estados S/R/V (**Could Have**) |
| PP12 | Equipaje | Lista reutilizable (**Should Have**) |
| PP13 | Planes auténticos | IA ciudad v1.1 |

_Página:_ [Pain Points (sp)](https://www.notion.so/33d6a48d93c88122955bddbb1352baee)

---

## Jobs To Be Done (sp)

Definición en Notion sin cambiar; **alineación de PP** con la tabla anterior (la página JTBD en Notion puede seguir citando IDs viejos):

1. **Centralizar** — todo en un sitio al planificar. → **PP1**  
2. **Sin cobertura** — acceso offline en momentos críticos. → **PP2**  
3. **Improvisar con orden** — reordenar itinerario. → **PP4**  
4. **Restaurantes** — estado por sitio. → **PP11**  
5. **Recordar el viaje** — capturar con contexto. → **PP7**  
6. **Controlar gasto** — registrar al momento. → **PP6**  

_Página:_ [Jobs To Be Done (sp)](https://www.notion.so/33d6a48d93c881569365d26a4d145156)

---

## Hipótesis a probar (revisión abril 2026)

_Página:_ [Hipótesis iniciales (sp)](https://www.notion.so/33d6a48d93c88170b63fd600083a8995)

### Validadas con evidencia externa (pendiente validación interna)

| ID | Resumen | Validar en producto |
|----|---------|---------------------|
| **H1** | Dispersión es el problema #1 para cambiar de herramienta | ¿Onboarding usa más Wallet / entradas que solo itinerario? |
| **H2** | Offline es diferenciador y retención, sobre todo internacional | Correlación destino internacional ↔ sesiones sin red |
| **H5** | Gasto con **>3 taps** → abandono | Drop-off por paso en “añadir gasto” |

### Cuestionadas / replanteadas

| ID | Qué cambia |
|----|------------|
| **H3** | Estados restaurante: **no** pain prioritario en reviews; útil solo para planificadores intensos; **Could Have**. Validar: ¿>20% añade >3 restaurantes por viaje? |
| **H6** | IA: riesgo si datos viejos (reviews Wanderlog). Solo valor si **fecha de actualización** visible y **corrección**. Medir reportes de error en v1.1 |

### Nuevas (datos reales)

| ID | Hipótesis | Cómo validar |
|----|-----------|--------------|
| **H7** | Usuarios prefieren **import manual** con control vs sync automático de correo | % onboarding “manual” vs “conectar email” |
| **H8** | **Menos features pero estables** > muchas con bugs | ¿Quejas en reviews: falta feature vs no funciona / crash? |
| **H9** | Freemium: si lo esencial (offline, docs) es solo pago → abandono y malas reseñas | Conversión free→premium vs reseñas negativas |

**Nota:** La revisión abril 2026 no reescribe la hipótesis “diario en destino vs planificación”; sigue siendo útil medir con timestamps de notas de diario vs fechas del viaje.

---

## Criterios de éxito MVP (revisados)

| Métrica | Objetivo | Notas |
|---------|----------|--------|
| Retención día 7 | > 40% | |
| Viajes creados | ≥ 1 en primera sesión | |
| Entradas Wallet / viaje | ≥ 3 | Centralización |
| Uso offline | ≥ 1 sesión / viaje | Valida H2 |
| Crashes / sesión | < 0,5% | Valida H8 |
| Play Store | ≥ 4,2 | |

---

## Priorización MVP (según datos)

**Must Have:** Wallet centralizada (PP1) · Offline-first (PP2) · Itinerario drag & drop (PP4) · Import manual verificable (H7 / PP5)

**Should Have:** Gastos simples (PP6, H5) · Diario integrado (PP7)

**Could Have:** Estados restaurante (H3) · Lista equipaje (PP12)

**Won’t Have en MVP:** Sugerencias IA sin datos frescos y mecanismo de corrección (H6)

---

## Mantenimiento

Tras cambios en Notion (persona, PP, entrevistas, hipótesis, métricas): actualizar este `SKILL.md` y la fecha de sincronización del encabezado.

Si cambian definiciones de JTBD o criterios de flujo acordados en producto: actualizar también **`docs/myowntrip-jtbd-flows.md`** para que código y research no diverjan.

Si se redefine el **North Star** (metáfora cuaderno / propósito emocional): actualizar la [página Notion](https://www.notion.so/33e6a48d93c881d8b9cce1418607f3d7) y la sección homónima en **`references/product.md`**.
