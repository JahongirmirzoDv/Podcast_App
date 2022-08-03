package com.mobiledv.podcastapp.domain.repository

import com.fabirt.podcastapp.domain.model.PodcastSearch
import com.mobiledv.podcastapp.error.Failure
import com.mobiledv.podcastapp.util.Either

interface PodcastRepository {

    suspend fun searchPodcasts(
        query: String,
        type: String,
    ): Either<Failure, PodcastSearch>
}