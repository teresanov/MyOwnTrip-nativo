package com.myowntrip.app.data.repository

import com.myowntrip.app.data.local.dao.RestaurantDao
import com.myowntrip.app.data.local.toDomain
import com.myowntrip.app.data.local.toEntity
import com.myowntrip.app.domain.model.PlaceCategory
import com.myowntrip.app.domain.model.Restaurant
import com.myowntrip.app.domain.model.RestaurantStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantRepository @Inject constructor(
  private val restaurantDao: RestaurantDao,
) {
  fun observeByTrip(tripId: String): Flow<List<Restaurant>> =
    restaurantDao.observeByTrip(tripId).map { list -> list.map { it.toDomain() } }

  fun observeById(restaurantId: String): Flow<Restaurant?> =
    restaurantDao.observeById(restaurantId).map { it?.toDomain() }

  suspend fun addRestaurant(
    tripId: String,
    name: String,
    address: String?,
    dayId: String?,
    notes: String?,
    category: PlaceCategory = PlaceCategory.OTRO,
    latitude: Double? = null,
    longitude: Double? = null,
    placeId: String? = null,
    phone: String? = null,
    website: String? = null,
    googleMapsUri: String? = null,
    rating: Double? = null,
  ) {
    restaurantDao.insert(
      Restaurant(
        id = UUID.randomUUID().toString(),
        tripId = tripId,
        dayId = dayId,
        name = name.trim(),
        address = address?.trim(),
        status = RestaurantStatus.WITHOUT_RESERVATION,
        notes = notes?.trim(),
        category = category,
        latitude = latitude,
        longitude = longitude,
        placeId = placeId,
        phone = phone,
        website = website,
        googleMapsUri = googleMapsUri,
        rating = rating,
      ).toEntity(),
    )
  }

  suspend fun updateStatus(restaurantId: String, status: RestaurantStatus) {
    restaurantDao.updateStatus(restaurantId, status)
  }

  suspend fun deleteRestaurant(restaurantId: String) {
    restaurantDao.deleteById(restaurantId)
  }
}
