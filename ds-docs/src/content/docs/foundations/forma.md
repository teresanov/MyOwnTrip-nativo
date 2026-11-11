---
title: Forma
description: Escala de radios de Material 3 en MyOwnTrip y el caso aparte del botón, que se redondea al tocarlo.
sidebar:
  order: 3
---

La escala de radios es la de Material 3, consumida como `MaterialTheme.shapes.*`. No hay una segunda
escala paralela con nombres propios.

## La escala

| En código | En Figma | dp |
|---|---|---:|
| `shapes.extraSmall` | `Corner/Extra-small` | 4 |
| `shapes.small` | `Corner/Small` | 8 |
| `shapes.medium` | `Corner/Medium` | 12 |
| `shapes.large` | `Corner/Large` | 16 |
| `shapes.extraLarge` | `Corner/Extra-large` | 28 |

En producto: las tarjetas van a 12 dp y los chips a 8 dp, fijos. El FAB es circular.

## El botón es la excepción

El botón de MyOwnTrip no tiene un radio fijo: es un rectángulo en reposo y se redondea a 20 dp
cuando se interactúa con él. Es la decisión visual más característica del sistema.

:::note[Fuera de la escala]
El radio del botón **no** sale de `MaterialTheme.shapes.*`. Se calcula en tiempo de ejecución con
`rememberMOTButtonShape()`. Dar forma a un botón con un valor de la escala es un error.
:::

La transición es asimétrica a propósito: entra rápido, para que el dedo note la respuesta, y sale
con un muelle que rebasa ligeramente y asienta. Al pulsar se añade una leve compresión de escala
(*squish*) que solo actúa en ese estado: al pasar el cursor o al recibir el foco, la esquina cambia
pero el botón no se comprime.

Si la persona tiene el movimiento reducido activado, ambas animaciones pasan a ser instantáneas.

El comportamiento completo, estado por estado, está en la [ficha de Button](/componentes/button/).

## Qué consumir en código

`MaterialTheme.shapes.*` para superficies, y `rememberMOTButtonShape()` a través de los wrappers
`MOT*` para botones. La fuente de verdad es `Shapes.kt` en el módulo `designsystem`.
