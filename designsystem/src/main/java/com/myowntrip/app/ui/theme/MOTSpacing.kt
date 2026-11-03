package com.myowntrip.app.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Espaciado de **producto / pantallas** (SoT nativo).
 *
 * Paridad Figma: colección **MOT · Spacing** (`mot/spacing/…`) → alias de **Primitives · Space**.
 * Ver `docs/design-system/spacing.md`.
 *
 * Solo layout de pantalla / entre bloques — no sustituye el padding interno de componentes M3
 * (Button, TextField, Chip, …). No usar la colección legado Semantic · Layout.
 */
object MOTSpacing {
  val none = 0.dp
  val screenHorizontal = 16.dp
  /** Colchón inferior en contenido scrolleable (zona segura gesto / barra / FAB). */
  val screenContentBottom = 24.dp
  val layoutMd = 16.dp
  val layoutLg = 24.dp
  val componentSm = 8.dp
  /** Hueco / inset intermedio (12). Figma: `mot/spacing/component-md`. */
  val componentMd = 12.dp
  val componentXs = 4.dp
  val gutterGrid = 16.dp
  /** Lista con FAB flotante en la misma pantalla. */
  val screenContentBottomWithFab = 88.dp

  /**
   * Padding interno Button Size=XSmall (Figma State-layer / [MOTButtonXSmallContentPadding]).
   * Semánticos Figma: `mot/padding/button/xsmall-horizontal|vertical` (colección MOT · Spacing).
   */
  object ButtonXSmall {
    val horizontal = 12.dp
    val vertical = 6.dp
  }

  /**
   * Menu de producto (Home filtros). Figma: `mot/spacing/menu/group-gap`.
   * Contenedor: `MaterialTheme.shapes.large` (16) ↔ `Corner/Large` — no literales sueltos.
   */
  object Menu {
    val groupGap = 2.dp
    /** Figma: `mot/padding/menu/vertical` y `horizontal`. */
    val vertical = 8.dp
    val horizontal = 0.dp
  }

  /**
   * List item / swipe reveal. Figma: `mot/padding/list-item/…` · `mot/spacing/list-item/gap`.
   * Swipe backgrounds en Compose usan [layoutMd] (16) = horizontal.
   */
  object ListItem {
    val horizontal = 16.dp
    val vertical = 12.dp
    val gap = 12.dp
  }

  /**
   * Date / time pickers. Figma kit M3 celdas = 10; rejilla producto Mot* = [dayGridGap].
   * `mot/spacing/date-picker/cell` · `mot/padding/date-picker/cell` · `mot/spacing/date-picker/day-grid-gap`.
   */
  object DatePicker {
    val cell = 10.dp
    val dayGridGap = 2.dp
    /**
     * Hueco entre bloques de la cabecera del set Input. Figma: `mot/spacing/date-picker/header-gap`.
     * Reservado: sin consumidor en Compose hasta que exista la estructura cabecera + dos campos.
     */
    val headerGap = 36.dp
    /**
     * Padding superior del campo de fecha (set Input). Figma: `mot/padding/date-picker/field-top`.
     * Reservado: sin consumidor en Compose (ver `headerGap`).
     */
    val fieldTop = 18.dp
    /**
     * Hueco de `Label and Date` en el set Modal. Figma: `mot/spacing/date-picker/label-gap`.
     * Reservado: sin consumidor en Compose.
     */
    val labelGap = 36.dp
    /**
     * Hueco de `Local Selection Row` en el set Modal. Figma: `mot/spacing/date-picker/selection-row-gap`.
     * Reservado: sin consumidor en Compose.
     */
    val selectionRowGap = 74.dp
    /**
     * Hueco del bloque `Frame 2608508` en el set Modal. Figma: `mot/spacing/date-picker/block-gap`.
     * Reservado: sin consumidor en Compose.
     */
    val blockGap = 244.dp
  }

