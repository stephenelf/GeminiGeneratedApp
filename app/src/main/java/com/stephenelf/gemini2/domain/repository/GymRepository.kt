package com.stephenelf.gemini2.domain.repository

import com.stephenelf.gemini2.data.remote.dto.GymDto

interface GymRepository {
    suspend fun getGyms(): List<GymDto>
}