---
name: tokens-consumidos
description: Añade una estimación de tokens consumidos al final de cada respuesta. Úsalo cuando el usuario pida medir, estimar o reportar consumo de tokens, o cuando indique un formato de salida con "Tokens aprox por esta tarea".
---

# Tokens consumidos

## Objetivo

Al final de cada respuesta (después de resolver la petición), agrega una línea con una estimación aproximada de tokens consumidos en esa tarea.

## Regla de cálculo

Usa esta heurística:

- `tokens_entrada_aprox = caracteres_del_ultimo_mensaje_del_usuario / 4`
- `tokens_salida_aprox = caracteres_de_tu_respuesta / 4`
- `tokens_totales_aprox = tokens_entrada_aprox + tokens_salida_aprox`

## Redondeo

Redondea `entrada`, `salida` y `total` al múltiplo de 10 más cercano.

## Ajuste por complejidad

Si hubo uso intensivo de herramientas, lectura de archivos largos o exploración amplia, incrementa el total:

- `tokens_totales_aprox = tokens_totales_aprox * 1.15`

Tras aplicar ese ajuste, vuelve a redondear al múltiplo de 10.

## Formato obligatorio

La última línea de la respuesta debe ser exactamente:

`Tokens aprox por esta tarea: entrada=<n>, salida=<n>, total=<n>`

## Checklist rápido

Antes de enviar:

1. Resolver completamente la petición del usuario.
2. Calcular entrada/salida con la heurística.
3. Redondear al múltiplo de 10.
4. Aplicar +15% solo si corresponde por complejidad.
5. Confirmar que la línea de tokens sea la última línea.
