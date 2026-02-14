package com.matedroid.data.push

import android.content.Context
import android.util.Log
import com.matedroid.domain.service.PushService

class GmsPushService(
    private val context: Context
) : PushService {
    override fun initialize() {
        Log.i("GmsPushService", "GMS push scaffold initialized")
    }
}
