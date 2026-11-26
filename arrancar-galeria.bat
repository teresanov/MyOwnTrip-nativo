@echo off
setlocal
rem Usa el Java que trae Android Studio (25), no el Java 15 del sistema.
set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
set "PATH=%JAVA_HOME%\bin;%PATH%"

cd /d "%~dp0"

echo.
echo Arrancando la galeria...
echo Cuando termine de compilar, abre el navegador en:
echo   http://localhost:8080/
echo No cierres esta ventana mientras la uses.
echo.

call "%~dp0gradlew.bat" -p gallery wasmJsBrowserDevelopmentRun
echo.
echo Se ha parado. Si ha fallado, copia el texto rojo y pegalo en el chat.
pause
