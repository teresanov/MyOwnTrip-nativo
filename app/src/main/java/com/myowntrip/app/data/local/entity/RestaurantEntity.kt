package com.myowntrip.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myowntrip.app.domain.model.PlaceCategory
import com.myowntrip.app.domain.model.RestaurantStatus

@Entity(
  tableName = "restaurants",
  foreignKeys = [
    ForeignKey(
      entity = TripEntity::class,
      parentColumns = ["id"],
      childColumns = ["tripId"],
      onDelete = ForeignKey.CASCADE,
    ),
  ],
  indices = [Index("tripId")],
)
data class RestaurantEntity(
  @PrimaryKey val id: String,
  val tripId: String,
  val dayId: String?,
  val name: String,
  val address: String?,
  val status: RestaurantStatus,
  val notes: String?,
  // Las filas existentes son restaurantes: defaultValue 'RESTAURANTE' coincide con la migración.
  @ColumnInfo(defaultValue = "RESTAURANTE") val category: PlaceCategory = PlaceCategory.RESTAURANTE,
  val latitude: Double? = null,
  val longitude: Double? = null,
  val placeId: String? = null,
  val phone: String? = null,
  val website: String? = null,
  val googleMapsUri: String? = null,
  val rating: Double? = null,
)
