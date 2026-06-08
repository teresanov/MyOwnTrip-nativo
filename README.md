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

```
app/src/main/java/com/myowntrip/app/
├── data/local/      # Room entities, DAOs
├── data/repository/
├── domain/model/
└── ui/features/     # trips, wallet, expenses, journal
```

## Documentación
- [JTBD flows](docs/product/jtbd-flows.md)
- [ADR M3](docs/decisions/001-m3-native-ds.md)
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
