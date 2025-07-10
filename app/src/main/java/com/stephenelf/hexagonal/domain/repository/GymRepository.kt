package com.stephenelf.hexagonal.domain.repository

import com.stephenelf.hexagonal.domain.model.Gym


/**
 * An interface defining the contract for fetching gym data.
 * The domain layer depends on this abstraction, not on a concrete implementation.
 * This allows us to swap out the data source (e.g., for a local database) without
 * changing the core application logic.
 */
interface GymRepository {
    suspend fun getGyms(): Result<List<Gym>>
}