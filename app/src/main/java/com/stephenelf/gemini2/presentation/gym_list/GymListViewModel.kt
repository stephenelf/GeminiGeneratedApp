package com.stephenelf.gemini2.presentation.gym_list

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stephenelf.gemini2.common.Resource
import com.stephenelf.gemini2.domain.model.Gym
import com.stephenelf.gemini2.domain.use_case.GetGymsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class GymListViewModel @Inject constructor(
    private val getGymsUseCase: GetGymsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GymListState())
    val state =_state.asStateFlow()

    init {
        getGyms()
    }

    private fun getGyms() {
        viewModelScope.launch{
            getGymsUseCase().collect{ result ->
                Log.d("KK","result="+result)
                when (result) {
                    is Resource.Success -> {
                        _state.value = GymListState(gyms = result.data ?: emptyList(), isLoading = false)
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
            }
        }
    }

    fun removeGym(gym: Gym) {
        val currentList = _state.value.gyms.toMutableList()
        currentList.remove(gym)
        _state.value = _state.value.copy(gyms = currentList)
    }
}