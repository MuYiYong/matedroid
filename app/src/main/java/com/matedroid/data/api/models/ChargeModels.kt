package com.matedroid.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChargesResponse(
    @Json(name = "data") val data: List<ChargeData>? = null
)

@JsonClass(generateAdapter = true)
data class ChargeData(
    @Json(name = "id") val id: Int,
    @Json(name = "start_date") val startDate: String? = null,
    @Json(name = "end_date") val endDate: String? = null,
    @Json(name = "address") val address: String? = null,
    @Json(name = "charge_energy_added") val chargeEnergyAdded: Double? = null,
    @Json(name = "charge_energy_used") val chargeEnergyUsed: Double? = null,
    @Json(name = "start_battery_level") val startBatteryLevel: Int? = null,
    @Json(name = "end_battery_level") val endBatteryLevel: Int? = null,
    @Json(name = "start_ideal_range_km") val startIdealRangeKm: Double? = null,
    @Json(name = "end_ideal_range_km") val endIdealRangeKm: Double? = null,
    @Json(name = "start_rated_range_km") val startRatedRangeKm: Double? = null,
    @Json(name = "end_rated_range_km") val endRatedRangeKm: Double? = null,
    @Json(name = "duration_min") val durationMin: Int? = null,
    @Json(name = "outside_temp_avg") val outsideTempAvg: Double? = null,
    @Json(name = "cost") val cost: Double? = null,
    @Json(name = "latitude") val latitude: Double? = null,
    @Json(name = "longitude") val longitude: Double? = null
)

@JsonClass(generateAdapter = true)
data class ChargeDetailResponse(
    @Json(name = "data") val data: ChargeDetail? = null
)

@JsonClass(generateAdapter = true)
data class ChargeDetail(
    @Json(name = "id") val id: Int,
    @Json(name = "start_date") val startDate: String? = null,
    @Json(name = "end_date") val endDate: String? = null,
    @Json(name = "address") val address: String? = null,
    @Json(name = "charge_energy_added") val chargeEnergyAdded: Double? = null,
    @Json(name = "charge_energy_used") val chargeEnergyUsed: Double? = null,
    @Json(name = "start_battery_level") val startBatteryLevel: Int? = null,
    @Json(name = "end_battery_level") val endBatteryLevel: Int? = null,
    @Json(name = "start_ideal_range_km") val startIdealRangeKm: Double? = null,
    @Json(name = "end_ideal_range_km") val endIdealRangeKm: Double? = null,
    @Json(name = "start_rated_range_km") val startRatedRangeKm: Double? = null,
    @Json(name = "end_rated_range_km") val endRatedRangeKm: Double? = null,
    @Json(name = "duration_min") val durationMin: Int? = null,
    @Json(name = "outside_temp_avg") val outsideTempAvg: Double? = null,
    @Json(name = "cost") val cost: Double? = null,
    @Json(name = "latitude") val latitude: Double? = null,
    @Json(name = "longitude") val longitude: Double? = null,
    @Json(name = "charging_process") val chargingProcess: List<ChargingProcessPoint>? = null
)

@JsonClass(generateAdapter = true)
data class ChargingProcessPoint(
    @Json(name = "date") val date: String? = null,
    @Json(name = "battery_level") val batteryLevel: Int? = null,
    @Json(name = "charge_energy_added") val chargeEnergyAdded: Double? = null,
    @Json(name = "charger_power") val chargerPower: Int? = null
)
