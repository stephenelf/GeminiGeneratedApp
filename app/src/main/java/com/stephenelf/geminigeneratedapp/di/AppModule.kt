package com.stephenelf.geminigeneratedapp.di

import com.stephenelf.geminigeneratedapp.data.repository.GymsRepositoryImpl
import com.stephenelf.geminigeneratedapp.domain.repository.GymsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java

/**
 * Hilt module for providing application-wide dependencies, like Retrofit and the Repository.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "[https://data.townofcary.org/api/explore/v2.1/](https://data.townofcary.org/api/explore/v2.1/)"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGymsApiService(retrofit: Retrofit): GymsApiService {
        return retrofit.create(GymsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGymsRepository(apiService: GymsApiService): GymsRepository {
        return GymsRepositoryImpl(apiService)
    }
}