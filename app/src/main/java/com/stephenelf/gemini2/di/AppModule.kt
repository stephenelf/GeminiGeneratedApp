package com.stephenelf.gemini2.di

import com.stephenelf.gemini2.data.remote.GymApiService
import com.stephenelf.gemini2.data.repository.GymRepositoryImpl
import com.stephenelf.gemini2.domain.repository.GymRepository

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
