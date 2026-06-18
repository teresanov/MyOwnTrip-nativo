package com.myowntrip.app.di

import com.myowntrip.app.data.cover.DestinationCoverRepositoryImpl
import com.myowntrip.app.domain.cover.DestinationCoverRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoverModule {
  @Binds
  @Singleton
  abstract fun bindDestinationCoverRepository(
    impl: DestinationCoverRepositoryImpl,
  ): DestinationCoverRepository
}
