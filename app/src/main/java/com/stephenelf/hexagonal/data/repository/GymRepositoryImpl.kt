package com.stephenelf.hexagonal.data.repository

import com.stephenelf.hexagonal.data.remote.GymApiService
import com.stephenelf.hexagonal.data.remote.dto.toDomain
import com.stephenelf.hexagonal.domain.model.Gym
import com.stephenelf.hexagonal.domain.repository.GymRepository
import javax.inject.Inject

/**
 * Concrete implementation of the GymRepository.
 * It uses the Retrofit service to fetch data from the network and maps the DTOs
 * to the domain models.
 */
class GymRepositoryImpl @Inject constructor(
    private val apiService: GymApiService
) : GymRepository {
    override suspend fun getGyms(): Result<List<Gym>> {
        return try {
            val response = apiService.getGyms()
            val gyms = response.results.map { it.toDomain() }
            Result.success(gyms)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}