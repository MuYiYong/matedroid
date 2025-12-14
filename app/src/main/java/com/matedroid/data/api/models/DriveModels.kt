package com.matedroid.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DrivesResponse(
    @Json(name = "data") val data: List<DriveData>? = null
)

@JsonClass(generateAdapter = true)
data class DriveData(
    @Json(name = "id") val id: Int,
    @Json(name = "start_date") val startDate: String? = null,
    @Json(name = "end_date") val endDate: String? = null,
    @Json(name = "start_address") val startAddress: String? = null,
    @Json(name = "end_address") val endAddress: String? = null,
    @Json(name = "distance") val distance: Double? = null,
    @Json(name = "duration_min") val durationMin: Int? = null,
    @Json(name = "speed_max") val speedMax: Int? = null,
    @Json(name = "power_max") val powerMax: Int? = null,
    @Json(name = "power_min") val powerMin: Int? = null,
    @Json(name = "start_battery_level") val startBatteryLevel: Int? = null,
    @Json(name = "end_battery_level") val endBatteryLevel: Int? = null,
    @Json(name = "start_ideal_range_km") val startIdealRangeKm: Double? = null,
    @Json(name = "end_ideal_range_km") val endIdealRangeKm: Double? = null,
    @Json(name = "start_rated_range_km") val startRatedRangeKm: Double? = null,
    @Json(name = "end_rated_range_km") val endRatedRangeKm: Double? = null,
    @Json(name = "outside_temp_avg") val outsideTempAvg: Double? = null,
    @Json(name = "inside_temp_avg") val insideTempAvg: Double? = null,
    @Json(name = "start_latitude") val startLatitude: Double? = null,
    @Json(name = "start_longitude") val startLongitude: Double? = null,
    @Json(name = "end_latitude") val endLatitude: Double? = null,
    @Json(name = "end_longitude") val endLongitude: Double? = null
) {
    val efficiencyWhKm: Double?
        get() {
            val dist = distance ?: return null
            val startRange = startRatedRangeKm ?: return null
            val endRange = endRatedRangeKm ?: return null
            if (dist <= 0) return null
            val rangeUsed = startRange - endRange
            // Assuming ~250 Wh per km of rated range (approximate)
            return (rangeUsed * 250) / dist
        }
}

@JsonClass(generateAdapter = true)
data class DriveDetailResponse(
    @Json(name = "data") val data: DriveDetail? = null
)

@JsonClass(generateAdapter = true)
data class DriveDetail(
    @Json(name = "id") val id: Int,
    @Json(name = "start_date") val startDate: String? = null,
    @Json(name = "end_date") val endDate: String? = null,
    @Json(name = "start_address") val startAddress: String? = null,
    @Json(name = "end_address") val endAddress: String? = null,
    @Json(name = "distance") val distance: Double? = null,
    @Json(name = "duration_min") val durationMin: Int? = null,
    @Json(name = "speed_max") val speedMax: Int? = null,
    @Json(name = "power_max") val powerMax: Int? = null,
    @Json(name = "power_min") val powerMin: Int? = null,
    @Json(name = "start_battery_level") val startBatteryLevel: Int? = null,
    @Json(name = "end_battery_level") val endBatteryLevel: Int? = null,
    @Json(name = "start_ideal_range_km") val startIdealRangeKm: Double? = null,
    @Json(name = "end_ideal_range_km") val endIdealRangeKm: Double? = null,
    @Json(name = "start_rated_range_km") val startRatedRangeKm: Double? = null,
    @Json(name = "end_rated_range_km") val endRatedRangeKm: Double? = null,
    @Json(name = "outside_temp_avg") val outsideTempAvg: Double? = null,
    @Json(name = "inside_temp_avg") val insideTempAvg: Double? = null,
    @Json(name = "start_latitude") val startLatitude: Double? = null,
    @Json(name = "start_longitude") val startLongitude: Double? = null,
    @Json(name = "end_latitude") val endLatitude: Double? = null,
    @Json(name = "end_longitude") val endLongitude: Double? = null,
    @Json(name = "positions") val positions: List<DrivePosition>? = null
)

@JsonClass(generateAdapter = true)
data class DrivePosition(
    @Json(name = "date") val date: String? = null,
    @Json(name = "latitude") val latitude: Double? = null,
    @Json(name = "longitude") val longitude: Double? = null,
    @Json(name = "speed") val speed: Int? = null,
    @Json(name = "power") val power: Int? = null,
    @Json(name = "battery_level") val batteryLevel: Int? = null,
    @Json(name = "elevation") val elevation: Int? = null
)
