package com.myowntrip.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myowntrip.app.data.local.dao.DayDao
import com.myowntrip.app.data.local.dao.ExpenseDao
import com.myowntrip.app.data.local.dao.ItineraryBlockDao
import com.myowntrip.app.data.local.dao.JournalNoteDao
import com.myowntrip.app.data.local.dao.RestaurantDao
import com.myowntrip.app.data.local.dao.TripDao
import com.myowntrip.app.data.local.dao.WalletEntryDao
import com.myowntrip.app.data.local.entity.DayEntity
import com.myowntrip.app.data.local.entity.ExpenseEntity
import com.myowntrip.app.data.local.entity.ItineraryBlockEntity
import com.myowntrip.app.data.local.entity.JournalNoteEntity
import com.myowntrip.app.data.local.entity.RestaurantEntity
import com.myowntrip.app.data.local.entity.TripEntity
import com.myowntrip.app.data.local.entity.WalletEntryEntity

@Database(
  entities = [
    TripEntity::class,
    DayEntity::class,
    WalletEntryEntity::class,
    ExpenseEntity::class,
    JournalNoteEntity::class,
    ItineraryBlockEntity::class,
    RestaurantEntity::class,
  ],
  version = 4,
  exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun tripDao(): TripDao
  abstract fun dayDao(): DayDao
  abstract fun walletEntryDao(): WalletEntryDao
  abstract fun expenseDao(): ExpenseDao
  abstract fun journalNoteDao(): JournalNoteDao
  abstract fun itineraryBlockDao(): ItineraryBlockDao
  abstract fun restaurantDao(): RestaurantDao
}
