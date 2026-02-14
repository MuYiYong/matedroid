package com.matedroid.data.push

import android.content.Context
import android.util.Log
import com.matedroid.domain.service.PushService

class AospPushService(
    private val context: Context
) : PushService {
    override fun initialize() {
        Log.i("AospPushService", "AOSP push scaffold initialized (no vendor SDK)")
    }
}
