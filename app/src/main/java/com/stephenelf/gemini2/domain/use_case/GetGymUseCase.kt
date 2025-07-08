package com.stephenelf.gemini2.domain.use_case

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.stephenelf.gemini2.common.Resource
import com.stephenelf.gemini2.data.remote.dto.GymDto
import com.stephenelf.gemini2.domain.model.Gym
import com.stephenelf.gemini2.domain.repository.GymRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class GetGymsUseCase @Inject constructor(
    private val repository: GymRepository
) {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    operator fun invoke(): Flow<Resource<List<Gym>>> = flow {
        try {
            emit(Resource.Loading())
            val gymsDto = repository.getGyms()
            Log.d("kk","gyms list="+gymsDto)
            val gyms = gymsDto.map { it.toGym() }
            emit(Resource.Success(gyms))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}

// Mapper function
fun GymDto.toGym(): Gym {
    return Gym(
        id = UUID.randomUUID().toString(), // Create a unique ID for each gym
        name = facility_title ?: "Unknown Facility",
        phone = phone ?: "No Phone",
        address = address11 ?: "No Address",
        activities = "N/A",
        scheduleUrl = ""
    )
}


