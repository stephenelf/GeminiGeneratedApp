package com.stephenelf.gemini2.data.repository

import com.stephenelf.gemini2.data.remote.GymApiService
import com.stephenelf.gemini2.data.remote.dto.GymDto

class GymRepositoryImpl @Inject constructor(
    private val api: GymApiService
) : GymRepository {
    override suspend fun getGyms(): List<GymDto> {
        return api.getGyms().results
    }
}