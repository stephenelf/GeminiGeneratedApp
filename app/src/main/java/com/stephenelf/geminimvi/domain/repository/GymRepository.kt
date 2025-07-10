package com.stephenelf.geminimvi.domain.repository

import com.stephenelf.geminimvi.data.Gym


interface GymRepository {
    suspend fun getGyms(): Result<List<Gym>>
}