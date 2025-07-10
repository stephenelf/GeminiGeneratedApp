package com.stephenelf.geminimvi.ui.gyms

import com.stephenelf.geminimvi.data.Gym

// State - Represents the state of the UI
data class GymsState(
    val gyms: List<Gym> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
