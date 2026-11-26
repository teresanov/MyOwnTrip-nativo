# Demos vivas — MyOwnTrip DS

Los ejemplos interactivos del showcase (`ds-showcase/`) son **componentes reales** de `:designsystem`, compilados de Compose a WebAssembly e incrustados en cada ficha. Esta carpeta es el taller donde se definen y se compilan. No es una web aparte: la documentación se lee solo en el showcase.

Kotlin 2.3 + Compose Multiplatform aquí; la app Android sigue en 2.2.10. Por eso es un proyecto Gradle separado.

## Cómo funciona

1. La ficha declara un hueco: `<div data-mot-specimen="button/playground" data-p-variant="filled">` (lo genera `liveHostHtml()` en `ds-showcase/src/lib/live-specimen.js`).
2. Al llegar a la demo, la página carga `live/demos.js`. Kotlin busca los huecos, carga Inter y Fraunces (los `.ttf` de `designsystem`) y monta en cada uno el specimen con `ComposeViewport`.
3. Los controles HTML de la ficha cambian los `data-p-*` y lanzan el evento `mot:props`; Compose recompone.

## Specimens

- `embed/LiveSpecimens.kt`: demos con parámetros desde la ficha (p. ej. `button/playground`).
- `specimens/*.kt` + `catalog/Catalog.kt`: casos fijos (`button/sizes`, `icon-button/round`…). Cualquiera se puede incrustar por su id.

## Compilar y llevar al showcase

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew -p live-demos wasmJsBrowserDistribution
cd ds-showcase; npm run live
```
