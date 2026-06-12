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

### Media y sensores (capacidades nativas MVP)
- Coil — carga y caché de imágenes
- PdfRenderer — visualización de PDFs (Android nativo)
- **CameraX** — captura de fotos (diario, recibo de gasto)
- **MediaRecorder** / **AudioRecord** — notas de voz en diario
- **Fused Location Provider** (Play Services) o **LocationManager** — lat/long al guardar nota o foto
- **BiometricPrompt** — desbloqueo con huella / rostro / PIN
- **Share Target** (`ImportActivity`) — `SEND` / `SEND_MULTIPLE` para PDF e imágenes → Wallet

Permisos previstos en manifest: `CAMERA`, `RECORD_AUDIO`, `ACCESS_FINE_LOCATION` (y `ACCESS_COARSE_LOCATION`); solicitud en runtime con degradación si el usuario deniega.

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
| Journal | Notas del día, Añadir nota, Galería, **cámara / audio / geo** | Must |
| Restaurants | Lista, Añadir, Cambiar estado | **Could Have** |
| Expenses | Resumen, Añadir gasto, Recibo | Should — pocos pasos |

## Design System (producto)

M3 canónico con identidad editorial. Ver `docs/design-system/README.md`.

| Capa | Fuente |
|------|--------|
| **Color** | Primary gris-azul `#4A5864` · Figma Bridge → repo · ADR 002 |
| **Figma** | [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=55141-14168) — **solo librería** (sin doc en canvas) |
| **Showcase** | `ds-showcase/` — web estática (`npm run dev`) |
| **Compose** | `MaterialTheme.colorScheme` + Fraunces/Inter |
| **Notion** | [Proyecto](https://www.notion.so/3796a48d93c8819486cfe3a7fd3f624e) · [Design System](https://www.notion.so/3796a48d93c88168b7dcf9d7e81f9bfa) |

Pipeline: `Figma (Bridge) → variables.json → Compose` + `Showcase (docs componentes)`.

DS histórico archivado: [MyOwnTrip](https://github.com/teresanov/MyOwnTrip).

## Archivos Figma (proyecto)

| Archivo | Uso |
|---------|-----|
| [JTBD Presentation](https://www.figma.com/board/FgYSO9p8dZfKIjcRnJ8nKZ/MyOwnTrip-%C2%B7-JTBD-Presentation?node-id=0-1) | One-pager visual del proyecto (FigJam) |
| [Project Definition](https://www.figma.com/design/YRVsgi3oHM5mFlDsOUdS9F/MyOwnTrip-%C2%B7-Project-Definition?node-id=0-1) | 7 slides: Brief, Lifecycle, Requirements, Benchmark, Architecture, Data Model |
| [08 · JTBD — Flujos](https://www.figma.com/design/YRVsgi3oHM5mFlDsOUdS9F/MyOwnTrip-%C2%B7-Project-Definition?node-id=89-2) | Diagramas de flujo (mismo archivo) |

## Contexto UX
Investigación, pain points, hipótesis y priorización: skill **`myowntrip-ux-notion`** (sincronizado con Notion).

Flujos por JTBD y edge cases para implementación: **`docs/myowntrip-jtbd-flows.md`** en la raíz del repo Android.
