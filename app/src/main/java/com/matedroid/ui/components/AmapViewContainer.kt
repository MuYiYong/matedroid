package com.matedroid.ui.components

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView

@Composable
fun AmapViewContainer(
    modifier: Modifier = Modifier,
    updateKey: Any? = Unit,
    onMapReady: (AMap) -> Unit = {},
    onMapUpdate: (AMap) -> Unit
) {
    val context = LocalContext.current
    @Suppress("DEPRECATION")
    val lifecycleOwner = LocalLifecycleOwner.current

    val mapView = remember {
        MapView(context).apply {
            onCreate(Bundle())
        }
    }

    var mapInitialized by remember { mutableStateOf(false) }
    var lastUpdateKey by remember { mutableStateOf<Any?>(null) }

    DisposableEffect(lifecycleOwner, mapView) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                mapView.onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                mapView.onPause()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                mapView.onDestroy()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            mapView.onResume()
        } else {
            mapView.onPause()
        }

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
        update = { view ->
            val map = view.map
            if (!mapInitialized) {
                map.uiSettings.logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_LEFT
                onMapReady(map)
                mapInitialized = true
            }
            if (lastUpdateKey != updateKey) {
                onMapUpdate(map)
                lastUpdateKey = updateKey
            }
        }
    )
}
