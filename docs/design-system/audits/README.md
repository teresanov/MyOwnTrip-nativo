# Audits — índice (nov 2026)

**Estado actual de paridad:** solo [`parity-log.md`](../parity-log.md) (§ Pendiente + Registro).

Los planes y snapshots de sesión (`compose-figma-parity-plan`, `disparity-remediation-plan`, `figma-*-phase-*`, etc.) **se eliminaron** al cerrar la cola (nov 2026). No recrearlos: cierre = fila en parity-log.

## Documentos que quedan aquí

| Archivo | Uso |
|---------|-----|
| [figma-date-product-contract-2026-11-03.md](figma-date-product-contract-2026-11-03.md) | Spec date/time **PRODUCT** (sets DS + gates §6). Vivo hasta cambio de contrato. |
| [scrapbook-album-tipografia-2026-11-11.md](scrapbook-album-tipografia-2026-11-11.md) | **Abierto (código):** tipografía álbum Diario — no es cola Figma; gap `m3Canonical` en `ScrapbookAlbum.kt`. |

## Mapa operativo (siempre)

1. [`governance/design-pipeline.md`](../governance/design-pipeline.md)
2. [`figma-compose-bindings.md`](../figma-compose-bindings.md)
3. [`parity-log.md`](../parity-log.md)

**Chequeo tokens (obligatorio si cambias variables en Figma):**

1. Export → `docs/design-system/variables.json` (`export_figma_variables_json.py`; si el Bridge no responde desde terminal, export MCP + `--input`).
2. Diff → `python .cursor/skills/myowntrip-ds-audit/scripts/diff-variables-kotlin.py` (**0 drift** en roles mapeados).
3. Cierre → fila en `parity-log.md` (§ Registro).

Inventario largo (opcional): `python scripts/generate_tokens_inventory.py` — no commitear salvo auditoría puntual.