package com.myowntrip.app.data.local

import androidx.room.TypeConverter
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.ExpenseCategory
import com.myowntrip.app.domain.model.RestaurantStatus
import java.time.LocalDate
import java.time.LocalTime

class Converters {
  @TypeConverter
  fun fromLocalDate(value: LocalDate?): String? = value?.toString()

  @TypeConverter
  fun toLocalDate(value: String?): LocalDate? = value?.let(LocalDate::parse)

  @TypeConverter
  fun fromLocalTime(value: LocalTime?): String? = value?.toString()

  @TypeConverter
  fun toLocalTime(value: String?): LocalTime? = value?.let(LocalTime::parse)

  @TypeConverter
  fun fromEntryType(value: EntryType): String = value.name

  @TypeConverter
  fun toEntryType(value: String): EntryType = EntryType.valueOf(value)

  @TypeConverter
  fun fromExpenseCategory(value: ExpenseCategory): String = value.name

  @TypeConverter
  fun toExpenseCategory(value: String): ExpenseCategory = ExpenseCategory.valueOf(value)

  @TypeConverter
  fun fromRestaurantStatus(value: RestaurantStatus): String = value.name

  @TypeConverter
  fun toRestaurantStatus(value: String): RestaurantStatus = RestaurantStatus.valueOf(value)
}
