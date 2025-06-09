package com.stephenelf.geminigeneratedapp.domain.repository

import com.stephenelf.geminigeneratedapp.domain.model.Gym

/**
 * An interface defining the contract for data operations.
 * The domain layer depends on this interface, not the concrete implementation.
 * This allows swapping the data source without affecting the business logic.
 */
interface GymsRepository {
    suspend fun getGyms(): Result<List<Gym>>
}