package com.stephenelf.geminimvi.ui.gyms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stephenelf.geminimvi.domain.use_case.GetGymsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class GymsViewModel @Inject constructor(
    private val getGymsUseCase: GetGymsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GymsState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<GymsSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        handleEvent(GymsEvent.LoadGyms)
    }

    fun handleEvent(event: GymsEvent) {
        when (event) {
            is GymsEvent.LoadGyms -> {
                loadGyms()
            }
            is GymsEvent.SwipeGym -> {
                // Handle swipe event, e.g., remove from list
                _state.update {
                    it.copy(gyms = it.gyms.toMutableList().apply { remove(event.gym) })
                }
            }
        }
    }

    private fun loadGyms() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getGymsUseCase().onSuccess { gyms ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        gyms = gyms
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = error.localizedMessage ?: "An unknown error occurred"
                    )
                }
            }
        }
    }
}