  /**
   * Tabs M3 (Primary label). Figma: `mot/padding/tab/…` · `mot/spacing/tab/gap`.
   * Compose [MOTTab] / TabRow; pads vía [Tab.horizontal] / [Tab.vertical].
   */
  object Tab {
    val horizontal = 16.dp
    val vertical = 14.dp
    val gap = 4.dp
    val indicatorInset = 2.dp
  }

  /**
   * Adjunto wallet. Figma: set Document attachment · preview altura fija.
   * Pads del card = [layoutMd]; gaps hint/footer = [componentSm] / [componentXs].
   */
  object DocumentAttachment {
    val previewHeight = 180.dp
  }

  /**
   * Campo de hora (M3 TimeInput) en el kit. Figma: `mot/spacing/time-input/…`.
   * fieldGap = hueco interno del campo (1). labelTop (7) sin uso hoy: reservado para un label bajo el campo.
   */
  object TimeInput {
    /** Reservado: sin uso en Compose ni en el set `Input` de Figma. */
    val labelTop = 7.dp
    val fieldGap = 1.dp
    val contentGap = 10.dp
    /** Hueco entre el campo de hora y la etiqueta "Time label" (Direct Input). */
    val labelGap = 8.dp
  }

  /** Botón por tamaño. Figma: `mot/padding/button/{small,medium,large}-*`. Small lo usa `MOTButton` (`MOTButtonSmallContentPadding`). */
  object ButtonSmall {
    /** Figma = código: el padding por defecto de `MOTButton` (M3 `ContentPadding`) es 24 horizontal y 8 vertical. */
    val horizontal = 24.dp
    val vertical = 8.dp
  }

  object ButtonMedium {
    val horizontal = 24.dp
    val vertical = 16.dp
  }

  object ButtonLarge {
    val horizontal = 48.dp
    val vertical = 32.dp
  }

  /** Chip. Figma: `mot/padding/chip/…` y `mot/spacing/chip/gap`. */
  object Chip {
    val horizontalSm = 8.dp
    val horizontalLg = 16.dp
    val vertical = 6.dp
    val gap = 8.dp
  }

  /** Diálogo. Figma: `mot/padding/dialog/…` y `mot/spacing/dialog/…`. */
  object Dialog {
    val container = 24.dp
    val sectionGap = 16.dp
    /** Hueco alrededor del divisor. Figma: `mot/spacing/dialog/divider-gap`. */
    val dividerGap = 12.dp
    val actionsGap = 8.dp
  }

  /** FAB por tamaño. Figma: `mot/padding/fab/{default,medium,large}`. */
  object Fab {
    val default = 16.dp
    val medium = 26.dp
    val large = 30.dp
  }

  /** Snackbar. Figma: `mot/padding/snackbar/…` y `mot/spacing/snackbar/…`. */
  object Snackbar {
    val horizontal = 16.dp
    val vertical = 12.dp
    val gap = 12.dp
    val gapSm = 8.dp
    val gapXs = 4.dp
  }

  /**
   * Teclado del selector de hora (Keyboard picker). Figma: `mot/spacing/keyboard-picker/gap` (alias de `spacing/20`).
   * Reservado: sin consumidor en Compose, que usa el `TimePicker` de M3.
   */
  object KeyboardPicker {
    val gap = 20.dp
  }

  /**
   * Padding interno del icon button (State-layer). Figma: `mot/padding/icon-button/…`.
   * Tamaño por defecto M3 (Small, 40 dp) no usa padding; wide queda en Figma para uso futuro.
   */
  object IconButton {
    val zeroHorizontal = 0.dp
    val zeroVertical = 0.dp
    val wideHorizontal = 32.dp
    val wideVertical = 16.dp
    val tenHorizontal = 10.dp
    val tenVertical = 10.dp
    val lgHorizontal = 32.dp
    val lgVertical = 32.dp
    val mdHorizontal = 16.dp
    val mdVertical = 16.dp
  }

  /**
   * Padding interno del campo de texto (Outlined). Figma: `mot/padding/text-field/horizontal|vertical`.
   * Compose: consumir desde el TextField de M3 cuando se personalice; el set Input usa estos valores.
   */
  object TextField {
    val horizontal = 16.dp
    val vertical = 4.dp
  }
}
