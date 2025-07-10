package com.stephenelf.hexagonal.presentation.viewmodel

import com.stephenelf.hexagonal.domain.model.Gym

data class GymsUiState(
    val isLoading: Boolean = false,
    val gyms: List<Gym> = emptyList(),
    val error: String? = null
)