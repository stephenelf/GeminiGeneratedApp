package com.stephenelf.geminimvi.data.repository


import com.stephenelf.geminimvi.data.Gym
import com.stephenelf.geminimvi.data.remote.GymApiService
import com.stephenelf.geminimvi.domain.repository.GymRepository
import javax.inject.Inject

class GymRepositoryImpl @Inject constructor(
    private val apiService: GymApiService
) : GymRepository {
    override suspend fun getGyms(): Result<List<Gym>> {
        return try {
            val response = apiService.getGyms()
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}