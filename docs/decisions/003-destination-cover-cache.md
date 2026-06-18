# ADR 003 — Portadas de destino (ciudad)

**Estado:** aceptado  
**Fecha:** 2026-06-18

## Contexto

Las cards de viaje muestran una imagen de portada. No es viable empaquetar fotos de todos los destinos del mundo en el APK.

## Decisión

### Producción (runtime)

1. **Fetch bajo demanda** al crear viaje o al listar viajes sin portada.
2. **Proveedor inicial:** Wikimedia Commons (sin API key, User-Agent identificable).
3. **Caché en disco** en `files/destination-covers/{clave-normalizada}.jpg` — compartida entre viajes al mismo destino.
4. **Persistencia:** ruta local en `Trip.coverPhoto` (Room). La UI solo lee ese campo.
5. **Offline-first:** con red se descarga una vez; sin red se muestra la imagen cacheada o iniciales del destino.

Capa: `DestinationCoverRepository` → `WikimediaDestinationCoverRemoteDataSource` + `DestinationCoverFileStore`.

### MVP / diseño

- Drawables `home_trip_*.jpg` y `PreviewCityCovers` **solo** para `@Preview` y revisión Figma.
- No se usan en pantallas con datos reales.

## Alternativas consideradas

| Opción | Por qué no (ahora) |
|--------|---------------------|
| APK con miles de ciudades | Tamaño, mantenimiento, imposible escalar |
| Solo URL remota sin cache | Rompe offline-first |
| Google Places / Unsplash API | API key, coste, términos — candidato post-MVP |
| Supabase Storage catálogo global | Requiere backend curado — fase sync |

## Evolución post-MVP

- Sustituir o complementar Wikimedia con proveedor con mejor relevancia (Places, CDN propio).
- Subir `coverPhoto` a Supabase Storage en sync y usar copia local como source of truth.
- Permitir foto personal del usuario (cámara/galería) con prioridad sobre portada automática.

## Referencias

- `app/.../domain/cover/DestinationCoverRepository.kt`
- `app/.../data/cover/`
- `Trip.coverPhoto` en modelo de datos
