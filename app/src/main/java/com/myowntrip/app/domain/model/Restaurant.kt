package com.myowntrip.app.domain.model

data class Restaurant(
  val id: String,
  val tripId: String,
  val dayId: String? = null,
  val name: String,
  val address: String? = null,
  val status: RestaurantStatus = RestaurantStatus.WITHOUT_RESERVATION,
  val notes: String? = null,
)
