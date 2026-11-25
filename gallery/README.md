# Galería Storytale — MyOwnTrip

Canvas vivo del design system: **Compose real** (Skia) en el navegador o en una ventana de escritorio. No es HTML. No es el teléfono.

Storytale 0.0.7 pide Kotlin 2.3; la app Android sigue en 2.2.10. Por eso este proyecto es un Gradle aparte (`gallery/`), no un módulo de `:app`.

## Arrancar

En Windows: **doble clic** en [`arrancar-galeria.bat`](../arrancar-galeria.bat) (en la raíz del repo). Ese archivo elige el Java de Android Studio solo; no hace falta tocar nada más. Cuando compile, abre http://localhost:8080/

Desde una terminal, el mismo archivo:

```powershell
.\arrancar-galeria.bat
```

Escritorio (compartir pantalla del PC), con el Java correcto:

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew -p gallery jvmStoriesRun
```

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

Stories de `MOTButton`, tonal, outlined, text e icon button, con knobs (label, enabled, dark, reduce motion). El kit sale de `designsystem/src/commonMain` + `skiaMain` (mismas fuentes que `:app`; Android-only en `androidMain`: `R.font`, dynamic color, `Settings`).

## Próximo

- Fraunces/Inter vía composeResources (paso 2).
- Stories del resto del kit (chip, dialog, text field).
