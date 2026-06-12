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
