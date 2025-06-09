package com.stephenelf.geminigeneratedapp.data.remote.dto

// Represents a single gym object as received from the API.
data class GymDto(
    val facility_name: String?,
    val facility_type: String?,
    val location_name: String?,
    val status: String?,
    val phone: String?,
    val address: String?
)
