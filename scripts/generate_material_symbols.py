"""Genera MOTIcons.kt con Material Symbols Sharp (peso 300, sin relleno).

Misma familia que la página Icons de Figma («Material Symbols Sharp (w300)»).
Descarga los SVG oficiales de Google Fonts y los convierte en ImageVector.

Uso: python scripts/generate_material_symbols.py
"""

import pathlib
import re
import urllib.request

# Nombre en Kotlin (igual que el antiguo Icons.*) -> (nombre Material Symbols, autoMirror)
ICONS = {
    "AccountBalanceWallet": ("account_balance_wallet", False),
    "Add": ("add", False),
    "Archive": ("archive", False),
    "ArrowBack": ("arrow_back", True),
    "AttachFile": ("attach_file", False),
    "Brush": ("brush", False),
    "CalendarMonth": ("calendar_month", False),
    "CameraAlt": ("photo_camera", False),
    "Check": ("check", False),
    "CheckCircle": ("check_circle", False),
    "Close": ("close", False),
    "Cloud": ("cloud", False),
    "ConfirmationNumber": ("confirmation_number", False),
    "ContentCopy": ("content_copy", False),
    "DarkMode": ("dark_mode", False),
    "Delete": ("delete", False),
    "DirectionsCar": ("directions_car", False),
    "DragHandle": ("drag_handle", False),
    "Edit": ("edit", False),
    "Error": ("error", False),
    "Flight": ("flight", False),
    "Hotel": ("hotel", False),
    "InsertDriveFile": ("draft", True),
    "Keyboard": ("keyboard", False),
    "KeyboardArrowDown": ("keyboard_arrow_down", False),
    "KeyboardArrowLeft": ("keyboard_arrow_left", True),
    "KeyboardArrowRight": ("keyboard_arrow_right", True),
    "KeyboardArrowUp": ("keyboard_arrow_up", False),
    "LightMode": ("light_mode", False),
    "Link": ("link", False),
    "LocationOn": ("location_on", False),
    "Luggage": ("luggage", False),
    "Mic": ("mic", False),
    "Menu": ("menu", False),
    "MoreVert": ("more_vert", False),
    "NoteAdd": ("note_add", True),
    "OpenInNew": ("open_in_new", True),
    "Pause": ("pause", False),
    "PhotoCamera": ("photo_camera", False),
    "PhotoLibrary": ("photo_library", False),
    "PlayArrow": ("play_arrow", False),
    "QrCode": ("qr_code", False),
    "QrCodeScanner": ("qr_code_scanner", False),
    "Receipt": ("receipt", False),
    "Redo": ("redo", True),
    "Save": ("save", False),
    "Schedule": ("schedule", False),
    "Search": ("search", False),
    "StickyNote2": ("sticky_note_2", False),
    "Stop": ("stop", False),
    "Today": ("today", False),
    "Tune": ("tune", False),
    "Unarchive": ("unarchive", False),
    "Undo": ("undo", True),
    "Upload": ("upload", False),
    "Visibility": ("visibility", False),
    "Warning": ("warning", False),
    "WifiOff": ("wifi_off", False),
}

URL = "https://fonts.gstatic.com/s/i/short-term/release/materialsymbolssharp/{name}/wght300/24px.svg"
OUT = pathlib.Path(__file__).resolve().parents[1] / (
    "designsystem/src/main/java/com/myowntrip/app/ui/theme/MOTIcons.kt"
)


def fetch_paths(symbol: str) -> list[str]:
    with urllib.request.urlopen(URL.format(name=symbol), timeout=30) as resp:
        svg = resp.read().decode("utf-8")
    paths = re.findall(r'<path[^>]*\sd="([^"]+)"', svg)
    if not paths:
        raise RuntimeError(f"Sin <path> en {symbol}")
    if 'viewBox="0 -960 960 960"' not in svg:
        raise RuntimeError(f"viewBox inesperado en {symbol}")
    return paths


def main() -> None:
    cache: dict[str, list[str]] = {}
    props = []
    for kotlin_name, (symbol, mirror) in ICONS.items():
        if symbol not in cache:
            cache[symbol] = fetch_paths(symbol)
        path_args = ", ".join(f'"{d}"' for d in cache[symbol])
        props.append(
            f"  /** Material Symbols Sharp `{symbol}` (w300). */\n"
            f"  val {kotlin_name}: ImageVector by lazy {{ symbol(\"{symbol}\", autoMirror = {str(mirror).lower()}, {path_args}) }}\n"
        )

    body = "\n".join(props)
    OUT.write_text(
        f"""package com.myowntrip.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.unit.dp

// Generado por scripts/generate_material_symbols.py — no editar a mano.

/**
 * Iconos de la app: **Material Symbols Sharp**, peso 300, sin relleno.
 * Misma familia que la página Icons de Figma («Material Symbols Sharp (w300)»).
 * Sustituye a `material-icons-extended` (Material Icons, familia antigua que Google ya no mantiene).
 */
object MOTIcons {{
{body}}}

private fun symbol(name: String, autoMirror: Boolean, vararg paths: String): ImageVector =
  ImageVector.Builder(
    name = "Symbols.Sharp.$name",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 960f,
    viewportHeight = 960f,
    autoMirror = autoMirror,
  ).apply {{
    // El viewBox de Material Symbols es «0 -960 960 960»: se desplaza 960 hacia abajo.
    group(translationY = 960f) {{
      paths.forEach {{ d -> addPath(pathData = addPathNodes(d), fill = SolidColor(Color.Black)) }}
    }}
  }}.build()
""",
        encoding="utf-8",
    )
    print(f"{len(ICONS)} propiedades, {len(cache)} iconos -> {OUT}")


if __name__ == "__main__":
    main()
