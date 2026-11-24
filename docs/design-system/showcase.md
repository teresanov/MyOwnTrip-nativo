# Showcase — documentación de componentes

ADR: [003-figma-library-showcase-docs.md](../decisions/003-figma-library-showcase-docs.md).

## Rol

El **showcase externo** es la única fuente de documentación de componentes del DS.

Figma conserva la **librería** (variables + component sets para diseñar pantallas). **No** se crean láminas de documentación, páginas Do/Don't ni matrices de referencia en Figma.

## Qué documenta cada capa

| Capa | Contenido |
|------|-----------|
| **Showcase** | Ficha por componente: propósito, anatomía, variantes, estados, tokens usados, a11y, ejemplos interactivos, changelog |
| **Figma** | Component set publicado, variables M3, instancias para UI de producto |
| **Repo `docs/design-system/`** | Foundations (color, tipo, iconos, governance) · **patrones de producto** en [`patterns/`](patterns/) |
| **Notion DS** | Enlaces al showcase; resumen para negocio |

## Plantilla mínima por componente (showcase)

1. Propósito y cuándo usarlo
2. Variantes y estados (runtime / state layers)
3. Tokens M3 consumidos (`colorScheme`, `typography`)
4. Do / Don't
5. Accesibilidad (TalkBack, error icono+texto si aplica)
6. Enlace al component set en Figma (referencia visual, no doc)
7. Snippet Compose o pantalla de ejemplo
8. Changelog del componente

## Estados de publicación

| Estado | Criterio |
|--------|----------|
| Draft | En Figma o en código; sin ficha showcase |
| In review | Ficha showcase en revisión |
| Ready | Figma publicado + ficha showcase + Compose alineado |
| Deprecated | Marcado en showcase; no usar en pantallas nuevas |

## Canvas vivo (Storytale)

Las demos interactivas **no** son HTML. Viven en [`gallery/`](../../gallery/): Compose Multiplatform + [Storytale](https://aryapreetam.github.io/storytale/) → Wasm (enlace de portfolio) o Desktop (compartir pantalla del PC).

```bash
./gradlew -p gallery wasmJsBrowserDevelopmentRun
./gradlew -p gallery jvmStoriesRun
```

Kotlin 2.3 (exigido por Storytale) vs app Android 2.2: proyectos Gradle separados. Detalle: [`gallery/README.md`](../../gallery/README.md).

## Implementación (ficha escrita)

**Carpeta:** [`ds-showcase/`](../../ds-showcase/) — web estática (Vite) para uso, tokens y a11y. El canvas del componente es `gallery/`.

```bash
cd ds-showcase && npm install && npm run dev
```

| Entorno | URL |
|---------|-----|
| Local | `http://localhost:5173` (Vite dev) |
| Producción | https://teresanov.github.io/MyOwnTrip-nativo/ (GitHub Pages, rama `gh-pages`, workflow `deploy-showcase.yml`) |

Fichas: `ds-showcase/src/data/components/*.json` · plantilla: `button.json`.

## Gates

- Cerrar componente nuevo sin ficha en showcase → **bloqueado**.
- `visualRegression`: captura showcase vs emulador para subset MVP.
