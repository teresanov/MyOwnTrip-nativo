package com.myowntrip.app.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
  override fun migrate(db: SupportSQLiteDatabase) {
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN audioUri TEXT")
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN latitude REAL")
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN longitude REAL")
  }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
  override fun migrate(db: SupportSQLiteDatabase) {
    db.execSQL(
      """
      CREATE TABLE IF NOT EXISTS itinerary_blocks (
        id TEXT NOT NULL PRIMARY KEY,
        dayId TEXT NOT NULL,
        title TEXT NOT NULL,
        timeLabel TEXT,
        sortOrder INTEGER NOT NULL,
        FOREIGN KEY(dayId) REFERENCES days(id) ON DELETE CASCADE
      )
      """.trimIndent(),
    )
    db.execSQL("CREATE INDEX IF NOT EXISTS index_itinerary_blocks_dayId ON itinerary_blocks(dayId)")
    db.execSQL(
      """
      CREATE TABLE IF NOT EXISTS restaurants (
        id TEXT NOT NULL PRIMARY KEY,
        tripId TEXT NOT NULL,
        dayId TEXT,
        name TEXT NOT NULL,
        address TEXT,
        status TEXT NOT NULL,
        notes TEXT,
        FOREIGN KEY(tripId) REFERENCES trips(id) ON DELETE CASCADE
      )
      """.trimIndent(),
    )
    db.execSQL("CREATE INDEX IF NOT EXISTS index_restaurants_tripId ON restaurants(tripId)")
  }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
  override fun migrate(db: SupportSQLiteDatabase) {
    db.execSQL("ALTER TABLE wallet_entries ADD COLUMN qrPayload TEXT")
  }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
  override fun migrate(db: SupportSQLiteDatabase) {
    db.execSQL("ALTER TABLE itinerary_blocks ADD COLUMN walletEntryId TEXT")
    db.execSQL(
      "CREATE INDEX IF NOT EXISTS index_itinerary_blocks_walletEntryId ON itinerary_blocks(walletEntryId)",
    )
  }
}
