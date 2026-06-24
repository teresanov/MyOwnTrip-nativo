package com.myowntrip.app.di

import com.myowntrip.app.data.demo.DebugPastTripsSeeder
import com.myowntrip.app.data.demo.PastTripsDemoLoader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DemoSeedModule {
  @Binds
  @Singleton
  abstract fun bindPastTripsDemoLoader(impl: DebugPastTripsSeeder): PastTripsDemoLoader
}
