package com.myowntrip.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.annotation.DrawableRes
import coil3.compose.AsyncImage
import com.myowntrip.app.domain.model.Trip
import java.io.File
import java.util.Locale

private val CoverLocale = Locale("es", "ES")

@Composable
fun TripCoverImage(
  trip: Trip,
  modifier: Modifier = Modifier,
  @DrawableRes previewCoverRes: Int? = null,
) {
  val coverPath = trip.coverPhoto

  when {
    !coverPath.isNullOrBlank() && File(coverPath).isFile -> {
      AsyncImage(
        model = File(coverPath),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier,
      )
    }
    previewCoverRes != null -> {
      TripCoverImage(
        imageRes = previewCoverRes,
        contentDescription = null,
        modifier = modifier,
      )
    }
    else -> TripCoverPlaceholder(destination = trip.destination, modifier = modifier)
  }
}

@Composable
fun TripCoverPlaceholder(
  destination: String,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier,
    color = MaterialTheme.colorScheme.surfaceContainerHighest,
    shape = MaterialTheme.shapes.small,
  ) {
    Box(contentAlignment = Alignment.Center) {
      Text(
        text = destination.take(2).uppercase(CoverLocale),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.tertiary,
      )
    }
  }
}

@Composable
fun TripCoverImage(
  @DrawableRes imageRes: Int,
  contentDescription: String?,
  modifier: Modifier = Modifier,
) {
  Image(
    painter = painterResource(imageRes),
    contentDescription = contentDescription,
    contentScale = ContentScale.Crop,
    modifier = modifier,
  )
}

/** Vista previa remota o en caché al crear viaje. */
@Composable
fun DestinationCoverPreview(
  imageModel: String?,
  destination: String,
  contentDescription: String?,
  modifier: Modifier = Modifier,
) {
  when {
    !imageModel.isNullOrBlank() -> {
      val model = if (imageModel.startsWith("/")) File(imageModel) else imageModel
      AsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier,
      )
    }
    else -> TripCoverPlaceholder(destination = destination, modifier = modifier)
  }
}

@Composable
fun TripCoverImage(
  imageUrl: String,
  contentDescription: String?,
  modifier: Modifier = Modifier,
) {
  AsyncImage(
    model = imageUrl,
    contentDescription = contentDescription,
    contentScale = ContentScale.Crop,
    modifier = modifier,
  )
}
