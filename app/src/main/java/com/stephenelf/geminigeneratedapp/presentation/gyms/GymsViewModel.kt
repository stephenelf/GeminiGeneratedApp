package com.stephenelf.geminigeneratedapp.presentation.gyms

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stephenelf.geminigeneratedapp.domain.model.Gym
import com.stephenelf.geminigeneratedapp.domain.use_case.GetGymsUseCase
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

    val gymsList = mutableStateListOf<Gym>()

    init {
        fetchGyms()
    }

    private fun fetchGyms() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getGymsUseCase()
                .onSuccess { gyms ->
                    gymsList.addAll(gyms)
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "An unknown error occurred."
                        )
                    }
                }
        }
    }

    fun swipeCard() {
        if (gymsList.isNotEmpty()) {
            gymsList.removeFirst()
        }
    }
}