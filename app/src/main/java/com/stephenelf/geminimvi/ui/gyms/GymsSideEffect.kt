package com.stephenelf.geminimvi.ui.gyms
// Side Effect - For one-time events like showing a toast
sealed class GymsSideEffect {
    data class ShowToast(val message: String) : GymsSideEffect()
}