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
}
