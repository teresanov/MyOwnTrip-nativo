#!/usr/bin/env python3
"""Genera inventario tokens Figma + Compose. Ejecutar tras export get_variable_defs."""
from __future__ import annotations

import json
import re
import sys
from collections import defaultdict
from pathlib import Path

REPO = Path(__file__).resolve().parents[1]
VAR_DEFS = Path(
    r"C:\Users\Usuario\.cursor\projects\c-Users-Usuario-AndroidProjects-MOT-nativo\agent-tools\62c412ea-4f7a-450c-ba27-aa233fa3d73f.txt"
)
MOT_SPACING_KT = REPO / "designsystem/src/main/java/com/myowntrip/app/ui/theme/MOTSpacing.kt"
OUT = REPO / "docs/design-system/audits/tokens-inventory-complete-2026-11-03.md"

# Kit M3 importado (ARCHIVE). Smoke PRODUCT 2026-11-18: 0 binds en 6 maestros — ver contrato date-product.
KIT_ARCHIVE_DATE_TIME: frozenset[str] = frozenset(
    {
        "mot/spacing/date-picker/header-gap",
        "mot/padding/date-picker/field-top",
        "mot/spacing/date-picker/label-gap",
        "mot/spacing/date-picker/selection-row-gap",
        "mot/spacing/date-picker/block-gap",
        "mot/spacing/time-input/label-top",
        "mot/spacing/time-input/field-gap",
        "mot/spacing/time-input/content-gap",
        "mot/spacing/time-input/label-gap",
        "mot/spacing/keyboard-picker/gap",
    }
)

PRODUCT_DATE_TIME_NOTES: dict[str, str] = {
    "mot/spacing/date-picker/day-grid-gap": "PRODUCT · `MotBoundedMonthCalendar` / MotDateRangePickerDialog",
    "mot/spacing/date-picker/cell": "Celdas calendario M3 (Building Blocks); producto usa dayGridGap",
    "mot/padding/date-picker/cell": "Kit M3 celdas; sin lectura directa en app",
}

# Compose usage scan
MOT_USAGE: dict[str, set[str]] = defaultdict(set)
for kt in REPO.rglob("*.kt"):
    if "build" in kt.parts:
        continue
    text = kt.read_text(encoding="utf-8", errors="ignore")
    rel = kt.relative_to(REPO).as_posix()
    for m in re.finditer(r"MOTSpacing(?:\.[A-Za-z][A-Za-z0-9_]*)+", text):
        MOT_USAGE[m.group(0)].add(rel)

