package com.stephenelf.hexagonal.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stephenelf.hexagonal.application.usecase.GetGymsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GymsViewModel @Inject constructor(
    private val getGymsUseCase: GetGymsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GymsUiState())
    val uiState = _uiState.asStateFlow()

    // We use a separate state for the swiped cards to avoid recomposing the whole list
    val swipedCardIds = mutableStateOf(setOf<String>())

    init {
        fetchGyms()
    }

    fun fetchGyms() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getGymsUseCase()
                .onSuccess { gyms ->
                    _uiState.update {
                        it.copy(isLoading = false, gyms = gyms.reversed()) // Reversed to show top of stack
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message ?: "An unknown error occurred")
                    }
                }
        }
    }

    fun onCardSwiped(gymId: String) {
        swipedCardIds.value = swipedCardIds.value + gymId
    }
}