package com.stephenelf.gemini2.data.repository

import com.stephenelf.gemini2.data.remote.GymApiService
import com.stephenelf.gemini2.data.remote.dto.GymDto
import com.stephenelf.gemini2.domain.repository.GymRepository
import javax.inject.Inject

class GymRepositoryImpl @Inject constructor(
    private val api: GymApiService
) : GymRepository {
    override suspend fun getGyms(): List<GymDto> {
        return api.getGyms().results
    }
}