# MOTSpacing.kt: Figma path comments + reserved
kt_text = MOT_SPACING_KT.read_text(encoding="utf-8")
# Figma mot/* → Compose (canónico, ver MOTSpacing.kt + spacing.md)
MOT_FIGMA_TO_COMPOSE: dict[str, str] = {
    "mot/spacing/screen-horizontal": "MOTSpacing.screenHorizontal",
    "mot/spacing/screen-content-bottom": "MOTSpacing.screenContentBottom",
    "mot/spacing/layout-md": "MOTSpacing.layoutMd",
    "mot/spacing/layout-lg": "MOTSpacing.layoutLg",
    "mot/spacing/component-sm": "MOTSpacing.componentSm",
    "mot/spacing/component-md": "MOTSpacing.componentMd",
    "mot/spacing/component-lg": "MOTSpacing.componentLg",
    "mot/spacing/component-xs": "MOTSpacing.componentXs",
    "mot/spacing/gutter-grid": "MOTSpacing.gutterGrid",
    "mot/spacing/screen-content-bottom-with-fab": "MOTSpacing.screenContentBottomWithFab",
    "mot/spacing/none": "MOTSpacing.none",
    "mot/padding/button/xsmall-horizontal": "MOTSpacing.ButtonXSmall.horizontal",
    "mot/padding/button/xsmall-vertical": "MOTSpacing.ButtonXSmall.vertical",
    "mot/padding/button/small-horizontal": "MOTSpacing.ButtonSmall.horizontal",
    "mot/padding/button/small-vertical": "MOTSpacing.ButtonSmall.vertical",
    "mot/padding/button/medium-horizontal": "MOTSpacing.ButtonMedium.horizontal",
    "mot/padding/button/medium-vertical": "MOTSpacing.ButtonMedium.vertical",
    "mot/padding/button/large-horizontal": "MOTSpacing.ButtonLarge.horizontal",
    "mot/padding/button/large-vertical": "MOTSpacing.ButtonLarge.vertical",
    "mot/spacing/menu/group-gap": "MOTSpacing.Menu.groupGap",
    "mot/padding/menu/vertical": "MOTSpacing.Menu.vertical",
    "mot/padding/menu/horizontal": "MOTSpacing.Menu.horizontal",
    "mot/padding/list-item/horizontal": "MOTSpacing.ListItem.horizontal",
    "mot/padding/list-item/vertical": "MOTSpacing.ListItem.vertical",
    "mot/spacing/list-item/gap": "MOTSpacing.ListItem.gap",
    "mot/spacing/date-picker/cell": "MOTSpacing.DatePicker.cell",
    "mot/padding/date-picker/cell": "MOTSpacing.DatePicker.cell",
    "mot/spacing/date-picker/day-grid-gap": "MOTSpacing.DatePicker.dayGridGap",
    "mot/spacing/date-picker/header-gap": "MOTSpacing.DatePicker.headerGap",
    "mot/padding/date-picker/field-top": "MOTSpacing.DatePicker.fieldTop",
    "mot/spacing/date-picker/label-gap": "MOTSpacing.DatePicker.labelGap",
    "mot/spacing/date-picker/selection-row-gap": "MOTSpacing.DatePicker.selectionRowGap",
    "mot/spacing/date-picker/block-gap": "MOTSpacing.DatePicker.blockGap",
    "mot/padding/tab/horizontal": "MOTSpacing.Tab.horizontal",
    "mot/padding/tab/vertical": "MOTSpacing.Tab.vertical",
    "mot/spacing/tab/gap": "MOTSpacing.Tab.gap",
    "mot/padding/tab/indicator-inset": "MOTSpacing.Tab.indicatorInset",
    "mot/padding/chip/horizontal-sm": "MOTSpacing.Chip.horizontalSm",
    "mot/padding/chip/horizontal-lg": "MOTSpacing.Chip.horizontalLg",
    "mot/padding/chip/vertical": "MOTSpacing.Chip.vertical",
    "mot/spacing/chip/gap": "MOTSpacing.Chip.gap",
    "mot/padding/dialog/container": "MOTSpacing.Dialog.container",
    "mot/spacing/dialog/section-gap": "MOTSpacing.Dialog.sectionGap",
    "mot/spacing/dialog/divider-gap": "MOTSpacing.Dialog.dividerGap",
    "mot/spacing/dialog/actions-gap": "MOTSpacing.Dialog.actionsGap",
    "mot/padding/fab/default": "MOTSpacing.Fab.default",
    "mot/padding/fab/medium": "MOTSpacing.Fab.medium",
    "mot/padding/fab/large": "MOTSpacing.Fab.large",
    "mot/padding/snackbar/horizontal": "MOTSpacing.Snackbar.horizontal",
    "mot/padding/snackbar/vertical": "MOTSpacing.Snackbar.vertical",
    "mot/spacing/snackbar/gap": "MOTSpacing.Snackbar.gap",
    "mot/spacing/snackbar/gap-sm": "MOTSpacing.Snackbar.gapSm",
    "mot/spacing/snackbar/gap-xs": "MOTSpacing.Snackbar.gapXs",
    "mot/spacing/keyboard-picker/gap": "MOTSpacing.KeyboardPicker.gap",
    "mot/padding/icon-button/0-horizontal": "MOTSpacing.IconButton.zeroHorizontal",
    "mot/padding/icon-button/0-vertical": "MOTSpacing.IconButton.zeroVertical",
    "mot/padding/icon-button/wide-horizontal": "MOTSpacing.IconButton.wideHorizontal",
    "mot/padding/icon-button/wide-vertical": "MOTSpacing.IconButton.wideVertical",
    "mot/padding/icon-button/lg-horizontal": "MOTSpacing.IconButton.lgHorizontal",
    "mot/padding/icon-button/lg-vertical": "MOTSpacing.IconButton.lgVertical",
    "mot/padding/icon-button/md-horizontal": "MOTSpacing.IconButton.mdHorizontal",
    "mot/padding/icon-button/md-vertical": "MOTSpacing.IconButton.mdVertical",
    "mot/padding/icon-button/10-horizontal": "MOTSpacing.IconButton.tenHorizontal",
    "mot/padding/icon-button/10-vertical": "MOTSpacing.IconButton.tenVertical",
    "mot/padding/text-field/horizontal": "MOTSpacing.TextField.horizontal",
    "mot/padding/text-field/vertical": "MOTSpacing.TextField.vertical",
    "mot/padding/text-field/outlined-vertical": "MOTSpacing.TextField.outlinedVertical",
    "mot/spacing/time-input/label-top": "MOTSpacing.TimeInput.labelTop",
    "mot/spacing/time-input/field-gap": "MOTSpacing.TimeInput.fieldGap",
    "mot/spacing/time-input/content-gap": "MOTSpacing.TimeInput.contentGap",
    "mot/spacing/time-input/label-gap": "MOTSpacing.TimeInput.labelGap",
}

