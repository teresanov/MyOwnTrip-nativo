# Checklist TalkBack — swipe y menús de acción

Verificación manual en dispositivo físico o emulador con **TalkBack activado** (Ajustes → Accesibilidad).

Política: [`android-compose-ux.md`](android-compose-ux.md) — toda acción por swipe debe tener equivalente por tap.

---

## Home — lista «Más viajes»

**Ruta:** `trip_list` · al menos 2 viajes en «Más viajes» (no en el hero).

| # | Acción | Esperado |
|---|--------|----------|
| 1 | Enfocar fila de viaje | Se anuncia nombre/destino del viaje |
| 2 | Menú de acciones TalkBack → «Archivar viaje» | Archiva; snackbar «archivado» con Deshacer |
| 3 | Deshacer en snackbar | Viaje vuelve a la lista activa |
| 4 | «Eliminar viaje» (custom action) | Abre diálogo de confirmación; no borra sin confirmar |
| 5 | Botón ⋮ visible → Archivar / Eliminar | Misma acción que custom actions |
| 6 | Filtro **Archivados** → «Restaurar viaje» | Restaura a lista activa |
| 7 | Hero destacado | **Sin** swipe ni menú ⋮ de archivar |

---

## Wallet — lista plana

**Ruta:** detalle de viaje → pestaña Wallet · chip **Activos** o **Archivados**.

| # | Acción | Esperado |
|---|--------|----------|
| 1 | Enfocar documento en lista plana | Título/tipo del documento |
| 2 | «Archivar documento» (custom action) | Archiva; feedback coherente con chip activo |
| 3 | Chip **Archivados** → «Restaurar documento» | Vuelve a Activos |
| 4 | «Eliminar documento» | Diálogo de confirmación |
| 5 | Menú ⋮ | Mismas opciones que custom actions |
| 6 | Carrusel «Próximos» (si visible) | **Sin** swipe de archivar |

---

## Wallet — duplicados al importar (EC-DUPLICATE)

**Ruta:** importar documento que ya existe (misma URL o mismo archivo).

| # | Acción | Esperado |
|---|--------|----------|
| 1 | Diálogo «Documento duplicado» | Título y cuerpo legibles |
| 2 | Enfocar **Ignorar** | Acción clara; cierra sin guardar |
| 3 | **Guardar como nuevo** | Crea entrada adicional |
| 4 | **Reemplazar existente** | Sustituye la entrada previa |
| 5 | Botón cerrar / atrás | No deja estado colgado |

---

## Registro

| Fecha | Dispositivo | Build | Resultado | Notas |
|-------|-------------|-------|-----------|-------|
| | | | ☐ OK / ☐ incidencias | |
