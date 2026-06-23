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

val MIGRATION_5_6 = object : Migration(5, 6) {
  override fun migrate(db: SupportSQLiteDatabase) {
    db.execSQL(
      "ALTER TABLE trips ADD COLUMN offlinePolicy TEXT NOT NULL DEFAULT 'FULL'",
    )
  }
}

val MIGRATION_6_7 = object : Migration(6, 7) {
  override fun migrate(db: SupportSQLiteDatabase) {
    db.execSQL(
      """
      CREATE TABLE IF NOT EXISTS trips_new (
        id TEXT NOT NULL PRIMARY KEY,
        name TEXT NOT NULL,
        destination TEXT NOT NULL,
        startDate TEXT NOT NULL,
        endDate TEXT NOT NULL,
        coverPhoto TEXT,
        createdAt INTEGER NOT NULL
      )
      """.trimIndent(),
    )
    db.execSQL(
      """
      INSERT INTO trips_new (id, name, destination, startDate, endDate, coverPhoto, createdAt)
      SELECT id, name, destination, startDate, endDate, coverPhoto, createdAt FROM trips
      """.trimIndent(),
    )
    db.execSQL("DROP TABLE trips")
    db.execSQL("ALTER TABLE trips_new RENAME TO trips")
  }
}

val MIGRATION_7_8 = object : Migration(7, 8) {
  override fun migrate(db: SupportSQLiteDatabase) {
    db.execSQL("ALTER TABLE trips ADD COLUMN archivedAt INTEGER")
  }
}

val MIGRATION_8_9 = object : Migration(8, 9) {
  override fun migrate(db: SupportSQLiteDatabase) {
    db.execSQL("ALTER TABLE wallet_entries ADD COLUMN archivedAt INTEGER")
  }
}