COMPOSE_TO_FIGMA = {v: k for k, v in MOT_FIGMA_TO_COMPOSE.items()}

RESERVED_COMPOSE: set[str] = set()
for i, line in enumerate(kt_text.splitlines()):
    block = "\n".join(kt_text.splitlines()[max(0, i - 6) : i + 1])
    if "Reservado" not in block and "reservado" not in block.lower():
        continue
    vm = re.match(r"\s*val (\w+)", line)
    if vm:
        om = re.search(r"object (\w+)", block)
        if om and f"object {om.group(1)}" in block:
            RESERVED_COMPOSE.add(f"MOTSpacing.{om.group(1)}.{vm.group(1)}")
        elif not om:
            RESERVED_COMPOSE.add(f"MOTSpacing.{vm.group(1)}")

# Manual Figma application notes (auditorías; no sustituye escaneo nodo a nodo)
FIGMA_APPLY_NOTES: dict[str, str] = {
    "mot/spacing/screen-horizontal": "Design-file / pantallas; márgenes columna producto",
    "mot/spacing/layout-md": "Stacks, cards, calendario modal (padding grid 16)",
    "mot/spacing/menu/group-gap": "Home · menú filtros (`HomeSearchAndFilter`)",
    "mot/spacing/date-picker/day-grid-gap": PRODUCT_DATE_TIME_NOTES["mot/spacing/date-picker/day-grid-gap"],
    "mot/padding/button/xsmall-horizontal": "Button/Toggle XSmall; TextButton Small corregido",
    "mot/padding/button/small-horizontal": "Button SM State-layer (24, nov 2026)",
    "mot/padding/button/small-vertical": "Button SM (8)",
    "mot/padding/text-field/outlined-vertical": "Text field set · 252 nodos (60 variantes)",
    "mot/padding/text-field/horizontal": "Text field, Time Input field",
    "mot/padding/text-field/vertical": "Time Input (4), no Outlined producto",
    "mot/spacing/time-input/field-gap": "ARCHIVE kit · Time Input (`61348:29338` usa layoutMd/componentSm)",
    "mot/spacing/time-input/content-gap": "ARCHIVE kit · Time Input",
    "mot/spacing/time-input/label-gap": "ARCHIVE kit · Direct Input",
    "mot/spacing/time-input/label-top": "ARCHIVE kit · sin nodo PRODUCT",
    "mot/spacing/date-picker/header-gap": "ARCHIVE · Input date picker `51954:18540`",
    "mot/padding/date-picker/field-top": "ARCHIVE · Input date picker",
    "mot/spacing/date-picker/label-gap": "ARCHIVE · Modal date picker `51954:18136`",
    "mot/spacing/date-picker/selection-row-gap": "ARCHIVE · Modal kit",
    "mot/spacing/date-picker/block-gap": "ARCHIVE · Modal kit",
    "mot/spacing/keyboard-picker/gap": "ARCHIVE · Keyboard picker `52949:28053`",
    "mot/padding/dialog/container": "Basic dialog · content/actions",
    "mot/spacing/dialog/section-gap": "Basic dialog · título/cuerpo/divisor",
    "mot/spacing/dialog/divider-gap": "Basic dialog · hueco divisor",
    "mot/spacing/dialog/actions-gap": "Basic dialog · acciones",
    "mot/stroke/emphasis": "Time Input Selected=True · stroke 2px",
}

