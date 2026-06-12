package com.myowntrip.app.platform.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

data class GeoPoint(val latitude: Double, val longitude: Double)

@Singleton
class LocationCapture @Inject constructor(
  @ApplicationContext private val context: Context,
) {
  @SuppressLint("MissingPermission")
  suspend fun getCurrentLocation(): GeoPoint? = suspendCancellableCoroutine { cont ->
    val client = LocationServices.getFusedLocationProviderClient(context)
    val token = CancellationTokenSource().token
    client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, token)
      .addOnSuccessListener { location ->
        if (cont.isActive) {
          cont.resume(
            location?.let { GeoPoint(it.latitude, it.longitude) },
          )
        }
      }
      .addOnFailureListener {
        if (cont.isActive) cont.resume(null)
      }
  }
}
