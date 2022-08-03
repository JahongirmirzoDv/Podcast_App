package com.mobiledv.podcastapp.data.network.service

import com.mobiledv.podcastapp.data.network.constant.ListenNotesAPI
import com.mobiledv.podcastapp.data.network.model.PodcastSearchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PodcastService {
    @GET(ListenNotesAPI.SEARCH)
    suspend fun searchPodcasts(
        @Query("q") query: String,
        @Query("type") type: String,
    ): PodcastSearchDto
}