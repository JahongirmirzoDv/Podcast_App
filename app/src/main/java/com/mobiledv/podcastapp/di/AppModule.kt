package com.mobiledv.podcastapp.di

import android.content.Context
import android.os.Looper
import com.mobiledv.podcastapp.BuildConfig
import com.mobiledv.podcastapp.data.datastore.PodcastDataStore
import com.mobiledv.podcastapp.data.exoplayer.PodcastMediaSource
import com.mobiledv.podcastapp.data.network.constant.ListenNotesAPI
import com.mobiledv.podcastapp.data.network.service.PodcastService
import com.mobiledv.podcastapp.data.service.MediaPlayerServiceConnection
import com.mobiledv.podcastapp.domain.repository.PodcastRepository
import com.mobiledv.podcastapp.domain.repository.PodcastRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    fun providePodcastDataStore(
        @ApplicationContext context: Context
    ): PodcastDataStore = PodcastDataStore(context)

    @Provides
    fun providePodcastRepository(
        service: PodcastService,
        dataStore: PodcastDataStore
    ): PodcastRepository = PodcastRepositoryImpl(service, dataStore)

    @Provides
    fun provideMediaPlayerServiceConnection(
        @ApplicationContext context: Context,
        mediaSource: PodcastMediaSource
    ): MediaPlayerServiceConnection {
        require(Looper.myLooper() != Looper.getMainLooper()){
            "Main thread"
        }
        return MediaPlayerServiceConnection(context, mediaSource)
    }
}