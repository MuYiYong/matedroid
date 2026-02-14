package com.matedroid.di

import android.content.Context
import com.matedroid.BuildConfig
import com.matedroid.data.local.SettingsDataStore
import com.matedroid.data.push.AospPushService
import com.matedroid.data.push.GmsPushService
import com.matedroid.data.push.HmsPushService
import com.matedroid.domain.model.MobileServicesStack
import com.matedroid.domain.model.MobileServicesProfile
import com.matedroid.domain.service.PushService
import com.matedroid.notification.ChargingNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): SettingsDataStore {
        return SettingsDataStore(context)
    }

    @Provides
    @Singleton
    fun provideChargingNotificationManager(
        @ApplicationContext context: Context
    ): ChargingNotificationManager {
        return ChargingNotificationManager(context)
    }

    @Provides
    @Singleton
    fun provideMobileServicesProfile(): MobileServicesProfile {
        return MobileServicesProfile.from(
            channelRaw = BuildConfig.DISTRIBUTION_CHANNEL,
            stackRaw = BuildConfig.MOBILE_SERVICES_STACK
        )
    }

    @Provides
    @Singleton
    fun providePushService(
        @ApplicationContext context: Context,
        profile: MobileServicesProfile
    ): PushService {
        return when (profile.stack) {
            MobileServicesStack.GMS -> GmsPushService(context)
            MobileServicesStack.HMS -> HmsPushService(context)
            MobileServicesStack.AOSP -> AospPushService(context)
        }
    }
}
