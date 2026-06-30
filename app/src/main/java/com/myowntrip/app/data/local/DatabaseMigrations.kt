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

val MIGRATION_9_10 = object : Migration(9, 10) {
  override fun migrate(db: SupportSQLiteDatabase) {
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN pieceType TEXT NOT NULL DEFAULT 'TEXT'")
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN sketchUri TEXT")
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN stickerUri TEXT")
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN planBlockId TEXT")
    db.execSQL("CREATE INDEX IF NOT EXISTS index_journal_notes_planBlockId ON journal_notes(planBlockId)")
    db.execSQL(
      """
      UPDATE journal_notes SET pieceType = 'PHOTO'
      WHERE photoUri IS NOT NULL AND audioUri IS NULL AND sketchUri IS NULL AND stickerUri IS NULL
      """.trimIndent(),
    )
    db.execSQL(
      """
      UPDATE journal_notes SET pieceType = 'AUDIO'
      WHERE audioUri IS NOT NULL AND photoUri IS NULL AND sketchUri IS NULL AND stickerUri IS NULL
      """.trimIndent(),
    )
  }
}

val MIGRATION_10_11 = object : Migration(10, 11) {
  override fun migrate(db: SupportSQLiteDatabase) {
    // Hojas del álbum (días = capítulos, varias hojas por día).
    db.execSQL(
      """
      CREATE TABLE IF NOT EXISTS journal_pages (
        id TEXT NOT NULL PRIMARY KEY,
        tripId TEXT NOT NULL,
        dayId TEXT NOT NULL,
        pageIndex INTEGER NOT NULL,
        paperStyle TEXT,
        FOREIGN KEY(dayId) REFERENCES days(id) ON DELETE CASCADE
      )
      """.trimIndent(),
    )
    db.execSQL("CREATE INDEX IF NOT EXISTS index_journal_pages_dayId ON journal_pages(dayId)")
    db.execSQL("CREATE INDEX IF NOT EXISTS index_journal_pages_tripId ON journal_pages(tripId)")

    // Pieza en el lienzo lógico de su hoja (pageId sin FK a nivel DB, solo índice).
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN pageId TEXT")
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN canvasX REAL")
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN canvasY REAL")
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN rotation REAL NOT NULL DEFAULT 0")
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN scale REAL NOT NULL DEFAULT 1")
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN zIndex INTEGER NOT NULL DEFAULT 0")
    db.execSQL("CREATE INDEX IF NOT EXISTS index_journal_notes_pageId ON journal_notes(pageId)")

    // Backfill no destructivo: una hoja por defecto (pageIndex 0) por cada día con notas,
    // y reasignar esas notas a su hoja. canvasX/canvasY quedan NULL → auto-flow al abrir.
    db.execSQL(
      """
      INSERT INTO journal_pages (id, tripId, dayId, pageIndex, paperStyle)
      SELECT 'page-' || d.id, d.tripId, d.id, 0, NULL
      FROM days d
      WHERE EXISTS (SELECT 1 FROM journal_notes n WHERE n.dayId = d.id)
      """.trimIndent(),
    )
    db.execSQL("UPDATE journal_notes SET pageId = 'page-' || dayId WHERE pageId IS NULL")
  }
}

val MIGRATION_11_12 = object : Migration(11, 12) {
  override fun migrate(db: SupportSQLiteDatabase) {
    // Estilo de las piezas de texto: mano (pluma) vs máquina (post-it). Las notas
    // existentes se quedan como "a mano", coherente con el tono del diario hasta ahora.
    db.execSQL("ALTER TABLE journal_notes ADD COLUMN textStyle TEXT NOT NULL DEFAULT 'HAND'")
  }
}

val MIGRATION_12_13 = object : Migration(12, 13) {
  override fun migrate(db: SupportSQLiteDatabase) {
    // "Sitios": el antiguo restaurante se generaliza. Las filas existentes SON
    // restaurantes → category 'RESTAURANTE'. El resto de campos (mapa/contacto) se
    // rellenan al compartir desde Google Maps; en filas a mano quedan NULL.
    db.execSQL("ALTER TABLE restaurants ADD COLUMN category TEXT NOT NULL DEFAULT 'RESTAURANTE'")
    db.execSQL("ALTER TABLE restaurants ADD COLUMN latitude REAL")
    db.execSQL("ALTER TABLE restaurants ADD COLUMN longitude REAL")
    db.execSQL("ALTER TABLE restaurants ADD COLUMN placeId TEXT")
    db.execSQL("ALTER TABLE restaurants ADD COLUMN phone TEXT")
    db.execSQL("ALTER TABLE restaurants ADD COLUMN website TEXT")
    db.execSQL("ALTER TABLE restaurants ADD COLUMN googleMapsUri TEXT")
    db.execSQL("ALTER TABLE restaurants ADD COLUMN rating REAL")
  }
}
