package com.matedroid.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CarsResponse(
    @Json(name = "data") val data: List<CarData>? = null
)

@JsonClass(generateAdapter = true)
data class CarData(
    @Json(name = "car_id") val carId: Int,
    @Json(name = "display_name") val displayName: String? = null,
    @Json(name = "model") val model: String? = null,
    @Json(name = "trim_badging") val trimBadging: String? = null,
    @Json(name = "vin") val vin: String? = null
)

@JsonClass(generateAdapter = true)
data class CarStatusResponse(
    @Json(name = "data") val data: CarStatus? = null
)

@JsonClass(generateAdapter = true)
data class CarStatus(
    @Json(name = "car_id") val carId: Int? = null,
    @Json(name = "display_name") val displayName: String? = null,
    @Json(name = "state") val state: String? = null,
    @Json(name = "healthy") val healthy: Boolean? = null,
    @Json(name = "version") val version: String? = null,
    @Json(name = "update_available") val updateAvailable: Boolean? = null,
    @Json(name = "update_version") val updateVersion: String? = null,

    // Battery
    @Json(name = "battery_level") val batteryLevel: Int? = null,
    @Json(name = "usable_battery_level") val usableBatteryLevel: Int? = null,
    @Json(name = "ideal_battery_range_km") val idealBatteryRangeKm: Double? = null,
    @Json(name = "est_battery_range_km") val estBatteryRangeKm: Double? = null,
    @Json(name = "rated_battery_range_km") val ratedBatteryRangeKm: Double? = null,

    // Charging
    @Json(name = "plugged_in") val pluggedIn: Boolean? = null,
    @Json(name = "charge_energy_added") val chargeEnergyAdded: Double? = null,
    @Json(name = "charge_limit_soc") val chargeLimitSoc: Int? = null,
    @Json(name = "charge_port_door_open") val chargePortDoorOpen: Boolean? = null,
    @Json(name = "charger_actual_current") val chargerActualCurrent: Int? = null,
    @Json(name = "charger_power") val chargerPower: Int? = null,
    @Json(name = "charger_voltage") val chargerVoltage: Int? = null,
    @Json(name = "scheduled_charging_start_time") val scheduledChargingStartTime: String? = null,
    @Json(name = "time_to_full_charge") val timeToFullCharge: Double? = null,

    // Climate
    @Json(name = "is_climate_on") val isClimateOn: Boolean? = null,
    @Json(name = "inside_temp") val insideTemp: Double? = null,
    @Json(name = "outside_temp") val outsideTemp: Double? = null,
    @Json(name = "is_preconditioning") val isPreconditioning: Boolean? = null,

    // Location
    @Json(name = "latitude") val latitude: Double? = null,
    @Json(name = "longitude") val longitude: Double? = null,
    @Json(name = "heading") val heading: Int? = null,
    @Json(name = "geofence") val geofence: String? = null,

    // Driving
    @Json(name = "speed") val speed: Int? = null,
    @Json(name = "power") val power: Int? = null,
    @Json(name = "shift_state") val shiftState: String? = null,

    // Odometer
    @Json(name = "odometer") val odometer: Double? = null,

    // Doors & Security
    @Json(name = "locked") val locked: Boolean? = null,
    @Json(name = "sentry_mode") val sentryMode: Boolean? = null,
    @Json(name = "is_user_present") val isUserPresent: Boolean? = null,

    // Windows
    @Json(name = "windows_open") val windowsOpen: Boolean? = null,

    // Frunk & Trunk
    @Json(name = "frunk_open") val frunkOpen: Boolean? = null,
    @Json(name = "trunk_open") val trunkOpen: Boolean? = null,

    // Timestamps
    @Json(name = "since") val since: String? = null
)
