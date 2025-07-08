package com.stephenelf.gemini2.data.remote

import com.stephenelf.gemini2.data.remote.dto.OpenGymResponse
import retrofit2.http.GET

interface GymApiService {
    @GET("catalog/datasets/open-gym/records?limit=20")
    suspend fun getGyms(): OpenGymResponse
}