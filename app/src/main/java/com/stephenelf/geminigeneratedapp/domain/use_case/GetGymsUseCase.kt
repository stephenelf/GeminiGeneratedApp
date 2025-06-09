package com.stephenelf.geminigeneratedapp.domain.use_case

import com.stephenelf.geminigeneratedapp.domain.model.Gym
import com.stephenelf.geminigeneratedapp.domain.repository.GymsRepository
import javax.inject.Inject

/**
 * The use case for getting a list of gyms. This class contains the specific
 * business logic for this feature. It orchestrates the flow of data from
 * the repository and prepares it for the presentation layer.
 */
class GetGymsUseCase @Inject constructor(
    private val repository: GymsRepository
) {
    suspend operator fun invoke(): Result<List<Gym>> {
        // Here you could add more business logic, like filtering, sorting, etc.
        return repository.getGyms()
    }
}