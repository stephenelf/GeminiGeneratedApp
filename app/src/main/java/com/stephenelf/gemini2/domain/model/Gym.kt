package com.stephenelf.gemini2.domain.model
@Parcelize
data class Gym(
    val id: String, // We'll generate a unique ID
    val name: String,
    val address: String,
    val activities: String,
    val phone: String,
    val scheduleUrl: String
) : Parcelable