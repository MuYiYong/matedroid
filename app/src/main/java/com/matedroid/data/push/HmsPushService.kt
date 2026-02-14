package com.matedroid.data.push

import android.content.Context
import android.util.Log
import com.matedroid.BuildConfig
import com.matedroid.domain.service.PushService
import java.util.concurrent.Executors

class HmsPushService(
    private val context: Context
) : PushService {
    override fun initialize() {
        val appId = BuildConfig.HMS_APP_ID.trim()
        if (appId.isBlank()) {
            Log.w("HmsPushService", "Skip HMS token init: HMS_APP_ID is empty")
            return
        }

        if (!isHmsPushAvailable()) {
            Log.w("HmsPushService", "Skip HMS token init: HMS Push SDK not available in this build")
            return
        }

        Executors.newSingleThreadExecutor().execute {
            runCatching {
                val hmsInstanceIdClass = Class.forName("com.huawei.hms.aaid.HmsInstanceId")
                val getInstanceMethod = hmsInstanceIdClass.getMethod("getInstance", Context::class.java)
                val hmsInstance = getInstanceMethod.invoke(null, context)

                val getTokenMethod = hmsInstanceIdClass.getMethod(
                    "getToken",
                    String::class.java,
                    String::class.java
                )

                val token = getTokenMethod.invoke(hmsInstance, appId, "HCM") as? String
                if (token.isNullOrBlank()) {
                    Log.w("HmsPushService", "HMS token request returned empty token")
                } else {
                    Log.i("HmsPushService", "HMS token acquired (length=${token.length})")
                }
            }.onFailure { error ->
                Log.w("HmsPushService", "HMS token init failed: ${error.message}")
            }
        }
    }

    private fun isHmsPushAvailable(): Boolean {
        return runCatching {
            Class.forName("com.huawei.hms.aaid.HmsInstanceId")
        }.isSuccess
    }
}
