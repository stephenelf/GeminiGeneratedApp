package com.stephenelf.hexagonal.data.remote

import com.stephenelf.hexagonal.data.remote.dto.GymResponse
import retrofit2.http.GET

/**
 * Retrofit interface for the Cary Open Gym API.
 */
interface GymApiService {
    @GET("catalog/datasets/open-gym/records?limit=20")
    suspend fun getGyms(): GymResponse
}