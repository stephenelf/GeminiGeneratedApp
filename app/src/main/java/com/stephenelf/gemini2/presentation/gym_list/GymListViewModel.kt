package com.stephenelf.gemini2.presentation.gym_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stephenelf.gemini2.domain.model.Gym
import com.stephenelf.gemini2.domain.use_case.GetGymsUseCase

@HiltViewModel
class GymListViewModel @Inject constructor(
    private val getGymsUseCase: GetGymsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(GymListState())
    val state: State<GymListState> = _state

    init {
        getGyms()
    }

    private fun getGyms() {
        getGymsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = GymListState(gyms = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = GymListState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _state.value = GymListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun removeGym(gym: Gym) {
        val currentList = _state.value.gyms.toMutableList()
        currentList.remove(gym)
        _state.value = _state.value.copy(gyms = currentList)
    }
}