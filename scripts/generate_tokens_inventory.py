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
    "mot/spacing/date-picker/day-grid-gap": "Modal date picker `Type=Day`; sets Input grid",
    "mot/padding/button/xsmall-horizontal": "Button/Toggle XSmall; TextButton Small corregido",
    "mot/padding/button/small-horizontal": "Button SM State-layer (24, nov 2026)",
    "mot/padding/button/small-vertical": "Button SM (8)",
    "mot/padding/text-field/outlined-vertical": "Text field set · 252 nodos (60 variantes)",
    "mot/padding/text-field/horizontal": "Text field, Time Input field",
    "mot/padding/text-field/vertical": "Time Input (4), no Outlined producto",
    "mot/spacing/time-input/field-gap": "Time Input · Input Field gap 1",
    "mot/spacing/time-input/content-gap": "Time Input · default gap 10",
    "mot/spacing/time-input/label-gap": "Direct Input · gap a Time label",
    "mot/spacing/time-input/label-top": "Sin nodo enlazado (reserva)",
    "mot/spacing/date-picker/header-gap": "Date Input set · Header",
    "mot/padding/date-picker/field-top": "Date Input · Date / Date return",
    "mot/spacing/date-picker/label-gap": "Modal date picker · Label and Date",
    "mot/spacing/date-picker/selection-row-gap": "Modal · Local Selection Row",
    "mot/spacing/date-picker/block-gap": "Modal · Frame 2608508",
    "mot/spacing/keyboard-picker/gap": "Keyboard picker · Format 12/24h itemSpacing 20",
    "mot/padding/dialog/container": "Basic dialog · content/actions",
    "mot/spacing/dialog/section-gap": "Basic dialog · título/cuerpo/divisor",
    "mot/spacing/dialog/divider-gap": "Basic dialog · hueco divisor",
    "mot/spacing/dialog/actions-gap": "Basic dialog · acciones",
    "mot/stroke/emphasis": "Time Input Selected=True · stroke 2px",
}

RESERVE_TOKENS = [
    # (figma path, compose, figma applied?, compose used?)
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


def token_estado(compose: str, uses: int) -> str:
    if compose == "—":
        return "Figma-only (sin espejo Kotlin)"
    if uses > 0:
        return "Activo"
    if compose in RESERVED_COMPOSE:
        return "**Reserva** (KDoc)"
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
    lines.append("**Fecha:** 2026-11-03 · **Figma live:** Figwright `get_variable_defs` (plugin conectado)")
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
        ("LEGACY · Semantic Layout", "semantic/*", "**Prohibido**", "Migrar a `mot/*`; no ampliar"),
    ]
    for cname, vars_list in sorted(by_collection.items(), key=lambda x: -len(x[1])):
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
        estado = token_estado(compose, uses)
        fig_note = FIGMA_APPLY_NOTES.get(name, "Sets DS (Button, FAB, Dialog, …) — ver `spacing.md`")
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
    lines.append("## Apartado: tokens de reserva")
    lines.append("")
    lines.append(
        "Definición pipeline: existen en **Figma** (y a menudo en nodos del kit) y en **`MOTSpacing`/`BrandColors`**, "
        "pero **ningún composable de `app/`** los lee. Cambiarlos en Figma no altera la app hasta conectar."
    )
    lines.append("")
    lines.append("| Token | Compose | ¿Enlazado en Figma? | Motivo reserva |")
    lines.append("|-------|---------|---------------------|----------------|")
    for row in RESERVE_TOKENS:
        lines.append(f"| `{row[0]}` | `{row[1]}` | {'Sí' if row[2] else 'No'} | Sin consumidor Compose / kit ahead of app |")

    lines.append("")
    lines.append("### Detalle reserva Date & time (auditoría 2026-10-30 / 2026-11-03)")
    lines.append("")
    lines.append("- **Input date:** `header-gap`, `field-top` → sets Input; app usa `MotDateRangeField` (un solo `OutlinedTextField`).")
    lines.append("- **Modal date:** `label-gap`, `selection-row-gap`, `block-gap` → modal M3 kit; app usa `MotTripDatePickerDialog` con `dayGridGap` + `layoutMd` only.")
    lines.append("- **Time input:** gaps en sets Time Input; app usa `TimePicker` M3 (`MotTimePickerDialog`), no réplica Figma 1:1.")
    lines.append("- **Keyboard picker:** `keyboard-picker/gap` → Format 12/24h; Compose no implementa keyboard picker custom.")
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
    lines.append("## LEGACY · Semantic Layout (55)")
    lines.append("")
    lines.append("**No usar.** Sustituir binds por `mot/*` al tocar frames. Ver `docs/design-system/spacing.md`.")
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
    lines.append("- `docs/design-system/audits/backlog-tokens-inventory-2026-10-30.md`")
    lines.append("- `docs/design-system/audits/figma-datetime-matrix-2026-10-30.md`")
    lines.append("- `designsystem/.../MOTSpacing.kt`, `Color.kt`, `Theme.kt`, `Shapes.kt`, `Type.kt`")

    OUT.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"Wrote {OUT} ({len(lines)} lines)")


if __name__ == "__main__":
    main()
