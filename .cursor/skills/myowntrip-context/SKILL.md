---
name: myowntrip-context
description: Contexto completo del proyecto MyOwnTrip. Úsala siempre que el usuario mencione MyOwnTrip, hable de la app de viajes, pregunte sobre las entidades, el stack, los requisitos o cualquier decisión del proyecto. Carga este contexto antes de responder cualquier pregunta relacionada con el proyecto.
---

# MyOwnTrip — Contexto del Proyecto

App nativa Android de viajes. Proyecto personal + portfolio de UX/producto.
Idioma de trabajo: **español**.

Lee los archivos de referencia según lo que necesites:
- `references/product.md` → qué es la app, **North Star (cuaderno del viaje)**, objetivos, decisiones de producto
- `references/requirements.md` → funcionalidades por prioridad MoSCoW
- `references/data-model.md` → entidades Room y sus relaciones
- `references/tech-stack.md` → arquitectura técnica, stack y pipeline DS (MTB → Figma → Compose)
- **`docs/myowntrip-jtbd-flows.md`** (raíz del repo) → flujos por JTBD, edge cases y criterios de éxito para implementación

Para **persona, dolores, JTBD, hipótesis y métricas de éxito** (resumen sincronizado desde Notion), carga el skill **`myowntrip-ux-notion`**; no hace falta MCP de Notion para el día a día. Las reglas de código alineadas con H2/H5/H7/H8 están en **`.cursor/rules/myowntrip-development.mdc`**.

---

## Estado actual del proyecto

- [x] Project Brief definido
- [x] Benchmark competitivo completado
- [x] Requisitos MoSCoW priorizados
- [x] Modelo de datos definido (7 entidades)
- [x] Presentación en Figma creada (7 slides)
- [x] User flows por JTBD en repo (`docs/myowntrip-jtbd-flows.md`)
- [x] Diagramas de flujo JTBD en Figma (página **08 · JTBD — Flujos**)
- [x] Wireframes / UI por JTBD en Figma ([design-file](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file) — páginas `00`–`06`)
- [x] Navegación Compose alineada a rutas de diseño (day hub, wallet detail, restaurants, EC-NO-TRIPS)
- [ ] Setup Android Studio
- [ ] Desarrollo por features (iteración hi-fi ↔ Compose)

---

## Reglas de trabajo

- Responder siempre en español
- Explicar conceptos técnicos con analogías de diseño UX
- No repetir contexto que ya está en los archivos de referencia
- Priorizar decisiones ya tomadas — no reabrir debates cerrados
- Si falta información, preguntar directamente antes de asumir

---

## Politica de mantenimiento del contexto

- Este skill y sus archivos `references/*.md` son la fuente unica de verdad del proyecto.
- Toda decision nueva debe documentarse en la misma sesion en el archivo correcto.
- Mapeo obligatorio de cambios:
  - producto/UX / North Star -> `references/product.md` y [North Star en Notion](https://www.notion.so/33e6a48d93c881d8b9cce1418607f3d7)
  - alcance/prioridades MoSCoW / capacidades nativas Android -> `references/requirements.md`
  - entidades/campos/relaciones -> `references/data-model.md`
  - arquitectura/librerias/patrones -> `references/tech-stack.md`
  - investigación UX (personas, pain points, hipótesis) -> actualizar primero `myowntrip-ux-notion/SKILL.md` (Notion) y después los `references/*.md` afectados
- Si una decision cambia algo anterior, no se anade duplicado: se reemplaza el criterio obsoleto y se deja la version vigente.
- Antes de cerrar sesion, ejecutar cierre rapido de 2 minutos:
  1) que se decidio
  2) donde quedo documentado
  3) que impacto tiene en el siguiente paso
