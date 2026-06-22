package com.myowntrip.app.ui.features.journal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AudioWaveformBars(
  levels: List<Float>,
  modifier: Modifier = Modifier,
  barCount: Int = 24,
  barColor: Color = MaterialTheme.colorScheme.error,
  minBarFraction: Float = 0.12f,
) {
  val displayLevels = when {
    levels.isEmpty() -> List(barCount) { minBarFraction }
    levels.size >= barCount -> levels.takeLast(barCount)
    else -> {
      val padding = List(barCount - levels.size) { minBarFraction }
      padding + levels
    }
  }

  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    displayLevels.forEach { level ->
      val fraction = level.coerceIn(minBarFraction, 1f)
      Box(
        modifier = Modifier
          .width(4.dp)
          .height(48.dp * fraction)
          .clip(RoundedCornerShape(2.dp))
          .background(barColor),
      )
    }
  }
}
