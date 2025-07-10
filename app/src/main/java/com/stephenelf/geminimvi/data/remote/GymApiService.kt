package com.stephenelf.geminimvi.data.remote

import com.stephenelf.geminimvi.data.GymsResponse
import retrofit2.http.GET

interface GymApiService {
    @GET("catalog/datasets/open-gym/records?limit=20")
    suspend fun getGyms(): GymsResponse
}