package com.stephenelf.hexagonal.infrastructure.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.stephenelf.hexagonal.application.usecase.GetGymsUseCase
import com.stephenelf.hexagonal.data.remote.GymApiService
import com.stephenelf.hexagonal.data.repository.GymRepositoryImpl
import com.stephenelf.hexagonal.domain.repository.GymRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("[https://data.townofcary.org/api/explore/v2.1/](https://data.townofcary.org/api/explore/v2.1/)")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): GymApiService {
        return retrofit.create(GymApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGymRepository(apiService: GymApiService): GymRepository {
        return GymRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetGymsUseCase(repository: GymRepository): GetGymsUseCase {
        return GetGymsUseCase(repository)
    }
}
