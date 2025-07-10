package com.stephenelf.hexagonal.application.usecase

import com.stephenelf.hexagonal.domain.model.Gym
import com.stephenelf.hexagonal.domain.repository.GymRepository
import javax.inject.Inject

/**
 * This use case encapsulates the business logic for retrieving a list of gyms.
 * It depends on the GymRepository abstraction from the domain layer.
 */
class GetGymsUseCase @Inject constructor(
    private val gymRepository: GymRepository
) {
    suspend operator fun invoke(): Result<List<Gym>> {
        return gymRepository.getGyms()
    }
}
