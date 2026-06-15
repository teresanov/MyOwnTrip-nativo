# Desarrollo en macOS

Guía mínima al cambiar de máquina (p. ej. PC → Mac). Android Studio incluye JDK y SDK; la terminal no los enlaza sola.

## Requisitos

1. [Android Studio](https://developer.android.com/studio) instalado.
2. SDK en `~/Library/Android/sdk` (se crea al abrir Android Studio la primera vez).

## Una sola vez por Mac

### 1. Java en la terminal

Android Studio trae OpenJDK en:

```
/Applications/Android Studio.app/Contents/jbr/Contents/Home
```

Añade a `~/.zshrc`:

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"
```

Recarga: `source ~/.zshrc` o abre una terminal nueva.

Comprueba: `java -version`

### 2. `local.properties` (por clone del repo)

Archivo **local**, no se commitea (`.gitignore`). En la raíz del proyecto:

```properties
sdk.dir=/Users/TU_USUARIO/Library/Android/sdk
```

Sustituye `TU_USUARIO` por tu nombre de usuario en macOS.

Android Studio suele generarlo al sincronizar Gradle; si falta, créalo a mano.

## Verificar

Desde la raíz del repo:

```bash
./gradlew :app:compileDebugKotlin
```

`BUILD SUCCESSFUL` = entorno listo.

## Cursor / agentes

Si `./gradlew` falla con *Unable to locate a Java Runtime*, el IDE no hereda `JAVA_HOME`. Misma solución: `~/.zshrc` + terminal nueva.

## No hace falta

- Instalar JDK aparte (salvo otros proyectos no Android).
- Homebrew solo para Java en este repo.
