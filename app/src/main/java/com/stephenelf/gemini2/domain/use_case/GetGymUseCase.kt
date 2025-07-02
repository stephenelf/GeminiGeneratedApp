package com.stephenelf.gemini2.domain.use_case

import android.net.http.HttpException
import com.stephenelf.gemini2.data.remote.dto.GymDto
import com.stephenelf.gemini2.domain.model.Gym
import com.stephenelf.gemini2.domain.repository.GymRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.util.UUID

class GetGymsUseCase @Inject constructor(
    private val repository: GymRepository
) {
    operator fun invoke(): Flow<Resource<List<Gym>>> = flow {
        try {
            emit(Resource.Loading())
            val gymsDto = repository.getGyms()
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
        name = name ?: "N/A",
        address = "${address ?: ""} ${city ?: ""}, ${state ?: ""} ${zip_code ?: ""}".trim(),
        activities = activities ?: "N/A",
        phone = phone ?: "N/A",
        scheduleUrl = schedule_url ?: ""
    )
}
