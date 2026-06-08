# Tipografía — MyOwnTrip Nativo

ADR: [002-brand-editorial-m3.md](../decisions/002-brand-editorial-m3.md).  
Código: `app/.../ui/theme/Type.kt` → `Theme.kt`.

## Familias

| Familia | Roles M3 | Pesos | Uso |
|---------|----------|-------|-----|
| **Fraunces** | Display, Headline | 400, 500, 600 | Voz editorial; serif solo aquí |
| **Inter** | Title, Body, Label | 400, 500, 600 | UI y texto funcional |
| Roboto | — | — | **Solo fallback** del sistema |

## Regla serif / sans

- **Display + Headline** → Fraunces.
- **Title, Body, Label** → Inter.
- Prohibido Fraunces en labels de botón, campos, chips o navegación.

## Entrega

- Fuentes **bundled** en `app/src/main/res/font/`.
- Definir `FontFamily` en `Type.kt` y mapear a `Typography` M3.
- No depender de Google Fonts en runtime para la marca.

## Refinamientos

| Ajuste | Detalle |
|--------|---------|
| Pesos | Contenidos (400–600); sin Black/ExtraBold |
| Line-height | Generoso en Body y Headline (legibilidad editorial) |
| Display | Tracking ligeramente negativo |
| Fraunces grande | `opsz` alto en tamaños Display |

## Consumo en UI

```kotlin
MaterialTheme.typography.headlineMedium  // Fraunces
MaterialTheme.typography.titleMedium     // Inter
MaterialTheme.typography.bodyLarge       // Inter
```

- Escalado del sistema: respetar `fontScale`; sin alturas fijas en `dp` para texto (ver `docs/ux/android-compose-ux.md`).

## Pendiente

- [ ] Añadir archivos `.ttf` / variable fonts a `res/font/`
- [ ] Mapeo completo Display/Headline/Title/Body/Label en `Type.kt` al recibir handoff
