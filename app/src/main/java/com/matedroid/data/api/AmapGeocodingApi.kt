package com.matedroid.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class AmapAddressComponent(
    val country: String? = null,
    val province: String? = null,
    val city: Any? = null,
    val district: String? = null,
    val township: String? = null,
    @Json(name = "streetNumber") val streetNumber: AmapStreetNumber? = null,
    val citycode: String? = null,
    val adcode: String? = null
)

@JsonClass(generateAdapter = true)
data class AmapStreetNumber(
    val street: String? = null,
    val number: String? = null
)

@JsonClass(generateAdapter = true)
data class AmapRegeocode(
    @Json(name = "formatted_address") val formattedAddress: String? = null,
    @Json(name = "addressComponent") val addressComponent: AmapAddressComponent? = null
)

@JsonClass(generateAdapter = true)
data class AmapRegeocodeResponse(
    val status: String? = null,
    val info: String? = null,
    val regeocode: AmapRegeocode? = null
)

interface AmapGeocodingApi {
    @GET("v3/geocode/regeo")
    suspend fun reverseGeocode(
        @Query("location") location: String,
        @Query("key") key: String,
        @Query("extensions") extensions: String = "base"
    ): Response<AmapRegeocodeResponse>
}
