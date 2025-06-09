package com.stephenelf.geminigeneratedapp.data.remote.dto

import retrofit2.http.GET

/**
 * Retrofit API interface for fetching gym data.
 */
interface GymsApiService {
    @GET("catalog/datasets/open-gym/records?limit=20")
    suspend fun getGyms(): GymsResponse
}