package com.myowntrip.app.data.cover

import com.myowntrip.app.data.local.dao.TripDao
import com.myowntrip.app.domain.cover.DestinationCoverRepository
import com.myowntrip.app.domain.model.Trip
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DestinationCoverRepositoryImpl @Inject constructor(
  private val remote: WikimediaDestinationCoverRemoteDataSource,
  private val fileStore: DestinationCoverFileStore,
  private val tripDao: TripDao,
) : DestinationCoverRepository {

  override suspend fun lookupPreviewImage(destination: String): String? {
    val trimmed = destination.trim()
    if (trimmed.isEmpty()) return null
    val cacheKey = DestinationCoverNormalizer.cacheKey(trimmed)
    fileStore.existingPath(cacheKey)?.let { return it }
    return remote.findThumbnailUrl(trimmed)
  }

  override suspend fun attachCoverToTrip(tripId: String, destination: String) {
    val trimmed = destination.trim()
    if (trimmed.isEmpty()) return
    val localPath = resolveLocalPath(trimmed) ?: return
    tripDao.updateCoverPhoto(tripId, localPath)
  }

  override suspend fun ensureCoversForTrips(trips: List<Trip>) {
    trips
      .filter { trip -> trip.coverPhoto.isNullOrBlank() || !File(trip.coverPhoto).isFile }
      .forEach { trip -> attachCoverToTrip(trip.id, trip.destination) }
  }

  private suspend fun resolveLocalPath(destination: String): String? {
    val cacheKey = DestinationCoverNormalizer.cacheKey(destination)
    fileStore.existingPath(cacheKey)?.let { return it }
    val remoteUrl = remote.findThumbnailUrl(destination) ?: return null
    return fileStore.download(cacheKey, remoteUrl)
  }
}
