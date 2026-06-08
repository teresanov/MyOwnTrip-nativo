# Repositorios de referencia (Material / Compose)

Carpeta **solo para consulta** al diseñar o implementar componentes canónicos en **Jetpack Compose**. No forman parte del build de MyOwnTrip: no añadas estos proyectos como módulos en `settings.gradle.kts`.

**Cursor / IA:** al tocar componentes o su documentación en el repo, aplica la regla **`.cursor/rules/components/material-reference-refs.mdc`** (búsqueda en estos clones y trazabilidad en la doc). Resumen también en **`.cursor/instructions/codebase-context.md`**.

## Lista mínima recomendada

| Carpeta local (convención) | Origen | Para qué sirve |
|----------------------------|--------|----------------|
| `material-components-android` | [material-components/material-components-android](https://github.com/material-components/material-components-android) | Comportamiento, estados, a11y y detalle de producto en la **capa View**; traducir patrones a Compose, no copiar implementación. |
| `compose-m3-expressive-catalog` | [emertozd/Compose-Material-3-Expressive-Catalog](https://github.com/emertozd/Compose-Material-3-Expressive-Catalog) | **Samples Compose** y catálogo Material 3 “expressive”; terceros — puede desfasarse del AndroidX oficial. |
| (sin clone) | [AndroidX — Compose Material3](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/) | Código **oficial** de `androidx.compose.material3`; en IDE suele bastar “Download sources” sobre las dependencias del BOM. |

Opcional si necesitás el monorepo completo (pesado): clonar [androidx/androidx](https://github.com/androidx/androidx) fuera de este repo o en otra ruta; no es necesario para el día a día.

## Clonar aquí (PowerShell)

Desde la raíz del repo MyOwnTrip:

```powershell
cd refs
git clone --depth 1 https://github.com/material-components/material-components-android.git material-components-android
git clone --depth 1 https://github.com/emertozd/Compose-Material-3-Expressive-Catalog.git compose-m3-expressive-catalog
cd ..
```

Los nombres de carpeta deben coincidir con la tabla para que agentes y personas encuentren lo mismo.

## Reglas de uso

1. **Versiones**: las refs pueden usar otras versiones de Compose/Material3 que el BOM de la app; mirá patrones y API, no copiéis números de versión sin alinearlos con `gradle/libs.versions.toml` (o equivalente).
2. **Tokens**: la UI de MyOwnTrip consume **semánticos** del DS (Figma → tema); los catálogos usan valores por defecto de Material.
3. **Licencias**: si reutilizáis fragmentos sustanciales, respetad la licencia de cada repo y documentad la procedencia si aplica.

## Git

Los clones bajo `refs/` están ignorados en `.gitignore`; **solo** se versiona este `README.md`.
