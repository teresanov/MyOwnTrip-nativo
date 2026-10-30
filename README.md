# MyOwnTrip Nativo

App Android offline-first — libreta de viaje digital. Material 3 nativo.

## Repo archivo DS
El design system histórico (Pencil, Figma custom, componentes `MyOwnTrip*`) está en **[MyOwnTrip](https://github.com/teresanov/MyOwnTrip)** (archivo). No usar ese contrato en este proyecto.

## Requisitos
- Android Studio Ladybug+ / JDK 17
- `local.properties` con `sdk.dir`

## Ejecutar
```bash
./gradlew assembleDebug
./gradlew installDebug
```

## MVP v0.1.0
| JTBD | Funcionalidad |
|------|----------------|
| 1 | Viajes, Wallet manual, import share (PDF/imagen) con confirmación H7 |
| 5 | Diario por día |
| 6 | Gastos rápidos |

## Arquitectura
- **UI:** Compose + Material 3 (`MaterialTheme.colorScheme`)
- **Datos:** Room (fuente de verdad local)
- **DI:** Hilt
- **Nav:** Navigation Compose

Módulos Gradle: `:app` (aplicación), `:designsystem` (tema, tokens y marca) y `:catalog` (app de catálogo del DS).

```
app/src/main/java/com/myowntrip/app/
├── data/            # local (Room), repository, cover, share, wallet, demo
├── di/              # módulos Hilt
├── domain/          # model, plan, expenses, wallet, cover
├── platform/        # location y otras APIs del sistema
└── ui/
    ├── components/  # componentes compartidos
    ├── features/    # trips, dayhub, plan, wallet, documents, expenses, journal, places, restaurants
    ├── navigation/
    └── splash/
```

## Pipeline
| Workflow | Cuándo | Qué hace |
|----------|--------|----------|
| [`android.yml`](.github/workflows/android.yml) | push a `main` / PR que toque Android | `testDebugUnitTest` + `assembleDebug` de `:app` y `:catalog` |
| [`deploy-showcase.yml`](.github/workflows/deploy-showcase.yml) | push a `main` / PR que toque `ds-showcase/` | `npm ci` + `vite build`; despliega a GitHub Pages solo en `main` |

En local, lo mismo que CI:
```bash
./gradlew testDebugUnitTest :app:assembleDebug :catalog:assembleDebug
cd ds-showcase && npm ci && npm run build
```

## Documentación
- [Notion — Proyecto](https://www.notion.so/3796a48d93c8819486cfe3a7fd3f624e) · [Notion — Design System](https://www.notion.so/3796a48d93c88168b7dcf9d7e81f9bfa)
- [DS Showcase](https://teresanov.github.io/MyOwnTrip-nativo/) · [código](ds-showcase/)
- [Mapa app (FigJam)](https://www.figma.com/board/YxJIkp5I524S9wLxGxulK1/MyOwnTrip_structure?node-id=0-1) · [One-pager JTBD (FigJam)](https://www.figma.com/board/FgYSO9p8dZfKIjcRnJ8nKZ/MyOwnTrip-%C2%B7-JTBD-Presentation?node-id=0-1) · [Figma Proyecto](https://www.figma.com/design/YRVsgi3oHM5mFlDsOUdS9F/MyOwnTrip-%C2%B7-Project-Definition?node-id=0-1) · [Figma DS](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=55141-14168) (solo librería)
- [JTBD flows](docs/product/jtbd-flows.md)
- [Design System (repo)](docs/design-system/README.md)
- [ADR marca editorial](docs/decisions/002-brand-editorial-m3.md)
- [Colores M3](docs/design-system/color.md)
- [UX runtime](docs/ux/android-compose-ux.md)

## Verificación manual (EC-KILL)
1. Crear viaje
2. Añadir 3 entradas Wallet (1 por share si es posible)
3. Registrar 2 gastos
4. Añadir 2 notas en días distintos
5. Forzar cierre de app → datos persisten

## Tests
```bash
./gradlew test
```
