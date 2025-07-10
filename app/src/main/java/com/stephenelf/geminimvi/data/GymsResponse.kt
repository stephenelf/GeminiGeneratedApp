package com.stephenelf.geminimvi.data
import com.google.gson.annotations.SerializedName

data class GymsResponse(
    @SerializedName("results") val results: List<Gym>
)