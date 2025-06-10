package com.stephenelf.geminigeneratedapp.data.repository

import android.util.Log
import com.stephenelf.geminigeneratedapp.data.remote.GymsApiService
import com.stephenelf.geminigeneratedapp.data.remote.dto.GymDto
import com.stephenelf.geminigeneratedapp.domain.model.Gym
import com.stephenelf.geminigeneratedapp.domain.repository.GymsRepository
import javax.inject.Inject

/**
 * Implementation of the GymsRepository. It fetches data from the remote
 * API service and maps it from DTOs to domain models.
 */
class GymsRepositoryImpl @Inject constructor(
    private val apiService: GymsApiService
) : GymsRepository {

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

    // Mapper function to convert a DTO to a domain model.
    private fun GymDto.toDomain(): Gym {
        return Gym(
            name = facility_title ?: "Unknown Facility",
            type = community_center ?: "N/A",
            location = location ?: "No Location",
            status = status ?: "No Status",
            phone = phone ?: "No Phone",
            address = address11 ?: "No Address"
        )
    }
}