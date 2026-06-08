# MyOwnTrip · DS Showcase

Web estática de documentación del Design System (M3 nativo editorial).

**Figma** = librería visual (variables + component sets).  
**Este showcase** = fichas de componentes, foundations, Do/Don't, a11y.

## Arranque local

```bash
cd ds-showcase
npm install
npm run dev
```

Abre la URL de Vite (suele ser `http://localhost:5173`).

## Build estático

```bash
npm run build
npm run preview
```

Salida en `dist/`.

**Producción:** https://teresanov.github.io/MyOwnTrip-nativo/ (rama `gh-pages`; despliegue automático en push a `main` vía GitHub Actions).

## Estructura

| Ruta | Contenido |
|------|-----------|
| `/` | Overview |
| `/color.html` | Roles M3, seeds, state layers |
| `/typography.html` | Fraunces + Inter |
| `/components.html` | Índice de componentes |
| `/components/button.html` | Ficha Button (plantilla) |

## Añadir un componente

1. Crear `src/data/components/<id>.json` (copiar `button.json`).
2. Registrar en `src/data/components.json`.
3. Crear `components/<id>.html` + `src/pages/<id>.js`.
4. Añadir entrada en `vite.config.js` → `rollupOptions.input`.

## Datos

- `src/data/tokens.json` — alinear con `docs/design-system/color.md` y MTB.
- Contrato: `docs/design-system/showcase.md` · ADR `003-figma-library-showcase-docs.md`.
