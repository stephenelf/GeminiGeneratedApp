package com.stephenelf.hexagonal.data.remote.dto
import com.squareup.moshi.Json

/**
 * Data Transfer Objects (DTOs) that map directly to the JSON structure of the API response.
 * These are kept separate from the domain models to isolate the application from
 * changes in the API contract.
 */
data class GymResponse(
    val results: List<GymRecord>
)