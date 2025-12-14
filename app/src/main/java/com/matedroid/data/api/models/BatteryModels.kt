package com.matedroid.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BatteryHealthResponse(
    @Json(name = "data") val data: BatteryHealth? = null
)

@JsonClass(generateAdapter = true)
data class BatteryHealth(
    @Json(name = "car_id") val carId: Int? = null,
    @Json(name = "battery_capacity") val batteryCapacity: Double? = null,
    @Json(name = "battery_health") val batteryHealth: Double? = null,
    @Json(name = "battery_level") val batteryLevel: Int? = null,
    @Json(name = "battery_range_km") val batteryRangeKm: Double? = null,
    @Json(name = "date") val date: String? = null
)

@JsonClass(generateAdapter = true)
data class UpdatesResponse(
    @Json(name = "data") val data: List<UpdateData>? = null
)

@JsonClass(generateAdapter = true)
data class UpdateData(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "version") val version: String? = null,
    @Json(name = "start_date") val startDate: String? = null,
    @Json(name = "end_date") val endDate: String? = null
)

@JsonClass(generateAdapter = true)
data class PingResponse(
    @Json(name = "ping") val ping: String? = null
)
