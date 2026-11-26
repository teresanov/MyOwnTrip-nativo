# Galería — MyOwnTrip

Canvas vivo del design system: **Compose real** (Skia) en el navegador o en escritorio. App propia (árbol + canvas), no Storytale.

Kotlin 2.3 + Compose Multiplatform aquí; la app Android sigue en 2.2.10. Por eso este proyecto es un Gradle aparte (`gallery/`).

## Arrancar

En Windows: **doble clic** en [`arrancar-galeria.bat`](../arrancar-galeria.bat). Usa el Java de Android Studio. Abre http://localhost:8080/

```powershell
.\arrancar-galeria.bat
```

Escritorio:

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew -p gallery run
```

## Publicar

```bash
./gradlew -p gallery wasmJsBrowserDistribution
```

El estático sale bajo `gallery/build/`. El directorio exacto lo imprime Gradle.

## Catálogo

Un archivo de specimens por componente. **Default** es el caso de producto (Filled Small / Icon Square Small 40×40). Tema y reduce-motion van en la barra, no en cada specimen.
