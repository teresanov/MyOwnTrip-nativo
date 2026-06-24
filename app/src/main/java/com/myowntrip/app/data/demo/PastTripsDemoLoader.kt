package com.myowntrip.app.data.demo

data class PastTripsDemoResult(
  val tripsCreated: Int,
  val message: String,
)

interface PastTripsDemoLoader {
  suspend fun seedIfAbsent(): PastTripsDemoResult

  suspend fun seed(force: Boolean = false): PastTripsDemoResult
}
