package com.stephenelf.gemini2.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Gym(
    val id: String, // We'll generate a unique ID
    val name: String,
    val address: String,
    val activities: String,
    val phone: String,
    val scheduleUrl: String
): Parcelable