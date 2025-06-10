package com.stephenelf.geminigeneratedapp.data.remote.dto

// Represents a single gym object as received from the API.
data class GymDto(
    val facility_title: String?,
    val community_center: String?,
    val location: String?,
    val status: String?,
    val phone: String?,
    val address11: String?
)