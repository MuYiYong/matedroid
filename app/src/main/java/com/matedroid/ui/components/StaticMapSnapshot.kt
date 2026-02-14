package com.matedroid.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

fun buildAmapStaticMarkerUrl(
    gcjLatitude: Double,
    gcjLongitude: Double,
    apiKey: String,
    width: Int,
    height: Int,
    zoom: Int = 14
): String? {
    if (apiKey.isBlank()) return null

    val location = "$gcjLongitude,$gcjLatitude"
    return Uri.parse("https://restapi.amap.com/v3/staticmap")
        .buildUpon()
        .appendQueryParameter("location", location)
        .appendQueryParameter("zoom", zoom.toString())
        .appendQueryParameter("size", "${width}*${height}")
        .appendQueryParameter("markers", "mid,0x2D7FF9,A:$location")
        .appendQueryParameter("key", apiKey)
        .build()
        .toString()
}

fun buildAmapStaticPathUrl(
    gcjPoints: List<Pair<Double, Double>>,
    apiKey: String,
    width: Int,
    height: Int,
    colorHex: String = "0x2D7FF9",
    weight: Int = 6
): String? {
    if (apiKey.isBlank() || gcjPoints.size < 2) return null

    val sampledPoints = sampleRoutePoints(gcjPoints, maxPoints = 120)
    val centerLat = sampledPoints.map { it.first }.average()
    val centerLon = sampledPoints.map { it.second }.average()
    val latSpan = sampledPoints.maxOf { it.first } - sampledPoints.minOf { it.first }
    val lonSpan = sampledPoints.maxOf { it.second } - sampledPoints.minOf { it.second }
    val zoom = estimateZoom(latSpan = latSpan, lonSpan = lonSpan)

    val pathCoords = sampledPoints.joinToString(";") { (lat, lon) -> "$lon,$lat" }
    val pathParam = "$weight,$colorHex,0.95,,:$pathCoords"

    val start = sampledPoints.first()
    val end = sampledPoints.last()
    val markerParam =
        "mid,0x34C759,S:${start.second},${start.first}|mid,0xFF3B30,E:${end.second},${end.first}"

    return Uri.parse("https://restapi.amap.com/v3/staticmap")
        .buildUpon()
        .appendQueryParameter("location", "$centerLon,$centerLat")
        .appendQueryParameter("zoom", zoom.toString())
        .appendQueryParameter("size", "${width}*${height}")
        .appendQueryParameter("paths", pathParam)
        .appendQueryParameter("markers", markerParam)
        .appendQueryParameter("key", apiKey)
        .build()
        .toString()
}

private fun sampleRoutePoints(
    points: List<Pair<Double, Double>>,
    maxPoints: Int
): List<Pair<Double, Double>> {
    if (points.size <= maxPoints) return points

    val step = (points.size - 1).toDouble() / (maxPoints - 1).toDouble()
    return buildList {
        for (index in 0 until maxPoints) {
            val sourceIndex = (index * step).toInt().coerceIn(0, points.lastIndex)
            add(points[sourceIndex])
        }
    }
}

private fun estimateZoom(latSpan: Double, lonSpan: Double): Int {
    val span = maxOf(latSpan, lonSpan)
    return when {
        span < 0.005 -> 16
        span < 0.01 -> 15
        span < 0.02 -> 14
        span < 0.04 -> 13
        span < 0.08 -> 12
        span < 0.16 -> 11
        span < 0.32 -> 10
        span < 0.64 -> 9
        span < 1.28 -> 8
        else -> 7
    }
}

@Composable
fun StaticMapSnapshot(
    modifier: Modifier = Modifier,
    staticMapUrl: String?,
    onClick: () -> Unit = {}
) {
    var staticBitmap by remember(staticMapUrl) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(staticMapUrl) {
        staticBitmap = staticMapUrl?.let { fetchBitmap(it) }
    }

    Box(
        modifier = modifier.clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (staticBitmap != null) {
            Image(
                bitmap = staticBitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Place,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private suspend fun fetchBitmap(url: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        runCatching {
            URL(url).openStream().use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        }.getOrNull()
    }
}
