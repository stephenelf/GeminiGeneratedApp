package com.stephenelf.geminimvi.domain.use_case

import com.stephenelf.geminimvi.data.Gym
import com.stephenelf.geminimvi.domain.repository.GymRepository
import javax.inject.Inject

class GetGymsUseCase @Inject constructor(
    private val repository: GymRepository
) {
    suspend operator fun invoke(): Result<List<Gym>> {
        return repository.getGyms()
    }
}