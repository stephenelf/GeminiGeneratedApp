package com.stephenelf.geminimvi.ui.gyms

import com.stephenelf.geminimvi.data.Gym

// Event - Represents user actions
sealed class GymsEvent {
    object LoadGyms : GymsEvent()
    data class SwipeGym(val gym: Gym) : GymsEvent()
}