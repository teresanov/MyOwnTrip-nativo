package com.myowntrip.app.ui.components

import com.myowntrip.app.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PreviewCityCoversTest {

  @Test
  fun `preview covers only known demo cities`() {
    assertEquals(R.drawable.home_trip_barcelona, PreviewCityCovers.coverResForCity("Barcelona"))
    assertNull(PreviewCityCovers.coverResForCity("Reykjavik"))
  }
}
