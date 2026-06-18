package com.myowntrip.app.data.cover

import org.junit.Assert.assertEquals
import org.junit.Test

class DestinationCoverNormalizerTest {

  @Test
  fun `normalize strips accents`() {
    assertEquals("malaga", DestinationCoverNormalizer.normalize(" Málaga "))
  }

  @Test
  fun `cache key is filesystem safe`() {
    assertEquals("sao_paulo", DestinationCoverNormalizer.cacheKey("São Paulo"))
  }
}
