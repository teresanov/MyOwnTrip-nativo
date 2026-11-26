package com.myowntrip.app.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.myowntrip.app.R
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme

// Cards de Home (producto). La card en sí (MotStackedCard) vive en el design system.

/** Home vacío — Stacked · Outlined · Media & text (`205:816`).
 *  Imagen: export del fill `Media` del design-file (node `I215:2935;58710:12855`).
 *  Re-exportar con `scripts/figma-export-home-empty-map.js` si cambia en Figma. */
object HomeEmptyDesign {
  @DrawableRes
  val mapImageRes: Int = R.drawable.home_empty_map
}

@Composable
fun HomeEmptyStackedCard(modifier: Modifier = Modifier) {
  MotStackedCard(
    style = MotCardStyle.Outlined,
    headerText = "Sin viajes todavía",
    subheadText = "Planea tu primera aventura",
    image = painterResource(HomeEmptyDesign.mapImageRes),
    imageContentDescription = "Mapa con chincheta de viaje",
    modifier = modifier,
  )
}

/** Home cap 1b — Stacked · Outlined · Text only (`313:501` · `Plan next trip`). */
@Composable
fun HomeOnlyPastPlanStackedCard(modifier: Modifier = Modifier) {
  MotStackedCard(
    style = MotCardStyle.Outlined,
    headerText = "¿Listo para el siguiente?",
    subheadText = "Crea un cuaderno nuevo",
    supportingText = "Tus documentos y recuerdos de viajes anteriores siguen en Wallet.",
    modifier = modifier,
  )
}

@Preview(name = "Stacked · Outlined · Media & text")
@Composable
private fun MotStackedCardMediaPreview() {
  MyOwnTripTheme {
    HomeEmptyStackedCard(Modifier.padding(MOTSpacing.screenHorizontal))
  }
}