ARCHIVE_KIT_TOKENS = [
    # (figma path, compose, bound on ARCHIVE kit sets?, compose used in app?)
    ("mot/spacing/time-input/label-top", "MOTSpacing.TimeInput.labelTop", False, False),
    ("mot/spacing/time-input/field-gap", "MOTSpacing.TimeInput.fieldGap", True, False),
    ("mot/spacing/time-input/content-gap", "MOTSpacing.TimeInput.contentGap", True, False),
    ("mot/spacing/time-input/label-gap", "MOTSpacing.TimeInput.labelGap", True, False),
    ("mot/spacing/date-picker/header-gap", "MOTSpacing.DatePicker.headerGap", True, False),
    ("mot/padding/date-picker/field-top", "MOTSpacing.DatePicker.fieldTop", True, False),
    ("mot/spacing/date-picker/label-gap", "MOTSpacing.DatePicker.labelGap", True, False),
    ("mot/spacing/date-picker/selection-row-gap", "MOTSpacing.DatePicker.selectionRowGap", True, False),
    ("mot/spacing/date-picker/block-gap", "MOTSpacing.DatePicker.blockGap", True, False),
    ("mot/spacing/keyboard-picker/gap", "MOTSpacing.KeyboardPicker.gap", True, False),
]

RESERVE_TOKENS = [
    # (figma path, compose, figma applied?, compose used?) — no date/time kit (ver ARCHIVE_KIT_TOKENS)
    ("mot/padding/chip/*", "MOTSpacing.Chip.*", True, False),
    ("mot/padding/dialog/*", "MOTSpacing.Dialog.*", True, False),
    ("mot/padding/fab/*", "MOTSpacing.Fab.*", True, False),
    ("mot/padding/snackbar/*", "MOTSpacing.Snackbar.*", True, False),
    ("mot/padding/menu/vertical|horizontal", "MOTSpacing.Menu.vertical/horizontal", True, False),
    ("mot/padding/icon-button/*", "MOTSpacing.IconButton.*", True, False),
    ("mot/padding/text-field/*", "MOTSpacing.TextField.*", True, False),
    ("Brand `ink-deep`", "BrandColors.InkDeep", True, False),
]


def figma_to_compose_path(name: str) -> str:
    return MOT_FIGMA_TO_COMPOSE.get(name, "—")


def compose_usage_count(compose: str) -> int:
    if compose == "—":
        return 0
    return len(MOT_USAGE.get(compose, set()))


