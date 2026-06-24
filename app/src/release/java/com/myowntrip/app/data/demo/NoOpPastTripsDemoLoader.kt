package com.myowntrip.app.data.demo

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoOpPastTripsDemoLoader @Inject constructor() : PastTripsDemoLoader {
  override suspend fun seedIfAbsent(): PastTripsDemoResult =
    PastTripsDemoResult(tripsCreated = 0, message = "")

  override suspend fun seed(force: Boolean): PastTripsDemoResult =
    PastTripsDemoResult(tripsCreated = 0, message = "")
}
