package com.myowntrip.app.di

import android.content.Context
import androidx.room.Room
import com.myowntrip.app.data.local.AppDatabase
import com.myowntrip.app.data.local.MIGRATION_1_2
import com.myowntrip.app.data.local.MIGRATION_2_3
import com.myowntrip.app.data.local.MIGRATION_3_4
import com.myowntrip.app.data.local.MIGRATION_4_5
import com.myowntrip.app.data.local.dao.DayDao
import com.myowntrip.app.data.local.dao.ExpenseDao
import com.myowntrip.app.data.local.dao.ItineraryBlockDao
import com.myowntrip.app.data.local.dao.JournalNoteDao
import com.myowntrip.app.data.local.dao.RestaurantDao
import com.myowntrip.app.data.local.dao.TripDao
import com.myowntrip.app.data.local.dao.WalletEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, "myowntrip.db")
      .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
      .build()

  @Provides fun provideTripDao(db: AppDatabase): TripDao = db.tripDao()
  @Provides fun provideDayDao(db: AppDatabase): DayDao = db.dayDao()
  @Provides fun provideWalletDao(db: AppDatabase): WalletEntryDao = db.walletEntryDao()
  @Provides fun provideExpenseDao(db: AppDatabase): ExpenseDao = db.expenseDao()
  @Provides fun provideJournalDao(db: AppDatabase): JournalNoteDao = db.journalNoteDao()
  @Provides fun provideItineraryBlockDao(db: AppDatabase): ItineraryBlockDao = db.itineraryBlockDao()
  @Provides fun provideRestaurantDao(db: AppDatabase): RestaurantDao = db.restaurantDao()
}
