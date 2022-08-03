package com.mobiledv.podcastapp.di

import com.mobiledv.podcastapp.BuildConfig
import com.mobiledv.podcastapp.data.network.constant.ListenNotesAPI
import com.mobiledv.podcastapp.data.network.service.PodcastService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun createHttpClient(): OkHttpClient {
        val requestInterceptor = Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("X-ListenAPI-Key", BuildConfig.API_KEY)
                .build()

            return@Interceptor chain.proceed(request)
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)

        return httpClient.build()
    }

    @Provides
    fun createPodcastService(
        client: OkHttpClient
    ): PodcastService {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(ListenNotesAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PodcastService::class.java)
    }


}