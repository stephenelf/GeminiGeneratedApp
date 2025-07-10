package com.stephenelf.hexagonal.data.remote.dto

import com.squareup.moshi.Json
import com.stephenelf.hexagonal.domain.model.Gym

data class GymRecord(
    @Json(name = "name_of_facility") val name: String,
    @Json(name = "physical_address") val address: String,
    @Json(name = "facility_type") val facilityType: String,
    @Json(name = "phone_number") val phone: String?,
    @Json(name = "geopoint") val geoPoint: GeoPoint,
    @Json(name = "recordid") val recordId: String
)

/**
 * A mapper function to convert the network DTO to our clean domain model.
 * This is a crucial part of the anti-corruption layer, protecting our domain.
 */
fun GymRecord.toDomain(): Gym {
    return Gym(
        id = this.recordId,
        name = this.name,
        address = this.address,
        facilityType = this.facilityType,
        phone = this.phone,
        lat = this.geoPoint.lat,
        lon = this.geoPoint.lon
    )
}