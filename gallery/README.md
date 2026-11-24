# Galería Storytale — MyOwnTrip

Canvas vivo del design system: **Compose real** (Skia) en el navegador o en una ventana de escritorio. No es HTML. No es el teléfono.

Storytale 0.0.7 pide Kotlin 2.3; la app Android sigue en 2.2.10. Por eso este proyecto es un Gradle aparte (`gallery/`), no un módulo de `:app`.

## Arrancar

Desde la raíz del repo (mismo wrapper):

```bash
# Navegador (Wasm) — el enlace local para el portfolio
./gradlew -p gallery wasmJsBrowserStoriesDevelopmentRun

# Ventana de escritorio — para compartir pantalla del ordenador
./gradlew -p gallery jvmStoriesRun
```

Hace falta JDK 17+ (el de Android Studio vale). Gradle 9 no arranca con Java 15.

Si una tarea no aparece:

```bash
./gradlew -p gallery tasks --group storytale
```

## Publicar (GitHub Pages)

```bash
./gradlew -p gallery wasmJsBrowserDistribution
```

El estático sale bajo `gallery/build/`. El directorio exacto lo imprime Gradle (`dist/wasmJs/…`). Eso es lo que se sube a Pages: un link de portfolio.

## Qué hay ahora

Stories de `MOTButton`, tonal, outlined, text e icon button, con knobs (label, enabled, dark, reduce motion). El morph 0→20 y el squish son el código de `designsystem`, copiado a `commonMain` hasta unificar el módulo KMP.

## Próximo

- Un solo `:designsystem` KMP (android + jvm + wasmJs) para no duplicar fuentes.
- Fraunces/Inter vía composeResources.
- Stories del resto del kit (chip, dialog, text field).
