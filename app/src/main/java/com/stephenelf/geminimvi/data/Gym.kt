package com.stephenelf.geminimvi.data
import com.google.gson.annotations.SerializedName

data class Gym(
    @SerializedName("gym_name") val name: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("type") val type: String?
)
