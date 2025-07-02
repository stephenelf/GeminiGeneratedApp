package com.stephenelf.gemini2.presentation.gym_list

import com.stephenelf.gemini2.domain.model.Gym

data class GymListState(
    val isLoading: Boolean = false,
    val gyms: List<Gym> = emptyList(),
    val error: String = ""
)