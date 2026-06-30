package com.myowntrip.app.data.share

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaceShareParserTest {

  @Test
  fun parseShared_nameAndShortLink_onSeparateLines() {
    val text = "Sagrada Família\nhttps://maps.app.goo.gl/AbCdEf123"
    val result = PlaceShareParser.parseShared(text)
    assertEquals("Sagrada Família", result.name)
    assertEquals("https://maps.app.goo.gl/AbCdEf123", result.url)
  }

  @Test
  fun parseShared_inlineSentenceFormat() {
    val text = "Mira Mercat de la Boqueria en Google Maps https://maps.app.goo.gl/xyz"
    val result = PlaceShareParser.parseShared(text)
    assertEquals("https://maps.app.goo.gl/xyz", result.url)
    assertNotNull(result.name)
  }

  @Test
  fun parseShared_prefersSubjectAsName() {
    val result = PlaceShareParser.parseShared(
      text = "https://maps.app.goo.gl/xyz",
      subject = "Park Güell",
    )
    assertEquals("Park Güell", result.name)
  }

  @Test
  fun parseShared_plainTextWithoutLink() {
    val result = PlaceShareParser.parseShared("Bar Pinotxo")
    assertEquals("Bar Pinotxo", result.name)
    assertNull(result.url)
  }

  @Test
  fun extractLatLng_atPattern() {
    val url = "https://www.google.com/maps/place/Sagrada+Fam%C3%ADlia/@41.4036299,2.1743558,17z/"
    val latlng = PlaceShareParser.extractLatLng(url)
    assertNotNull(latlng)
    assertEquals(41.4036299, latlng!!.first, 1e-6)
    assertEquals(2.1743558, latlng.second, 1e-6)
  }

  @Test
  fun extractLatLng_bangPatternPreferredOverAt() {
    // El @ lleva el centro del mapa; !3d!4d lleva el punto exacto del sitio. Debe ganar el preciso.
    val url = "https://www.google.com/maps/place/X/@41.40,2.17,17z/data=!3d41.4036299!4d2.1743558"
    val latlng = PlaceShareParser.extractLatLng(url)
    assertNotNull(latlng)
    assertEquals(41.4036299, latlng!!.first, 1e-6)
    assertEquals(2.1743558, latlng.second, 1e-6)
  }

  @Test
  fun extractLatLng_queryPattern() {
    val url = "https://maps.google.com/?q=40.4167754,-3.7037902"
    val latlng = PlaceShareParser.extractLatLng(url)
    assertNotNull(latlng)
    assertEquals(40.4167754, latlng!!.first, 1e-6)
    assertEquals(-3.7037902, latlng.second, 1e-6)
  }

  @Test
  fun extractLatLng_scansChainAndIgnoresOutOfRange() {
    val chain = listOf(
      "https://maps.app.goo.gl/short",
      "https://consent.google.com/?continue=whatever",
      "https://www.google.com/maps/place/X/@41.4036299,2.1743558,17z",
    )
    val latlng = PlaceShareParser.extractLatLng(chain)
    assertNotNull(latlng)
    assertEquals(41.4036299, latlng!!.first, 1e-6)
  }

  @Test
  fun extractLatLng_returnsNullWhenNoCoords() {
    assertNull(PlaceShareParser.extractLatLng("https://maps.app.goo.gl/short"))
    assertNull(PlaceShareParser.extractLatLng(emptyList()))
  }

  @Test
  fun extractLatLng_rejectsImpossibleValues() {
    // 999 no es una latitud válida → debe descartarse.
    assertNull(PlaceShareParser.extractLatLng("https://x/@999.0,2.0,17z"))
    assertTrue(true)
  }
}
