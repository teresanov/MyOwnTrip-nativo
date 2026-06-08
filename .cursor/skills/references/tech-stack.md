# MyOwnTrip — Stack Técnico

## Plataforma
Android nativo · Kotlin · API mínima: 26 (Android 8)

## Patrón arquitectónico
MVVM + Clean Architecture + Repository pattern + **Offline-first**

**Implicación research (PP2, PP3, H8):** la UI y las operaciones críticas leen/escriben **Room primero**; la nube es sincronización y backup, no condición para usar el viaje. Objetivo de calidad: **crashes &lt; 0,5% por sesión** en MVP (ver `myowntrip-ux-notion`).

## Capas

### UI
- Jetpack Compose — pantallas y componentes
- Material 3 — design system
- Navigation Compose — navegación entre pantallas

### Lógica
- ViewModel — estado de UI y lógica de presentación
- Kotlin Coroutines — operaciones asíncronas sin bloquear la UI
- Flow — streams de datos reactivos
- Hilt (DI) — inyección de dependencias

### Datos locales
- Room — base de datos local (las 7 entidades del modelo de datos)
- DataStore — preferencias de usuario y configuración

### Cloud sync
- Supabase — PostgreSQL, auth según necesidad
- Supabase Storage — fotos y PDFs
- Sincronización **explícita** al reconectar: estrategia de conflictos documentada en código (p. ej. last-write-wins por entidad o preferencia por copia local hasta merge manual) — evitar sensación de “datos desaparecidos” (PP3)

### Media
- Coil — carga y caché de imágenes
- PdfRenderer — visualización de PDFs (Android nativo)
- CameraX — captura de fotos desde la app

## Por qué Supabase sobre Firebase
- Open source
- PostgreSQL real — relaciones entre entidades del modelo de datos
- SDK oficial de Kotlin
- Tier gratuito generoso para desarrollo y portfolio
- Mayor valoración en procesos de selección tech actualmente

## Módulos / pantallas principales
| Módulo | Pantallas | Notas MVP |
|--------|-----------|-----------|
| Trips | Lista, Crear, Detalle | Activación ≥1 viaje primera sesión |
| Itinerary | Por día, Añadir bloque, Drag & drop | Must |
| Wallet | Lista, Visor PDF, **Añadir / confirmar entrada** | Must; sin flujo “solo email sync” |
| Journal | Notas del día, Añadir nota, Galería | Must |
| Restaurants | Lista, Añadir, Cambiar estado | **Could Have** |
| Expenses | Resumen, Añadir gasto, Recibo | Should — pocos pasos |

## Design System (producto)

| Herramienta | Uso |
|-------------|-----|
| **Pencil** | `DS.pen` — iteración de componentes (Pencil-first) |
| **Figma** | [MyOwnTrip · Design System](https://www.figma.com/design/OKK2uhBaitAlF9KTgdFeFL/MyOwnTrip-%C2%B7-Design-System) — biblioteca publicada |
| **Notion** | [MyOwnTrip · UI Design](https://www.notion.so/MyOwnTrip-UI-Design-3426a48d93c8801390b8ffa35655b745) — contexto y decisiones |
| **Showcase** | Matrices doc en Figma + índice `showcaseGroup` en repo |

Pipeline: `docs/design-system/governance/design-pipeline.md`

## Archivo Figma (proyecto)
`MyOwnTrip · Project Definition` — 7 slides: Cover, Project Brief, Travel Lifecycle, Requirements, Benchmark, Architecture, Data Model

## Contexto UX
Investigación, pain points, hipótesis y priorización: skill **`myowntrip-ux-notion`** (sincronizado con Notion).

Flujos por JTBD y edge cases para implementación: **`docs/myowntrip-jtbd-flows.md`** en la raíz del repo Android.
