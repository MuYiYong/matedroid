package com.matedroid.di

import com.matedroid.data.api.TeslamateApi
import com.matedroid.data.local.SettingsDataStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(settingsDataStore: SettingsDataStore): Interceptor {
        return Interceptor { chain ->
            val settings = runBlocking { settingsDataStore.settings.first() }
            val request = if (settings.apiToken.isNotBlank()) {
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${settings.apiToken}")
                    .build()
            } else {
                chain.request()
            }
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideTeslamateApiFactory(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): TeslamateApiFactory {
        return TeslamateApiFactory(okHttpClient, moshi)
    }
}

class TeslamateApiFactory(
    private val okHttpClient: OkHttpClient,
    private val moshi: Moshi
) {
    private var currentBaseUrl: String? = null
    private var currentApi: TeslamateApi? = null

    fun create(baseUrl: String): TeslamateApi {
        val normalizedUrl = baseUrl.trimEnd('/') + "/"

        if (currentBaseUrl == normalizedUrl && currentApi != null) {
            return currentApi!!
        }

        currentBaseUrl = normalizedUrl
        currentApi = Retrofit.Builder()
            .baseUrl(normalizedUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TeslamateApi::class.java)

        return currentApi!!
    }
}
