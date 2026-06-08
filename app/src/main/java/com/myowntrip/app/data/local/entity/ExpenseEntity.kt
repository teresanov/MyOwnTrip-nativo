package com.myowntrip.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myowntrip.app.domain.model.ExpenseCategory

@Entity(
  tableName = "expenses",
  foreignKeys = [
    ForeignKey(
      entity = TripEntity::class,
      parentColumns = ["id"],
      childColumns = ["tripId"],
      onDelete = ForeignKey.CASCADE,
    ),
    ForeignKey(
      entity = DayEntity::class,
      parentColumns = ["id"],
      childColumns = ["dayId"],
      onDelete = ForeignKey.SET_NULL,
    ),
  ],
  indices = [Index("tripId"), Index("dayId")],
)
data class ExpenseEntity(
  @PrimaryKey val id: String,
  val tripId: String,
  val dayId: String?,
  val concept: String,
  val amount: Double,
  val currency: String,
  val category: ExpenseCategory,
  val receiptUri: String?,
)
