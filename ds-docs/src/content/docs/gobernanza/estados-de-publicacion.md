---
title: Estados de publicación
description: Qué significa que un componente esté en borrador, en revisión, listo u obsoleto, y qué hace falta para cerrarlo.
sidebar:
  order: 1
---

Cada componente lleva un estado visible en el menú lateral y en su ficha. Sirve para saber si te
puedes apoyar en él o si todavía puede cambiar.

| Estado | Qué significa |
|---|---|
| **Borrador** | Existe en Figma o en código, pero no tiene ficha aquí. Puede cambiar sin avisar. |
| **En revisión** | Tiene ficha, y la ficha está en revisión. Usable, con reservas. |
| **Listo** | Publicado en Figma, documentado aquí y alineado con el código. |
| **Obsoleto** | No usar en pantallas nuevas. Se mantiene por las que ya lo usan. |

## Qué hace falta para llegar a «Listo»

Un componente se cierra cuando se cumplen las cuatro cosas, en este orden:

1. **Construido en código**, en `designsystem/` o en `app/`, con su preview.
2. **Publicado en la librería de Figma**, como component set con sus variables.
3. **Documentado aquí**, con la ficha completa.
4. **Registrado en el mapa de equivalencias** entre Figma y Compose, en
   `docs/design-system/figma-compose-bindings.md`.

El orden importa: el código va primero porque es la fuente de verdad del comportamiento. Figma es
el espejo visual, y esta web, la documentación.

## Qué tiene que traer una ficha completa

Una ficha no está completa si le falta alguno de estos bloques:

- **Uso** — para qué sirve, cuándo usarlo, cuándo no, y qué variante elegir.
- **Anatomía y estados** — las partes que lo componen y cómo se comporta al interactuar.
- **Accesibilidad** — área táctil, foco, contraste, lectura con TalkBack.
- **Código** — el composable, sus parámetros y los tokens que consume.
- **Procedencia** — de dónde sale, por qué existe, qué decisión lo respalda y qué ha cambiado.

Una ficha a la que le falta el bloque de uso aparece marcada como sin migrar: se ve que está
incompleta en lugar de parecer terminada.

## Lo que no se documenta en Figma

En el archivo de Figma **no** se crean láminas de documentación, páginas de Do/Don't ni matrices de
referencia. Figma guarda la librería; la documentación vive aquí. Mantener las dos cosas en dos
sitios es trabajo duplicado que se desincroniza.

La decisión está registrada en `docs/decisions/003-figma-library-showcase-docs.md`.
