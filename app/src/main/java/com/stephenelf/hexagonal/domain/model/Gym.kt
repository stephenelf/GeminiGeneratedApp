package com.stephenelf.hexagonal.domain.model
/**
 * Represents the core Gym entity in our application.
 * This is a clean data class, independent of any data source implementation.
 */
data class Gym(
    val id: String,
    val name: String,
    val address: String,
    val facilityType: String,
    val phone: String?,
    val lat: Double,
    val lon: Double
)