def token_estado(name: str, compose: str, uses: int) -> str:
    if compose == "—":
        return "Figma-only (sin espejo Kotlin)"
    if uses > 0:
        return "Activo"
    if name in KIT_ARCHIVE_DATE_TIME:
        return "**ARCHIVE · kit M3** (no producto)"
    if compose in RESERVED_COMPOSE:
        return "**Reserva** (KDoc kit; no ampliar)"
    return "**Reserva** (espejo sin uso app)"


def main() -> None:
    raw = json.loads(VAR_DEFS.read_text(encoding="utf-8"))
    variables = raw["variables"]

    by_collection: dict[str, list[dict]] = defaultdict(list)
    coll_by_id = {c["id"]: c for c in raw.get("collections", [])}
    for v in variables:
        cid = v.get("collectionId")
        cname = coll_by_id.get(cid, {}).get("name", cid or "?")
        by_collection[cname].append(v)

    lines: list[str] = []
    lines.append("# Inventario completo de tokens — Figma + Compose")
    lines.append("")
    lines.append("**Fecha:** 2026-11-03 (regenerado 2026-11-18) · **Figma live:** Figwright `get_variable_defs` (plugin conectado)")
    lines.append("")
    lines.append(
        "> **Gobernanza:** contrato date/time → [figma-date-product-contract-2026-11-03.md](figma-date-product-contract-2026-11-03.md). "
        "Tokens listados como **ARCHIVE · kit M3** existen en `MOTSpacing.kt` para el kit importado; **no** son contrato de láminas PRODUCT "
        "(smoke 2026-11-18: 0 binds kit-only en 6 maestros). No etiquetar como «reserva producto»."
    )
    lines.append("")
    lines.append("**Archivo:** MyOwnTrip_nativo - Design System")
    lines.append("**Compose SoT consumo UI:** `MaterialTheme.colorScheme` / `typography` / `shapes` + `MOTSpacing` + `LocalExtendedColors`")
    lines.append("")
    lines.append("## Resumen por colección Figma")
    lines.append("")
    lines.append("| Colección | Variables | Consumo Compose | Aplicación Figma |")
    lines.append("|-----------|----------:|-----------------|------------------|")
    summary_rows = [
        ("M3", "Schemes, Palettes, State Layers, Extended Colors", "`AppColors` → `colorScheme`", "Todos los sets DS + láminas"),
        ("Shape", "Corner/*", "`MOTThemeShapes` + ADR 004 botones", "Componentes + morph botón"),
        ("Typescale + Font theme", "Static/*", "`AppTypography` (Fraunces/Inter)", "Text styles enlazados en sets"),
        ("Brand", "paper, ink, ocre, …", "`BrandColors` (logo)", "Lockups / apariencia marca"),
        ("Primitives · Space / Size", "spacing/*, size/*", "No directo (alias)", "Detrás de `mot/*` y semánticos legado"),
        ("MOT · Spacing", "mot/spacing/*, mot/padding/*, mot/stroke/*", "`MOTSpacing.kt`", "Sets M3 documentados + producto"),
    ]
    for cname, vars_list in sorted(
        ((c, v) for c, v in by_collection.items() if "LEGACY" not in c),
        key=lambda x: -len(x[1]),
    ):
        note = next((r[2] for r in summary_rows if r[0] in cname or cname.startswith(r[0][:3])), "Ver doc DS")
        fig = next((r[3] for r in summary_rows if r[0] in cname), "Variables panel + binds en sets")
        lines.append(f"| {cname} | {len(vars_list)} | {note} | {fig} |")

    lines.append("")
    lines.append("## Color M3 (`Schemes/*`) → Compose")
    lines.append("")
    lines.append("| Token Figma | Rol Compose | Dónde se aplica |")
    lines.append("|-------------|-------------|-----------------|")
    lines.append("| `Schemes/*` (49 roles × 6 modos) | `MaterialTheme.colorScheme.*` | Toda UI `app/` vía M3 (botones, campos, superficies) |")
    lines.append("| `Extended Colors/Success|Warning|Info` (+ containers) | `LocalExtendedColors` | Wallet drift, chips estado plan, indicadores |")
    lines.append("| `Palettes/*` | No consumo directo | Base de alias M3 en Figma |")
    lines.append("| `State Layers/*` | **Runtime** (8/10/10/16%, disabled 38/12%) | Láminas Figma; **no** importar a Kotlin |")
    lines.append("")
    lines.append("## Shape (`Corner/*`) → Compose")
    lines.append("")
    lines.append("| Figma | Compose | Aplicación |")
    lines.append("|-------|---------|------------|")
    lines.append("| `Corner/Extra-small` … `Extra-large` | `MaterialTheme.shapes.*` | Cards, chips, sheets, menús |")
    lines.append("| `Corner/None` ↔ `Large-increased` | `rememberMOTButtonShape` | Botones producto (ADR 004) |")
    lines.append("")
    lines.append("## Tipografía (`Static/*`) → Compose")
    lines.append("")
    lines.append("| Figma Typescale | Compose | Aplicación Figma | Aplicación app |")
    lines.append("|-----------------|---------|------------------|----------------|")
    lines.append("| Display/Headline | Fraunces | Sets DS | Títulos pantalla, hero |")
    lines.append("| Title/Body/Label | Inter | Sets DS | UI funcional, formularios |")
    lines.append("| Button Label * | Inter Medium | Button sets | `MOTButton` label style M3 |")
    lines.append("")
    lines.append("## MOT · Spacing — tabla maestra (88 tokens)")
    lines.append("")
    lines.append("| Token Figma | Espejo Compose | Usos Compose (.kt) | Aplicación Figma (resumen) | Estado |")
    lines.append("|-------------|----------------|--------------------|----------------------------|--------|")

    mot_vars = []
    for cname, vars_list in by_collection.items():
        if "MOT" in cname and "Spacing" in cname:
            mot_vars = vars_list
            break

    for v in sorted(mot_vars, key=lambda x: x.get("name", "")):
        name = v["name"]
        compose = figma_to_compose_path(name)
        uses = compose_usage_count(compose)
        use_str = f"{uses} archivos" if uses else "**0**"
        estado = token_estado(name, compose, uses)
        fig_note = PRODUCT_DATE_TIME_NOTES.get(
            name,
            FIGMA_APPLY_NOTES.get(name, "Sets DS (Button, FAB, Dialog, …) — ver `spacing.md`"),
        )
        lines.append(f"| `{name}` | `{compose}` | {use_str} | {fig_note} | {estado} |")

    lines.append("")
    lines.append("## Compose — tokens `MOTSpacing` más usados en `app/`")
    lines.append("")
    lines.append("| Propiedad | Archivos | Figma |")
    lines.append("|-----------|----------|-------|")
    for tok, files in sorted(MOT_USAGE.items(), key=lambda x: -len(x[1]))[:25]:
        fp = COMPOSE_TO_FIGMA.get(tok, "—")
        lines.append(f"| `{tok}` | {len(files)} | `{fp}` |")

    lines.append("")
    lines.append("## Apartado: Date & time — ARCHIVE kit M3 (no producto)")
    lines.append("")
    lines.append(
        "Tras remediación R1–R4 y smoke PRODUCT (parity-log 2026-11-18). Variables siguen en Figma/`MOTSpacing.kt` "
        "para nodos **ARCHIVE** (`51954:18540`, `51954:18136`, `52949:28053`); **prohibido** enlazarlas en los 6 maestros PRODUCT."
    )
    lines.append("")
    lines.append("| Token | Compose | ¿Bind en kit ARCHIVE? | Motivo |")
    lines.append("|-------|---------|----------------------|--------|")
    archive_motivo = "Kit M3 importado; 0 uso app; 0 bind PRODUCT (smoke 2026-11-18)"
    for row in ARCHIVE_KIT_TOKENS:
        lines.append(f"| `{row[0]}` | `{row[1]}` | {'Sí' if row[2] else 'No'} | {archive_motivo} |")

    lines.append("")
    lines.append("### PRODUCT date/time — tokens activos en app")
    lines.append("")
    lines.append("| Token | Compose | Uso |")
    lines.append("|-------|---------|-----|")
    lines.append("| `mot/spacing/date-picker/day-grid-gap` | `MOTSpacing.DatePicker.dayGridGap` | `MotCalendarPicker.kt` · rejilla días |")
    lines.append("| `mot/spacing/screen-horizontal` | `MOTSpacing.screenHorizontal` | Modales Mot*Date/Time |")
    lines.append("| `mot/spacing/layout-md` / `component-sm` / `component-xs` | idem | Padding modales y nav mes |")
    lines.append("| `mot/spacing/component-sm` | `MOTSpacing.componentSm` | Gap fila `MotDateRangeField` |")
    lines.append("")
    lines.append("## Apartado: tokens de reserva (otros dominios)")
    lines.append("")
    lines.append(
        "Existen en **Figma** y en **`MOTSpacing`/`BrandColors`**, pero **ningún composable de `app/`** los lee. "
        "Distinto de **ARCHIVE date/time**: estos son espejo kit genérico (chips, dialog, FAB…)."
    )
    lines.append("")
    lines.append("| Token | Compose | ¿Enlazado en Figma? | Motivo reserva |")
    lines.append("|-------|---------|---------------------|----------------|")
    for row in RESERVE_TOKENS:
        lines.append(f"| `{row[0]}` | `{row[1]}` | {'Sí' if row[2] else 'No'} | Sin consumidor Compose / kit ahead of app |")

    lines.append("")
    lines.append("- **Chip / Dialog / FAB / Snackbar / IconButton / TextField objects:** espejo para paridad kit; M3 aplica padding por defecto en app.")
    lines.append("- **`ink-deep`:** token Brand; lockups usan `ink` / `ocre` (`BrandColors.InkDeep` = alias de `Ink`).")
    lines.append("")
    lines.append("## Figma-only sin espejo Kotlin")
    lines.append("")
    lines.append("| Token | Aplicación Figma | Notas |")
    lines.append("|-------|------------------|-------|")
    fonly = [
        "mot/padding/extended-fab/*",
        "mot/spacing/extended-fab/gap-*",
        "mot/padding/segmented/*",
        "mot/spacing/segmented/gap",
        "mot/spacing/button-icon-gap*",
        "mot/padding/date-picker/cell",
    ]
    for t in fonly:
        lines.append(f"| `{t}` | Sets Extended FAB, Segmented, Date cells | Sin objeto `MOTSpacing`; M3 o kit |")
    lines.append("")
    lines.append("## Limitaciones de este inventario")
    lines.append("")
    lines.append("- **Nodos Figma:** la columna «Aplicación Figma» combina auditorías (`figma-datetime-matrix`, `spacing.md`, dialog B.3) + reglas de página Buttons (30/30 bound). No lista cada `nodeId` × 630 variables.")
    lines.append("- **`docs/design-system/variables.json`:** export parcial (~512 entradas); **live Figma = 630** variables (Figwright nov 2026).")
    lines.append("- **Literales `.dp` en `app/`:** ~395 valores sin token; fuera del alcance de fila por fila.")
    lines.append("")
    lines.append("## Referencias")
    lines.append("")
    lines.append("- `docs/design-system/spacing.md`")
    lines.append("- `docs/design-system/parity-log.md`")
    lines.append("- `docs/design-system/audits/figma-date-product-contract-2026-11-03.md`")
    lines.append("- `designsystem/.../MOTSpacing.kt`, `Color.kt`, `Theme.kt`, `Shapes.kt`, `Type.kt`")

    OUT.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"Wrote {OUT} ({len(lines)} lines)")


if __name__ == "__main__":
    main()
