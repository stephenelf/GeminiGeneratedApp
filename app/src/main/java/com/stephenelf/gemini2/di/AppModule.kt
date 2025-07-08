package com.stephenelf.gemini2.di

import com.stephenelf.gemini2.data.remote.GymApiService
import com.stephenelf.gemini2.data.repository.GymRepositoryImpl
import com.stephenelf.gemini2.domain.repository.GymRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGymApiService(): GymApiService {
        return Retrofit.Builder()
            .baseUrl("https://data.townofcary.org/api/explore/v2.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GymApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGymRepository(api: GymApiService): GymRepository {
        return GymRepositoryImpl(api)
    }
}
