# MyOwnTrip · DS Docs (prueba con Starlight)

Web de documentación del design system montada con **Astro + Starlight**, como alternativa a
`ds-showcase/`. Las dos conviven: esta no toca nada de la anterior.

## Arrancar

```bash
cd ds-docs
npm install
npm run dev
```

Abre `http://localhost:4321`.

```bash
npm run build     # genera dist/
npm run preview   # sirve dist/
```

## De dónde salen los datos

**No se duplica nada.** Esta web lee los mismos JSON que `ds-showcase`:

| Qué | De dónde |
|---|---|
| Fichas de componente | `../ds-showcase/src/data/components/*.json` |
| Categoría, resumen y orden | `../ds-showcase/src/data/components.json` |
| Valores de color | `../ds-showcase/src/data/tokens.json` |
| Escala tipográfica | `../designsystem/.../theme/Type.kt` (se parsea en cada build) |
| Las fuentes | `../designsystem/src/main/res/font/*.ttf` — los mismos archivos que la app |

La ruta está en una sola constante, arriba de `astro.config.mjs`. Si algún día `ds-showcase`
desaparece, se mueve la carpeta de datos aquí y se cambia esa línea.

## Añadir un componente

Crear su JSON en `ds-showcase/src/data/components/` y registrarlo en `components.json`. La página
y la entrada del menú aparecen solas: no hay que tocar ningún archivo de esta carpeta.

## Cómo está montado

```
astro.config.mjs          menú lateral (lee los JSON para construirlo)
src/
  content.config.ts       el contrato de una ficha, validado en cada build
  lib/
    model.ts              traduce fichas antiguas y nuevas a un solo modelo
    manifest.ts           categoría, resumen y orden desde components.json
    links.ts              traduce los enlaces .html antiguos a rutas nuevas
    vocabulario.mjs       nombres de estados y categorías (compartido)
  components/ficha/       las cinco secciones de una ficha
  pages/
    componentes/[id].astro   una página por JSON
    componentes/index.astro  el índice
    llms.txt.ts              la versión en texto plano para agentes
  content/docs/           las páginas escritas a mano (Markdown)
  styles/custom.css       los ajustes propios sobre el tema
```

### El contrato de una ficha

`src/content.config.ts` define qué campos tiene que traer un JSON. Si falta uno obligatorio,
`npm run build` **falla** y dice en qué archivo. La documentación incompleta no se publica.

Una ficha que todavía no tiene el bloque `usage` se marca en su página como «sin migrar», para que
se vea que está incompleta en lugar de parecer terminada. Hoy solo `button.json` tiene el formato
nuevo.

### El orden de una ficha

Uso → Anatomía y estados → Accesibilidad → Código → Procedencia. Primero cuándo usarlo, después
cómo se implementa.

### `/llms.txt`

La misma documentación en texto plano, generada desde los mismos datos, en
`http://localhost:4321/llms.txt`. Sirve para que un agente la lea entera sin rastrear el HTML.
Como sale de la misma fuente que las páginas, no puede desincronizarse.

## Desplegar

El `base` se controla con una variable de entorno, porque GitHub Pages sirve el sitio en un
subdirectorio:

```bash
DS_DOCS_BASE=/MyOwnTrip-nativo/ npm run build
```

En local no hace falta: por defecto es `/`.

## Qué falta

- Migrar las ocho fichas que siguen en el formato antiguo.
- Generar la tabla de parámetros desde las firmas de Kotlin, en lugar de escribirla a mano.
- Decidir si esta web sustituye a `ds-showcase` o si se descarta.
