package com.myowntrip.app.data.repository

import android.content.Context
import com.myowntrip.app.data.local.AppDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class AppDataRepository @Inject constructor(
  @ApplicationContext private val context: Context,
  private val database: AppDatabase,
) {
  suspend fun clearAllUserData() {
    withContext(Dispatchers.IO) {
      database.runInTransaction {
        database.clearAllTables()
      }
      clearLocalFiles()
    }
  }

  private fun clearLocalFiles() {
    listOf("trips", "journal", "destination-covers").forEach { dirName ->
      context.filesDir.resolve(dirName).deleteRecursively()
    }
  }
}
